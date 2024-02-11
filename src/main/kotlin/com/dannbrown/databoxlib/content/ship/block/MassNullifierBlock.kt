package com.dannbrown.databoxlib.content.ship.block

import com.dannbrown.databoxlib.content.ship.SpaceShipControl
import com.dannbrown.databoxlib.content.ship.extensions.getShipManagingPos
import com.dannbrown.databoxlib.content.ship.extensions.getShipObjectManagingPos
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.DirectionalBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import org.valkyrienskies.core.api.ships.getAttachment

class MassNullifierBlock(properties: Properties) : DirectionalBlock(properties) {
  val POWERED: BooleanProperty = BlockStateProperties.POWERED

  init {
    registerDefaultState(defaultBlockState().setValue(BlockStateProperties.POWERED, false))
  }

  override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
    builder.add(BlockStateProperties.POWERED)
    super.createBlockStateDefinition(builder)
  }

  override fun getStateForPlacement(context: BlockPlaceContext): BlockState {
    return super.getStateForPlacement(context)!!
      .setValue(BlockStateProperties.POWERED,
        context.level.hasNeighborSignal(context.clickedPos))
  }

  override fun neighborChanged(state: BlockState, worldIn: Level, pos: BlockPos, blockIn: Block, fromPos: BlockPos, isMoving: Boolean) {
    if (worldIn.isClientSide) return
    val previouslyPowered = state.getValue(BlockStateProperties.POWERED)
    if (previouslyPowered != worldIn.hasNeighborSignal(pos)) {
      val newState = state.cycle(BlockStateProperties.POWERED)
      worldIn.setBlock(pos, newState, 2)
      // update mass nullifier
      val ship = (worldIn as ServerLevel).getShipObjectManagingPos(pos) ?: worldIn.getShipManagingPos(pos) ?: return
      val shipControl = ship.getAttachment<SpaceShipControl>() ?: return
      shipControl.updateMassNullifierActive(pos, newState.getValue(BlockStateProperties.POWERED))
    }
  }

  override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, isMoving: Boolean) {
    super.onPlace(state, level, pos, oldState, isMoving)

    if (level.isClientSide) return
    level as ServerLevel
    val ship = level.getShipObjectManagingPos(pos) ?: level.getShipManagingPos(pos) ?: return
    val shipControl = SpaceShipControl.getOrCreate(ship)
    shipControl.addMassNullifier(pos, state.getValue(BlockStateProperties.POWERED))
  }

  override fun destroy(level: LevelAccessor, pos: BlockPos, state: BlockState) {
    super.destroy(level, pos, state)

    if (level.isClientSide) return
    level as ServerLevel
    val ship = level.getShipObjectManagingPos(pos) ?: level.getShipManagingPos(pos) ?: return
    val shipControl = ship.getAttachment<SpaceShipControl>() ?: return
    shipControl.removeMassNullifier(pos)
  }
}
