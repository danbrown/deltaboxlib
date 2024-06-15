package com.dannbrown.deltaboxlib.registry.generators

import com.dannbrown.deltaboxlib.content.fluid.ProjectFluidType
import com.dannbrown.deltaboxlib.content.fluid.FluidPropertiesExtended
import com.dannbrown.deltaboxlib.content.fluid.FluidVariant
import com.dannbrown.deltaboxlib.content.fluid.SpecialFluid
import com.dannbrown.deltaboxlib.lib.LibTags
import com.dannbrown.deltaboxlib.registry.DeltaboxRegistrate
import com.tterrag.registrate.builders.FluidBuilder
import com.tterrag.registrate.util.entry.FluidEntry
import com.tterrag.registrate.util.nullness.NonNullFunction
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.fluids.FluidType
import net.minecraftforge.fluids.ForgeFlowingFluid
import java.util.*

class FluidGen(private val registrate: DeltaboxRegistrate) {

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
    transformer: NonNullFunction<FluidBuilder<ForgeFlowingFluid, DeltaboxRegistrate>, FluidBuilder<ForgeFlowingFluid, DeltaboxRegistrate>>,
  ): FluidBuilder<ForgeFlowingFluid, DeltaboxRegistrate> {
    return registrate
      .fluid<ForgeFlowingFluid>(
        name,
        fluidStillResourceLocation(fluidVariant, registrate),
        fluidFlowResourceLocation(fluidVariant, registrate),
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
      .tag(LibTags.forgeFluidTag(name))
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
    transformer: NonNullFunction<FluidBuilder<ForgeFlowingFluid, DeltaboxRegistrate>, FluidBuilder<ForgeFlowingFluid, DeltaboxRegistrate>>
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
    transformer: NonNullFunction<FluidBuilder<ForgeFlowingFluid, DeltaboxRegistrate>, FluidBuilder<ForgeFlowingFluid, DeltaboxRegistrate>>
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
  private fun fluidStillResourceLocation(variant: FluidVariant, registrate: DeltaboxRegistrate): ResourceLocation {
    return ResourceLocation(registrate.modid, "fluid/" + variant + "_still")
  }

  private fun fluidFlowResourceLocation(variant: FluidVariant, registrate: DeltaboxRegistrate): ResourceLocation {
    return ResourceLocation(registrate.modid, "fluid/" + variant + "_flow")
  }

  private fun formatTankLang(id: String): String {
    val words = id.split("_")
    val formattedWords = words.map { it -> it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } }
    return formattedWords.joinToString(" ") + " Tank"
  }
}

