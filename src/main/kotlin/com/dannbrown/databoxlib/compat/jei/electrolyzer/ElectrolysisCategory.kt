package com.dannbrown.databoxlib.compat.jei.electrolyzer

import com.mojang.blaze3d.vertex.PoseStack
import com.simibubi.create.compat.jei.category.BasinCategory
import com.simibubi.create.compat.jei.category.animations.AnimatedBlazeBurner
import com.simibubi.create.content.processing.basin.BasinRecipe
import com.simibubi.create.content.processing.recipe.HeatCondition
import mezz.jei.api.gui.ingredient.IRecipeSlotsView
import net.minecraft.client.gui.GuiGraphics
import javax.annotation.ParametersAreNonnullByDefault


@ParametersAreNonnullByDefault
class ElectrolysisCategory(info: Info<BasinRecipe>) : BasinCategory(info, true) {
  private val electrolysis = AnimatedElectrolysis()
  private val heater = AnimatedBlazeBurner()

  override fun draw(
    recipe: BasinRecipe,
    iRecipeSlotsView: IRecipeSlotsView,
    guiGraphics: GuiGraphics,
    mouseX: Double,
    mouseY: Double
  ) {
    super.draw(recipe, iRecipeSlotsView, guiGraphics, mouseX, mouseY)

    val requiredHeat = recipe.requiredHeat
    if (requiredHeat != HeatCondition.NONE) heater.withHeat(requiredHeat.visualizeAsBlazeBurner())
      .draw(guiGraphics, getBackground().width / 2 + 3, 55)

    electrolysis.draw(guiGraphics, getBackground().width / 2 + 3, 34)
  }
}

