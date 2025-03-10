package com.dannbrown.deltaboxlib.registrate

import com.dannbrown.deltaboxlib.registrate.builders.*
import com.dannbrown.deltaboxlib.registrate.presets.blocks.BlockPresets
import com.dannbrown.deltaboxlib.registrate.registry.*
import com.dannbrown.deltaboxlib.registrate.types.RecipeFactory
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.tags.TagKey
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import java.util.function.Supplier

abstract class AbstractDeltaboxRegistrate(val modId: String) {
  val blockRegistry: BlockRegistry = BlockRegistry(modId)
  val itemRegistry: ItemRegistry = ItemRegistry(modId)
  val langRegistry: LangRegistry = LangRegistry(modId)
  val tagRegistry: TagRegistry = TagRegistry(modId)
  val recipeRegistry: RecipeRegistry = RecipeRegistry(modId)
  val creativeTabRegistry: CreativeTabRegistry = CreativeTabRegistry(modId)


  fun <T : Block> block(blockId: String): BlockBuilder<T> {
    return BlockBuilder(this, blockId)
  }

  fun <T : Block> blockPreset(blockId: String): BlockPresets<T> {
    return BlockPresets(this, blockId)
  }

  fun <T : Item> item(blockId: String): ItemBuilder<T> {
    return ItemBuilder(this, blockId)
  }

  fun <T : Block, R : Item> item(blockId: String, blockBuilder: BlockBuilder<T>): ItemBuilder<R> {
    return ItemBuilder(this, blockBuilder, blockId)
  }

  fun langs(_modId: String = modId): LangBuilder {
    return LangBuilder(this, _modId)
  }

  fun blockTags(hostTag: TagKey<Block>): BlockTagBuilder {
    return BlockTagBuilder(this, hostTag)
  }

  fun itemTags(hostTag: TagKey<Item>): ItemTagBuilder {
    return ItemTagBuilder(this, hostTag)
  }

  fun recipe(factory: RecipeFactory): AbstractDeltaboxRegistrate {
    recipeRegistry.addRecipe(factory)
    return this
  }

  fun creativeTab(
    id: String,
    phrase: String,
    icon: Supplier<ItemStack>,
    displayItems: CreativeModeTab.DisplayItemsGenerator,
  ): RegistrySupplier<CreativeModeTab> {
    this.langs().creativeTab(id, phrase)
    return this.creativeTabRegistry.register(id, icon, displayItems)
  }


  fun buildRegistries() {
    blockRegistry.build()
    itemRegistry.build()
    creativeTabRegistry.build()
  }
}