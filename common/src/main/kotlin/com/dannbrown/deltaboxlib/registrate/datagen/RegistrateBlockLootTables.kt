package com.dannbrown.deltaboxlib.registrate.datagen

import com.dannbrown.deltaboxlib.content.block.CropLeavesBlock
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
import net.minecraft.world.level.block.CropBlock
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

  /**
   * Drops nothing
   */
  fun noLoot(block: Block) {
    super.add(block, LootTable.lootTable())
  }


  /**
   * Drops the block itself
   */
  public fun dropItself(block: Block) {
    super.dropSelf(block)
  }

  /**
   * Drops the other loot instead
   * @param itemLike the item to drop
   */
  public fun dropAnother(block: Block, itemLike: ItemLike) {
    super.dropOther(block, itemLike)
  }

  /**
   * Create a potted plant loot table, it will drop the flower pot and the plant item
   * @param plant the plant item to drop
   */
  fun pottedBlock(block: Block, plant: Supplier<out ItemLike>) {
    super.add(
      block, LootTable.lootTable()
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

  /**
   * Drops the slab item table
   */
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

  /**
   * Create a door loot table, it will drop the door item itself, ignores drops from the second half of the door
   */
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

  fun dropItselfSilkShearsOtherLoot(block: Block, other: Supplier<ItemLike>, chance: Float = 1f, multiplier: Int = 1) {
    simpleSilkShearsLootTable(block, block, other, chance, multiplier)
  }

  /**
   * Create a Leaves loot table, it will drop the sapling with a 5% chance, and 1-2 sticks with a 1/200 chance
   * Also adds silk touch and shears support
   * @param saplingDrop the sapling to drop
   */
  fun dropLeaves(block: Block, saplingDrop: Supplier<out Block>) {
    this.add(block, this.createLeavesDrops(block, saplingDrop.get(), 0.05f, 0.0625f, 0.083333336f, 0.1f))
  }

  /**
   * Create a Leaves loot table, it will drop the sapling if not fully grown, and the stick if fully grown
   * @param cropItem the item to drop if the block is fully grown
   * @param saplingItem the item to drop if the block is not fully grown
   * @param cropChance the chance to drop the crop item
   * @param cropMultiplier the amount of items to drop
   * @param saplingChance the chance to drop the sapling item
   * @param saplingMultiplier the amount of saplings to drop
   */
  fun dropLeafCropLoot(
    block: Block,
    cropItem: Supplier<ItemLike>,
    saplingItem: Supplier<ItemLike>,
    cropChance: Float = 0.5f,
    cropMultiplier: Int = 2,
    saplingChance: Float = 0.1f,
    saplingMultiplier: Int = 1
  ) {
    val pool1 = LootPool.lootPool()
      .setRolls(ConstantValue.exactly(cropMultiplier.toFloat()))
      .`when`(
        LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
          .setProperties(
            StatePropertiesPredicate.Builder.properties().hasProperty(CropLeavesBlock.AGE, CropLeavesBlock.MAX_AGE)
          )
          .and(LootItemRandomChanceCondition.randomChance(cropChance))
      )
      .add(LootItem.lootTableItem(cropItem.get()))

    // drop sapling at any age
    val pool2 = LootPool.lootPool()
      .setRolls(ConstantValue.exactly(saplingMultiplier.toFloat()))
      .`when`(LootItemRandomChanceCondition.randomChance(saplingChance))
      .add(LootItem.lootTableItem(saplingItem.get()))

    this.add(
      block,
      LootTable.lootTable()
        .withPool(pool1)
        .withPool(pool2)
    )
  }

  /**
   * Create a Crop loot table, it will drop the item if fully grown, and the seed if not fully grown
   * @param cropItem the item to drop if the block is fully grown
   * @param _seedItem the seed to drop, can be null
   * @param includeSeedOnDrop if it will drop itself as a seed
   * @param chance the chance to drop the crop item
   * @param multiplier the amount of items to drop
   * @param age the age the crop is ready
   */
  fun dropCropLoot(
    block: Block,
    cropItem: Supplier<ItemLike>?,
    _seedItem: Supplier<ItemLike>?,
    includeSeedOnDrop: Boolean,
    chance: Float = 0.5f,
    multiplier: Int = 1,
    age: Int = 7
  ) {
    val enchant = Enchantments.BLOCK_FORTUNE
    val seedItem = _seedItem ?: Supplier { block.asItem() }

    val dropGrownCondition = LootItemRandomChanceCondition.randomChance(chance)
      .and(
        LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
          .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, age))
      )

    val itemBuilder =
      LootItem.lootTableItem(if (cropItem !== null) cropItem.get() else seedItem.get()).`when`(dropGrownCondition)

    if (cropItem !== null && includeSeedOnDrop) {
      itemBuilder.otherwise(LootItem.lootTableItem(seedItem.get()))
    }

    val lootBuilder = LootTable.lootTable().withPool(
      LootPool.lootPool().add(
        itemBuilder
      ).setRolls(ConstantValue.exactly(multiplier.toFloat()))
    )

    if (cropItem !== null && includeSeedOnDrop) {
      lootBuilder.withPool(
        LootPool.lootPool()
          .`when`(dropGrownCondition)
          .apply(ApplyBonusCount.addBonusBinomialDistributionCount(enchant, 0.5714286f, 3))
          .add(LootItem.lootTableItem(seedItem.get()))
      )
    }

    this.add(block, this.applyExplosionDecay(block, lootBuilder))
  }

  /**
   * Create a Double Crop loot table, it will drop the item if fully grown, and the seed if not fully grown
   * @param cropItem the item to drop if the block is fully grown
   * @param _seedItem the seed to drop, can be null
   * @param includeSeedOnDrop if it will drop itself as a seed
   * @param chance the chance to drop the crop item
   * @param multiplier the amount of items to drop
   */
  fun dropDoubleCropLoot(
    block: Block,
    cropItem: Supplier<ItemLike>?,
    _seedItem: Supplier<ItemLike>? = null,
    includeSeedOnDrop: Boolean,
    chance: Float = 0.25f,
    multiplier: Int = 1
  ) {
    val seedItem = _seedItem ?: Supplier { block.asItem() }

    var builder: LootPoolEntryContainer.Builder<*> = LootItem.lootTableItem(block)
      .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1f)))
      .`when`(hasShearsOrSilkTouch())

    builder = if (cropItem != null && includeSeedOnDrop) {
      builder.otherwise(
        this.applyExplosionCondition(block, LootItem.lootTableItem(seedItem.get()))
          .apply(SetItemCountFunction.setCount(ConstantValue.exactly(multiplier.toFloat())))
          .`when`(LootItemRandomChanceCondition.randomChance(chance))
          .otherwise(LootItem.lootTableItem(cropItem.get()))
      )
    } else {
      builder.otherwise(LootItem.lootTableItem(if (cropItem != null) cropItem.get() else seedItem.get()))
    }

    val pool = LootTable.lootTable()
      .withPool(
        LootPool.lootPool()
          .add(builder)
          .`when`(
            LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
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
                    .of(block)
                    .setProperties(
                      StatePropertiesPredicate.Builder.properties()
                        .hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER).build()
                    )
                    .build()
                ),
              BlockPos(0, 1, 0)
            )
          )
      )
      .withPool(
        LootPool.lootPool()
          .add(builder)
          .`when`(
            LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
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
                    .of(block)
                    .setProperties(
                      StatePropertiesPredicate.Builder.properties()
                        .hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER).build()
                    )
                    .build()
                ),
              BlockPos(0, -1, 0)
            )
          )
      )
    this.add(block, pool)
  }


  /**
   * Drops the silk item if the block is mined with silk touch or shears, and the other with a chance and multiplier if not
   * Ex usage: vine, plants without item
   * @param silk the item to drop if the block is mined with silk touch or shears
   * @param other the item to drop if the block is mined normally
   * @param chance the chance to drop the silk item
   * @param multiplier the amount of items to drop
   */
  fun <B : Block> dropSilkShearsOtherLoot(
    block: Block,
    silk: Supplier<ItemLike>,
    other: Supplier<ItemLike>? = null,
    chance: Float = 1f,
    multiplier: Int = 1
  ) {
    simpleSilkShearsLootTable(block, silk.get(), other, chance, multiplier)
  }

  /**
   * Drops the block itself if mined with silk touch or shears, and the other with a chance and multiplier if not
   * Ex usage: plants with itself as the item
   * @param other the item to drop if the block is mined normally
   * @param chance the chance to drop the silk item
   * @param multiplier the amount of items to drop
   */
  fun dropSelfSilkShearsOtherLoot(
    block: Block,
    other: Supplier<ItemLike>? = null,
    chance: Float = 1f,
    multiplier: Int = 1
  ) {
    simpleSilkShearsLootTable(block, block, other, chance, multiplier)
  }

  /**
   * Drops the silk item if the block is mined with silk touch, and the other with a chance and multiplier if not
   * * Ex usage: Ore blocks without item
   * @param silk the item to drop if the block is mined with silk touch
   * @param other the item to drop if the block is mined normally
   * @param chance the chance to drop the silk item
   * @param multiplier the amount of items to drop
   */
  fun dropSilkOtherLoot(
    block: Block,
    silk: Supplier<ItemLike>,
    other: Supplier<ItemLike>? = null,
    chance: Float = 1f,
    multiplier: Int = 1
  ) {
    simpleSilkLootTable(block, silk.get(), other, chance, multiplier)
  }

  /**
   * Drops the block itself if mined with silk touch, and the other with a chance and multiplier if not
   * * Ex usage: Ore blocks
   * @param other the item to drop if the block is mined normally
   * @param chance the chance to drop the silk item
   * @param multiplier the amount of items to drop
   */
  fun dropSelfSilkOtherLoot(block: Block, other: Supplier<ItemLike>? = null, chance: Float = 1f, multiplier: Int = 1) {
    simpleSilkLootTable(block, block, other, chance, multiplier)
  }

  // private functions
  private fun hasShearsOrSilkTouch(): LootItemCondition.Builder {
    return HAS_SHEARS.or(hasSilkTouch())
  }

  private fun hasSilkTouch(): LootItemCondition.Builder {
    return MatchTool.toolMatches(
      ItemPredicate.Builder.item()
        .hasEnchantment(EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1)))
    )
  }

