package com.dannbrown.databoxlib.content.utils

import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.level.biome.BiomeGenerationSettings
import net.minecraft.world.level.biome.BiomeSpecialEffects
import net.minecraft.world.level.biome.MobSpawnSettings
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData

object BiomeFeaturePresets {
  fun addOresAndCaves(builder: BiomeGenerationSettings.Builder): BiomeGenerationSettings.Builder {
    return builder
//      .addCarver(GenerationStep.Carving.AIR, DatagenConfiguredCarvers.SAMPLE_CAVE)
//      .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, DataboxPlacedFeatures.BASALT_ADAMANTIUM_ORE_PLACED)
  }

  fun addCaveMobs(builder: MobSpawnSettings.Builder): MobSpawnSettings.Builder {
    return builder
      .addSpawn(MobCategory.MONSTER, SpawnerData(EntityType.FROG, 50, 1, 1))
      .addSpawn(MobCategory.MONSTER, SpawnerData(EntityType.CAVE_SPIDER, 50, 1, 1))
      .addSpawn(MobCategory.MONSTER, SpawnerData(EntityType.STRIDER, 50, 1, 1))
  }

  fun addPlanetZeroMobs(builder: MobSpawnSettings.Builder): MobSpawnSettings.Builder {
    return builder
      .addSpawn(MobCategory.AMBIENT, SpawnerData(EntityType.BAT, 10, 8, 8))
      // many more from other mods
      .addSpawn(MobCategory.MONSTER, SpawnerData(EntityType.HUSK, 80, 4, 4))
      .addSpawn(MobCategory.MONSTER, SpawnerData(EntityType.SKELETON, 100, 4, 4))
      .addSpawn(MobCategory.MONSTER, SpawnerData(EntityType.CREEPER, 100, 2, 4))
    // many more from other mods
  }

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