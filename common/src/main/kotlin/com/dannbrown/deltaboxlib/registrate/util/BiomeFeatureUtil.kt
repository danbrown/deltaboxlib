package com.dannbrown.deltaboxlib.registrate.util

import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.level.biome.BiomeGenerationSettings
import net.minecraft.world.level.biome.BiomeSpecialEffects
import net.minecraft.world.level.biome.MobSpawnSettings
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData

object BiomeFeaturePresets {
  fun generateColors(
    builder: BiomeSpecialEffects.Builder,
    skyFog: Int,
    grass: Int
  ): BiomeSpecialEffects.Builder {
    return builder
      .skyColor(1186057)
      .fogColor(skyFog)
      .waterColor(342306)
      .waterFogColor(332810)
      .grassColorOverride(grass)
      .foliageColorOverride(grass)
  }
}