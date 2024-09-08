package com.dannbrown.deltaboxlib.registry.datagen.recipe

import com.dannbrown.deltaboxlib.DeltaboxLib
import com.dannbrown.deltaboxlib.lib.LibObjects
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataGenerator
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.ItemLike
import net.minecraftforge.common.crafting.conditions.IConditionBuilder
import net.minecraftforge.fluids.FluidType
import java.io.IOException
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer
import java.util.function.Supplier

// class based on https://github.com/Creators-of-Create/Create/blob/mc1.19/0.5.1/src/main/java/com/simibubi/create/foundation/data/recipe/ProcessingRecipeGen.java
// the goal is to be able to create custom processing recipes
class DeltaboxRecipeProvider(private val output: PackOutput, private val slices: List<DeltaboxRecipeSlice>) : RecipeProvider(output), IConditionBuilder {
  override fun buildRecipes(recipeConsumer: Consumer<FinishedRecipe>) {
    for(slice in slices) {
      slice.addRecipes(recipeConsumer)
      DeltaboxLib.LOGGER.info("DeltaBox Lib registered " + slice.all.size + " recipe" + (if (slice.all.size == 1) "" else "s" + " for " + slice.name()))
    }
  }

  abstract class DeltaBoxRecipeBuilder {
    abstract fun getRecipes(consumer: Consumer<FinishedRecipe>): Set<ResourceLocation>
  }

  companion object {
    protected val BUCKET = FluidType.BUCKET_VOLUME
    protected val BOTTLE = 250
    fun createSimpleLocation(
      modId: String,
      recipeType: String,
      result: Supplier<ItemLike>,
      prefix: String,
      suffix: String
    ): ResourceLocation {
      return ResourceLocation(modId, recipeType + "/" + prefix + LibObjects.getKeyOrThrow(result.get().asItem()).path + suffix)
    }
  }
}