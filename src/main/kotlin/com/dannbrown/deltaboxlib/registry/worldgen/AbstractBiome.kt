package com.dannbrown.deltaboxlib.registry.worldgen

import net.minecraft.core.Holder
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.biome.Biome

abstract class AbstractBiome {
  abstract val biomeId: String
  abstract val BIOME_KEY: ResourceKey<Biome>
  abstract fun createBiome(context: BootstapContext<Biome>): Biome
  fun bootstrapBiome(context: BootstapContext<Biome>): Holder.Reference<Biome> {
    return context.register(BIOME_KEY, createBiome(context))
  }
}