package com.dannbrown.databoxlib.content.worldgen.featureConfiguration


import com.dannbrown.databoxlib.noise.FastNoise
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.HolderSet
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider
import net.minecraft.world.level.levelgen.placement.PlacedFeature


class PointyRockConfig (
  val blockProvider: BlockStateProvider,
  val seed: Int,
  val heightMultiplier: Double,
  val placedFeatureHolderSet: HolderSet<PlacedFeature>
) :
  FeatureConfiguration {
  private var noiseGen: FastNoise? = null
  fun setUpNoise(worldSeed: Long) {
    if (noiseGen == null) {
      noiseGen = FastNoise((worldSeed + seed).toInt())
      noiseGen!!.SetFractalType(FastNoise.FractalType.RigidMulti)
      noiseGen!!.SetNoiseType(FastNoise.NoiseType.SimplexFractal)
      noiseGen!!.SetGradientPerturbAmp(5f)
      noiseGen!!.SetFractalOctaves(1)
      noiseGen!!.SetFractalGain(0.3f)
      noiseGen!!.SetFrequency(0.02f)
    }
  }

  fun getNoiseGen(): FastNoise? {
    return if (noiseGen == null) throw NullPointerException("Initialize the noiseGen variable with \"setupNoise\" in your feature!") else noiseGen
  }

  class Builder {
    private var blockProvider: BlockStateProvider = SimpleStateProvider.simple(Blocks.STONE.defaultBlockState())
    private var seed = 65
    private var heightMultiplier = 1.0
    private var features: HolderSet<PlacedFeature> = HolderSet.direct()
    fun setBlockProvider(block: Block): Builder {
      blockProvider = SimpleStateProvider.simple(block.defaultBlockState())
      return this
    }

    fun setBlockProvider(state: BlockState?): Builder {
      blockProvider = SimpleStateProvider.simple(state)
      return this
    }

    fun setBlockProvider(blockProvider: BlockStateProvider): Builder {
      this.blockProvider = blockProvider
      return this
    }

    fun setSeed(seed: Int): Builder {
      this.seed = seed
      return this
    }

    fun setHeightMultiplier(heightMultiplier: Double): Builder {
      this.heightMultiplier = heightMultiplier
      return this
    }

    fun setPostFeatures(features: HolderSet<PlacedFeature>): Builder {
      this.features = features
      return this
    }

    fun build(): PointyRockConfig {
      return PointyRockConfig(blockProvider, seed, heightMultiplier, features)
    }
  }

  companion object {
    val CODEC: Codec<PointyRockConfig> =
      RecordCodecBuilder.create { codecRecorder: RecordCodecBuilder.Instance<PointyRockConfig> ->
        codecRecorder.group(
          BlockStateProvider.CODEC.fieldOf("block_provider")
            .forGetter { config: PointyRockConfig -> config.blockProvider },
          Codec.INT.fieldOf("seed").orElse(0).forGetter { config: PointyRockConfig -> config.seed },
          Codec.DOUBLE.fieldOf("height_multiplier").orElse(1.0)
            .forGetter { config: PointyRockConfig -> config.heightMultiplier },
          PlacedFeature.LIST_CODEC.fieldOf("post_features")
            .forGetter { config: PointyRockConfig -> config.placedFeatureHolderSet }
        ).apply(
          codecRecorder
        ) { blockProvider: BlockStateProvider, seed: Int, heightMultiplier: Double, placedFeatureHolderSet: HolderSet<PlacedFeature> ->
          PointyRockConfig(
            blockProvider,
            seed,
            heightMultiplier,
            placedFeatureHolderSet
          )
        }
      }
  }
}

