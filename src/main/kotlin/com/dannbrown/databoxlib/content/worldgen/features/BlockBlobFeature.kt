package com.dannbrown.databoxlib.content.worldgen.features

import com.dannbrown.databoxlib.content.worldgen.featureConfiguration.MultiBlockStateConfiguration
import com.mojang.serialization.Codec
import net.minecraft.core.BlockPos
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext

class BlockBlobFeature(configCodec: Codec<MultiBlockStateConfiguration>) :
  Feature<MultiBlockStateConfiguration>(configCodec) {

  private fun isPlaceable(level: WorldGenLevel, pos: BlockPos, predicates: List<BlockPredicate>): Boolean {
    for (predicate in predicates) {
      val state = level.getBlockState(pos)
      if(state != Blocks.AIR.defaultBlockState() && predicate.test(level, pos)){
        return true
      }
    }
    return false
  }


  override fun place(context: FeaturePlaceContext<MultiBlockStateConfiguration>): Boolean {
    var pos = context.origin()
    val level = context.level()
    val random = context.random()
    val config: MultiBlockStateConfiguration = context.config()

//    while (pos.y > level.minBuildHeight + 3) {
//      if (!level.isEmptyBlock(pos.below())) {
//        if (isPlaceable(level, pos.below(), config.getPredicates())) {
//          break
//        }
//      }
//      pos = pos.below()
//    }

    while (pos.y > level.minBuildHeight + config.getSize()) {
      if (!level.isEmptyBlock(pos.below()) && level.isEmptyBlock(pos)) {
        if (isPlaceable(level, pos.below(), config.getPredicates())) {
          break
        }
      }
      pos = pos.below()
    }

    return if (pos.y <= level.minBuildHeight + 3) {
      false
    } else {
      for (l in 0..config.getSize()) {
        val i = random.nextInt(config.getSize())
        val j = random.nextInt(config.getSize())
        val k = random.nextInt(config.getSize())
        val f = (i + j + k).toFloat() * 0.333f + 0.5f
        for (blockpos1 in BlockPos.betweenClosed(pos.offset(-i, -j, -k), pos.offset(i, j, k))) {
          if (blockpos1.distSqr(pos) <= (f * f).toDouble()) {
            level.setBlock(blockpos1, config.getBlockProvider().getState(random, blockpos1), 4)
          }
        }
        pos = pos.offset(-1 + random.nextInt(config.getSize()), -random.nextInt(config.getSize()), -1 + random.nextInt(config.getSize()))
      }
      true
    }
  }
}