package com.dannbrown.databoxlib.content.core.renderer

import net.minecraft.util.Mth
import org.joml.Vector3f
import java.awt.Color
import java.util.*
import java.util.function.BiFunction

class StarInformation(seed: Long, stars: Int) {
  private val param1: Array<Vector3f>
  private val multiplier: FloatArray
  private val randomPi: FloatArray
  private val color: Array<Array<Color>>

  init {
    param1 = Array(stars) { Vector3f() }
    multiplier = FloatArray(stars)
    randomPi = FloatArray(stars)
    color = Array(stars) { Array(4) { Color(0, 0, 0, 0) } }
    val random = Random(seed)
    for (i in 0 until stars) {
      val d = random.nextFloat() * 2.0f - 1.0f
      val e = random.nextFloat() * 2.0f - 1.0f
      val f = random.nextFloat() * 2.0f - 1.0f
      param1[i] = Vector3f(d, e, f)
      multiplier[i] = 0.15f + random.nextFloat() * 0.01f
      randomPi[i] = random.nextFloat() * Mth.TWO_PI
      for (j in 0..3) {
        color[i][j] = STAR_COLORS[random.nextInt(STAR_COLORS.size)]
      }
    }
  }

  fun getParam1(i: Int): Vector3f {
    return param1[i]
  }

  fun getMultiplier(i: Int): Float {
    return multiplier[i]
  }

  fun getRandomPi(i: Int): Float {
    return randomPi[i]
  }

  fun getColor(i: Int, j: Int, colored: Boolean): Color {
    return if (colored) color[i][j] else BASE_COLOR
  }

  companion object {
    val BASE_COLOR: Color = Color(255, 255, 255, 255)
    val STAR_COLORS: Array<Color> = arrayOf<Color>(
      BASE_COLOR,
      Color(204, 238, 255, 255),
      Color(204, 153, 255, 255),
      Color(255, 255, 153, 255),
      Color(255, 204, 102, 255)
    )

    //    val STAR_CACHE: CacheableBiFunction<Long, Int, StarInformation> = CacheableBiFunction { seed: Long, stars: Int -> StarInformation(seed, stars) }
    val STAR_CACHE: BiFunction<Long, Int, StarInformation> = BiFunction { seed: Long, stars: Int -> StarInformation(seed, stars) }
  }
}

