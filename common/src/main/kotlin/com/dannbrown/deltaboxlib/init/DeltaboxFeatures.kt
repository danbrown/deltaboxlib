package com.dannbrown.deltaboxlib.init

import com.dannbrown.deltaboxlib.content.worldgen.configuration.WildCropConfiguration
import com.dannbrown.deltaboxlib.content.worldgen.feature.WildCropFeature
import com.dannbrown.deltaboxlib.init.DeltaboxLibMod.REGISTRATE

object DeltaboxFeatures {
  val WILD_CROP =
    REGISTRATE.feature<WildCropConfiguration>("wild_crop") { WildCropFeature(WildCropConfiguration.CODEC) }

  fun register() {
    // init
  }
}