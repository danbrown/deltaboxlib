package com.dannbrown.deltaboxlib.content.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.DoublePlantBlock
import net.minecraft.world.level.block.GrassBlock
import net.minecraft.world.level.block.SandBlock
import net.minecraft.world.level.block.SimpleWaterloggedBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.material.Fluids

class SimplePlantBlock(props: Properties) : DoublePlantBlock(props), SimpleWaterloggedBlock {
  init {
    registerDefaultState(stateDefinition.any()
      .setValue(HALF, DoubleBlockHalf.LOWER)
      .setValue(WATERLOGGED, false))
  }

  override fun canSurvive(state: BlockState, world: LevelReader, pos: BlockPos): Boolean {
    if (state.getValue(HALF) == DoubleBlockHalf.UPPER && state.getValue(WATERLOGGED)) {
      return false
    }
    return if (state.getValue(HALF) != DoubleBlockHalf.UPPER) {
      val groundPos = pos.below()
      val ground = world.getBlockState(groundPos).block
      if (world.getFluidState(pos).type === Fluids.WATER) return canGrow(ground)
      for (direction in Direction.Plane.HORIZONTAL) {
        if (world.getFluidState(groundPos.relative(direction)).type === Fluids.WATER) {
          return canGrow(ground)
        }
      }
      false
    }
    else {
      val blockstate = world.getBlockState(pos.below())
      if (state.block !== this) false else blockstate.block === this && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER
    }
  }

  fun canGrow(ground: Block): Boolean {
    return ground === Blocks.DIRT || ground is GrassBlock || ground is SandBlock || ground === Blocks.GRAVEL || ground === Blocks.CLAY
  }

    override fun getCloneItemStack(blockGetter: BlockGetter, blockPos: BlockPos, blockState: BlockState): ItemStack {
      return ItemStack(this.asItem())
    }

  override fun neighborChanged(state: BlockState, world: Level, pos: BlockPos, blockIn: Block, fromPos: BlockPos, isMoving: Boolean) {
    if (!canSurvive(state, world, pos)) {
      if (state.getValue(WATERLOGGED)) {
        world.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState())
      }
      else {
        world.destroyBlock(pos, false)
      }
    }
    if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
      val stateUpper = world.getBlockState(pos.above())
      if (stateUpper.block is SimplePlantBlock) {
        if (!canSurvive(stateUpper, world, pos.above())) {
          world.destroyBlock(pos.above(), false)
        }
      }
    }
  }

  override fun updateShape(state: BlockState, facing: Direction, facingState: BlockState, world: LevelAccessor, currentPos: BlockPos, facingPos: BlockPos
  ): BlockState {
    if (state.getValue(WATERLOGGED)) {
      world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world))
    }
    return super.updateShape(state, facing, facingState, world, currentPos, facingPos)
  }

  override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
    val FluidState = context.level.getFluidState(context.clickedPos)
    val state = super.getStateForPlacement(context)
    return state?.setValue(WATERLOGGED, FluidState.type === Fluids.WATER)
  }

  override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
    builder.add(HALF, WATERLOGGED)
  }

  override fun getFluidState(state: BlockState): FluidState {
    return if (state.getValue(WATERLOGGED)) Fluids.WATER.getSource(false) else super.getFluidState(state)
  }

  override fun canBeReplaced(state: BlockState, useContext: BlockPlaceContext): Boolean {
    return false
  }

  companion object {
    val HALF = BlockStateProperties.DOUBLE_BLOCK_HALF
    val WATERLOGGED = BlockStateProperties.WATERLOGGED
  }
}


