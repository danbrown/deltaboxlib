package com.dannbrown.deltaboxlib.registrate.registry

import com.dannbrown.deltaboxlib.registrate.types.AdvancementSupplier
import com.dannbrown.deltaboxlib.registrate.util.AdvancementUtil
import net.minecraft.advancements.Advancement
import java.util.function.Supplier

class AdvancementRegistry(val modId: String) {
  private val ADVANCEMENTS: MutableMap<String, Supplier<Advancement>> = mutableMapOf()

  fun addAdvancement(
    name: String,
    advancement: AdvancementSupplier
  ): Supplier<Advancement> {
    val adv = Supplier { advancement.invoke(name, AdvancementUtil(modId), Advancement.Builder.advancement()) }
    ADVANCEMENTS[name] = adv
    return adv
  }

  fun getAdvancements(): MutableMap<String, Supplier<Advancement>> {
    return ADVANCEMENTS
  }
}