package com.dannbrown.deltaboxlib.registrate.registry

import com.dannbrown.deltaboxlib.registrate.builders.ItemBuilder
import net.minecraft.world.item.Item
import java.util.function.Supplier

data class ItemEntry<T : Item>(private val builder: ItemBuilder<T>?) {
  constructor(item: Supplier<T>) : this(null) {
    setupItem = item
  }

  private var setupItem: Supplier<T>? = null

  fun getBuilder(): ItemBuilder<T> {
    if (builder == null) throw NoSuchFieldError("Cannot get the builder from an external block entry.")
    return builder
  }

  fun get(): T {
    return if (setupItem !== null) setupItem!!.get() else if (builder !== null) builder.getItem()
      .get() else throw throw NoSuchFieldError("This block entry is invalid")
  }

  companion object {
    fun from(item: Item): ItemEntry<*> {
      return ItemEntry<Item> { item }
    }

    fun from(item: Supplier<Item>): ItemEntry<*> {
      return ItemEntry<Item>(item)
    }
  }
}