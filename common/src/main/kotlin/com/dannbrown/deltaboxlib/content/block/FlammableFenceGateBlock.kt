package com.dannbrown.deltaboxlib.content.block


import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.FenceGateBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.WoodType

open class FlammableFenceGateBlock(
  props: Properties,
  woodType: WoodType,
  private val flammability: Int = 20,
  private val fireSpread: Int = 5
) : FenceGateBlock(props, woodType) {
  fun isFlammable(
    state: BlockState,
    level: BlockGetter,
    pos: BlockPos,
    direction: Direction
  ): Boolean { // soft-override on forge
    return true
  }

  fun getFlammability(
    state: BlockState,
    level: BlockGetter,
    pos: BlockPos,
    direction: Direction
  ): Int { // soft-override on forge
    return flammability
  }

  fun getFireSpreadSpeed(
    state: BlockState,
    level: BlockGetter,
    pos: BlockPos,
    direction: Direction
  ): Int { // soft-override on forge
    return fireSpread
  }
}