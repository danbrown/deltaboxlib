package com.dannbrown.databoxlib.content.core.utils

import com.mojang.serialization.Codec
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

enum class BlendingFunction {
  EAST_IN_OUT_CIRC,
  EASE_OUT_BOUNCE,
  EASE_OUT_CUBIC,
  EASE_OUT_ELASTIC,
  EASE_IN_CIRC,
  EASE_OUT_QUINT;

  companion object {
    val CODEC: Codec<BlendingFunction> = CodecUtils.createEnumCodec(BlendingFunction::class.java)
    fun getFunction(blendingFunction: BlendingFunction): (Double) -> Double {
      return when (blendingFunction) {
        EAST_IN_OUT_CIRC -> ::easeInOutCirc
        EASE_OUT_BOUNCE -> ::easeOutBounce
        EASE_OUT_CUBIC -> ::easeOutCubic
        EASE_OUT_ELASTIC -> ::easeOutElastic
        EASE_IN_CIRC -> ::easeInCirc
        EASE_OUT_QUINT -> ::easeOutQuint
      }
    }

    fun applyFunction(blendingFunction: BlendingFunction, x: Double): Double {
      return when (blendingFunction) {
        EAST_IN_OUT_CIRC -> easeInOutCirc(x)
        EASE_OUT_BOUNCE -> easeOutBounce(x)
        EASE_OUT_CUBIC -> easeOutCubic(x)
        EASE_OUT_ELASTIC -> easeOutElastic(x)
        EASE_IN_CIRC -> easeInCirc(x)
        EASE_OUT_QUINT -> easeOutQuint(x)
      }
    }

    fun applyFunction(blendingFunction: BlendingFunction, factor: Double, min: Double, max: Double): Double {
      val range = max - min
      return min + range * applyFunction(blendingFunction, factor)
    }

    // Credits for the functions below to CorgiTaco: https://github.com/CorgiTaco/CorgiLib/blob/1.20.X/Common/src/main/java/corgitaco/corgilib/math/blendingfunction/BlendingFunctions.java
    private fun easeInOutCirc(x: Double): Double {
      return if (x < 0.5) (1.0 - sqrt(1.0 - (2.0 * x).pow(2.0))) / 2.0 else (sqrt(1.0 - (-2.0 * x + 2.0).pow(2.0)) + 1.0) / 2.0
    }

    private fun easeOutCubic(x: Double): Double {
      return 1.0 - (1.0 - x).pow(3.0)
    }

    private fun easeOutBounce(x: Double): Double {
      var x = x
      val n1 = 7.5625
      val d1 = 2.75
      return if (x < 1.0 / d1) {
        n1 * x * x
      }
      else if (x < 2.0 / d1) {
        n1 * (1.5 / d1).let { x -= it; x } * x + 0.75
      }
      else {
        if (x < 2.5 / d1) n1 * (2.25 / d1).let { x -= it; x } * x + 0.9375 else n1 * (2.625 / d1).let { x -= it; x } * x + 0.984375
      }
    }

    private fun easeOutElastic(x: Double): Double {
      val c4 = 2.0943951023931953
      return if (x == 0.0) 0.0 else if (x == 1.0) 1.0 else 2.0.pow(-10.0 * x) * sin((x * 10.0 - 0.75) * c4) + 1.0
    }

    private fun easeInCirc(x: Double): Double {
      return 1.0 - sqrt(1.0 - x.pow(2.0))
    }

    private fun easeOutQuint(x: Double): Double {
      return 1.0 - (1.0 - x).pow(5.0)
    }
  }
}