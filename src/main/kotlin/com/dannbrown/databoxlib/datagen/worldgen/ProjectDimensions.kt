package com.dannbrown.databoxlib.datagen.worldgen

import com.dannbrown.databoxlib.content.utils.AbstractDimension
import com.dannbrown.databoxlib.content.worldgen.dimension.PlanetZeroDimension
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.world.level.dimension.DimensionType
import net.minecraft.world.level.dimension.LevelStem
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings

object ProjectDimensions {
  val DIMENSIONS: MutableList<AbstractDimension> = ArrayList()

  init {
    registerDimension(PlanetZeroDimension)
  }

  fun registerDimension(dim: AbstractDimension) {
    DIMENSIONS.add(dim) // add the dimension to the list
  }

  fun bootstrapType(context: BootstapContext<DimensionType>) {
    for (dimension in DIMENSIONS) {
      dimension.bootstrapType(context)
    }
  }

  fun bootstrapStem(context: BootstapContext<LevelStem>) {
    for (dimension in DIMENSIONS) {
      dimension.bootstrapStem(context)
    }
  }

  fun bootstrapNoise(context: BootstapContext<NoiseGeneratorSettings>) {
    for (dimension in DIMENSIONS) {
      dimension.bootstrapNoise(context)
    }
  }
}