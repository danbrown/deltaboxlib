package com.dannbrown.deltaboxlib.registrate.registry

class FlammableBlockRegistry {
  private val FLAMMABLE_BLOCKS: MutableList<Triple<BlockEntry, Int, Int>> = mutableListOf()

  fun addFlammableBlock(block: BlockEntry, burnChance: Int, spreadChance: Int) {
    FLAMMABLE_BLOCKS.add(Triple(block, burnChance, spreadChance))
  }

  fun getFlammableBlocks(): List<Triple<BlockEntry, Number, Number>> {
    return FLAMMABLE_BLOCKS
  }
}