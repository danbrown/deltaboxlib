package com.dannbrown.databoxlib.registry.datagen

import com.dannbrown.databoxlib.DataboxLib
import com.dannbrown.databoxlib.lib.LibObjects
import com.dannbrown.databoxlib.registry.datagen.DataboxRecipeProvider.GeneratedRecipe
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataGenerator
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleCookingSerializer
import net.minecraft.world.level.ItemLike
import net.minecraftforge.fluids.FluidType
import java.io.IOException
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer
import java.util.function.Supplier
import java.util.function.UnaryOperator
import kotlin.reflect.KClass

// class based on https://github.com/Creators-of-Create/Create/blob/mc1.19/0.5.1/src/main/java/com/simibubi/create/foundation/data/recipe/ProcessingRecipeGen.java
// the goal is to be able to create custom processing recipes
abstract class DataboxRecipeProvider(output: PackOutput, private val modId: String) : RecipeProvider(output) {
  val all: MutableList<GeneratedRecipe> = ArrayList()

  override fun buildRecipes(recipeConsumer: Consumer<FinishedRecipe>) {
    all.forEach(Consumer<GeneratedRecipe> { c: GeneratedRecipe -> c.register(recipeConsumer) })
    DataboxLib.LOGGER.info(name + " registered " + all.size + " recipe" + (if (all.size == 1) "" else "s"))
  }

  fun register(recipe: GeneratedRecipe): GeneratedRecipe {
    all.add(recipe)
    return recipe
  }

  fun interface GeneratedRecipe {
    fun register(consumer: Consumer<FinishedRecipe>)
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
      return ResourceLocation(
				modId,
        recipeType + "/" + prefix + LibObjects.getKeyOrThrow(
          result.get()
            .asItem()
        ).path + suffix,
        
      )
    }


