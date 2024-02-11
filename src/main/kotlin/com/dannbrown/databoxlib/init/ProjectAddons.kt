package com.dannbrown.databoxlib.init

import com.dannbrown.databoxlib.content.core.AbstractAddon
import net.minecraftforge.eventbus.api.IEventBus

object ProjectAddons {
  val ADDONS: MutableList<AbstractAddon> = mutableListOf()
  fun register(addon: AbstractAddon) {
    ADDONS.add(addon)
  }

  fun init(modBus: IEventBus, forgeEventBus: IEventBus) {
    for (addon in ADDONS) {
      addon.registerAddon(modBus, forgeEventBus)
    }
  }
}