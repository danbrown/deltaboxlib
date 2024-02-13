package com.dannbrown.databoxlib.registry.worldgen

import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.placement.BiomeFilter
import net.minecraft.world.level.levelgen.placement.CountPlacement
import net.minecraft.world.level.levelgen.placement.InSquarePlacement
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.placement.PlacementModifier
import net.minecraft.world.level.levelgen.placement.RarityFilter

abstract class AbstractPlacedFeaturesGen {
  abstract val modId: String

  fun registerKey(name: String): ResourceKey<PlacedFeature> {
    return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation(modId, name))
  }
  fun register(
    context: BootstapContext<PlacedFeature>,
    key: ResourceKey<PlacedFeature>,
    configuration: Holder<ConfiguredFeature<*, *>>,
    modifiers: List<PlacementModifier>
  ) {
    context.register(key, PlacedFeature(configuration, modifiers))
  }

  abstract fun bootstrap(context: BootstapContext<PlacedFeature>)


  // Utility Functions
  fun orePlacement(placementModifier: PlacementModifier, modifier: PlacementModifier): List<PlacementModifier> {
    return listOf(placementModifier, InSquarePlacement.spread(), modifier, BiomeFilter.biome())
  }

  fun commonOrePlacement(pCount: Int, pHeightRange: PlacementModifier): List<PlacementModifier> {
    return orePlacement(CountPlacement.of(pCount), pHeightRange)
  }

  fun rareOrePlacement(pChance: Int, pHeightRange: PlacementModifier): List<PlacementModifier> {
    return orePlacement(RarityFilter.onAverageOnceEvery(pChance), pHeightRange)
  }
}