    fun registerGenerators(doRun: Boolean, gen: DataGenerator, vararg generators: KClass<out RecipeProvider>) {
      gen.addProvider(doRun, object : DataProvider {
        override fun getName(): String {
          return "Explore's Processing Recipes"
        }

        @Throws(IOException::class)
        override fun run(dc: CachedOutput): CompletableFuture<*> {
          for (generator in generators) {
            try {
              val generatorInstance = generator.java.getDeclaredConstructor(DataGenerator::class.java)
                .newInstance(gen)
              generatorInstance.run(dc)
            } catch (e: Exception) {
              e.printStackTrace()
            }
          }
          return CompletableFuture.completedFuture(null)
        }
      })
    }
  }

  // generator
  fun cooking(
    ingredient: Supplier<Ingredient>,
    result: Supplier<ItemLike>,
    builder: UnaryOperator<CookingRecipeBuilder> = UnaryOperator.identity()
  ): List<GeneratedRecipe> {
    val allCooking = CookingRecipeBuilder(modId, ingredient, result).apply(builder)
      .getRecipes()

    all.addAll(allCooking)
    return allCooking
  }

  fun crafting(
    result: Supplier<ItemLike>,
    amount: Int = 1,
    builder: UnaryOperator<StandardRecipeBuilder> = UnaryOperator.identity()
  ): List<GeneratedRecipe> {
    val allCrafting = StandardRecipeBuilder(modId, result, amount).apply(builder)
      .getRecipes()

    all.addAll(allCrafting)
    return allCrafting
  }


  // STANDARD RECIPE CLASS
  class StandardRecipeBuilder(private val modId: String, result: Supplier<ItemLike>, amount: Int) {
    private var result: Supplier<ItemLike>? = null
    private var amount: Int = 0
    private var unlockedBy: Supplier<ItemPredicate>? = null
    private var prefix: String = ""
    private var suffix: String = ""
    private val allRecipes: MutableList<GeneratedRecipe> = ArrayList()

    init {
      this.result = result
      this.amount = amount
    }

    fun apply(builder: UnaryOperator<StandardRecipeBuilder>): StandardRecipeBuilder {
      return builder.apply(this)
    }

    fun prefix(prefix: String): StandardRecipeBuilder {
      this.prefix = prefix
      return this
    }

    fun suffix(suffix: String): StandardRecipeBuilder {
      this.suffix = suffix
      return this
    }

    fun amount(amount: Int): StandardRecipeBuilder {
      this.amount = amount
      return this
    }

    fun unlockedBy(unlockedBy: Supplier<ItemPredicate>): StandardRecipeBuilder {
      this.unlockedBy = unlockedBy
      return this
    }

    // SHAPED
    fun shaped(
      builder: UnaryOperator<ShapedRecipeBuilder> = UnaryOperator.identity(),
    ): StandardRecipeBuilder {
      return shapedBase(amount, prefix, suffix, builder)
    }

    fun shaped(
      amount: Int,
      builder: UnaryOperator<ShapedRecipeBuilder> = UnaryOperator.identity(),
    ): StandardRecipeBuilder {
      return shapedBase(amount, prefix, suffix, builder)
    }

    fun shaped(
      amount: Int,
      prefix: String,
      suffix: String,
      builder: UnaryOperator<ShapedRecipeBuilder> = UnaryOperator.identity(),
    ): StandardRecipeBuilder {
      return shapedBase(amount, prefix, suffix, builder)
    }

    protected fun shapedBase(
      amount: Int,
      prefix: String,
      suffix: String,
      builder: UnaryOperator<ShapedRecipeBuilder>,
    ): StandardRecipeBuilder {
      val generatedRecipe = GeneratedRecipe { consumer: Consumer<FinishedRecipe> ->
        val builder = builder.apply(ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result?.get(), amount))

        if (unlockedBy != null) {
          builder.unlockedBy("has_item", inventoryTrigger(unlockedBy?.get()))
        }
        else {
          builder.unlockedBy("has_item",
            inventoryTrigger(ItemPredicate.Builder.item()
              .of(Items.AIR)
              .build()))
        }

        builder.save(
          { result -> consumer.accept(result) },
          createSimpleLocation(modId, "crafting_shaped", result!!, prefix, suffix)
        )
      }

      allRecipes.add(generatedRecipe)

      return this
    }

    // SHAPELESS
    fun shapeless(
      requiresList: List<Ingredient>,
      builder: UnaryOperator<ShapelessRecipeBuilder> = UnaryOperator.identity(),
    ): StandardRecipeBuilder {
      return shapelessBase(requiresList, amount, prefix, suffix, builder)
    }

    fun shapeless(
      amount: Int,
      requiresList: List<Ingredient>,
      builder: UnaryOperator<ShapelessRecipeBuilder> = UnaryOperator.identity(),
    ): StandardRecipeBuilder {
      return shapelessBase(requiresList, amount, prefix, suffix, builder)
    }

    fun shapeless(
      amount: Int,
      prefix: String,
      suffix: String,
      requiresList: List<Ingredient>,
      builder: UnaryOperator<ShapelessRecipeBuilder> = UnaryOperator.identity(),
    ): StandardRecipeBuilder {
      return shapelessBase(requiresList, amount, prefix, suffix, builder)
    }

    protected fun shapelessBase(
      requiresList: List<Ingredient>,
      amount: Int,
      prefix: String,
      suffix: String,
      builder: UnaryOperator<ShapelessRecipeBuilder> = UnaryOperator.identity(),
    ): StandardRecipeBuilder {
      val generatedRecipe = GeneratedRecipe { consumer: Consumer<FinishedRecipe> ->
        val builder = builder.apply(ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result?.get(), amount))

        requiresList.forEach { ingredient ->
          builder.requires(ingredient)
        }

        if (unlockedBy != null) {
          builder.unlockedBy("has_item", inventoryTrigger(unlockedBy?.get()))
        }
        else {
          builder.unlockedBy("has_item",
            inventoryTrigger(ItemPredicate.Builder.item()
              .of(Items.AIR)
              .build()))
        }

        builder.save(
          { result -> consumer.accept(result) },
          createSimpleLocation(modId, "crafting_shapeless", result!!, prefix, suffix)
        )
      }

      allRecipes.add(generatedRecipe)

      return this
    }

    fun getRecipes(): List<GeneratedRecipe> {
      return allRecipes
    }
  }


  // COOKING RECIPE CLASS
  class CookingRecipeBuilder(private val modId: String, ingredient: Supplier<Ingredient>, result: Supplier<ItemLike>) {
    private var ingredient: Supplier<Ingredient>? = null
    private var result: Supplier<ItemLike>? = null
    private var unlockedBy: Supplier<ItemPredicate>? = null
    private var prefix: String = ""
    private var suffix: String = ""
    private var exp: Float = 0f
    private var cookingTime: Int = 200
    private val COOKING_TIME_MODIFIER = 1.0f
    private val allRecipes: MutableList<GeneratedRecipe> = ArrayList()

    init {
      this.ingredient = ingredient
      this.result = result
    }

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
      createCookingRecipe("_smelting", SimpleCookingSerializer.SMELTING_RECIPE, cookingTime, exp) { b -> b }
      return this
    }

    fun inBlastFurnace(
      cookingTime: Int = this.cookingTime,
      exp: Float = this.exp,
    ): CookingRecipeBuilder {
      createCookingRecipe("_blasting", SimpleCookingSerializer.BLASTING_RECIPE, cookingTime, exp) { b -> b }
      return this
    }

    fun inSmoker(
      cookingTime: Int = this.cookingTime,
      exp: Float = this.exp,
    ): CookingRecipeBuilder {
      createCookingRecipe("_smoking", SimpleCookingSerializer.SMOKING_RECIPE, cookingTime, exp) { b -> b }
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
      ) { b -> b }
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
      transform: UnaryOperator<SimpleCookingRecipeBuilder>,
    ): GeneratedRecipe {
      val generatedRecipe = GeneratedRecipe { consumer: Consumer<FinishedRecipe> ->
        val builder = transform.apply(
          SimpleCookingRecipeBuilder.generic(
            ingredient?.get(),
            RecipeCategory.MISC,
            result?.get(),
            exp,
            (cookingTime * COOKING_TIME_MODIFIER).toInt(),
            serializer
          )
        )

        if (unlockedBy != null) {
          builder.unlockedBy("has_item", inventoryTrigger(unlockedBy?.get()))
        }
        else {
          builder.unlockedBy("has_item",
            inventoryTrigger(ItemPredicate.Builder.item()
              .of(Items.AIR)
              .build()))
        }

        builder.save(
          { result -> consumer.accept(result) },
          createSimpleLocation(
            modId,
            LibObjects.getKeyOrThrow(serializer).path,
            result!!,
            prefix,
            suffix + suffixRecipe
          )
        )
      }

      allRecipes.add(generatedRecipe)

      return generatedRecipe
    }

    fun getRecipes(): List<GeneratedRecipe> {
      return allRecipes
    }
  }
}