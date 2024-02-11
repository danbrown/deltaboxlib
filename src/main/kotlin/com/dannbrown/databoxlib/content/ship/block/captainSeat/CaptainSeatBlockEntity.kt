package com.dannbrown.databoxlib.content.ship.block.captainSeat

import com.dannbrown.databoxlib.content.ship.SpaceShipControl
import com.dannbrown.databoxlib.content.ship.extensions.getShipObjectManagingPos
import com.dannbrown.databoxlib.content.ship.extensions.toDoubles
import net.minecraft.commands.arguments.EntityAnchorArgument
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.Half
import org.joml.Vector3d
import org.joml.Vector3dc
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.mod.common.ValkyrienSkiesMod
import org.valkyrienskies.mod.common.entity.ShipMountingEntity

class CaptainSeatBlockEntity(type: BlockEntityType<CaptainSeatBlockEntity>, pos: BlockPos, state: BlockState) : BlockEntity(type, pos, state) {
  private val ship: ServerShip? get() = (level as ServerLevel).getShipObjectManagingPos(this.blockPos)
  private val control: SpaceShipControl? get() = ship?.getAttachment(SpaceShipControl::class.java)
  private val seats = mutableListOf<ShipMountingEntity>()
  private var shouldDisassembleWhenPossible = false

  // Needs to get called server-side
  fun spawnSeat(blockPos: BlockPos, state: BlockState, level: ServerLevel): ShipMountingEntity {
//    val newPos = blockPos.relative(state.getValue(HorizontalDirectionalBlock.FACING))
    val newState = level.getBlockState(blockPos)
    val newShape = newState.getShape(level, blockPos)
    val newBlock = newState.block
    var height = 0.5
    if (!newState.isAir) {
      height = if (
        newBlock is StairBlock &&
        (!newState.hasProperty(StairBlock.HALF) || newState.getValue(StairBlock.HALF) == Half.BOTTOM)
      )
        0.5 // Valid StairBlock
      else
        newShape.max(Direction.Axis.Y)
    }
    val entity = ValkyrienSkiesMod.SHIP_MOUNTING_ENTITY_TYPE.create(level)!!
      .apply {
        val seatEntityPos: Vector3dc = Vector3d(blockPos.x + .5, (blockPos.y - .5) + height, blockPos.z + .5)
        moveTo(seatEntityPos.x(), seatEntityPos.y(), seatEntityPos.z())

        lookAt(
          EntityAnchorArgument.Anchor.EYES,
          state.getValue(BlockStateProperties.HORIZONTAL_FACING).normal.toDoubles()
            .add(position())
        )

        isController = true
      }

    level.addFreshEntityWithPassengers(entity)
    return entity
  }

  fun startRiding(player: Player, force: Boolean, blockPos: BlockPos, state: BlockState, level: ServerLevel): Boolean {
    for (i in seats.size - 1 downTo 0) {
      if (!seats[i].isVehicle) {
        seats[i].kill()
        seats.removeAt(i)
      }
      else if (!seats[i].isAlive) {
        seats.removeAt(i)
      }
    }
    val seat = spawnSeat(blockPos, blockState, level)
    val ride = player.startRiding(seat, force)

    if (ride) {
      control?.seatedPlayer = player
      seats.add(seat)
    }

    return ride
  }

  fun tick() {
//    if(ship?.getAttachment<DataboxShipControl>() === null){
//
//    }
    control?.ship = ship
  }

  override fun setRemoved() {
    if (level?.isClientSide == false) {
      for (i in seats.indices) {
        seats[i].kill()
      }
      seats.clear()
    }

    super.setRemoved()
  }

  fun sit(player: Player, force: Boolean = false): Boolean {
    // If player is already controlling the ship, open the helm menu
    if (!force && player.vehicle?.type == ValkyrienSkiesMod.SHIP_MOUNTING_ENTITY_TYPE && seats.contains(player.vehicle as ShipMountingEntity)) {
      return true
    }
    return startRiding(player, force, blockPos, blockState, level as ServerLevel)
  }
}
