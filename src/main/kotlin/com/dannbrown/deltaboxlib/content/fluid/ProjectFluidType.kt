package com.dannbrown.deltaboxlib.content.fluid

import com.tterrag.registrate.builders.FluidBuilder.FluidTypeFactory
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.BlockAndTintGetter
import net.minecraft.world.level.material.FluidState
import net.minecraftforge.fluids.FluidStack
import org.joml.Vector3f

class ProjectFluidType private constructor(
  properties: Properties, stillTexture: ResourceLocation,
  flowingTexture: ResourceLocation
) : TintedFluidType(properties, stillTexture, flowingTexture) {
  private var fogColor: Vector3f = Vector3f(0.0f, 0.0f, 0.0f)
  private var fogDistance: Float = 0.0f
  private var colorTint: Int = 0
  override fun getTintColor(stack: FluidStack): Int {
    return colorTint
  }

  public override fun getTintColor(state: FluidState, getter: BlockAndTintGetter, pos: BlockPos): Int {
    return colorTint
  }



  companion object {
    fun create(fogColor: Long, fogDistance: Float, hasAlpha: Boolean): FluidTypeFactory {
      return FluidTypeFactory { p: Properties, s: ResourceLocation, f: ResourceLocation ->
        val fluidType = ProjectFluidType(p, s, f)
        fluidType.fogColor = Vector3f(((fogColor shr 16) and 0xFF) / 255f, ((fogColor shr 8) and 0xFF) / 255f, (fogColor and 0xFF) / 255f)
        fluidType.colorTint = fogColor.toInt()
        fluidType.fogDistance = fogDistance
        fluidType
      }
    }
  }
}