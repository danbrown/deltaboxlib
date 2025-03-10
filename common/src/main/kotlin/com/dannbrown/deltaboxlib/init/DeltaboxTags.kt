package com.dannbrown.deltaboxlib.init

import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil

object DeltaboxTags {
//  val BIOME_TAGS = DeltaboxLibMod.REGISTRATE.biomeTags(BiomeTags.IS_OVERWORLD).add(Biomes.NETHER_WASTES)

  object ITEM {
    val EXCLUDE_FROM_CREATIVE = DeltaboxUtil.TAGS.deltaboxItemTag("exclude_from_creative")
  }


  fun register() {
    // init class
  }
}