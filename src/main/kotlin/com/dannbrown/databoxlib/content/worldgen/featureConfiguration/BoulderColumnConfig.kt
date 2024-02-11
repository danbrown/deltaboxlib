package com.dannbrown.databoxlib.content.worldgen.featureConfiguration

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.tags.BlockTags
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import java.util.function.Supplier


class BoulderColumnConfig(
  val blockProvider: BlockStateProvider,
  val middleBlockProvider: BlockStateProvider,
  val topBlockProvider: BlockStateProvider,
  val stackHeight: IntProvider,
  val radiusSettings: RadiusSettings,
  val radiusDivisorPerStack: Double,
  val noiseFrequency: Float,
  val predicate: List<BlockPredicate>,
//  val fluidStartY: Int,
//  val fluidState: FluidState,
  val noise2DChance: Double,
  val radiusMatcher: RadiusMatcher,
  val checkSquareDistance: Boolean,
  val useScaledNoiseHeight: Boolean,
  val pointed: Boolean,
  val verifiesHeight: Boolean,
  val belowSurfaceDepth: IntProvider,
  val spawningFeatures: HolderSet<PlacedFeature>
) :
  FeatureConfiguration {
  class RadiusSettings(xRadius: IntProvider, yRadius: IntProvider, upperHalfAdditional: Int, zRadius: IntProvider) {
    val xRadius: IntProvider
    val yRadius: IntProvider
    val upperHalfAdditional: Int
    val zRadius: IntProvider

    init {
      this.xRadius = xRadius
      this.yRadius = yRadius
      this.upperHalfAdditional = upperHalfAdditional
      this.zRadius = zRadius
    }

    companion object {
      var CODEC: Codec<RadiusSettings> =
        RecordCodecBuilder.create { builder: RecordCodecBuilder.Instance<RadiusSettings> ->
          builder.group(
            IntProvider.CODEC.fieldOf("x_radius")
              .forGetter { config: RadiusSettings -> config.xRadius },
            IntProvider.CODEC.fieldOf("y_radius")
              .forGetter { config: RadiusSettings -> config.yRadius },
            Codec.INT.fieldOf("upper_half_additional").orElse(0)
              .forGetter { config: RadiusSettings -> config.upperHalfAdditional },
            IntProvider.CODEC.fieldOf("z_radius")
              .forGetter { config: RadiusSettings -> config.zRadius }
          ).apply(
            builder
          ) { xRadius: IntProvider, yRadius: IntProvider, upperHalfAdditional: Int, zRadius: IntProvider ->
            RadiusSettings(
              xRadius,
              yRadius,
              upperHalfAdditional,
              zRadius
            )
          }
        }
    }
  }

  class Builder {
    private var blockProvider = DEFAULT
    private var middleBlockProvider = DEFAULT
    private var topBlockProvider = DEFAULT
    private var stackHeight: IntProvider = ConstantInt.of(1)
    private var radiusSettings = RadiusSettings(UniformInt.of(1, 3), UniformInt.of(1, 3), 0, UniformInt.of(1, 3))
    private var radiusDivisorPerStack = 1.0
    private var noiseFrequency = 0.04f
    private var predicate: List<BlockPredicate> = listOf(BlockPredicate.matchesTag(BlockTags.DIRT))
//    private var fluidStartY = Int.MIN_VALUE
//    private var fluidState = Fluids.EMPTY.defaultFluidState()
    private var noise2DChance = 0.25
    private var radiusMatcher: RadiusMatcher = RadiusMatcher.NONE
    private var checkSquareDistance = true
    private var useScaledNoiseHeight = true
    private var pointed = false
    private var verifiesHeight = false
    private var belowSurfaceDepth: IntProvider = ConstantInt.of(Int.MAX_VALUE)
    private var spawningFeatures: List<Holder<PlacedFeature>> = ArrayList()
    fun setBlockProvider(blockProvider: BlockStateProvider): Builder {
      this.blockProvider = blockProvider
      return this
    }

    fun setMiddleBlockProvider(middleBlockProvider: BlockStateProvider): Builder {
      this.middleBlockProvider = middleBlockProvider
      return this
    }

    fun setTopBlockProvider(topBlockProvider: BlockStateProvider): Builder {
      this.topBlockProvider = topBlockProvider
      return this
    }

    fun setStackHeight(stackHeight: IntProvider): Builder {
      this.stackHeight = stackHeight
      return this
    }

    fun setRadiusSettings(radiusSettings: RadiusSettings): Builder {
      this.radiusSettings = radiusSettings
      return this
    }

    fun withRadiusDivisorPerStack(radiusDivisorPerStack: Double): Builder {
      this.radiusDivisorPerStack = radiusDivisorPerStack
      return this
    }

    fun withNoiseFrequency(noiseFrequency: Float): Builder {
      this.noiseFrequency = noiseFrequency
      return this
    }

    fun setPlacementPredicates(predicate: List<BlockPredicate>): Builder {
      this.predicate = predicate
      return this
    }

//    fun withFluidStartY(fluidStartY: Int): Builder {
//      this.fluidStartY = fluidStartY
//      return this
//    }

//    fun withFluidState(fluidState: FluidState): Builder {
//      this.fluidState = fluidState
//      return this
//    }

    fun withNoise2DChance(noise2DChance: Double): Builder {
      this.noise2DChance = noise2DChance
      return this
    }

    fun withRadiusMatcher(radiusMatcher: RadiusMatcher): Builder {
      this.radiusMatcher = radiusMatcher
      return this
    }

    fun withCheckSquareDistance(checkSquareDistance: Boolean): Builder {
      this.checkSquareDistance = checkSquareDistance
      return this
    }

    fun withScaledHeight(useScaledNoiseHeight: Boolean): Builder {
      this.useScaledNoiseHeight = useScaledNoiseHeight
      return this
    }

    fun setPointed(pointed: Boolean): Builder {
      this.pointed = pointed
      return this
    }

    fun withVerifiesHeight(verifiesHeight: Boolean): Builder {
      this.verifiesHeight = verifiesHeight
      return this
    }

    fun withBelowSurfaceDepth(belowSurfaceDepth: IntProvider): Builder {
      this.belowSurfaceDepth = belowSurfaceDepth
      return this
    }

    fun withSpawningFeatures(spawningFeatures: List<Holder<PlacedFeature>>): Builder {
      this.spawningFeatures = spawningFeatures
      return this
    }

    fun copy(copy: Supplier<out BoulderColumnConfig>): Builder {
      return copy(copy.get())
    }

    fun copy(copy: BoulderColumnConfig): Builder {
      blockProvider = copy.blockProvider
      middleBlockProvider = copy.middleBlockProvider
      topBlockProvider = copy.topBlockProvider
      stackHeight = copy.stackHeight
      radiusSettings = copy.radiusSettings
      radiusDivisorPerStack = copy.radiusDivisorPerStack
      noiseFrequency = copy.noiseFrequency
      predicate = copy.predicate
//      fluidStartY = copy.fluidStartY
//      fluidState = copy.fluidState
      noise2DChance = copy.noise2DChance
      radiusMatcher = copy.radiusMatcher
      checkSquareDistance = copy.checkSquareDistance
      useScaledNoiseHeight = copy.useScaledNoiseHeight
      belowSurfaceDepth = copy.belowSurfaceDepth
      pointed = copy.pointed
      verifiesHeight = copy.verifiesHeight
      spawningFeatures = copy.spawningFeatures.stream().toList()
      return this
    }

    fun build(): BoulderColumnConfig {
      return BoulderColumnConfig(
        blockProvider,
        middleBlockProvider,
        topBlockProvider,
        stackHeight,
        radiusSettings,
        radiusDivisorPerStack,
        noiseFrequency,
        predicate,
//        fluidStartY,
//        fluidState,
        noise2DChance,
        radiusMatcher,
        checkSquareDistance,
        useScaledNoiseHeight,
        pointed,
        verifiesHeight,
        belowSurfaceDepth,
        HolderSet.direct(spawningFeatures)
      )
    }

    companion object {
      val DEFAULT: BlockStateProvider = SimpleStateProvider.simple(Blocks.STONE)
    }
  }



//  init {
//    this.radiusMatcher = radiusMatcher
//    this.checkSquareDistance = checkSquareDistance
//    this.useScaledNoiseHeight = useScaledNoiseHeight
//    this.pointed = pointed
//    this.verifiesHeight = verfiesHeight
//    this.belowSurfaceDepth = belowSurfaceDepth
//    this.spawningFeatures = spawningFeatures
//  }

  companion object {

//    private val DIRECT_CODEC: Codec<BlockPredicateFilter> =
//      RecordCodecBuilder.create { codecRecorder: RecordCodecBuilder.Instance<BlockPredicateFilter> ->
//        codecRecorder.group(
//          PlacementModifier.CODEC.listOf().fieldOf("placement")
//            .forGetter { p_191796_: BlockPredicateFilter -> p_191796_.predicate }
//        ).apply(codecRecorder, ::BlockPredicateFilter)
//      }

    val CODEC = RecordCodecBuilder.create { codecRecorder: RecordCodecBuilder.Instance<BoulderColumnConfig> ->
      codecRecorder.group(
        BlockStateProvider.CODEC.fieldOf("block_provider")
          .forGetter { config: BoulderColumnConfig -> config.blockProvider },
        BlockStateProvider.CODEC.fieldOf("middle_block_provider")
          .forGetter { config: BoulderColumnConfig -> config.middleBlockProvider },
        BlockStateProvider.CODEC.fieldOf("top_block_provider")
          .forGetter { config: BoulderColumnConfig -> config.topBlockProvider },
        IntProvider.CODEC.fieldOf("stackHeight")
          .forGetter { config: BoulderColumnConfig -> config.stackHeight },
        RadiusSettings.CODEC.fieldOf("radius_settings")
          .forGetter { config: BoulderColumnConfig -> config.radiusSettings },
        Codec.DOUBLE.fieldOf("radius_divisor_per_stack").orElse(1.0)
          .forGetter { config: BoulderColumnConfig -> config.radiusDivisorPerStack },
        Codec.FLOAT.fieldOf("noise_frequency").orElse(1.0f)
          .forGetter { config: BoulderColumnConfig -> config.noiseFrequency },
        Codec.list(BlockPredicate.CODEC).fieldOf("predicate")
          .orElse(ArrayList())
          .forGetter { config: BoulderColumnConfig -> config.predicate },
//        Codec.INT.fieldOf("fluid_start_y").orElse(12)
//          .forGetter { config: NoisySphereConfig -> config.fluidStartY },
//        FluidState.CODEC.fieldOf("fluidState")
//          .orElse(Fluids.EMPTY.defaultFluidState()).forGetter { config: NoisySphereConfig -> config.fluidState },
        Codec.DOUBLE.fieldOf("2d_noise_chance").orElse(0.25)
          .forGetter { config: BoulderColumnConfig -> config.noise2DChance },
        RadiusMatcher.CODEC.fieldOf("radius_matcher").orElse(RadiusMatcher.NONE)
          .forGetter { config -> config.radiusMatcher },
        Codec.BOOL.fieldOf("squared_distance_check").orElse(true)
          .forGetter { config -> config.checkSquareDistance },
        Codec.BOOL.fieldOf("use_scaled_noise_height").orElse(false)
          .forGetter { config -> config.useScaledNoiseHeight },
        Codec.BOOL.fieldOf("verifies_height").orElse(true)
          .forGetter { config -> config.verifiesHeight },
        Codec.BOOL.fieldOf("pointed").orElse(false)
          .forGetter { config -> config.pointed },
        IntProvider.CODEC.fieldOf("belowSurfaceDepth")
          .forGetter { config -> config.belowSurfaceDepth },
        PlacedFeature.LIST_CODEC.fieldOf("edge_features")
          .forGetter { noisySphereConfig -> noisySphereConfig.spawningFeatures }
      ).apply(codecRecorder, ::BoulderColumnConfig)
    }
  }
}