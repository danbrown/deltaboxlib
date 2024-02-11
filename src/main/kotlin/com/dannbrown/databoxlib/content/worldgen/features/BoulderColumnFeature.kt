package com.dannbrown.databoxlib.content.worldgen.features


import com.dannbrown.databoxlib.content.worldgen.featureConfiguration.BoulderColumnConfig
import com.dannbrown.databoxlib.content.worldgen.featureConfiguration.RadiusMatcher
import com.dannbrown.databoxlib.datagen.worldgen.ProjectConfiguredFeatures
import com.dannbrown.databoxlib.noise.FastNoise
import com.mojang.serialization.Codec
import net.minecraft.core.BlockPos
import net.minecraft.core.BlockPos.MutableBlockPos
import net.minecraft.core.Direction
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.withSign


class BoulderColumnFeature(configCodec: Codec<BoulderColumnConfig>) :
  Feature<BoulderColumnConfig>(configCodec) {
  protected var seed: Long = 0

  private fun isPlaceable(level: WorldGenLevel, pos: BlockPos, predicates: List<BlockPredicate>): Boolean {
    for (predicate in predicates) {
      val state = level.getBlockState(pos)
      if(state != Blocks.AIR.defaultBlockState() && predicate.test(level, pos)){
        return true
      }
    }
    return false
  }

  override fun place(featurePlaceContext: FeaturePlaceContext<BoulderColumnConfig>): Boolean {

    val config = featurePlaceContext.config()
    val level = featurePlaceContext.level()
    val chunkGenerator = featurePlaceContext.chunkGenerator()
    val random = featurePlaceContext.random()
    val origin = featurePlaceContext.origin()

    val place: Boolean = place(
      level,
      chunkGenerator,
      random,
      origin,
      config,
    )

    for (spawningFeature in config.spawningFeatures) {
      spawningFeature.value().place(level, chunkGenerator, random, origin)
    }

    return place
  }

  fun place(
    level: WorldGenLevel,
    chunkGenerator: ChunkGenerator?,
    random: RandomSource,
    position: BlockPos,
    config: BoulderColumnConfig
  ): Boolean {
    setSeed(level.seed, config.noiseFrequency)
    val use2D: Boolean = random.nextDouble() < config.noise2DChance
    val radiusMatcher: RadiusMatcher = config.radiusMatcher
    val mutable = MutableBlockPos().set(position)
    val mutable2 = MutableBlockPos().set(mutable)
    val radiusSettings: BoulderColumnConfig.RadiusSettings = config.radiusSettings
    val xRadius = (radiusSettings.xRadius.sample(random) / 2)
    val yRadius = if (radiusMatcher === RadiusMatcher.ALL) xRadius else ((radiusSettings.yRadius.sample(random) / 2))
    val zRadius = if (radiusMatcher === RadiusMatcher.ALL || radiusMatcher === RadiusMatcher.XZ) xRadius else ((radiusSettings.zRadius.sample(random) / 2))
    var lowestX = position.x
    var lowestY = position.y
    var lowestZ = position.z
    var verifiedHeight: Boolean = !config.verifiesHeight
    val perlin = fastNoise!!.GetPerlin(position.x.toFloat(), position.y.toFloat(), position.z.toFloat())
    val noiseScale = 2
    val scaledNoise = (perlin * noiseScale).toDouble()
    val centerHeight = Mth.lerp(scaledNoise, 1.0, 3.0) + 1
//    val centerHeight = Mth.lerp(scaledNoise, 2.0, 7.0) + 10
    val stackHeight: Int = config.stackHeight.sample(random)
    val built = Array(xRadius * 2 + 1) {
      IntArray(
        zRadius * 2 + 1
      )
    }
    var stackIDX = 0

    while (mutable2.y > level.minBuildHeight + yRadius) {
      if (!level.isEmptyBlock(mutable2.below()) && level.isEmptyBlock(mutable2)) {
        // check if its placeable on the middle block
        // and the radius corners
        if (
          isPlaceable(level, mutable2.below(), config.predicate)
//          &&
//          isPlaceable(level, mutable2.below().north(xRadius).west(xRadius), config.predicate) &&
//          isPlaceable(level, mutable2.below().north(xRadius).east(xRadius), config.predicate) &&
//          isPlaceable(level, mutable2.below().south(xRadius).west(xRadius), config.predicate) &&
//          isPlaceable(level, mutable2.below().south(xRadius).east(xRadius), config.predicate)
          ) {
          break
        }
      }
      mutable2.move(0, -1, 0)
    }

    while (stackIDX <= stackHeight) {
      for (x in -xRadius..xRadius) {
        mutable2.setX(mutable.x + x)
        val xFract = x.toDouble() / xRadius
        for (z in -zRadius..zRadius) {
          mutable2.setZ(mutable.z + z)
          val zFract = z.toDouble() / zRadius

          if (verifiedHeight) {
            val addedHeight: Double = if (config.useScaledNoiseHeight) getScaledNoiseExtensionHeight(mutable2, centerHeight) else (1).toDouble()
            if (addedHeight > built[x + xRadius][z + zRadius]) {
              for (y in -yRadius..yRadius) {
                val yFract = (y / yRadius).toDouble()
                mutable2.setY((mutable.y + y).toInt())

                //Credits to Hex_26 for this equation!
                var distanceSquaredFromCenter = xFract * xFract + yFract * yFract + zFract * zFract
                val yDistSquared = yFract * yFract
                val noise =
                  if (use2D) fastNoise!!.GetNoise(mutable2.x.toFloat(), mutable2.z.toFloat()) else fastNoise!!.GetNoise(
                    mutable2.x.toFloat(),
                    mutable2.y.toFloat(),
                    mutable2.z.toFloat()
                  )
                val threshold = 1 + 0.7f * noise

                // check whether the center of this column at this Y would be empty
                val factor = yDistSquared / threshold
                if (factor >= 1) {
                  // make sure it's not empty
                  distanceSquaredFromCenter /= factor
                  // add some extra noise back to make it not just a thin one block column
                  // the magnitude of the noise can be configurable
                  // this should probably be using its own noise for best effect
                  distanceSquaredFromCenter -= (noise * 0.2).withSign(distanceSquaredFromCenter)
                }

                val density: Double = if (stackIDX == stackHeight && config.pointed) ((y + yRadius + 1) / (yRadius * 2 + 1)).toDouble() else (0).toDouble()
                val squaredDistance = (x * x + z * z) / Mth.clampedLerp(0.1, 1.0, 1 - density)
                if (config.checkSquareDistance && squaredDistance >= xRadius * zRadius) {
                  continue
                }

                val mutable3 = MutableBlockPos().set(mutable2)
                var noiseExtensionY = 0

                val blockDividerThreshold = 0.1
                val mixThreshold = 1

                while (noiseExtensionY < addedHeight) {
                  val minY = min(
                    position.y.toDouble(),
                    level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, mutable3.x, mutable3.z).toDouble()
                  ).toInt()

                  level.setBlock(mutable3, config.topBlockProvider.getState(random, mutable3), 2)

                  // push the block 1 block to the x and z axis towards the center
//                  val position4 = mutable3.relative(Direction.DOWN)


                  // mix the middle block and the bottom block provider to add a bit of variety in the transition
                  val betweenBlockProvider = ProjectConfiguredFeatures.weightedStateProvider(
                    mapOf(
                      config.middleBlockProvider.getState(random, mutable3) to 2,
                      config.blockProvider.getState(random, mutable3) to 2,
                    )
                  )

                  if (noiseExtensionY > addedHeight * blockDividerThreshold) {
                    level.setBlock(
                      mutable3.relative(Direction.DOWN),
                      config.middleBlockProvider.getState(random, mutable3),
                      2
                    )
                  }
                  else if (noiseExtensionY > ((addedHeight * blockDividerThreshold) - mixThreshold)) {
                    level.setBlock(
                      mutable3.relative(Direction.DOWN),
                      betweenBlockProvider.getState(random, mutable3),
                      2
                    )
                  }
                  else {
                    level.setBlock(
                      mutable3.relative(Direction.DOWN),
                      config.blockProvider.getState(random, mutable3),
                      2
                    )
                  }

                  mutable3.move(Direction.UP)
                  noiseExtensionY++
                }

                built[x + xRadius][z + zRadius] =
                  max(mutable3.y.toDouble(), built[x + xRadius][z + zRadius].toDouble())
                    .toInt()
                lowestX = min(lowestX.toDouble(), mutable2.x.toDouble()).toInt()
                lowestY = min(lowestY.toDouble(), mutable2.y.toDouble()).toInt()
                lowestZ = min(lowestZ.toDouble(), mutable2.z.toDouble()).toInt()
              }
            }
          }
          else {
            val oceanFloor = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, mutable2.x, mutable2.z)
            if (level.isOutsideBuildHeight(oceanFloor - 1) || position.y - oceanFloor > 15) {
              return false
            }
          }
        }
      }

      if (verifiedHeight) {
//                xRadius = Math.max(xRadius / config.radiusDivisorPerStack(), 1);
//                yRadius = Math.max(yRadius * 0.1F, 1);
        mutable.setY((mutable.y + yRadius))
        //                zRadius = Math.max(zRadius / config.radiusDivisorPerStack(), 1);
        stackIDX++
      }
      verifiedHeight = true
    }



    return true
  }


  private fun getScaledNoiseExtensionHeight(mutable2: MutableBlockPos, centerHeight: Double): Double {
    val perlin1 =
      abs(fastNoise!!.GetPerlin(mutable2.x.toFloat(), mutable2.z.toFloat()).toDouble())
        .toFloat()
    val height = Mth.lerp(perlin1, 2f, 5f).toDouble()
    return Mth.lerp(perlin1.toDouble(), height, centerHeight + 25)
  }

  fun setSeed(seed: Long, noiseFreq: Float) {
    if (this.seed != seed || fastNoise == null) {
      fastNoise = FastNoise(seed.toInt())
      fastNoise!!.SetNoiseType(FastNoise.NoiseType.Cellular)
      this.seed = seed
    }
    fastNoise!!.SetFrequency(noiseFreq)
  }

  companion object {
    protected var fastNoise: FastNoise? = null
  }



