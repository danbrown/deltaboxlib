package com.dannbrown.deltaboxlib.fabric

import com.dannbrown.deltaboxlib.ExampleMod
import net.fabricmc.api.ModInitializer


object ExampleModFabric: ModInitializer {
    override fun onInitialize() {
        ExampleMod.init()
    }
}
