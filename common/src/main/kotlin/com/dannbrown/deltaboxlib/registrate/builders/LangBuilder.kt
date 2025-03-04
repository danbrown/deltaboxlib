package com.dannbrown.deltaboxlib.registrate.builders

import com.dannbrown.deltaboxlib.init.DeltaboxLibMod
import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.registry.LangRegistry

class LangBuilder(_registrate: AbstractDeltaboxRegistrate, val _modId: String): AbstractBuilder(_registrate) {
  fun formula(name: String, phrase: String) : LangBuilder {
    return addRawLang("formula.${_modId}.$name", phrase)
  }

  fun item(name: String, phrase: String) : LangBuilder {
    return addRawLang("item.${_modId}.$name", phrase)
  }

  fun block(name: String, phrase: String) : LangBuilder {
    return addRawLang("block.${_modId}.$name", phrase)
  }

  fun tooltip(name: String, phrase: String) : LangBuilder {
    return addRawLang(LangRegistry.getTooltipKey(_modId, name), phrase)
  }

  fun genericTooltip(name: String, phrase: String) : LangBuilder {
    return addRawLang(LangRegistry.getTooltipKey(null, name), phrase)
  }

  fun creativeTab(name: String, phrase: String) : LangBuilder {
    return addRawLang("itemGroup.${_modId}.$name", phrase)
  }

  fun potion(name: String, phrase: String) : LangBuilder {
    return addRawLang("item.minecraft.potion.effect.$name", "Potion of $phrase")
      .addRawLang("item.minecraft.splash_potion.effect.$name", "Splash Potion of $phrase")
      .addRawLang("item.minecraft.lingering_potion.effect.$name", "Lingering Potion of $phrase")
      .addRawLang("item.minecraft.tipped_arrow.effect.$name", "Arrow of $phrase")
  }

  fun advancement(name: String, title: String, description: String) : LangBuilder {
    return addRawLang("advancements.${_modId}.$name.title", title)
      .addRawLang("advancements.${_modId}.$name.description", description)
  }

  fun effect(name: String, phrase: String) : LangBuilder {
    return addRawLang("effect.${_modId}.$name", phrase)
  }

  fun deathMessage(name: String, phrase: String) : LangBuilder {
    return addRawLang("death.attack.$name", phrase)
  }

  fun goggles(name: String, phrase: String) : LangBuilder {
    return addRawLang("${_modId}.gui.goggles.$name", phrase)
  }

  fun biome(name: String, phrase: String) : LangBuilder {
    return addRawLang("biome.${_modId}.$name", phrase)
  }

  fun sound(name: String, phrase: String) : LangBuilder {
    return addRawLang("sound.${_modId}.$name", phrase)
  }

  fun dimension(name: String, phrase: String) : LangBuilder {
    return addRawLang("dimension.${_modId}.$name", phrase)
  }

  fun worldPreset(name: String, phrase: String) : LangBuilder {
    return addRawLang("generator.${_modId}.$name", phrase)
  }

  fun paintingVariant(name: String, phrase: String, author: String) : LangBuilder {
    return addRawLang("painting.${_modId}.$name.title", phrase)
      .addRawLang("painting.${_modId}.$name.author", author)
  }

  fun entity(name: String, phrase: String) : LangBuilder {
    return addRawLang("entity.${_modId}.$name", phrase)
  }

  fun addRawLang(key: String, phrase: String) : LangBuilder {
    registrate.langRegistry.register(key, phrase)
    return this
  }

  fun register() {
    registrate.langRegistry.build()
  }
}