package com.dannbrown.deltaboxlib.registrate.util

import com.dannbrown.deltaboxlib.init.DeltaboxLibMod
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import org.apache.logging.log4j.LogManager
import java.util.function.Supplier

object DeltaboxUtil {
  val LOGGER = LogManager.getLogger()

  fun logInfo(message: String, modId: String = DeltaboxLibMod.MOD_ID) {
    LOGGER.info("[${modId}] $message")
  }


  // @ ResourceLocation related
  fun resourceLocation(namespace: String, path: String): ResourceLocation {
    return ResourceLocation(namespace, path) // example: resourceLocation("minecraft", "block") -> "minecraft:block"
  }

  fun resourceLocation(path: String): ResourceLocation {
    return ResourceLocation(path) // example: resourceLocation("block") -> "block"
  }

  fun resourceLocation(namespace: String, path: String, string: String): ResourceLocation {
    return ResourceLocation(
      namespace,
      path
    ).withPath { string2: String -> "$string2$string" } // example: resourceLocation("minecraft", "block/", "stone") -> "minecraft:block/stone"
  }

  fun getBlockTexture(block: Block): ResourceLocation {
    val resourceLocation = BuiltInRegistries.BLOCK.getKey(block)
    return resourceLocation.withPrefix("block/")
  }

  fun getBlockTexture(block: Block, string: String): ResourceLocation {
    val resourceLocation = BuiltInRegistries.BLOCK.getKey(block)
    return resourceLocation.withPath { string2: String -> "block/$string2$string" }
  }

  fun getItemTexture(item: Item): ResourceLocation {
    val resourceLocation = BuiltInRegistries.ITEM.getKey(item)
    return resourceLocation.withPrefix("item/")
  }

  fun getItemTexture(item: Item, string: String): ResourceLocation {
    val resourceLocation = BuiltInRegistries.ITEM.getKey(item)
    return resourceLocation.withPath { string2: String -> "item/$string2$string" }
  }

  fun getBlockModId(block: Block): String {
    return BuiltInRegistries.BLOCK.getKey(block).namespace
  }

  fun getItemModId(item: Item): String {
    return BuiltInRegistries.ITEM.getKey(item).namespace
  }

  fun getBlockId(block: Block): String {
    return BuiltInRegistries.BLOCK.getKey(block).path
  }

  fun getItemId(item: Item): String {
    return BuiltInRegistries.ITEM.getKey(item).path
  }

  fun getItemId(item: Supplier<ItemLike>): String {
    val names = item.get().asItem().descriptionId.split(".")
    return names[names.size - 1]
  }

  // @ Lang related
  val CONNECTING_WORDS = setOf(
    "of",
    "the",
    "and",
    "in",
    "on",
    "at",
    "to",
    "with",
    "by",
    "for",
    "as",
    "or",
    "nor",
    "but",
    "so",
    "yet",
    "a",
    "an"
  )

  fun asId(name: String): String {
    return name.lowercase().replace(" ", "_")
  }

  fun asName(id: String): String {
    return id.split("_")
      .joinToString(" ") { word ->
        if (word.lowercase() in CONNECTING_WORDS) {
          word.lowercase()
        } else {
          word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
      }
      .replace("  ", " ")
      .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
  }

  fun nonPluralId(name: String): String {
    val asId = asId(name)
    return if (asId.endsWith("s")) asId.substring(0, asId.length - 1) else asId
  }

  // TAGS

  object TAGS {
    fun <R, T : Registry<R>> optionalTag(registry: ResourceKey<T>, id: ResourceLocation): TagKey<R> {
      return TagKey.create(registry, id)
    }

    // VANILLA
    fun <R, T : Registry<R>> vanillaTag(registry: ResourceKey<T>, path: String): TagKey<R> {
      return optionalTag(registry, resourceLocation("minecraft", path))
    }

    fun vanillaBlockTag(path: String): TagKey<Block> {
      return vanillaTag(Registries.BLOCK, path)
    }

    fun vanillaItemTag(path: String): TagKey<Item> {
      return vanillaTag(Registries.ITEM, path)
    }

    // DELTABOX
    fun <R, T : Registry<R>> deltaboxTag(registry: ResourceKey<T>, path: String): TagKey<R> {
      return optionalTag(registry, resourceLocation(DeltaboxLibMod.MOD_ID, path))
    }

    fun deltaboxBlockTag(path: String): TagKey<Block> {
      return deltaboxTag(Registries.BLOCK, path)
    }

    fun deltaboxItemTag(path: String): TagKey<Item> {
      return deltaboxTag(Registries.ITEM, path)
    }

    fun deltaboxFluidTag(path: String): TagKey<Fluid> {
      return deltaboxTag(Registries.FLUID, path)
    }

    fun deltaboxBiomeTag(path: String): TagKey<Biome> {
      return deltaboxTag(Registries.BIOME, path)
    }

    fun deltaboxEntityTag(path: String): TagKey<EntityType<*>> {
      return deltaboxTag(Registries.ENTITY_TYPE, path)
    }

    // ANY MOD
    fun <R, T : Registry<R>> modTag(modId: String, registry: ResourceKey<T>, path: String): TagKey<R> {
      return optionalTag(registry, resourceLocation(modId, path))
    }

    fun modBlockTag(modId: String, path: String): TagKey<Block> {
      return modTag(modId, Registries.BLOCK, path)
    }

    fun modItemTag(modId: String, path: String): TagKey<Item> {
      return modTag(modId, Registries.ITEM, path)
    }

    fun modBiomeTag(modId: String, path: String): TagKey<Biome> {
      return modTag(modId, Registries.BIOME, path)
    }

    fun modEntityTag(modId: String, path: String): TagKey<EntityType<*>> {
      return modTag(modId, Registries.ENTITY_TYPE, path)
    }

    fun modFluidTag(modId: String, path: String): TagKey<Fluid> {
      return modTag(modId, Registries.FLUID, path)
    }

    // MODLOADERS
    fun <R, T : Registry<R>> modloaderTag(registry: ResourceKey<T>, path: String): MutableList<TagKey<R>> {
      return mutableListOf(
        optionalTag(registry, resourceLocation("c", path)), // tag for fabric
        optionalTag(registry, resourceLocation("forge", path)), // tag for forge
        optionalTag(registry, resourceLocation("neoforge", path)), // tag for neoforged
        optionalTag(
          registry,
          resourceLocation("deltaboxlib", path)
        ) // tag for deltaboxlib (generic for global iteration)
      )
    }

    fun modloaderBlockTag(path: String): MutableList<TagKey<Block>> {
      return modloaderTag(Registries.BLOCK, path)
    }

    fun modloaderItemTag(path: String): MutableList<TagKey<Item>> {
      return modloaderTag(Registries.ITEM, path)
    }

    fun modloaderFluidTag(path: String): MutableList<TagKey<Fluid>> {
      return modloaderTag(Registries.FLUID, path)
    }

    fun modloaderBiomeTag(path: String): MutableList<TagKey<Biome>> {
      return modloaderTag(Registries.BIOME, path)
    }

    fun modloaderEntityTag(path: String): MutableList<TagKey<EntityType<*>>> {
      return modloaderTag(Registries.ENTITY_TYPE, path)
    }

    // allow to use for recipe tags
    fun modloaderItemIngredient(path: String): Ingredient {
      return Ingredient.of(modloaderItemTag(path).map { Ingredient.of(it).items }.toTypedArray().flatten().stream())
    }
  }
}