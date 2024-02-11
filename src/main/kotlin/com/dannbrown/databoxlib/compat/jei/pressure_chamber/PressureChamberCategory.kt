package com.dannbrown.databoxlib.compat.jei.pressure_chamber

//import com.simibubi.create.foundation.item.ItemHelper
//import mezz.jei.api.forge.ForgeTypes
//import com.simibubi.create.content.processing.burner.BlazeBurnerBlock
//import com.simibubi.create.AllBlocks
//import com.simibubi.create.AllItems
//import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
//import mezz.jei.api.recipe.IFocusGroup
//import mezz.jei.api.recipe.RecipeIngredientRole
//import net.minecraft.world.item.ItemStack
import com.mojang.blaze3d.vertex.PoseStack
import com.simibubi.create.AllBlocks
import com.simibubi.create.AllItems
import com.simibubi.create.compat.jei.category.BasinCategory
import com.simibubi.create.compat.jei.category.animations.AnimatedBlazeBurner
import com.simibubi.create.content.processing.basin.BasinRecipe
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel
import com.simibubi.create.content.processing.recipe.HeatCondition
import com.simibubi.create.foundation.gui.AllGuiTextures
import com.simibubi.create.foundation.item.ItemHelper
import com.simibubi.create.foundation.utility.Lang
import mezz.jei.api.forge.ForgeTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.ingredient.IRecipeSlotsView
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.world.item.ItemStack
import javax.annotation.ParametersAreNonnullByDefault


@ParametersAreNonnullByDefault
class PressureChamberCategory(info: Info<BasinRecipe>) : BasinCategory(info, true) {
  private val pressureChamber: AnimatedPressureChamber = AnimatedPressureChamber()
  private val heater = AnimatedBlazeBurner()


//  override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: BasinRecipe, focuses: IFocusGroup) {
//    val condensedIngredients = ItemHelper.condenseIngredients(recipe.ingredients)
//    var size = condensedIngredients.size + recipe.fluidIngredients.size
//    val xOffset = if (size < 3) (3 - size) * 19 / 2 else 0
//    var i = 0
//    for (pair in condensedIngredients) {
//      val stacks: MutableList<ItemStack> = ArrayList()
//      for (itemStack in pair.first.getItems()) {
//        val copy = itemStack.copy()
//        copy.setCount(pair.second.value)
//        stacks.add(copy)
//      }
//      builder
//        .addSlot(RecipeIngredientRole.INPUT, 17 + xOffset + i % 3 * 19, 41 - i / 3 * 19)
//        .setBackground(getRenderedSlot(), -1, -1)
//        .addItemStacks(stacks)
//      i++
//    }
//
//    for (fluidIngredient in recipe.fluidIngredients) {
//      builder
//        .addSlot(RecipeIngredientRole.INPUT, 17 + xOffset + i % 3 * 19, 41 - i / 3 * 19)
//        .setBackground(getRenderedSlot(), -1, -1)
//        .addIngredients(ForgeTypes.FLUID_STACK, withImprovedVisibility(fluidIngredient.getMatchingFluidStacks()))
//        .addTooltipCallback(addFluidTooltip(fluidIngredient.requiredAmount))
//      i++
//    }
//    size = recipe.rollableResults.size + recipe.fluidResults.size
//    i = 0
//    for (result in recipe.rollableResults) {
//      val xPosition = 142 - if (size % 2 != 0 && i == size - 1) 0 else if (i % 2 == 0) 10 else -9
//      val yPosition = -19 * (i / 2) + 51
//      builder
//        .addSlot(RecipeIngredientRole.OUTPUT, xPosition, yPosition)
//        .setBackground(getRenderedSlot(result), -1, -1)
//        .addItemStack(result.stack)
//        .addTooltipCallback(addStochasticTooltip(result))
//      i++
//    }
//    for (fluidResult in recipe.fluidResults) {
//      val xPosition = 142 - if (size % 2 != 0 && i == size - 1) 0 else if (i % 2 == 0) 10 else -9
//      val yPosition = -19 * (i / 2) + 51
//      builder
//        .addSlot(RecipeIngredientRole.OUTPUT, xPosition, yPosition)
//        .setBackground(getRenderedSlot(), -1, -1)
//        .addIngredient(ForgeTypes.FLUID_STACK, withImprovedVisibility(fluidResult))
//        .addTooltipCallback(addFluidTooltip(fluidResult.amount))
//      i++
//    }
//    val requiredHeat = recipe.requiredHeat
//    if (!requiredHeat.testBlazeBurner(BlazeBurnerBlock.HeatLevel.NONE)) {
//      builder
//        .addSlot(RecipeIngredientRole.RENDER_ONLY, 134, 81)
//        .addItemStack(AllBlocks.BLAZE_BURNER.asStack())
//    }
//    if (!requiredHeat.testBlazeBurner(BlazeBurnerBlock.HeatLevel.KINDLED)) {
//      builder
//        .addSlot(RecipeIngredientRole.CATALYST, 153, 81)
//        .addItemStack(AllItems.BLAZE_CAKE.asStack())
//    }
//  }


//  override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: BasinRecipe, focuses: IFocusGroup?) {
//    val condensedIngredients = ItemHelper.condenseIngredients(recipe.ingredients)
//    var size = condensedIngredients.size + recipe.fluidIngredients.size
//    val xOffset = if (size < 3) (3 - size) * 19 / 2 else 0
//    var i = 0
//    for (pair in condensedIngredients) {
//      val stacks: MutableList<ItemStack> = ArrayList()
//      for (itemStack in pair.first.getItems()) {
//        val copy = itemStack.copy()
//        copy.setCount(pair.second.value)
//        stacks.add(copy)
//      }
//      builder
//        .addSlot(RecipeIngredientRole.INPUT, 17 + xOffset + i % 3 * 19, 51 - i / 3 * 19)
//        .setBackground(getRenderedSlot(), -1, -1)
//        .addItemStacks(stacks)
//      i++
//    }
//    for (fluidIngredient in recipe.fluidIngredients) {
//      builder
//        .addSlot(RecipeIngredientRole.INPUT, 17 + xOffset + i % 3 * 19, 51 - i / 3 * 19)
//        .setBackground(getRenderedSlot(), -1, -1)
//        .addIngredients(ForgeTypes.FLUID_STACK, withImprovedVisibility(fluidIngredient.getMatchingFluidStacks()))
//        .addTooltipCallback(addFluidTooltip(fluidIngredient.requiredAmount))
//      i++
//    }
//    size = recipe.rollableResults.size + recipe.fluidResults.size
//    i = 0
//    for (result in recipe.rollableResults) {
//      val xPosition = 142 - if (size % 2 != 0 && i == size - 1) 0 else if (i % 2 == 0) 10 else -9
//      val yPosition = -19 * (i / 2) + 51
//      builder
//        .addSlot(RecipeIngredientRole.OUTPUT, xPosition, yPosition)
//        .setBackground(getRenderedSlot(result), -1, -1)
//        .addItemStack(result.stack)
//        .addTooltipCallback(addStochasticTooltip(result))
//      i++
//    }
//    for (fluidResult in recipe.fluidResults) {
//      val xPosition = 142 - if (size % 2 != 0 && i == size - 1) 0 else if (i % 2 == 0) 10 else -9
//      val yPosition = -19 * (i / 2) + 51
//      builder
//        .addSlot(RecipeIngredientRole.OUTPUT, xPosition, yPosition)
//        .setBackground(getRenderedSlot(), -1, -1)
//        .addIngredient(ForgeTypes.FLUID_STACK, withImprovedVisibility(fluidResult))
//        .addTooltipCallback(addFluidTooltip(fluidResult.amount))
//      i++
//    }
//    val requiredHeat = recipe.requiredHeat
//    if (!requiredHeat.testBlazeBurner(HeatLevel.NONE)) {
//      builder
//        .addSlot(RecipeIngredientRole.RENDER_ONLY, 134, 81)
//        .addItemStack(AllBlocks.BLAZE_BURNER.asStack())
//    }
//    if (!requiredHeat.testBlazeBurner(HeatLevel.KINDLED)) {
//      builder
//        .addSlot(RecipeIngredientRole.CATALYST, 153, 81)
//        .addItemStack(AllItems.BLAZE_CAKE.asStack())
//    }
//  }

