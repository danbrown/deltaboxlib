package com.dannbrown.databoxlib.content.core.renderer

import com.dannbrown.databoxlib.content.core.utils.SkyUtil
import com.dannbrown.databoxlib.datagen.planets.PlanetCodec
import com.dannbrown.databoxlib.datagen.planets.PlanetCodec.SkyObject
import com.dannbrown.databoxlib.datagen.planets.PlanetCodec.StarsRenderer
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.BufferBuilder.RenderedBuffer
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.Tesselator
import com.mojang.blaze3d.vertex.VertexBuffer
import net.minecraft.client.Camera
import net.minecraft.client.GraphicsStatus
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.FogRenderer
import net.minecraft.client.renderer.GameRenderer
import org.joml.Matrix4f
import org.joml.Vector3f

class ProjectSkyRenderer(skybox: PlanetCodec.SkyBoxProperties) {
  private val starsRenderer: StarsRenderer
  private val sunsetColor: PlanetCodec.ColorInstance
  private val skyObjects: List<SkyObject>

  //  private val horizonAngle: Int
  private val shouldRenderWhileRaining: Boolean
  private var starsBuffer: VertexBuffer? = null
  private var starsCount = 0

  init {
    starsRenderer = skybox.starsRenderer
    sunsetColor = skybox.sunsetColor
    skyObjects = skybox.skyObjects
//    horizonAngle = skyRenderer.horizonAngle()
    shouldRenderWhileRaining = skybox.weatherEffects != PlanetCodec.WeatherEffects.NONE
  }

  fun render(level: ClientLevel, ticks: Int, tickDelta: Float, poseStack: PoseStack, camera: Camera, projectionMatrix: Matrix4f, foggy: Boolean) {
    val bufferBuilder = Tesselator.getInstance().builder
    val minecraft = Minecraft.getInstance()
    if (shouldRenderWhileRaining && level.isRaining) {
      return
    }
    // Cancel rendering if the player is in fog, i.e. in lava or powdered snow
    if (SkyUtil.isSubmerged(camera)) {
      return
    }
    SkyUtil.preRender(level, minecraft.levelRenderer, camera, projectionMatrix, bufferBuilder, sunsetColor, poseStack, tickDelta)
    // Stars
    if (starsRenderer.fastStars > 0) {
      val stars: Int = if (minecraft.options.graphicsMode()
          .get() != GraphicsStatus.FAST) starsRenderer.fancyStars
      else starsRenderer.fastStars
      starsBuffer = renderStars(level, poseStack, tickDelta, bufferBuilder, stars, starsRenderer, projectionMatrix)
    }
    // Render all sky objects
    for (skyObject in skyObjects) {
      var scale: Float = skyObject.scale
      var rotation: Vector3f = skyObject.rotation
      when (skyObject.renderType) {
        PlanetCodec.RenderTypeProperty.STATIC -> {}
        PlanetCodec.RenderTypeProperty.DYNAMIC -> rotation = Vector3f(level.getTimeOfDay(tickDelta) * 360.0f + rotation.x(), rotation.y(), rotation.z())
        PlanetCodec.RenderTypeProperty.SCALING -> scale *= SkyUtil.scale
        PlanetCodec.RenderTypeProperty.DEBUG -> rotation = Vector3f(60f, 0f, 0f)
      }
      SkyUtil.render(poseStack, bufferBuilder, skyObject.texture, rotation, scale, skyObject.blending, skyObject.color)
    }
    SkyUtil.postRender(minecraft.gameRenderer, level, tickDelta)
  }

  private fun renderStars(level: ClientLevel, poseStack: PoseStack, tickDelta: Float, bufferBuilder: BufferBuilder, stars: Int, starsRenderer: StarsRenderer, projectionMatrix: Matrix4f): VertexBuffer {
    SkyUtil.startRendering(poseStack, Vector3f(-30.0f, 0.0f, level.getTimeOfDay(tickDelta) * 360.0f))
    RenderSystem.setShader { GameRenderer.getPositionColorShader() }
    createStarBuffer(bufferBuilder, stars)
    if (!starsRenderer.daylightVisible) {
      val rot = level.getStarBrightness(tickDelta)
      RenderSystem.setShaderColor(rot, rot, rot, rot)
    }
    else {
      RenderSystem.setShaderColor(0.8f, 0.8f, 0.8f, 0.8f)
    }
    FogRenderer.setupNoFog()
    starsBuffer!!.bind()
    starsBuffer!!.drawWithShader(poseStack.last()
      .pose(), projectionMatrix, GameRenderer.getPositionColorShader())
    VertexBuffer.unbind()
    poseStack.popPose()
    return starsBuffer as VertexBuffer
  }

  private fun createStarBuffer(bufferBuilder: BufferBuilder, stars: Int) {
    if (starsBuffer != null) {
      if (starsCount == stars) {
        return
      }
      starsBuffer!!.close()
    }
    starsBuffer = VertexBuffer(VertexBuffer.Usage.STATIC)
    starsCount = stars
    val renderedBuffer: RenderedBuffer = SkyUtil.renderStars(bufferBuilder, stars, starsRenderer.randomColors)
    starsBuffer!!.bind()
    starsBuffer!!.upload(renderedBuffer)
    VertexBuffer.unbind()
  }
}