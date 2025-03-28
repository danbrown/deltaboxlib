package com.dannbrown.deltaboxlib.init

import com.dannbrown.deltaboxlib.init.test.*

object DeltaboxLibMod {
  const val MOD_ID = "deltaboxlib"
  var REGISTRATE = DeltaboxRegistrate(MOD_ID)

  fun init() {
    DeltaboxConfig.register()
    DeltaboxBlocks.register()
    DeltaboxItems.register()
    DeltaboxTags.register()
    DeltaboxCreativeTabs.register()
    DeltaboxBlockEntities.register()
    DeltaboxEntityTypes.register()
    DeltaboxTrades.register()
    DeltaboxPlacerTypes.register()
    DeltaboxConfiguredFeatures.register()
    DeltaboxPlacedFeatures.register()
    DeltaboxBiomeModifiers.register()
    DeltaboxParticles.register()
    DeltaboxWoodTypes.register()
    DeltaboxModelLayers.register()
    DeltaboxSounds.register()
    DeltaboxBiomes.register()
    DeltaboxDimensions.register()
    DeltaboxAttributes.register()
    DeltaboxFeatures.register()
    DeltaboxAdvancements.register()
    DeltaboxMenus.register()
    REGISTRATE.buildRegistries()
  }
}