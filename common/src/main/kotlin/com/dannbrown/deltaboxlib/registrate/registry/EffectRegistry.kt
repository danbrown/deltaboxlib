package com.dannbrown.deltaboxlib.registrate.registry

import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.world.effect.MobEffect
import java.util.function.Supplier

class EffectRegistry(modId: String) {
  private val effects = DeferredRegister.create(modId, Registries.MOB_EFFECT)
  var isRegistered = false

  fun register(
    id: String,
    supplier: Supplier<MobEffect>
  ): Supplier<MobEffect> {
    return effects.register(id, supplier)
  }

  fun build() {
    if (isRegistered) return
    isRegistered = true
    effects.register()
  }
}