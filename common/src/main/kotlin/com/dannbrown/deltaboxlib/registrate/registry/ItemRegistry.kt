package com.dannbrown.deltaboxlib.registrate.registry

import com.dannbrown.deltaboxlib.registrate.builders.ItemBuilder
import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import java.util.function.Supplier

class ItemRegistry(modId: String) {
  private val items = DeferredRegister.create(modId, Registries.ITEM)
  val entries = mutableListOf<ItemBuilder<out Item>>()
  var isRegistered = false

  fun <T : Item> register(id: String, itemSupplier: Supplier<T>, itemBuilder: ItemBuilder<out Item>): Supplier<T> {
    entries.add(itemBuilder)
    return items.register(id, itemSupplier)
  }

  fun build() {
    if (isRegistered) return
    isRegistered = true
    items.register()
  }
}