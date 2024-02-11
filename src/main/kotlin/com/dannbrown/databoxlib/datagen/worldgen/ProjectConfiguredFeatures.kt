package com.dannbrown.databoxlib.datagen.worldgen

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.content.worldgen.featureConfiguration.BoulderColumnConfig
import com.dannbrown.databoxlib.content.worldgen.featureConfiguration.BoulderConfig
import com.dannbrown.databoxlib.content.worldgen.featureConfiguration.MultiBlockStateConfiguration
import com.dannbrown.databoxlib.content.worldgen.featureConfiguration.SpikeConfig
import com.dannbrown.databoxlib.content.worldgen.trunkPlacer.ForkingStalkTrunkPlacer
import com.dannbrown.databoxlib.datagen.tags.ProjectBlockTags
import com.dannbrown.databoxlib.init.ProjectBlocks
import com.dannbrown.databoxlib.init.ProjectTags
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.data.worldgen.features.CaveFeatures
import net.minecraft.data.worldgen.features.FeatureUtils
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraft.util.random.SimpleWeightedRandomList
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SweetBerryBushBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.GeodeBlockSettings
import net.minecraft.world.level.levelgen.GeodeCrackSettings
import net.minecraft.world.level.levelgen.GeodeLayerSettings
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.ReplaceSphereConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration.TreeConfigurationBuilder
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize
import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.placement.PlacementModifier
import net.minecraft.world.level.levelgen.placement.RarityFilter
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest
import net.minecraft.world.level.material.Fluids

object ProjectConfiguredFeatures {
  val OVERWORLD_ALUMINIUM_ORE_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("aluminium_ore")
  val MOON_ALUMINIUM_ORE_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("moon_aluminium_ore")
  val OVERWORLD_TIN_ORE_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("tin_ore")
  val OVERWORLD_TUNGSTEN_ORE_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("tungsten_ore")
  val MOON_TUNGSTEN_ORE_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("moon_tungsten_ore")
  val RED_HEMATITE_IRON_ORE_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("red_hematite_iron_ore")
  val BASALT_ADAMANTIUM_ORE_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("basalt_adamantium_ore")
  val PHOSPHORITE_ORE_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("phosphorite_ore")
  val PYRITE_ORE_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("pyrite_ore")
  val OLIVINE_ORE_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("olivine_ore")
  val NORITE_ORE_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("norite_ore")
  val ICE_ORE_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("ice_ore")
  val SOUL_SOIL_ORE_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("soul_soil_ore")
  val JOSHUA_TREE_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("joshua_tree")
  val GEODE_BOULDER_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("geode_boulder")
  val ROSEATE_BOULDER_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("roseate_boulder")
  val ROSEATE_SMALL_BOULDER_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("roseate_small_boulder")
  val ROSEATE_LARGE_BOULDER_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("roseate_large_boulder")
  val ROSEATE_HOLLOW_BOULDER_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("roseate_hollow_boulder")
  val RED_SAND_BOULDER_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("red_sand_boulder")
  val RED_SANDSTONE_SMALL_BOULDER_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("red_sand_small_boulder")
  val RED_SANDSTONE_LARGE_BOULDER_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("red_sand_large_boulder")
  val ROSEATE_GEODE_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("roseate_geode")
  val STONE_BLOBS_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("block_blobs")
  val BASALT_BLOBS_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("basalt_blobs")
  val BOULDER_COLUMNS_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("boulder_columns")
  val SMALL_BOULDER_COLUMN_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("small_boulder_column")
  val TALL_BOULDER_COLUMN_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("tall_boulder_column")
  val BROWN_SLATE_VEGETATION_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("brown_slate_vegetation")
  val ROSEATE_SPIKES_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("roseate_spikes")
  val ROSEATE_SMALL_SPIKE_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("roseate_small_spike")
  val ROSEATE_TALL_SPIKE_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("roseate_tall_spike")
  val ROSEATE_GIANT_SPIKE_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("roseate_giant_spike")
  val PATCH_HIMALAYAN_SALT_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("patch_himalayan_salt")
  val CRATER_HOLE_MEDIUM_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("crater_hole_medium")
  val CRATER_HOLE_LARGE_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("crater_hole_large")
  val CRATER_HOLES_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("crater_holes")
  val SPARSE_DRY_GRASSES_SAND_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("sparse_dry_grasses_sand")
  val DRY_PATCHES_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("dry_patches")
  val MOSS_CARPET_PATCHES_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("moss_carpet_patches")
  val PATCH_DRY_SHRUB_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("patch_dry_shrub")
  val PATCH_DEAD_GRASS_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("patch_dead_grass")
  val PATCH_GUAYULE_SHRUB_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("patch_guayule_shrub")
  val PATCH_OCOTILLO_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("patch_ocotillo")
  val PATCH_BERRY_BUSH_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("patch_berry_bush")

  // FILTER KEYS
  private val SAND_FILTER: BlockPredicateFilter =
    BlockPredicateFilter.forPredicate(BlockPredicate.matchesTag(BlockPos.ZERO.below(), BlockTags.SAND))
  private val DIRT_FILTER: BlockPredicateFilter =
    BlockPredicateFilter.forPredicate(BlockPredicate.matchesTag(BlockPos.ZERO.below(), BlockTags.DIRT))
  private val ON_WATER_FILTER =
    BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(BlockPos.ZERO.below(), Fluids.WATER))

  // Bootstrap
  fun bootstrap(context: BootstapContext<ConfiguredFeature<*, *>>) {
    val stoneReplaceable: RuleTest = TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES)
    val deepslateReplaceables: RuleTest = TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES)
    val netherrackReplacables: RuleTest = BlockMatchTest(Blocks.NETHERRACK)
    val endReplaceables: RuleTest = BlockMatchTest(Blocks.END_STONE)
    val sandReplaceables: RuleTest = TagMatchTest(BlockTags.SAND)
    val moonslateReplaceables: RuleTest = TagMatchTest(ProjectTags.BLOCK.MOONSLATE_REPLACEABLES)
    val anorthositeReplaceables: RuleTest = TagMatchTest(ProjectTags.BLOCK.ANORTHOSITE_REPLACEABLES)
    val redHematiteReplaceables: RuleTest = TagMatchTest(ProjectTags.BLOCK.RED_HEMATITE_REPLACEABLES)
    val basaltReplaceables: RuleTest = BlockMatchTest(Blocks.BASALT)
    val lookup = context.lookup(Registries.CONFIGURED_FEATURE)
    // ALUMINIUM OVERWORLD
    register(
      context, OVERWORLD_ALUMINIUM_ORE_KEY, Feature.ORE, OreConfiguration(
        listOf(
          OreConfiguration.target(stoneReplaceable,
            ProjectBlocks.ALUMINIUM_ORE.get()
              .defaultBlockState()),
          OreConfiguration.target(deepslateReplaceables,
            ProjectBlocks.DEEPSLATE_ALUMINIUM_ORE.get()
              .defaultBlockState())
        ), 12
      )
    )
    // ALUMINIUM MOON
    register(
      context, MOON_ALUMINIUM_ORE_KEY, Feature.ORE, OreConfiguration(
        listOf(
          OreConfiguration.target(moonslateReplaceables,
            ProjectBlocks.MOONSLATE_ALUMINIUM_ORE.get()
              .defaultBlockState()),
          OreConfiguration.target(anorthositeReplaceables,
            ProjectBlocks.ANORTHOSITE_ALUMINIUM_ORE.get()
              .defaultBlockState())
        ), 15
      )
    )
    // TIN
