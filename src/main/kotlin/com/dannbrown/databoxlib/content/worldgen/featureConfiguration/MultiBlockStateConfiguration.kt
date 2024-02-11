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


class MultiBlockStateConfiguration(
  private val blockProvider: BlockStateProvider,
  private val size: Int,
  private val predicate: List<BlockPredicate>,
) : FeatureConfiguration {
  companion object {
    val CODEC: Codec<MultiBlockStateConfiguration> =
      RecordCodecBuilder.create{ codecRecorder: RecordCodecBuilder.Instance<MultiBlockStateConfiguration> ->
        codecRecorder.group(
          BlockStateProvider.CODEC.fieldOf("block_provider").forGetter { config: MultiBlockStateConfiguration -> config.blockProvider },
          Codec.INT.fieldOf("size").orElse(3).forGetter { config: MultiBlockStateConfiguration -> config.size },
          Codec.list(BlockPredicate.CODEC).fieldOf("predicate")
            .orElse(ArrayList())
            .forGetter { config: MultiBlockStateConfiguration -> config.predicate },
        ).apply(codecRecorder, ::MultiBlockStateConfiguration)
      }
  }

  fun getBlockProvider(): BlockStateProvider {
    return blockProvider
  }

  fun getSize(): Int {
    return size
  }

  fun getPredicates(): List<BlockPredicate> {
    return predicate
  }


  class Builder {
    private var blockProvider: BlockStateProvider = SimpleStateProvider.simple(Blocks.STONE.defaultBlockState())
    private var size = 65
    private var predicate: List<BlockPredicate> = listOf(BlockPredicate.matchesTag(BlockTags.DIRT))

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


    fun setSize(size: Int): Builder {
     this.size = size
      return this
    }

    fun setPlacementPredicates(predicate: List<BlockPredicate>): Builder {
      this.predicate = predicate
      return this
    }

    fun build(): MultiBlockStateConfiguration {
      return MultiBlockStateConfiguration(blockProvider, size, predicate)
    }
  }

}