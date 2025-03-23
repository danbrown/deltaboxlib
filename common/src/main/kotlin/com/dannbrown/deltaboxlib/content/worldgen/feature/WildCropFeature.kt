package com.dannbrown.deltaboxlib.content.worldgen.feature

import com.dannbrown.deltaboxlib.content.worldgen.configuration.WildCropConfiguration
import com.mojang.serialization.Codec
import net.minecraft.core.BlockPos
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext

class WildCropFeature(codec: Codec<WildCropConfiguration>) : Feature<WildCropConfiguration>(codec) {
  override fun place(context: FeaturePlaceContext<WildCropConfiguration>): Boolean {
    val config = context.config()
    val origin = context.origin()
    val level = context.level()
    val random = context.random()

    var i = 0
    val tries = config.tries
    val xzSpread = config.xzSpread + 1
    val ySpread = config.ySpread + 1

    val mutablePos = BlockPos.MutableBlockPos()

    val floorFeature = config.floorFeature
    if (floorFeature != null) {
      repeat(tries) {
        mutablePos.setWithOffset(
          origin,
          random.nextInt(xzSpread) - random.nextInt(xzSpread),
          random.nextInt(ySpread) - random.nextInt(ySpread),
          random.nextInt(xzSpread) - random.nextInt(xzSpread)
        )
        if (config.floorFeature.value().place(level, context.chunkGenerator(), random, mutablePos)) {
          i++
        }
      }
    }

    repeat(tries) {
      val shorterXZ = xzSpread - 2
      mutablePos.setWithOffset(
        origin,
        random.nextInt(shorterXZ) - random.nextInt(shorterXZ),
        random.nextInt(ySpread) - random.nextInt(ySpread),
        random.nextInt(shorterXZ) - random.nextInt(shorterXZ)
      )
      if (config.primaryFeature.value().place(level, context.chunkGenerator(), random, mutablePos)) {
        i++
      }
    }

    repeat(tries) {
      mutablePos.setWithOffset(
        origin,
        random.nextInt(xzSpread) - random.nextInt(xzSpread),
        random.nextInt(ySpread) - random.nextInt(ySpread),
        random.nextInt(xzSpread) - random.nextInt(xzSpread)
      )
      if (config.secondaryFeature.value().place(level, context.chunkGenerator(), random, mutablePos)) {
        i++
      }
    }

    return i > 0
  }
}