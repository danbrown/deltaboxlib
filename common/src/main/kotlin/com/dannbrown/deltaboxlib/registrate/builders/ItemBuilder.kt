package com.dannbrown.deltaboxlib.registrate.builders

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.datagen.RegistrateItemModelGenerator
import net.minecraft.data.models.ItemModelGenerators
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import java.util.function.BiFunction
import java.util.function.Function
import java.util.function.Supplier

class ItemBuilder : AbstractBuilder {
  protected lateinit var blockBuilder: BlockBuilder
  protected val itemId: String
  protected var props: Item.Properties = Item.Properties()
  protected var itemFactory: Supplier<Item> = Supplier { Item(props) }

  var itemModelFactory: ((RegistrateItemModelGenerator, Supplier<Item>) -> Unit)? = { g, i -> g.flatItem(i.get()) }

  var itemInstance: Supplier<Item>? = null

  constructor(_registrate: AbstractDeltaboxRegistrate, _itemId: String) : super(_registrate) {
    itemId = _itemId
  }

  constructor(_registrate: AbstractDeltaboxRegistrate, _blockBuilder: BlockBuilder, _itemId: String) : super(_registrate) {
    blockBuilder = _blockBuilder
    itemId = _itemId
  }



  fun factory(_factoryFunction: Function<Item.Properties, Item>): ItemBuilder {
    this.itemFactory = Supplier { _factoryFunction.apply(props) }
    return this
  }

  fun factory(_factoryFunction: BiFunction<Item.Properties, Block, Item>): ItemBuilder {
    this.itemFactory = Supplier { _factoryFunction.apply(props, blockBuilder.blockInstance!!.get()) }
    return this
  }

  fun properties(_factoryFunction: Function<Item.Properties, Item.Properties>): ItemBuilder {
    this.props = _factoryFunction.apply(props)
    return this
  }

  @SafeVarargs
  fun tag(vararg tag: TagKey<Item>): ItemBuilder {
    for (itemTagKey in tag) {
      // this.registrate.itemTags(itemTagKey, itemInstance)
    }
    return this
  }

  fun register(): Supplier<Item> {
    val item: Supplier<Item> = this.registrate.itemRegistry.register(itemId, itemFactory, this)
    itemInstance = item
    return item
  }

  fun build(): BlockBuilder {
    this.register()
    return blockBuilder
  }
}