package com.dannbrown.databoxlib.lib

import com.dannbrown.databoxlib.ProjectContent
import com.simibubi.create.foundation.utility.Components
import com.simibubi.create.foundation.utility.Lang.resolveBuilders
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

    fun addKey(key: String, en_us: String): TranslatableContents {
      ProjectContent.REGISTRATE.addRawLang(key, en_us)
      return TranslatableContents(key, null, emptyArray())
    }

    //
    @JvmStatic
    fun translateDirect(key: String, vararg args: Any): MutableComponent {
      return Components.translatable(key, *resolveBuilders(args))
    }
//
//    // check if a translation exists for a given key
//    @JvmStatic
//    fun hasTranslation(key: String): Boolean {
//      return TranslatableContents(key).key != key
//    }
  }

  object TRANSLATIONS {
    @JvmField
    val FORMULA_FORMAT: TranslatableContents = addKey("menu." + ProjectContent.MOD_ID + ".formula_format", "Chemical Formula: %1\$s")
  }
}