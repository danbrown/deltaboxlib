package com.dannbrown.databoxlib.registry.generators

import com.dannbrown.databoxlib.init.DataboxTags
import com.dannbrown.databoxlib.registry.DataboxRegistrate
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
    fun displayAllRegistrate(registrate: DataboxRegistrate, parameters: CreativeModeTab.ItemDisplayParameters, output:CreativeModeTab.Output) {
        for (entry in registrate.getAll(Registries.BLOCK)) {
          val block = entry.get()
          if (block.asItem() === Items.AIR) continue // avoid fluids and blocks without items
          if (block.asItem().defaultInstance.tags.anyMatch { item: TagKey<Item> -> DataboxTags.ITEM.EXCLUDE_FROM_CREATIVE == item }) continue // avoid items with the tag "explore:exclude_from_creative"
          output.accept(ItemStack(block.asItem()))
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