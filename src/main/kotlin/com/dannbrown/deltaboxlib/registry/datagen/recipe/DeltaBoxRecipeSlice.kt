package com.dannbrown.deltaboxlib.registry.datagen.recipe

import com.dannbrown.deltaboxlib.lib.LibTags
import com.tterrag.registrate.util.DataIngredient
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import java.util.function.Consumer
import java.util.function.Supplier
import java.util.function.UnaryOperator

abstract class DeltaBoxRecipeSlice(private val modId: String) {
  val all: MutableList<ResourceLocation> = mutableListOf()

  abstract fun addRecipes(recipeConsumer: Consumer<FinishedRecipe>)
  abstract fun name(): String

  // generator
  fun cooking(
    recipeConsumer: Consumer<FinishedRecipe>,
    ingredient: Supplier<Ingredient>,
    result: Supplier<ItemLike>,
    builder: UnaryOperator<CookingRecipeBuilder> = UnaryOperator.identity()
  ): MutableMap<ResourceLocation, RecipeBuilder> {
    val allRecipes = CookingRecipeBuilder(modId, ingredient, result).apply(builder).getRecipes(recipeConsumer)
    all.addAll(allRecipes.keys)
    return allRecipes
  }

  fun crafting(
    recipeConsumer: Consumer<FinishedRecipe>,
    result: Supplier<ItemLike>,
    amount: Int = 1,
    builder: UnaryOperator<StandardRecipeBuilder> = UnaryOperator.identity()
  ): MutableMap<ResourceLocation, RecipeBuilder> {
    val allRecipes = StandardRecipeBuilder(modId, result, amount).apply(builder).getRecipes(recipeConsumer)
    all.addAll(allRecipes.keys)
    return allRecipes
  }


  fun oreRecipes(
    consumer: Consumer<FinishedRecipe>,
    name: String,
    ingot: ItemLike?,
    block: ItemLike?,
    nugget: ItemLike?,
    dust: ItemLike?,
    plate: ItemLike?,
    rawIngot: ItemLike?,
    crushedRaw: ItemLike?,
    rawBlock: ItemLike?,
    ores: List<OreBlockProcessor>?,
  ) {
    val hasOres = ores != null && ores.isNotEmpty()
    // smelt the raw ore into ingots
    if (ingot != null && rawIngot != null) {
      val SMELT_RAW = cooking(consumer, {
        DataIngredient.tag(LibTags.forgeItemTag("raw_materials/$name"))
      },
        { ingot }) { b ->
        b
          .suffix("_from_raw")
          .comboOreSmelting(200, 0.5f)
      }
    }
    // smelt the raw block into a block
    if (block != null && rawBlock != null) {
      val SMELT_RAW_BLOCK = cooking(consumer, {
        DataIngredient.tag(LibTags.forgeItemTag("storage_blocks/raw_$name"))
      },
        { block }) { b ->
        b
          .suffix("_from_raw_block")
          .comboOreSmelting(400, 4.5f)
      }
    }
    // smelt the ores into ingots
    if (ingot != null && hasOres) {
      val SMELT_ORE = cooking(consumer, {
        DataIngredient.tag(LibTags.forgeItemTag("ores/$name"))
      },
        { ingot }) { b ->
        b
          .suffix("_from_ore")
          .comboOreSmelting(200, 1f)
      }
    }
    // smelt the crushed raw ore into ingots
    if (ingot != null && crushedRaw != null) {
      val SMELT_CRUSHED = cooking(consumer, {
        DataIngredient.items(crushedRaw)
      },
        { ingot }) { b ->
        b
          .suffix("_from_crushed_raw")
          .comboOreSmelting(200, 1f)
      }
    }

    // smelt the dust into ingots
    if (ingot != null && dust != null) {
      val SMELT_DUST = cooking(consumer, {
        DataIngredient.tag(LibTags.forgeItemTag("dusts/$name"))
      },
        { ingot }) { b ->
        b
          .suffix("_from_dust")
          .comboOreSmelting(200, 0.5f)
      }
    }


    // smelt the plate into nuggets
    if (nugget != null && plate != null) {
      val SMELT_PLATE = cooking(consumer, {
        DataIngredient.tag(LibTags.forgeItemTag("plates/$name"))
      },
        { nugget }) { b ->
        b
          .suffix("_from_plate")
          .comboOreSmelting(200, 0.5f)
      }
    }
  }

  class OreBlockProcessor(oreBlock: ItemLike, stoneBlock: ItemLike, multiplier: Int) {
    val oreBlock = oreBlock
    val stoneBlock = stoneBlock
    val multiplier = multiplier
  }
}
