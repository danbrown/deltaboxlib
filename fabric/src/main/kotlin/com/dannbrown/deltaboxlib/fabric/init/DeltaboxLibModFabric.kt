package com.dannbrown.deltaboxlib.fabric.init

import com.dannbrown.deltaboxlib.fabric.init.loaders.DeltaboxLibLoadTradesFabric
import com.dannbrown.deltaboxlib.fabric.registrate.RegistrateInitFabric
import com.dannbrown.deltaboxlib.init.DeltaboxLibMod
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.server.MinecraftServer


object DeltaboxLibModFabric : ModInitializer, ClientModInitializer {
  val registrateInit = RegistrateInitFabric(DeltaboxLibMod.REGISTRATE)
  var currentServer: MinecraftServer? = null
  override fun onInitialize() {
    DeltaboxLibMod.init()
    registrateInit.init()

    // register datapack entries, like villager trades
    DeltaboxLibLoadTradesFabric.onDatapackReload(DeltaboxLibMod.REGISTRATE)

    // add current server
    ServerLifecycleEvents.SERVER_STARTING.register({ s ->
      currentServer = s
    })
    ServerLifecycleEvents.SERVER_STOPPING.register({ s ->
      currentServer = null
    })
  }

  @Environment(EnvType.CLIENT)
  override fun onInitializeClient() {
    registrateInit.initClient()
  }
}
