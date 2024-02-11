package com.dannbrown.databoxlib.content.fluid

import com.simibubi.create.AllFluids
import com.simibubi.create.foundation.utility.Color
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
) : AllFluids.TintedFluidType(properties, stillTexture, flowingTexture) {
  private var fogColor: Vector3f = Vector3f(0.0f, 0.0f, 0.0f)
  private var fogDistance: Float = 0.0f
  private var colorTint: Int = 0
  override fun getTintColor(stack: FluidStack): Int {
    return colorTint
  }

  public override fun getTintColor(state: FluidState, world: BlockAndTintGetter, pos: BlockPos): Int {
    return colorTint
  }
//  override fun getTintColor(stack: FluidStack?): Int {
//    return NO_TINT
//  }
//
//  /*
//		 * Removing alpha from tint prevents optifine from forcibly applying biome
//		 * colors to modded fluids (this workaround only works for fluids in the solid
//		 * render layer)
//		 */
//  public override fun getTintColor(state: FluidState?, world: BlockAndTintGetter?, pos: BlockPos?): Int {
//    return 0x00ffffff
//  }
  override fun getCustomFogColor(): Vector3f {
    return fogColor!!
  }

  override fun getFogDistanceModifier(): Float {
    return fogDistance
  }

  companion object {
    fun create(fogColor: Long, fogDistance: Float, hasAlpha: Boolean): FluidTypeFactory {
      return FluidTypeFactory { p: Properties, s: ResourceLocation, f: ResourceLocation ->
        val fluidType = ProjectFluidType(p, s, f)
        fluidType.fogColor = Color(fogColor.toInt(), true).asVectorF()
        fluidType.colorTint = fogColor.toInt()
        fluidType.fogDistance = fogDistance
        fluidType
      }
    }
  }
}