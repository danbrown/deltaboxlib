package com.dannbrown.deltaboxlib.fabric.init

import com.dannbrown.deltaboxlib.init.DeltaboxLibMod
import net.fabricmc.api.ModInitializer


object DeltaboxLibModFabric: ModInitializer {
    override fun onInitialize() {
        DeltaboxLibMod.init()
    }
}
