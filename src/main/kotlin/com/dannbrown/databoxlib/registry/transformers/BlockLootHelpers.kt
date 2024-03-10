package com.dannbrown.databoxlib.registry.transformers

import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables
import net.minecraft.advancements.critereon.EnchantmentPredicate
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.advancements.critereon.MinMaxBounds
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.properties.Property
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.MatchTool
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue

object BlockLootHelpers {
  val HAS_SHEARS = MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS))
  val HAS_SILK_TOUCH = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))))

  fun createSelfDropDispatchTable(pBlock: Block, pConditionBuilder: LootItemCondition.Builder, pAlternativeBuilder: LootPoolEntryContainer.Builder<*>
  ): LootTable.Builder {
    return LootTable.lootTable()
      .withPool(LootPool.lootPool()
        .setRolls(ConstantValue.exactly(1.0f))
        .add(LootItem.lootTableItem(pBlock)
          .`when`(pConditionBuilder)
          .otherwise(pAlternativeBuilder)))
  }

  fun createSelfDropDispatchTable(pBlock: Block, pConditionBuilder: LootItemCondition.Builder): LootTable.Builder {
    return LootTable.lootTable()
      .withPool(LootPool.lootPool()
        .setRolls(ConstantValue.exactly(1.0f))
        .add(LootItem.lootTableItem(pBlock)
          .`when`(pConditionBuilder)))
  }

  fun createShearsDispatchTable(pBlock: Block, pBuilder: LootPoolEntryContainer.Builder<*>): LootTable.Builder {
    return createSelfDropDispatchTable(pBlock, HAS_SHEARS, pBuilder)
  }

  fun createShearsDispatchTable(pBlock: Block): LootTable.Builder {
    return createSelfDropDispatchTable(pBlock, HAS_SHEARS)
  }

  fun <T> createSinglePropConditionTable(pItem: ItemLike, pBlock: Block, pProperty: Property<T>, pValue: T, count: Float = 1f, lt: RegistrateBlockLootTables): LootTable.Builder where T : Comparable<T>, T : StringRepresentable {
    return LootTable.lootTable()
      .withPool(lt.applyExplosionCondition(pBlock,
        LootPool.lootPool()
          .setRolls(ConstantValue.exactly(1.0f))
          .add(LootItem.lootTableItem(pItem)
            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(count)))
            .`when`(LootItemBlockStatePropertyCondition.hasBlockStateProperties(pBlock)
              .setProperties(StatePropertiesPredicate.Builder.properties()
                .hasProperty(pProperty, pValue))))))
  }
  fun createSilkTouchDispatchTable(pBlock: Block, pBuilder: LootPoolEntryContainer.Builder<*>): LootTable.Builder {
    return createSelfDropDispatchTable(pBlock, HAS_SILK_TOUCH, pBuilder)
  }
}