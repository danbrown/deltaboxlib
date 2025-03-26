package com.dannbrown.deltaboxlib.registrate.registry

import dev.architectury.registry.menu.MenuRegistry as ArchMenuRegistry
import dev.architectury.registry.menu.MenuRegistry.ExtendedMenuTypeFactory
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.core.registries.Registries
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType

class MenuRegistry(modId: String) {
  private val SCREENS = DeferredRegister.create(modId, Registries.MENU)

  fun <T : AbstractContainerMenu> registerMenu(
    name: String,
    factory: ExtendedMenuTypeFactory<T>
  ): RegistrySupplier<MenuType<T>> {
    return SCREENS.register(name) { ArchMenuRegistry.ofExtended(factory) }
  }

  fun build() {
    SCREENS.register()
  }
}