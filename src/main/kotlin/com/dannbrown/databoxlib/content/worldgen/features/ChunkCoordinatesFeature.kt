package com.dannbrown.databoxlib.content.worldgen.features

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;


abstract class ChunkCoordinatesFeature<FC : FeatureConfiguration?>(codec: Codec<FC>) : Feature<FC>(codec) {
  override fun place(featurePlaceContext: FeaturePlaceContext<FC>): Boolean {
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
    rand: RandomSource,
    pos: BlockPos,
    config: FC
  ): Boolean {
    val chunk = world.getChunk(pos).pos
    val xStart = chunk.minBlockX
    val zStart = chunk.minBlockZ
    for (xMove in 0..15) {
      for (zMove in 0..15) {
        val x = xStart + xMove
        val z = zStart + zMove
        generate(world, rand, world.getChunk(pos), x, z, config)
      }
    }
    return true
  }

  abstract fun generate(
    world: WorldGenLevel,
    random: RandomSource,
    chunkIn: ChunkAccess,
    x: Int,
    z: Int,
    config: FC
  ): Boolean
}

