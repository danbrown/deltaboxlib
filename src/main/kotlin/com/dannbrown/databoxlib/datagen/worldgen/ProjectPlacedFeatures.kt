package com.dannbrown.databoxlib.datagen.worldgen

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.init.ProjectBlocks
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.data.worldgen.placement.VegetationPlacements
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.valueproviders.ClampedInt
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.placement.BiomeFilter
import net.minecraft.world.level.levelgen.placement.CountPlacement
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement
import net.minecraft.world.level.levelgen.placement.InSquarePlacement
import net.minecraft.world.level.levelgen.placement.NoiseThresholdCountPlacement
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.placement.PlacementModifier
import net.minecraft.world.level.levelgen.placement.RarityFilter

object ProjectPlacedFeatures {
  // Registering
  private fun registerKey(name: String): ResourceKey<PlacedFeature> {
    return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation(ProjectContent.MOD_ID, name))
  }

  private fun register(
    context: BootstapContext<PlacedFeature>,
    key: ResourceKey<PlacedFeature>,
    configuration: Holder<ConfiguredFeature<*, *>>,
    modifiers: List<PlacementModifier>
  ) {
    context.register(key, PlacedFeature(configuration, java.util.List.copyOf(modifiers)))
  }

  // Content
  val ALUMINIUM_ORE_PLACED: ResourceKey<PlacedFeature> = registerKey("aluminium_ore_placed")
  val MOON_ALUMINIUM_ORE_PLACED: ResourceKey<PlacedFeature> = registerKey("moon_aluminium_ore_placed")

  //  val TIN_ORE_PLACED: ResourceKey<PlacedFeature> = registerKey("tin_ore_placed")
  val TUNGSTEN_ORE_PLACED: ResourceKey<PlacedFeature> = registerKey("tungsten_ore_placed")
  val MOON_TUNGSTEN_ORE_PLACED: ResourceKey<PlacedFeature> = registerKey("moon_tungsten_ore_placed")

  //  val BASALT_ADAMANTIUM_ORE_PLACED: ResourceKey<PlacedFeature> = registerKey("basalt_adamantium_ore_placed")
  val PHOSPHORITE_ORE_UPPER_PLACED: ResourceKey<PlacedFeature> = registerKey("phosphorite_ore_upper_placed")
  val PHOSPHORITE_ORE_LOWER_PLACED: ResourceKey<PlacedFeature> = registerKey("phosphorite_ore_lower_placed")
  val PYRITE_ORE_UPPER_PLACED: ResourceKey<PlacedFeature> = registerKey("pyrite_ore_upper_placed")
  val PYRITE_ORE_LOWER_PLACED: ResourceKey<PlacedFeature> = registerKey("pyrite_ore_lower_placed")
  val OLIVINE_ORE_UPPER_PLACED: ResourceKey<PlacedFeature> = registerKey("olivine_ore_upper_placed")
  val OLIVINE_ORE_LOWER_PLACED: ResourceKey<PlacedFeature> = registerKey("olivine_ore_lower_placed")
  val NORITE_ORE_UPPER_PLACED: ResourceKey<PlacedFeature> = registerKey("norite_ore_upper_placed")
  val NORITE_ORE_LOWER_PLACED: ResourceKey<PlacedFeature> = registerKey("norite_ore_lower_placed")
  val ICE_ORE_UPPER_PLACED: ResourceKey<PlacedFeature> = registerKey("ice_ore_upper_placed")
  val ICE_ORE_LOWER_PLACED: ResourceKey<PlacedFeature> = registerKey("ice_ore_lower_placed")
  val SOUL_SOIL_ORE_UPPER_PLACED: ResourceKey<PlacedFeature> = registerKey("soul_soil_upper_placed")
  val SOUL_SOIL_ORE_LOWER_PLACED: ResourceKey<PlacedFeature> = registerKey("soul_soil_lower_placed")
  val JOSHUA_PLACED: ResourceKey<PlacedFeature> = registerKey("joshua_placed")
  val RED_SAND_BOULDER_PLACED: ResourceKey<PlacedFeature> = registerKey("red_sand_boulder_placed")
  val STONE_BLOBS_PLACED: ResourceKey<PlacedFeature> = registerKey("stone_blobs_placed")
  val BASALT_BLOBS_PLACED: ResourceKey<PlacedFeature> = registerKey("basalt_blobs_placed")
  val BOULDER_COLUMNS_PLACED: ResourceKey<PlacedFeature> = registerKey("boulder_columns_placed")
  val BROWN_SLATE_VEGETATION_PLACED: ResourceKey<PlacedFeature> = registerKey("brown_slate_vegetation_placed")
  val SPARSE_DRY_GRASSES_PLACED: ResourceKey<PlacedFeature> = registerKey("sparse_dry_grasses_placed")
  val DRY_PATCHES_NOISE_PLACED: ResourceKey<PlacedFeature> = registerKey("dry_patches_noise_placed")
  val MOSS_CARPET_NOISE_PLACED: ResourceKey<PlacedFeature> = registerKey("moss_carpets_noise_placed")
  val PATCH_DRY_SHRUBS_PLACED: ResourceKey<PlacedFeature> = registerKey("patch_dry_shrubs_placed")
  val PATCH_DEAD_GRASSES_PLACED: ResourceKey<PlacedFeature> = registerKey("patch_dead_grasses_placed")
  val PATCH_GUAYULE_SHRUBS_PLACED: ResourceKey<PlacedFeature> = registerKey("patch_guayule_shrubs_placed")
  val PATCH_OCOTILLO_PLACED: ResourceKey<PlacedFeature> = registerKey("patch_ocotillo_placed")
  val PATCH_BERRY_BUSH_PLACED: ResourceKey<PlacedFeature> = registerKey("patch_berry_bush_placed")
  val ROSEATE_SPIKES_PLACED: ResourceKey<PlacedFeature> = registerKey("roseate_spikes_placed")
  val ROSEATE_BOULDER_PLACED: ResourceKey<PlacedFeature> = registerKey("roseate_boulder_placed")
  val ROSEATE_GEODE_PLACED: ResourceKey<PlacedFeature> = registerKey("roseate_geode_placed")
  val PATCH_HIMALAYAN_SALT_PLACED: ResourceKey<PlacedFeature> = registerKey("patch_himalayan_salt_placed")
  val CRATER_HOLES_PLACED: ResourceKey<PlacedFeature> = registerKey("crater_holes_placed")
  fun bootstrap(context: BootstapContext<PlacedFeature>) {
    val configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE)
    // ALUMINIUM
    register(
      context,
      ALUMINIUM_ORE_PLACED,
      configuredFeatures.getOrThrow(ProjectConfiguredFeatures.OVERWORLD_ALUMINIUM_ORE_KEY),
      commonOrePlacement(
        12,  // VeinsPerChunk
        HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(320))
      )
    )
    // MOON ALUMINIUM
    register(
      context,
      MOON_ALUMINIUM_ORE_PLACED,
      configuredFeatures.getOrThrow(ProjectConfiguredFeatures.MOON_ALUMINIUM_ORE_KEY),
      commonOrePlacement(
        14,  // VeinsPerChunk
        HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(320))
      )
    )
