package com.dannbrown.databoxlib.registry.transformers

import com.dannbrown.databoxlib.registry.transformers.BlockLootHelpers
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables
import com.tterrag.registrate.util.nullness.NonNullBiConsumer
import net.minecraft.advancements.critereon.BlockPredicate
import net.minecraft.advancements.critereon.LocationPredicate
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.core.BlockPos
import net.minecraft.world.item.Item
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.DoublePlantBlock
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.predicates.LocationCheck
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import java.util.function.Supplier

object BlockLootPresets {

  fun <B : Block> noLoot(): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b ->
      lt.add(b, LootTable.lootTable())
    }
  }

  fun <B : Block> pottedPlantLoot(item: Supplier<ItemLike>): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b ->
      lt.add(b, lt.createPotFlowerItemTable(item.get()))
    }
  }

  fun <B : Block> oreLootTable(dropItem: Supplier<ItemLike>): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b ->
      lt.add(b,
        RegistrateBlockLootTables.createSilkTouchDispatchTable(b,
          lt.applyExplosionDecay(b,
            LootItem.lootTableItem(dropItem.get())
              .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)))))
    }
  }

  fun <B : Block> dropItselfLoot(): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b -> lt.dropSelf(b) }
  }

  fun <B : Block> dropOtherLoot(other: Supplier<ItemLike>): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b ->
      lt.dropOther(b,
        other.get()
          .asItem())
    }
  }

  fun <B : Block> dropSelfSilkLoot(other: Supplier<ItemLike>): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b ->
      lt.add(b, RegistrateBlockLootTables.createSilkTouchDispatchTable(b, lt.applyExplosionDecay(b, LootItem.lootTableItem(other.get()))))
    }
  }

  fun <B : Block> dropSelfSilkShearsOtherLoot(other: Supplier<ItemLike>, chance: Float = 1f): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b ->
      lt.add(b,
        BlockLootHelpers.createShearsDispatchTable(b,
          lt.applyExplosionDecay(b,
            LootItem.lootTableItem(other.get())
              .`when`(LootItemRandomChanceCondition.randomChance(chance))
              .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE, 2)))))
    }
  }

  fun <B : Block> dropSelfSilkShearsLoot(): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b ->
      lt.add(b, BlockLootHelpers.createShearsDispatchTable(b))
    }
  }

  fun <B : Block> dropCropLoot(cropItem: Supplier<Item>, seedItem: Supplier<Item>, chance: Float = 0.5f): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b ->
      lt.add(b, lt.createCropDrops(b, cropItem.get(), seedItem.get(), LootItemRandomChanceCondition.randomChance(chance)))
    }
  }

  fun <B : Block> dropDoubleCropLoot(cropItem: Supplier<Item>, seedItem: Supplier<Item>, chance: Float = 0.25f, count: Float = 2f): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b ->
      val builder: LootPoolEntryContainer.Builder<*> = LootItem.lootTableItem(b)
        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1f)))
        .`when`(BlockLootHelpers.HAS_SHEARS)
        .otherwise(lt.applyExplosionCondition(b, LootItem.lootTableItem(seedItem.get()))
          .apply(SetItemCountFunction.setCount(ConstantValue.exactly(count)))
          .`when`(LootItemRandomChanceCondition.randomChance(chance))
          .otherwise(LootItem.lootTableItem(cropItem.get())))
      val pool = LootTable.lootTable()
        .withPool(LootPool.lootPool()
          .add(builder)
          .`when`(LootItemBlockStatePropertyCondition.hasBlockStateProperties(b)
            .setProperties(StatePropertiesPredicate.Builder.properties()
              .hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)))
          .`when`(LocationCheck.checkLocation(LocationPredicate.Builder.location()
            .setBlock(BlockPredicate.Builder.block()
              .of(b)
              .setProperties(StatePropertiesPredicate.Builder.properties()
                .hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)
                .build())
              .build()), BlockPos(0, 1, 0))))
        .withPool(LootPool.lootPool()
          .add(builder)
          .`when`(LootItemBlockStatePropertyCondition.hasBlockStateProperties(b)
            .setProperties(StatePropertiesPredicate.Builder.properties()
              .hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)))
          .`when`(LocationCheck.checkLocation(LocationPredicate.Builder.location()
            .setBlock(BlockPredicate.Builder.block()
              .of(b)
              .setProperties(StatePropertiesPredicate.Builder.properties()
                .hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)
                .build())
              .build()), BlockPos(0, -1, 0))))

      lt.add(b, pool)
    }
  }

  fun <B : Block> dropDoubleFlowerLootLower(drop: Supplier<ItemLike>? = null, count: Number = 1): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return dropDoubleFlowerLoot(DoubleBlockHalf.LOWER, drop, count)
  }

  fun <B : Block> dropDoubleFlowerLootUpper(drop: Supplier<ItemLike>? = null, count: Number = 1): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return dropDoubleFlowerLoot(DoubleBlockHalf.UPPER, drop, count)
  }

  private fun <B : Block> dropDoubleFlowerLoot(property: DoubleBlockHalf, drop: Supplier<ItemLike>? = null, count: Number = 1): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b ->
      lt.add(b, BlockLootHelpers.createSinglePropConditionTable(if (drop !== null) drop.get() else b, b, DoublePlantBlock.HALF, property, count.toFloat(), lt))
    }
  }

  fun <B : Block> doorLoot(): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b ->
      lt.add(b, lt.createDoorTable(b))
    }
  }

  fun <B : Block> leavesLoot(saplingDrop: Supplier<Block>): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b ->
      lt.add(b, lt.createLeavesDrops(b, saplingDrop.get(), 0.05f, 0.0625f, 0.083333336f, 0.1f))
    }
  }
}