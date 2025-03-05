package com.dannbrown.deltaboxlib.registrate.datagen

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import java.util.function.Consumer
import java.util.function.Supplier

class RegistrateRecipes(
  private val registrate: AbstractDeltaboxRegistrate,
  private val exporter: Consumer<FinishedRecipe>
) {
  // SHAPED
  fun simpleShapedRecipe(
    result: Supplier<ItemLike>,
    pattern: Array<String>,
    key: Map<Char, Supplier<Ingredient>>,
    amount: Int = 1,
    name: String,
    suffix: String = ""
  ) {
    val builder = ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result.get(), amount)

    for (line in pattern) builder.pattern(line)
    for ((k, v) in key) {
      builder.define(k, v.get())
    }


    builder.unlockedBy(
      "has_ingredients",
      InventoryChangeTrigger.TriggerInstance.hasItems(*key.values.map { it.get().items[0].item }.toTypedArray())
    )
    builder.save(exporter, DeltaboxUtil.resourceLocation(registrate.modId, name + suffix))
  }

  fun simpleShapedRecipe(
    result: Supplier<ItemLike>,
    pattern: Array<String>,
    key: Map<Char, Supplier<Ingredient>>,
    amount: Int = 1,
    suffix: String = ""
  ) {
    simpleShapedRecipe(result, pattern, key, amount, DeltaboxUtil.getItemId(result), suffix)
  }

  // SHAPELESS
  fun simpleShapelessRecipe(
    result: Supplier<ItemLike>,
    ingredients: List<Supplier<Ingredient>>,
    category: RecipeCategory,
    amount: Int = 1,
    name: String,
    suffix: String = ""
  ) {
    val builder = ShapelessRecipeBuilder.shapeless(category, result.get(), amount)
    for (ingredient in ingredients) builder.requires(ingredient.get())

    builder.unlockedBy(
      "has_ingredients",
      InventoryChangeTrigger.TriggerInstance.hasItems(*ingredients.map { it.get().items[0].item }.toTypedArray())
    )
    builder.save(exporter, DeltaboxUtil.resourceLocation(registrate.modId, name + suffix))
  }

  fun simpleShapelessRecipe(
    result: Supplier<ItemLike>,
    ingredients: List<Supplier<Ingredient>>,
    category: RecipeCategory,
    amount: Int = 1,
    suffix: String = ""
  ) {
    simpleShapelessRecipe(result, ingredients, category, amount, DeltaboxUtil.getItemId(result), suffix)
  }

  fun directShapelessRecipe(
    result: Supplier<ItemLike>,
    ingredients: Supplier<Ingredient>,
    category: RecipeCategory,
    amount: Int = 1,
    suffix: String = ""
  ) {
    simpleShapelessRecipe(result, listOf(ingredients), category, amount, DeltaboxUtil.getItemId(result), suffix)
  }

}