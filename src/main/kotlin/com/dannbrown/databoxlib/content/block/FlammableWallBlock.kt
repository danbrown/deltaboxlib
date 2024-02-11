package com.dannbrown.databoxlib.content.block

import com.dannbrown.databoxlib.init.ProjectBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.AxeItem
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.WallBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.ToolAction

class FlammableWallBlock(props: Properties) : WallBlock(props) {
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

    // update neighbors
    val neighborPos = context.clickedPos.relative(context.clickedFace.opposite)
    val neighborState = context.level.getBlockState(neighborPos)

    if (neighborState.block is WallBlock) {
      context.level.updateNeighborsAt(neighborPos, neighborState.block)
    }


    return when {
      context.itemInHand.item is AxeItem -> when {
        state.`is`(ProjectBlocks.JOSHUA_FAMILY.STALK!!.get()) ->
           ProjectBlocks.JOSHUA_FAMILY.STRIPPED_STALK!!.get().defaultBlockState()

        else -> super.getToolModifiedState(state, context, toolAction, simulate)
      }
      else -> super.getToolModifiedState(state, context, toolAction, simulate)
    }
  }
}