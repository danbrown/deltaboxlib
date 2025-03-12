package com.dannbrown.deltaboxlib.registrate.registry

import com.dannbrown.deltaboxlib.registrate.util.AbstractDimension
import net.minecraft.world.level.dimension.DimensionType
import net.minecraft.world.level.dimension.LevelStem
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings
import net.minecraft.data.worldgen.BootstapContext as BootstrapContext

class DimensionRegistry(modId: String) {
  private val DIMENSIONS: MutableList<AbstractDimension> = ArrayList()

  fun addDimension(dimension: AbstractDimension) {
    DIMENSIONS.add(dimension)
  }

  fun getDimensions(): MutableList<AbstractDimension> {
    return DIMENSIONS
  }

  fun bootstrapNoise(context: BootstrapContext<NoiseGeneratorSettings>) {
    for (dimension in DIMENSIONS) {
      dimension.bootstrapNoise(context)
    }
  }

  fun bootstrapStem(context: BootstrapContext<LevelStem>) {
    for (dimension in DIMENSIONS) {
      dimension.bootstrapStem(context)
    }
  }

  fun bootstrapType(context: BootstrapContext<DimensionType>) {
    for (dimension in DIMENSIONS) {
      dimension.bootstrapType(context)
    }
  }
}