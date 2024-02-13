package com.dannbrown.databoxlib.content.fluid

import com.dannbrown.databoxlib.registry.DataboxRegistrate
import com.mojang.blaze3d.shaders.FogShape
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Camera
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.FogRenderer
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions
import net.minecraftforge.fluids.FluidType
import org.jetbrains.annotations.NotNull
import org.joml.Vector3f
import java.util.function.Consumer

class GenericFluidType(textureType: FluidVariant, colorOverlay: Long, props: Properties, private val registrate: DataboxRegistrate) : FluidType(props) {
  val _colorOverlay: Int = colorOverlay.toInt()
  val _fogColor: Vector3f = Vector3f(((colorOverlay shr 16) and 0xFF) / 255f, ((colorOverlay shr 8) and 0xFF) / 255f, (colorOverlay and 0xFF) / 255f)
  val _textureType: FluidVariant = textureType

  override fun initializeClient(consumer: Consumer<IClientFluidTypeExtensions?>) {
    consumer.accept(object : IClientFluidTypeExtensions {
      override fun getStillTexture(): ResourceLocation {
        return ResourceLocation(registrate.modid,  "fluid/" + _textureType + "_still")
      }

      override fun getFlowingTexture(): ResourceLocation {
        return ResourceLocation(registrate.modid,  "fluid/" + _textureType + "_flow")
      }

      override fun getOverlayTexture(): ResourceLocation? {
        return ResourceLocation(registrate.modid,  "block/" + _textureType + "_overlay")
      }

      override fun getTintColor(): Int {
        return _colorOverlay
      }

      @NotNull
      override fun modifyFogColor(
        camera: Camera,
        partialTicks: Float,
        level: ClientLevel,
        renderDistance: Int,
        darkenWorldAmount: Float,
        fluidFogColor: Vector3f
      ): Vector3f {
        return _fogColor
      }

      override fun modifyFogRender(
        camera: Camera,
        mode: FogRenderer.FogMode,
        renderDistance: Float,
        partialTicks: Float,
        nearDistance: Float,
        farDistance: Float,
        shape: FogShape
      ) {
        RenderSystem.setShaderFogStart(0.0f)
        RenderSystem.setShaderFogEnd(3.0f)
      }
    })
  }
}