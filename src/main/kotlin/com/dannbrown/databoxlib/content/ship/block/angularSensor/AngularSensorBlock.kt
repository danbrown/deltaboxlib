package com.dannbrown.databoxlib.content.ship.block.angularSensor

import com.dannbrown.databoxlib.content.ship.SpaceShipControl
import com.dannbrown.databoxlib.content.ship.extensions.getShipManagingPos
import com.dannbrown.databoxlib.content.ship.extensions.getShipObjectManagingPos
import com.dannbrown.databoxlib.init.ProjectBlockEntities
import com.simibubi.create.content.equipment.wrench.IWrenchable
import com.simibubi.create.foundation.block.IBE
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
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
import org.joml.Vector3d
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.api.ships.getAttachment

class AngularSensorBlock(props: Properties) : DirectionalBlock(props), IBE<AngularSensorBlockEntity>, IWrenchable {
  companion object {
    val POWERED: BooleanProperty = BlockStateProperties.POWERED
  }

  init {
    registerDefaultState(this.stateDefinition.any()
      .setValue(FACING, Direction.NORTH)
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
    builder.add(POWERED)
    super.createBlockStateDefinition(builder)
  }

  override fun getBlockEntityClass(): Class<AngularSensorBlockEntity> {
    return AngularSensorBlockEntity::class.java
  }

  override fun getBlockEntityType(): BlockEntityType<out AngularSensorBlockEntity> {
    return ProjectBlockEntities.ANGULAR_SENSOR.get()
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
    withBlockEntityDo(world, pos) { obj: AngularSensorBlockEntity -> obj.updated() }
    return super.onWrenched(state, context)
  }

  override fun <T : BlockEntity> getTicker(
    level: Level,
    state: BlockState,
    type: BlockEntityType<T>
  ): BlockEntityTicker<T> = BlockEntityTicker { pLevel, pPos, pState, blockEntity ->
    if (pLevel.isClientSide) return@BlockEntityTicker

    if (blockEntity is AngularSensorBlockEntity) {
      handleAngular(pLevel, pPos, pState)
    }
  }

  private fun handleAngular(pLevel: Level, pPos: BlockPos, pState: BlockState) {
    val START_ANGLE_DETECTION = 0.0
    val END_ANGLE_DETECTION = 180.0
    // 90 = ( 3.470E-6  7.510E-4 -3.538E-4) // 0 = ( 8.634E-3  8.634E-3  7.071E-1) // 180 = (-1.853E-6  2.083E-6 -7.071E-1)
    val ship: ServerShip? = (pLevel as ServerLevel).getShipObjectManagingPos(pPos) ?: pLevel.getShipManagingPos(pPos)
    if (ship == null) return
    val shipControl = ship.getAttachment<SpaceShipControl>() ?: return
    val physShip = shipControl.physShip ?: return
    val angularVelocity = physShip.poseVel.rot.getEulerAnglesXYZ(Vector3d())
    val angularVelocityDirection = Vector3d(physShip.poseVel.rot.x(), physShip.poseVel.rot.y(), physShip.poseVel.rot.z())
    val directionAxis = pState.getValue(FACING).axis
    val angle = when (directionAxis) {
      Direction.Axis.X -> Math.toDegrees(angularVelocity.x())
      Direction.Axis.Y -> Math.toDegrees(angularVelocity.y())
      Direction.Axis.Z -> Math.toDegrees(angularVelocity.z())
    }
    val isDetected: Boolean = angle in START_ANGLE_DETECTION..END_ANGLE_DETECTION
    // detection is done, now update the block state
    val newState = pState.setValue(POWERED, isDetected)
    pLevel.setBlock(pPos, newState, 2)
    val direction = pState.getValue(FACING)
    val opPos = pPos.relative(direction)
    pLevel.neighborChanged(opPos, this, pPos)
    pLevel.updateNeighborsAtExceptFromFacing(opPos, this, direction.opposite)
  }
}