package com.dannbrown.arboria

import com.dannbrown.deltaboxlib.DeltaboxLib
import com.dannbrown.deltaboxlib.registry.DeltaboxRegistrate
import com.dannbrown.deltaboxlib.registry.generators.CreativeTabGen
import com.dannbrown.deltaboxlib.registry.generators.ItemGen
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.ItemStack
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.ModList
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.RegistryObject

object ArboriaContent {
  val MOD_ID = "arboria"
  val NAME = "Arboria Series"
  val modIds = mutableListOf<String>()

  val ITEMS = ItemGen(DeltaboxLib.REGISTRATE)
  val ICON = ITEMS.simpleItem("${MOD_ID}_icon")

  fun isArboriaInstalled(): Boolean {
    return ModList.get().mods.any { it.modId.startsWith(MOD_ID) } // all arboria mods start with "arboria"
  }

  // registrate
  val REGISTRATES = mutableListOf<DeltaboxRegistrate>()
  fun addRegistry(modId: String, registrate: DeltaboxRegistrate) {
    modIds.add(modId)
    REGISTRATES.add(registrate)
  }

  // creative tab
  val CREATIVE_TAB_KEY = "${MOD_ID}_tab"

  val TABS: DeferredRegister<CreativeModeTab> = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DeltaboxLib.MOD_ID)
  fun register(modBus: IEventBus) {
    TABS.register(modBus)
  }

  val MOD_TAB: RegistryObject<CreativeModeTab> =
    CreativeTabGen(TABS, DeltaboxLib.MOD_ID).createTab(
      CREATIVE_TAB_KEY,
      { ItemStack(ICON) },
      CreativeModeTabs.SPAWN_EGGS,
      { parameters, output ->
        REGISTRATES.forEach { CreativeTabGen.displayAll(it, parameters, output) }
      },
      NAME
    )
}