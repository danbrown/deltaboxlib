package com.dannbrown.databoxlib.content.ship.block.altitudeSensor

import com.dannbrown.databoxlib.content.ship.extensions.getShipManagingPos
import com.dannbrown.databoxlib.content.ship.extensions.getShipObjectManagingPos
import com.dannbrown.databoxlib.content.ship.extensions.toBlockPos
import com.dannbrown.databoxlib.content.ship.extensions.toWorldCoordinates
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
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.api.ships.Ship

class AltitudeSensorBlock(props: Properties) : DirectionalBlock(props), IBE<AltitudeSensorBlockEntity>, IWrenchable {
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

  override fun getBlockEntityClass(): Class<AltitudeSensorBlockEntity> {
    return AltitudeSensorBlockEntity::class.java
  }

  override fun getBlockEntityType(): BlockEntityType<out AltitudeSensorBlockEntity> {
    return ProjectBlockEntities.ALTITUDE_SENSOR.get()
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
    withBlockEntityDo(world, pos) { obj: AltitudeSensorBlockEntity -> obj.updated() }
    return super.onWrenched(state, context)
  }

  override fun <T : BlockEntity> getTicker(
    level: Level,
    state: BlockState,
    type: BlockEntityType<T>
  ): BlockEntityTicker<T> = BlockEntityTicker { pLevel, pPos, pState, blockEntity ->
    if (pLevel.isClientSide) return@BlockEntityTicker

    if (blockEntity is AltitudeSensorBlockEntity) {
      handleAltitude(pLevel, pPos, pState)
    }
  }

  private fun handleAltitude(pLevel: Level, pPos: BlockPos, pState: BlockState) {
    val START_DETECTION = 100
    val END_DETECTION = 105
    val ship: ServerShip? = (pLevel as ServerLevel).getShipObjectManagingPos(pPos) ?: pLevel.getShipManagingPos(pPos)

    // if the block is in the detection range, in the Y axis, then it should be activated
    fun detectPositionInWorld(pos: BlockPos): Boolean {
      val y = pos.y
      return y in START_DETECTION..END_DETECTION
    }

    // if the block is inside a ship, detect the world position of the block
    fun detectPositionInShip(pos: BlockPos, ship: Ship): Boolean {
      val posInShip = ship.toWorldCoordinates(pos)
      return detectPositionInWorld(posInShip.toBlockPos())
    }

    val isDetected: Boolean
    if (ship != null) {
      isDetected = detectPositionInShip(pPos, ship)
    }
    else {
      isDetected = detectPositionInWorld(pPos)
    }
    // detection is done, now update the block state
    if (isDetected) {
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
}