package com.dannbrown.deltaboxlib.registrate.registry

import com.dannbrown.deltaboxlib.registrate.util.PlacedFeaturesUtil
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.data.worldgen.BootstapContext as BootstrapContext

class PlacedFeatureRegistry(val modId: String) {
  private val PLACED_FEATURES: MutableMap<ResourceKey<PlacedFeature>, (ResourceKey<PlacedFeature>, BootstrapContext<PlacedFeature>, PlacedFeaturesUtil) -> Unit> =
    mutableMapOf()

  fun addPlacedFeature(
    name: String,
    consumer: (ResourceKey<PlacedFeature>, BootstrapContext<PlacedFeature>, PlacedFeaturesUtil) -> Unit
  ): ResourceKey<PlacedFeature> {
    val key = PlacedFeaturesUtil.registerKey(name, modId)
    PLACED_FEATURES[key] = consumer
    return key
  }

  fun bootstrapPlacedFeatures(context: BootstrapContext<PlacedFeature>) {
    PLACED_FEATURES.forEach { key, consumer ->
      consumer(key, context, PlacedFeaturesUtil)
    }
  }
}