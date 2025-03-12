package com.dannbrown.deltaboxlib.registrate.registry

import com.dannbrown.deltaboxlib.registrate.util.AbstractDimension
import net.minecraft.world.level.dimension.DimensionType
import net.minecraft.world.level.dimension.LevelStem
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings
import net.minecraft.data.worldgen.BootstapContext as BootstrapContext

class DimensionRegistry(modId: String) {
  private val DIMENSIONS: MutableList<AbstractDimension> = ArrayList()

  fun addDimension(biome: AbstractDimension) {
    DIMENSIONS.add(biome)
  }

  fun bootstrapNoise(context: BootstrapContext<NoiseGeneratorSettings>) {
    for (biome in DIMENSIONS) {
      biome.bootstrapNoise(context)
    }
  }

  fun bootstrapStem(context: BootstrapContext<LevelStem>) {
    for (biome in DIMENSIONS) {
      biome.bootstrapStem(context)
    }
  }

  fun bootstrapType(context: BootstrapContext<DimensionType>) {
    for (biome in DIMENSIONS) {
      biome.bootstrapType(context)
    }
  }
}