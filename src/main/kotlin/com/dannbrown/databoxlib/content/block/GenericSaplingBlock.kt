package com.dannbrown.databoxlib.content.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.SaplingBlock
import net.minecraft.world.level.block.grower.AbstractTreeGrower
import net.minecraft.world.level.block.state.BlockState

class GenericSaplingBlock(
  treeGrower:AbstractTreeGrower,
  props: Properties,
  private val placeOn: ((blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos) -> Boolean)? = null
) : SaplingBlock(treeGrower, props) {

  override fun mayPlaceOn(blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos): Boolean {
    if (placeOn != null) {
      return placeOn.invoke(blockState, blockGetter, blockPos)
    }
    return super.mayPlaceOn(blockState, blockGetter, blockPos)
  }

  override fun getPlant(world: BlockGetter, pos: BlockPos): BlockState {
    val state = world.getBlockState(pos)
    return if (state.block !== this) defaultBlockState() else state
  }

  override fun canSurvive(blockState: BlockState, levelReader: LevelReader, blockPos: BlockPos): Boolean {
    val blockpos = blockPos.below()
    val bellowBlockState = levelReader.getBlockState(blockpos)

//    return bellowBlockState.`is`(placeOn)
//    Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
    return if (blockState.block === this)
      levelReader.getBlockState(blockpos)
      .canSustainPlant(levelReader, blockpos, Direction.UP, this)
    else mayPlaceOn(
      levelReader.getBlockState(blockpos),
      levelReader,
      blockpos
    )
  }

}