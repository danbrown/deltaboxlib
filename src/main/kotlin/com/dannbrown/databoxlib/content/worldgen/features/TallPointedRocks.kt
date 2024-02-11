package com.dannbrown.databoxlib.content.worldgen.features


import com.dannbrown.databoxlib.content.worldgen.featureConfiguration.PointyRockConfig
import com.dannbrown.databoxlib.noise.FNVector3f
import com.mojang.serialization.Codec
import net.minecraft.core.BlockPos.MutableBlockPos
import net.minecraft.core.Direction
import net.minecraft.util.RandomSource
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.chunk.ChunkAccess
import net.minecraft.world.level.levelgen.Heightmap
import kotlin.math.abs


class TallPointedRocks(codec: Codec<PointyRockConfig>) :
  ChunkCoordinatesFeature<PointyRockConfig>(codec) {

  override fun generate(
    world: WorldGenLevel,
    random: RandomSource,
    chunkIn: ChunkAccess,
    x: Int,
    z: Int,
    config: PointyRockConfig
  ): Boolean {
    config.setUpNoise(world.seed)
    val xPos = x and 15
    val zPos = z and 15
    val mutable = MutableBlockPos(xPos, 0, zPos)
    val fnVector3f = FNVector3f(x.toFloat(), 0f, z.toFloat())
    config.getNoiseGen()!!.GradientPerturb(fnVector3f)
    val sampleNoise = config.getNoiseGen()!!.GetNoise(fnVector3f.x, fnVector3f.z)
    val groundLevel = chunkIn.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z)
    val center = MutableBlockPos()
    if (!chunkIn.getBlockState(mutable.above(groundLevel)).isAir) {
      if (sampleNoise < 0.43) {
        val valueToReverse = abs((sampleNoise * 645.0).toInt() * 1.8).toInt()
        var topHeight = (valueToReverse - abs(-sampleNoise * 645.0 * 1.8 - valueToReverse) + 63 * 9.5).toInt()

        // Some magic to stop spires going over the world limit. Point should always occur under world limit(<256).
        topHeight = (redistribute(topHeight.toFloat(), groundLevel.toFloat()) * config.heightMultiplier).toInt()
        if (topHeight > groundLevel) {
          mutable.move(Direction.UP, topHeight)
          if (center.y < mutable.y) {
            center.set(mutable)
          }
          for (yPos in topHeight downTo groundLevel) {
            if (chunkIn.getBlockState(mutable).isAir && mutable.y <= chunkIn.maxBuildHeight) {
              chunkIn.setBlockState(mutable, config.blockProvider.getState(random, mutable), false)
            }
            mutable.move(Direction.DOWN)
          }
        }
      }
    }
    return true
  }

  companion object {
    private fun redistribute(height: Float, groundLevel: Float): Int {
      var height = height
      val halfG = groundLevel * 0.5f
      height = (height - 125 - halfG) / 80
      val sigmoid = (height / (1 + abs(height.toDouble()))).toFloat()
      return ((170 - groundLevel) * sigmoid + halfG + 125).toInt()
    }
  }
}