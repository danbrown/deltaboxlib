package com.dannbrown.databoxlib.compat.jei

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.compat.jei.cooling.CoolingCategory
import com.dannbrown.databoxlib.compat.jei.electrolyzer.ElectrolysisCategory
import com.dannbrown.databoxlib.compat.jei.pressure_chamber.PressureChamberCategory
import com.dannbrown.databoxlib.init.ProjectBlocks
import com.dannbrown.databoxlib.init.ProjectRecipeTypes
import com.dannbrown.databoxlib.lib.LibData
import com.dannbrown.databoxlib.lib.LibUtils
import com.simibubi.create.AllBlocks
import com.simibubi.create.compat.jei.CreateJEI
import com.simibubi.create.compat.jei.DoubleItemIcon
import com.simibubi.create.compat.jei.EmptyBackground
import com.simibubi.create.compat.jei.ItemIcon
import com.simibubi.create.compat.jei.category.CreateRecipeCategory
import com.simibubi.create.content.processing.basin.BasinRecipe
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo
import com.simibubi.create.foundation.utility.Lang
import com.simibubi.create.infrastructure.config.AllConfigs
import com.simibubi.create.infrastructure.config.CRecipes
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import mezz.jei.api.runtime.IIngredientManager
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.ItemLike
import org.jetbrains.annotations.NotNull
import java.util.List
import java.util.function.Consumer
import java.util.function.Predicate
import java.util.function.Supplier


@JeiPlugin
@Suppress("unused")
class JEIPlugin : IModPlugin {
  var ingredientManager: IIngredientManager? = null
  val ALL: MutableList<CreateRecipeCategory<*>> = ArrayList()
  @Suppress("unused")
  private fun loadCategories() {
    ALL.clear()

    val cooling: CreateRecipeCategory<*> = builder(BasinRecipe::class.java)
      .addTypedRecipes(ProjectRecipeTypes.COOLING)
      .catalyst(ProjectBlocks.MECHANICAL_COOLER::get)
      .catalyst(AllBlocks.BASIN::get)
      .doubleItemIcon(ProjectBlocks.MECHANICAL_COOLER.get(), AllBlocks.BASIN.get())
      .emptyBackground(177, 103)
      .build(LibData.RECIPES.COOLING) { i -> CoolingCategory(i) }

    val electrolysis: CreateRecipeCategory<*> = builder(BasinRecipe::class.java)
      .addTypedRecipes(ProjectRecipeTypes.ELECTROLYSIS)
      .catalyst(ProjectBlocks.KINETIC_ELECTROLYZER::get)
      .catalyst(AllBlocks.BASIN::get)
      .doubleItemIcon(ProjectBlocks.KINETIC_ELECTROLYZER.get(), AllBlocks.BASIN.get())
      .emptyBackground(177, 103)
      .build(LibData.RECIPES.ELECTROLYSIS) { i -> ElectrolysisCategory(i) }

    val pressure_chamber: CreateRecipeCategory<*> = builder(BasinRecipe::class.java)
      .addTypedRecipes(ProjectRecipeTypes.PRESSURE_CHAMBER)
      .catalyst(ProjectBlocks.PRESSURE_CHAMBER_VALVE::get)
      .catalyst(AllBlocks.BASIN::get)
      .doubleItemIcon(AllBlocks.BASIN.get(), ProjectBlocks.PRESSURE_CHAMBER_VALVE.get())
      .emptyBackground(177, 103)
      .build(LibData.RECIPES.PRESSURE_CHAMBER) { i -> PressureChamberCategory(i) }

  }

  @NotNull
  override fun getPluginUid(): ResourceLocation {
    return ResourceLocation(ProjectContent.MOD_ID, "jei_plugin")
  }

  override fun registerCategories(registration: IRecipeCategoryRegistration) {
    loadCategories()
    ALL.forEach(Consumer { recipeCategories: CreateRecipeCategory<*>? ->
      registration.addRecipeCategories(
        recipeCategories
      )
    })
  }

