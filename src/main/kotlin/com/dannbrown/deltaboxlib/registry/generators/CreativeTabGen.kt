package com.dannbrown.deltaboxlib.registry.generators

import com.dannbrown.deltaboxlib.init.DeltaboxTags
import com.dannbrown.deltaboxlib.registry.DeltaboxRegistrate
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.RegistryObject

class CreativeTabGen(private val REGISTER: DeferredRegister<CreativeModeTab>,private val modId: String) {

  fun createTab(name: String,
                icon: () -> ItemStack,
                before: ResourceKey<CreativeModeTab>?,
                displayItems: CreativeModeTab.DisplayItemsGenerator
  ): RegistryObject<CreativeModeTab> {
    return REGISTER.register(name) {
      CreativeModeTab.builder()
        .withTabsBefore(before)
        .title(Component.translatable("itemGroup.${modId}.$name"))
        .icon(icon)
        .displayItems(displayItems)
        .build()
    }
  }

  companion object{
    // Utility method to display all items in the creative tab
    fun displayAllRegistrate(registrate: DeltaboxRegistrate, parameters: CreativeModeTab.ItemDisplayParameters, output:CreativeModeTab.Output) {
        for (entry in registrate.getAll(Registries.BLOCK)) {
          val block = entry.get()
          if (block.asItem() === Items.AIR) continue // avoid fluids and blocks without items
          if (block.asItem().defaultInstance.tags.anyMatch { itemTag: TagKey<Item> -> DeltaboxTags.ITEM.EXCLUDE_FROM_CREATIVE == itemTag }) continue // avoid items with the tag "deltaboxlib:exclude_from_creative"
          output.accept(ItemStack(block.asItem()))
        }

      for (entry in registrate.getAll(Registries.ITEM)) {
        val item = entry.get()
        if (item === Items.AIR) continue // avoid fluids and blocks without items
        if (item.defaultInstance.tags.anyMatch { itemTag: TagKey<Item> -> DeltaboxTags.ITEM.EXCLUDE_FROM_CREATIVE == itemTag }) continue // avoid items with the tag "deltaboxlib:exclude_from_creative"
        output.accept(ItemStack(item))
      }
    }
  }

  // OLD METHODS
  // Enchanted books
//        DatagenEnchantments.ENCHANTMENTS.getEntries().forEach { enchantment ->
//          if (!DatagenEnchantments.DONT_INCLUDE_CREATIVE.contains(enchantment)) {
//            output.accept(
//              EnchantedBookItem.createForEnchantment(
//                EnchantmentInstance(enchantment.get(), enchantment.get().getMaxLevel())
//              )
//            )
//          }
//        }
  //  // This will register items into the vanilla creative tabs
//  @SubscribeEvent
//  fun addVanillaCreativeTabs(event: BuildCreativeModeTabContentsEvent) {
//    if (event.tabKey === CreativeModeTabs.BUILDING_BLOCKS) {
////      event.accept(DatagenItems.ADAMANTIUM_INGOT)
//    }
//  }
}