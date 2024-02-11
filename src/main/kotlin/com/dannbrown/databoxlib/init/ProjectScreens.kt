package com.dannbrown.databoxlib.init

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.content.ship.block.sail.SailBlockScreenMenu
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.minecraftforge.common.extensions.IForgeMenuType
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.network.IContainerFactory
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ProjectScreens {
  private val SCREENS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, ProjectContent.MOD_ID)
  val SAIL_BLOCK = registerMenu<SailBlockScreenMenu>("sail_block", ::SailBlockScreenMenu)

  // Helper method to register a menu type
  private fun <T : AbstractContainerMenu> registerMenu(name: String, factory: IContainerFactory<T>): RegistryObject<MenuType<T>> {
    return SCREENS.register(name) { IForgeMenuType.create(factory) }
  }

  fun register(modBus: IEventBus) {
    SCREENS.register(modBus)
  }
}