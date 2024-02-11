package com.dannbrown.databoxlib.content.ship.block.captainSeat

import com.dannbrown.databoxlib.content.ship.SpaceShipControl
import com.dannbrown.databoxlib.content.ship.extensions.getShipManagingPos
import com.dannbrown.databoxlib.content.ship.extensions.getShipObjectManagingPos
import com.dannbrown.databoxlib.init.ProjectBlockEntities
import com.simibubi.create.AllShapes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.pathfinder.PathComputationType
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import org.valkyrienskies.core.api.ships.getAttachment
import org.valkyrienskies.mod.common.ValkyrienSkiesMod

class CaptainSeatBlock(props: Properties) : BaseEntityBlock(props) {

  init {
    registerDefaultState(this.stateDefinition.any()
      .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH))
  }

  override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, isMoving: Boolean) {
    super.onPlace(state, level, pos, oldState, isMoving)

    if (level.isClientSide) return
    level as ServerLevel
    val ship = level.getShipObjectManagingPos(pos) ?: level.getShipManagingPos(pos) ?: return
    val shipControl = SpaceShipControl.getOrCreate(ship)
    shipControl.addCaptainSeat(pos)
  }

  override fun destroy(level: LevelAccessor, pos: BlockPos, state: BlockState) {
    super.destroy(level, pos, state)

    if (level.isClientSide) return
    level as ServerLevel
    val ship = level.getShipObjectManagingPos(pos) ?: level.getShipManagingPos(pos) ?: return
    val shipControl = ship.getAttachment<SpaceShipControl>() ?: return
    // unride the player from the seat
    shipControl.let { control ->
      if (control.getCaptainSeats()
          .count() <= 1 && control.seatedPlayer?.vehicle?.type == ValkyrienSkiesMod.SHIP_MOUNTING_ENTITY_TYPE) {
        control.seatedPlayer!!.unRide()
        control.seatedPlayer = null
      }
      control.removeCaptainSeat(pos)
    }
  }

  override fun use(state: BlockState, level: Level, pos: BlockPos, player: Player, hand: InteractionHand, blockHitResult: BlockHitResult): InteractionResult {
    if (level.isClientSide) return InteractionResult.SUCCESS
    val blockEntity = level.getBlockEntity(pos) as CaptainSeatBlockEntity

    return if (level.getShipManagingPos(pos) == null) {
      player.displayClientMessage(Component.literal("You can only sit on a ship!"), true)
      InteractionResult.CONSUME
    }
    else if (blockEntity.sit(player)) {
      InteractionResult.CONSUME
    }
    else InteractionResult.PASS
  }

  override fun getRenderShape(blockState: BlockState): RenderShape {
    return RenderShape.MODEL
  }

  override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState? {
    return defaultBlockState()
      .setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.horizontalDirection.opposite)
  }

  override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
    builder.add(BlockStateProperties.HORIZONTAL_FACING)
  }

  override fun newBlockEntity(blockPos: BlockPos, state: BlockState): BlockEntity {
    return CaptainSeatBlockEntity(ProjectBlockEntities.CAPTAIN_SEAT.get(), blockPos, state)
  }

  override fun useShapeForLightOcclusion(blockState: BlockState): Boolean {
    return true
  }

  override fun isPathfindable(blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos, pathComputationType: PathComputationType): Boolean {
    return false
  }

  override fun rotate(state: BlockState, rotation: Rotation): BlockState {
    return state.setValue(BlockStateProperties.HORIZONTAL_FACING, rotation.rotate(state.getValue(BlockStateProperties.HORIZONTAL_FACING) as Direction)) as BlockState
  }

  override fun <T : BlockEntity?> getTicker(level: Level, state: BlockState, type: BlockEntityType<T>): BlockEntityTicker<T> = BlockEntityTicker { level, pos, state, blockEntity ->
    if (level.isClientSide) return@BlockEntityTicker
    if (blockEntity is CaptainSeatBlockEntity) {
      blockEntity.tick()
    }
  }

  override fun getShape(blockState: BlockState?, blockGetter: BlockGetter?, blockPos: BlockPos?, collisionContext: CollisionContext?): VoxelShape {
    return AllShapes.SEAT
  }
}