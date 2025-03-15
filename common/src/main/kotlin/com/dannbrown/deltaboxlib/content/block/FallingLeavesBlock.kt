package com.dannbrown.deltaboxlib.content.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Supplier

class FallingLeavesBlock(props: Properties, val particle: Supplier<ParticleOptions>) :
  FlammableLeavesBlock(props, 60, 30) {
  override fun animateTick(blockState: BlockState, level: Level, blockPos: BlockPos, randomSource: RandomSource) {
    super.animateTick(blockState, level, blockPos, randomSource)
    if (randomSource.nextInt(10) == 0) {
      val bellow = blockPos.below()
      val bellowState = level.getBlockState(bellow)
      if (!Block.isFaceFull(bellowState.getCollisionShape(level, bellow), Direction.UP)) {
        val d = blockPos.x + randomSource.nextDouble()
        val e = blockPos.y - 0.05
        val f = blockPos.z + randomSource.nextDouble()
        level.addParticle(particle.get(), d, e, f, 0.0, 0.0, 0.0);
      }
    }
  }
}