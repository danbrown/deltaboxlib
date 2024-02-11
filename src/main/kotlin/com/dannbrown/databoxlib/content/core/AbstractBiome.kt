package com.dannbrown.databoxlib.content.utils

import net.minecraft.core.HolderGetter
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver
import net.minecraft.world.level.levelgen.placement.PlacedFeature

abstract class AbstractBiome {
  abstract val biomeId: String
  abstract val BIOME_KEY: ResourceKey<Biome>
  abstract fun createBiome(
    placedFeatures: HolderGetter<PlacedFeature>,
    caveGetter: HolderGetter<ConfiguredWorldCarver<*>>
  ): Biome
}