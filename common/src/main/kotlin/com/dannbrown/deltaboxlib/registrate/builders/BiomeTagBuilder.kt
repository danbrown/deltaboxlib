package com.dannbrown.deltaboxlib.registrate.builders

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome

class BiomeTagBuilder(private val registrate: AbstractDeltaboxRegistrate, private val hostTag: TagKey<Biome>) {
  fun add(tagKey: ResourceKey<Biome>): BiomeTagBuilder {
    registrate.tagRegistry.addBiome(hostTag, tagKey)
    return this
  }
}