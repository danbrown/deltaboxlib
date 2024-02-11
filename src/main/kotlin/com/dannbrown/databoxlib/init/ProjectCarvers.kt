package com.dannbrown.databoxlib.init

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.content.worldgen.worldCarver.CustomCaveWorldCarver
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration
import net.minecraft.world.level.levelgen.carver.WorldCarver
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries

object ProjectCarvers {
  val CARVERS = DeferredRegister.create(
    ForgeRegistries.WORLD_CARVERS,
    ProjectContent.MOD_ID
  )
  val SAMPLE_CAVE = CARVERS.register<WorldCarver<CaveCarverConfiguration>>("sample_cave")
  { CustomCaveWorldCarver(CaveCarverConfiguration.CODEC) }

  fun register(eventBus: IEventBus?) {
    CARVERS.register(eventBus)
  }
}