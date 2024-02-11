package com.dannbrown.databoxlib.content.ship.block.shipConsole

import com.dannbrown.databoxlib.content.ship.SpaceShipControl
import com.dannbrown.databoxlib.content.ship.block.captainSeat.CaptainSeatBlockEntity
import com.dannbrown.databoxlib.content.ship.extensions.getShipManagingPos
import com.dannbrown.databoxlib.content.ship.extensions.getShipObjectManagingPos
import com.dannbrown.databoxlib.content.ship.extensions.shipObjectWorld
import com.dannbrown.databoxlib.content.ship.extensions.toMinecraft
import com.dannbrown.databoxlib.content.ship.extensions.toWorldCoordinates
import com.dannbrown.databoxlib.content.ship.extensions.vsCore
import com.dannbrown.databoxlib.init.ProjectBlockEntities
import com.dannbrown.databoxlib.init.ProjectTeleporter
import com.simibubi.create.content.contraptions.actors.seat.SeatEntity
import com.simibubi.create.foundation.block.IBE
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.TamableAnimal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.portal.PortalInfo
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3
import org.joml.Vector3d
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.api.ships.getAttachment
import org.valkyrienskies.core.apigame.ShipTeleportData
import org.valkyrienskies.core.apigame.world.properties.DimensionId
import org.valkyrienskies.core.impl.game.ShipTeleportDataImpl
import org.valkyrienskies.mod.common.ValkyrienSkiesMod
import org.valkyrienskies.mod.common.entity.ShipMountingEntity
import thedarkcolour.kotlinforforge.forge.vectorutil.v3d.toVec3

class ShipConsoleBlock(props: Properties) : HorizontalDirectionalBlock(props), IBE<ShipConsoleBlockEntity> {
  companion object {
    val TELEPORT_HEIGHT = 280.0 // TODO: make this dynamic via the current Level
  }

  var playerToTeleport: ServerPlayer? = null
  var playerSeat: CaptainSeatBlockEntity? = null

  init {
    registerDefaultState(this.stateDefinition.any()
      .setValue(FACING, Direction.NORTH))
  }

  override fun getStateForPlacement(pContext: BlockPlaceContext): BlockState? {
    return defaultBlockState().setValue(FACING, pContext.horizontalDirection)
  }

