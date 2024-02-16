package com.dannbrown.databoxlib.content.block

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState

class NonExistingBlock(props: BlockBehaviour.Properties): Block(props) {
  // destroy the block when it ticks
  override fun tick(pState: BlockState, pLevel: ServerLevel, pPos: BlockPos, pRandom: RandomSource) {
    pLevel.removeBlock(pPos, false)
  }

  // destroy the block when it is placed
  override fun onPlace(pState: BlockState, pLevel: Level, pPos: BlockPos, pOldState: BlockState, pIsMoving: Boolean) {
    pLevel.removeBlock(pPos, false)
  }

  // destroy the block when a neighbor changes
  override fun neighborChanged(pState: BlockState, pLevel: Level, pPos: BlockPos, pBlock: Block, pFromPos: BlockPos, pIsMoving: Boolean) {
    pLevel.removeBlock(pPos, false)
  }

  // destroy the block when it is removed
  override fun onRemove(pState: BlockState, pLevel: Level, pPos: BlockPos, pNewState: BlockState, pIsMoving: Boolean) {
    pLevel.removeBlock(pPos, false)
  }
}