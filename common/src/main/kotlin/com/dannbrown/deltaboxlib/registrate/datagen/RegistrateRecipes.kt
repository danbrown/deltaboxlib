package com.dannbrown.deltaboxlib.registrate.datagen

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.data.recipes.SingleItemRecipeBuilder
import net.minecraft.world.item.Items
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
  // END SHAPELESS

  // Storage Blocks
  fun storageBlockRecipe(result: Supplier<ItemLike>, ingotItem: Supplier<ItemLike>, ingredient: Supplier<Ingredient>) {
    simpleShapedRecipe(
      result,
      arrayOf("III", "III", "III"),
      mapOf('I' to ingredient),
      1,
      "_from_materials"
    )
    simpleShapelessRecipe(
      ingotItem,
      listOf(Supplier { Ingredient.of(result.get()) }),
      RecipeCategory.BUILDING_BLOCKS,
      9,
      DeltaboxUtil.getItemId(result),
      "_to_materials"
    )
  }

  fun smallStorageBlockRecipe(
    result: Supplier<ItemLike>,
    ingotItem: Supplier<ItemLike>,
    ingredient: Supplier<Ingredient>
  ) {
    simpleShapedRecipe(
      result,
      arrayOf("II", "II"),
      mapOf('I' to ingredient),
      1,
      "_from_materials"
    )
    simpleShapelessRecipe(
      ingotItem,
      listOf(Supplier { Ingredient.of(result.get()) }),
      RecipeCategory.BUILDING_BLOCKS,
      4,
      DeltaboxUtil.getItemId(result),
      "_to_materials"
    )
  }
  // End Storage Blocks

  // Stonecutting
  fun simpleStonecuttingRecipe(result: Supplier<ItemLike>, ingredient: Supplier<ItemLike>, amount: Int = 1) {
    SingleItemRecipeBuilder.stonecutting(
      Ingredient.of(ingredient.get()),
      RecipeCategory.BUILDING_BLOCKS,
      result.get(),
      amount
    )
      .unlockedBy("has_ingredients", InventoryChangeTrigger.TriggerInstance.hasItems(ingredient.get()))
      .save(
        exporter,
        DeltaboxUtil.resourceLocation(
          registrate.modId,
          DeltaboxUtil.getItemId(ingredient) + "_to_" + DeltaboxUtil.getItemId(result) + "_stonecutting"
        )
      )
  }
  // End Stonecutting

  fun stairsCraftingRecipe(result: Supplier<ItemLike>, ingredient: Supplier<Ingredient>) {
    simpleShapedRecipe(result, arrayOf("I  ", "II ", "III"), mapOf('I' to ingredient), 4, "_craft")
  }

  fun slabCraftingRecipe(result: Supplier<ItemLike>, ingredient: Supplier<Ingredient>) {
    simpleShapedRecipe(result, arrayOf("III"), mapOf('I' to ingredient), 6, "_craft")
  }

  fun wallCraftingRecipe(result: Supplier<ItemLike>, ingredient: Supplier<Ingredient>) {
    simpleShapedRecipe(result, arrayOf("III", "III"), mapOf('I' to ingredient), 6, "_craft")
  }

  fun fenceCraftingRecipe(result: Supplier<ItemLike>, ingredient: Supplier<Ingredient>) {
    simpleShapedRecipe(
      result,
      arrayOf("ISI", "ISI"),
      mapOf('I' to ingredient, 'S' to Supplier { Ingredient.of(Items.STICK) }),
      3,
      "_craft"
    )
  }

  fun fenceGateCraftingRecipe(result: Supplier<ItemLike>, ingredient: Supplier<Ingredient>) {
    simpleShapedRecipe(
      result,
      arrayOf("SIS", "SIS"),
      mapOf('I' to ingredient, 'S' to Supplier { Ingredient.of(Items.STICK) }),
      1,
      "_craft"
    )
  }

  fun signCraftingRecipe(result: Supplier<ItemLike>, ingredient: Supplier<Ingredient>) {
    simpleShapedRecipe(
      result,
      arrayOf("III", "III", " S "),
      mapOf('I' to ingredient, 'S' to Supplier { Ingredient.of(Items.STICK) }),
      3,
      "_craft"
    )
  }

  fun hangingSignCraftingRecipe(result: Supplier<ItemLike>, ingredient: Supplier<Ingredient>) {
    simpleShapedRecipe(
      result,
      arrayOf("C C", "III", "III"),
      mapOf('I' to ingredient, 'C' to Supplier { Ingredient.of(Items.CHAIN) }),
      6,
      "_craft"
    )
  }

  fun pressurePlateCraftingRecipe(result: Supplier<ItemLike>, ingredient: Supplier<Ingredient>) {
    simpleShapedRecipe(result, arrayOf("II"), mapOf('I' to ingredient), 1, "_craft")
  }

  fun doorCraftingRecipe(result: Supplier<ItemLike>, ingredient: Supplier<Ingredient>) {
    simpleShapedRecipe(result, arrayOf("II", "II", "II"), mapOf('I' to ingredient), 3, "_craft")
  }

  fun trapdoorCraftingRecipe(result: Supplier<ItemLike>, ingredient: Supplier<Ingredient>) {
    simpleShapedRecipe(result, arrayOf("III", "III"), mapOf('I' to ingredient), 2, "_craft")
  }

  fun polishedCraftingRecipe(result: Supplier<ItemLike>, ingredient: Supplier<Ingredient>, amount: Int = 4) {
    simpleShapedRecipe(result, arrayOf("II", "II"), mapOf('I' to ingredient), amount, "_craft")
  }

  fun slabToChiseledRecipe(result: Supplier<ItemLike>, ingredient: Supplier<Ingredient>) {
    simpleShapedRecipe(result, arrayOf("I", "I"), mapOf('I' to ingredient), 1, "_craft")
  }
}