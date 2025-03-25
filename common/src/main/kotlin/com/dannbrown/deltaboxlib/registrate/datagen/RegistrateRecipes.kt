package com.dannbrown.deltaboxlib.registrate.datagen

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.util.DataIngredient
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder
import net.minecraft.data.recipes.SingleItemRecipeBuilder
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import java.util.function.Consumer
import java.util.function.Supplier

class RegistrateRecipes(
  val registrate: AbstractDeltaboxRegistrate,
  val exporter: Consumer<FinishedRecipe>
) {

  val DEFAULT_COOKING_XP = 0.0f
  val DEFAULT_COOKING_TIME = 200

  // SHAPED
  fun simpleShapedRecipe(
    result: Supplier<ItemLike>,
    pattern: Array<String>,
    key: Map<Char, Supplier<DataIngredient>>,
    amount: Int = 1,
    name: String? = null,
    suffix: String = ""
  ) {
    val asName = name ?: DeltaboxUtil.getItemId(result.get())
    val builder = ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result.get(), amount)

    for (line in pattern) builder.pattern(line)
    for ((k, v) in key) {
      builder.define(k, v.get().ingredient())
    }

    val ingredients = key.values.toList()
    DataIngredient.addIngredientsRecipeCriterions(builder, ingredients, asName)

    builder.save(exporter, DeltaboxUtil.resourceLocation(registrate.modId, asName + suffix))
  }

  fun simpleShapedRecipe(
    result: Supplier<ItemLike>,
    pattern: Array<String>,
    key: Map<Char, Supplier<DataIngredient>>,
    amount: Int = 1,
    suffix: String = ""
  ) {
    simpleShapedRecipe(result, pattern, key, amount, DeltaboxUtil.getItemId(result), suffix)
  }

  // SHAPELESS
  fun simpleShapelessRecipe(
    result: Supplier<ItemLike>,
    ingredients: List<Supplier<DataIngredient>>,
    category: RecipeCategory,
    amount: Int = 1,
    name: String? = null,
    suffix: String = ""
  ) {
    val asName = name ?: DeltaboxUtil.getItemId(result.get())
    val builder = ShapelessRecipeBuilder.shapeless(category, result.get(), amount)
    for (ingredient in ingredients) builder.requires(ingredient.get().ingredient())

    DataIngredient.addIngredientsRecipeCriterions(builder, ingredients, asName)

    builder.save(exporter, DeltaboxUtil.resourceLocation(registrate.modId, asName + suffix))
  }

  fun simpleShapelessRecipe(
    result: Supplier<ItemLike>,
    ingredients: List<Supplier<DataIngredient>>,
    category: RecipeCategory,
    amount: Int = 1,
    suffix: String = ""
  ) {
    simpleShapelessRecipe(result, ingredients, category, amount, DeltaboxUtil.getItemId(result), suffix)
  }

  fun directShapelessRecipe(
    result: Supplier<ItemLike>,
    ingredients: Supplier<DataIngredient>,
    category: RecipeCategory,
    amount: Int = 1,
    suffix: String = ""
  ) {
    simpleShapelessRecipe(result, listOf(ingredients), category, amount, DeltaboxUtil.getItemId(result), suffix)
  }
  // END SHAPELESS

  // COOKING
  enum class CookingRecipeType {
    SMELTING, BLASTING, CAMPFIRE, SMOKING;
  }

  fun simpleCookingRecipe(
    result: Supplier<ItemLike>,
    ingredients: Supplier<DataIngredient>,
    category: RecipeCategory,
    type: CookingRecipeType,
    experience: Float = DEFAULT_COOKING_XP,
    cookingTime: Int = DEFAULT_COOKING_TIME,
    name: String? = null,
    suffix: String = ""
  ) {
    val asName = name ?: DeltaboxUtil.getItemId(result.get())
    val builder = when (type) {
      CookingRecipeType.SMELTING -> SimpleCookingRecipeBuilder.smelting(
        ingredients.get().ingredient(),
        category,
        result.get(),
        experience,
        cookingTime
      )

      CookingRecipeType.BLASTING -> SimpleCookingRecipeBuilder.blasting(
        ingredients.get().ingredient(),
        category,
        result.get(),
        experience,
        cookingTime
      )

      CookingRecipeType.CAMPFIRE -> SimpleCookingRecipeBuilder.campfireCooking(
        ingredients.get().ingredient(),
        category,
        result.get(),
        experience,
        cookingTime
      )

      CookingRecipeType.SMOKING -> SimpleCookingRecipeBuilder.smoking(
        ingredients.get().ingredient(),
        category,
        result.get(),
        experience,
        cookingTime
      )
    }
    val _suffix = when (type) {
      CookingRecipeType.SMELTING -> suffix + "_smelting"
      CookingRecipeType.BLASTING -> suffix + "_blasting"
      CookingRecipeType.CAMPFIRE -> suffix + "_campfire"
      CookingRecipeType.SMOKING -> suffix + "_smoking"
    }

    DataIngredient.addIngredientsRecipeCriterions(builder, listOf(ingredients), asName)

    builder.save(exporter, DeltaboxUtil.resourceLocation(registrate.modId, asName + _suffix))
  }

  fun simpleCookingRecipe(
    result: Supplier<ItemLike>,
    ingredients: Supplier<DataIngredient>,
    category: RecipeCategory,
    type: CookingRecipeType,
    experience: Float = DEFAULT_COOKING_XP,
    cookingTime: Int = DEFAULT_COOKING_TIME,
    suffix: String = ""
  ) {
    simpleCookingRecipe(
      result,
      ingredients,
      category,
      type,
      experience,
      cookingTime,
      DeltaboxUtil.getItemId(result),
      suffix
    )
  }

  fun comboBlastingRecipe(
    result: Supplier<ItemLike>,
    ingredients: Supplier<DataIngredient>,
    category: RecipeCategory,
    experience: Float = DEFAULT_COOKING_XP,
    cookingTime: Int = DEFAULT_COOKING_TIME,
    suffix: String = ""
  ) {
    simpleCookingRecipe(
      result,
      ingredients,
      category,
      CookingRecipeType.BLASTING,
      experience * 2,
      cookingTime / 2,
      suffix
    )

    simpleCookingRecipe(
      result,
      ingredients,
      category,
      CookingRecipeType.SMELTING,
      experience,
      cookingTime,
      suffix
    )
  }

  fun comboFoodRecipe(
    result: Supplier<ItemLike>,
    ingredients: Supplier<DataIngredient>,
    category: RecipeCategory,
    experience: Float = DEFAULT_COOKING_XP,
    cookingTime: Int = DEFAULT_COOKING_TIME,
    suffix: String = ""
  ) {
    simpleCookingRecipe(
      result,
      ingredients,
      category,
      CookingRecipeType.SMOKING,
      experience * 2,
      cookingTime / 2,
      suffix
    )

    simpleCookingRecipe(
      result,
      ingredients,
      category,
      CookingRecipeType.CAMPFIRE,
      experience * 2,
      cookingTime / 2,
      suffix
    )

    simpleCookingRecipe(
      result,
      ingredients,
      category,
      CookingRecipeType.SMELTING,
      experience,
      cookingTime,
      suffix
    )
  }


  // END COOKING

  // Storage Blocks
  fun storageBlockRecipe(
    result: Supplier<ItemLike>,
    ingotItem: Supplier<ItemLike>,
    ingredient: Supplier<DataIngredient>
  ) {
    simpleShapedRecipe(
      result,
      arrayOf("III", "III", "III"),
      mapOf('I' to ingredient),
      1,
      "_from_materials"
    )
    simpleShapelessRecipe(
      ingotItem,
      listOf(Supplier { DataIngredient(result.get()) }),
      RecipeCategory.BUILDING_BLOCKS,
      9,
      DeltaboxUtil.getItemId(result),
      "_to_materials"
    )
  }

  fun smallStorageBlockRecipe(
    result: Supplier<ItemLike>,
    ingotItem: Supplier<ItemLike>,
    ingredient: Supplier<DataIngredient>
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
      listOf(Supplier { DataIngredient(result.get()) }),
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
      DataIngredient(ingredient.get()).ingredient(),
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

  fun stairsCraftingRecipe(result: Supplier<ItemLike>, ingredient: Supplier<DataIngredient>) {
    simpleShapedRecipe(result, arrayOf("I  ", "II ", "III"), mapOf('I' to ingredient), 4, "_craft")
  }

  fun slabCraftingRecipe(result: Supplier<ItemLike>, ingredient: Supplier<DataIngredient>) {
    simpleShapedRecipe(result, arrayOf("III"), mapOf('I' to ingredient), 6, "_craft")
  }

  fun wallCraftingRecipe(result: Supplier<ItemLike>, ingredient: Supplier<DataIngredient>) {
    simpleShapedRecipe(result, arrayOf("III", "III"), mapOf('I' to ingredient), 6, "_craft")
  }

  fun fenceCraftingRecipe(result: Supplier<ItemLike>, ingredient: Supplier<DataIngredient>) {
    simpleShapedRecipe(
      result,
      arrayOf("ISI", "ISI"),
      mapOf('I' to ingredient, 'S' to Supplier { DataIngredient(Items.STICK) }),
      3,
      "_craft"
    )
  }

  fun fenceGateCraftingRecipe(result: Supplier<ItemLike>, ingredient: Supplier<DataIngredient>) {
    simpleShapedRecipe(
      result,
      arrayOf("SIS", "SIS"),
      mapOf('I' to ingredient, 'S' to Supplier { DataIngredient(Items.STICK) }),
      1,
      "_craft"
    )
  }

  fun signCraftingRecipe(result: Supplier<ItemLike>, ingredient: Supplier<DataIngredient>) {
    simpleShapedRecipe(
      result,
      arrayOf("III", "III", " S "),
      mapOf('I' to ingredient, 'S' to Supplier { DataIngredient(Items.STICK) }),
      3,
      "_craft"
    )
  }

  fun hangingSignCraftingRecipe(result: Supplier<ItemLike>, ingredient: Supplier<DataIngredient>) {
    simpleShapedRecipe(
      result,
      arrayOf("C C", "III", "III"),
      mapOf('I' to ingredient, 'C' to Supplier { DataIngredient(Items.CHAIN) }),
      6,
      "_craft"
    )
  }

  fun pressurePlateCraftingRecipe(result: Supplier<ItemLike>, ingredient: Supplier<DataIngredient>) {
    simpleShapedRecipe(result, arrayOf("II"), mapOf('I' to ingredient), 1, "_craft")
  }

  fun doorCraftingRecipe(result: Supplier<ItemLike>, ingredient: Supplier<DataIngredient>) {
    simpleShapedRecipe(result, arrayOf("II", "II", "II"), mapOf('I' to ingredient), 3, "_craft")
  }

  fun trapdoorCraftingRecipe(result: Supplier<ItemLike>, ingredient: Supplier<DataIngredient>) {
    simpleShapedRecipe(result, arrayOf("III", "III"), mapOf('I' to ingredient), 2, "_craft")
  }

  fun polishedCraftingRecipe(result: Supplier<ItemLike>, ingredient: Supplier<DataIngredient>, amount: Int = 4) {
    simpleShapedRecipe(result, arrayOf("II", "II"), mapOf('I' to ingredient), amount, "_craft")
  }

  fun slabToChiseledRecipe(result: Supplier<ItemLike>, ingredient: Supplier<DataIngredient>) {
    simpleShapedRecipe(result, arrayOf("I", "I"), mapOf('I' to ingredient), 1, "_craft")
  }

  fun boatCraftingRecipe(result: Supplier<ItemLike>, ingredient: Supplier<DataIngredient>) {
    simpleShapedRecipe(result, arrayOf("I I", "III"), mapOf('I' to ingredient), 1, "_craft")
  }

  fun chestboatCraftingRecipe(result: Supplier<ItemLike>, ingredient: Supplier<ItemLike>) {
    directShapelessRecipe(
      result,
      { DataIngredient(Items.CHEST, ingredient.get()) },
      RecipeCategory.BUILDING_BLOCKS,
      1,
      "_craft"
    )
  }
}