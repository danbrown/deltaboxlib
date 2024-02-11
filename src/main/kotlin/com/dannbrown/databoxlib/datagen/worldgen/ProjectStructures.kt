package com.dannbrown.databoxlib.datagen.worldgen

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.content.worldgen.structures.ArchStructure
import com.dannbrown.databoxlib.content.worldgen.structures.PlatformStructure
import com.dannbrown.databoxlib.init.ProjectBlocks
import com.dannbrown.databoxlib.init.ProjectTags
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.random.SimpleWeightedRandomList
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider
import net.minecraft.world.level.levelgen.structure.Structure
import net.minecraft.world.level.levelgen.structure.Structure.StructureSettings
import net.minecraft.world.level.levelgen.structure.StructureSet
import net.minecraft.world.level.levelgen.structure.StructureSet.StructureSelectionEntry
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType
import java.util.Map

object ProjectStructures {
  // @ STRUCTURES
  val SAMPLE_PLATFORM =
    ResourceKey.create(Registries.STRUCTURE, ResourceLocation(ProjectContent.MOD_ID, "sample_platform"))
  val LUSH_RED_ROCK_ARCH =
    ResourceKey.create(Registries.STRUCTURE, ResourceLocation(ProjectContent.MOD_ID, "lush_red_rock_arch"))
  val RED_ROCK_ARCH =
    ResourceKey.create(Registries.STRUCTURE, ResourceLocation(ProjectContent.MOD_ID, "red_rock_arch"))
  val GRANITE_RED_ROCK_ARCH =
    ResourceKey.create(Registries.STRUCTURE, ResourceLocation(ProjectContent.MOD_ID, "granite_red_rock_arch"))

  // @ STRUCTURE SETS
  val SAMPLE_PLATFORM_SET =
    ResourceKey.create(Registries.STRUCTURE_SET, ResourceLocation(ProjectContent.MOD_ID, "sample_platform_set"))
  val RED_ROCK_ARCH_SET =
    ResourceKey.create(Registries.STRUCTURE_SET, ResourceLocation(ProjectContent.MOD_ID, "red_rock_arch_set"))
  val LUSH_RED_ROCK_ARCH_SET =
    ResourceKey.create(Registries.STRUCTURE_SET, ResourceLocation(ProjectContent.MOD_ID, "lush_red_rock_arch_set"))

  // registration
  fun bootstrapStructures(context: BootstapContext<Structure>) {
    val biomes = context.lookup(Registries.BIOME)
    val pools = context.lookup(Registries.TEMPLATE_POOL)
    val lookup = context.lookup(Registries.CONFIGURED_FEATURE)
    // SAMPLE PLATFORM, INSPIRED BY AETHER CLOUDS
    context.register(
      SAMPLE_PLATFORM,
      PlatformStructure(
        StructureSettings(
          biomes.getOrThrow(ProjectTags.BIOME.HAS_SAMPLE_PLATFORM),
          Map.of(),
          GenerationStep.Decoration.SURFACE_STRUCTURES,
          TerrainAdjustment.BEARD_THIN
        ),
        WeightedStateProvider(
          SimpleWeightedRandomList.builder<BlockState>()
            .add(ProjectBlocks.PHOSPHORUS_BLOCK.defaultState, 1)
            .add(ProjectBlocks.PHOSPHORITE_FAMILY.MAIN!!.defaultState, 4)
            .add(ProjectBlocks.PHOSPHORITE_FAMILY.POLISHED!!.defaultState, 3)
        ),
        5
      )
    )
    // -----
    // RED ROCK ARCH
    context.register(RED_ROCK_ARCH, ArchStructure.Companion.PRESETS(biomes, lookup).RED_ROCK_ARCH_PRESET)
    context.register(
      GRANITE_RED_ROCK_ARCH,
      ArchStructure.Companion.PRESETS(biomes, lookup).GRANITE_RED_ROCK_ARCH_PRESET
    )
    context.register(LUSH_RED_ROCK_ARCH, ArchStructure.Companion.PRESETS(biomes, lookup).LUSH_RED_ROCK_ARCH_PRESET)
    // -----
  }

  fun bootstrapSets(context: BootstapContext<StructureSet?>) {
    val structures = context.lookup(Registries.STRUCTURE)
    // SAMPLE PLATFORM
    context.register(
      SAMPLE_PLATFORM_SET,
      StructureSet(
        structures.getOrThrow(SAMPLE_PLATFORM),
        RandomSpreadStructurePlacement(24, 12, RandomSpreadType.LINEAR, 276320045)
      )
    )
    // -----
    // RED ROCK ARCH
    context.register(
      RED_ROCK_ARCH_SET,
      StructureSet(
        listOf(
          StructureSelectionEntry(structures.getOrThrow(RED_ROCK_ARCH), 3),
          StructureSelectionEntry(structures.getOrThrow(GRANITE_RED_ROCK_ARCH), 1)
        ),
        RandomSpreadStructurePlacement(15, 8, RandomSpreadType.LINEAR, 498548954)
      )
    )
    context.register(
      LUSH_RED_ROCK_ARCH_SET,
      StructureSet(
        listOf(
          StructureSelectionEntry(structures.getOrThrow(LUSH_RED_ROCK_ARCH), 3),
          StructureSelectionEntry(structures.getOrThrow(RED_ROCK_ARCH), 1),
        ),
        RandomSpreadStructurePlacement(15, 8, RandomSpreadType.LINEAR, 498552986)
      )
    )
    // -----
  }
  // ----
}