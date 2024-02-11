package com.dannbrown.databoxlib.content.block.resistor

import com.dannbrown.databoxlib.init.ProjectBlockEntities
import com.simibubi.create.content.kinetics.transmission.GearshiftBlock
import com.simibubi.create.content.kinetics.transmission.SplitShaftBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.IntegerProperty

class ResistorBlock(props: Properties) : GearshiftBlock(props) {
  companion object {
    val POWER: IntegerProperty = BlockStateProperties.POWER
  }

  override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
    builder.add(POWER)
    super.createBlockStateDefinition(builder)
  }

  override fun getStateForPlacement(context: BlockPlaceContext): BlockState {
    return super.getStateForPlacement(context)!!
      .setValue(POWERED,
        context.level.hasNeighborSignal(context.clickedPos))
      .setValue(POWER, context.level.getBestNeighborSignal(context.clickedPos))
  }

  override fun neighborChanged(state: BlockState, worldIn: Level, pos: BlockPos, blockIn: Block, fromPos: BlockPos, isMoving: Boolean) {
    if (worldIn.isClientSide) return
    val previouslyPower = state.getValue(POWER)
    if (previouslyPower != worldIn.getBestNeighborSignal(pos)) {
      worldIn.setBlock(pos,
        state.setValue(POWERED, worldIn.hasNeighborSignal(pos))
          .setValue(POWER, worldIn.getBestNeighborSignal(pos)),
        3)
      detachKinetics(worldIn, pos, true)
    }
  }

  override fun getBlockEntityType(): BlockEntityType<out SplitShaftBlockEntity> {
    return ProjectBlockEntities.RESISTOR.get()
  }
}