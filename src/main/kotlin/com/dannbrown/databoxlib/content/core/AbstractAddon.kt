package com.dannbrown.databoxlib.content.core

import net.minecraftforge.event.server.ServerStartingEvent
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent

abstract class AbstractAddon {
  abstract fun initAddon()
  abstract fun registerAddon(modBus: IEventBus, forgeEventBus: IEventBus)
  abstract fun onServerStarting(event: ServerStartingEvent)
  abstract fun onClientSetup(event: FMLCommonSetupEvent)
}