//    register(
//      context, OVERWORLD_TIN_ORE_KEY, Feature.ORE, OreConfiguration(
//        listOf(
//          OreConfiguration.target(stoneReplaceable,
//            DataboxBlocks.TIN_ORE.get()
//              .defaultBlockState()),
//          OreConfiguration.target(deepslateReplaceables,
//            DataboxBlocks.DEEPSLATE_TIN_ORE.get()
//              .defaultBlockState())
//        ), 12
//      )
//    )
    // TUNGSTEN OVERWORLD
    register(
      context, OVERWORLD_TUNGSTEN_ORE_KEY, Feature.ORE, OreConfiguration(
        listOf(
          OreConfiguration.target(stoneReplaceable,
            ProjectBlocks.TUNGSTEN_ORE.get()
              .defaultBlockState()),
          OreConfiguration.target(deepslateReplaceables,
            ProjectBlocks.DEEPSLATE_TUNGSTEN_ORE.get()
              .defaultBlockState())
        ), 10
      )
    )
    // TUNGSTEN MOON
    register(
      context, MOON_TUNGSTEN_ORE_KEY, Feature.ORE, OreConfiguration(
        listOf(
          OreConfiguration.target(moonslateReplaceables,
            ProjectBlocks.MOONSLATE_TUNGSTEN_ORE.get()
              .defaultBlockState()),
          OreConfiguration.target(anorthositeReplaceables,
            ProjectBlocks.ANORTHOSITE_TUNGSTEN_ORE.get()
              .defaultBlockState())
        ), 8
      )
    )
    // ADAMANTIUM
