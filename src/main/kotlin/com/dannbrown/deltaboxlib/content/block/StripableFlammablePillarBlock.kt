package com.dannbrown.deltaboxlib.content.block

import net.minecraft.world.item.AxeItem
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.ToolAction
import java.util.function.Supplier

class StripableFlammablePillarBlock(props: Properties, private val strippedBlock: Supplier<Block>): FlammablePillarBlock(props) {
  override fun getToolModifiedState(state: BlockState, context: UseOnContext, toolAction: ToolAction?, simulate: Boolean): BlockState? {
    if (context.itemInHand.item is AxeItem) {
      return strippedBlock.get()
        .defaultBlockState()
        .setValue(AXIS, state.getValue(AXIS))
    }

    return super.getToolModifiedState(state, context, toolAction, simulate)
  }
}