package com.dannbrown.databoxlib.lib

import com.dannbrown.databoxlib.registry.DataboxRegistrate
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.contents.TranslatableContents

class LibLang {
  companion object {
    fun asId(name: String): String {
      return name.lowercase()
    }

    fun nonPluralId(name: String): String {
      val asId = asId(name)
      return if (asId.endsWith("s")) asId.substring(0, asId.length - 1) else asId
    }

    fun addKey(key: String, en_us: String, registrate: DataboxRegistrate): TranslatableContents {
      registrate.addRawLang(key, en_us)
      return TranslatableContents(key, null, emptyArray())
    }

    @JvmStatic
    fun translateDirect(key: String): MutableComponent {
      return Component.translatable(key)
    }
  }


}