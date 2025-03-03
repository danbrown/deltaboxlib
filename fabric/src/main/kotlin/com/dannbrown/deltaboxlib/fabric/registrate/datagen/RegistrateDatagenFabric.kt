package com.dannbrown.deltaboxlib.fabric.registrate.datagen

import com.dannbrown.deltaboxlib.mixin.registrate.BlockModelGeneratorsMixin
import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.datagen.RegistrateBlockLootTables
import com.dannbrown.deltaboxlib.registrate.datagen.RegistrateBlockModelGenerator
import com.dannbrown.deltaboxlib.registrate.datagen.RegistrateItemModelGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLootTableProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.fabricmc.fabric.impl.datagen.loot.FabricLootTableProviderImpl
import net.minecraft.data.CachedOutput
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.ItemModelGenerators
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import java.util.concurrent.CompletableFuture

object RegistrateDatagenFabric {
  fun buildDatagenResources(pack: FabricDataGenerator.Pack, registrate: AbstractDeltaboxRegistrate) {

    // Block Loot Tables
    pack.addProvider(blockLootTableFactory(registrate))
    // Models
    pack.addProvider(modelsFactory(registrate))
    // Tags
    // Recipes

    // ----
  }

  private fun blockLootTableFactory(registrate: AbstractDeltaboxRegistrate): FabricDataGenerator.Pack.Factory<FabricLootTableProvider> {
    return FabricDataGenerator.Pack.Factory { dataOutput ->
      object : RegistrateBlockLootTables(registrate), FabricLootTableProvider {
        override fun generate() {
          for (block in registrate.blockRegistry.entries) {
            block.lootTableFactory.invoke(this, block.blockInstance)
            println("Generated loot table for ${block.blockInstance.get().name}")
          }
        }

        override fun run(cachedOutput: CachedOutput): CompletableFuture<*> {
          return FabricLootTableProviderImpl.run(cachedOutput, this, LootContextParamSets.BLOCK, dataOutput)
        }

        override fun getName(): String {
          return "Block Loot Tables"
        }
      }
    }
  }

  private fun modelsFactory(registrate: AbstractDeltaboxRegistrate): FabricDataGenerator.Pack.Factory<FabricModelProvider> {
    return FabricDataGenerator.Pack.Factory { dataOutput ->
      object : FabricModelProvider(dataOutput) {

        override fun generateBlockStateModels(modelGenerators: BlockModelGenerators) {
          val registrateBlockModelGenerator = RegistrateBlockModelGenerator(modelGenerators.blockStateOutput, modelGenerators.modelOutput, modelGenerators.skippedAutoModelsOutput)
          for (block in registrate.blockRegistry.entries) {
            block.blockstateFactory.invoke(registrateBlockModelGenerator, block.blockInstance)
            println("Generated blockstate for ${block.blockInstance.get().name}")
          }
        }
        override fun generateItemModels(modelGenerators: ItemModelGenerators) {
          val registrateItemModelGenerator = RegistrateItemModelGenerator(modelGenerators.output)
          for (item in registrate.itemRegistry.entries) {
            item.itemModelFactory.invoke(registrateItemModelGenerator, item.itemInstance)
            println("Generated item model for ${item.itemInstance.get().descriptionId}")
          }
        }
      }
    }
  }


  // ----
}
