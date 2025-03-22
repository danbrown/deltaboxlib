package com.dannbrown.deltaboxlib.registrate.registry

import net.minecraft.core.dispenser.DispenseItemBehavior

class DispenserBehaviorRegistry(modId: String) {
  private val entries: MutableMap<ItemEntry<*>, DispenseItemBehavior> = mutableMapOf()

  fun register(item: ItemEntry<*>, behavior: DispenseItemBehavior) {
    entries[item] = behavior
  }

  fun getRegistries(): MutableMap<ItemEntry<*>, DispenseItemBehavior> {
    return entries
  }
}