  override fun draw(
    recipe: BasinRecipe,
    iRecipeSlotsView: IRecipeSlotsView,
    guiGraphics: GuiGraphics,
    mouseX: Double,
    mouseY: Double
  ) {
//    AllGuiTextures.JEI_DOWN_ARROW.render(matrixStack, 136, 30)
//    val requiredHeat = recipe.requiredHeat
//    val noHeat = requiredHeat == HeatCondition.NONE
//    val shadow = if (noHeat) AllGuiTextures.JEI_SHADOW else AllGuiTextures.JEI_LIGHT
//    shadow.render(matrixStack, 81, 58 + if (noHeat) 10 else 30)
//    pressureChamber.draw(matrixStack, 91, 47)
//    if (!noHeat) heater.withHeat(requiredHeat.visualizeAsBlazeBurner()).draw(matrixStack, 91, 55)
//    val heatBar = if (noHeat) AllGuiTextures.JEI_NO_HEAT_BAR else AllGuiTextures.JEI_HEAT_BAR
//    heatBar.render(matrixStack, 4, 80)
//    Minecraft.getInstance().font.draw(
//      matrixStack, Lang.translateDirect(requiredHeat.translationKey), 9f,
//      86f, requiredHeat.color
//    )

    super.draw(recipe, iRecipeSlotsView, guiGraphics, mouseX, mouseY)

    val requiredHeat = recipe.requiredHeat
    if (requiredHeat != HeatCondition.NONE) heater.withHeat(requiredHeat.visualizeAsBlazeBurner())
      .draw(guiGraphics, getBackground().width / 2 + 3, 55)

    pressureChamber.draw(guiGraphics, getBackground().width / 2 + 3, 34)
  }
}