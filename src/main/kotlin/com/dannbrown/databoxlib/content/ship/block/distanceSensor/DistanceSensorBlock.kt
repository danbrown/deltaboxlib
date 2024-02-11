package com.dannbrown.databoxlib.content.ship.block.distanceSensor

import com.dannbrown.databoxlib.content.ship.extensions.getShipManagingPos
import com.dannbrown.databoxlib.content.ship.extensions.getShipObjectManagingPos
import com.dannbrown.databoxlib.content.ship.extensions.getShipsIntersecting
import com.dannbrown.databoxlib.content.ship.extensions.toBlockPos
import com.dannbrown.databoxlib.content.ship.extensions.toWorldCoordinates
import com.dannbrown.databoxlib.init.ProjectBlockEntities
import com.simibubi.create.content.equipment.wrench.IWrenchable
import com.simibubi.create.foundation.block.IBE
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.DirectionalBlock
import net.minecraft.world.level.block.ObserverBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.phys.AABB
import org.joml.Vector3i
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.api.ships.Ship
import thedarkcolour.kotlinforforge.forge.vectorutil.v3d.toVec3

class DistanceSensorBlock(props: Properties) : DirectionalBlock(props), IBE<DistanceSensorBlockEntity>, IWrenchable {
  companion object {
    val DEBUG = true
    val DISTANCE: IntegerProperty = IntegerProperty.create("distance", 1, 64)
    val METHOD: EnumProperty<DistanceSensorBlockEntity.DetectionMode> = EnumProperty.create("method", DistanceSensorBlockEntity.DetectionMode::class.java)
    val POWERED: BooleanProperty = BlockStateProperties.POWERED
  }

  init {
    registerDefaultState(this.stateDefinition.any()
      .setValue(FACING, Direction.NORTH)
      .setValue(DISTANCE, 8)
      .setValue(METHOD, DistanceSensorBlockEntity.DetectionMode.WORLD_AND_SHIP)
      .setValue(POWERED, false)
    )
  }

  override fun getStateForPlacement(pContext: BlockPlaceContext): BlockState? {
    var facing = pContext.nearestLookingDirection
    if (pContext.player != null && pContext.player!!.isShiftKeyDown) facing = facing.opposite
    return defaultBlockState().setValue(FACING, facing)
  }

