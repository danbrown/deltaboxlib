package com.dannbrown.deltaboxlib.content.worldgen.decorator

import com.dannbrown.deltaboxlib.init.DeltaboxPlacerTypes
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.Util
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.feature.treedecorators.AttachedToLeavesDecorator
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType

class DeltaVineDecorator(
  probability: Float,
  exclusionRadiusXZ: Int,
  exclusionRadiusY: Int,
  blockProvider: BlockStateProvider,
  requiredEmptyBlocks: Int,
  directions: MutableList<Direction>,
  private val tipBlockProvider: BlockStateProvider // Tip block provider for the vine
) : AttachedToLeavesDecorator(
  probability,
  exclusionRadiusXZ,
  exclusionRadiusY,
  blockProvider,
  requiredEmptyBlocks,
  directions
) {

  override fun type(): TreeDecoratorType<*> = DeltaboxPlacerTypes.VINE_DECORATOR.get()

  companion object {
    val CODEC: Codec<DeltaVineDecorator> = RecordCodecBuilder.create { instance ->
      instance.group(
        Codec.floatRange(0.0f, 1.0f)
          .fieldOf("probability")
          .forGetter { it.probability },

        Codec.intRange(0, 16)
          .fieldOf("exclusion_radius_xz")
          .forGetter { it.exclusionRadiusXZ },

        Codec.intRange(0, 16)
          .fieldOf("exclusion_radius_y")
          .forGetter { it.exclusionRadiusY },

        BlockStateProvider.CODEC
          .fieldOf("block_provider")
          .forGetter { it.blockProvider },

        Codec.intRange(1, 16)
          .fieldOf("required_empty_blocks")
          .forGetter { it.requiredEmptyBlocks },

        ExtraCodecs.nonEmptyList(Direction.CODEC.listOf())
          .fieldOf("directions")
          .forGetter { it.directions },

        BlockStateProvider.CODEC
          .fieldOf("tip_block_provider")
          .forGetter { it.tipBlockProvider }

      )
        .apply(instance) { probability, exclusionRadiusXZ, exclusionRadiusY, blockProvider, requiredEmptyBlocks, directions, tipBlockProvider ->
          DeltaVineDecorator(
            probability,
            exclusionRadiusXZ,
            exclusionRadiusY,
            blockProvider,
            requiredEmptyBlocks,
            directions,
            tipBlockProvider
          )
        }
    }
  }

  override fun place(context: Context) {
    val placedPositions = mutableSetOf<BlockPos>()
    val random = context.random()

    for (leafPos in Util.shuffledCopy(context.leaves(), random)) {
      val direction = Util.getRandom(this.directions, random)
      val vinePos = leafPos.relative(direction)

      if (vinePos !in placedPositions && random.nextFloat() < probability && hasRequiredEmptyBlocks(
          context,
          leafPos,
          direction
        )
      ) {
        val minBound = vinePos.offset(-exclusionRadiusXZ, -exclusionRadiusY, -exclusionRadiusXZ)
        val maxBound = vinePos.offset(exclusionRadiusXZ, exclusionRadiusY, exclusionRadiusXZ)

        placedPositions.addAll(BlockPos.betweenClosed(minBound, maxBound).map { it.immutable() })

        // Determine number of stacked vine blocks (1 to 3)
        val vineLength = random.nextInt(3) + 1

        // Place stacked vine blocks
        for (i in 0 until vineLength) {
          val stackedPos = vinePos.below(i)
          context.setBlock(stackedPos, blockProvider.getState(random, stackedPos))
        }

        // Place the tip block at the bottom of the vine stack
        val tipPos = vinePos.below(vineLength)
        context.setBlock(tipPos, tipBlockProvider.getState(random, tipPos))
      }
    }
  }

  private fun hasRequiredEmptyBlocks(context: Context, pos: BlockPos, direction: Direction): Boolean {
    return (1..requiredEmptyBlocks).all { context.isAir(pos.relative(direction, it)) }
  }
}
