package com.dannbrown.databoxlib

import com.dannbrown.databoxlib.registry.DataboxRegistrate
import com.dannbrown.databoxlib.init.DataboxBlocks
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import org.apache.logging.log4j.LogManager

@Mod(DataboxLib.MOD_ID)
class DataboxLib {
  companion object {
    const val MOD_ID = "databoxlib"
    const val NAME = "Databox Lib"
    val LOGGER = LogManager.getLogger()

    // mod registrate instance
    val REGISTRATE: DataboxRegistrate = DataboxRegistrate(MOD_ID)

    init {
      val eventBus = FMLJavaModLoadingContext.get().modEventBus
      val forgeEventBus = MinecraftForge.EVENT_BUS
      register(eventBus, forgeEventBus)
    }

    // init coupled addons (other addons should be loaded in their respective mods)
    private fun register(modBus: IEventBus, forgeEventBus: IEventBus) {
      LOGGER.info("$MOD_ID has started!")
      // register content
      DataboxBlocks.register()
      // register all registrate event listeners
      REGISTRATE.registerEventListeners(modBus)
    }
  }
}



