package com.dannbrown.deltaboxlib.init

import net.minecraft.world.item.Item
import com.dannbrown.deltaboxlib.init.DeltaboxLibMod.REGISTRATE

object DeltaboxItems {
  val ADAMANTIUM_INGOT = REGISTRATE
    .item<Item>("adamantium_ingot")
    .register()

  fun register() {
    // init
  }
}