  override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
    builder.add(FACING)
    builder.add(DISTANCE)
    builder.add(METHOD)
    builder.add(POWERED)
    super.createBlockStateDefinition(builder)
  }

  override fun getBlockEntityClass(): Class<DistanceSensorBlockEntity> {
    return DistanceSensorBlockEntity::class.java
  }

  override fun getBlockEntityType(): BlockEntityType<out DistanceSensorBlockEntity> {
    return ProjectBlockEntities.DISTANCE_SENSOR.get()
  }

  override fun isSignalSource(pState: BlockState): Boolean {
    return true
  }

  override fun getDirectSignal(pBlockState: BlockState, pBlockAccess: BlockGetter, pPos: BlockPos, pSide: Direction): Int {
    return pBlockState.getSignal(pBlockAccess, pPos, pSide)
  }

  override fun getSignal(pBlockState: BlockState, pBlockAccess: BlockGetter, pPos: BlockPos, pSide: Direction): Int {
    return if (pBlockState.getValue(ObserverBlock.POWERED) && pBlockState.getValue(FACING).opposite == pSide) 15 else 0
  }

  override fun onWrenched(state: BlockState?, context: UseOnContext?): InteractionResult {
    val world = context!!.level
    val pos = context.clickedPos
    withBlockEntityDo(world, pos) { obj: DistanceSensorBlockEntity -> obj.updated() }
    return super.onWrenched(state, context)
  }

  override fun <T : BlockEntity> getTicker(
    level: Level,
    state: BlockState,
    type: BlockEntityType<T>
  ): BlockEntityTicker<T> = BlockEntityTicker { pLevel, pPos, pState, blockEntity ->
    if (pLevel.isClientSide) return@BlockEntityTicker


    if (blockEntity is DistanceSensorBlockEntity) {
      handleObserving(pState, pLevel, pPos)
    }
  }

  private fun handleObserving(pState: BlockState, pLevel: Level, pPos: BlockPos) {
    if (pLevel.isClientSide) return // only run on server
    val DISTANCE_TO_OBSERVE = pState.getValue(DISTANCE) // the amount of blocks to observe in front of the block
    val DIRECTION_TO_OBSERVE = pState.getValue(FACING).opposite
    val ship: ServerShip? = (pLevel as ServerLevel).getShipObjectManagingPos(pPos) ?: pLevel.getShipManagingPos(pPos)

    // Use Cases:
    // 1. If there is a block in angle it is looking, then activate
    // 2. If there is a ship in front of it in the direction it is facing, then activate
    // 3. If the block is inside a ship, translate the position to observe the world outside the ship, and then use the above two cases
    //
    // OBSERVE FUNCTION
    // Detect if there is a block in front of it by the range
    fun observeBlocksInPos(blockPosToLook: BlockPos): Boolean {
      var hasBlockInFront = false
      val monitorAABB: AABB = getMonitorAABB(pState, pLevel, blockPosToLook, DIRECTION_TO_OBSERVE, DISTANCE_TO_OBSERVE, -0.5)
      val positions = BlockPos.betweenClosedStream(monitorAABB)
      // Monitor the AABB for anything that is not air
      for (blockPos in positions) {
        val blockState = pLevel.getBlockState(blockPos)
        val isInFront = blockState.block != byItem(null) && !blockState.isAir && blockState.block != this

        if (isInFront) {
          hasBlockInFront = true
          break
        }
      }
      return hasBlockInFront
    }

    // Check if it has any ships in front of it
    fun observeShipsInFront(initialPos: BlockPos): Boolean {
      val blockPosToLook = initialPos
      val monitorAABB = getMonitorAABB(pState, pLevel, blockPosToLook, DIRECTION_TO_OBSERVE, DISTANCE_TO_OBSERVE, -0.5)
      val ships: Iterable<Ship> = pLevel.getShipsIntersecting(monitorAABB)
      // has any ships intersecting the monitor
      val hasAnyShips = ships.any()
      // check if the ship is actually in front of the monitor, not just the hitbox
      val shipsInFront = ships.filter {
        if (it.shipAABB === null) return@filter false
        // look for the central point of the ship and check if it is in front of the monitor via direction
        val shipCenterX = it.shipAABB!!.minX() + (it.shipAABB!!.maxX() - it.shipAABB!!.minX()) / 2
        val shipCenterY = it.shipAABB!!.minY() + (it.shipAABB!!.maxY() - it.shipAABB!!.minY()) / 2
        val shipCenterZ = it.shipAABB!!.minZ() + (it.shipAABB!!.maxZ() - it.shipAABB!!.minZ()) / 2
        val shipCenter = Vector3i(shipCenterX, shipCenterY, shipCenterZ)
        val shipCenterInWorld = it.toWorldCoordinates(shipCenter.toBlockPos())
        val shipCenterInFront = when (DIRECTION_TO_OBSERVE) {
          Direction.NORTH -> shipCenterInWorld.z < blockPosToLook.z
          Direction.SOUTH -> shipCenterInWorld.z > blockPosToLook.z
          Direction.EAST -> shipCenterInWorld.x > blockPosToLook.x
          Direction.WEST -> shipCenterInWorld.x < blockPosToLook.x
          Direction.UP -> shipCenterInWorld.y > blockPosToLook.y
          Direction.DOWN -> shipCenterInWorld.y < blockPosToLook.y
          else -> false
        }
        shipCenterInFront
      }

      return hasAnyShips && shipsInFront.any()
    }

    // check if it is close to other ships
    fun observeShipsInFrontShips(initialPos: BlockPos, currentShip: Ship): Boolean {
      val blockPosToLook = currentShip.toWorldCoordinates(initialPos)
        .toBlockPos()
      val targetPos = calculateOffset(initialPos, DIRECTION_TO_OBSERVE, DISTANCE_TO_OBSERVE)
      val targetPosToWorld = currentShip.toWorldCoordinates(targetPos)
        .toBlockPos()
      val positions = getBlock3DLine(pLevel, initialPos, targetPos, DIRECTION_TO_OBSERVE)
      val positionsToWorldCoords = positions.map {
        currentShip.toWorldCoordinates(it)
          .toBlockPos()
      }
//      if (DEBUG) {
//        positionsToWorldCoords.forEach {
//          pLevel.sendParticles(
//            ParticleTypes.GLOW,
//            it.x.toDouble(),
//            it.y.toDouble(),
//            it.z.toDouble(),
//            0,
//            0.0,
//            0.0,
//            0.0,
//            0.01
//          )
//        }
//      }
      var validatedShips = false

      positionsToWorldCoords.forEach { internalPos ->
        val ship = pLevel.getShipsIntersecting(AABB(internalPos))
          .firstOrNull()
        // has any ships intersecting the monitor
        if (ship === null) return@forEach
        if (ship.shipAABB === null) return@forEach
        // has the current ship intersecting the monitor
        val hasCurrentShip = ship.id == currentShip.id
        // check if the ship is actually in front of the monitor, not just the hitbox
        // look for the central point of the ship and check if it is in front of the monitor via direction
        val shipCenterX = ship.shipAABB!!.minX() + (ship.shipAABB!!.maxX() - ship.shipAABB!!.minX()) / 2
        val shipCenterY = ship.shipAABB!!.minY() + (ship.shipAABB!!.maxY() - ship.shipAABB!!.minY()) / 2
        val shipCenterZ = ship.shipAABB!!.minZ() + (ship.shipAABB!!.maxZ() - ship.shipAABB!!.minZ()) / 2
        val shipCenter = Vector3i(shipCenterX, shipCenterY, shipCenterZ)
        val shipCenterInWorld = ship.toWorldCoordinates(shipCenter.toBlockPos())
        val shipCenterInFront = when (DIRECTION_TO_OBSERVE) {
          Direction.NORTH -> shipCenterInWorld.z > blockPosToLook.z
          Direction.SOUTH -> shipCenterInWorld.z < blockPosToLook.z
          Direction.EAST -> shipCenterInWorld.x < blockPosToLook.x
          Direction.WEST -> shipCenterInWorld.x > blockPosToLook.x
          Direction.UP -> shipCenterInWorld.y < blockPosToLook.y
          Direction.DOWN -> shipCenterInWorld.y > blockPosToLook.y
          else -> false
        }

        if (shipCenterInFront && !hasCurrentShip) {
          validatedShips = true
        }
      }


      return validatedShips
    }

    // check if it is close to the world
    fun observeWorldInsideShip(blockPosToLook: BlockPos, currentShip: Ship): Boolean {
      // add to the block pos the DISTANCE_TO_OBSERVE in the direction it is facing
      val targetPos = calculateOffset(pPos, DIRECTION_TO_OBSERVE, DISTANCE_TO_OBSERVE)
      // get a line of blocks from the block to the monitor, considering the angle
      val positions = getBlock3DLine(pLevel, blockPosToLook, targetPos, DIRECTION_TO_OBSERVE)
      // check if any of the blocks is not air or the block itself
      val positionsToShip = positions.map {
        currentShip.toWorldCoordinates(it)
          .toBlockPos()
      }
      val hasBlockInFront = positionsToShip.any {
        if (DEBUG) {
          pLevel.sendParticles(
            ParticleTypes.FIREWORK,
            it.x.toDouble(),
            it.y.toDouble(),
            it.z.toDouble(),
            0,
            0.0,
            0.0,
            0.0,
            0.01
          )
        }
        !pLevel.getBlockState(it).isAir && pLevel.getBlockState(it).block != this
      }

      return hasBlockInFront
    }
    //
    // CASES
    var isSeeingBlockFromWorld = false
    var isSeeingBlockFromShip = false
    var isSeeingShipFromWorld = false
    var isSeeingShipFromShip = false
    //
    // ACTIONS
    if (pState.getValue(METHOD)
        .equals(DistanceSensorBlockEntity.DetectionMode.WORLD) || pState.getValue(METHOD)
        .equals(DistanceSensorBlockEntity.DetectionMode.WORLD_AND_SHIP)) {
//    if (METHOD.equals(Method.WORLD) || METHOD.equals(Method.WORLD_AND_SHIP)) {
      isSeeingBlockFromWorld = observeBlocksInPos(pPos)
      if (ship !== null) {
        isSeeingBlockFromShip = observeWorldInsideShip(pPos.above(), ship)
      }
    }
    if (pState.getValue(METHOD)
        .equals(DistanceSensorBlockEntity.DetectionMode.SHIP) || pState.getValue(METHOD)
        .equals(DistanceSensorBlockEntity.DetectionMode.WORLD_AND_SHIP)) {
      if (ship !== null) {
        isSeeingShipFromShip = observeShipsInFrontShips(pPos.above(), ship)
      }
      else {
        isSeeingShipFromWorld = observeShipsInFront(pPos)
      }
    }
    //
    // UPDATE STATE
    if (isSeeingBlockFromWorld || isSeeingBlockFromShip || isSeeingShipFromWorld || isSeeingShipFromShip) {
      pLevel.setBlock(pPos, pState.setValue(POWERED, true), 2)
    }
    else {
      pLevel.setBlock(pPos, pState.setValue(POWERED, false), 2)
    }
    val direction = pState.getValue(FACING)
    val opPos = pPos.relative(direction)
    pLevel.neighborChanged(opPos, this, pPos)
    pLevel.updateNeighborsAtExceptFromFacing(opPos, this, direction.opposite)
  }

  // UTILS
  private fun calculateOffset(blockPos: BlockPos, direction: Direction, distance: Int): BlockPos {
    return when (direction) {
      Direction.NORTH -> blockPos.north(distance)
      Direction.SOUTH -> blockPos.south(distance)
      Direction.EAST -> blockPos.east(distance)
      Direction.WEST -> blockPos.west(distance)
      Direction.UP -> blockPos.above(distance)
      Direction.DOWN -> blockPos.below(distance)
      else -> blockPos
    }
  }

  private fun getBlock3DLine(level: ServerLevel, initialPos: BlockPos, finalPos: BlockPos, direction: Direction): List<BlockPos> {
    val centralInitialPos = initialPos.toVec3()
      .toBlockPos()
    val centralFinalPos = finalPos.toVec3()
      .toBlockPos()
    val positions = when (direction) {
      Direction.NORTH -> (centralInitialPos.z downTo centralFinalPos.z).map { BlockPos(centralInitialPos.x, centralInitialPos.y, it) }
      Direction.SOUTH -> (centralInitialPos.z until centralFinalPos.z).map { BlockPos(centralInitialPos.x, centralInitialPos.y, it) }
      Direction.EAST -> (centralInitialPos.x until centralFinalPos.x).map { BlockPos(it, centralInitialPos.y, centralInitialPos.z) }
      Direction.WEST -> (centralInitialPos.x downTo centralFinalPos.x).map { BlockPos(it, centralInitialPos.y, centralInitialPos.z) }
      Direction.UP -> (centralInitialPos.y until centralFinalPos.y).map { BlockPos(centralInitialPos.x, it, centralInitialPos.z) }
      Direction.DOWN -> (centralInitialPos.y downTo centralFinalPos.y).map { BlockPos(centralInitialPos.x, it, centralInitialPos.z) }
      else -> emptyList()
    }

    return positions
  }

  private fun getMonitorAABB(pState: BlockState, pLevel: Level, blockPos: BlockPos, directionToObserve: Direction, distanceToObserve: Int, inflation: Double): AABB {
    val bounds = pState.getShape(pLevel, blockPos)
      .bounds()
      .move(blockPos)
    // Databoxs towards the front of the block by {BLOCKS_TO_OBSERVE} blocks
    val expansionX = directionToObserve.stepX * distanceToObserve.toDouble()
    val expansionY = directionToObserve.stepY * distanceToObserve.toDouble()
    val expansionZ = directionToObserve.stepZ * distanceToObserve.toDouble()

    return bounds
      .expandTowards(expansionX, expansionY, expansionZ) // expand the bounds
      .inflate(inflation) // remove 0.5 from all the values to get the center of the block
  }
}