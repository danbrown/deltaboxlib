package com.dannbrown.databoxlib.registry.generators

import com.dannbrown.databoxlib.registry.worldgen.AbstractDimension
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.biome.MultiNoiseBiomeSource
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterLists
import net.minecraft.world.level.biome.TheEndBiomeSource
import net.minecraft.world.level.dimension.BuiltinDimensionTypes
import net.minecraft.world.level.dimension.DimensionType
import net.minecraft.world.level.dimension.LevelStem
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings
import net.minecraft.world.level.levelgen.presets.WorldPreset
import java.util.Map

object WorldPresetGen {
  fun createFromDimension(context: BootstapContext<WorldPreset>, dimension: AbstractDimension): WorldPreset {
    val biomeRegistry = context.lookup(Registries.BIOME)
    val dimTypes = context.lookup(Registries.DIMENSION_TYPE)
    val noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS)
    val multiNoiseBiomeSourceParameterLists = context.lookup(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST)
    // create nether
    val netherTypeHolder: Holder<DimensionType> = dimTypes.getOrThrow(BuiltinDimensionTypes.NETHER)
    val netherNoiseHolder: Holder<NoiseGeneratorSettings> = noiseGenSettings.getOrThrow(NoiseGeneratorSettings.NETHER)
    val netherBiomeHolder: Holder.Reference<MultiNoiseBiomeSourceParameterList> = multiNoiseBiomeSourceParameterLists.getOrThrow(MultiNoiseBiomeSourceParameterLists.NETHER)
    val netherStem = LevelStem(netherTypeHolder, NoiseBasedChunkGenerator(MultiNoiseBiomeSource.createFromPreset(netherBiomeHolder), netherNoiseHolder))
    // create end
    val endTypeHolder: Holder<DimensionType> = dimTypes.getOrThrow(BuiltinDimensionTypes.END)
    val endNoiseHolder: Holder<NoiseGeneratorSettings> = noiseGenSettings.getOrThrow(NoiseGeneratorSettings.END)
    val endStem = LevelStem(endTypeHolder, NoiseBasedChunkGenerator(TheEndBiomeSource.create(biomeRegistry), endNoiseHolder))
    // create overworld based in dimension
    val levelStem = LevelStem(
      dimTypes.getOrThrow(dimension.DIMENSION_TYPE),
      NoiseBasedChunkGenerator(
        dimension.buildBiomeSource(biomeRegistry),
        noiseGenSettings.getOrThrow(dimension.NOISE_SETTINGS)
      )
    )

    return WorldPreset(
      Map.of<ResourceKey<LevelStem>, LevelStem>(
        LevelStem.OVERWORLD,
        levelStem,
        LevelStem.NETHER,
        netherStem,
        LevelStem.END,
        endStem
      )
    )
  }
}