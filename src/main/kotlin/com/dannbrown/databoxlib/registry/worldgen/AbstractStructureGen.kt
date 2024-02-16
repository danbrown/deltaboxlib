package com.dannbrown.databoxlib.registry.worldgen

import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.levelgen.structure.Structure
import net.minecraft.world.level.levelgen.structure.StructureSet
import net.minecraftforge.common.world.BiomeModifier
import net.minecraftforge.registries.ForgeRegistries

abstract class AbstractStructureGen {
  abstract val modId: String

  fun registerKey(name: String): ResourceKey<Structure> {
    return ResourceKey.create(Registries.STRUCTURE, ResourceLocation(modId, name))
  }

  fun registerSetKey(name: String): ResourceKey<StructureSet> {
    return ResourceKey.create(Registries.STRUCTURE_SET, ResourceLocation(modId,name))
  }

  abstract fun bootstrapStructures(context: BootstapContext<Structure>)
  abstract fun bootstrapSets(context: BootstapContext<StructureSet>)
}