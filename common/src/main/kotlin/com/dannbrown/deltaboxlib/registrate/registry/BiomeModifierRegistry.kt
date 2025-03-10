package com.dannbrown.deltaboxlib.registrate.registry

import com.dannbrown.deltaboxlib.registrate.providers.biomeModifier.BiomeModifierCodec
import com.dannbrown.deltaboxlib.registrate.providers.biomeModifier.BiomeSpawnCodec

class BiomeModifierRegistry(modId: String) {
  private val BIOME_MODIFIER_FACTORIES: MutableMap<String, BiomeModifierCodec> = mutableMapOf()
  private val BIOME_SPAWN_FACTORIES: MutableMap<String, BiomeSpawnCodec> = mutableMapOf()

  fun addBiomeModifier(name: String, modifier: BiomeModifierCodec) {
    BIOME_MODIFIER_FACTORIES[name] = modifier
  }

  fun getBiomeModifiers(): Map<String, BiomeModifierCodec> {
    return BIOME_MODIFIER_FACTORIES
  }

  fun addBiomeSpawn(name: String, spawn: BiomeSpawnCodec) {
    BIOME_SPAWN_FACTORIES[name] = spawn
  }

  fun getBiomeSpawns(): Map<String, BiomeSpawnCodec> {
    return BIOME_SPAWN_FACTORIES
  }
}