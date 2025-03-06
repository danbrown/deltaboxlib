package com.dannbrown.deltaboxlib.fabric.init

import com.dannbrown.deltaboxlib.fabric.registrate.RegistrateInitFabric
import com.dannbrown.deltaboxlib.init.DeltaboxLibMod
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.api.ModInitializer


object DeltaboxLibModFabric : ModInitializer, ClientModInitializer {
  override fun onInitialize() {
    DeltaboxLibMod.init()
    RegistrateInitFabric(DeltaboxLibMod.REGISTRATE).init()
  }

  @Environment(EnvType.CLIENT)
  override fun onInitializeClient() {
    RegistrateInitFabric(DeltaboxLibMod.REGISTRATE).initClient()
  }
}
