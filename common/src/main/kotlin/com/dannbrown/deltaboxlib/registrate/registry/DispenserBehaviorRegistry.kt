package com.dannbrown.deltaboxlib.registrate.registry

import net.minecraft.core.dispenser.DispenseItemBehavior
import net.minecraft.world.level.ItemLike
import java.util.function.Supplier

class DispenserBehaviorRegistry(modId: String) {
  private val entries: MutableMap<Supplier<ItemLike>, DispenseItemBehavior> = mutableMapOf()

  fun register(item: Supplier<ItemLike>, behavior: DispenseItemBehavior) {
    entries[item] = behavior
  }

  fun getRegistries(): MutableMap<Supplier<ItemLike>, DispenseItemBehavior> {
    return entries
  }
}