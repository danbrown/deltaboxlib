package com.dannbrown.deltaboxlib.content.worldgen.dimension

import com.dannbrown.deltaboxlib.content.worldgen.biome.SpaceVoidBiome
import com.dannbrown.deltaboxlib.init.DeltaboxBlocks
import com.dannbrown.deltaboxlib.init.DeltaboxLibMod
import com.dannbrown.deltaboxlib.registrate.util.AbstractDimension
import com.google.common.collect.ImmutableList
import com.mojang.datafixers.util.Pair
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.BiomeSource
import net.minecraft.world.level.biome.Climate
import net.minecraft.world.level.biome.MultiNoiseBiomeSource
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.dimension.DimensionType
import net.minecraft.world.level.dimension.LevelStem
import net.minecraft.world.level.levelgen.DensityFunctions
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings
import net.minecraft.world.level.levelgen.NoiseRouter
import net.minecraft.world.level.levelgen.NoiseSettings
import net.minecraft.world.level.levelgen.SurfaceRules
import java.util.*

object DeepSpaceDimension : AbstractDimension() {
  override val dimensionId: String = "deep_space"
  override val LEVEL = ResourceKey.create(Registries.DIMENSION, ResourceLocation(DeltaboxLibMod.MOD_ID, dimensionId))
  override val LEVEL_STEM =
    ResourceKey.create(Registries.LEVEL_STEM, ResourceLocation(DeltaboxLibMod.MOD_ID, dimensionId))
  override val DIMENSION_TYPE =
    ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation(DeltaboxLibMod.MOD_ID, dimensionId))
  override val NOISE_SETTINGS =
    ResourceKey.create(Registries.NOISE_SETTINGS, ResourceLocation(DeltaboxLibMod.MOD_ID, dimensionId))


  // BOOTSTRAP
  override fun bootstrapType(context: BootstapContext<DimensionType>) {
    context.register(
      DIMENSION_TYPE, DimensionType(
        OptionalLong.of(16000), // fixed time
        true,  // skylight
        false,  // ceiling
        false,  // ultrawarm
        true,  // natural
        1.0,  // coordinate scale
        true,  // bed works
        false,  // respawn anchor works
        -64,  // Minimum Y Level
        384,  // Height + Min Y = Max Y
        384,  // Logical Height
        BlockTags.INFINIBURN_OVERWORLD,  // infiniburn
        ResourceLocation(DeltaboxLibMod.MOD_ID, dimensionId),  // DimensionRenderInfo
        0.0f,  // ambient light
        DimensionType.MonsterSettings(false, false, UniformInt.of(0, 7), 0)
      )
    )
  }

  override fun bootstrapStem(context: BootstapContext<LevelStem>) {
    val biomeRegistry = context.lookup(Registries.BIOME)
    val dimTypes = context.lookup(Registries.DIMENSION_TYPE)
    val noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS)
    context.register(
      LEVEL_STEM, LevelStem(
        dimTypes.getOrThrow(DIMENSION_TYPE),
        NoiseBasedChunkGenerator(
          buildBiomeSource(biomeRegistry),
          noiseGenSettings.getOrThrow(NOISE_SETTINGS)
        )
      )
    )
  }

  override fun bootstrapNoise(context: BootstapContext<NoiseGeneratorSettings>) {
    val functions = context.lookup(Registries.DENSITY_FUNCTION)
    val noises = context.lookup(Registries.NOISE)

    context.register(
      NOISE_SETTINGS, NoiseGeneratorSettings(
        // Noise
        NoiseSettings.create(0, 256, 2, 1),
        // Default Block
        DeltaboxBlocks.ADAMANTIUM_BLOCK.get().defaultBlockState(),
        // Default Fluid
        Blocks.AIR.defaultBlockState(),
        // Noise Router
        NoiseRouter(
          DensityFunctions.constant(0.0),
          DensityFunctions.constant(0.0),
          DensityFunctions.constant(0.0),
          DensityFunctions.constant(0.0),
          DensityFunctions.constant(0.0),
          DensityFunctions.constant(0.0),
          DensityFunctions.constant(0.0),
          DensityFunctions.constant(0.0),
          DensityFunctions.constant(0.0),
          DensityFunctions.constant(0.0),
          DensityFunctions.constant(0.0),
          DensityFunctions.constant(0.0),
          DensityFunctions.constant(0.0),
          DensityFunctions.constant(0.0),
          DensityFunctions.constant(0.0),
        ),
        // surface rules builder
        SurfaceRules.state(Blocks.AIR.defaultBlockState()),
        // spawn targets
        listOf<Climate.ParameterPoint>(
        ),
        // sea level
        0,
        // disable mob generation
        false,
        // aquifers enabled
        false,
        // ore veins enabled
        false,
        // use legacy random source
        false
      )
    )
  }

  // build the biome source argument for the dimension type builder
  override fun buildBiomeSource(biomes: HolderGetter<Biome>): BiomeSource {
    return MultiNoiseBiomeSource.createFromList(
      Climate.ParameterList(
        ImmutableList.of(
          Pair.of(
            Climate.parameters(
              0f, // temperature
              0f, // humidity
              0f, // continentalness
              0f, // erosion
              0f,// depth
              0f, // weirdness
              0f  // offset
            ),
            biomes.getOrThrow(
              SpaceVoidBiome.BIOME_KEY
            )
          ),
          // -----
        )
      )
    )
  }
}