package com.dannbrown.databoxlib.content.worldgen.features

import com.dannbrown.databoxlib.content.worldgen.featureConfiguration.SpikeConfig
import com.dannbrown.databoxlib.noise.FastNoise
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.material.Fluids;


class SpikeFeature(codec: Codec<SpikeConfig>) : Feature<SpikeConfig>(codec) {
  var fnPerlin: FastNoise? = null

  override fun place(featurePlaceContext: FeaturePlaceContext<SpikeConfig>): Boolean {
    return place(
      featurePlaceContext.level(),
      featurePlaceContext.chunkGenerator(),
      featurePlaceContext.random(),
      featurePlaceContext.origin(),
      featurePlaceContext.config()
    )
  }

  fun place(
    world: WorldGenLevel,
    generator: ChunkGenerator,
    random: RandomSource,
    pos: BlockPos,
    config: SpikeConfig
  ): Boolean {
    defineSeed(world.seed)

    val mutable = MutableBlockPos()
//    val baseRadius = (config.getRadiusVariance().sample(random)).coerceAtLeast(1)
//    val height = (config.getHeightVariance().sample(random)).coerceAtLeast(1)
//    val startHeight = (height - 5).coerceAtLeast(1)

    val baseRadius = 4.0
    val height = (config.getHeightVariance().sample(random)).coerceAtLeast(1)
    val startHeight = (height - 5).coerceAtLeast(1)

    for (y in -height..-1) {
      for (x in -height..height) {
        for (z in -height..height) {
          mutable.set(pos).move(x, y + startHeight, z)
          val noise = (fnPerlin!!.GetNoise(mutable.x.toFloat(), mutable.z.toFloat()) * 12).toDouble()

          val coordsSquared = (x * x + z * z).toDouble()
          val noiseMultiplier = -(y * baseRadius) / coordsSquared
          val scaledNoise = noise / 11 * noiseMultiplier

          val threshold = 0.5
          if (y == -height) {
            if (scaledNoise >= threshold) if (!world.getBlockState(mutable.relative(Direction.DOWN))
                .canOcclude()
            ) return false
          }
          if (scaledNoise >= threshold) {
            if (!world.getBlockState(mutable).canOcclude()) {
              val blockState: BlockState = config.getBlockProvider().getState(random, mutable)

              world.setBlock(mutable, blockState, 2)

              if (blockState.block === Blocks.LAVA) world.scheduleTick(mutable, Fluids.LAVA, 0)
            }
          }
        }
      }
    }
    return true
  }

  fun defineSeed(seed: Long) {
    if (fnPerlin == null) {
      fnPerlin = FastNoise(seed.toInt())
      fnPerlin!!.SetNoiseType(FastNoise.NoiseType.PerlinFractal)
      fnPerlin!!.SetFractalType(FastNoise.FractalType.FBM)
      fnPerlin!!.SetFrequency(0.2f)
    }
  }
}

