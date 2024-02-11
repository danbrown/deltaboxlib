package com.dannbrown.databoxlib.datagen.content

import com.dannbrown.databoxlib.DataboxLib
import com.dannbrown.databoxlib.lib.LibTags
import com.dannbrown.databoxlib.registry.DataboxRegistrate
import com.tterrag.registrate.providers.RegistrateRecipeProvider
import com.tterrag.registrate.util.DataIngredient
import com.tterrag.registrate.util.entry.ItemEntry
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import java.util.function.Supplier

class ItemGen(private val registrate: DataboxRegistrate = DataboxLib.REGISTRATE) {
  fun simpleItem(
    name: String,
    itemFactory: (Item.Properties) -> Item = { p: Item.Properties -> Item(p) },
    vararg tags: TagKey<Item>
  ): ItemEntry<Item> {
    return registrate.item<Item>(name, itemFactory)
      .tag(*tags)
      .register()
  }

  fun sheetItem(
    name: String,
    itemFactory: (Item.Properties) -> Item = { p: Item.Properties -> Item(p) },
    vararg tags: TagKey<Item>
  ): ItemEntry<Item> {
    return registrate.item<Item>(name + "_sheet", itemFactory)
      .tag(LibTags.forgeItemTag("plates"))
      .tag(LibTags.forgeItemTag("plates/$name"))
      .tag(LibTags.modItemTag("create","plates"))
      .tag(LibTags.modItemTag("create","plates/$name"))
      .tag(*tags)
      .recipe { c, p ->
      }
      .register()
  }

  fun rawOreItem(
    name: String,
    itemFactory: (Item.Properties) -> Item = { p: Item.Properties -> Item(p) },
    vararg tags: TagKey<Item>
  ): ItemEntry<Item> {
    return registrate.item<Item>("raw_$name", itemFactory)
      .tag(LibTags.forgeItemTag("raw_materials"))
      .tag(LibTags.forgeItemTag("raw_materials/$name"))
      .tag(*tags)
      .register()
  }

  fun crushedRawOreItem(
    name: String,
    itemFactory: (Item.Properties) -> Item = { p: Item.Properties -> Item(p) },
    vararg tags: TagKey<Item>
  ): ItemEntry<Item> {
    return registrate.item<Item>("crushed_raw_$name", itemFactory)
      .tag(LibTags.modItemTag("create", "crushed_raw_materials"))
      .tag(LibTags.modItemTag("create","crushed_raw_$name"))
      .tag(*tags)
      .register()
  }

  fun dustItemControlled(
    name: String,
    dustTagKey: Supplier<TagKey<Item>>,
    itemFactory: (Item.Properties) -> Item = { p: Item.Properties -> Item(p) },
    vararg tags: TagKey<Item>
  ): ItemEntry<Item> {
    return registrate.item<Item>(name, itemFactory)
      .tag(LibTags.forgeItemTag("dusts"))
      .tag(dustTagKey.get())
      .tag(*tags)
      .register()
  }

  fun dustItem(
    name: String,
    itemFactory: (Item.Properties) -> Item = { p: Item.Properties -> Item(p) },
    vararg tags: TagKey<Item>
  ): ItemEntry<Item> {
    return dustItemControlled(name + "_dust", { LibTags.forgeItemTag("dusts/$name") }, itemFactory, *tags)
  }

  fun ingotItemControlled(
    name: String,
    ingotTagKey: Supplier<TagKey<Item>>,
    itemFactory: (Item.Properties) -> Item = { p: Item.Properties -> Item(p) },
    vararg tags: TagKey<Item>
  ): ItemEntry<Item> {
    return registrate.item<Item>(name, itemFactory)
      .tag(LibTags.forgeItemTag("ingots"))
      .tag(ingotTagKey.get())
      .tag(*tags)
      .register()
  }

  fun gemItemControlled(
    name: String,
    ingotTagKey: Supplier<TagKey<Item>>,
    itemFactory: (Item.Properties) -> Item = { p: Item.Properties -> Item(p) },
    vararg tags: TagKey<Item>
  ): ItemEntry<Item> {
    return registrate.item<Item>(name, itemFactory)
      .tag(LibTags.forgeItemTag("gems"))
      .tag(ingotTagKey.get())
      .tag(*tags)
      .register()
  }

  fun ingotItem(
    name: String,
    itemFactory: (Item.Properties) -> Item = { p: Item.Properties -> Item(p) },
    vararg tags: TagKey<Item>
  ): ItemEntry<Item> {
    return ingotItemControlled(name + "_ingot", { LibTags.forgeItemTag("ingots/$name") }, itemFactory, *tags)
  }

  fun gemItem(
    name: String,
    itemFactory: (Item.Properties) -> Item = { p: Item.Properties -> Item(p) },
    vararg tags: TagKey<Item>
  ): ItemEntry<Item> {
    return ingotItemControlled(name, { LibTags.forgeItemTag("gems/$name") }, itemFactory, *tags)
  }

  fun nuggetItemControlled(
    name: String,
    nuggetTagKey: Supplier<TagKey<Item>>,
    ingotIngredient: Supplier<DataIngredient>,
    ingotItem: Supplier<Item>,
    itemFactory: (Item.Properties) -> Item = { p: Item.Properties -> Item(p) },
    vararg tags: TagKey<Item>
  ): ItemEntry<Item> {
    return registrate.item<Item>(name, itemFactory)
      .recipe { c, p ->
        val recipePrefix = registrate.modid + ":" + c.name

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ingotItem.get(), 1)
          .define('I', nuggetTagKey.get())
          .pattern("III")
          .pattern("III")
          .pattern("III")
          .unlockedBy("has_ingredient", RegistrateRecipeProvider.has(c.get()))
          .save({ t: FinishedRecipe -> p.accept(t) }, recipePrefix + "_to_ingot")

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, c.get(), 9)
          .requires(ingotIngredient.get())
          .unlockedBy("has_ingredient", RegistrateRecipeProvider.has(ingotItem.get()))
          .save({ t: FinishedRecipe -> p.accept(t) }, recipePrefix + "_from_ingot")
      }
      .tag(LibTags.forgeItemTag("nuggets"))
      .tag(nuggetTagKey.get())
      .tag(*tags)
      .register()
  }

  fun nuggetItem(
    name: String,
    ingotItem: Supplier<Item>,
    itemFactory: (Item.Properties) -> Item = { p: Item.Properties -> Item(p) },
    vararg tags: TagKey<Item>
  ): ItemEntry<Item> {
    return nuggetItemControlled(name + "_nugget",
      { LibTags.forgeItemTag("nuggets/$name") },
      { DataIngredient.tag(LibTags.forgeItemTag("ingots/$name")) },
      ingotItem,
      itemFactory, *tags)
  }

}