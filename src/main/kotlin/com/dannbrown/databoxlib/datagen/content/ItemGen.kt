package com.dannbrown.databoxlib.datagen.content

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.content.block.CustomBacktankBlock
import com.dannbrown.databoxlib.content.item.GeckoArmor.BacktankCustomArmorItem
import com.dannbrown.databoxlib.content.item.GeckoArmor.CustomArmorItem
import com.dannbrown.databoxlib.content.item.GeckoArmor.SpaceHelmetItem
import com.dannbrown.databoxlib.datagen.transformers.ItemModelPresets
import com.dannbrown.databoxlib.init.ProjectTags
import com.dannbrown.databoxlib.lib.LibTags
import com.dannbrown.databoxlib.lib.LibUtils
import com.simibubi.create.AllTags
import com.simibubi.create.content.equipment.armor.BacktankItem
import com.tterrag.registrate.providers.DataGenContext
import com.tterrag.registrate.providers.RegistrateItemModelProvider
import com.tterrag.registrate.providers.RegistrateRecipeProvider
import com.tterrag.registrate.util.DataIngredient
import com.tterrag.registrate.util.entry.ItemEntry
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.tags.TagKey
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.Item
import java.util.function.Supplier

object ItemGen {
  fun simpleItem(
    name: String,
    itemFactory: (Item.Properties) -> Item = { p: Item.Properties -> Item(p) },
    vararg tags: TagKey<Item>
  ): ItemEntry<Item> {
    return ProjectContent.REGISTRATE.item<Item>(name, itemFactory)
      .tag(*tags)
      .register()
  }

  fun sheetItem(
    name: String,
    itemFactory: (Item.Properties) -> Item = { p: Item.Properties -> Item(p) },
    vararg tags: TagKey<Item>
  ): ItemEntry<Item> {
    return ProjectContent.REGISTRATE.item<Item>(name + "_sheet", itemFactory)
      .tag(LibTags.forgeItemTag("plates"))
      .tag(LibTags.forgeItemTag("plates/$name"))
      .tag(LibTags.createItemTag("plates"))
      .tag(LibTags.createItemTag("plates/$name"))
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
    return ProjectContent.REGISTRATE.item<Item>("raw_$name", itemFactory)
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
    return ProjectContent.REGISTRATE.item<Item>("crushed_raw_$name", itemFactory)
      .tag(LibTags.createItemTag("crushed_raw_materials"))
      .tag(LibTags.createItemTag("crushed_raw_$name"))
      .tag(*tags)
      .register()
  }

