package com.dannbrown.deltaboxlib.registrate.registry

import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

class TagRegistry(modId: String) {
  private val BLOCK_TAGS: MutableMap<TagKey<Block>, MutableList<BlockEntry>> = mutableMapOf()
  private val ITEM_TAGS: MutableMap<TagKey<Item>, MutableList<ItemEntry>> = mutableMapOf()

  // BLOCK
  fun add(tagKey: TagKey<Block>, block: BlockEntry) {
    BLOCK_TAGS.computeIfAbsent(tagKey) { mutableListOf() }.add(block)
  }

  fun getBlockTags(): MutableMap<TagKey<Block>, MutableList<BlockEntry>> {
    return BLOCK_TAGS
  }

  // ITEM
  fun add(tagKey: TagKey<Item>, item: ItemEntry) {
    ITEM_TAGS.computeIfAbsent(tagKey) { mutableListOf() }.add(item)
  }

  fun getItemTags(): MutableMap<TagKey<Item>, MutableList<ItemEntry>> {
    return ITEM_TAGS
  }
}