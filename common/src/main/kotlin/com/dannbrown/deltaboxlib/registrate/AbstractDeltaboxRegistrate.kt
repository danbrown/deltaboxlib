package com.dannbrown.deltaboxlib.registrate

import com.dannbrown.deltaboxlib.registrate.builders.BlockBuilder
import com.dannbrown.deltaboxlib.registrate.builders.ItemBuilder
import com.dannbrown.deltaboxlib.registrate.builders.LangBuilder
import com.dannbrown.deltaboxlib.registrate.registry.BlockRegistry
import com.dannbrown.deltaboxlib.registrate.registry.ItemRegistry
import com.dannbrown.deltaboxlib.registrate.registry.LangRegistry

abstract class AbstractDeltaboxRegistrate(val modId: String) {
  var blockRegistry: BlockRegistry = BlockRegistry(modId)
  var itemRegistry: ItemRegistry = ItemRegistry(modId)
  var langRegistry: LangRegistry = LangRegistry(modId)


  fun block(blockId: String): BlockBuilder {
    return BlockBuilder(this, blockId)
  }

  fun item(blockId: String): ItemBuilder {
    return ItemBuilder(this, blockId)
  }

  fun item(blockId: String, blockBuilder: BlockBuilder): ItemBuilder {
    return ItemBuilder(this, blockBuilder, blockId)
  }

  fun langs(_modId: String = modId): LangBuilder {
    return LangBuilder(this, _modId)
  }

  fun buildRegistries() {
    blockRegistry.build()
    itemRegistry.build()
  }

//  // test
//  private val createModeTabs = DeferredRegister.create(MOD_ID, Registries.CREATIVE_MODE_TAB)
//  private val items = DeferredRegister.create(MOD_ID, Registries.ITEM)
//  // test

  // @ Block Tags
//  @SafeVarargs
//  fun blockTags(tag: TagKey<Block>, vararg tags: TagKey<Block>) {
//    tagRegistry.blockTags(tag, tags)
//  }
//
//  @SafeVarargs
//  fun blockTags(tag: TagKey<Block>, vararg blocks: Supplier<Block>) {
//    tagRegistry.blockTags(tag, blocks)
//  }
//
//  @SafeVarargs
//  fun itemTags(tag: TagKey<Item>, vararg tags: TagKey<Item>) {
//    tagRegistry.itemTags(tag, tags)
//  }
//
//  @SafeVarargs
//  fun itemTags(tag: TagKey<Item>, vararg items: Supplier<Item>) {
//    tagRegistry.itemTags(tag, items)
//  }
}