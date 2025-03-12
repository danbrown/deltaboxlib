package com.dannbrown.deltaboxlib.init


import com.dannbrown.deltaboxlib.content.worldgen.biome.SpaceVoidBiome
import com.dannbrown.deltaboxlib.init.DeltaboxLibMod.REGISTRATE

object DeltaboxBiomes {
  val SPACE_VOID = REGISTRATE.biome(SpaceVoidBiome)

  fun register() {
    // init
  }
}