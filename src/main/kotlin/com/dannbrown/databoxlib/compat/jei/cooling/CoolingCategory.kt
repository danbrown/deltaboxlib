package com.dannbrown.databoxlib.compat.jei.cooling

import com.mojang.blaze3d.vertex.PoseStack
import com.simibubi.create.compat.jei.category.BasinCategory
import com.simibubi.create.compat.jei.category.animations.AnimatedBlazeBurner
import com.simibubi.create.content.processing.basin.BasinRecipe
import com.simibubi.create.content.processing.recipe.HeatCondition
import mezz.jei.api.gui.ingredient.IRecipeSlotsView
import net.minecraft.client.gui.GuiGraphics
import javax.annotation.ParametersAreNonnullByDefault


@ParametersAreNonnullByDefault
class CoolingCategory(info: Info<BasinRecipe?>?) : BasinCategory(info, true) {
  private val cooler = AnimatedCooler()
  private val heater = AnimatedBlazeBurner()

  override fun draw(
    recipe: BasinRecipe,
    recipeSlotsView: IRecipeSlotsView,
    graphics: GuiGraphics,
    mouseX: Double,
    mouseY: Double
  ) {
    super.draw(recipe, recipeSlotsView, graphics, mouseX, mouseY)
    val requiredHeat = recipe.requiredHeat
    if (requiredHeat != HeatCondition.NONE) heater
      .withHeat(requiredHeat.visualizeAsBlazeBurner())
      .draw(graphics, getBackground().width / 2 + 3, 55)
    cooler.draw(graphics, getBackground().width / 2 + 3, 34)
  }

//  override fun draw(
//    recipe: BasinRecipe,
//    iRecipeSlotsView: IRecipeSlotsView,
//    matrixStack: PoseStack,
//    mouseX: Double,
//    mouseY: Double
//  ) {
//    super.draw(recipe, iRecipeSlotsView, matrixStack, mouseX, mouseY)
//
//    val requiredHeat = recipe.requiredHeat
//    if (requiredHeat != HeatCondition.NONE) heater.withHeat(requiredHeat.visualizeAsBlazeBurner())
//      .draw(matrixStack, getBackground().width / 2 + 3, 55)
//
//    cooler.draw(matrixStack, getBackground().width / 2 + 3, 34)
//  }
}