//    register(
//      context, BASALT_ADAMANTIUM_ORE_KEY, Feature.ORE, OreConfiguration(
//        listOf(
//          OreConfiguration.target(basaltReplaceables,
//            DataboxBlocks.BASALT_ADAMANTIUM_ORE.get()
//              .defaultBlockState())
//        ), 2
//      )
//    )
    // RED HEMATITE IRON
    register(
      context, RED_HEMATITE_IRON_ORE_KEY, Feature.REPLACE_BLOBS, ReplaceSphereConfiguration(
        ProjectBlocks.RED_HEMATITE.get()
          .defaultBlockState(),
        ProjectBlocks.RED_HEMATITE_IRON_ORE.get()
          .defaultBlockState(),
        UniformInt.of(1, 2),
      )
    )
    // PHOSPHORITE
    register(
      context, PHOSPHORITE_ORE_KEY, Feature.ORE, OreConfiguration(
        listOf(
          OreConfiguration.target(stoneReplaceable,
            ProjectBlocks.PHOSPHORITE_FAMILY.MAIN!!.get()
              .defaultBlockState()),
          OreConfiguration.target(
            deepslateReplaceables,
            ProjectBlocks.PHOSPHORITE_FAMILY.MAIN!!.get()
              .defaultBlockState()
          )
        ), 64
      )
    )
    // PYRITE
    register(
      context, PYRITE_ORE_KEY, Feature.ORE, OreConfiguration(
        listOf(
          OreConfiguration.target(stoneReplaceable,
            ProjectBlocks.PYRITE_FAMILY.MAIN!!.get()
              .defaultBlockState()),
          OreConfiguration.target(deepslateReplaceables,
            ProjectBlocks.PYRITE_FAMILY.MAIN!!.get()
              .defaultBlockState())
        ), 64
      )
    )
    // OLIVINE
    register(
      context, OLIVINE_ORE_KEY, Feature.ORE, OreConfiguration(
        listOf(
          OreConfiguration.target(stoneReplaceable,
            ProjectBlocks.OLIVINE_FAMILY.MAIN!!.get()
              .defaultBlockState()),
          OreConfiguration.target(deepslateReplaceables,
            ProjectBlocks.OLIVINE_FAMILY.MAIN!!.get()
              .defaultBlockState())
        ), 64
      )
    )
    // NORITE
    register(
      context, NORITE_ORE_KEY, Feature.ORE, OreConfiguration(
        listOf(
          OreConfiguration.target(stoneReplaceable,
            ProjectBlocks.NORITE_FAMILY.MAIN!!.get()
              .defaultBlockState()),
          OreConfiguration.target(deepslateReplaceables,
            ProjectBlocks.NORITE_FAMILY.MAIN!!.get()
              .defaultBlockState())
        ), 64
      )
    )
    // ICE
    register(
      context, ICE_ORE_KEY, Feature.ORE, OreConfiguration(
        listOf(
          OreConfiguration.target(stoneReplaceable, Blocks.PACKED_ICE.defaultBlockState()),
          OreConfiguration.target(deepslateReplaceables, Blocks.PACKED_ICE.defaultBlockState())
        ), 64
      )
    )
    // SOUL SOIL
    register(
      context, SOUL_SOIL_ORE_KEY, Feature.ORE, OreConfiguration(
        listOf(
          OreConfiguration.target(stoneReplaceable, Blocks.SOUL_SOIL.defaultBlockState()),
          OreConfiguration.target(deepslateReplaceables, Blocks.SOUL_SOIL.defaultBlockState())
        ), 32
      )
    )
    // JOSHUA TREE
    register(
      context, JOSHUA_TREE_KEY, Feature.TREE, TreeConfigurationBuilder(
        BlockStateProvider.simple(ProjectBlocks.JOSHUA_FAMILY.STALK!!.get()),
        ForkingStalkTrunkPlacer(5, 6, 3),
        BlockStateProvider.simple(ProjectBlocks.JOSHUA_FAMILY.LEAVES!!.get()),
        AcaciaFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)),
        TwoLayersFeatureSize(1, 0, 2)
      )
        .build()
    )
    // BLOCK BLOBS
    register(
      context, STONE_BLOBS_KEY, ProjectFeatures.BLOCK_BLOBS.get(), MultiBlockStateConfiguration.Builder()
        .setSize(3)
        .setBlockProvider(
          weightedStateProvider(
            mapOf(
              Blocks.COBBLESTONE.defaultBlockState() to 4,
              Blocks.ANDESITE.defaultBlockState() to 3,
              Blocks.MOSSY_COBBLESTONE.defaultBlockState() to 2,
            )
          )
        )
        .setPlacementPredicates(
          listOf(
            BlockPredicate.matchesBlocks(Blocks.BROWN_TERRACOTTA),
            BlockPredicate.matchesBlocks(ProjectBlocks.COBBLED_BROWN_SLATE.get()),
            BlockPredicate.matchesTag(BlockTags.DIRT),
          )
        )
        .build()
    )
    // BLOCK BLOBS
    register(
      context, BASALT_BLOBS_KEY, ProjectFeatures.BLOCK_BLOBS.get(), MultiBlockStateConfiguration.Builder()
        .setSize(3)
        .setBlockProvider(
          weightedStateProvider(
            mapOf(
              Blocks.BASALT.defaultBlockState() to 6,
              Blocks.DEEPSLATE.defaultBlockState() to 4,
              Blocks.SMOOTH_BASALT.defaultBlockState() to 3,
              Blocks.POLISHED_BASALT.defaultBlockState() to 1,
            )
          )
        )
        .setPlacementPredicates(
          listOf(
            BlockPredicate.matchesTag(BlockTags.DIRT),
            BlockPredicate.matchesBlocks(Blocks.BROWN_TERRACOTTA),
            BlockPredicate.matchesBlocks(ProjectBlocks.COBBLED_BROWN_SLATE.get()),
            BlockPredicate.matchesTag(ProjectTags.BLOCK.IS_MOON_SURFACE)
          )
        )
        .build()
    )
    // GEODE BOULDER
    register(
      context, GEODE_BOULDER_KEY, ProjectFeatures.BOULDER.get(), BoulderConfig.Builder()
        .setRadiusSettings(
          BoulderConfig.RadiusSettings(
            UniformInt.of(16, 24), // x radius
            UniformInt.of(10, 16), 0, // y radius
            UniformInt.of(16, 24)
          ) // z radius
        )
        .setBlockProvider(
          weightedStateProvider(
            mapOf(
              Blocks.AIR.defaultBlockState() to 1,
            )
          )
        )
        .setMiddleBlockProvider(
          weightedStateProvider(
            mapOf(
              Blocks.AMETHYST_BLOCK.defaultBlockState() to 4,
              Blocks.BUDDING_AMETHYST.defaultBlockState() to 1
            )
          )
        )
        .setTopBlockProvider(
          weightedStateProvider(
            mapOf(
              Blocks.SMOOTH_BASALT.defaultBlockState() to 4,
              Blocks.DEEPSLATE.defaultBlockState() to 1
            )
          )
        )
        .setPlacementPredicates(
          listOf(
            BlockPredicate.matchesTag(BlockTags.SAND),
          )
        )
        .build()
    )
    // ROSEATE BOULDER
    register(
      context, ROSEATE_HOLLOW_BOULDER_KEY, ProjectFeatures.BOULDER.get(), BoulderConfig.Builder()
        .setRadiusSettings(
          BoulderConfig.RadiusSettings(
            UniformInt.of(16, 24), // x radius
            UniformInt.of(10, 16), 0, // y radius
            UniformInt.of(16, 24)
          ) // z radius
        )
        .setBlockProvider(
          weightedStateProvider(
            mapOf(
              Blocks.AIR.defaultBlockState() to 1,
            )
          )
        )
        .setMiddleBlockProvider(
          weightedStateProvider(
            mapOf(
              ProjectBlocks.RAW_HIMALAYAN_SALT.get()
                .defaultBlockState() to 3,
              ProjectBlocks.ROSEATE_SANDSTONE_PEBBLES.get()
                .defaultBlockState() to 1,
              ProjectBlocks.ROSEATE_GRAINS.get()
                .defaultBlockState() to 1,
            )
          )
        )
        .setTopBlockProvider(
          weightedStateProvider(
            mapOf(
              ProjectBlocks.ROSEATE_FAMILY.SANDSTONE!!.get()
                .defaultBlockState() to 4,
              ProjectBlocks.ROSEATE_FAMILY.SMOOTH!!.get()
                .defaultBlockState() to 2,
              ProjectBlocks.ROSEATE_SANDSTONE_PEBBLES.get()
                .defaultBlockState() to 2,
              ProjectBlocks.ROSEATE_GRAINS.get()
                .defaultBlockState() to 1,
            )
          )
        )
        .setPlacementPredicates(
          listOf(
            BlockPredicate.matchesTag(BlockTags.SAND),
          )
        )
        .build()
    )


    register(
      context, ROSEATE_LARGE_BOULDER_KEY, ProjectFeatures.BOULDER.get(), BoulderConfig.Builder()
        .setRadiusSettings(
          BoulderConfig.RadiusSettings(
            UniformInt.of(16, 24), // x radius
            UniformInt.of(10, 16), 0, // y radius
            UniformInt.of(16, 24)
          ) // z radius
        )
        .setBlockProvider(
          weightedStateProvider(
            mapOf(
              ProjectBlocks.ROSEATE_FAMILY.SANDSTONE!!.get()
                .defaultBlockState() to 4,
              ProjectBlocks.ROSEATE_FAMILY.SMOOTH!!.get()
                .defaultBlockState() to 2,
              ProjectBlocks.ROSEATE_SANDSTONE_PEBBLES.get()
                .defaultBlockState() to 2,
              ProjectBlocks.ROSEATE_GRAINS.get()
                .defaultBlockState() to 1,
            )
          )
        )
        .setMiddleBlockProvider(
          weightedStateProvider(
            mapOf(
              ProjectBlocks.ROSEATE_FAMILY.SANDSTONE!!.get()
                .defaultBlockState() to 4,
              ProjectBlocks.ROSEATE_FAMILY.SMOOTH!!.get()
                .defaultBlockState() to 2,
              ProjectBlocks.ROSEATE_SANDSTONE_PEBBLES.get()
                .defaultBlockState() to 2,
              ProjectBlocks.ROSEATE_GRAINS.get()
                .defaultBlockState() to 1,
            )
          )
        )
        .setTopBlockProvider(
          weightedStateProvider(
            mapOf(
              ProjectBlocks.ROSEATE_FAMILY.SANDSTONE!!.get()
                .defaultBlockState() to 4,
              ProjectBlocks.ROSEATE_FAMILY.SMOOTH!!.get()
                .defaultBlockState() to 2,
              ProjectBlocks.ROSEATE_SANDSTONE_PEBBLES.get()
                .defaultBlockState() to 2,
              ProjectBlocks.ROSEATE_GRAINS.get()
                .defaultBlockState() to 1,
            )
          )
        )
        .setPlacementPredicates(
          listOf(
            BlockPredicate.matchesTag(BlockTags.SAND),
          )
        )
        .build()
    )

    register(
      context, ROSEATE_SMALL_BOULDER_KEY, ProjectFeatures.BOULDER.get(), BoulderConfig.Builder()
        .setRadiusSettings(
          BoulderConfig.RadiusSettings(
            UniformInt.of(8, 16), // x radius
            UniformInt.of(5, 8), 0, // y radius
            UniformInt.of(8, 16)
          ) // z radius
        )
        .setBlockProvider(
          weightedStateProvider(
            mapOf(
              ProjectBlocks.ROSEATE_FAMILY.SANDSTONE!!.get()
                .defaultBlockState() to 4,
              ProjectBlocks.ROSEATE_FAMILY.SMOOTH!!.get()
                .defaultBlockState() to 2,
              ProjectBlocks.ROSEATE_SANDSTONE_PEBBLES.get()
                .defaultBlockState() to 2,
              ProjectBlocks.ROSEATE_GRAINS.get()
                .defaultBlockState() to 1,
            )
          )
        )
        .setMiddleBlockProvider(
          weightedStateProvider(
            mapOf(
              ProjectBlocks.ROSEATE_FAMILY.SANDSTONE!!.get()
                .defaultBlockState() to 4,
              ProjectBlocks.ROSEATE_FAMILY.SMOOTH!!.get()
                .defaultBlockState() to 2,
              ProjectBlocks.ROSEATE_SANDSTONE_PEBBLES.get()
                .defaultBlockState() to 2,
              ProjectBlocks.ROSEATE_GRAINS.get()
                .defaultBlockState() to 1,
            )
          )
        )
        .setTopBlockProvider(
          weightedStateProvider(
            mapOf(
              ProjectBlocks.ROSEATE_FAMILY.SANDSTONE!!.get()
                .defaultBlockState() to 4,
              ProjectBlocks.ROSEATE_FAMILY.SMOOTH!!.get()
                .defaultBlockState() to 2,
              ProjectBlocks.ROSEATE_SANDSTONE_PEBBLES.get()
                .defaultBlockState() to 2,
              ProjectBlocks.ROSEATE_GRAINS.get()
                .defaultBlockState() to 1,
            )
          )
        )
        .setPlacementPredicates(
          listOf(
            BlockPredicate.matchesTag(BlockTags.SAND),
          )
        )
        .build()
    )
    // ROSEATE BOULDER
    register(
      context, ROSEATE_BOULDER_KEY, Feature.RANDOM_SELECTOR, RandomFeatureConfiguration(
        listOf(
          WeightedPlacedFeature(directPlacedFeature(lookup.getOrThrow(ROSEATE_LARGE_BOULDER_KEY)), 0.75f),
          WeightedPlacedFeature(directPlacedFeature(lookup.getOrThrow(ROSEATE_HOLLOW_BOULDER_KEY)), 0.25f)
        ),
        directPlacedFeature(lookup.getOrThrow(ROSEATE_SMALL_BOULDER_KEY))
      )
    )
    // ROSEATE GEODE
    register(
      context, ROSEATE_GEODE_KEY, Feature.GEODE, GeodeConfiguration(
        GeodeBlockSettings(
          BlockStateProvider.simple(Blocks.AIR),
          BlockStateProvider.simple(ProjectBlocks.RAW_HIMALAYAN_SALT.get()),
          BlockStateProvider.simple(ProjectBlocks.RAW_HIMALAYAN_SALT.get()),
          BlockStateProvider.simple(Blocks.CALCITE),
          BlockStateProvider.simple(Blocks.SMOOTH_BASALT),
          listOf(
            ProjectBlocks.HIMALAYAN_SALT_CLUSTER.get()
              .defaultBlockState(),
//            Blocks.MEDIUM_AMETHYST_BUD.defaultBlockState(),
          ),
          BlockTags.FEATURES_CANNOT_REPLACE,
          BlockTags.GEODE_INVALID_BLOCKS
        ),
        GeodeLayerSettings(1.7, 2.2, 3.2, 4.2),
        GeodeCrackSettings(0.95, 2.0, 2),
        0.35,
        0.083,
        true,
        UniformInt.of(4, 6),
        UniformInt.of(3, 4),
        UniformInt.of(1, 2),
        -16,
        16,
        0.05,
        1
      )
    )
    // RED SAND BOULDER
    register(
      context, RED_SANDSTONE_LARGE_BOULDER_KEY, ProjectFeatures.BOULDER.get(), BoulderConfig.Builder()
        .setRadiusSettings(
          BoulderConfig.RadiusSettings(
            UniformInt.of(16, 24), // x radius
            UniformInt.of(10, 16), 0, // y radius
            UniformInt.of(16, 24)
          ) // z radius
        )
        .setBlockProvider(
          weightedStateProvider(
            mapOf(
              Blocks.RED_SANDSTONE.defaultBlockState() to 4,
              Blocks.SMOOTH_RED_SANDSTONE.defaultBlockState() to 2,
              ProjectBlocks.RED_SANDSTONE_PEBBLES.get()
                .defaultBlockState() to 2,
            )
          )
        )
        .setMiddleBlockProvider(
          weightedStateProvider(
            mapOf(
              Blocks.RED_SANDSTONE.defaultBlockState() to 4,
              Blocks.SMOOTH_RED_SANDSTONE.defaultBlockState() to 2,
              ProjectBlocks.RED_SANDSTONE_PEBBLES.get()
                .defaultBlockState() to 2,
            )
          )
        )
        .setTopBlockProvider(
          weightedStateProvider(
            mapOf(
              Blocks.RED_SANDSTONE.defaultBlockState() to 4,
              Blocks.ORANGE_TERRACOTTA.defaultBlockState() to 3,
              Blocks.SMOOTH_RED_SANDSTONE.defaultBlockState() to 2,
              ProjectBlocks.RED_SANDSTONE_PEBBLES.get()
                .defaultBlockState() to 2,
            )
          )
        )
        .setPlacementPredicates(
          listOf(
            BlockPredicate.matchesTag(BlockTags.SAND),
          )
        )
        .build()
    )

    register(
      context, RED_SANDSTONE_SMALL_BOULDER_KEY, ProjectFeatures.BOULDER.get(), BoulderConfig.Builder()
        .setRadiusSettings(
          BoulderConfig.RadiusSettings(
            UniformInt.of(8, 16), // x radius
            UniformInt.of(5, 8), 0, // y radius
            UniformInt.of(8, 16)
          ) // z radius
        )
        .setBlockProvider(
          weightedStateProvider(
            mapOf(
              Blocks.RED_SANDSTONE.defaultBlockState() to 4,
              Blocks.ORANGE_TERRACOTTA.defaultBlockState() to 3,
              Blocks.SMOOTH_RED_SANDSTONE.defaultBlockState() to 2,
              ProjectBlocks.RED_SANDSTONE_PEBBLES.get()
                .defaultBlockState() to 2,
            )
          )
        )
        .setMiddleBlockProvider(
          weightedStateProvider(
            mapOf(
              Blocks.RED_SANDSTONE.defaultBlockState() to 4,
              Blocks.SMOOTH_RED_SANDSTONE.defaultBlockState() to 2,
              ProjectBlocks.RED_SANDSTONE_PEBBLES.get()
                .defaultBlockState() to 2,
            )
          )
        )
        .setTopBlockProvider(
          weightedStateProvider(
            mapOf(
              Blocks.RED_SANDSTONE.defaultBlockState() to 4,
              Blocks.SMOOTH_RED_SANDSTONE.defaultBlockState() to 2,
              ProjectBlocks.RED_SANDSTONE_PEBBLES.get()
                .defaultBlockState() to 2,
            )
          )
        )
        .setPlacementPredicates(
          listOf(
            BlockPredicate.matchesTag(BlockTags.SAND),
          )
        )
        .build()
    )
    // RED SAND BOULDER
    register(
      context, RED_SAND_BOULDER_KEY, Feature.RANDOM_SELECTOR, RandomFeatureConfiguration(
        listOf(
          WeightedPlacedFeature(directPlacedFeature(lookup.getOrThrow(RED_SANDSTONE_LARGE_BOULDER_KEY)), 0.75f)
        ),
        directPlacedFeature(lookup.getOrThrow(RED_SANDSTONE_SMALL_BOULDER_KEY))
      )
    )
    // BASIC BOULDER COLUMN
    register(
      context, SMALL_BOULDER_COLUMN_KEY, ProjectFeatures.BOULDER_COLUMN.get(), BoulderColumnConfig.Builder()
        .setStackHeight(UniformInt.of(1, 3))
        .setPointed(false)
        .setRadiusSettings(
          BoulderColumnConfig.RadiusSettings(
            UniformInt.of(32, 40), // x radius
            UniformInt.of(10, 16), 0, // y radius
            UniformInt.of(32, 40)
          ) // z radius
        )
        .setBlockProvider(
          weightedStateProvider(
            mapOf(
              Blocks.DEEPSLATE.defaultBlockState() to 6,
              Blocks.COBBLED_DEEPSLATE.defaultBlockState() to 3,
              Blocks.AIR.defaultBlockState() to 1
            )
          )
        )
        .setMiddleBlockProvider(
          weightedStateProvider(
            mapOf(
              ProjectBlocks.BROWN_SLATE.get()
                .defaultBlockState() to 6,
              ProjectBlocks.COBBLED_BROWN_SLATE.get()
                .defaultBlockState() to 2,
              Blocks.AIR.defaultBlockState() to 1
            )
          )
        )
        .setTopBlockProvider(
          weightedStateProvider(
            mapOf(
              Blocks.MOSS_CARPET.defaultBlockState() to 5,
              Blocks.AIR.defaultBlockState() to 1
            )
          )
        )
        .setPlacementPredicates(
          listOf(
            BlockPredicate.matchesBlocks(Blocks.MUD),
            BlockPredicate.matchesBlocks(ProjectBlocks.COBBLED_BROWN_SLATE.get()),
            BlockPredicate.matchesBlocks(Blocks.BROWN_TERRACOTTA),
          )
        )
        .withSpawningFeatures(
          listOf(
            directPlacedFeature(lookup.getOrThrow(CaveFeatures.GLOW_LICHEN), RarityFilter.onAverageOnceEvery(20)),
            directPlacedFeature(lookup.getOrThrow(CaveFeatures.MOSS_PATCH), RarityFilter.onAverageOnceEvery(10)),
          )
        )
        .build()
    )
    // TALL BOULDER COLUMN
    register(
      context, TALL_BOULDER_COLUMN_KEY, ProjectFeatures.BOULDER_COLUMN.get(), BoulderColumnConfig.Builder()
        .setStackHeight(UniformInt.of(3, 7))
        .setPointed(true)
        .setRadiusSettings(
          BoulderColumnConfig.RadiusSettings(
            UniformInt.of(16, 24), // x radius
            UniformInt.of(10, 16), 0, // y radius
            UniformInt.of(16, 24)
          ) // z radius
        )
        .setBlockProvider(
          weightedStateProvider(
            mapOf(
              Blocks.DEEPSLATE.defaultBlockState() to 6,
              Blocks.COBBLED_DEEPSLATE.defaultBlockState() to 3,
              Blocks.AIR.defaultBlockState() to 1
            )
          )
        )
        .setMiddleBlockProvider(
          weightedStateProvider(
            mapOf(
              ProjectBlocks.BROWN_SLATE.get()
                .defaultBlockState() to 6,
              ProjectBlocks.COBBLED_BROWN_SLATE.get()
                .defaultBlockState() to 2,
              Blocks.AIR.defaultBlockState() to 1
            )
          )
        )
        .setTopBlockProvider(
          weightedStateProvider(
            mapOf(
              Blocks.MOSS_CARPET.defaultBlockState() to 5,
              Blocks.AIR.defaultBlockState() to 1
            )
          )
        )
        .setPlacementPredicates(
          listOf(
            BlockPredicate.matchesBlocks(Blocks.MUD),
            BlockPredicate.matchesBlocks(ProjectBlocks.COBBLED_BROWN_SLATE.get()),
            BlockPredicate.matchesBlocks(Blocks.BROWN_TERRACOTTA),
          )
        )
        .withSpawningFeatures(
          listOf(
            directPlacedFeature(lookup.getOrThrow(CaveFeatures.GLOW_LICHEN), RarityFilter.onAverageOnceEvery(20)),
            directPlacedFeature(lookup.getOrThrow(CaveFeatures.MOSS_PATCH), RarityFilter.onAverageOnceEvery(10)),
          )
        )
        .build()
    )
    // BOULDER COLUMNS
    register(
      context, BOULDER_COLUMNS_KEY, Feature.RANDOM_SELECTOR, RandomFeatureConfiguration(
        listOf(
          WeightedPlacedFeature(directPlacedFeature(lookup.getOrThrow(TALL_BOULDER_COLUMN_KEY)), 0.75f)
        ),
        directPlacedFeature(lookup.getOrThrow(SMALL_BOULDER_COLUMN_KEY))
      )
    )
    // ROSEATE SPIKES
    register(
      context, ROSEATE_SMALL_SPIKE_KEY, ProjectFeatures.SPIKE.get(), SpikeConfig.Builder()
        .setBlockProvider(
          weightedStateProvider(
            mapOf(
              ProjectBlocks.ROSEATE_FAMILY.SANDSTONE!!.get()
                .defaultBlockState() to 4,
              ProjectBlocks.ROSEATE_FAMILY.SMOOTH!!.get()
                .defaultBlockState() to 2,
              ProjectBlocks.ROSEATE_SANDSTONE_PEBBLES.get()
                .defaultBlockState() to 2,
              ProjectBlocks.ROSEATE_GRAINS.get()
                .defaultBlockState() to 1,
            )
          )
        )
        .setHeightVariance(UniformInt.of(15, 40))
        .build()
    )

    register(
      context, ROSEATE_TALL_SPIKE_KEY, ProjectFeatures.SPIKE.get(), SpikeConfig.Builder()
        .setBlockProvider(
          weightedStateProvider(
            mapOf(
              ProjectBlocks.ROSEATE_FAMILY.SANDSTONE!!.get()
                .defaultBlockState() to 6,
              ProjectBlocks.ROSEATE_FAMILY.SMOOTH!!.get()
                .defaultBlockState() to 3,
              ProjectBlocks.ROSEATE_SANDSTONE_PEBBLES.get()
                .defaultBlockState() to 2,
              ProjectBlocks.ROSEATE_GRAINS.get()
                .defaultBlockState() to 1,
              Blocks.SMOOTH_SANDSTONE.defaultBlockState() to 1
            )
          )
        )
        .setHeightVariance(UniformInt.of(40, 75))
        .build()
    )

    register(
      context, ROSEATE_GIANT_SPIKE_KEY, ProjectFeatures.SPIKE.get(), SpikeConfig.Builder()
        .setBlockProvider(
          weightedStateProvider(
            mapOf(
              ProjectBlocks.ROSEATE_FAMILY.SANDSTONE!!.get()
                .defaultBlockState() to 6,
              ProjectBlocks.ROSEATE_FAMILY.SMOOTH!!.get()
                .defaultBlockState() to 3,
              ProjectBlocks.ROSEATE_SANDSTONE_PEBBLES.get()
                .defaultBlockState() to 2,
              ProjectBlocks.ROSEATE_GRAINS.get()
                .defaultBlockState() to 1,
              Blocks.SMOOTH_SANDSTONE.defaultBlockState() to 1
            )
          )
        )
        .setHeightVariance(UniformInt.of(75, 90))
        .build()
    )

    register(
      context, ROSEATE_SPIKES_KEY, Feature.RANDOM_SELECTOR, RandomFeatureConfiguration(
        listOf(
          WeightedPlacedFeature(directPlacedFeature(lookup.getOrThrow(ROSEATE_GIANT_SPIKE_KEY)), 0.25f),
          WeightedPlacedFeature(directPlacedFeature(lookup.getOrThrow(ROSEATE_TALL_SPIKE_KEY)), 0.75f)
        ),
        directPlacedFeature(lookup.getOrThrow(ROSEATE_SMALL_SPIKE_KEY))
      )
    )
    // HIMALAYAN SALT CLUSTER
    register(
      context, PATCH_HIMALAYAN_SALT_KEY, Feature.RANDOM_PATCH, FeatureUtils.simpleRandomPatchConfiguration(
        8,
        PlacementUtils.filtered(
          Feature.SIMPLE_BLOCK,
          SimpleBlockConfiguration(
            BlockStateProvider.simple(ProjectBlocks.HIMALAYAN_SALT_CLUSTER.get())
          ),
          BlockPredicate.allOf(
            BlockPredicate.ONLY_IN_AIR_PREDICATE,
            BlockPredicate.anyOf(
              BlockPredicate.matchesTag(Direction.DOWN.normal, BlockTags.SAND),
            ),
          )
        )
      )
    )
    // CRATER HOLES
    register(
      context, CRATER_HOLE_MEDIUM_KEY, ProjectFeatures.BOULDER.get(), BoulderConfig.Builder()
        .setRadiusSettings(
          BoulderConfig.RadiusSettings(
            UniformInt.of(8, 16), // x radius
            UniformInt.of(5, 8), 0, // y radius
            UniformInt.of(8, 16)
          ) // z radius
        )
        .setBlockProvider(
          weightedStateProvider(
            mapOf(
              Blocks.AIR.defaultBlockState() to 1,
            )
          )
        )
        .setMiddleBlockProvider(
          weightedStateProvider(
            mapOf(
              Blocks.AIR.defaultBlockState() to 1,
            )
          )
        )
        .setTopBlockProvider(
          weightedStateProvider(
            mapOf(
              Blocks.AIR.defaultBlockState() to 1,
            )
          )
        )
        .setPlacementPredicates(
          listOf(
            BlockPredicate.matchesTag(BlockTags.SAND),
            BlockPredicate.matchesTag(BlockTags.DIRT),
            BlockPredicate.matchesTag(BlockTags.TERRACOTTA),
          )
        )
        .build()
    )
    register(
      context, CRATER_HOLE_LARGE_KEY, ProjectFeatures.BOULDER.get(), BoulderConfig.Builder()
        .setRadiusSettings(
          BoulderConfig.RadiusSettings(
            UniformInt.of(16, 32), // x radius
            UniformInt.of(8, 12), 0, // y radius
            UniformInt.of(16, 32)
          ) // z radius
        )
        .setBlockProvider(
          weightedStateProvider(
            mapOf(
              Blocks.AIR.defaultBlockState() to 1,
            )
          )
        )
        .setMiddleBlockProvider(
          weightedStateProvider(
            mapOf(
              Blocks.AIR.defaultBlockState() to 1,
            )
          )
        )
        .setTopBlockProvider(
          weightedStateProvider(
            mapOf(
              Blocks.AIR.defaultBlockState() to 1,
            )
          )
        )
        .setPlacementPredicates(
          listOf(
            BlockPredicate.matchesTag(BlockTags.SAND),
            BlockPredicate.matchesTag(BlockTags.DIRT),
            BlockPredicate.matchesTag(BlockTags.TERRACOTTA),
          )
        )
        .build()
    )
    // CRATER HOLES
    register(
      context, CRATER_HOLES_KEY, Feature.RANDOM_SELECTOR, RandomFeatureConfiguration(
        listOf(
          WeightedPlacedFeature(directPlacedFeature(lookup.getOrThrow(CRATER_HOLE_LARGE_KEY)), 0.65f)
        ),
        directPlacedFeature(lookup.getOrThrow(CRATER_HOLE_MEDIUM_KEY))
      )
    )
    // SPARSE GRASSES
    register(
      context, SPARSE_DRY_GRASSES_SAND_KEY, Feature.RANDOM_SELECTOR, RandomFeatureConfiguration(
        listOf(
          WeightedPlacedFeature(
            PlacementUtils.inlinePlaced(
              Feature.RANDOM_PATCH,
              FeatureUtils.simplePatchConfiguration(
                Feature.SIMPLE_BLOCK,
                SimpleBlockConfiguration(
                  BlockStateProvider.simple(ProjectBlocks.TALL_SPARSE_DRY_GRASS.get())
                ),
                listOf(), 32,
              ), SAND_FILTER
            ),
            0.33f
          )
        ),
        PlacementUtils.inlinePlaced(
          Feature.RANDOM_PATCH,
          FeatureUtils.simplePatchConfiguration(
            Feature.SIMPLE_BLOCK,
            SimpleBlockConfiguration(
              BlockStateProvider.simple(ProjectBlocks.SPARSE_DRY_GRASS.get())
            ), listOf(), 32
          ), SAND_FILTER
        ),
      )
    )
    // DRY PATCHES
    register(
      context, DRY_PATCHES_KEY, Feature.RANDOM_PATCH, FeatureUtils.simpleRandomPatchConfiguration(
        96,
        PlacementUtils.filtered(
          Feature.SIMPLE_BLOCK,
          SimpleBlockConfiguration(
            BlockStateProvider.simple(ProjectBlocks.DRY_PATCHES.get())
          ),
          BlockPredicate.allOf(
            BlockPredicate.ONLY_IN_AIR_PREDICATE,
            BlockPredicate.anyOf(
              BlockPredicate.matchesTag(Direction.DOWN.normal, BlockTags.SAND),
              BlockPredicate.matchesTag(Direction.DOWN.normal, BlockTags.DIRT),
              BlockPredicate.matchesTag(Direction.DOWN.normal, BlockTags.TERRACOTTA),
              BlockPredicate.matchesBlocks(Direction.DOWN.normal, ProjectBlocks.COBBLED_BROWN_SLATE.get()),
            ),
          )
        )
      )
    )
    // MOSS CARPET PATCHES
    register(
      context, MOSS_CARPET_PATCHES_KEY, Feature.RANDOM_PATCH, FeatureUtils.simpleRandomPatchConfiguration(
        96,
        PlacementUtils.filtered(
          Feature.SIMPLE_BLOCK,
          SimpleBlockConfiguration(
            BlockStateProvider.simple(Blocks.MOSS_CARPET)
          ),
          BlockPredicate.allOf(
            BlockPredicate.ONLY_IN_AIR_PREDICATE,
            BlockPredicate.anyOf(
              BlockPredicate.matchesTag(Direction.DOWN.normal, BlockTags.SAND),
              BlockPredicate.matchesTag(Direction.DOWN.normal, BlockTags.DIRT),
              BlockPredicate.matchesTag(Direction.DOWN.normal, BlockTags.TERRACOTTA),
              BlockPredicate.matchesBlocks(Direction.DOWN.normal, ProjectBlocks.COBBLED_BROWN_SLATE.get()),
            ),
          )
        )
      )
    )
    // DRYS SHRUBS
    register(
      context, PATCH_DRY_SHRUB_KEY, Feature.RANDOM_PATCH, FeatureUtils.simpleRandomPatchConfiguration(
        6,
        PlacementUtils.filtered(
          Feature.SIMPLE_BLOCK,
          SimpleBlockConfiguration(
            BlockStateProvider.simple(ProjectBlocks.DRY_SHRUB.get())
          ),
          BlockPredicate.allOf(
            BlockPredicate.ONLY_IN_AIR_PREDICATE,
            BlockPredicate.anyOf(
              BlockPredicate.matchesTag(Direction.DOWN.normal, BlockTags.SAND),
              BlockPredicate.matchesTag(Direction.DOWN.normal, BlockTags.DIRT),
              BlockPredicate.matchesTag(Direction.DOWN.normal, BlockTags.TERRACOTTA),
              BlockPredicate.matchesBlocks(Direction.DOWN.normal, ProjectBlocks.COBBLED_BROWN_SLATE.get()),
            ),
          )
        )
      )
    )
    // DEAD GRASS
    register(
      context, PATCH_DEAD_GRASS_KEY, Feature.RANDOM_PATCH, FeatureUtils.simpleRandomPatchConfiguration(
        6,
        PlacementUtils.filtered(
          Feature.SIMPLE_BLOCK,
          SimpleBlockConfiguration(
            BlockStateProvider.simple(ProjectBlocks.DEAD_GRASS.get())
          ),
          BlockPredicate.allOf(
            BlockPredicate.ONLY_IN_AIR_PREDICATE,
            BlockPredicate.anyOf(
              BlockPredicate.matchesTag(Direction.DOWN.normal, BlockTags.SAND),
              BlockPredicate.matchesTag(Direction.DOWN.normal, BlockTags.DIRT),
              BlockPredicate.matchesTag(Direction.DOWN.normal, BlockTags.TERRACOTTA),
            ),
          )
        )
      )
    )
    // GUAYULE SHRUBS
    register(
      context, PATCH_GUAYULE_SHRUB_KEY, Feature.RANDOM_PATCH, FeatureUtils.simpleRandomPatchConfiguration(
        6,
        PlacementUtils.filtered(
          Feature.SIMPLE_BLOCK,
          SimpleBlockConfiguration(
            BlockStateProvider.simple(ProjectBlocks.GUAYULE_SHRUB.get())
          ),
          BlockPredicate.allOf(
            BlockPredicate.ONLY_IN_AIR_PREDICATE,
            BlockPredicate.anyOf(
              BlockPredicate.matchesTag(Direction.DOWN.normal, BlockTags.SAND),
            ),
          )
        )
      )
    )
    // OCOTILLO
    register(
      context, PATCH_OCOTILLO_KEY, Feature.RANDOM_PATCH, FeatureUtils.simpleRandomPatchConfiguration(
        5,
        PlacementUtils.filtered(
          Feature.SIMPLE_BLOCK,
          SimpleBlockConfiguration(
            BlockStateProvider.simple(ProjectBlocks.OCOTILLO.get())
          ),
          BlockPredicate.allOf(
            BlockPredicate.ONLY_IN_AIR_PREDICATE,
            BlockPredicate.anyOf(
              BlockPredicate.matchesTag(Direction.DOWN.normal, BlockTags.SAND),
              BlockPredicate.matchesTag(Direction.DOWN.normal, BlockTags.DIRT),
            ),
          )
        )
      )
    )
    // BROWN SLATE VEGETATION
    register(
      context, BROWN_SLATE_VEGETATION_KEY, Feature.SIMPLE_RANDOM_SELECTOR,
      SimpleRandomFeatureConfiguration(
        HolderSet.direct(
          PlacementUtils.inlinePlaced(
            Feature.RANDOM_PATCH,
            FeatureUtils.simplePatchConfiguration(
              Feature.SIMPLE_BLOCK,
              SimpleBlockConfiguration(
                BlockStateProvider.simple(ProjectBlocks.OCOTILLO.get())
              )
            )
          ),
          PlacementUtils.inlinePlaced(
            Feature.RANDOM_PATCH,
            FeatureUtils.simplePatchConfiguration(
              Feature.SIMPLE_BLOCK,
              SimpleBlockConfiguration(
                BlockStateProvider.simple(ProjectBlocks.DRY_PATCHES.get())
              )
            )
          ),
          PlacementUtils.inlinePlaced(
            Feature.RANDOM_PATCH,
            FeatureUtils.simplePatchConfiguration(
              Feature.SIMPLE_BLOCK,
              SimpleBlockConfiguration(
                BlockStateProvider.simple(Blocks.MOSS_CARPET)
              )
            )
          ),
          PlacementUtils.inlinePlaced(
            Feature.NO_BONEMEAL_FLOWER,
            FeatureUtils.simplePatchConfiguration(
              Feature.SIMPLE_BLOCK,
              SimpleBlockConfiguration(
                BlockStateProvider.simple(ProjectBlocks.DRY_SHRUB.get())
              )
            )
          ),
          PlacementUtils.inlinePlaced(
            Feature.RANDOM_PATCH,
            FeatureUtils.simpleRandomPatchConfiguration(
              32, PlacementUtils.onlyWhenEmpty<SimpleBlockConfiguration, Feature<SimpleBlockConfiguration>>(
                Feature.SIMPLE_BLOCK,
                SimpleBlockConfiguration(BlockStateProvider.simple(ProjectBlocks.SPARSE_DRY_GRASS.get()))
              )
            )
          )
        )
      )
    )
    // PATCH BERRY BUSH
    register(
      context,
      PATCH_BERRY_BUSH_KEY,
      Feature.RANDOM_PATCH,
      FeatureUtils.simpleRandomPatchConfiguration(
        96,
        PlacementUtils.filtered(
          Feature.SIMPLE_BLOCK,
          SimpleBlockConfiguration(
            BlockStateProvider.simple(Blocks.SWEET_BERRY_BUSH.defaultBlockState()
              .setValue(SweetBerryBushBlock.AGE, 3))
          ),
          BlockPredicate.allOf(
            BlockPredicate.ONLY_IN_AIR_PREDICATE,
            BlockPredicate.anyOf(
              BlockPredicate.matchesTag(Direction.DOWN.normal, BlockTags.DIRT),
            ),
          )
        )
      )
    )
    // -----
  }

  fun registerKey(name: String?): ResourceKey<ConfiguredFeature<*, *>> {
    return ResourceKey.create(
      Registries.CONFIGURED_FEATURE,
      ResourceLocation(ProjectContent.MOD_ID, name)
    )
  }

  private fun <FC : FeatureConfiguration?, F : Feature<FC>?> register(
    context: BootstapContext<ConfiguredFeature<*, *>>,
    key: ResourceKey<ConfiguredFeature<*, *>>, feature: F, configuration: FC
  ) {
    context.register(key, ConfiguredFeature(feature, configuration))
  }

  // Utils
  // facilitate the creation of a WeightedStateProvider
  fun weightedStateProvider(blocks: Map<BlockState, Int>): WeightedStateProvider {
    val builder = SimpleWeightedRandomList.builder<BlockState>()
    blocks.forEach { (state, weight) -> builder.add(state, weight) }
    return WeightedStateProvider(builder.build())
  }

  fun directPlacedFeature(
    configuredFeature: Holder<ConfiguredFeature<*, *>>,
    vararg modifiers: PlacementModifier
  ): Holder<PlacedFeature> {
    return Holder.direct(PlacedFeature(configuredFeature, listOf(*modifiers)))
  }
}