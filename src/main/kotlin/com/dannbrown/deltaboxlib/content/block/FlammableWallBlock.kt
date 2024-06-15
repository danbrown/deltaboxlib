package com.dannbrown.deltaboxlib.content.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.WallBlock
import net.minecraft.world.level.block.state.BlockState

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
}