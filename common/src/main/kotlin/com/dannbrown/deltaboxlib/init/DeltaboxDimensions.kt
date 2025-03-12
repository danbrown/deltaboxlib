package com.dannbrown.deltaboxlib.init

import com.dannbrown.deltaboxlib.content.worldgen.dimension.DeepSpaceDimension
import com.dannbrown.deltaboxlib.init.DeltaboxLibMod.REGISTRATE

object DeltaboxDimensions {
  val DEEP_SPACE = REGISTRATE.dimension(DeepSpaceDimension)
  
  fun register() {
    // init
  }
}