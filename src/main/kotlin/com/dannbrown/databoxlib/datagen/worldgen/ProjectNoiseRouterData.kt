package com.dannbrown.databoxlib.datagen.worldgen

import net.minecraft.core.HolderGetter
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.dimension.DimensionType
import net.minecraft.world.level.levelgen.DensityFunction
import net.minecraft.world.level.levelgen.DensityFunctions.*
import net.minecraft.world.level.levelgen.NoiseRouter
import net.minecraft.world.level.levelgen.NoiseRouterData
import net.minecraft.world.level.levelgen.Noises
import net.minecraft.world.level.levelgen.synth.NormalNoise.NoiseParameters
import java.util.stream.Stream

object ProjectNoiseRouterData {

   fun overworld(
    pDensityFunctions: HolderGetter<DensityFunction>,
    pNoiseParameters: HolderGetter<NoiseParameters>,
    pLarge: Boolean,
    pAmplified: Boolean,
    pMoon: Boolean
  ): NoiseRouter {
    val densityfunction = noise(pNoiseParameters.getOrThrow(Noises.AQUIFER_BARRIER), 0.5)
    val densityfunction1 = noise(pNoiseParameters.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_FLOODEDNESS), 0.67)
    val densityfunction2 = noise(pNoiseParameters.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_SPREAD), 0.7142857142857143)
    val densityfunction3 = noise(pNoiseParameters.getOrThrow(Noises.AQUIFER_LAVA))
    val densityfunction4 = ProjectDensityFunctions.getFunction(pDensityFunctions, ProjectDensityFunctions.SHIFT_X)
    val densityfunction5 = ProjectDensityFunctions.getFunction(pDensityFunctions, ProjectDensityFunctions.SHIFT_Z)
    val densityfunction6 = shiftedNoise2d(
      densityfunction4,
      densityfunction5,
      0.25,
      pNoiseParameters.getOrThrow(if (pLarge) Noises.TEMPERATURE_LARGE else Noises.TEMPERATURE)
    )
    val densityfunction7 = shiftedNoise2d(
      densityfunction4,
      densityfunction5,
      0.25,
      pNoiseParameters.getOrThrow(if (pLarge) Noises.VEGETATION_LARGE else Noises.VEGETATION)
    )
    val densityfunction8 = ProjectDensityFunctions.getFunction(
      pDensityFunctions,
      if (pLarge) ProjectDensityFunctions.FACTOR_LARGE else if (pAmplified) ProjectDensityFunctions.FACTOR_AMPLIFIED else ProjectDensityFunctions.FACTOR
    )
    val densityfunction9 = ProjectDensityFunctions.getFunction(
      pDensityFunctions,
      if (pLarge) ProjectDensityFunctions.DEPTH_LARGE else if (pAmplified) ProjectDensityFunctions.DEPTH_AMPLIFIED else ProjectDensityFunctions.DEPTH
    )
    val densityfunction10 = ProjectDensityFunctions.noiseGradientDensity(cache2d(densityfunction8), densityfunction9)
    val densityfunction11 = ProjectDensityFunctions.getFunction(
      pDensityFunctions,
      if (pLarge) ProjectDensityFunctions.SLOPED_CHEESE_LARGE else if (pAmplified) ProjectDensityFunctions.SLOPED_CHEESE_AMPLIFIED else ProjectDensityFunctions.SLOPED_CHEESE
    )
    val densityfunction12 = min(
      densityfunction11,
      mul(constant(5.0), ProjectDensityFunctions.getFunction(pDensityFunctions, ProjectDensityFunctions.ENTRANCES))
    )
    val densityfunction13 = rangeChoice(
      densityfunction11,
      -1000000.0,
      1.5625,
      densityfunction12,
      ProjectDensityFunctions.underground(pDensityFunctions, pNoiseParameters, densityfunction11)
    )
    val densityfunction14 = min(
      ProjectDensityFunctions.postProcess(ProjectDensityFunctions.slideOverworld(pAmplified, densityfunction13)),
      ProjectDensityFunctions.getFunction(pDensityFunctions, ProjectDensityFunctions.NOODLE)
    )
    val densityfunction15 = ProjectDensityFunctions.getFunction(pDensityFunctions, ProjectDensityFunctions.Y)
    val i = Stream.of(*VeinType.values()).mapToInt { p_224495_ -> p_224495_.minY }
      .min().orElse(-DimensionType.MIN_Y * 2)
    val j = Stream.of(*VeinType.values()).mapToInt { p_224457_ -> p_224457_.maxY }
      .max().orElse(-DimensionType.MIN_Y * 2)
    val densityfunction16 = ProjectDensityFunctions.yLimitedInterpolatable(
      densityfunction15,
      noise(pNoiseParameters.getOrThrow(Noises.ORE_VEININESS), 1.5, 1.5),
      i,
      j,
      0
    )
    val f = 4.0f
    val densityfunction17 = ProjectDensityFunctions.yLimitedInterpolatable(
      densityfunction15,
      noise(pNoiseParameters.getOrThrow(Noises.ORE_VEIN_A), 4.0, 4.0),
      i,
      j,
      0
    ).abs()
    val densityfunction18 = ProjectDensityFunctions.yLimitedInterpolatable(
      densityfunction15,
      noise(pNoiseParameters.getOrThrow(Noises.ORE_VEIN_B), 4.0, 4.0),
      i,
      j,
      0
    ).abs()
    val densityfunction19 = add(constant(-0.08), max(densityfunction17, densityfunction18))
    val densityfunction20 = noise(pNoiseParameters.getOrThrow(Noises.ORE_GAP))
    return NoiseRouter(
      densityfunction,
      densityfunction1,
      densityfunction2,
      densityfunction3,
      densityfunction6,
      densityfunction7,
      ProjectDensityFunctions.getFunction(
        pDensityFunctions,
        if (pLarge) NoiseRouterData.CONTINENTS_LARGE else NoiseRouterData.CONTINENTS
      ),
      ProjectDensityFunctions.getFunction(
        pDensityFunctions,
        if (pLarge) NoiseRouterData.EROSION_LARGE else NoiseRouterData.EROSION
      ),
      densityfunction9,
      ProjectDensityFunctions.getFunction(pDensityFunctions, NoiseRouterData.RIDGES),
      if(pMoon) ProjectDensityFunctions.moonSurface(pDensityFunctions)
      else ProjectDensityFunctions.slideOverworld(pAmplified, add(densityfunction10, constant(-0.703125)).clamp(-64.0, 64.0)),
      densityfunction14,
      densityfunction16,
      densityfunction19,
      densityfunction20
    )
  }


  enum class VeinType(
    val ore: BlockState,
    val rawOreBlock: BlockState,
    val filler: BlockState,
    val minY: Int,
    val maxY: Int
  ) {
    COPPER(
      Blocks.COPPER_ORE.defaultBlockState(),
      Blocks.RAW_COPPER_BLOCK.defaultBlockState(),
      Blocks.GRANITE.defaultBlockState(),
      0,
      50
    ),
    IRON(
      Blocks.DEEPSLATE_IRON_ORE.defaultBlockState(),
      Blocks.RAW_IRON_BLOCK.defaultBlockState(),
      Blocks.TUFF.defaultBlockState(),
      -60,
      -8
    )
  }


}