//  fun place(
//    level: WorldGenLevel,
//    chunkGenerator: ChunkGenerator?,
//    random: RandomSource,
//    position: BlockPos,
//    config: BoulderColumnConfig
//  ): Boolean {
//    setSeed(level.seed, config.noiseFrequency)
//    val use2D: Boolean = random.nextDouble() < config.noise2DChance
//    val radiusMatcher: RadiusMatcher = config.radiusMatcher
//    val mutable = MutableBlockPos().set(position)
//    val mutable2 = MutableBlockPos().set(mutable)
//    val radiusSettings: BoulderColumnConfig.RadiusSettings = config.radiusSettings
//    val xRadius = (radiusSettings.xRadius.sample(random) / 2)
//    val yRadius = if (radiusMatcher === RadiusMatcher.ALL) xRadius else ((radiusSettings.yRadius
//      .sample(random) / 2))
//    val zRadius =
//      if (radiusMatcher === RadiusMatcher.ALL || radiusMatcher === RadiusMatcher.XZ) xRadius else ((radiusSettings.zRadius
//        .sample(random) / 2))
//    var lowestX = position.x
//    var lowestY = position.y
//    var lowestZ = position.z
//    var verifiedHeight: Boolean = !config.verifiesHeight
//    val perlin = fastNoise!!.GetPerlin(position.x.toFloat(), position.y.toFloat(), position.z.toFloat())
//    val scaledNoise = (perlin * 8).toDouble()
//    val centerHeight = Mth.lerp(scaledNoise, 2.0, 7.0) + 10
//    val stackHeight: Int = config.stackHeight.sample(random)
//    val built = Array(xRadius * 2 + 1) {
//      IntArray(
//        zRadius * 2 + 1
//      )
//    }
//    var stackIDX = 0
//
//    while (mutable2.y > level.minBuildHeight + yRadius) {
//      if (!level.isEmptyBlock(mutable2.below()) && level.isEmptyBlock(mutable2)) {
//        if (isPlaceable(level, mutable2.below(), config.predicate)) {
//          break
//        }
//      }
//      mutable2.move(0, -1, 0)
//    }
//
//    while (stackIDX <= stackHeight) {
//      for (x in -xRadius..xRadius) {
//        mutable2.setX(mutable.x + x)
//        val xFract = x.toDouble() / xRadius
//        for (z in -zRadius..zRadius) {
//          mutable2.setZ(mutable.z + z)
//          val zFract = z.toDouble() / zRadius
//          if (verifiedHeight) {
//            val addedHeight: Double = if (config.useScaledNoiseHeight) getScaledNoiseExtensionHeight(mutable2, centerHeight) else (1).toDouble()
//            if (addedHeight > built[x + xRadius][z + zRadius]) {
//              for (y in -yRadius..yRadius) {
//                val yFract = (y / yRadius).toDouble()
//                mutable2.setY((mutable.y + y).toInt())
//
//                //Credits to Hex_26 for this equation!
//                var distanceSquaredFromCenter = xFract * xFract + yFract * yFract + zFract * zFract
//                val yDistSquared = yFract * yFract
//                val noise =
//                  if (use2D) fastNoise!!.GetNoise(mutable2.x.toFloat(), mutable2.z.toFloat()) else fastNoise!!.GetNoise(
//                    mutable2.x.toFloat(),
//                    mutable2.y.toFloat(),
//                    mutable2.z.toFloat()
//                  )
//                val threshold = 1 + 0.7f * noise
//
//                // check whether the center of this column at this Y would be empty
//                val factor = yDistSquared / threshold
//                if (factor >= 1) {
//                  // make sure it's not empty
//                  distanceSquaredFromCenter /= factor
//                  // add some extra noise back to make it not just a thin one block column
//                  // the magnitude of the noise can be configurable
//                  // this should probably be using its own noise for best effect
//                  distanceSquaredFromCenter -= (noise * 0.2).withSign(distanceSquaredFromCenter)
//                }
//                //                            if (distanceSquaredFromCenter >= threshold) {
////                                continue;
////                            }
//                val density: Double =
//                  if (stackIDX == stackHeight && config.pointed) ((y + yRadius + 1) / (yRadius * 2 + 1)).toDouble() else (0).toDouble()
//                val squaredDistance = (x * x + z * z) / Mth.clampedLerp(0.1, 1.0, 1 - density)
//                if (config.checkSquareDistance && squaredDistance >= xRadius * zRadius) {
//                  continue
//                }
//                val mutable3 = MutableBlockPos().set(mutable2)
//                var noiseExtensionY = 0
//                while (noiseExtensionY < addedHeight) {
//                  val minY = min(
//                    position.y.toDouble(),
//                    level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, mutable3.x, mutable3.z).toDouble()
//                  ).toInt()
//
//                  val belowSurfaceDepth: Boolean = minY - mutable3.y < config.belowSurfaceDepth.sample(random)
//                  //                                    if (!config.verfiesHeight() || belowSurfaceDepth) {
//                  level.setBlock(mutable3, config.topBlockProvider.getState(random, mutable3), 2)
//                  level.setBlock(
//                    mutable3.relative(Direction.DOWN),
//                    config.blockProvider.getState(random, mutable3),
//                    2
//                  )
//                  //                                    }
//                  mutable3.move(Direction.UP)
//                  noiseExtensionY++
//                }
//                built[x + xRadius][z + zRadius] =
//                  max(mutable3.y.toDouble(), built[x + xRadius][z + zRadius].toDouble())
//                    .toInt()
//                lowestX = min(lowestX.toDouble(), mutable2.x.toDouble()).toInt()
//                lowestY = min(lowestY.toDouble(), mutable2.y.toDouble()).toInt()
//                lowestZ = min(lowestZ.toDouble(), mutable2.z.toDouble()).toInt()
//              }
//            }
//          } else {
//            val oceanFloor = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, mutable2.x, mutable2.z)
//            if (level.isOutsideBuildHeight(oceanFloor - 1) || position.y - oceanFloor > 15) {
//              return false
//            }
//          }
//        }
//      }
//      if (verifiedHeight) {
////                xRadius = Math.max(xRadius / config.radiusDivisorPerStack(), 1);
////                yRadius = Math.max(yRadius * 0.1F, 1);
//        mutable.setY((mutable.y + yRadius))
//        //                zRadius = Math.max(zRadius / config.radiusDivisorPerStack(), 1);
//        stackIDX++
//      }
//      verifiedHeight = true
//    }
//
//
//
//    return true
//  }
}