//  private fun createSilkTouchOrShearsDispatchTable(
//    arg: ItemLike,
//    arg2: LootPoolEntryContainer.Builder<*>
//  ): LootTable.Builder? {
//    return LootTable.lootTable()
//      .withPool(
//        LootPool.lootPool()
//          .setRolls(ConstantValue.exactly(1.0f))
//          .add((LootItem.lootTableItem(arg).`when`(this.hasShearsOrSilkTouch())).otherwise(arg2))
//      );
//  }

  // create a silk touch table for a item to be dropped with silk touch
  fun createSecondaryDispatchTable(
    specificItem: ItemLike,
    secondaryItem: LootPoolEntryContainer.Builder<*>?,
    condition: LootItemCondition.Builder
  ): LootTable.Builder {
    val pool = LootPool.lootPool()
      .setRolls(ConstantValue.exactly(1.0f))
      .add(LootItem.lootTableItem(specificItem).`when`(condition))

    secondaryItem?.let { pool.add(it.otherwise(secondaryItem)) }

    return LootTable.lootTable().withPool(pool)
  }

// create a silk touch table for a item to be dropped with silk touch
//  private fun createSilkTouchDispatchTable(
//    arg: ItemLike,
//    arg2: LootPoolEntryContainer.Builder<*>?
//  ): LootTable.Builder {
//    val pool = LootPool.lootPool()
//      .setRolls(ConstantValue.exactly(1.0f))
//      .add(LootItem.lootTableItem(arg).`when`(this.hasSilkTouch()))
//
//    arg2?.let { pool.add(it.otherwise(arg2)) }
//
//    return LootTable.lootTable().withPool(pool)
//  }

  private fun simpleSilkShearsLootTable(
    b: Block,
    silk: ItemLike,
    other: Supplier<ItemLike>? = null,
    chance: Float = 1f,
    multiplier: Int = 1,
    applyFortune: Boolean = true,
  ) {
    val enchant = Enchantments.BLOCK_FORTUNE
    val lootItem = other?.get()?.let {
      var entry = LootItem.lootTableItem(it)
        .`when`(LootItemRandomChanceCondition.randomChance(chance))

      if (applyFortune) {
        entry = entry.apply(ApplyBonusCount.addUniformBonusCount(enchant, 2))
      }

      this.applyExplosionDecay(silk, entry)
    }

    this.add(
      b,
      createSecondaryDispatchTable(silk, lootItem, hasShearsOrSilkTouch()).withPool(
        LootPool.lootPool().setRolls(ConstantValue.exactly(multiplier.toFloat()))
      )
    )
  }

  private fun simpleSilkLootTable(
    b: Block,
    silk: ItemLike,
    other: Supplier<ItemLike>? = null,
    chance: Float = 1f,
    multiplier: Int = 1,
    applyFortune: Boolean = true,
  ) {
    val enchant = Enchantments.BLOCK_FORTUNE
    val lootItem = other?.get()?.let {
      var entry = LootItem.lootTableItem(it)
        .`when`(LootItemRandomChanceCondition.randomChance(chance))

      if (applyFortune) {
        entry = entry.apply(ApplyBonusCount.addUniformBonusCount(enchant, 2))
      }

      this.applyExplosionDecay(silk, entry)
    }

    this.add(
      b,
      createSecondaryDispatchTable(silk, lootItem, hasSilkTouch()).withPool(
        LootPool.lootPool().setRolls(ConstantValue.exactly(multiplier.toFloat()))
      )
    )
  }

  // functions from BlockLootSubProvider that need to be public
  public override fun add(b: Block, lt: LootTable.Builder) {
    super.add(b, lt)
  }

  public override fun add(block: Block, function: Function<Block, LootTable.Builder>) {
    super.add(block, function)
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