package com.dannbrown.databoxlib.content.core.utils

import com.dannbrown.databoxlib.content.core.renderer.StarInformation
import com.dannbrown.databoxlib.datagen.planets.PlanetCodec
import com.dannbrown.databoxlib.mixin.render.LevelRendererAccessor
import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.BufferBuilder.RenderedBuffer
import com.mojang.blaze3d.vertex.BufferUploader
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexBuffer
import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.math.Axis
import net.minecraft.client.Camera
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.FogRenderer
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.LevelRenderer
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.material.FogType
import org.joml.Matrix4f
import org.joml.Vector3f
import java.awt.Color
import kotlin.math.atan2
import kotlin.math.max

object SkyUtil {
  val scale: Float
    // Scales the planet as you fall closer to it.
    get() {
      val minecraft = Minecraft.getInstance()
      val distance = (-3000.0f + minecraft.player!!.y * 4.5f).toFloat()
      val scale = 100 * (0.2f - distance / 10000.0f)
      return max(scale.toDouble(), 0.5)
        .toFloat()
    }

  fun preRender(level: ClientLevel, levelRenderer: LevelRenderer, camera: Camera, projectionMatrix: Matrix4f, bufferBuilder: BufferBuilder, sunsetColor: PlanetCodec.ColorInstance, poseStack: PoseStack, tickDelta: Float) {
    // Render colors.
    val vec3d = level.getSkyColor(camera.position, tickDelta)
    val f = vec3d.x()
      .toFloat()
    val g = vec3d.y()
      .toFloat()
    val h = vec3d.z()
      .toFloat()
    FogRenderer.levelFogColor()
    RenderSystem.depthMask(false)
    RenderSystem.setShaderColor(f, g, h, 1.0f)
    val skyBuffer: VertexBuffer = (levelRenderer as LevelRendererAccessor).getSkyBuffer()
    skyBuffer.bind()
    skyBuffer.drawWithShader(poseStack.last()
      .pose(), projectionMatrix, RenderSystem.getShader())
    VertexBuffer.unbind()
    RenderSystem.enableBlend()
    RenderSystem.defaultBlendFunc()
    renderColoring(sunsetColor, bufferBuilder, poseStack, level, tickDelta, level.getTimeOfDay(tickDelta), 90)
    RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO)
    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
  }

  fun postRender(renderer: GameRenderer, level: ClientLevel, tickDelta: Float) {
    val vec3d = level.getSkyColor(renderer.mainCamera.position, tickDelta)
    val f = vec3d.x()
      .toFloat()
    val g = vec3d.y()
      .toFloat()
    val h = vec3d.z()
      .toFloat()
    RenderSystem.setShaderColor(0.0f, 0.0f, 0.0f, 1.0f)
    if (level.effects()
        .hasGround()) {
      RenderSystem.setShaderColor(f * 0.2f + 0.04f, g * 0.2f + 0.04f, h * 0.6f + 0.1f, 1.0f)
    }
    else {
      RenderSystem.setShaderColor(f, g, h, 1.0f)
    }
    RenderSystem.depthMask(true)
    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
  }

  fun isSubmerged(camera: Camera): Boolean {
    val cameraSubmersionType = camera.fluidInCamera
    if (cameraSubmersionType == FogType.POWDER_SNOW || cameraSubmersionType == FogType.LAVA) {
      return true
    }
    return if (camera.entity is LivingEntity) {
      val livingEntity = camera.entity as LivingEntity
      livingEntity.hasEffect(MobEffects.BLINDNESS) || livingEntity.hasEffect(MobEffects.DARKNESS)
    }
    else false
  }

  fun startRendering(poseStack: PoseStack, rotation: Vector3f) {
    poseStack.pushPose()
    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
    // Rotation
    poseStack.mulPose(Axis.YP.rotationDegrees(rotation.y()))
    poseStack.mulPose(Axis.ZP.rotationDegrees(rotation.z()))
    poseStack.mulPose(Axis.XP.rotationDegrees(rotation.x()))
  }

  private fun endRendering(poseStack: PoseStack) {
    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
    RenderSystem.disableBlend()
    poseStack.popPose()
  }

  // For rendering textures in the sky
  fun render(poseStack: PoseStack, bufferBuilder: BufferBuilder, texture: ResourceLocation, rotation: Vector3f, scale: Float, blending: Boolean, color: PlanetCodec.ColorInstance) {
    startRendering(poseStack, rotation)
    RenderSystem.setShader { GameRenderer.getPositionTexColorShader() }
    if (blending) RenderSystem.enableBlend()
    else RenderSystem.disableBlend()
    val positionMatrix = poseStack.last()
      .pose()
    val overlayColor = Color(color.r, color.g, color.b, color.a)

    RenderSystem.setShaderTexture(0, texture)
    bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR)
    bufferBuilder.vertex(positionMatrix, -scale, 100.0f, -scale)
      .uv(1.0f, 0.0f)
      .color(overlayColor.red, overlayColor.green, overlayColor.blue, overlayColor.alpha)
      .endVertex()
    bufferBuilder.vertex(positionMatrix, scale, 100.0f, -scale)
      .uv(0.0f, 0.0f)
      .color(overlayColor.red, overlayColor.green, overlayColor.blue, overlayColor.alpha)
      .endVertex()
    bufferBuilder.vertex(positionMatrix, scale, 100.0f, scale)
      .uv(0.0f, 1.0f)
      .color(overlayColor.red, overlayColor.green, overlayColor.blue, overlayColor.alpha)
      .endVertex()
    bufferBuilder.vertex(positionMatrix, -scale, 100.0f, scale)
      .uv(1.0f, 1.0f)
      .color(overlayColor.red, overlayColor.green, overlayColor.blue, overlayColor.alpha)
      .endVertex()
    BufferUploader.drawWithShader(bufferBuilder.end())
    endRendering(poseStack)
  }

  fun renderStars(buffer: BufferBuilder, stars: Int, coloredStars: Boolean): RenderedBuffer {
    RenderSystem.setShader { GameRenderer.getPositionColorShader() }
    buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR)
    val info: StarInformation = StarInformation.STAR_CACHE.apply(0, stars)
    for (i in 0 until stars) {
      val vec3f: Vector3f = info.getParam1(i)
      var d = vec3f.x()
      var e = vec3f.y()
      var f = vec3f.z()
      val g: Float = info.getMultiplier(i)
      var h = d * d + e * e + f * f
      if (h >= 1 || h <= 0.01f) continue
      h = 1f / Mth.sqrt(h)
      d *= h
      e *= h
      f *= h
      val j = d * 100.0f
      val k = e * 100.0f
      val l = f * 100.0f
      val m = atan2(d.toDouble(), f.toDouble())
        .toFloat()
      val n = Mth.sin(m)
      val o = Mth.cos(m)
      val p = atan2(Mth.sqrt(d * d + f * f)
        .toDouble(), e.toDouble())
        .toFloat()
      val q = Mth.sin(p)
      val r = Mth.cos(p)
      val s: Float = info.getRandomPi(i)
      val t = Mth.sin(s)
      val u = Mth.cos(s)
      for (v in 0..3) {
        val x = ((v and 2) - 1) * g
        val y = ((v + 1 and 2) - 1) * g
        val aa = x * u - y * t
        val ac = y * u + x * t
        val ae = aa * -r
        val color: Color = if (coloredStars) info.getColor(i, 1, true) else Color(255, 255, 255, 255)
        buffer.vertex((j + ae * n - ac * o).toDouble(), (k + aa * q).toDouble(), (l + ac * n + ae * o).toDouble())
          .color(color.red, color.green, color.blue, color.alpha)
          .endVertex()
      }
    }
    return buffer.end()
  }

  fun renderColoring(sunsetColors: PlanetCodec.ColorInstance, bufferBuilder: BufferBuilder, poseStack: PoseStack, level: ClientLevel, tickDelta: Float, timeOfDay: Float, sunsetAngle: Int) {
    val colorAlpha = 0.5f
    val fogColor = Color(sunsetColors.r, sunsetColors.g, sunsetColors.b, sunsetColors.a)

    RenderSystem.setShader { GameRenderer.getPositionColorShader() }
    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
    poseStack.pushPose()
    poseStack.mulPose(Axis.XP.rotationDegrees(90.0f))
    val sine = if (Mth.sin(level.getSunAngle(tickDelta)) < 0f) 180f else 0f
    poseStack.mulPose(Axis.ZP.rotationDegrees(sine))
    poseStack.mulPose(Axis.ZP.rotationDegrees(90.0f))
    val matrix4f = poseStack.last()
      .pose()
    bufferBuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR)
    bufferBuilder.vertex(matrix4f, 0.0f, 100.0f, 0.0f)
      .color(fogColor.red.toFloat(), fogColor.green.toFloat(), fogColor.blue.toFloat(), colorAlpha)
      .endVertex()
    for (i in 0..16) {
      val o = i.toFloat() * Mth.TWO_PI / 16.0f
      val cosine = Mth.cos(o)
      bufferBuilder.vertex(matrix4f, Mth.sin(o) * 120.0f, cosine * 120.0f, -cosine * 40.0f * colorAlpha)
        .color(fogColor.red.toFloat(), fogColor.green.toFloat(), fogColor.blue.toFloat(), 0.0f)
        .endVertex()
    }
    BufferUploader.drawWithShader(bufferBuilder.end())
    poseStack.popPose()
  }
}