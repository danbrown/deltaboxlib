package com.dannbrown.databoxlib.content.fluid

import com.mojang.blaze3d.shaders.FogShape
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Camera
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.FogRenderer.FogMode
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.BlockAndTintGetter
import net.minecraft.world.level.material.FluidState
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.FluidType
import org.joml.Vector3f
import java.util.function.Consumer

abstract class TintedFluidType(properties: Properties, private val stillTexture: ResourceLocation, private val flowingTexture: ResourceLocation) : FluidType(properties) {
  override fun initializeClient(consumer: Consumer<IClientFluidTypeExtensions>) {
    consumer.accept(object : IClientFluidTypeExtensions {

      override fun getTintColor(stack: FluidStack): Int {
        return this@TintedFluidType.getTintColor(stack)
      }

      override fun getTintColor(state: FluidState, blockAndTintGetter: BlockAndTintGetter, pos: BlockPos): Int {
        return this@TintedFluidType.getTintColor(state, blockAndTintGetter, pos)
      }

      override fun modifyFogColor(camera: Camera, partialTick: Float, level: ClientLevel,
                                  renderDistance: Int, darkenWorldAmount: Float, fluidFogColor: Vector3f
      ): Vector3f {
        val customFogColor = this@TintedFluidType.customFogColor
        return customFogColor ?: fluidFogColor
      }

      override fun modifyFogRender(camera: Camera, mode: FogMode, renderDistance: Float, partialTick: Float,
                                   nearDistance: Float, farDistance: Float, shape: FogShape
      ) {
        val modifier = this@TintedFluidType.fogDistanceModifier
        val baseWaterFog = 96.0f
        if (modifier != 1f) {
          RenderSystem.setShaderFogShape(FogShape.CYLINDER)
          RenderSystem.setShaderFogStart(-8f)
          RenderSystem.setShaderFogEnd(baseWaterFog * modifier)
        }
      }
    })
  }

  protected abstract fun getTintColor(stack: FluidStack): Int
  protected abstract fun getTintColor(state: FluidState, getter: BlockAndTintGetter, pos: BlockPos): Int
  protected val customFogColor: Vector3f?
    get() = null
  protected val fogDistanceModifier: Float
    get() = 1f

  companion object {
    protected const val NO_TINT: Int = -0x1
  }
}