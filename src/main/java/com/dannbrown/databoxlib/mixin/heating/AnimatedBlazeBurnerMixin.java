package com.dannbrown.databoxlib.mixin.heating;


import com.dannbrown.databoxlib.init.ProjectBlocks;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.compat.jei.category.animations.AnimatedBlazeBurner;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.block.render.SpriteShiftEntry;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = AnimatedBlazeBurner.class, remap = false)
public class AnimatedBlazeBurnerMixin extends AnimatedKinetics {

  @Shadow private BlazeBurnerBlock.HeatLevel heatLevel;

  /**
   * @author DannBrown & ZehMaria
   * @reason Adds HYPER and PASSIVE to the basin recipes, thanks to ZehMaria for how to implement new heat levels!
   */
  @Overwrite
  public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
    PoseStack matrixStack = guiGraphics.pose();
    matrixStack.pushPose();
    matrixStack.translate(xOffset, yOffset, 200);
    matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
    matrixStack.mulPose(Axis.YP.rotationDegrees(22.5f));
    int scale = 23;


    float offset = (Mth.sin(AnimationTickHolder.getRenderTime() / 16f) + 0.5f) / 16f;

    // is PASSIVE
    if (heatLevel == HeatLevel.valueOf("PASSIVE")) {
      blockElement(Blocks.FIRE.defaultBlockState()).atLocal(0, 1.65, 0)
              .scale(scale)
              .render(guiGraphics);

      blockElement(Blocks.FIRE.defaultBlockState()).atLocal(1, 1.8, 1)
              .rotate(0, 180, 0)
              .scale(scale)
              .render(guiGraphics);
    }

    // is HYPER
    else if(heatLevel == HeatLevel.valueOf("HYPER")) {
      blockElement(ProjectBlocks.INSTANCE.getHYPER_HEATER().getDefaultState()).atLocal(0, 1.65, 0)
              .scale(scale)
              .render(guiGraphics);

      blockElement(ProjectBlocks.INSTANCE.getHYPER_HEATER().getDefaultState()).atLocal(1, 1.8, 1)
              .rotate(0, 180, 0)
              .scale(scale)
              .render(guiGraphics);
    }

    // is HEATED or SUPERHEATED
    else {
      blockElement(AllBlocks.BLAZE_BURNER.getDefaultState()).atLocal(0, 1.65, 0)
              .scale(scale)
              .render(guiGraphics);

      PartialModel blaze = heatLevel == HeatLevel.SEETHING ? AllPartialModels.BLAZE_SUPER : AllPartialModels.BLAZE_ACTIVE;
      PartialModel rods2 = heatLevel == HeatLevel.SEETHING ? AllPartialModels.BLAZE_BURNER_SUPER_RODS_2 : AllPartialModels.BLAZE_BURNER_RODS_2;

      blockElement(blaze).atLocal(1, 1.8, 1)
              .rotate(0, 180, 0)
              .scale(scale)
              .render(guiGraphics);
      blockElement(rods2).atLocal(1, 1.7 + offset, 1)
              .rotate(0, 180, 0)
              .scale(scale)
              .render(guiGraphics);
    }

    matrixStack.scale(scale, -scale, scale);
    matrixStack.translate(0, -1.8, 0);

    SpriteShiftEntry spriteShift = heatLevel == HeatLevel.SEETHING ? AllSpriteShifts.SUPER_BURNER_FLAME : AllSpriteShifts.BURNER_FLAME;

      float spriteWidth = spriteShift.getTarget()
              .getU1()
              - spriteShift.getTarget()
              .getU0();

      float spriteHeight = spriteShift.getTarget()
              .getV1()
              - spriteShift.getTarget()
              .getV0();

      float time = AnimationTickHolder.getRenderTime(Minecraft.getInstance().level);
      float speed = 1 / 32f + 1 / 64f * heatLevel.ordinal();

      double vScroll = speed * time;
      vScroll = vScroll - Math.floor(vScroll);
      vScroll = vScroll * spriteHeight / 2;

      double uScroll = speed * time / 2;
      uScroll = uScroll - Math.floor(uScroll);
      uScroll = uScroll * spriteWidth / 2;

      Minecraft mc = Minecraft.getInstance();
      MultiBufferSource.BufferSource buffer = mc.renderBuffers()
              .bufferSource();
      VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());
      CachedBufferer.partial(AllPartialModels.BLAZE_BURNER_FLAME, Blocks.AIR.defaultBlockState())
              .shiftUVScrolling(spriteShift, (float) uScroll, (float) vScroll)
              .light(LightTexture.FULL_BRIGHT)
              .renderInto(matrixStack, vb);

      matrixStack.popPose();
  }

}