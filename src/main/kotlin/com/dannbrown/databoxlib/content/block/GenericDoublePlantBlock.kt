package com.dannbrown.databoxlib.content.block

import net.minecraft.core.BlockPos
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.DoublePlantBlock
import net.minecraft.world.level.block.state.BlockState

open class GenericDoublePlantBlock(
  props: Properties,
  private val placeOn: ((blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos) -> Boolean)? = null
): DoublePlantBlock(props) {

  override fun mayPlaceOn(blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos): Boolean {
    if (placeOn != null) {
      return placeOn.invoke(blockState, blockGetter, blockPos)
    }
    return blockState.`is`(BlockTags.DIRT) || blockState.`is`(Blocks.GRASS_BLOCK)
  }
}