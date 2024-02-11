package com.dannbrown.databoxlib.datagen.worldgen

import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.levelgen.DensityFunction
import net.minecraft.world.level.levelgen.DensityFunctions
import net.minecraft.world.level.levelgen.DensityFunctions.HolderHolder
import net.minecraft.world.level.levelgen.Noises
import net.minecraft.world.level.levelgen.synth.NormalNoise.NoiseParameters
import kotlin.math.abs

// The majority of vanilla density functions are  so most of this file are copied from the vanilla keys
object ProjectDensityFunctions {
  const val GLOBAL_OFFSET = -0.50375f
  const val ORE_THICKNESS = 0.08f
  const val VEININESS_FREQUENCY = 1.5
  const val NOODLE_SPACING_AND_STRAIGHTNESS = 1.5
  const val SURFACE_DENSITY_THRESHOLD = 1.5625
  const val CHEESE_NOISE_TARGET = -0.703125
  const val ISLAND_CHUNK_DISTANCE = 64
  const val ISLAND_CHUNK_DISTANCE_SQR = 4096L
  val BLENDING_FACTOR = DensityFunctions.constant(10.0)
  val BLENDING_JAGGEDNESS = DensityFunctions.zero()
  val ZERO = createKey("zero")
  val Y = createKey("y")
  val SHIFT_X = createKey("shift_x")
  val SHIFT_Z = createKey("shift_z")
  val BASE_3D_NOISE_OVERWORLD = createKey("overworld/base_3d_noise")
  val BASE_3D_NOISE_NETHER = createKey("nether/base_3d_noise")
  val BASE_3D_NOISE_END = createKey("end/base_3d_noise")
  val CONTINENTS = createKey("overworld/continents")
  val EROSION = createKey("overworld/erosion")
  val RIDGES = createKey("overworld/ridges")
  val RIDGES_FOLDED = createKey("overworld/ridges_folded")
  val OFFSET = createKey("overworld/offset")
  val FACTOR = createKey("overworld/factor")
  val JAGGEDNESS = createKey("overworld/jaggedness")
  val DEPTH = createKey("overworld/depth")
  val SLOPED_CHEESE = createKey("overworld/sloped_cheese")
  val CONTINENTS_LARGE = createKey("overworld_large_biomes/continents")
  val EROSION_LARGE = createKey("overworld_large_biomes/erosion")
  val OFFSET_LARGE = createKey("overworld_large_biomes/offset")
  val FACTOR_LARGE = createKey("overworld_large_biomes/factor")
  val JAGGEDNESS_LARGE = createKey("overworld_large_biomes/jaggedness")
  val DEPTH_LARGE = createKey("overworld_large_biomes/depth")
  val SLOPED_CHEESE_LARGE = createKey(
    "overworld_large_biomes/sloped_cheese"
  )
  val OFFSET_AMPLIFIED = createKey("overworld_amplified/offset")
  val FACTOR_AMPLIFIED = createKey("overworld_amplified/factor")
  val JAGGEDNESS_AMPLIFIED = createKey("overworld_amplified/jaggedness")
  val DEPTH_AMPLIFIED = createKey("overworld_amplified/depth")
  val SLOPED_CHEESE_AMPLIFIED = createKey(
    "overworld_amplified/sloped_cheese"
  )
  val SLOPED_CHEESE_END = createKey("end/sloped_cheese")
  val SPAGHETTI_ROUGHNESS_FUNCTION = createKey(
    "overworld/caves/spaghetti_roughness_function"
  )
  val ENTRANCES = createKey("overworld/caves/entrances")
  val NOODLE = createKey("overworld/caves/noodle")
  val PILLARS = createKey("overworld/caves/pillars")
  val SPAGHETTI_2D_THICKNESS_MODULATOR = createKey(
    "overworld/caves/spaghetti_2d_thickness_modulator"
  )
  val SPAGHETTI_2D = createKey("overworld/caves/spaghetti_2d")
  fun createKey(keyName: String): ResourceKey<DensityFunction> {
    return ResourceKey.create(Registries.DENSITY_FUNCTION, ResourceLocation("minecraft", keyName))
  }

  fun getFunction(
    densityFunctionHolderGetter: HolderGetter<DensityFunction>,
    resourceKey: ResourceKey<DensityFunction>
  ): DensityFunction {
    return HolderHolder(densityFunctionHolderGetter.getOrThrow(resourceKey))
  }

