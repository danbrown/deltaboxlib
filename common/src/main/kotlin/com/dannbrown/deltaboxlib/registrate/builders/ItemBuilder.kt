package com.dannbrown.deltaboxlib.registrate.builders

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.datagen.model.RegistrateItemModelGenerator
import com.dannbrown.deltaboxlib.registrate.registry.ItemEntry
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import com.dannbrown.deltaboxlib.registrate.types.NonNullBiConsumer
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import java.util.function.BiFunction
import java.util.function.Function
import java.util.function.Supplier

class ItemBuilder(_registrate: AbstractDeltaboxRegistrate, val itemId: String) : AbstractBuilder(_registrate) {
  constructor(_registrate: AbstractDeltaboxRegistrate, _blockBuilder: BlockBuilder, _itemId: String) : this(
    _registrate,
    _itemId
  ) {
    blockBuilder = _blockBuilder
    itemModelFactory = defaultBlockItemModelFactory()
  }

  protected lateinit var blockBuilder: BlockBuilder
  protected var props: Item.Properties = Item.Properties()
  protected var itemFactory: Supplier<Item> = Supplier { Item(props) }
  protected var itemName = DeltaboxUtil.asName(itemId)
  private lateinit var itemInstance: Supplier<Item>

  var itemModelFactory: NonNullBiConsumer<RegistrateItemModelGenerator, Supplier<Item>> = defaultModelFactory()

  // @ Default Factories
  private fun defaultModelFactory(): NonNullBiConsumer<RegistrateItemModelGenerator, Supplier<Item>> {
    return { g, i -> g.flatItem(i.get()) }
  }

  private fun defaultBlockItemModelFactory(): NonNullBiConsumer<RegistrateItemModelGenerator, Supplier<Item>> {
    return { g, i ->
      g.blockItem(
        blockBuilder.getBlock().get()
      )
    } // it item deviates from block, the default model is a block model
  }

  // @ Get Functions
  fun getItem(): Supplier<Item> {
    return itemInstance
  }

  fun getName(): String {
    return itemName
  }

  // @ Builder Functions
  fun factory(_factoryFunction: Function<Item.Properties, Item>): ItemBuilder {
    this.itemFactory = Supplier { _factoryFunction.apply(props) }
    return this
  }

  fun factory(_factoryFunction: BiFunction<Item.Properties, Block, Item>): ItemBuilder {
    this.itemFactory = Supplier { _factoryFunction.apply(props, blockBuilder.getBlock().get()) }
    return this
  }

  fun properties(_factoryFunction: Function<Item.Properties, Item.Properties>): ItemBuilder {
    this.props = _factoryFunction.apply(props)
    return this
  }

  fun model(_factoryFunction: NonNullBiConsumer<RegistrateItemModelGenerator, Supplier<Item>>): ItemBuilder {
    this.itemModelFactory = _factoryFunction
    return this
  }

  fun lang(langKey: String): ItemBuilder {
    this.itemName = langKey
    return this
  }

  @SafeVarargs
  fun tag(vararg tag: TagKey<Item>): ItemBuilder {
    for (itemTagKey in tag) {
      // this.registrate.itemTags(itemTagKey, itemInstance)
    }
    return this
  }

  // @ Registering
  private fun asEntry(): ItemEntry {
    return ItemEntry(this)
  }

  fun register(): ItemEntry {
    itemInstance = this.registrate.itemRegistry.register(itemId, itemFactory, this)
    return asEntry()
  }

  fun build(): BlockBuilder {
    blockBuilder.buildItemEntry(this.register())
    return blockBuilder
  }
}