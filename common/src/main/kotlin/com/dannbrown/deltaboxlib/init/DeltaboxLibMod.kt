package com.dannbrown.deltaboxlib.init

object DeltaboxLibMod {
  const val MOD_ID = "deltaboxlib"
  var REGISTRATE = DeltaboxRegistrate(MOD_ID)

  fun init() {
    DeltaboxBlocks.register()
    DeltaboxItems.register()
    DeltaboxTags.register()
    DeltaboxCreativeTabs.register()
    DeltaboxBlockEntities.register()
    DeltaboxTrades.register()
    DeltaboxPlacerTypes.register()
    DeltaboxConfiguredFeatures.register()
    DeltaboxPlacedFeatures.register()
    DeltaboxBiomeModifiers.register()
    DeltaboxParticles.register()
    DeltaboxWoodTypes.register()
    DeltaboxModelLayers.register()
    REGISTRATE.buildRegistries()
  }
}