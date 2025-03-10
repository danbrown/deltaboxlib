package com.dannbrown.deltaboxlib.registrate

import com.dannbrown.deltaboxlib.registrate.builders.*
import com.dannbrown.deltaboxlib.registrate.presets.blocks.BlockPresets
import com.dannbrown.deltaboxlib.registrate.providers.biomeModifier.BiomeModifierCodec
import com.dannbrown.deltaboxlib.registrate.providers.trades.*
import com.dannbrown.deltaboxlib.registrate.registry.*
import com.dannbrown.deltaboxlib.registrate.types.RecipeFactory
import com.dannbrown.deltaboxlib.registrate.util.ConfiguredFeaturesUtil
import com.dannbrown.deltaboxlib.registrate.util.PlacedFeaturesUtil
import com.mojang.serialization.Codec
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.npc.VillagerProfession
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType
import net.minecraft.data.worldgen.BootstapContext as BootstrapContext
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import java.util.function.Supplier

abstract class AbstractDeltaboxRegistrate(val modId: String) {
  val blockRegistry: BlockRegistry = BlockRegistry(modId)
  val itemRegistry: ItemRegistry = ItemRegistry(modId)
  val langRegistry: LangRegistry = LangRegistry(modId)
  val tagRegistry: TagRegistry = TagRegistry(modId)
  val recipeRegistry: RecipeRegistry = RecipeRegistry(modId)
  val creativeTabRegistry: CreativeTabRegistry = CreativeTabRegistry(modId)
  val tradesRegistry: TradeRegistry = TradeRegistry(modId)
  val placerTypeRegistry: PlacerTypeRegistry = PlacerTypeRegistry(modId)
  val configuredFeatureRegistry: ConfiguredFeatureRegistry = ConfiguredFeatureRegistry(modId)
  val placedFeatureRegistry: PlacedFeatureRegistry = PlacedFeatureRegistry(modId)
  val biomeModifierRegistry: BiomeModifierRegistry = BiomeModifierRegistry(modId)


  fun <T : Block> block(blockId: String): BlockBuilder<T> {
    return BlockBuilder(this, blockId)
  }

  fun <T : Block> blockPreset(blockId: String): BlockPresets<T> {
    return BlockPresets(this, blockId)
  }

  fun <T : Item> item(blockId: String): ItemBuilder<T> {
    return ItemBuilder(this, blockId)
  }

  fun <T : Block, R : Item> item(blockId: String, blockBuilder: BlockBuilder<T>): ItemBuilder<R> {
    return ItemBuilder(this, blockBuilder, blockId)
  }

  fun langs(_modId: String = modId): LangBuilder {
    return LangBuilder(this, _modId)
  }

  fun blockTags(hostTag: TagKey<Block>): BlockTagBuilder {
    return BlockTagBuilder(this, hostTag)
  }

  fun itemTags(hostTag: TagKey<Item>): ItemTagBuilder {
    return ItemTagBuilder(this, hostTag)
  }

  fun recipe(factory: RecipeFactory): AbstractDeltaboxRegistrate {
    recipeRegistry.addRecipe(factory)
    return this
  }

  fun creativeTab(
    id: String,
    phrase: String,
    icon: Supplier<ItemStack>,
    displayItems: CreativeModeTab.DisplayItemsGenerator,
  ): RegistrySupplier<CreativeModeTab> {
    this.langs().creativeTab(id, phrase)
    return this.creativeTabRegistry.register(id, icon, displayItems)
  }

  fun villagerTrade(
    profession: VillagerProfession,
    level: VillagerLevel,
    tradeCosts: List<VillagerTradeItem>,
    tradeSells: List<VillagerTradeItem>,
    maxUses: Int,
    xpAmount: Int,
    priceMultiplier: Float
  ): AbstractDeltaboxRegistrate {
    this.tradesRegistry.addTrade(
      VillagerTradeCodec(
        profession,
        level,
        tradeCosts,
        tradeSells,
        maxUses,
        xpAmount,
        priceMultiplier
      )
    )
    return this
  }

  fun wandererTrade(
    rarity: WandererTradeRarity,
    tradeCosts: List<VillagerTradeItem>,
    tradeSells: List<VillagerTradeItem>,
    maxUses: Int,
    xpAmount: Int,
    priceMultiplier: Float
  ): AbstractDeltaboxRegistrate {
    this.tradesRegistry.addWanderer(
      WandererTradeCodec(rarity, tradeCosts, tradeSells, maxUses, xpAmount, priceMultiplier)
    )
    return this
  }

  fun foliagePlacer(
    name: String, codec: Supplier<Codec<out FoliagePlacer>>
  ): RegistrySupplier<FoliagePlacerType<out FoliagePlacer>> {
    return this.placerTypeRegistry.registerFoliage(name, codec)
  }

  fun trunkPlacer(
    name: String, codec: Supplier<Codec<out TrunkPlacer>>
  ): RegistrySupplier<TrunkPlacerType<out TrunkPlacer>> {
    return this.placerTypeRegistry.registerTrunk(name, codec)
  }

  fun treeDecorator(
    name: String, codec: Supplier<Codec<out TreeDecorator>>
  ): RegistrySupplier<TreeDecoratorType<out TreeDecorator>> {
    return this.placerTypeRegistry.registerTreeDecorator(name, codec)
  }

  fun configuredFeature(
    name: String,
    consumer: (ResourceKey<ConfiguredFeature<*, *>>, BootstrapContext<ConfiguredFeature<*, *>>, ConfiguredFeaturesUtil) -> Unit
  ): ResourceKey<ConfiguredFeature<*, *>> {
    return configuredFeatureRegistry.addConfiguredfeature(name, consumer)
  }

  fun placedFeature(
    name: String,
    consumer: (ResourceKey<PlacedFeature>, BootstrapContext<PlacedFeature>, PlacedFeaturesUtil) -> Unit
  ): ResourceKey<PlacedFeature> {
    return placedFeatureRegistry.addPlacedFeature(name, consumer)
  }

  fun biomeModifier(
    name: String,
    biomeTag: TagKey<Biome>,
    placedFeature: ResourceKey<PlacedFeature>,
    step: GenerationStep.Decoration
  ) {
    val modifier = BiomeModifierCodec(biomeTag, placedFeature, step)
    this.biomeModifierRegistry.addBiomeModifier(name, modifier)
  }

  fun buildRegistries() {
    blockRegistry.build()
    itemRegistry.build()
    creativeTabRegistry.build()
    placerTypeRegistry.build()
  }
}