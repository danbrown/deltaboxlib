package com.dannbrown.deltaboxlib.registrate.registry

import com.dannbrown.deltaboxlib.init.DeltaboxLibMod
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

class LangRegistry(modId: String) {
  val langEntries = mutableMapOf<String, String>()

  fun register(langKey: String, langValue: String): Pair<String, String> {
    val langEntry = Pair(langKey, langValue)
    langEntries[langKey] = langValue
    return langEntry
  }

  fun build(): Map<String, String> {
    return langEntries
  }

  companion object {
    fun getTooltipKey(modId: String?, itemId: String): String {
      return "tooltip." + DeltaboxLibMod.MOD_ID + (if(modId !== null) ".$modId" else "") + "." + itemId
    }

    fun translateDirect(key: String): MutableComponent {
      return Component.translatable(key)
    }
  }
}