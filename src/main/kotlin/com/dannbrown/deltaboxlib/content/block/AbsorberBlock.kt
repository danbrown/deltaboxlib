package com.dannbrown.deltaboxlib.content.block

import com.google.common.collect.Lists
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.Tuple
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.BucketPickup
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.block.state.BlockState
import java.util.*
import java.util.function.Supplier

class AbsorberBlock(private val fluidToAbsorb: Supplier<Block>, private val itemToDrop: Supplier<Item>, props: Properties) : Block(props) {
  override fun onPlace(
    blockState: BlockState,
    level: Level,
    blockPos: BlockPos,
    blockState2: BlockState,
    p_56815_: Boolean
  ) {
    if (!blockState2.`is`(blockState.block)) {
      tryAbsorbFluid(level, blockPos)
    }
  }

  override fun neighborChanged(
    blockState: BlockState,
    level: Level,
    blockPos: BlockPos,
    block: Block,
    blocPos2: BlockPos,
    p_56806_: Boolean
  ) {
//    tryAbsorbFluid(level, blockPos)
    super.neighborChanged(blockState, level, blockPos, block, blocPos2, p_56806_)
  }

  protected fun tryAbsorbFluid(level: Level, blockPos: BlockPos) {
    // absorb the desired fluid Max radius 6, max block count 64
    var blocksRemoved = 0
    var fullBlocksRemoved = 0
    for (i in 0..6) {
      for (j in 0..6) {
        for (k in 0..6) {
          val blockpos = blockPos.offset(i - 3, j - 3, k - 3)
          val blockstate = level.getBlockState(blockpos)

          if (blocksRemoved > 64) { break }

          if (blockstate.block === fluidToAbsorb.get()) {
            if (blockstate.getValue(LiquidBlock.LEVEL) == 0) {
              fullBlocksRemoved++
            }
            blocksRemoved++
            level.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 3)
          }
        }
      }
    }

    // drop water bubble according to the amount of water absorbed, max stack is 8
    val stacks = fullBlocksRemoved / 8
    val remainder = fullBlocksRemoved % 8
    for (i in 0 until stacks) {
      val bubbleItem = ItemStack(itemToDrop.get())
      bubbleItem.count = 8
      popResource(level, blockPos, bubbleItem)
    }
    if (remainder > 0) {
      val bubbleItem = ItemStack(itemToDrop.get())
      bubbleItem.count = remainder
      popResource(level, blockPos, bubbleItem)
    }

    // destroy itself if it dropped any bubbles
    if (fullBlocksRemoved > 0) {
      level.destroyBlock(blockPos, false)
    }

//    if (removeWaterBreadthFirstSearch(level, blockPos)) {
//
//
//    }
  }

  private fun removeWaterBreadthFirstSearch(level: Level, blockPos: BlockPos): Boolean {
    val queue: Queue<Tuple<BlockPos, Int>> = Lists.newLinkedList()
    queue.add(Tuple(blockPos, 0))
    var i = 0
    val state = level.getBlockState(blockPos)
    while (!queue.isEmpty()) {
      val tuple = queue.poll()
      val blockpos = tuple.a
      val j = tuple.b
      for (direction in Direction.values()) {
        val blockpos1 = blockpos.relative(direction)
        val blockstate = level.getBlockState(blockpos1)
        val fluidstate = level.getFluidState(blockpos1)
        if (state.canBeHydrated(level, blockPos, fluidstate, blockpos1)) {
          if (blockstate.block is BucketPickup && !(blockstate.block as BucketPickup).pickupBlock(
              level,
              blockpos1,
              blockstate
            ).isEmpty
          ) {
            ++i
            if (j < 6) {
              queue.add(Tuple(blockpos1, j + 1))
            }
          } else if (blockstate.block is LiquidBlock) {
            level.setBlock(blockpos1, Blocks.AIR.defaultBlockState(), 3)
            ++i
            if (j < 6) {
              queue.add(Tuple(blockpos1, j + 1))
            }
          } else if (!blockstate.`is`(Blocks.KELP) && !blockstate.`is`(Blocks.KELP_PLANT) && !blockstate.`is`(Blocks.SEAGRASS) && !blockstate.`is`(
              Blocks.TALL_SEAGRASS
            )
          ) {
            val blockentity = if (blockstate.hasBlockEntity()) level.getBlockEntity(blockpos1) else null
            dropResources(blockstate, level, blockpos1, blockentity)
            level.setBlock(blockpos1, Blocks.AIR.defaultBlockState(), 3)
            ++i
            if (j < 6) {
              queue.add(Tuple(blockpos1, j + 1))
            }
          }
        }
      }
      if (i > 64) {
        break
      }
    }
    return i > 0
  }

  companion object {
    const val MAX_DEPTH = 6
    const val MAX_COUNT = 64
  }
}

