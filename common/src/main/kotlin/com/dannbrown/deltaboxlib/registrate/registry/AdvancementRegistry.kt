package com.dannbrown.deltaboxlib.registrate.registry

import com.dannbrown.deltaboxlib.registrate.util.AdvancementUtil
import net.minecraft.advancements.Advancement

class AdvancementRegistry(val modId: String) {
  private val ADVANCEMENTS: MutableMap<String, Advancement> = mutableMapOf()

  fun addAdvancement(
    name: String,
    advancement: (String, AdvancementUtil, Advancement.Builder) -> Advancement
  ): Advancement {
    val adv = advancement.invoke(name, AdvancementUtil(modId), Advancement.Builder.advancement())
    ADVANCEMENTS[name] = adv
    return adv
  }

  fun getAdvancements(): MutableMap<String, Advancement> {
    return ADVANCEMENTS
  }
}