//    // TIN
//    register(
//      context, TIN_ORE_PLACED, configuredFeatures.getOrThrow(DataboxConfiguredFeatures.OVERWORLD_TIN_ORE_KEY),
//      commonOrePlacement(
//        12,  // VeinsPerChunk
//        HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(320))
//      )
//    )
    // TUNGSTEN
    register(
      context,
      TUNGSTEN_ORE_PLACED,
      configuredFeatures.getOrThrow(ProjectConfiguredFeatures.OVERWORLD_TUNGSTEN_ORE_KEY),
      commonOrePlacement(
        10,  // VeinsPerChunk
        HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(32))
      )
    )
    // MOON TUNGSTEN
    register(
      context,
      MOON_TUNGSTEN_ORE_PLACED,
      configuredFeatures.getOrThrow(ProjectConfiguredFeatures.MOON_TUNGSTEN_ORE_KEY),
      commonOrePlacement(
        12,  // VeinsPerChunk
        HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(32))
      )
    )
//    // ADAMANTIUM
//    register(
//      context,
//      BASALT_ADAMANTIUM_ORE_PLACED,
//      configuredFeatures.getOrThrow(DataboxConfiguredFeatures.BASALT_ADAMANTIUM_ORE_KEY),
//      commonOrePlacement(
//        12,  // VeinsPerChunk
//        HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(320))
//      )
//    )
    // PHOSPHORITE
    register(
      context,
      PHOSPHORITE_ORE_UPPER_PLACED,
      configuredFeatures.getOrThrow(ProjectConfiguredFeatures.PHOSPHORITE_ORE_KEY),
      rareOrePlacement(
        2,  // VeinsPerChunk
        HeightRangePlacement.triangle(VerticalAnchor.absolute(80), VerticalAnchor.absolute(384))
      )
    )
    register(
      context,
      PHOSPHORITE_ORE_LOWER_PLACED,
      configuredFeatures.getOrThrow(ProjectConfiguredFeatures.PHOSPHORITE_ORE_KEY),
      commonOrePlacement(
        2,  // VeinsPerChunk
        HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(72))
      )
    )
    // PYRITE
    register(
      context,
      PYRITE_ORE_UPPER_PLACED,
      configuredFeatures.getOrThrow(ProjectConfiguredFeatures.PYRITE_ORE_KEY),
      rareOrePlacement(
        2,  // VeinsPerChunk
        HeightRangePlacement.triangle(VerticalAnchor.absolute(80), VerticalAnchor.absolute(384))
      )
    )
    register(
      context,
      PYRITE_ORE_LOWER_PLACED,
      configuredFeatures.getOrThrow(ProjectConfiguredFeatures.PYRITE_ORE_KEY),
      commonOrePlacement(
        2,  // VeinsPerChunk
        HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(72))
      )
    )
    // OLIVINE
    register(
      context,
      OLIVINE_ORE_UPPER_PLACED,
      configuredFeatures.getOrThrow(ProjectConfiguredFeatures.OLIVINE_ORE_KEY),
      rareOrePlacement(
        2,  // VeinsPerChunk
        HeightRangePlacement.triangle(VerticalAnchor.absolute(80), VerticalAnchor.absolute(384))
      )
    )
    register(
      context,
      OLIVINE_ORE_LOWER_PLACED,
      configuredFeatures.getOrThrow(ProjectConfiguredFeatures.OLIVINE_ORE_KEY),
      commonOrePlacement(
        2,  // VeinsPerChunk
        HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(72))
      )
    )
    // NORITE
    register(
      context,
      NORITE_ORE_UPPER_PLACED,
      configuredFeatures.getOrThrow(ProjectConfiguredFeatures.NORITE_ORE_KEY),
      rareOrePlacement(
        2,  // VeinsPerChunk
        HeightRangePlacement.triangle(VerticalAnchor.absolute(80), VerticalAnchor.absolute(384))
      )
    )
    register(
      context,
      NORITE_ORE_LOWER_PLACED,
      configuredFeatures.getOrThrow(ProjectConfiguredFeatures.NORITE_ORE_KEY),
      commonOrePlacement(
        2,  // VeinsPerChunk
        HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(72))
      )
    )
    // ICE
    register(
      context,
      ICE_ORE_UPPER_PLACED,
      configuredFeatures.getOrThrow(ProjectConfiguredFeatures.ICE_ORE_KEY),
      commonOrePlacement(
        2,  // VeinsPerChunk
        HeightRangePlacement.triangle(VerticalAnchor.absolute(80), VerticalAnchor.absolute(384))
      )
    )
    register(
      context,
      ICE_ORE_LOWER_PLACED,
      configuredFeatures.getOrThrow(ProjectConfiguredFeatures.ICE_ORE_KEY),
      commonOrePlacement(
        2,  // VeinsPerChunk
        HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(72))
      )
    )
    // SOUL SOIL
    register(
      context,
      SOUL_SOIL_ORE_UPPER_PLACED,
      configuredFeatures.getOrThrow(ProjectConfiguredFeatures.SOUL_SOIL_ORE_KEY),
      commonOrePlacement(
        2,  // VeinsPerChunk
        HeightRangePlacement.triangle(VerticalAnchor.absolute(80), VerticalAnchor.absolute(384))
      )
    )
    register(
      context,
      SOUL_SOIL_ORE_LOWER_PLACED,
      configuredFeatures.getOrThrow(ProjectConfiguredFeatures.SOUL_SOIL_ORE_KEY),
      commonOrePlacement(
        2,  // VeinsPerChunk
        HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(72))
      )
    )
    // JOSHUA TREE
    register(
      context, JOSHUA_PLACED, configuredFeatures.getOrThrow(ProjectConfiguredFeatures.JOSHUA_TREE_KEY),
      VegetationPlacements.treePlacement(
        RarityFilter.onAverageOnceEvery(8),
        ProjectBlocks.JOSHUA_FAMILY.SAPLING!!.get()
      )
    )
    // BLOCK BLOBS
    register(
      context, STONE_BLOBS_PLACED, configuredFeatures.getOrThrow(ProjectConfiguredFeatures.STONE_BLOBS_KEY),
      listOf(
        CountPlacement.of(2),
        InSquarePlacement.spread(),
        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
        BiomeFilter.biome(),
        RarityFilter.onAverageOnceEvery(15),
      )
    )
    // BASALT BLOBS
    register(
      context, BASALT_BLOBS_PLACED, configuredFeatures.getOrThrow(ProjectConfiguredFeatures.BASALT_BLOBS_KEY),
      listOf(
        CountPlacement.of(2),
        InSquarePlacement.spread(),
        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
        BiomeFilter.biome(),
        RarityFilter.onAverageOnceEvery(15),
      )
    )
    // ROSEATE BOULDER
    register(
      context, ROSEATE_BOULDER_PLACED, configuredFeatures.getOrThrow(ProjectConfiguredFeatures.ROSEATE_BOULDER_KEY),
      listOf(
        CountPlacement.of(1),
        InSquarePlacement.spread(),
        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
        BiomeFilter.biome(),
        RarityFilter.onAverageOnceEvery(40),
      )
    )
    // RED SAND BOULDER
    register(
      context, RED_SAND_BOULDER_PLACED, configuredFeatures.getOrThrow(ProjectConfiguredFeatures.RED_SAND_BOULDER_KEY),
      listOf(
        CountPlacement.of(1),
        InSquarePlacement.spread(),
        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
        BiomeFilter.biome(),
        RarityFilter.onAverageOnceEvery(50),
      )
    )
    // BOULDER COLUMNS
    register(
      context, BOULDER_COLUMNS_PLACED, configuredFeatures.getOrThrow(ProjectConfiguredFeatures.BOULDER_COLUMNS_KEY),
      listOf(
        CountPlacement.of(1),
        InSquarePlacement.spread(),
        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
        BiomeFilter.biome(),
        RarityFilter.onAverageOnceEvery(30),
      )
    )
    // ROSEATE SPIKES
    register(
      context, ROSEATE_SPIKES_PLACED, configuredFeatures.getOrThrow(ProjectConfiguredFeatures.ROSEATE_SPIKES_KEY),
      listOf(
        CountPlacement.of(1),
        InSquarePlacement.spread(),
        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
        BiomeFilter.biome(),
        RarityFilter.onAverageOnceEvery(15),
      )
    )
    // ROSEATE GEODE
    register(
      context, ROSEATE_GEODE_PLACED, configuredFeatures.getOrThrow(ProjectConfiguredFeatures.ROSEATE_GEODE_KEY),
      listOf(
        RarityFilter.onAverageOnceEvery(20),
        InSquarePlacement.spread(),
        HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(6), VerticalAnchor.absolute(50)),
        BiomeFilter.biome()
      )
    )
    // PATCH HIMALAYAN SALT CLUSTERS
    register(
      context,
      PATCH_HIMALAYAN_SALT_PLACED,
      configuredFeatures.getOrThrow(ProjectConfiguredFeatures.PATCH_HIMALAYAN_SALT_KEY),
      listOf(
        InSquarePlacement.spread(),
        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
        BiomeFilter.biome(),
        RarityFilter.onAverageOnceEvery(2),
      )
    )
    // SPARSE DRY GRASSES
    register(
      context,
      SPARSE_DRY_GRASSES_PLACED,
      configuredFeatures.getOrThrow(ProjectConfiguredFeatures.SPARSE_DRY_GRASSES_SAND_KEY),
      listOf(
        InSquarePlacement.spread(),
        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
        BiomeFilter.biome(),
        RarityFilter.onAverageOnceEvery(2),
      )
    )
    // DRY PATCHES
    register(
      context, DRY_PATCHES_NOISE_PLACED, configuredFeatures.getOrThrow(ProjectConfiguredFeatures.DRY_PATCHES_KEY),
      listOf(
        NoiseThresholdCountPlacement.of(-0.25, 7, 0),
        InSquarePlacement.spread(),
        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
        BiomeFilter.biome(),
        RarityFilter.onAverageOnceEvery(6),
      )
    )
    // MOSS CARPETS
    register(
      context,
      MOSS_CARPET_NOISE_PLACED,
      configuredFeatures.getOrThrow(ProjectConfiguredFeatures.MOSS_CARPET_PATCHES_KEY),
      listOf(
        NoiseThresholdCountPlacement.of(-0.45, 3, 1),
        InSquarePlacement.spread(),
        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
        BiomeFilter.biome(),
        RarityFilter.onAverageOnceEvery(6),
      )
    )
    // PATCH DRY SHRUBS
    register(
      context, PATCH_DRY_SHRUBS_PLACED, configuredFeatures.getOrThrow(ProjectConfiguredFeatures.PATCH_DRY_SHRUB_KEY),
      listOf(
        InSquarePlacement.spread(),
        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
        BiomeFilter.biome(),
        RarityFilter.onAverageOnceEvery(3),
      )
    )
    // PATCH DEAD GRASSES
    register(
      context, PATCH_DEAD_GRASSES_PLACED, configuredFeatures.getOrThrow(ProjectConfiguredFeatures.PATCH_DEAD_GRASS_KEY),
      listOf(
        InSquarePlacement.spread(),
        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
        BiomeFilter.biome(),
        RarityFilter.onAverageOnceEvery(3),
      )
    )
    // PATCH GUAYULE SHRUBS
    register(
      context,
      PATCH_GUAYULE_SHRUBS_PLACED,
      configuredFeatures.getOrThrow(ProjectConfiguredFeatures.PATCH_GUAYULE_SHRUB_KEY),
      listOf(
        InSquarePlacement.spread(),
        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
        BiomeFilter.biome(),
        RarityFilter.onAverageOnceEvery(3),
      )
    )
    // PATCH OCOTILLO
    register(
      context, PATCH_OCOTILLO_PLACED, configuredFeatures.getOrThrow(ProjectConfiguredFeatures.PATCH_OCOTILLO_KEY),
      listOf(
        InSquarePlacement.spread(),
        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
        BiomeFilter.biome(),
        RarityFilter.onAverageOnceEvery(2),
      )
    )
    // PATCH BERRY
    register(
      context, PATCH_BERRY_BUSH_PLACED, configuredFeatures.getOrThrow(ProjectConfiguredFeatures.PATCH_BERRY_BUSH_KEY),
      listOf(
        InSquarePlacement.spread(),
        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
        BiomeFilter.biome(),
        RarityFilter.onAverageOnceEvery(20),
      )
    )
    // BROWN SLATE VEGETATION
    register(
      context,
      BROWN_SLATE_VEGETATION_PLACED,
      configuredFeatures.getOrThrow(ProjectConfiguredFeatures.BROWN_SLATE_VEGETATION_KEY),
      listOf(
        RarityFilter.onAverageOnceEvery(2),
        InSquarePlacement.spread(),
        PlacementUtils.HEIGHTMAP,
        CountPlacement.of(ClampedInt.of(UniformInt.of(-3, 1), 0, 1)),
        BiomeFilter.biome()
      )
    )
    // CRATER HOLES
    register(
      context, CRATER_HOLES_PLACED, configuredFeatures.getOrThrow(ProjectConfiguredFeatures.CRATER_HOLES_KEY),
      listOf(
        CountPlacement.of(1),
        InSquarePlacement.spread(),
        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
        BiomeFilter.biome(),
        RarityFilter.onAverageOnceEvery(10),
      )
    )
    // ----
  }

  // Utility Functions
  fun orePlacement(placementModifier: PlacementModifier, modifier: PlacementModifier): List<PlacementModifier> {
    return listOf(placementModifier, InSquarePlacement.spread(), modifier, BiomeFilter.biome())
  }

  fun commonOrePlacement(pCount: Int, pHeightRange: PlacementModifier): List<PlacementModifier> {
    return orePlacement(CountPlacement.of(pCount), pHeightRange)
  }

  fun rareOrePlacement(pChance: Int, pHeightRange: PlacementModifier): List<PlacementModifier> {
    return orePlacement(RarityFilter.onAverageOnceEvery(pChance), pHeightRange)
  }
}