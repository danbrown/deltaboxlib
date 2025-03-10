package com.dannbrown.deltaboxlib.forge.init

import com.dannbrown.deltaboxlib.forge.registrate.RegistrateInitForge
import com.dannbrown.deltaboxlib.init.DeltaboxLibMod
import dev.architectury.platform.forge.EventBuses
import net.minecraftforge.client.event.RegisterColorHandlersEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import thedarkcolour.kotlinforforge.forge.DIST
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(DeltaboxLibMod.MOD_ID)
object DeltaboxLibModForge {
  val registrateInit = RegistrateInitForge(DeltaboxLibMod.REGISTRATE)

  init {
    val modBus = MOD_BUS
    val forgeEventBus = MinecraftForge.EVENT_BUS
    register(modBus, forgeEventBus)
    // client
    if (DIST.isClient) {
      // register main mod client content
      registerClient(modBus, forgeEventBus)
    }
  }

  // RUN SETUP
  private fun commonSetup(event: FMLCommonSetupEvent) {
    event.enqueueWork {
      registrateInit.setup()
    }
  }

  private fun register(modBus: IEventBus, forgeEventBus: IEventBus) {
    // Submit our event bus to let architectury register our content on the right time
    EventBuses.registerModEventBus(DeltaboxLibMod.MOD_ID, MOD_BUS)
    DeltaboxLibMod.init()
    registrateInit.init()

    MOD_BUS.addListener(::commonSetup)

    forgeEventBus.addListener(registrateInit::registerVillagerTrades)
    forgeEventBus.addListener(registrateInit::registerWandererTrades)
  }

  private fun registerClient(modBus: IEventBus, forgeEventBus: IEventBus) {
    modBus.addListener(registrateInit::registerBlockBiomeColors)
    modBus.addListener(registrateInit::registerItemBiomeColors)
  }
}