  fun slide(
    pDensityFunction: DensityFunction,
    pMinY: Int,
    pMaxY: Int,
    p_224447_: Int,
    p_224448_: Int,
    p_224449_: Double,
    p_224450_: Int,
    p_224451_: Int,
    p_224452_: Double
  ): DensityFunction {
    val densityfunction1 =
      DensityFunctions.yClampedGradient(pMinY + pMaxY - p_224447_, pMinY + pMaxY - p_224448_, 1.0, 0.0)
    val `$$9` = DensityFunctions.lerp(densityfunction1, p_224449_, pDensityFunction)
    val densityfunction2 = DensityFunctions.yClampedGradient(pMinY + p_224450_, pMinY + p_224451_, 0.0, 1.0)
    return DensityFunctions.lerp(densityfunction2, p_224452_, `$$9`)
  }

  fun slideOverworld(pAmplified: Boolean, pDensityFunction: DensityFunction): DensityFunction {
    return slide(
      pDensityFunction,
      -64,
      384,
      if (pAmplified) 16 else 80,
      if (pAmplified) 0 else 64,
      -0.078125,
      0,
      24,
      if (pAmplified) 0.4 else 0.1171875
    )
  }

  fun moonSurface(functions: HolderGetter<DensityFunction>): DensityFunction {
    return DensityFunctions.mul(
      DensityFunctions.constant(4.0),
      DensityFunctions.mul(
        DensityFunctions.mul(
          DensityFunctions.yClampedGradient(-64, 320, 1.5, -1.5),
          ProjectDensityFunctions.getFunction(functions, ProjectDensityFunctions.OFFSET)
        ).quarterNegative(),
        DensityFunctions.cache2d(
          ProjectDensityFunctions.getFunction(functions, ProjectDensityFunctions.FACTOR)
        )
      )
    ).squeeze()
  }

  fun underground(
    pDensityFunctions: HolderGetter<DensityFunction>,
    pNoiseParameters: HolderGetter<NoiseParameters>,
    p_256658_: DensityFunction
  ): DensityFunction {
    val densityfunction = ProjectDensityFunctions.getFunction(pDensityFunctions, ProjectDensityFunctions.SPAGHETTI_2D)
    val densityfunction1 = ProjectDensityFunctions.getFunction(pDensityFunctions, ProjectDensityFunctions.SPAGHETTI_ROUGHNESS_FUNCTION)
    val densityfunction2 = DensityFunctions.noise(pNoiseParameters.getOrThrow(Noises.CAVE_LAYER), 8.0)
    val densityfunction3 = DensityFunctions.mul(DensityFunctions.constant(4.0), densityfunction2.square())
    val densityfunction4 = DensityFunctions.noise(pNoiseParameters.getOrThrow(Noises.CAVE_CHEESE), 0.6666666666666666)
    val densityfunction5 = DensityFunctions.add(
      DensityFunctions.add(DensityFunctions.constant(0.27), densityfunction4).clamp(-1.0, 1.0),
      DensityFunctions.add(
        DensityFunctions.constant(1.5),
        DensityFunctions.mul(DensityFunctions.constant(-0.64), p_256658_)
      ).clamp(0.0, 0.5)
    )
    val densityfunction6 = DensityFunctions.add(densityfunction3, densityfunction5)
    val densityfunction7 = DensityFunctions.min(
      DensityFunctions.min(
        densityfunction6,
        ProjectDensityFunctions.getFunction(pDensityFunctions, ProjectDensityFunctions.ENTRANCES)
      ), DensityFunctions.add(densityfunction, densityfunction1)
    )
    val densityfunction8 = ProjectDensityFunctions.getFunction(pDensityFunctions, ProjectDensityFunctions.PILLARS)
    val densityfunction9 = DensityFunctions.rangeChoice(
      densityfunction8,
      -1000000.0,
      0.03,
      DensityFunctions.constant(-1000000.0),
      densityfunction8
    )
    return DensityFunctions.max(densityfunction7, densityfunction9)
  }

  fun yLimitedInterpolatable(
    p_209472_: DensityFunction,
    p_209473_: DensityFunction,
    p_209474_: Int,
    p_209475_: Int,
    p_209476_: Int
  ): DensityFunction {
    return DensityFunctions.interpolated(
      DensityFunctions.rangeChoice(
        p_209472_,
        p_209474_.toDouble(),
        (p_209475_ + 1).toDouble(),
        p_209473_,
        DensityFunctions.constant(p_209476_.toDouble())
      )
    )
  }

  fun postProcess(pDensityFunction: DensityFunction): DensityFunction {
    val densityfunction = DensityFunctions.blendDensity(pDensityFunction)
    return DensityFunctions.mul(DensityFunctions.interpolated(densityfunction), DensityFunctions.constant(0.64))
      .squeeze()
  }

