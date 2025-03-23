package com.dannbrown.deltaboxlib.registrate

import com.dannbrown.deltaboxlib.registrate.builders.*
import com.dannbrown.deltaboxlib.registrate.presets.blocks.BlockPresets
import com.dannbrown.deltaboxlib.registrate.providers.biomeModifier.BiomeModifierCodec
import com.dannbrown.deltaboxlib.registrate.providers.biomeModifier.BiomeSpawnCodec
import com.dannbrown.deltaboxlib.registrate.providers.trades.*
import com.dannbrown.deltaboxlib.registrate.registry.*
import com.dannbrown.deltaboxlib.registrate.types.RecipeFactory
import com.dannbrown.deltaboxlib.registrate.util.*
import com.mojang.serialization.Codec
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.advancements.Advancement
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.dispenser.DispenseItemBehavior
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvent
import net.minecraft.tags.TagKey
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.decoration.PaintingVariant
import net.minecraft.world.entity.npc.VillagerProfession
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.properties.BlockSetType
import net.minecraft.world.level.block.state.properties.WoodType
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType
import net.minecraft.data.worldgen.BootstapContext as BootstrapContext
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.presets.WorldPreset
import net.minecraft.world.level.material.Fluid
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
  val modelLayersRegistry: ModelLayerRegistry = ModelLayerRegistry(modId)
  val boatVariantRegistry: BoatVariantRegistry = BoatVariantRegistry(modId)
  val blockEntityRegistry: BlockEntityRegistry = BlockEntityRegistry(modId)
  val entityTypeRegistry: EntityTypeRegistry = EntityTypeRegistry(modId)
  val particleRegistry: ParticleRegistry = ParticleRegistry(modId)
  val woodTypesRegistry: WoodTypeRegistry = WoodTypeRegistry(modId)
  val soundRegistry: SoundRegistry = SoundRegistry(modId)
  val biomeRegistry: BiomeRegistry = BiomeRegistry(modId)
  val dimensionRegistry: DimensionRegistry = DimensionRegistry(modId)
  val attributeRegistry: AttributeRegistry = AttributeRegistry(modId)
  val configRegistry: ConfigRegistry = ConfigRegistry(modId)
  val effectRegistry: EffectRegistry = EffectRegistry(modId)
  val dispenserBehaviorRegistry: DispenserBehaviorRegistry = DispenserBehaviorRegistry(modId)
  val featureRegistry: FeatureRegistry = FeatureRegistry(modId)
  val advancementRegistry: AdvancementRegistry = AdvancementRegistry(modId)

  fun <T : Block> block(blockId: String): BlockBuilder<T> {
    return BlockBuilder(this, blockId)
  }

  fun <T : Block> blockPreset(blockId: String): BlockPresets<T> {
    return BlockPresets(this, blockId)
  }

  fun blockfamily(blockId: String): BlockFamilyGeneratorBuilder {
    return BlockFamilyGeneratorBuilder(this, blockId)
  }

  fun <T : Item> item(itemId: String): ItemBuilder<T> {
    return ItemBuilder(this, itemId)
  }

  fun <T : Block, R : Item> item(blockId: String, blockBuilder: BlockBuilder<T>): ItemBuilder<R> {
    return ItemBuilder(this, blockBuilder, blockId)
  }

  fun <T : BlockEntity> blockEntity(blockEntityId: String): BlockEntityBuilder<T> {
    return BlockEntityBuilder(this, blockEntityId)
  }

  fun <T : Entity> entityType(entityId: String): EntityTypeBuilder<T> {
    return EntityTypeBuilder(this, entityId)
  }

  fun langs(_modId: String = modId): LangBuilder {
    return LangBuilder(this, _modId)
  }

  fun blockTags(hostTag: TagKey<Block>): TagBuilder<Block> {
    return object : TagBuilder<Block>(this, hostTag) {
      override fun register() {
        tagRegistry.addBlock(hostTag, this)
      }
    }
  }

  fun itemTags(hostTag: TagKey<Item>): TagBuilder<Item> {
    return object : TagBuilder<Item>(this, hostTag) {
      override fun register() {
        tagRegistry.addItem(hostTag, this)
      }
    }
  }

  fun fluidTags(hostTag: TagKey<Fluid>): TagBuilder<Fluid> {
    return object : TagBuilder<Fluid>(this, hostTag) {
      override fun register() {
        tagRegistry.addFluid(hostTag, this)
      }
    }
  }

  fun biomeTags(hostTag: TagKey<Biome>): TagBuilder<Biome> {
    return object : TagBuilder<Biome>(this, hostTag) {
      override fun register() {
        tagRegistry.addBiome(hostTag, this)
      }
    }
  }

  fun entityTags(hostTag: TagKey<EntityType<*>>): TagBuilder<EntityType<*>> {
    return object : TagBuilder<EntityType<*>>(this, hostTag) {
      override fun register() {
        tagRegistry.addEntity(hostTag, this)
      }
    }
  }

  fun paintingTags(hostTag: TagKey<PaintingVariant>): TagBuilder<PaintingVariant> {
    return object : TagBuilder<PaintingVariant>(this, hostTag) {
      override fun register() {
        tagRegistry.addPainting(hostTag, this)
      }
    }
  }

  fun worldPresetTags(hostTag: TagKey<WorldPreset>): TagBuilder<WorldPreset> {
    return object : TagBuilder<WorldPreset>(this, hostTag) {
      override fun register() {
        tagRegistry.addWorldPreset(hostTag, this)
      }
    }
  }

  fun deltaboxBlockTag(path: String): TagKey<Block> {
    val tag = DeltaboxUtil.TAGS.deltaboxBlockTag(path)
    this.blockTags(tag)
      .add(*DeltaboxUtil.TAGS.modloaderBlockTag(path).toTypedArray())
      .register()
    return tag
  }

  fun deltaboxItemTag(path: String): TagKey<Item> {
    val tag = DeltaboxUtil.TAGS.deltaboxItemTag(path)
    this.itemTags(tag)
      .add(*DeltaboxUtil.TAGS.modloaderItemTag(path).toTypedArray())
      .register()
    return tag
  }

  fun deltaboxFluidTag(path: String): TagKey<Fluid> {
    val tag = DeltaboxUtil.TAGS.deltaboxFluidTag(path)
    this.fluidTags(tag)
      .add(*DeltaboxUtil.TAGS.modloaderFluidTag(path).toTypedArray())
      .register()
    return tag
  }

  fun deltaboxEntityTypeTag(path: String): TagKey<EntityType<*>> {
    val tag = DeltaboxUtil.TAGS.deltaboxEntityTag(path)
    this.entityTags(tag)
      .add(*DeltaboxUtil.TAGS.modloaderEntityTag(path).toTypedArray())
      .register()
    return tag
  }

  fun deltaboxBiomeTag(path: String): TagKey<Biome> {
    val tag = DeltaboxUtil.TAGS.deltaboxBiomeTag(path)
    this.biomeTags(tag)
      .add(*DeltaboxUtil.TAGS.modloaderBiomeTag(path).toTypedArray())
      .register()
    return tag
  }

  fun deltaboxPaintingTag(path: String): TagKey<PaintingVariant> {
    val tag = DeltaboxUtil.TAGS.deltaboxPaintingTag(path)
    this.paintingTags(tag)
      .add(*DeltaboxUtil.TAGS.modloaderPaintingTag(path).toTypedArray())
      .register()
    return tag
  }

  fun deltaboxWorldPresetTag(path: String): TagKey<WorldPreset> {
    val tag = DeltaboxUtil.TAGS.deltaboxWorldPresetTag(path)
    this.worldPresetTags(tag)
      .add(*DeltaboxUtil.TAGS.modloaderWorldPresetTag(path).toTypedArray())
      .register()
    return tag
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

  fun biomeSpawn(
    name: String,
    biomeTag: TagKey<Biome>,
    entityType: EntityType<*>,
    weight: Int,
    minCount: Int,
    maxCount: Int
  ) {
    val entityLocation =
      DeltaboxUtil.resourceLocation(DeltaboxUtil.getEntityModId(entityType), DeltaboxUtil.getEntityId(entityType))
    val entityTypeKey = ResourceKey.create(Registries.ENTITY_TYPE, entityLocation)
    val spawn = BiomeSpawnCodec(biomeTag, entityTypeKey, weight, minCount, maxCount)
    this.biomeModifierRegistry.addBiomeSpawn(name, spawn)
  }

  fun modelLayer(
    path: String, model: Supplier<LayerDefinition>, folder: String = "main"
  ): ModelLayerLocation {
    return this.modelLayersRegistry.add(path, model, folder)
  }

  fun boatVariant(name: String): AbstractDeltaboxRegistrate {
    this.boatVariantRegistry.add(name)
    return this
  }

  fun blockSet(name: String): BlockSetType {
    return this.woodTypesRegistry.addBlockSet(name)
  }

  fun woodType(name: String, blockSet: BlockSetType): WoodType {
    return this.woodTypesRegistry.addWoodType(name, blockSet)
  }

  fun <T : ParticleOptions> particleType(
    name: String, supplier: Supplier<ParticleType<T>>,
    provider: (sprite: SpriteSet) -> ParticleProvider<T>
  ): RegistrySupplier<ParticleType<T>> {
    return this.particleRegistry.particleType(name, supplier, provider)
  }

  fun soundEvent(name: String, count: Int, range: Float): Supplier<SoundEvent> {
    val location = DeltaboxUtil.resourceLocation(modId, name)
    this.langRegistry.register("sounds.${modId}.${name}", DeltaboxUtil.asName(name))
    return this.soundRegistry.register(name, count, { SoundEvent.createFixedRangeEvent(location, range) })
  }

  fun soundEvent(name: String, count: Int): Supplier<SoundEvent> {
    val location = DeltaboxUtil.resourceLocation(modId, name)
    this.langRegistry.register("sounds.${modId}.${name}", DeltaboxUtil.asName(name))
    return this.soundRegistry.register(name, count, { SoundEvent.createVariableRangeEvent(location) })
  }

  fun biome(biome: AbstractBiome): AbstractBiome {
    this.biomeRegistry.addBiome(biome)
    return biome
  }

  fun attribute(name: String, attributeSupplier: Supplier<Attribute>): Supplier<Attribute> {
    return this.attributeRegistry.register(name, attributeSupplier)
  }

  fun <T : FeatureConfiguration> feature(name: String, featureupplier: Supplier<Feature<T>>): Supplier<Feature<T>> {
    return this.featureRegistry.register(name, featureupplier)
  }

  fun dimension(dimension: AbstractDimension): AbstractDimension {
    this.dimensionRegistry.addDimension(dimension)
    return dimension
  }

  fun mobEffect(name: String, effect: Supplier<MobEffect>): Supplier<MobEffect> {
    this.langs().effect(name, DeltaboxUtil.asName(name))
    return this.effectRegistry.register(name, effect)
  }

  fun dispenserBehavior(itemEntry: Supplier<ItemLike>, behavior: DispenseItemBehavior): AbstractDeltaboxRegistrate {
    this.dispenserBehaviorRegistry.register(itemEntry, behavior)
    return this
  }

  fun advancement(
    name: String,
    title: String,
    description: String,
    consumer: (String, AdvancementUtil, Advancement.Builder) -> Advancement
  ): Advancement {
    this.langs().advancement(name, title, description)
    return this.advancementRegistry.addAdvancement(name, consumer)
  }

  fun configBoolean(
    key: String,
    defaultValue: Boolean,
    comment: String? = null
  ): ConfigRegistry.ConfigSupplier<Boolean> {
    return configRegistry.registerBoolean(key, defaultValue, comment)
  }

  fun configInt(key: String, defaultValue: Int, comment: String? = null): ConfigRegistry.ConfigSupplier<Int> {
    return configRegistry.registerInt(key, defaultValue, comment)
  }

  fun configFloat(key: String, defaultValue: Float, comment: String? = null): ConfigRegistry.ConfigSupplier<Float> {
    return configRegistry.registerFloat(key, defaultValue, comment)
  }

  fun configString(key: String, defaultValue: String, comment: String? = null): ConfigRegistry.ConfigSupplier<String> {
    return configRegistry.registerString(key, defaultValue, comment)
  }

  fun buildRegistries() {
    configRegistry.loadConfig()
    blockRegistry.build()
    itemRegistry.build()
    creativeTabRegistry.build()
    placerTypeRegistry.build()
    entityTypeRegistry.build()
    blockEntityRegistry.build()
    particleRegistry.build()
    soundRegistry.build()
    attributeRegistry.build()
    effectRegistry.build()
    featureRegistry.build()
  }

  fun buildBlocks() {
    blockRegistry.build()
  }

  fun buildItems() {
    itemRegistry.build()
  }

  fun buildCreativeTabs() {
    creativeTabRegistry.build()
  }

  fun buildPlacerTypes() {
    placerTypeRegistry.build()
  }

  fun buildEntityTypes() {
    entityTypeRegistry.build()
  }

  fun buildBlockEntities() {
    blockEntityRegistry.build()
  }

  fun buildParticles() {
    particleRegistry.build()
  }

  fun buildSounds() {
    soundRegistry.build()
  }

  fun buildAttributes() {
    attributeRegistry.build()
  }

  fun buildEffects() {
    effectRegistry.build()
  }

  fun buildFeatures() {
    featureRegistry.build()
  }

  fun freezeConfig() {
    configRegistry.loadConfig()
  }
}