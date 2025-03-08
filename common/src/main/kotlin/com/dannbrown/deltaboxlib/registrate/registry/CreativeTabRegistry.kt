package com.dannbrown.deltaboxlib.registrate.registry

import dev.architectury.registry.CreativeTabRegistry as ArchCreativeTabRegistry
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import java.util.function.Supplier

class CreativeTabRegistry(val modId: String) {
  private val creativeTabs = DeferredRegister.create(modId, Registries.CREATIVE_MODE_TAB)

  fun register(
    name: String,
    icon: Supplier<ItemStack>,
    displayItems: CreativeModeTab.DisplayItemsGenerator,
  ): RegistrySupplier<CreativeModeTab> {
    return creativeTabs.register(name) {
      ArchCreativeTabRegistry.create { builder ->
        builder
          .title(Component.translatable("itemGroup.${modId}.$name"))
          .icon(icon)
          .displayItems(displayItems)
          .build()
      }
    }
  }

  fun build() {
    creativeTabs.register()
  }
}

