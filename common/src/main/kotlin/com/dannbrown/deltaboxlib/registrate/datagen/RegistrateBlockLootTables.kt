package com.dannbrown.deltaboxlib.registrate.datagen

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.DoorBlock
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import net.minecraft.world.level.block.state.properties.SlabType
import net.minecraft.world.level.storage.loot.BuiltInLootTables
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition
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