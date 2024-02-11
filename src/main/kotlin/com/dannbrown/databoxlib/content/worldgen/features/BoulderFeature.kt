package com.dannbrown.databoxlib.content.worldgen.features

import com.dannbrown.databoxlib.content.worldgen.featureConfiguration.BoulderConfig
import com.dannbrown.databoxlib.content.worldgen.featureConfiguration.BoulderConfig.RadiusSettings
import com.dannbrown.databoxlib.noise.FastNoise
import com.mojang.serialization.Codec
import it.unimi.dsi.fastutil.longs.Long2ObjectMap
import net.minecraft.core.BlockPos
import net.minecraft.core.BlockPos.MutableBlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext

// Credits to CorgiTaco for BYG's implementation of this feature: https://github.com/AOCAWOL/BYG/blob/1.20.X/Common/src/main/java/potionstudios/byg/common/world/feature/gen/Boulder.java
// This is a modified version to allow for more control over the shape and layers of the boulder.
class BoulderFeature(configCodec: Codec<BoulderConfig>) :
  Feature<BoulderConfig>(configCodec) {
  protected var fastNoise: FastNoise? = null
  protected var seed: Long = 0

  fun fillList(
    positions: Long2ObjectMap<BlockState>,
    level: WorldGenLevel,
    seed: Long,
    random: RandomSource,
    origin: BlockPos,
    config: BoulderConfig
  ) {
    place(object : ApplyPlacement {
      override fun apply(pos: BlockPos, state: BlockState) {
        positions.put(pos.asLong(), state)
      }

      override fun replaceOccupied(blockPos: BlockPos): Boolean {
        return positions.containsKey(blockPos.asLong())
      }

      override fun ignorePlacement(): Boolean {
        return true
      }
    }, level, seed, random, origin, config)
  }


  interface ApplyPlacement {
    fun apply(pos: BlockPos, state: BlockState)
    fun replaceOccupied(blockPos: BlockPos): Boolean
    fun ignorePlacement(): Boolean {
      return false
    }
  }


  fun place(
    placement: ApplyPlacement,
    level: WorldGenLevel,
    seed: Long,
    random: RandomSource?,
    origin: BlockPos?,
    config: BoulderConfig
  ): Boolean {
    defineSeed(seed)

    val mutable = MutableBlockPos().set(origin)
    val mutable2 = MutableBlockPos().set(mutable)
    val stackHeight: Int = config.stackHeight.sample(random)
    val radiusSettings: RadiusSettings = config.radiusSettings
    val xRadius: Int = radiusSettings.xRadius.sample(random) / 2
    val yRadius: Int = radiusSettings.yRadius.sample(random) / 2
    val zRadius: Int = radiusSettings.zRadius.sample(random) / 2

    while (mutable2.y > level.minBuildHeight + yRadius) {
      if (!level.isEmptyBlock(mutable2.below()) && level.isEmptyBlock(mutable2)) {
        if (isPlaceable(level, mutable2.below(), config.predicate)) {
          break
        }
      }
      mutable2.move(0, -1, 0)
    }


    if (!isPlaceable(level, mutable2.below(), config.predicate) && !placement.ignorePlacement()) {
      return false
    }

    fastNoise?.SetFrequency(config.noiseFrequency)
    val xRadiusSquared = (xRadius * xRadius).toDouble()
    val yRadiusSquared = (yRadius * yRadius).toDouble()
    val zRadiusSquared = (zRadius * zRadius).toDouble()
    for (stackIDX in 0 until stackHeight) {
      for (x in -xRadius..xRadius) {
        for (z in -zRadius..zRadius) {
          for (y in yRadius downTo -yRadius) {
            mutable2.set(mutable).move(x, y, z)
            if (placement.replaceOccupied(mutable2)) {
              continue
            }
            // Credits to Hex_26 for this equation!
            val equationResult = x * x / xRadiusSquared + y * y / yRadiusSquared + z * z / zRadiusSquared
            val threshold: Double = 1 + 0.7 * fastNoise?.GetNoise(mutable2.x.toFloat(), mutable2.y.toFloat(),
              mutable2.z.toFloat()
            )!!
            if (equationResult >= threshold) continue

            val state: BlockState = config.blockProvider.getState(random, mutable2)
            val middleState: BlockState = config.middleBlockProvider.getState(random, mutable2)
            val outerState: BlockState = config.topBlockProvider.getState(random, mutable2)

            // if placing on the last circunference of the sphere, place the outerState
            if (equationResult >= threshold - 0.6) {
              placement.apply(mutable2, outerState)
            } else if (equationResult >= threshold - 0.8) {
              placement.apply(mutable2, middleState)
            } else {
              placement.apply(mutable2, state)
            }
          }
        }
      }
    }
    return true
  }

  private fun isPlaceable(level: WorldGenLevel, pos: BlockPos, predicates: List<BlockPredicate>): Boolean {
    for (predicate in predicates) {
      val state = level.getBlockState(pos)
      if(state != Blocks.AIR.defaultBlockState() && predicate.test(level, pos)){
        return true
      }
    }
    return false
  }


  override fun place(featurePlaceContext: FeaturePlaceContext<BoulderConfig>): Boolean {
    val origin = featurePlaceContext.origin()
    val random = featurePlaceContext.random()
    val chunkGenerator = featurePlaceContext.chunkGenerator()
    val level = featurePlaceContext.level()

    val place: Boolean = place(object : ApplyPlacement {
      override fun apply(pos: BlockPos, state: BlockState) {
        level.setBlock(pos, state, 2)
      }

      override fun replaceOccupied(blockPos: BlockPos): Boolean {
        return false
      }
    }, level, level.seed, random, origin, featurePlaceContext.config())

    for (spawningFeature in featurePlaceContext.config().spawningFeatures) {
      spawningFeature.value().place(level, chunkGenerator, random, origin)
    }

    return place
  }


  fun defineSeed(seed: Long) {
    if (this.seed != seed || fastNoise == null) {
      fastNoise = FastNoise(seed.toInt())
      fastNoise!!.SetNoiseType(FastNoise.NoiseType.Simplex)
      this.seed = seed
    }
  }
}