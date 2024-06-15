package com.dannbrown.deltaboxlib.content.block

import net.minecraft.core.BlockPos
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.FlowerBlock
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Supplier

class GenericFlowerBlock(
  mobEffect: Supplier<MobEffect>,
  effectDuration: Int,
  props: Properties,
  private val placeOn: ((blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos) -> Boolean)? = null
): FlowerBlock(mobEffect, effectDuration, props) {
  override fun mayPlaceOn(blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos): Boolean {
    if (placeOn != null) {
      return placeOn.invoke(blockState, blockGetter, blockPos)
    }
    return super.mayPlaceOn(blockState, blockGetter, blockPos)
  }
}