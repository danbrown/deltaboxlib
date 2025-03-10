package com.dannbrown.deltaboxlib.fabric.registrate

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.builders.BlockBuilder
import com.dannbrown.deltaboxlib.registrate.datagen.RegistrateBlockLootTables
import com.dannbrown.deltaboxlib.registrate.datagen.RegistrateRecipes
import com.dannbrown.deltaboxlib.registrate.datagen.model.RegistrateBlockModelGenerator
import com.dannbrown.deltaboxlib.registrate.datagen.model.RegistrateItemModelGenerator
import com.dannbrown.deltaboxlib.registrate.providers.biomeModifier.BiomeModifierProvider
import com.dannbrown.deltaboxlib.registrate.providers.trades.VillagerTradeProvider
import com.dannbrown.deltaboxlib.registrate.providers.trades.WandererTradeProvider
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLootTableProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.fabricmc.fabric.impl.datagen.loot.FabricLootTableProviderImpl
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.ItemModelGenerators
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.decoration.PaintingVariant
import net.minecraft.world.item.BlockItem
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.presets.WorldPreset
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

object RegistrateDatagenFabric {
  fun buildDatagenResources(pack: FabricDataGenerator.Pack, registrate: AbstractDeltaboxRegistrate) {

    // Block Loot Tables
    pack.addProvider(blockLootTableFactory(registrate))
    // Models
    pack.addProvider(modelsFactory(registrate))
    // Language
    pack.addProvider(languageFactory(registrate))
    // Tags
    pack.addProvider(blockTagsFactory(registrate))
    pack.addProvider(itemTagsFactory(registrate))
    pack.addProvider(fluidTagsFactory(registrate))
    pack.addProvider(biomeTagsFactory(registrate))
    pack.addProvider(entityTagsFactory(registrate))
    pack.addProvider(paintingTagsFactory(registrate))
    pack.addProvider(worldPresetTagsFactory(registrate))
    // Recipes
    pack.addProvider(recipesFactory(registrate))
    // villager trades
    pack.addProvider { packOutput -> VillagerTradeProvider(registrate, packOutput) }
    // wanderer trades
    pack.addProvider { packOutput -> WandererTradeProvider(registrate, packOutput) }
    // configured features
    pack.addProvider(configuredFeaturesFactory(registrate))
    // placed features
    pack.addProvider(placedFeaturesFactory(registrate))
    // biome modifiers
    pack.addProvider { packOutput -> BiomeModifierProvider(registrate, packOutput) }
    // ----
  }

  fun buildRegistry(builder: RegistrySetBuilder, registrate: AbstractDeltaboxRegistrate) {
    builder.add(Registries.CONFIGURED_FEATURE, registrate.configuredFeatureRegistry::bootstrapConfiguredfeatures)
    builder.add(Registries.PLACED_FEATURE, registrate.placedFeatureRegistry::bootstrapPlacedFeatures)
  }


