package com.dannbrown.databoxlib.init

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.content.block.StickerBlock
import com.dannbrown.databoxlib.content.item.GeckoArmor.BacktankCustomArmorItem
import com.simibubi.create.AllCreativeModeTabs
import com.simibubi.create.content.equipment.armor.BacktankUtil
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.tags.TagKey
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.RegistryObject

object ProjectCreativeTabs {
  val TABS: DeferredRegister<CreativeModeTab> = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ProjectContent.MOD_ID)

  fun register(modBus: IEventBus) {
    TABS.register(modBus)
  }

  val DATABOX_MAIN_TAB: RegistryObject<CreativeModeTab> = TABS.register("databoxlib_tab") {
    CreativeModeTab.builder()
      .withTabsBefore(AllCreativeModeTabs.BASE_CREATIVE_TAB.key)
      .title(Component.translatable("itemGroup.databoxlib_tab"))
      .icon { ItemStack(ProjectBlocks.SAIL_BLOCK.get()) }
      .displayItems { parameters, output ->
        for (entry in ProjectContent.REGISTRATE.getAll(Registries.BLOCK)) {
          val block = entry.get()
          if (block.asItem() === Items.AIR) continue // avoid fluids and blocks without items
          if (block is StickerBlock) continue // avoid sticker blocks
          if (block.asItem().defaultInstance.tags.anyMatch { item: TagKey<Item> -> ProjectTags.ITEM.EXCLUDE_FROM_CREATIVE == item }) continue // avoid items with the tag "databoxlib:exclude_from_creative"
          output.accept(ItemStack(block.asItem()))
        }
      }
      .build()
  }

  val DATABOX_ITEMS_TAB: RegistryObject<CreativeModeTab> = TABS.register("databoxlib_items_tab") {
    CreativeModeTab.builder()
      .withTabsBefore(DATABOX_MAIN_TAB.key)
      .title(Component.translatable("itemGroup.databoxlib_items_tab"))
      .icon { ItemStack(ProjectItems.CATHODE_TUBE.get()) }
      .displayItems { parameters, output ->
        for (entry in ProjectContent.REGISTRATE.getAll(Registries.ITEM)) {
          val item = entry.get()
          if (item is BlockItem) continue // avoid blocks
          if (item.defaultInstance.tags.anyMatch { item: TagKey<Item> -> ProjectTags.ITEM.EXCLUDE_FROM_CREATIVE == item }) continue // avoid items with the tag "databoxlib:exclude_from_creative"
          output.accept(ItemStack(item))
          if (item is BacktankCustomArmorItem) {
            val backtankStack = ItemStack(item)
            backtankStack.getOrCreateTag().putInt("Air", BacktankUtil.maxAirWithoutEnchants())
            output.accept(backtankStack)
          }
        }
      }
      .build()
  }

  val DATABOX_STICKERS_TAB: RegistryObject<CreativeModeTab> = TABS.register("databoxlib_stickers_tab") {
    CreativeModeTab.builder()
      .withTabsBefore(DATABOX_ITEMS_TAB.key)
      .title(Component.translatable("itemGroup.databoxlib_stickers_tab"))
      .icon { ItemStack(ProjectBlocks.LAUGHING_STICKER.get()) }
      .displayItems { parameters, output ->
        for (entry in ProjectContent.REGISTRATE.getAll(Registries.BLOCK)) {
          val block = entry.get()
          if (block.asItem() === Items.AIR) continue // avoid fluids and blocks without items
          if (block !is StickerBlock) continue // avoid non-sticker blocks
          if (block.asItem().defaultInstance.tags.anyMatch { item: TagKey<Item> -> ProjectTags.ITEM.EXCLUDE_FROM_CREATIVE == item }) continue // avoid items with the tag "databoxlib:exclude_from_creative"
          output.accept(ItemStack(block.asItem()))
        }
      }
      .build()
  }

  val ALL_TAB: RegistryObject<CreativeModeTab> = TABS.register("all") {
    CreativeModeTab.builder()
      .withTabsBefore(DATABOX_STICKERS_TAB.key)
      .title(Component.translatable("itemGroup.all"))
      .icon { ItemStack(Items.BARRIER) }
      .displayItems { parameters, output ->
        for (entry in ProjectContent.REGISTRATE.getAll(Registries.BLOCK)) {
          val block = entry.get()
          if (block.asItem() === Items.AIR) continue // avoid fluids and blocks without items
          if (block.asItem().defaultInstance.tags.anyMatch { item: TagKey<Item> -> ProjectTags.ITEM.EXCLUDE_FROM_CREATIVE == item }) continue // avoid items with the tag "databoxlib:exclude_from_creative"
          output.accept(ItemStack(block.asItem()))
        }
        for (entry in ProjectContent.REGISTRATE.getAll(Registries.ITEM)) {
          val item = entry.get()
          if (item is BlockItem) continue // avoid blocks
          if (item.defaultInstance.tags.anyMatch { item: TagKey<Item> -> ProjectTags.ITEM.EXCLUDE_FROM_CREATIVE == item }) continue // avoid items with the tag "databoxlib:exclude_from_creative"
          output.accept(ItemStack(item))
          if (item is BacktankCustomArmorItem) {
            val backtankStack = ItemStack(item)
            backtankStack.getOrCreateTag().putInt("Air", BacktankUtil.maxAirWithoutEnchants())
            output.accept(backtankStack)
          }
        }
      }
      .build()
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