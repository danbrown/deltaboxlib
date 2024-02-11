package com.dannbrown.databoxlib.content.worldgen.featureConfiguration

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import java.util.*


enum class RadiusMatcher {
  XZ,
  NONE,
  ALL;

  companion object {
    val CODEC = Codec.STRING.comapFlatMap(
      { s: String ->
        try {
          val equipmentSlotType = valueOf(s.uppercase())
          DataResult.success(equipmentSlotType)
        } catch (var3: IllegalArgumentException) {
          throw IllegalArgumentException(
            String.format("\"%s\" is not a valid radius matcher. Valid radius matchers: %s",
              s,
              Arrays.stream(values())
                .map { obj: RadiusMatcher -> obj.toString() }
                .toArray().contentToString()
            )
          )
        }
      }
    ) { obj: RadiusMatcher -> obj.toString() }
  }
}