  override fun registerRecipes(registration: IRecipeRegistration) {
    ingredientManager = registration.ingredientManager
    ALL.forEach(Consumer { c: CreateRecipeCategory<*> ->
      c.registerRecipes(
        registration
      )
    })
  }

  override fun registerRecipeCatalysts(@NotNull registration: IRecipeCatalystRegistration?) {
    ALL.forEach(Consumer { c: CreateRecipeCategory<*> ->
      c.registerCatalysts(
        registration
      )
    })
  }

  private fun <T : Recipe<*>?> builder(recipeClass: Class<out T>): CategoryBuilder<T> {
    return CategoryBuilder(recipeClass)
  }

  private inner class CategoryBuilder<T : Recipe<*>?>(private val recipeClass: Class<out T>) {
    private val predicate: Predicate<CRecipes> = Predicate<CRecipes> { cRecipes -> true }
    private var background: IDrawable? = null
    private var icon: IDrawable? = null
    private val recipeListConsumers: MutableList<Consumer<List<T>>> = ArrayList<Consumer<List<T>>>()
    private val catalysts: MutableList<Supplier<out ItemStack>> = ArrayList<Supplier<out ItemStack>>()
    fun addRecipeListConsumer(consumer: Consumer<List<T>>): CategoryBuilder<T> {
      recipeListConsumers.add(consumer)
      return this
    }

    fun addTypedRecipes(recipeTypeEntry: IRecipeTypeInfo): CategoryBuilder<T> {
      return addTypedRecipes { recipeTypeEntry.getType() }
    }

    fun addTypedRecipes(recipeType: () -> net.minecraft.world.item.crafting.RecipeType<*>): CategoryBuilder<T> {
      return addRecipeListConsumer { recipes -> CreateJEI.consumeTypedRecipes<T>(recipes::add, recipeType()) }
    }

    fun catalystStack(supplier: Supplier<ItemStack>): CategoryBuilder<T> {
      catalysts.add(supplier)
      return this
    }

    fun catalyst(supplier: Supplier<ItemLike?>): CategoryBuilder<T> {
      return catalystStack { ItemStack(supplier.get()?.asItem()) }
    }


    fun icon(icon: IDrawable?): CategoryBuilder<T> {
      this.icon = icon
      return this
    }

    fun doubleItemIcon(item1: ItemLike?, item2: ItemLike?): CategoryBuilder<T> {
      icon(DoubleItemIcon(
        { ItemStack(item1) }
      ) { ItemStack(item2) })
      return this
    }


    fun itemIcon(item: ItemLike?): CategoryBuilder<T> {
      icon(ItemIcon { ItemStack(item) })
      return this
    }

    fun background(background: IDrawable?): CategoryBuilder<T> {
      this.background = background
      return this
    }

    fun emptyBackground(width: Int, height: Int): CategoryBuilder<T> {
      background(EmptyBackground(width, height))
      return this
    }

    fun build(name: String, factory: CreateRecipeCategory.Factory<T>): CreateRecipeCategory<T> {
      val recipesSupplier: Supplier<MutableList<T>>
      if (predicate.test(AllConfigs.server().recipes)) {
        recipesSupplier = Supplier<MutableList<T>> {
          val recipes: MutableList<T> = ArrayList()
          for (consumer in recipeListConsumers) consumer.accept(recipes as List<T>)
          recipes
        }
      } else {
        recipesSupplier = Supplier<MutableList<T>> { emptyList<Any>() as ArrayList<T>  }
      }
      val info = CreateRecipeCategory.Info<T>(
        RecipeType<T>(LibUtils.resourceLocation(name), recipeClass),
        Lang.translateDirect("recipe.$name"), background, icon, recipesSupplier, catalysts
      )
      val category = factory.create(info)
      ALL.add(category)
      return category
    }
  }
}

