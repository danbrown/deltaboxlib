package com.dannbrown.deltaboxlib.registrate.builders

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.datagen.model.RegistrateItemModelGenerator
import com.dannbrown.deltaboxlib.registrate.registry.ItemEntry
import com.dannbrown.deltaboxlib.registrate.types.ItemRecipeFactory
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import com.dannbrown.deltaboxlib.registrate.types.NonNullBiConsumer
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import java.util.function.BiFunction
import java.util.function.Function
import java.util.function.Supplier

class ItemBuilder<T : Item>(_registrate: AbstractDeltaboxRegistrate, val itemId: String) :
  AbstractBuilder(_registrate) {
  constructor(_registrate: AbstractDeltaboxRegistrate, _blockBuilder: BlockBuilder<out Block>, _itemId: String) : this(
    _registrate,
    _itemId
  ) {
    blockBuilder = _blockBuilder
    itemModelFactory = defaultBlockItemModelFactory()
  }

  protected lateinit var blockBuilder: BlockBuilder<out Block>
  protected var props: Item.Properties = Item.Properties()
  protected var itemFactory: Supplier<T> = Supplier { Item(props) as T }
  protected var itemName = DeltaboxUtil.asName(itemId)
  private lateinit var itemInstance: Supplier<T>
  var compostableAmount = 0f

  var itemModelFactory: NonNullBiConsumer<RegistrateItemModelGenerator, Supplier<out Item>> = defaultModelFactory()
  var recipeFactory: ItemRecipeFactory = defaultRecipeFactory()


  // @ Default functions
  private fun defaultModelFactory(): NonNullBiConsumer<RegistrateItemModelGenerator, Supplier<out Item>> {
    return { g, i -> g.flatItem(i.get()) }
  }

  private fun defaultBlockItemModelFactory(): NonNullBiConsumer<RegistrateItemModelGenerator, Supplier<out Item>> {
    return { g, i ->
      g.blockItem(
        blockBuilder.getBlock().get()
      )
    } // it item deviates from block, the default model is a block model
  }

  private fun defaultRecipeFactory(): ItemRecipeFactory {
    return { r, b -> /* do nothing */ }
  }


  // @ Get Functions
  fun getItem(): Supplier<T> {
    return itemInstance
  }

  fun getName(): String {
    return itemName
  }

  // @ Builder Functions
  fun factory(_factoryFunction: Function<Item.Properties, out Item>): ItemBuilder<T> {
    this.itemFactory = Supplier { _factoryFunction.apply(props) as T }
    return this
  }

  fun factory(_factoryFunction: BiFunction<Item.Properties, Block, out Item>): ItemBuilder<T> {
    this.itemFactory = Supplier { _factoryFunction.apply(props, blockBuilder.getBlock().get()) as T }
    return this
  }

  fun properties(_factoryFunction: Function<Item.Properties, Item.Properties>): ItemBuilder<T> {
    this.props = _factoryFunction.apply(props)
    return this
  }

  fun model(_factoryFunction: NonNullBiConsumer<RegistrateItemModelGenerator, Supplier<out Item>>): ItemBuilder<T> {
    this.itemModelFactory = _factoryFunction
    return this
  }

  fun lang(langKey: String): ItemBuilder<T> {
    this.itemName = langKey
    return this
  }

  @SafeVarargs
  fun itemTags(vararg tags: TagKey<Item>): ItemBuilder<T> {
    for (itemTagKey in tags) {
      this.registrate.itemTags(itemTagKey).add({ itemInstance.get() }).register()
    }
    return this
  }

  fun recipe(factory: ItemRecipeFactory): ItemBuilder<T> {
    this.recipeFactory = factory
    return this
  }

  fun compostable(amount: Float = 0.2f): ItemBuilder<T> {
    this.compostableAmount = amount
    return this
  }


  // @ Registering
  private fun asEntry(): ItemEntry<T> {
    return ItemEntry(this)
  }

  fun register(): ItemEntry<T> {
    itemInstance = this.registrate.itemRegistry.register<T>(itemId, itemFactory, this)
    return asEntry()
  }

  fun build(): BlockBuilder<out Block> {
    blockBuilder.buildItemEntry(this.register())
    return blockBuilder
  }
}