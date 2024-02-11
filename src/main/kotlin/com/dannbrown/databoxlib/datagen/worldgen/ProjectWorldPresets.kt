package com.dannbrown.databoxlib.datagen.worldgen

import com.dannbrown.databoxlib.content.worldgen.dimension.PlanetZeroDimension
import com.dannbrown.databoxlib.content.worldgen.ProjectWorldPreset
import com.dannbrown.databoxlib.lib.LibUtils
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.presets.WorldPreset

object ProjectWorldPresets {
  val DATABOX_WORLD_PRESET = ResourceKey.create(Registries.WORLD_PRESET, LibUtils.resourceLocation("databoxlib_world"))

  fun bootstrap(context: BootstapContext<WorldPreset>) {
    context.register(DATABOX_WORLD_PRESET, ProjectWorldPreset.createFromDimension(context, PlanetZeroDimension))
  }

}