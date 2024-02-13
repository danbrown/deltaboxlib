package com.dannbrown.databoxlib.registry.worldgen

import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraft.util.random.SimpleWeightedRandomList
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.placement.PlacementModifier
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest

abstract class AbstractConfiguredFeaturesGen {
  abstract val modId: String
  fun registerKey(name: String): ResourceKey<ConfiguredFeature<*, *>> {
    return ResourceKey.create(
      Registries.CONFIGURED_FEATURE,
      ResourceLocation(modId, name)
    )
  }

  fun <FC : FeatureConfiguration?, F : Feature<FC>?> register(
    context: BootstapContext<ConfiguredFeature<*, *>>,
    key: ResourceKey<ConfiguredFeature<*, *>>, feature: F, configuration: FC
  ) {
    context.register(key, ConfiguredFeature(feature, configuration))
  }

  abstract fun bootstrap(context: BootstapContext<ConfiguredFeature<*, *>>)

  // Replaceables
  val stoneReplaceable: RuleTest = TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES)
  val deepslateReplaceables: RuleTest = TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES)
  val netherrackReplacables: RuleTest = BlockMatchTest(Blocks.NETHERRACK)
  val endReplaceables: RuleTest = BlockMatchTest(Blocks.END_STONE)
  val sandReplaceables: RuleTest = TagMatchTest(BlockTags.SAND)
  val basaltReplaceables: RuleTest = BlockMatchTest(Blocks.BASALT)


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