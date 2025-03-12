package com.dannbrown.deltaboxlib.registrate.registry

import com.dannbrown.deltaboxlib.registrate.util.AbstractBiome
import net.minecraft.world.level.biome.Biome
import net.minecraft.data.worldgen.BootstapContext as BootstrapContext

class BiomeRegistry(modId: String) {
  private val BIOMES: MutableList<AbstractBiome> = ArrayList()

  fun addBiome(biome: AbstractBiome) {
    BIOMES.add(biome)
  }

  fun bootstrap(context: BootstrapContext<Biome>) {
    for (biome in BIOMES) {
      biome.bootstrapBiome(context)
    }
  }
}