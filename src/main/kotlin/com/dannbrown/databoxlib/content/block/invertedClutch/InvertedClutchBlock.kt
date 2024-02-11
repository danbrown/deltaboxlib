package com.dannbrown.databoxlib.content.block.invertedClutch

import com.dannbrown.databoxlib.init.ProjectBlockEntities
import com.simibubi.create.AllBlocks
import com.simibubi.create.content.kinetics.transmission.ClutchBlock
import com.simibubi.create.content.kinetics.transmission.SplitShaftBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class InvertedClutchBlock(props: Properties) : ClutchBlock(props) {
  override fun getBlockEntityType(): BlockEntityType<out SplitShaftBlockEntity> {
    return ProjectBlockEntities.INVERTED_CLUTCH.get()
  }

  override fun neighborChanged(state: BlockState, worldIn: Level, pos: BlockPos, blockIn: Block, fromPos: BlockPos, isMoving: Boolean) {
    if (worldIn.isClientSide) return
    val previouslyPowered = state.getValue(POWERED)
    if (previouslyPowered != worldIn.hasNeighborSignal(pos)) {
      worldIn.setBlock(pos, state.cycle(POWERED), 2 or 16)
      detachKinetics(worldIn, pos, !previouslyPowered)
    }
  }

  override fun onWrenched(state: BlockState, context: UseOnContext): InteractionResult {
    val wrenchResult = super.onWrenched(state, context)
    val directionAxis = state.getValue(AXIS)

    if (context.clickedFace.axis == directionAxis) {
      val world = context.level
      val pos = context.clickedPos
      // put a clutch block with the same state as this one
      val clutchBlock = AllBlocks.CLUTCH.get()
        .defaultBlockState()
        .setValue(AXIS, directionAxis)
        .setValue(POWERED, state.getValue(POWERED))
      world.setBlock(pos, clutchBlock, 2 or 16)
      return InteractionResult.SUCCESS
    }

    return wrenchResult
  }
}