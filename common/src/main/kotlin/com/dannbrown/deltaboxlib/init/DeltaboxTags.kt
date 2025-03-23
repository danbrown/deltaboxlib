package com.dannbrown.deltaboxlib.init

import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import net.minecraft.tags.BiomeTags
import net.minecraft.world.level.biome.Biomes
import com.dannbrown.deltaboxlib.init.DeltaboxLibMod.REGISTRATE


object DeltaboxTags {
//  val BIOME_TAGS = DeltaboxLibMod.REGISTRATE.biomeTags(BiomeTags.IS_OVERWORLD)
//    .add(Biomes.NETHER_WASTES)
//    .add(BiomeTags.IS_BADLANDS)
//    .register()

  object ITEM {
    val EXCLUDE_FROM_CREATIVE = REGISTRATE.comboItemTag("exclude_from_creative")
    fun register() {
      // init class
    }
  }


  fun register() {
    // init class
    ITEM.register()
  }
}