package com.dannbrown.deltaboxlib.registrate.registry

import com.dannbrown.deltaboxlib.registrate.providers.biomeModifier.BiomeModifierCodec

class BiomeModifierRegistry(modId: String) {
  private val BIOME_MODIFIER_FACTORIES: MutableMap<String, BiomeModifierCodec> = mutableMapOf()

  fun addBiomeModifier(name: String, modifier: BiomeModifierCodec) {
    BIOME_MODIFIER_FACTORIES[name] = modifier
  }

  fun getBiomeModifiers(): Map<String, BiomeModifierCodec> {
    return BIOME_MODIFIER_FACTORIES
  }
}