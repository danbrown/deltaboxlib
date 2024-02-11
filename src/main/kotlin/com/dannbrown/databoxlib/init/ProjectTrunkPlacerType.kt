package com.dannbrown.databoxlib.init

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.content.worldgen.trunkPlacer.ForkingStalkTrunkPlacer
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister

object ProjectTrunkPlacerType {
  val TRUNK_PLACER = DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, ProjectContent.MOD_ID)

  fun register(modBus: IEventBus) {
    TRUNK_PLACER.register(modBus)
  }
  
  // content

  val FORKING_STALK_TRUNK_PLACER = TRUNK_PLACER.register("forking_stalk_trunk_placer") {
    TrunkPlacerType(ForkingStalkTrunkPlacer.CODEC)
  }
}