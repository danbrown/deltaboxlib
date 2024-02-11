package com.dannbrown.databoxlib.content.worldgen.featureConfiguration

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.tags.BlockTags
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider


class SpikeConfig(
  private val blockProvider: BlockStateProvider,
  private val predicate: List<BlockPredicate>,
  private val heightVariance: IntProvider,
) : FeatureConfiguration {
  companion object {
    val CODEC: Codec<SpikeConfig> =
      RecordCodecBuilder.create{ codecRecorder: RecordCodecBuilder.Instance<SpikeConfig> ->
        codecRecorder.group(
          BlockStateProvider.CODEC.fieldOf("block_provider").forGetter { config: SpikeConfig -> config.blockProvider },
          Codec.list(BlockPredicate.CODEC).fieldOf("predicate")
            .orElse(ArrayList())
            .forGetter { config: SpikeConfig -> config.predicate },
          IntProvider.CODEC.fieldOf("height_variance")
            .forGetter { config: SpikeConfig -> config.heightVariance },
        ).apply(codecRecorder, ::SpikeConfig)
      }
  }

  fun getBlockProvider(): BlockStateProvider {
    return blockProvider
  }


  fun getPredicates(): List<BlockPredicate> {
    return predicate
  }

  fun getHeightVariance(): IntProvider {
    return heightVariance
  }

  class Builder {
    private var blockProvider: BlockStateProvider = SimpleStateProvider.simple(Blocks.STONE.defaultBlockState())
    private var predicate: List<BlockPredicate> = listOf(BlockPredicate.matchesTag(BlockTags.DIRT))
    private var heightVariance: IntProvider = UniformInt.of(5, 10)

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

    fun setHeightVariance(heightVariance: IntProvider): Builder {
      this.heightVariance = heightVariance
      return this
    }

    fun setPlacementPredicates(predicate: List<BlockPredicate>): Builder {
      this.predicate = predicate
      return this
    }

    fun build(): SpikeConfig {
      return SpikeConfig(blockProvider, predicate, heightVariance)
    }
  }

}