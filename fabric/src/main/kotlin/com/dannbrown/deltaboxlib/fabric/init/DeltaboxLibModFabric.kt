package com.dannbrown.deltaboxlib.fabric.init

import com.dannbrown.deltaboxlib.fabric.init.loaders.DeltaboxLibLoadTradesFabric
import com.dannbrown.deltaboxlib.fabric.registrate.RegistrateInitFabric
import com.dannbrown.deltaboxlib.init.DeltaboxLibMod
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.api.ModInitializer


object DeltaboxLibModFabric : ModInitializer, ClientModInitializer {
  val registrateInit = RegistrateInitFabric(DeltaboxLibMod.REGISTRATE)
  override fun onInitialize() {
    DeltaboxLibMod.init()
    registrateInit.init()

    // register datapack entries, like villager trades
    DeltaboxLibLoadTradesFabric.onDatapackReload(DeltaboxLibMod.REGISTRATE)
  }

  @Environment(EnvType.CLIENT)
  override fun onInitializeClient() {
    registrateInit.initClient()
  }
}
