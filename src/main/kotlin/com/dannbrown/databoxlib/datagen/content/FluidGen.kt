package com.dannbrown.databoxlib.datagen.content

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.content.fluid.ProjectFluidType
import com.dannbrown.databoxlib.content.fluid.FluidPropertiesExtended
import com.dannbrown.databoxlib.content.fluid.FluidVariant
import com.dannbrown.databoxlib.content.fluid.SpecialFluid
import com.dannbrown.databoxlib.lib.LibUtils
import com.simibubi.create.AllFluids.TintedFluidType
import com.simibubi.create.AllTags
import com.simibubi.create.foundation.data.CreateRegistrate
import com.simibubi.create.foundation.utility.Color
import com.tterrag.registrate.builders.FluidBuilder
import com.tterrag.registrate.builders.FluidBuilder.FluidTypeFactory
import com.tterrag.registrate.util.entry.FluidEntry
import com.tterrag.registrate.util.nullness.NonNullFunction
import net.minecraft.client.renderer.RenderType
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.BlockAndTintGetter
import net.minecraft.world.level.material.FluidState
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.FluidType
import net.minecraftforge.fluids.ForgeFlowingFluid
import org.joml.Vector3f
import java.util.*
import java.util.function.Supplier

object FluidGen {
  //   fun createFluid_BASE2(
//    name: String,
//    fluidVariant: FluidVariant,
//    fogColor: Long,
//    fogDistance: Float,
//    hasAlpha: Boolean,
//    levelDecreasePerBlock: Int,
//    tickRate: Int,
//    slopeFindDistance: Int,
//    explosionResistance: Float = 100f,
//    fluidTypeProps: (FluidType.Properties) -> FluidType.Properties = { p: FluidType.Properties -> p },
//    fluidProps: (FluidPropertiesExtended) -> FluidPropertiesExtended = { p: FluidPropertiesExtended -> p }
//  ): FluidEntry<VirtualFluid> {
//     return DataboxContent.REGISTRATE.virtualFluid(
//       name,
//      fluidStillResourceLocation(fluidVariant),
//      fluidFlowResourceLocation(fluidVariant),
////       SolidRenderedPlaceableFluidType.create(fogColor.toInt(), null)
//     )
//       .tag(LibTags.forgeFluidTag(name))
//       .register()
//   }
  private fun createFluid_BASE(
    name: String,
    fluidVariant: FluidVariant,
    fogColor: Long,
    fogDistance: Float,
    hasAlpha: Boolean,
    levelDecreasePerBlock: Int,
    tickRate: Int,
    slopeFindDistance: Int,
    explosionResistance: Float = 100f,
    fluidTypeProps: (FluidType.Properties) -> FluidType.Properties = { p: FluidType.Properties -> p },
    fluidProps: (FluidPropertiesExtended) -> FluidPropertiesExtended = { p: FluidPropertiesExtended -> p },
    transformer: NonNullFunction<FluidBuilder<ForgeFlowingFluid, CreateRegistrate>, FluidBuilder<ForgeFlowingFluid, CreateRegistrate>>
  ): FluidBuilder<ForgeFlowingFluid, CreateRegistrate> {
    return ProjectContent.REGISTRATE
//      .fluid(name,
//      LibUtils.resourceLocation("fluid/" + "hydrogen" + "_still"),
//      LibUtils.resourceLocation("fluid/" + "hydrogen" + "_flow"),
////      fluidStillResourceLocation(fluidVariant),
////      fluidFlowResourceLocation(fluidVariant),
//      SolidRenderedPlaceableFluidType.create(fogColor.toInt(), null))
//      .tag(AllTags.forgeFluidTag(name))
//      .source { p -> ForgeFlowingFluid.Source(p) }
//      .properties { b ->
//        b
//          .viscosity(1500)
//          .density(1400)
//
//      }
//    .fluidProperties{p ->
//      p.levelDecreasePerBlock(2)
//      .tickRate(25)
//      .slopeFindDistance(3)
//      .explosionResistance(100f)
//
//
//      }
      .fluid<ForgeFlowingFluid>(
        name,
        fluidStillResourceLocation(fluidVariant),
        fluidFlowResourceLocation(fluidVariant),
        ProjectFluidType.create(fogColor, fogDistance, hasAlpha),
      )
      { p: ForgeFlowingFluid.Properties -> SpecialFluid.Flowing(p, fluidProps(FluidPropertiesExtended())) }
      .source { p: ForgeFlowingFluid.Properties -> SpecialFluid.Source(p, fluidProps(FluidPropertiesExtended())) }
      .properties { b: FluidType.Properties -> fluidTypeProps(b) }
      .fluidProperties { p: ForgeFlowingFluid.Properties ->
        p
          .levelDecreasePerBlock(levelDecreasePerBlock)
          .tickRate(tickRate)
          .slopeFindDistance(slopeFindDistance)
          .explosionResistance(explosionResistance)
      }
      .tag(AllTags.forgeFluidTag(name))
      .renderType { RenderType.translucent() }
      .transform(transformer)
  }

