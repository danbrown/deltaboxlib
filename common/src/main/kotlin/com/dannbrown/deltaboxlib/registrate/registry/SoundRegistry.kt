package com.dannbrown.deltaboxlib.registrate.registry

import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.sounds.SoundEvent

import java.util.function.Supplier

class SoundRegistry(modId: String) {
  private val sounds = DeferredRegister.create(modId, Registries.SOUND_EVENT)
  var isRegistered = false

  fun <T : SoundEvent> register(id: String, blockSupplier: Supplier<T>): Supplier<T> {
    return sounds.register(id, blockSupplier)
  }

  fun build() {
    if (isRegistered) return
    isRegistered = true
    sounds.register()
  }
}