package com.dannbrown.deltaboxlib.registrate.datagen

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import net.minecraft.advancements.critereon.BlockPredicate
import net.minecraft.advancements.critereon.EnchantmentPredicate
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.advancements.critereon.LocationPredicate
import net.minecraft.advancements.critereon.MinMaxBounds
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.DoorBlock
import net.minecraft.world.level.block.DoublePlantBlock
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import net.minecraft.world.level.block.state.properties.SlabType
import net.minecraft.world.level.storage.loot.BuiltInLootTables
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.predicates.LocationCheck
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition
import net.minecraft.world.level.storage.loot.predicates.MatchTool
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer
import java.util.function.Function
import java.util.function.Supplier

abstract class RegistrateBlockLootTables(val registrate: AbstractDeltaboxRegistrate) :
  BlockLootSubProvider(setOf(), FeatureFlags.REGISTRY.allFlags()), DataProvider {
  // new functions for Registrate
  fun noLoot(block: Supplier<Block>) {
    super.add(block.get(), LootTable.lootTable())
  }

  fun pottedBlock(block: Supplier<Block>, plant: Supplier<Block>) {
    super.add(
      block.get(), LootTable.lootTable()
        .withPool(
          applyExplosionCondition(
            Blocks.FLOWER_POT, LootPool.lootPool()
              .setRolls(ConstantValue.exactly(1.0f))
              .add(LootItem.lootTableItem(Blocks.FLOWER_POT))
          )
        )
        .withPool(
          applyExplosionCondition(
            plant.get(), LootPool.lootPool()
              .setRolls(ConstantValue.exactly(1.0f))
              .add(LootItem.lootTableItem(plant.get()))
          )
        )
    )
  }

  fun dropSlab(block: Block) {
    super.add(
      block, LootTable.lootTable().withPool(
        LootPool.lootPool()
          .setRolls(ConstantValue.exactly(1f))
          .add(
            super.applyExplosionDecay(
              block, LootItem.lootTableItem(block)
                .apply(
                  SetItemCountFunction.setCount(ConstantValue.exactly(2f))
                    .`when`(
                      LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                        .setProperties(
                          StatePropertiesPredicate.Builder.properties().hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)
                        )
                    )
                )
            )
          )
      )
    )
  }

  fun dropDoor(block: Block) {
    super.add(
      block,
      LootTable.lootTable().withPool(
        this.applyExplosionCondition(
          block,
          LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(
            LootItem.lootTableItem(block).`when`(
              LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                .setProperties(
                  StatePropertiesPredicate.Builder.properties().hasProperty(DoorBlock.HALF, DoubleBlockHalf.LOWER)
                )
            )
          )
        )
      )
    )
  }

  fun dropSelfSilkShearsOtherLoot(block: Block, other: Supplier<ItemLike>, chance: Float = 1f, multiplier: Int = 1) {
    simpleSilkShearsLootTable(block, block, other, chance, multiplier)
  }

  fun dropDoubleCropLoot(
    b: Block,
    cropItem: Supplier<ItemLike>?,
    _seedItem: Supplier<ItemLike>? = null,
    includeSeedOnDrop: Boolean,
    chance: Float = 0.25f,
    multiplier: Int = 1
  ) {
    val registries = null
    val seedItem = _seedItem ?: Supplier { b.asItem() }

    var builder: LootPoolEntryContainer.Builder<*> = LootItem.lootTableItem(b)
      .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1f)))
      .`when`(hasShearsOrSilkTouch(registries))
    builder = if (cropItem !== null && includeSeedOnDrop) {
      builder.otherwise(
        this.applyExplosionCondition(b, LootItem.lootTableItem(seedItem.get()))
          .apply(SetItemCountFunction.setCount(ConstantValue.exactly(multiplier.toFloat())))
          .`when`(LootItemRandomChanceCondition.randomChance(chance))
          .otherwise(LootItem.lootTableItem(cropItem.get()))
      )
    } else {
      builder.otherwise(LootItem.lootTableItem(if (cropItem !== null) cropItem.get() else seedItem.get()))
    }
    val pool = LootTable.lootTable()
      .withPool(
        LootPool.lootPool()
          .add(builder)
          .`when`(
            LootItemBlockStatePropertyCondition.hasBlockStateProperties(b)
              .setProperties(
                StatePropertiesPredicate.Builder.properties()
                  .hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)
              )
          )
          .`when`(
            LocationCheck.checkLocation(
              LocationPredicate.Builder.location()
                .setBlock(
                  BlockPredicate.Builder.block()
                    .of(b)
                    .setProperties(
                      StatePropertiesPredicate.Builder.properties()
                        .hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)
                        /*? if <1.21 {*/
                        .build()
                      /*?}*/
                    )
                    /*? if <1.21 {*/
                    .build()
                  /*?}*/
                ), BlockPos(0, 1, 0)
            )
          )
      )
      .withPool(
        LootPool.lootPool()
          .add(builder)
          .`when`(
            LootItemBlockStatePropertyCondition.hasBlockStateProperties(b)
              .setProperties(
                StatePropertiesPredicate.Builder.properties()
                  .hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)
              )
          )
          .`when`(
            LocationCheck.checkLocation(
              LocationPredicate.Builder.location()
                .setBlock(
                  BlockPredicate.Builder.block()
                    .of(b)
                    .setProperties(
                      StatePropertiesPredicate.Builder.properties()
                        .hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)
                        /*? if <1.21 {*/
                        .build()
                      /*?}*/
                    )
                    /*? if <1.21 {*/
                    .build()
                  /*?}*/
                ), BlockPos(0, -1, 0)
            )
          )
      )
    this.add(b, pool)
  }

  // private functions
  private fun <B : Block> simpleSilkShearsLootTable(
    b: B,
    silk: ItemLike,
    other: Supplier<ItemLike>,
    chance: Float = 1f,
    multiplier: Int = 1
  ) {
    val registries = null
    val enchant = Enchantments.BLOCK_FORTUNE
    this.add(
      b,
      createSilkTouchOrShearsDispatchTable(
        silk,
        this.applyExplosionDecay(
          silk, LootItem.lootTableItem(other.get())
            .`when`(LootItemRandomChanceCondition.randomChance(chance))
            .apply(ApplyBonusCount.addUniformBonusCount(enchant, 2))
        ),
        registries
      )!!.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(multiplier.toFloat())))
    )
  }

  private fun createSilkTouchOrShearsDispatchTable(
    arg: ItemLike,
    arg2: LootPoolEntryContainer.Builder<*>,
    registries: HolderLookup.Provider? = null
  ): LootTable.Builder? {
    return LootTable.lootTable()
      .withPool(
        LootPool.lootPool()
          .setRolls(ConstantValue.exactly(1.0f))
          .add((LootItem.lootTableItem(arg).`when`(this.hasShearsOrSilkTouch(registries))).otherwise(arg2))
      );
  }

  private fun hasShearsOrSilkTouch(registries: HolderLookup.Provider? = null): LootItemCondition.Builder {
    return HAS_SHEARS.or(hasSilkTouch(registries))
  }

  private fun hasSilkTouch(registries: HolderLookup.Provider? = null): LootItemCondition.Builder {
    return MatchTool.toolMatches(
      ItemPredicate.Builder.item()
        .hasEnchantment(EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1)))
    )
  }

  // functions from BlockLootSubProvider that need to be public
  public override fun add(b: Block, lt: LootTable.Builder) {
    super.add(b, lt)
  }

  public override fun add(block: Block, function: Function<Block, LootTable.Builder>) {
    super.add(block, function)
  }

  public override fun dropSelf(block: Block) {
    super.dropSelf(block)
  }

  public override fun dropOther(block: Block, itemLike: ItemLike) {
    super.dropOther(block, itemLike)
  }

  // generate function to hold on fabric
  override fun generate(biConsumer: BiConsumer<ResourceLocation, LootTable.Builder>) {
    generate()
    for ((identifier, value) in map) {
      if (identifier == BuiltInLootTables.EMPTY) continue
      biConsumer.accept(identifier, value)
    }
  }

  // Should be overridden by implementation
  override fun run(cachedOutput: CachedOutput): CompletableFuture<*> {
    throw UnsupportedOperationException("Unsupported")
  }

  override fun getName(): String {
    throw UnsupportedOperationException("Unsupported")
  }
}