  fun registerAndWrap(
    bootstapContext: BootstapContext<DensityFunction>,
    resourceKey: ResourceKey<DensityFunction>, densityFunction: DensityFunction
  ): DensityFunction {
    return HolderHolder(bootstapContext.register(resourceKey, densityFunction))
  }

  fun peaksAndValleys(densityFunction: DensityFunction): DensityFunction {
    return DensityFunctions.mul(
      DensityFunctions.add(
        DensityFunctions.add(densityFunction.abs(), DensityFunctions.constant(-0.6666666666666666)).abs(),
        DensityFunctions.constant(-0.3333333333333333)
      ), DensityFunctions.constant(-3.0)
    )
  }

  fun peaksAndValleys(p_224436_: Float): Float {
    return (-(abs((abs(p_224436_.toDouble()) - 0.6666667f).toDouble()) - 0.33333334f) * 3.0f).toFloat()
  }

  fun pillars(p_255985_: HolderGetter<NoiseParameters>): DensityFunction {
    val d0 = 25.0
    val d1 = 0.3
    val densityfunction = DensityFunctions.noise(p_255985_.getOrThrow(Noises.PILLAR), 25.0, 0.3)
    val densityfunction1 = DensityFunctions.mappedNoise(
      p_255985_.getOrThrow(Noises.PILLAR_RARENESS), 0.0,
      -2.0
    )
    val densityfunction2 = DensityFunctions.mappedNoise(
      p_255985_.getOrThrow(Noises.PILLAR_THICKNESS), 0.0,
      1.1
    )
    val densityfunction3 = DensityFunctions
      .add(DensityFunctions.mul(densityfunction, DensityFunctions.constant(2.0)), densityfunction1)
    return DensityFunctions.cacheOnce(DensityFunctions.mul(densityfunction3, densityfunction2.cube()))
  }

  fun noiseGradientDensity(pMinFunction: DensityFunction, pMaxFunction: DensityFunction): DensityFunction {
    val densityfunction = DensityFunctions.mul(pMaxFunction, pMinFunction)
    return DensityFunctions.mul(DensityFunctions.constant(4.0), densityfunction.quarterNegative())
  }

  fun bootstrap(context: BootstapContext<DensityFunction>) {
//    val holdergetter = context.lookup(Registries.NOISE)
//    val holdergetter1 = context.lookup(Registries.DENSITY_FUNCTION)
//    context.register(ZERO, DensityFunctions.zero())
//    val i = DimensionType.MIN_Y * 2
//    val j = DimensionType.MAX_Y * 2
//    context.register(Y, DensityFunctions.yClampedGradient(i, j, i.toDouble(), j.toDouble()))
//    val densityfunction = registerAndWrap(
//      context, SHIFT_X, DensityFunctions
//        .flatCache(DensityFunctions.cache2d(DensityFunctions.shiftA(holdergetter.getOrThrow(Noises.SHIFT))))
//    )
//    val densityfunction1 = registerAndWrap(
//      context, SHIFT_Z, DensityFunctions
//        .flatCache(DensityFunctions.cache2d(DensityFunctions.shiftB(holdergetter.getOrThrow(Noises.SHIFT))))
//    )
//
//    context.register(BASE_3D_NOISE_OVERWORLD, BlendedNoise.createUnseeded(0.25, 0.125, 80.0, 160.0, 8.0))
//    context.register(BASE_3D_NOISE_NETHER, BlendedNoise.createUnseeded(0.25, 0.375, 80.0, 60.0, 8.0))
//    context.register(BASE_3D_NOISE_END, BlendedNoise.createUnseeded(0.25, 0.25, 80.0, 160.0, 4.0))
//
//    val holder: Holder<DensityFunction> = context.register(
//      CONTINENTS, DensityFunctions.flatCache(
//        DensityFunctions
//          .shiftedNoise2d(densityfunction, densityfunction1, 0.25, holdergetter.getOrThrow(Noises.CONTINENTALNESS))
//      )
//    )
//    val holder1: Holder<DensityFunction> = context.register(
//      EROSION, DensityFunctions.flatCache(
//        DensityFunctions
//          .shiftedNoise2d(densityfunction, densityfunction1, 0.25, holdergetter.getOrThrow(Noises.EROSION))
//      )
//    )
//    val densityfunction2 = registerAndWrap(
//      context, RIDGES, DensityFunctions.flatCache(
//        DensityFunctions
//          .shiftedNoise2d(densityfunction, densityfunction1, 0.25, holdergetter.getOrThrow(Noises.RIDGE))
//      )
//    )
//    context.register(RIDGES_FOLDED, peaksAndValleys(densityfunction2))
//    return context.register(PILLARS, pillars(holdergetter))
  }
}