package com.dannbrown.deltaboxlib.registrate.registry

import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.sounds.SoundEvent

import java.util.function.Supplier

class SoundRegistry(val modId: String) {
  private val sounds = DeferredRegister.create(modId, Registries.SOUND_EVENT)
  private val soundVariants: MutableMap<String, Int> = mutableMapOf()
  var isRegistered = false

  fun <T : SoundEvent> register(id: String, variants: Int, blockSupplier: Supplier<T>): Supplier<T> {
    soundVariants[id] = variants
    return sounds.register(id, blockSupplier)
  }

  fun getVariants(): MutableMap<String, Int> {
    return soundVariants
  }

  fun build() {
    if (isRegistered) return
    isRegistered = true
    sounds.register()
  }
}