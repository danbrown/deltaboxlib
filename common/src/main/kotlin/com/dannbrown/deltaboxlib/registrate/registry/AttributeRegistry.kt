package com.dannbrown.deltaboxlib.registrate.registry

import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.world.entity.ai.attributes.Attribute
import java.util.function.Supplier

class AttributeRegistry(modId: String) {
  private val attributes = DeferredRegister.create(modId, Registries.ATTRIBUTE)
  var isRegistered = false

  fun register(
    id: String,
    supplier: Supplier<Attribute>
  ): Supplier<Attribute> {
    return attributes.register(id, supplier)
  }

  fun build() {
    if (isRegistered) return
    isRegistered = true
    attributes.register()
  }
}