  fun dustItemControlled(
    name: String,
    dustTagKey: Supplier<TagKey<Item>>,
    itemFactory: (Item.Properties) -> Item = { p: Item.Properties -> Item(p) },
    vararg tags: TagKey<Item>
  ): ItemEntry<Item> {
    return ProjectContent.REGISTRATE.item<Item>(name, itemFactory)
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
    return ProjectContent.REGISTRATE.item<Item>(name, itemFactory)
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
    return ProjectContent.REGISTRATE.item<Item>(name, itemFactory)
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
    return ProjectContent.REGISTRATE.item<Item>(name, itemFactory)
      .recipe { c, p ->
        val recipePrefix = ProjectContent.MOD_ID + ":" + c.name

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

  fun backtankItem(name: String, material: Supplier<ArmorMaterial>, block: Supplier<CustomBacktankBlock>, ingredient: Supplier<Item>, ingredient2: Supplier<Item>): Pair<ItemEntry<out Item>, ItemEntry<out Item>> {
    // the backtank item
    var BACKTANK: ItemEntry<BacktankItem>? = null // delay initialization
    // the backtank placeable item
    val PLACEABLE = ProjectContent.REGISTRATE.item<BacktankItem.BacktankBlockItem?>(name + "_backtank_placeable"
    ) { p: Item.Properties? -> BacktankItem.BacktankBlockItem(block.get(), { BACKTANK!!.get() }, p) }
      .model { c: DataGenContext<Item?, BacktankItem.BacktankBlockItem?>, p: RegistrateItemModelProvider -> p.withExistingParent(c.name, p.mcLoc("item/barrier")) }
      .tag(ProjectTags.ITEM.EXCLUDE_FROM_CREATIVE) // exclude from creative tabs
      .register()
    // initialize the backtank item
    BACKTANK = ProjectContent.REGISTRATE.item<BacktankItem>(name + "_backtank")
    { p: Item.Properties -> BacktankCustomArmorItem(material.get(), p, LibUtils.resourceLocation(name), PLACEABLE) }
      .model(ItemModelPresets.simpleBlockItem("${name}_backtank/item"))
      .tag(AllTags.AllItemTags.PRESSURIZED_AIR_SOURCES.tag, LibTags.forgeItemTag("armors/${name}"), LibTags.forgeItemTag("armors/chestplates"), LibTags.databoxlibItemTag("${name}_armor"))
      .recipe { c, p ->
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get())
          .define('S', ingredient2.get())
          .define('I', ingredient.get())
          .pattern("S S")
          .pattern("SIS")
          .pattern("SSS")
          .unlockedBy("has_item", RegistrateRecipeProvider.has(ingredient.get()))
          .save(p)
      }
      .register()

    return Pair(BACKTANK, PLACEABLE)
  }

  fun helmetItem(name: String, material: Supplier<ArmorMaterial>, ingredient: Supplier<Item>, suffix: String = "_helmet"): ItemEntry<out Item> {
    return ProjectContent.REGISTRATE.item<Item>(name + suffix) { p -> SpaceHelmetItem(material.get(), p, LibUtils.resourceLocation(name)) }
      .tag(LibTags.forgeItemTag("armors/${name}"), LibTags.forgeItemTag("armors/helmets"), LibTags.databoxlibItemTag("${name}_armor"))
      .recipe { c, p ->
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get())
          .define('S', ingredient.get())
          .pattern("SSS")
          .pattern("S S")
          .unlockedBy("has_item", RegistrateRecipeProvider.has(ingredient.get()))
          .save(p)
      }
      .register()
  }

  fun chestplateItem(name: String, material: Supplier<ArmorMaterial>, ingredient: Supplier<Item>, suffix: String = "_chestplate"): ItemEntry<out Item> {
    return ProjectContent.REGISTRATE.item<Item>(name + suffix) { p -> CustomArmorItem(material.get(), ArmorItem.Type.CHESTPLATE, p) }
      .tag(LibTags.forgeItemTag("armors/${name}"), LibTags.forgeItemTag("armors/chestplates"), LibTags.databoxlibItemTag("${name}_armor"))
      .recipe { c, p ->
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get())
          .define('S', ingredient.get())
          .pattern("S S")
          .pattern("SSS")
          .pattern("SSS")
          .unlockedBy("has_item", RegistrateRecipeProvider.has(ingredient.get()))
          .save(p)
      }
      .register()
  }

  fun leggingsItem(name: String, material: Supplier<ArmorMaterial>, ingredient: Supplier<Item>, suffix: String = "_leggings"): ItemEntry<out Item> {
    return ProjectContent.REGISTRATE.item<Item>(name + suffix) { p -> CustomArmorItem(material.get(), ArmorItem.Type.LEGGINGS, p) }
      .tag(LibTags.forgeItemTag("armors/${name}"), LibTags.forgeItemTag("armors/leggings"), LibTags.databoxlibItemTag("${name}_armor"))
      .recipe { c, p ->
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get())
          .define('S', ingredient.get())
          .pattern("SSS")
          .pattern("S S")
          .pattern("S S")
          .unlockedBy("has_item", RegistrateRecipeProvider.has(ingredient.get()))
          .save(p)
      }
      .register()
  }

  fun bootsItem(name: String, material: Supplier<ArmorMaterial>, ingredient: Supplier<Item>, suffix: String = "_boots"): ItemEntry<out Item> {
    return ProjectContent.REGISTRATE.item<Item>(name + suffix) { p -> CustomArmorItem(material.get(), ArmorItem.Type.BOOTS, p) }
      .tag(LibTags.forgeItemTag("armors/${name}"), LibTags.forgeItemTag("armors/boots"), LibTags.databoxlibItemTag("${name}_armor"))
      .recipe { c, p ->
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get())
          .define('S', ingredient.get())
          .pattern("S S")
          .pattern("S S")
          .unlockedBy("has_item", RegistrateRecipeProvider.has(ingredient.get()))
          .save(p)
      }
      .register()
  }
}