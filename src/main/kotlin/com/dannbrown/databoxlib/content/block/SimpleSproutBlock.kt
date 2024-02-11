package com.dannbrown.databoxlib.content.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.tags.BlockTags
import net.minecraft.tags.FluidTags
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.SwordItem
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.BambooSaplingBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.BonemealableBlock
import net.minecraft.world.level.block.SimpleWaterloggedBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.material.Fluids
import javax.annotation.Nullable

open class SimpleSproutBlock(properties: Properties?) : BambooSaplingBlock(properties), SimpleWaterloggedBlock, BonemealableBlock {

  //    val plantBlock = plantBlock
  //    val item = item
  init {
    registerDefaultState(stateDefinition.any()
      .setValue(WATERLOGGED, false))
  } //  override fun randomTick(blockState: BlockState, serverLevel: ServerLevel, blockPos: BlockPos, random: RandomSource) {
  //    val i = random.nextInt(5)
  //    if (i == 4) {
  //      if (serverLevel.getBlockState(blockPos.above()) === Blocks.AIR.defaultBlockState()) {
  //        growCatTail(serverLevel, blockPos)
  //      }
  //    }
  //  }
  override fun getFluidState(blockState: BlockState): FluidState {
    return if (blockState.getValue(WATERLOGGED)) Fluids.WATER.getSource(false)
    else super.getFluidState(blockState)
  }

  @Nullable
  override fun getStateForPlacement(blockPlaceContext: BlockPlaceContext): BlockState? {
    val fluidState = blockPlaceContext.level.getFluidState(blockPlaceContext.clickedPos)
    return if (fluidState.type === Fluids.WATER) {
      super.getStateForPlacement(blockPlaceContext)!!
        .setValue<Boolean, Boolean>(WATERLOGGED, true)
    }
    else defaultBlockState()
  } //  fun growCatTail(serverLevel: ServerLevel, blockPos: BlockPos) {
  //    if (serverLevel.getFluidState(blockPos).type === Fluids.WATER) {
  //      serverLevel.setBlock(
  //        blockPos,
  //        plantBlock.defaultBlockState().setValue(SimplePlantBlock.HALF, DoubleBlockHalf.LOWER)
  //          .setValue(SimplePlantBlock.WATERLOGGED, true),
  //        3
  //      )
  //    } else {
  //      serverLevel.setBlock(
  //        blockPos,
  //        plantBlock.defaultBlockState().setValue(SimplePlantBlock.HALF, DoubleBlockHalf.LOWER)
  //          .setValue(SimplePlantBlock.WATERLOGGED, false),
  //        3
  //      )
  //    }
  //    serverLevel.setBlock(
  //      blockPos.above(),
  //      plantBlock.defaultBlockState().setValue(SimplePlantBlock.HALF, DoubleBlockHalf.UPPER)
  //        .setValue(SimplePlantBlock.WATERLOGGED, false),
  //      3
  //    )
  //  }
  override fun canSurvive(blockState: BlockState, levelReader: LevelReader, blockPos: BlockPos): Boolean {
    val state = levelReader.getBlockState(blockPos)
    val blockState2 = levelReader.getBlockState(blockPos.below())
    if (levelReader.getBlockState(blockPos.above(1)) === Blocks.AIR.defaultBlockState() && (blockState2.`is`(BlockTags.DIRT) || blockState2.`is`(Blocks.SAND) || blockState2.`is`(Blocks.RED_SAND)) && state === Blocks.WATER.defaultBlockState()) {
      return true
    }
    if (blockState2.`is`(BlockTags.DIRT) || blockState2.`is`(Blocks.SAND) || blockState2.`is`(Blocks.RED_SAND)) {
      val blockPos2 = blockPos.below()
      for (direction in Direction.Plane.HORIZONTAL) {
        val blockState3 = levelReader.getBlockState(blockPos2.relative(direction))
        val fluidState = levelReader.getFluidState(blockPos2.relative(direction))
        if (fluidState.`is`(FluidTags.WATER) || blockState3.`is`(Blocks.FROSTED_ICE)) {
          return true
        }
      }
    }
    return false
  }

  override fun updateShape(blockState: BlockState, direction: Direction?, blockState2: BlockState?, levelAccessor: LevelAccessor, blockPos: BlockPos?, blockPos2: BlockPos?
  ): BlockState {
    if (blockState.getValue<Boolean>(WATERLOGGED)) {
      levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor))
    }
    return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2)
  } //  override fun getCloneItemStack(blockGetter: BlockGetter, blockPos: BlockPos, blockState: BlockState): ItemStack {
  //    return ItemStack(item)
  //  }
  //  override fun performBonemeal(
  //    serverLevel: ServerLevel,
  //    random: RandomSource,
  //    blockPos: BlockPos,
  //    blockState: BlockState
  //  ) {
  ////    growCatTail(serverLevel, blockPos)
  //  }
  override fun getDestroyProgress(blockState: BlockState?, player: Player, blockGetter: BlockGetter?, blockPos: BlockPos?
  ): Float {
    return if (player.mainHandItem
        .item is SwordItem) 1.0f
    else super.getDestroyProgress(blockState, player, blockGetter, blockPos)
  }

  override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
    builder.add(WATERLOGGED)
  }

  companion object {
    val WATERLOGGED = BlockStateProperties.WATERLOGGED
  }
}

