package com.dannbrown.databoxlib.registry.worldgen

import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BiomeTags
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.MobSpawnSettings
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraftforge.common.world.BiomeModifier
import net.minecraftforge.common.world.ForgeBiomeModifiers
import net.minecraftforge.registries.ForgeRegistries

abstract class AbstractBiomeModifiersGen {
  abstract val modId: String

  fun registerKey(name: String): ResourceKey<BiomeModifier> {
    return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation(modId, name))
  }

  abstract fun bootstrap(context: BootstapContext<BiomeModifier>)

  // @ Utils Functions
  fun addOre(biomes: HolderSet<Biome>, feature: Holder<PlacedFeature>): ForgeBiomeModifiers.AddFeaturesBiomeModifier {
    return ForgeBiomeModifiers.AddFeaturesBiomeModifier(biomes, HolderSet.direct(feature), GenerationStep.Decoration.UNDERGROUND_ORES)
  }

  fun addVegetation(biomes: HolderSet<Biome>, feature: Holder<PlacedFeature>): ForgeBiomeModifiers.AddFeaturesBiomeModifier {
    return ForgeBiomeModifiers.AddFeaturesBiomeModifier(biomes, HolderSet.direct(feature), GenerationStep.Decoration.VEGETAL_DECORATION)
  }

  fun addRawGeneration(biomes: HolderSet<Biome>, feature: Holder<PlacedFeature>): ForgeBiomeModifiers.AddFeaturesBiomeModifier {
    return ForgeBiomeModifiers.AddFeaturesBiomeModifier(biomes, HolderSet.direct(feature), GenerationStep.Decoration.RAW_GENERATION)
  }

  fun addMob(biomes: HolderSet<Biome>, spawnerData: List<MobSpawnSettings.SpawnerData>): ForgeBiomeModifiers.AddSpawnsBiomeModifier {
    return ForgeBiomeModifiers.AddSpawnsBiomeModifier(biomes, spawnerData)
  }
}