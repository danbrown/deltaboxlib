package com.dannbrown.databoxlib.content.worldgen.biome

import com.dannbrown.databoxlib.content.utils.AbstractBiome
import com.dannbrown.databoxlib.lib.LibUtils
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver
import net.minecraft.world.level.levelgen.placement.PlacedFeature

object LushRedSandArchesBiome : AbstractBiome() {
  override val biomeId = "lush_red_sand_arches"
  override val BIOME_KEY: ResourceKey<Biome> = ResourceKey.create(Registries.BIOME, LibUtils.resourceLocation(biomeId))
  override fun createBiome(
    placedFeatures: HolderGetter<PlacedFeature>,
    caveGetter: HolderGetter<ConfiguredWorldCarver<*>>
  ): Biome {
    return RedSandArchesBiome.createBiome(placedFeatures, caveGetter)
  }
}