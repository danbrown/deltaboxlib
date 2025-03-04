package com.dannbrown.deltaboxlib.fabric.registrate.datagen

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.datagen.RegistrateBlockLootTables
import com.dannbrown.deltaboxlib.registrate.datagen.model.RegistrateBlockModelGenerator
import com.dannbrown.deltaboxlib.registrate.datagen.model.RegistrateItemModelGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLootTableProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.fabricmc.fabric.impl.datagen.loot.FabricLootTableProviderImpl
import net.minecraft.data.CachedOutput
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.ItemModelGenerators
import net.minecraft.world.item.BlockItem
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import java.util.concurrent.CompletableFuture

object RegistrateDatagenFabric {
  fun buildDatagenResources(pack: FabricDataGenerator.Pack, registrate: AbstractDeltaboxRegistrate) {

    // Block Loot Tables
    pack.addProvider(blockLootTableFactory(registrate))
    // Models
    pack.addProvider(modelsFactory(registrate))
    // Language
    pack.addProvider(languageFactory(registrate))
    // Tags
    // Recipes

    // ----
  }

  // Block Loot Tables
  private fun blockLootTableFactory(registrate: AbstractDeltaboxRegistrate): FabricDataGenerator.Pack.Factory<FabricLootTableProvider> {
    return FabricDataGenerator.Pack.Factory { dataOutput ->
      object : RegistrateBlockLootTables(registrate), FabricLootTableProvider {
        override fun generate() {
          for (block in registrate.blockRegistry.entries) {
            block.lootTableFactory.invoke(this, block.blockInstance)
            println("Generated loot table for ${block.blockInstance.get().descriptionId}")
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

  // Blockstates, Block Models, Item Models
  private fun modelsFactory(registrate: AbstractDeltaboxRegistrate): FabricDataGenerator.Pack.Factory<FabricModelProvider> {
    return FabricDataGenerator.Pack.Factory { dataOutput ->
      object : FabricModelProvider(dataOutput) {
        override fun generateBlockStateModels(modelGenerators: BlockModelGenerators) {
          val registrateBlockModelGenerator = RegistrateBlockModelGenerator(
            modelGenerators.blockStateOutput,
            modelGenerators.modelOutput,
            modelGenerators.skippedAutoModelsOutput
          )
          for (block in registrate.blockRegistry.entries) {
            block.blockstateFactory.invoke(registrateBlockModelGenerator, block.blockInstance)
            println("Generated blockstate for ${block.blockInstance.get().descriptionId}")
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

  // Language
  private fun languageFactory(registrate: AbstractDeltaboxRegistrate): FabricDataGenerator.Pack.Factory<FabricLanguageProvider> {
    return FabricDataGenerator.Pack.Factory { dataOutput ->
      object : FabricLanguageProvider(dataOutput, "en_us") {
        override fun generateTranslations(builder: TranslationBuilder) {
          // Langs
          for (lang in registrate.langRegistry.langEntries) {
            try {
              builder.add(lang.key, lang.value)
            } catch (e: Exception) {
              println("Failed to generate translation for ${lang.key}, it is a possible duplicate")
            } finally {
              println("Generated translation for ${lang.key}")
            }
          }
          // Blocks
          for (block in registrate.blockRegistry.entries) {
            try {
              builder.add(block.blockInstance.get(), block.getName())
            } catch (e: Exception) {
              println("Failed to generate translation for ${block.blockInstance.get().name}, it is a possible duplicate")
            } finally {
              println("Generated translation for ${block.blockInstance.get().name}")
            }
          }
          // Items
          for (item in registrate.itemRegistry.entries) {
            try {
              builder.add(item.itemInstance.get(), item.getName())
            } catch (e: Exception) {
              if (item.itemInstance.get() is BlockItem) continue
              println("Failed to generate translation for ${item.itemInstance.get().descriptionId}, it is a possible duplicate")
            } finally {
              println("Generated translation for ${item.itemInstance.get().descriptionId}")
            }
          }
        }
      }
    }
  }


  // ----
}
