package com.dannbrown.deltaboxlib.registrate.builders

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.registry.BlockEntry
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

class BlockTagBuilder(private val registrate: AbstractDeltaboxRegistrate, private val hostTag: TagKey<Block>) {
  fun add(tagKey: BlockEntry<*>): BlockTagBuilder {
    registrate.tagRegistry.add(hostTag, tagKey)
    return this
  }
}