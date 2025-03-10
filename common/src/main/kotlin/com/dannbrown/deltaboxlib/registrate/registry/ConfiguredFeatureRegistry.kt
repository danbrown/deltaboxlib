package com.dannbrown.deltaboxlib.registrate.registry

import com.dannbrown.deltaboxlib.registrate.util.ConfiguredFeaturesUtil
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.data.worldgen.BootstapContext as BootstrapContext

class ConfiguredFeatureRegistry(val modId: String) {
  private val CONFIGURED_FEATURES: MutableMap<ResourceKey<ConfiguredFeature<*, *>>, (ResourceKey<ConfiguredFeature<*, *>>, BootstrapContext<ConfiguredFeature<*, *>>, ConfiguredFeaturesUtil) -> Unit> =
    mutableMapOf()

  fun addConfiguredfeature(
    name: String,
    consumer: (ResourceKey<ConfiguredFeature<*, *>>, BootstrapContext<ConfiguredFeature<*, *>>, ConfiguredFeaturesUtil) -> Unit
  ): ResourceKey<ConfiguredFeature<*, *>> {
    val key = ConfiguredFeaturesUtil.registerKey(name, modId)
    CONFIGURED_FEATURES[key] = consumer
    return key
  }

  fun getConfiguredfeatures(): List<ResourceKey<ConfiguredFeature<*, *>>> {
    return CONFIGURED_FEATURES.map { it.key }
  }

  fun bootstrapConfiguredfeatures(context: BootstrapContext<ConfiguredFeature<*, *>>) {
    CONFIGURED_FEATURES.forEach { key, consumer ->
      consumer(key, context, ConfiguredFeaturesUtil)
      DeltaboxUtil.LOGGER.info("Registering ${key} configured feature for ${modId}")
    }
  }
}