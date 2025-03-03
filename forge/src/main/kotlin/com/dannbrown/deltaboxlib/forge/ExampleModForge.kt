package com.dannbrown.deltaboxlib.forge

import dev.architectury.platform.forge.EventBuses
import com.dannbrown.deltaboxlib.ExampleMod
import net.minecraftforge.fml.common.Mod
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.MOD_CONTEXT

@Mod(ExampleMod.MOD_ID)
object ExampleModForge {
    init {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(ExampleMod.MOD_ID, MOD_BUS)
        ExampleMod.init()
    }
}