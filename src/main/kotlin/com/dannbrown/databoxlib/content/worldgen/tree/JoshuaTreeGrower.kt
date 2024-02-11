package com.dannbrown.databoxlib.content.worldgen.tree

import com.dannbrown.databoxlib.datagen.worldgen.ProjectConfiguredFeatures
import net.minecraft.resources.ResourceKey
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.grower.AbstractTreeGrower
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature

class JoshuaTreeGrower : AbstractTreeGrower() {
  override fun getConfiguredFeature(
    pRandom: RandomSource?,
    pHasFlowers: Boolean
  ): ResourceKey<ConfiguredFeature<*, *>> {
    return ProjectConfiguredFeatures.JOSHUA_TREE_KEY
  }
}