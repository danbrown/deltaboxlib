package com.dannbrown.databoxlib.datagen.worldgen

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.content.worldgen.featureConfiguration.*
import com.dannbrown.databoxlib.content.worldgen.features.*
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister


object ProjectFeatures {
  private val FEATURES: DeferredRegister<Feature<*>> = DeferredRegister.create(Registries.FEATURE, ProjectContent.MOD_ID)

  fun register(modBus: IEventBus){
    FEATURES.register(modBus)
  }

  // content
  val BLOCK_BLOBS = FEATURES.register("block_blobs") { BlockBlobFeature(MultiBlockStateConfiguration.CODEC) }
  val BOULDER = FEATURES.register("boulder") { BoulderFeature(BoulderConfig.CODEC) }
  val BOULDER_COLUMN = FEATURES.register("boulder_column") { BoulderColumnFeature(BoulderColumnConfig.CODEC) }
  val POINTED_ROCK = FEATURES.register("pointed_rock") { TallPointedRocks(PointyRockConfig.CODEC) }
  val SPIKE = FEATURES.register("spike") { SpikeFeature(SpikeConfig.CODEC) }
}