  fun createBucketFluid(
    name: String,
    fluidVariant: FluidVariant,
    fogColor: Long,
    fogDistance: Float,
    hasAlpha: Boolean,
    levelDecreasePerBlock: Int,
    tickRate: Int,
    slopeFindDistance: Int,
    explosionResistance: Float = 100f,
    fluidTypeProps: (FluidType.Properties) -> FluidType.Properties = { p: FluidType.Properties -> p },
    fluidProps: (FluidPropertiesExtended) -> FluidPropertiesExtended = { p: FluidPropertiesExtended -> p },
    transformer: NonNullFunction<FluidBuilder<ForgeFlowingFluid, CreateRegistrate>, FluidBuilder<ForgeFlowingFluid, CreateRegistrate>>
  ): FluidEntry<ForgeFlowingFluid> {
    return createFluid_BASE(
      name,
      fluidVariant,
      fogColor,
      fogDistance,
      hasAlpha,
      levelDecreasePerBlock,
      tickRate,
      slopeFindDistance,
      explosionResistance,
      fluidTypeProps,
      fluidProps,
      transformer
    )
      .bucket()
      .build()
      .register()
  }

  fun createTankFluid(
    name: String,
    fluidVariant: FluidVariant,
    fogColor: Long,
    fogDistance: Float,
    hasAlpha: Boolean,
    levelDecreasePerBlock: Int,
    tickRate: Int,
    slopeFindDistance: Int,
    explosionResistance: Float = 100f,
    fluidTypeProps: (FluidType.Properties) -> FluidType.Properties = { p: FluidType.Properties -> p },
    fluidProps: (FluidPropertiesExtended) -> FluidPropertiesExtended = { p: FluidPropertiesExtended -> p },
    transformer: NonNullFunction<FluidBuilder<ForgeFlowingFluid, CreateRegistrate>, FluidBuilder<ForgeFlowingFluid, CreateRegistrate>>
  ): FluidEntry<ForgeFlowingFluid> {
    return createFluid_BASE(
      name,
      fluidVariant,
      fogColor,
      fogDistance,
      hasAlpha,
      levelDecreasePerBlock,
      tickRate,
      slopeFindDistance,
      explosionResistance,
      fluidTypeProps,
      fluidProps,
      transformer
    )
      .bucket()
      .lang(formatTankLang(name))
      .build()
      .register()
  }

  // @ UTILS
  // Resource Locations solve
  private fun fluidStillResourceLocation(variant: FluidVariant): ResourceLocation {
    return LibUtils.resourceLocation("fluid/" + variant + "_still")
  }

  private fun fluidFlowResourceLocation(variant: FluidVariant): ResourceLocation {
    return LibUtils.resourceLocation("fluid/" + variant + "_flow")
  }

  private fun formatTankLang(id: String): String {
    val words = id.split("_")
    val formattedWords = words.map { it -> it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } }
    return formattedWords.joinToString(" ") + " Tank"
  }
}

private class SolidRenderedPlaceableFluidType private constructor(
  properties: Properties, stillTexture: ResourceLocation,
  flowingTexture: ResourceLocation
) : TintedFluidType(properties, stillTexture, flowingTexture) {
  private var fogColor: Vector3f? = null
  private var fogDistance: Supplier<Float>? = null
  override fun getTintColor(stack: FluidStack?): Int {
    return NO_TINT
  }

  /*
		 * Removing alpha from tint prevents optifine from forcibly applying biome
		 * colors to modded fluids (this workaround only works for fluids in the solid
		 * render layer)
		 */
  public override fun getTintColor(state: FluidState?, world: BlockAndTintGetter?, pos: BlockPos?): Int {
    return 0x00ffffff
  }

  override fun getCustomFogColor(): Vector3f? {
    return fogColor
  }

  override fun getFogDistanceModifier(): Float {
    return fogDistance!!.get()
  }

  companion object {
    fun create(fogColor: Int, fogDistance: Supplier<Float>?): FluidTypeFactory {
      return FluidTypeFactory { p: Properties, s: ResourceLocation, f: ResourceLocation ->
        val fluidType = SolidRenderedPlaceableFluidType(p, s, f)
        fluidType.fogColor = Color(fogColor, false).asVectorF()
        fluidType.fogDistance = fogDistance
        fluidType
      }
    }
  }
}

