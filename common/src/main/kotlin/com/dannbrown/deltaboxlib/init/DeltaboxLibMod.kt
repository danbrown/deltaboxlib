package com.dannbrown.deltaboxlib.init

object DeltaboxLibMod {
  const val MOD_ID = "deltaboxlib"
  var REGISTRATE = DeltaboxRegistrate(MOD_ID)

  fun init() {
    DeltaboxBlocks.register()
    DeltaboxItems.register()
    DeltaboxCreativeTabs.register()
    DeltaboxTrades.register()
    REGISTRATE.buildRegistries()
  }
}