  // Block Loot Tables
  private fun blockLootTableFactory(registrate: AbstractDeltaboxRegistrate): FabricDataGenerator.Pack.Factory<FabricLootTableProvider> {
    return FabricDataGenerator.Pack.Factory { dataOutput ->
      object : RegistrateBlockLootTables(registrate), FabricLootTableProvider {
        override fun generate() {
          val blockRegistryEntries: List<BlockBuilder<out Block>> = registrate.blockRegistry.entries
          for (block in blockRegistryEntries) {
            block.lootTableFactory.invoke(this, block.getBlock())
            println("Generated loot table for ${block.getBlock().get().descriptionId}")
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
            block.blockstateFactory.invoke(registrateBlockModelGenerator, block.getBlock())
            println("Generated blockstate for ${block.getBlock().get().descriptionId}")
          }
        }

        override fun generateItemModels(modelGenerators: ItemModelGenerators) {
          val registrateItemModelGenerator = RegistrateItemModelGenerator(modelGenerators.output)
          for (item in registrate.itemRegistry.entries) {
            item.itemModelFactory.invoke(registrateItemModelGenerator, item.getItem())
            println("Generated item model for ${item.getItem().get().descriptionId}")
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
              builder.add(block.getBlock().get(), block.getName())
            } catch (e: Exception) {
              println("Failed to generate translation for ${block.getBlock().get().name}, it is a possible duplicate")
            } finally {
              println("Generated translation for ${block.getBlock().get().name}")
            }
          }
          // Items
          for (item in registrate.itemRegistry.entries) {
            try {
              builder.add(item.getItem().get(), item.getName())
            } catch (e: Exception) {
              if (item.getItem().get() is BlockItem) continue
              println(
                "Failed to generate translation for ${
                  item.getItem().get().descriptionId
                }, it is a possible duplicate"
              )
            } finally {
              println("Generated translation for ${item.getItem().get().descriptionId}")
            }
          }
        }
      }
    }
  }

  // BLOCK TAGS
  private fun blockTagsFactory(registrate: AbstractDeltaboxRegistrate): FabricDataGenerator.Pack.RegistryDependentFactory<FabricTagProvider.BlockTagProvider> {
    return FabricDataGenerator.Pack.RegistryDependentFactory { dataOutput, registriesFuture ->
      object : FabricTagProvider.BlockTagProvider(dataOutput, registriesFuture) {
        override fun addTags(arg: HolderLookup.Provider) {
          registrate.tagRegistry.getBlockTags().forEach { (tagKey, blocks) ->
            val builder = getOrCreateTagBuilder(tagKey)
            blocks.forEach { entry ->
              builder.add(entry.get())
            }
          }
        }
      }
    }
  }

  // ITEM TAGS
  private fun itemTagsFactory(registrate: AbstractDeltaboxRegistrate): FabricDataGenerator.Pack.RegistryDependentFactory<FabricTagProvider.ItemTagProvider> {
    return FabricDataGenerator.Pack.RegistryDependentFactory { dataOutput, registriesFuture ->
      object : FabricTagProvider.ItemTagProvider(dataOutput, registriesFuture) {
        override fun addTags(arg: HolderLookup.Provider) {
          registrate.tagRegistry.getItemTags().forEach { (tagKey, items) ->
            val builder = getOrCreateTagBuilder(tagKey)
            items.forEach { entry ->
              builder.add(entry.get())
            }
          }
        }
      }
    }
  }

  // FLUID TAGS
  private fun fluidTagsFactory(registrate: AbstractDeltaboxRegistrate): FabricDataGenerator.Pack.RegistryDependentFactory<FabricTagProvider.FluidTagProvider> {
    return FabricDataGenerator.Pack.RegistryDependentFactory { dataOutput, registriesFuture ->
      object : FabricTagProvider.FluidTagProvider(dataOutput, registriesFuture) {
        override fun addTags(arg: HolderLookup.Provider) {
          registrate.tagRegistry.getFluidTags().forEach { (tagKey, fluids) ->
            val builder = getOrCreateTagBuilder(tagKey)
            fluids.forEach { supplier ->
              builder.add(supplier.get())
            }
          }
        }
      }
    }
  }

  // BIOME TAGS
  private fun biomeTagsFactory(registrate: AbstractDeltaboxRegistrate): FabricDataGenerator.Pack.RegistryDependentFactory<FabricTagProvider<Biome>> {
    return FabricDataGenerator.Pack.RegistryDependentFactory { dataOutput, registriesFuture ->
      object : FabricTagProvider<Biome>(dataOutput, Registries.BIOME, registriesFuture) {
        override fun addTags(arg: HolderLookup.Provider) {
          registrate.tagRegistry.getBiomeTags().forEach { (tagKey, biomes) ->
            val builder = getOrCreateTagBuilder(tagKey)
            biomes.forEach { resourceKey ->
              builder.add(resourceKey)
            }
          }
        }
      }
    }
  }

  // ENTITY TAGS
  private fun entityTagsFactory(registrate: AbstractDeltaboxRegistrate): FabricDataGenerator.Pack.RegistryDependentFactory<FabricTagProvider.EntityTypeTagProvider> {
    return FabricDataGenerator.Pack.RegistryDependentFactory { dataOutput, registriesFuture ->
      object : FabricTagProvider.EntityTypeTagProvider(dataOutput, registriesFuture) {
        override fun addTags(arg: HolderLookup.Provider) {
          registrate.tagRegistry.getEntityTags().forEach { (tagKey, entities) ->
            val builder = getOrCreateTagBuilder(tagKey)
            entities.forEach { supplier ->
              builder.add(supplier.get())
            }
          }
        }
      }
    }
  }

  // PAINTING TAGS
  private fun paintingTagsFactory(registrate: AbstractDeltaboxRegistrate): FabricDataGenerator.Pack.RegistryDependentFactory<FabricTagProvider<PaintingVariant>> {
    return FabricDataGenerator.Pack.RegistryDependentFactory { dataOutput, registriesFuture ->
      object : FabricTagProvider<PaintingVariant>(dataOutput, Registries.PAINTING_VARIANT, registriesFuture) {
        override fun addTags(arg: HolderLookup.Provider) {
          registrate.tagRegistry.getPaintingTags().forEach { (tagKey, paintings) ->
            val builder = getOrCreateTagBuilder(tagKey)
            paintings.forEach { supplier ->
              builder.add(supplier.get())
            }
          }
        }
      }
    }
  }

  // WORLD PRESET TAGS
  private fun worldPresetTagsFactory(registrate: AbstractDeltaboxRegistrate): FabricDataGenerator.Pack.RegistryDependentFactory<FabricTagProvider<WorldPreset>> {
    return FabricDataGenerator.Pack.RegistryDependentFactory { dataOutput, registriesFuture ->
      object : FabricTagProvider<WorldPreset>(dataOutput, Registries.WORLD_PRESET, registriesFuture) {
        override fun addTags(arg: HolderLookup.Provider) {
          registrate.tagRegistry.getWorldPresetTags().forEach { (tagKey, presets) ->
            val builder = getOrCreateTagBuilder(tagKey)
            presets.forEach { supplier ->
              builder.add(supplier.get())
            }
          }
        }
      }
    }
  }

  private fun recipesFactory(registrate: AbstractDeltaboxRegistrate): FabricDataGenerator.Pack.Factory<FabricRecipeProvider> {
    return FabricDataGenerator.Pack.Factory { dataOutput ->
      object : FabricRecipeProvider(dataOutput) {
        override fun buildRecipes(exporter: Consumer<FinishedRecipe>) {
          for (factory in registrate.recipeRegistry.getRecipes()) {
            factory.invoke(RegistrateRecipes(registrate, exporter))
          }
          for (block in registrate.blockRegistry.entries) {
            block.recipeFactory.invoke(RegistrateRecipes(registrate, exporter), block.getBlock())
          }
          for (item in registrate.itemRegistry.entries) {
            item.recipeFactory.invoke(RegistrateRecipes(registrate, exporter), item.getItem())
          }
        }
      }
    }
  }

  private fun configuredFeaturesFactory(registrate: AbstractDeltaboxRegistrate): FabricDataGenerator.Pack.RegistryDependentFactory<FabricDynamicRegistryProvider> {
    return FabricDataGenerator.Pack.RegistryDependentFactory { dataOutput, registriesFuture ->
      object : FabricDynamicRegistryProvider(dataOutput, registriesFuture) {
        override fun getName(): String {
          return "worldgen/configured_feature"
        }

        override fun configure(registries: HolderLookup.Provider, entries: Entries) {
          for (configuredFeature in registrate.configuredFeatureRegistry.getConfiguredfeatures()) {
            add(registries, entries, configuredFeature)
          }
        }

        private fun add(
          registries: HolderLookup.Provider,
          entries: Entries,
          key: ResourceKey<ConfiguredFeature<*, *>>
        ) {
          val configuredFeatureRegistryLookup = registries.lookupOrThrow(Registries.CONFIGURED_FEATURE)
          entries.add(key, configuredFeatureRegistryLookup.getOrThrow(key).value())
        }
      }
    }
  }

  private fun placedFeaturesFactory(registrate: AbstractDeltaboxRegistrate): FabricDataGenerator.Pack.RegistryDependentFactory<FabricDynamicRegistryProvider> {
    return FabricDataGenerator.Pack.RegistryDependentFactory { dataOutput, registriesFuture ->
      object : FabricDynamicRegistryProvider(dataOutput, registriesFuture) {
        override fun getName(): String {
          return "worldgen/placed_feature"
        }

        override fun configure(registries: HolderLookup.Provider, entries: Entries) {
          for (placedFeature in registrate.placedFeatureRegistry.getPlacedFeatures()) {
            add(registries, entries, placedFeature)
          }
        }

        private fun add(
          registries: HolderLookup.Provider,
          entries: Entries,
          key: ResourceKey<PlacedFeature>
        ) {
          val placedFeatureRegistryLookup = registries.lookupOrThrow(Registries.PLACED_FEATURE)
          entries.add(key, placedFeatureRegistryLookup.getOrThrow(key).value())
        }
      }
    }
  }

// ----
}
