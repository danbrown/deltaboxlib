package com.dannbrown.deltaboxlib.forge.init

import com.dannbrown.deltaboxlib.init.DeltaboxLibMod
import dev.architectury.platform.forge.EventBuses
import net.minecraftforge.fml.common.Mod
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(DeltaboxLibMod.MOD_ID)
object DeltaboxLibModForge {
    init {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(DeltaboxLibMod.MOD_ID, MOD_BUS)
        DeltaboxLibMod.init()
    }
}