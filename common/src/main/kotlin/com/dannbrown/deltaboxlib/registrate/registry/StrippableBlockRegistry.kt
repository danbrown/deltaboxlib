package com.dannbrown.deltaboxlib.registrate.registry

import net.minecraft.world.level.block.Block
import java.util.function.Supplier

class StrippableBlockRegistry {
  private val STRIPPABLE_BLOCKS: MutableList<Pair<BlockEntry, BlockEntry>> = mutableListOf()

  fun addStrippableBlock(block: BlockEntry, strippedBlock: BlockEntry) {
    STRIPPABLE_BLOCKS.add(Pair(block, strippedBlock))
  }

  fun getStrippableBlocks(): List<Pair<BlockEntry, BlockEntry>> {
    return STRIPPABLE_BLOCKS
  }
}