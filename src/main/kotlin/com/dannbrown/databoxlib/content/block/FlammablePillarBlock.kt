package com.dannbrown.databoxlib.content.block

import com.dannbrown.databoxlib.init.ProjectBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.AxeItem
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.ToolAction

class FlammablePillarBlock(props: Properties) : RotatedPillarBlock(props) {
  override fun isFlammable(state: BlockState?, level: BlockGetter?, pos: BlockPos?, direction: Direction?): Boolean {
    return true
  }

  override fun getFlammability(state: BlockState?, level: BlockGetter?, pos: BlockPos?, direction: Direction?): Int {
    return 5
  }

  override fun getFireSpreadSpeed(state: BlockState?, level: BlockGetter?, pos: BlockPos?, direction: Direction?): Int {
    return 5
  }

  override fun getToolModifiedState(
    state: BlockState,
    context: UseOnContext,
    toolAction: ToolAction,
    simulate: Boolean
  ): BlockState? {
    return when {
      context.itemInHand.item is AxeItem -> when {
        state.`is`(ProjectBlocks.JOSHUA_FAMILY.LOG!!.get()) ->
          ProjectBlocks.JOSHUA_FAMILY.STRIPPED_LOG!!.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS))

        state.`is`(ProjectBlocks.JOSHUA_FAMILY.WOOD!!.get()) ->
          ProjectBlocks.JOSHUA_FAMILY.STRIPPED_WOOD!!.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS))

        else -> super.getToolModifiedState(state, context, toolAction, simulate)
      }
      else -> super.getToolModifiedState(state, context, toolAction, simulate)
    }
  }
}