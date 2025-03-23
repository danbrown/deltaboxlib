package com.dannbrown.deltaboxlib.registrate.registry

import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import java.util.function.Supplier

class FeatureRegistry(modId: String) {
  private val features = DeferredRegister.create(modId, Registries.FEATURE)
  var isRegistered = false

  fun <T : FeatureConfiguration> register(
    id: String,
    supplier: Supplier<Feature<T>>
  ): Supplier<Feature<T>> {
    return features.register(id, supplier)
  }

  fun build() {
    if (isRegistered) return
    isRegistered = true
    features.register()
  }
}