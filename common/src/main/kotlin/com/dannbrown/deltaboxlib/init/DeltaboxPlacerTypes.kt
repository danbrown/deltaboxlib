package com.dannbrown.deltaboxlib.init

import com.dannbrown.deltaboxlib.content.worldgen.decorator.DeltaVineDecorator
import com.dannbrown.deltaboxlib.content.worldgen.placerType.CrookedTrunkPlacer
import com.dannbrown.deltaboxlib.content.worldgen.placerType.PalmFoliagePlacer

object DeltaboxPlacerTypes {
  val CROOKED_TRUNK_PLACER = DeltaboxLibMod.REGISTRATE.trunkPlacer("crooked_trunk_placer") { CrookedTrunkPlacer.CODEC }
  val PALM_FOLIAGE_PLACER = DeltaboxLibMod.REGISTRATE.foliagePlacer("palm_foliage_placer") { PalmFoliagePlacer.CODEC }
  val VINE_DECORATOR = DeltaboxLibMod.REGISTRATE.treeDecorator("vine_placer") { DeltaVineDecorator.CODEC }

  fun register() {
    // init class
  }
}