  override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
    builder.add(FACING)
    super.createBlockStateDefinition(builder)
  }

  override fun getBlockEntityClass(): Class<ShipConsoleBlockEntity> {
    return ShipConsoleBlockEntity::class.java
  }

  override fun getBlockEntityType(): BlockEntityType<out ShipConsoleBlockEntity> {
    return ProjectBlockEntities.SHIP_CONSOLE.get()
  }

  override fun use(pState: BlockState, pLevel: Level, pPos: BlockPos, pPlayer: Player, pHand: InteractionHand, pHit: BlockHitResult): InteractionResult {
    if (pLevel.isClientSide) {
      return InteractionResult.PASS
    }
    val blockEntity = pLevel.getBlockEntity(pPos)
    //hand is empty
    if (blockEntity is ShipConsoleBlockEntity && pPlayer.mainHandItem.isEmpty && pHand == InteractionHand.MAIN_HAND) {
      handleRightClick(pState, pLevel, pPos, pPlayer, pHand, pHit)
      return InteractionResult.SUCCESS
    }
    return InteractionResult.PASS
  }

  private fun handleRightClick(pState: BlockState, pLevel: Level, pPos: BlockPos, pPlayer: Player, pHand: InteractionHand, pHit: BlockHitResult) {
    val ship: ServerShip? = (pLevel as ServerLevel).getShipObjectManagingPos(pPos) ?: pLevel.getShipManagingPos(pPos)
    val isPlayerRiding = pPlayer.vehicle !== null && pPlayer.vehicle!!.type == ValkyrienSkiesMod.SHIP_MOUNTING_ENTITY_TYPE
    // if there is no ship, then the player cannot travel
    if (ship === null) {
      pPlayer.displayClientMessage(Component.literal("You can only travel on a ship!"), true)
      return
    }
    // if the player is not riding a ship, then they cannot travel
    if (!isPlayerRiding) {
      pPlayer.displayClientMessage(Component.literal("You must be commanding a ship to travel!"), true)
      return
    }
    // if the current height is not high enough, then the player cannot travel
//    if (pPlayer.y < TELEPORT_HEIGHT) {
//      pPlayer.displayClientMessage(Component.literal("Ship needs to be higher to travel!"), true)
//      return
//    }
    // get ship control
    val shipControl = ship.getAttachment<SpaceShipControl>() ?: return
    // HARDCODED DIMENSION KEYS START
    val overworldKey = "minecraft:overworld"
    val overworldId = "minecraft:dimension:${overworldKey}"
    val overworldResourceKey: ResourceKey<Level> = ResourceKey.create(Registries.DIMENSION, ResourceLocation(overworldKey))
    val moonKey = "databoxlib:moon"
    val moonId = "minecraft:dimension:${moonKey}"
    val moonResourceKey: ResourceKey<Level> = ResourceKey.create(Registries.DIMENSION, ResourceLocation(moonKey))
    // Choose
    val nextDimension: DimensionId = if (ship.chunkClaimDimension == overworldId) moonId else overworldId
    val nextDimensionResourceKey: ResourceKey<Level> = if (ship.chunkClaimDimension == overworldId) moonResourceKey else overworldResourceKey
    // HARDCODED DIMENSION KEYS END
    val thisBlockWorldPos = ship.toWorldCoordinates(pPos)
    ship.shipAABB ?: return
    val shipHeight = ship.shipAABB!!.maxY() - ship.shipAABB!!.minY()
    val posToTeleportShip = Vector3d(thisBlockWorldPos.x, TELEPORT_HEIGHT - shipHeight, thisBlockWorldPos.z)
    val posToTeleportEntities = Vector3d(thisBlockWorldPos.x, TELEPORT_HEIGHT + 1, thisBlockWorldPos.z)
    pPlayer.server?.getLevel(nextDimensionResourceKey) ?: return // just checks if the new dimension exists
    // GET THE PLAYER VEHICLE START
    val playerVehicle = pPlayer.vehicle as ShipMountingEntity // here it was already verified that the player is riding the correct vehicle
    val shipMountingEntityPos = playerVehicle.blockPosition()
    val seatBlockEntity = pLevel.getBlockEntity(shipMountingEntityPos) ?: return
    // GET THE PLAYER VEHICLE END
    // GET ALL LIVING ENTITIES INSIDE THE SHIP
    val livingEntitiesInsideShip = getEntitiesInsideAABB(pLevel, ship.shipAABB!!.toMinecraft()).filterIsInstance<LivingEntity>()
      .filter { it !== pPlayer } // remove the ship captain
    val rangedAABB = ship.shipAABB!!.toMinecraft()
      .inflate(1.0) // inflate the ship AABB by 1 block in all directions to catch all entities
    val entities = getEntitiesInsideAABB(pLevel, rangedAABB).filter { it !== pPlayer } // remove the ship captain
    // GET ALL LIVING ENTITIES INSIDE THE SHIP END
    // teleport all entities
    handleEntitiesTeleport(entities, ship, pLevel, nextDimensionResourceKey)
    // compile teleport data
    val teleportData: ShipTeleportData = ShipTeleportDataImpl(newPos = posToTeleportShip, newDimension = nextDimension)
    // teleport the ship
    vsCore.teleportShip(pLevel.shipObjectWorld, ship, teleportData)
    // teleport the player
    handlePlayerTeleport(pPlayer, posToTeleportEntities, nextDimensionResourceKey)
    handleCaptainSitBack(pPlayer as ServerPlayer, ship, seatBlockEntity as CaptainSeatBlockEntity)
//    handleEntitiesSitBack(livingEntitiesInsideShip, ship, pLevel, posToTeleportEntities, nextDimensionResourceKey)
  }

  private fun getBlockEntitiesInsideAABB(level: ServerLevel, aabb: AABB): List<BlockEntity> {
    val positions = BlockPos.betweenClosedStream(aabb)
    val blockEntities = mutableListOf<BlockEntity>()
    positions.forEach { pos ->
      val blockEntity = level.getBlockEntity(pos)
      if (blockEntity !== null) {
        blockEntities.add(blockEntity)
      }
    }
    return blockEntities
  }

  private fun getEntitiesInsideAABB(level: ServerLevel, aabb: AABB): List<Entity> {
    return level.getEntities(null, aabb)
  }

  private fun handlePlayerTeleport(pPlayer: Player, pos: Vector3d, dimensionKey: ResourceKey<Level>) {
    val newLevel = pPlayer.server?.getLevel(dimensionKey) ?: return
    // get player vehicle
    // teleport the player to the new dimension and to the new block position
    val portalInfo = PortalInfo(pos.toVec3(), pPlayer.deltaMovement, pPlayer.xRot, pPlayer.yRot)
    val target = ProjectTeleporter(portalInfo)
    pPlayer.changeDimension(newLevel, target)
  }

  private fun handleCaptainSitBack(player: ServerPlayer, ship: ServerShip, seatBlockEntity: CaptainSeatBlockEntity) {
    val shipBlockEntities = getBlockEntitiesInsideAABB(player.level() as ServerLevel, ship.shipAABB!!.toMinecraft())
    val seatInsideShip = shipBlockEntities.filterIsInstance<CaptainSeatBlockEntity>()
      .firstOrNull {
        it.blockPos.x == seatBlockEntity.blockPos.x &&
          it.blockPos.y == seatBlockEntity.blockPos.y &&
          it.blockPos.z == seatBlockEntity.blockPos.z
      }

    if (seatInsideShip === null) return

    seatInsideShip.sit(player, false)
  }

  private fun handleEntitiesTeleport(entities: List<Entity>, ship: ServerShip, level: ServerLevel, dimensionKey: ResourceKey<Level>) {
    entities.forEach { entity ->
      // teleport the entity to the new dimension
      val newLevel = entity.level().server?.getLevel(dimensionKey) ?: return@forEach
      val entityPos = entity.position()
      val entityPosInShip = entityPos.add(0.0, 0.0, 0.0)
      val portalInfo = PortalInfo(entityPosInShip, Vec3(0.0, 0.0, 0.0), entity.xRot, entity.yRot)
      val target = ProjectTeleporter(portalInfo)
      entity.changeDimension(newLevel, target)
    }
  }

  private fun handleEntitiesSitBack(entities: List<LivingEntity>, ship: ServerShip, level: ServerLevel, pos: Vector3d, dimensionKey: ResourceKey<Level>) {
    // first, filter all entities with a vehicle that is either a ShipMountingEntity or a SeatEntity
    val entitiesWithSeat = entities.filter { it.vehicle !== null && (it.vehicle is ShipMountingEntity || it.vehicle is SeatEntity) }
    // then, for each entity, get the seat block entity and the ship mounting entity
    val seatRiders: MutableList<Pair<LivingEntity, BlockPos>> = mutableListOf()
    entitiesWithSeat.forEach { entity ->
      if (entity.vehicle === null) return@forEach
      if (entity.vehicle is SeatEntity) {
        val seatEntity = entity.vehicle as SeatEntity
        val seatEntityPos = seatEntity.blockPosition()
        // teleport the entity to the new dimension
        val newLevel = entity.level().server?.getLevel(dimensionKey) ?: return@forEach
        val portalInfo = PortalInfo(pos.toVec3(), entity.deltaMovement, entity.xRot, entity.yRot)
        val target = ProjectTeleporter(portalInfo)
        seatEntity.ejectPassengers()
        entity.stopRiding()
        entity.changeDimension(newLevel, target)
        entity.onAddedToWorld()

        seatRiders.add(Pair(entity, seatEntityPos))
//
////        SeatBlock.sitDown(newLevel, seatEntityPos, entity)
//        // make the entity sit on the seat
//        val currentVehicle = entity.vehicle
//        val seat = SeatEntity(entity.level(), seatEntityPos)
//        seat.setPos((pos.x + .5f), pos.y, (pos.z + .5f))
//        newLevel.addFreshEntity(seat)
//        entity.startRiding(seat, true)
//        if (entity is TamableAnimal) entity.isInSittingPose = true
//        val nowVehicle = entity.vehicle
//        if (nowVehicle !== null && nowVehicle is SeatEntity) {
////          nowVehicle.setPos((pos.x + .5f), pos.y, (pos.z + .5f))
//        }
      }
      else if (entity.vehicle is ShipMountingEntity) {
      }
    }
    // then, for each entity, get the seat block entity and the ship mounting entity
    seatRiders.forEach { (entity, seatEntityPos) ->
      // make the entity sit on the seat
      val currentVehicle = entity.vehicle
      if (currentVehicle !== null) return@forEach
      val seat = SeatEntity(entity.level(), seatEntityPos)
      seat.setPos((pos.x + .5f), pos.y, (pos.z + .5f))
      entity.level()
        .addFreshEntity(seat)
      entity.startRiding(seat, true)
      if (entity is TamableAnimal) entity.isInSittingPose = true
      val nowVehicle = entity.vehicle
    }
    val amem = 0
//    val shipBlockEntities = getBlockEntitiesInsideAABB(ship.shipObjectWorld as ServerLevel, ship.shipAABB!!.toMinecraft())
//    val seatInsideShip = shipBlockEntities.filterIsInstance<CaptainSeatBlockEntity>()
//      .firstOrNull {
//        it.blockPos.x == seatBlockEntity.blockPos.x &&
//          it.blockPos.y == seatBlockEntity.blockPos.y &&
//          it.blockPos.z == seatBlockEntity.blockPos.z
//      }
//
//    if (seatInsideShip === null) return
//
//    seatInsideShip.sit(player, false)
  }
}

