package com.dannbrown.databoxlib.compat.jei.pressure_chamber

import com.dannbrown.databoxlib.content.block.pressureChamberValve.PressureChamberCapBlock
import com.dannbrown.databoxlib.init.ProjectBlocks
import com.dannbrown.databoxlib.init.ProjectPartialModels
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import com.simibubi.create.AllBlocks
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics
import com.simibubi.create.foundation.gui.element.GuiGameElement
import net.minecraft.client.gui.GuiGraphics

class AnimatedPressureChamber : AnimatedKinetics() {
  override fun draw(guiGraphics: GuiGraphics, xOffset: Int, yOffset: Int) {
    val poseStack: PoseStack = guiGraphics.pose()
    poseStack.pushPose()
    poseStack.translate(xOffset.toDouble(), yOffset.toDouble(), 200.0)
    poseStack.mulPose(Axis.XP.rotationDegrees(-15.5f))
    poseStack.mulPose(Axis.YP.rotationDegrees(22.5f))
    val scale = 23
    val offsetY = 0.55
    GuiGameElement.of(ProjectBlocks.PRESSURE_CHAMBER_VALVE.defaultState.setValue(PressureChamberCapBlock.ON_A_BASIN, true))
      .atLocal(0.0, offsetY, 0.0)
      .scale(scale.toDouble())
      .render(guiGraphics)
    GuiGameElement.of(AllBlocks.BASIN.defaultState)
      .atLocal(0.0, 1 + offsetY, 0.0)
      .scale(scale.toDouble())
      .render(guiGraphics)
    blockElement(ProjectPartialModels.SMALL_GAUGE_DIAL).atLocal(0.5625, 0.375 + offsetY, 0.5625)
      .scale(scale.toDouble())
      .rotate(0.0, 0.0, (getCurrentAngle() / 4).toDouble())
      .render(guiGraphics)
    poseStack.popPose()
  }
}

