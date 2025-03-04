package com.dannbrown.deltaboxlib.registrate.registry

import net.minecraft.world.level.block.Block
import java.util.function.Supplier

class PottedBlockRegistry {
  private val POTTED_BLOCKS: MutableList<Pair<BlockEntry, BlockEntry>> = mutableListOf()

  fun addPottedBlock(block: BlockEntry, pottedBlock: BlockEntry) {
    POTTED_BLOCKS.add(Pair(block, pottedBlock))
  }

  fun getPottedBlocks(): List<Pair<BlockEntry, BlockEntry>> {
    return POTTED_BLOCKS
  }
}