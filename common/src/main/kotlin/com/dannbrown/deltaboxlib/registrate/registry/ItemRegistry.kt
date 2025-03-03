package com.dannbrown.deltaboxlib.registrate.registry

import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import java.util.function.Supplier

class ItemRegistry(modId: String) {
  private val items = DeferredRegister.create(modId, Registries.ITEM)

  fun <T : Item> register(id: String, itemSupplier: Supplier<T>): Supplier<T> {
    return items.register(id, itemSupplier)
  }

  fun build() {
    items.register()
  }
}