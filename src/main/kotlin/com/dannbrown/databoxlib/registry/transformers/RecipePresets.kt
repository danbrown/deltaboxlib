package com.dannbrown.databoxlib.registry.transformers

import net.minecraft.resources.ResourceLocation
import com.tterrag.registrate.providers.DataGenContext
import com.tterrag.registrate.providers.RegistrateRecipeProvider
import com.tterrag.registrate.util.DataIngredient
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraftforge.common.Tags
import java.util.function.Supplier

object RecipePresets {
  fun <B : Block> polishedCraftingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>, amount: Int = 1
  ) {
    val recipePrefix = c.name // ProjectContent.MOD_ID + ":" + c.name

    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), amount)
      .define('X', ingredient.get())
      .pattern("XX")
      .pattern("XX")
      .unlockedBy("has_ingredient",
        ingredient.get()
          .getCritereon(p))
      .save({ t: FinishedRecipe -> p.accept(t) }, recipePrefix + "_from_block")
  }

  fun <B : Block> simpleStonecuttingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>, amount: Int = 1
  ) {
    p.stonecutting(ingredient.get(), RecipeCategory.BUILDING_BLOCKS, { c.get() }, amount)
  }

  fun <B : Block> stairsCraftingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
  ) {
    p.stairs(ingredient.get(), RecipeCategory.BUILDING_BLOCKS, { c.get() }, c.name, false)
  }

  fun <B : Block> stairsStonecuttingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
  ) {
    simpleStonecuttingRecipe(c, p, ingredient, 1)
  }

  fun <B : Block> slabCraftingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
  ) {
    p.slab(ingredient.get(), RecipeCategory.BUILDING_BLOCKS, { c.get() }, c.name, false)
  }

  fun <B : Block> fenceCraftingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
  ) {
    p.fence(ingredient.get(), RecipeCategory.BUILDING_BLOCKS, { c.get() }, c.name)
  }

  fun <B : Block> fenceGateCraftingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
  ) {
    p.fenceGate(ingredient.get(), RecipeCategory.BUILDING_BLOCKS, { c.get() }, c.name)
  }

  fun <B : Block> pressurePlateCraftingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
  ) {
    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 1)
      .define('X', ingredient.get())
      .pattern("XX")
      .unlockedBy("has_" + p.safeName(ingredient.get()),
        ingredient.get()
          .getCritereon(p))
      .save(p, p.safeId(c.get()))
  }

  fun <B : Block> directShapelessRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>, amount: Int = 1
  ) {
    ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, c.get(), amount)
      .requires(ingredient.get())
      .unlockedBy("has_" + p.safeName(ingredient.get()),
        ingredient.get()
          .getCritereon(p))
      .save(p, ResourceLocation("crafting/" + c.name + "_from_" + p.safeName(ingredient.get())))
  }

  fun <B : Block> directConversionRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>, result: Supplier<ItemLike>, amount: Int = 1
  ) {
    ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, result.get(), amount)
      .requires(ingredient.get())
      .unlockedBy("has_" + p.safeName(ingredient.get()),
        ingredient.get()
          .getCritereon(p))
      .save(p, ResourceLocation("crafting/" + p.safeName(result.get()) + "_from_" + p.safeName(ingredient.get())))
  }

  fun <B : Block> slabStonecuttingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
  ) {
    simpleStonecuttingRecipe(c, p, ingredient, 2)
  }

  fun <B : Block> slabRecycleRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<ItemLike>
  ) {
    val asIngredient = DataIngredient.items(c.get())
    ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ingredient.get())
      .requires(asIngredient)
      .requires(asIngredient)
      .unlockedBy("has_" + c.name, asIngredient.getCritereon(p))
      .save(p, c.name + "_recycling") // ProjectContent.MOD_ID + ":" + c.name
  }

  fun <B : Block> slabToChiseledRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
  ) {
    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 1)
      .define('X', ingredient.get())
      .pattern("X")
      .pattern("X")
      .unlockedBy("has_" + p.safeName(ingredient.get()),
        ingredient.get()
          .getCritereon(p))
      .save(p, p.safeId(c.get()))
  }

  fun <B : Block> signCraftingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
  ) {
    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 3)
      .define('X', ingredient.get())
      .define('S', Tags.Items.RODS_WOODEN)
      .pattern("XXX")
      .pattern("XXX")
      .pattern(" S ")
      .unlockedBy("has_" + p.safeName(ingredient.get()),
        ingredient.get()
          .getCritereon(p))
      .save(p, p.safeId(c.get()))
  }

  fun <B : Block> wallCraftingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
  ) {
    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 6)
      .define('X', ingredient.get())
      .pattern("XXX")
      .pattern("XXX")
      .unlockedBy("has_" + p.safeName(ingredient.get()),
        ingredient.get()
          .getCritereon(p))
      .save(p, p.safeId(c.get()))
  }

  fun <B : Block> wallStonecuttingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
  ) {
    simpleStonecuttingRecipe(c, p, ingredient, 1)
  }

  fun <B : Block> trapdoorStonecuttingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
  ) {
    simpleStonecuttingRecipe(c, p, ingredient, 1)
  }

  fun <B : Block> trapdoorCraftingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
  ) {
    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 2)
      .pattern("XXX")
      .pattern("XXX")
      .define('X', ingredient.get())
      .group("trapdoors")
      .unlockedBy("has_" + p.safeName(ingredient.get()),
        ingredient.get()
          .getCritereon(p))
      .save(p, p.safeId(c.get()))
  }

  fun <B : Block> doorCraftingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
  ) {
    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 3)
      .pattern("XX")
      .pattern("XX")
      .pattern("XX")
      .define('X', ingredient.get())
      .group("doors")
      .unlockedBy("has_" + p.safeName(ingredient.get()),
        ingredient.get()
          .getCritereon(p))
      .save(p, p.safeId(c.get()))
  }

  fun <B : Block> barsStonecuttingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
  ) {
    simpleStonecuttingRecipe(c, p, ingredient, 4)
  }

  fun <B : Block> scaffoldingStonecuttingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
  ) {
    simpleStonecuttingRecipe(c, p, ingredient, 2)
  }

  fun <B : Block> storageBlockRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingotItem: Supplier<ItemLike>, ingredient: Supplier<DataIngredient>
  ) {
    val recipePrefix =  c.name  // ProjectContent.MOD_ID + ":" + c.name

    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 1)
      .define('I', ingredient.get())
      .pattern("III")
      .pattern("III")
      .pattern("III")
      .unlockedBy("has_ingredient",
        ingredient.get()
          .getCritereon(p))
      .save({ t: FinishedRecipe -> p.accept(t) }, recipePrefix + "_from_materials")

    ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ingotItem.get(), 9)
      .requires(c.get())
      .unlockedBy("has_ingredient",
        ingredient.get()
          .getCritereon(p))
      .save({ t: FinishedRecipe -> p.accept(t) }, recipePrefix + "_to_materials")
  }

  fun <B : Block> smallStorageBlockRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingotItem: Supplier<ItemLike>, ingredient: Supplier<DataIngredient>
  ) {
    val recipePrefix =  c.name // ProjectContent.MOD_ID + ":" + c.name

    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 1)
      .define('I', ingredient.get())
      .pattern("II")
      .pattern("II")
      .unlockedBy("has_ingredient",
        ingredient.get()
          .getCritereon(p))
      .save({ t: FinishedRecipe -> p.accept(t) }, recipePrefix + "_from_materials")

    ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ingotItem.get(), 4)
      .requires(c.get())
      .unlockedBy("has_ingredient",
        ingredient.get()
          .getCritereon(p))
      .save({ t: FinishedRecipe -> p.accept(t) }, recipePrefix + "_to_materials")
  }

  fun <B : Block> ladderStonecuttingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
  ) {
    simpleStonecuttingRecipe(c, p, ingredient, 2)
  }
}