package com.dannbrown.deltaboxlib.registry.datagen.recipe

import com.dannbrown.deltaboxlib.lib.LibObjects
import com.dannbrown.deltaboxlib.registry.datagen.recipe.DeltaboxRecipeProvider.Companion.createSimpleLocation
import net.minecraft.advancements.critereon.ItemPredicate
import com.tterrag.registrate.providers.RegistrateRecipeProvider
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleCookingSerializer
import net.minecraft.world.level.ItemLike
import java.util.function.Consumer
import java.util.function.Supplier
import java.util.function.UnaryOperator

// COOKING RECIPE CLASS
class CookingRecipeBuilder(private val modId: String, private val ingredient: Supplier<Ingredient>, private val result: Supplier<ItemLike>): DeltaboxRecipeProvider.DeltaBoxRecipeBuilder() {
  private var unlockedBy: Supplier<ItemPredicate>? = null
  private var prefix: String = ""
  private var suffix: String = ""
  private var exp: Float = 0f
  private var cookingTime: Int = 200
  private val COOKING_TIME_MODIFIER = 1.0f
  private val allRecipes: MutableMap<ResourceLocation, RecipeBuilder> = HashMap()

  fun apply(builder: UnaryOperator<CookingRecipeBuilder>): CookingRecipeBuilder {
    return builder.apply(this)
  }

  fun prefix(prefix: String): CookingRecipeBuilder {
    this.prefix = prefix
    return this
  }

  fun suffix(suffix: String): CookingRecipeBuilder {
    this.suffix = suffix
    return this
  }

  fun cookingTime(time: Int): CookingRecipeBuilder {
    this.cookingTime = time
    return this
  }

  fun experience(exp: Float): CookingRecipeBuilder {
    this.exp = exp
    return this
  }

  fun unlockedBy(unlockedBy: Supplier<ItemPredicate>): CookingRecipeBuilder {
    this.unlockedBy = unlockedBy
    return this
  }

  fun inFurnace(
    cookingTime: Int = this.cookingTime,
    exp: Float = this.exp,
  ): CookingRecipeBuilder {
    createCookingRecipe("_smelting", SimpleCookingSerializer.SMELTING_RECIPE, cookingTime, exp)
    return this
  }

  fun inBlastFurnace(
    cookingTime: Int = this.cookingTime,
    exp: Float = this.exp,
  ): CookingRecipeBuilder {
    createCookingRecipe("_blasting", SimpleCookingSerializer.BLASTING_RECIPE, cookingTime, exp)
    return this
  }

  fun inSmoker(
    cookingTime: Int = this.cookingTime,
    exp: Float = this.exp,
  ): CookingRecipeBuilder {
    createCookingRecipe("_smoking", SimpleCookingSerializer.SMOKING_RECIPE, cookingTime, exp)
    return this
  }

  fun inCampfire(
    cookingTime: Int = this.cookingTime,
    exp: Float = this.exp,
  ): CookingRecipeBuilder {
    createCookingRecipe(
      "_campfire_cooking",
      SimpleCookingSerializer.CAMPFIRE_COOKING_RECIPE,
      cookingTime,
      exp
    )
    return this
  }

  fun comboOreSmelting(
    cookingTime: Int = this.cookingTime,
    exp: Float = this.exp,
  ): CookingRecipeBuilder {
    this.inFurnace(cookingTime, exp)
    this.inBlastFurnace(cookingTime / 2, exp)
    return this
  }

  fun comboFoodCooking(
    cookingTime: Int = this.cookingTime,
    exp: Float = this.exp,
  ): CookingRecipeBuilder {
    this.inFurnace(cookingTime, exp)
    this.inSmoker(cookingTime / 2, exp)
    this.inCampfire(cookingTime / 2, exp)
    return this
  }

  protected fun <T : AbstractCookingRecipe> createCookingRecipe(
    suffixRecipe: String,
    serializer: RecipeSerializer<T>,
    cookingTime: Int,
    exp: Float,
  ): CookingRecipeBuilder {
    val builder = SimpleCookingRecipeBuilder.generic(
      ingredient.get(),
      RecipeCategory.MISC,
      result.get(),
      exp,
      (cookingTime * COOKING_TIME_MODIFIER).toInt(),
      serializer
    )

    if (unlockedBy != null) {
      builder.unlockedBy("has_item", RegistrateRecipeProvider.inventoryTrigger(unlockedBy!!.get()))
    } else {
      builder.unlockedBy(
        "has_item",
        RegistrateRecipeProvider.inventoryTrigger(
          ItemPredicate.Builder.item().of(Items.AIR).build()
        )
      )
    }
    val path = LibObjects.getKeyOrThrow(serializer).path


    allRecipes[createSimpleLocation(modId, path, result, prefix, suffix + suffixRecipe)] = builder

    return this
  }

  override fun getRecipes(consumer: Consumer<FinishedRecipe>): Set<ResourceLocation> {
    for ((key, value) in allRecipes) {
      value.save(consumer, key)
    }
    return allRecipes.keys
  }
}
