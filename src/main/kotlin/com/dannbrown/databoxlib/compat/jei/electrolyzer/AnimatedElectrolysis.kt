package com.dannbrown.databoxlib.compat.jei.electrolyzer

import com.dannbrown.databoxlib.init.ProjectBlocks
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import com.simibubi.create.AllBlocks
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.core.Direction

class AnimatedElectrolysis : AnimatedKinetics() {
  override fun draw(guiGraphics: GuiGraphics, xOffset: Int, yOffset: Int) {
    val matrixStack: PoseStack = guiGraphics.pose()
    matrixStack.pushPose()
    matrixStack.translate(xOffset.toDouble(), yOffset.toDouble(), 200.0)
    matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5f))
    matrixStack.mulPose(Axis.YP.rotationDegrees(22.5f))
    val scale = 23
    blockElement(shaft(Direction.Axis.Y))
      .rotateBlock(0.0, getCurrentAngle().toDouble(), 0.0)
      .scale(scale.toDouble())
      .render(guiGraphics)
    blockElement(ProjectBlocks.KINETIC_ELECTROLYZER.defaultState)
      .atLocal(0.0, 0.0, 0.0)
      .scale(scale.toDouble())
      .render(guiGraphics)
    blockElement(AllBlocks.BASIN.defaultState)
      .atLocal(0.0, 1.65, 0.0)
      .scale(scale.toDouble())
      .render(guiGraphics)
    matrixStack.popPose()
  }
}

