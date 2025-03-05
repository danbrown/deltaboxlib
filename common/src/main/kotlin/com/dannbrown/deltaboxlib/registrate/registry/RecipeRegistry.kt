package com.dannbrown.deltaboxlib.registrate.registry

import com.dannbrown.deltaboxlib.registrate.types.RecipeFactory

class RecipeRegistry(modId: String) {
  private val RECIPE_FACTORIES: MutableList<RecipeFactory> = mutableListOf()

  fun addRecipe(factory: RecipeFactory) {
    RECIPE_FACTORIES.add(factory)
  }

  fun getRecipes(): List<RecipeFactory> {
    return RECIPE_FACTORIES
  }
}