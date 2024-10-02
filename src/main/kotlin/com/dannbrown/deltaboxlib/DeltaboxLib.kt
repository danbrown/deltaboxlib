package com.dannbrown.deltaboxlib

import com.dannbrown.arboria.ArboriaContent
import com.dannbrown.deltaboxlib.registry.DeltaboxRegistrate
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.ModList
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import org.apache.logging.log4j.LogManager

@Mod(DeltaboxLib.MOD_ID)
class DeltaboxLib {
  companion object {
    const val MOD_ID = "deltaboxlib"
    const val NAME = "Deltabox Lib"
    val LOGGER = LogManager.getLogger()

    // mod registrate instance
    val REGISTRATE: DeltaboxRegistrate = DeltaboxRegistrate(MOD_ID)
    init {
      val eventBus = FMLJavaModLoadingContext.get().modEventBus
      val forgeEventBus = MinecraftForge.EVENT_BUS
      register(eventBus, forgeEventBus)
    }

    // init coupled addons (other addons should be loaded in their respective mods)
    private fun register(modBus: IEventBus, forgeEventBus: IEventBus) {
      LOGGER.info("$MOD_ID has started!")

      // register arboria if it is installed
      if(ArboriaContent.isArboriaInstalled()) {
        ArboriaContent.register(modBus)
      }

      // register all registrate event listeners
      REGISTRATE.registerEventListeners(modBus)
    }
  }
}



