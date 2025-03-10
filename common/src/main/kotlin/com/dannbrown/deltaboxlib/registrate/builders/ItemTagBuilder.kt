package com.dannbrown.deltaboxlib.registrate.builders

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.registry.ItemEntry
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

class ItemTagBuilder(private val registrate: AbstractDeltaboxRegistrate, private val hostTag: TagKey<Item>) {
  fun add(tagKey: ItemEntry<*>): ItemTagBuilder {
    registrate.tagRegistry.addItem(hostTag, tagKey)
    return this
  }
}