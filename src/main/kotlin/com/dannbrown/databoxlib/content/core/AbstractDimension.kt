package com.dannbrown.databoxlib.content.utils

import com.dannbrown.databoxlib.datagen.planets.PlanetCodec
import net.minecraft.core.HolderGetter
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.BiomeSource
import net.minecraft.world.level.dimension.DimensionType
import net.minecraft.world.level.dimension.LevelStem
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings

abstract class AbstractDimension {
  abstract val dimensionId: String
  abstract val LEVEL: ResourceKey<Level>
  abstract val LEVEL_STEM: ResourceKey<LevelStem>
  abstract val DIMENSION_TYPE: ResourceKey<DimensionType>
  abstract val NOISE_SETTINGS: ResourceKey<NoiseGeneratorSettings>
  abstract fun bootstrapType(context: BootstapContext<DimensionType>)
  abstract fun bootstrapStem(context: BootstapContext<LevelStem>)
  abstract fun bootstrapNoise(context: BootstapContext<NoiseGeneratorSettings>)
  abstract fun buildBiomeSource(biomes: HolderGetter<Biome>): BiomeSource
  abstract val GRAVITY: Float
  abstract val TEMPERATURE: Short
  abstract val PRESSURE: Short
  abstract val ATMOSPHERE: PlanetCodec.AtmosphereType
  abstract val SKYBOX: PlanetCodec.SkyBoxProperties?
}