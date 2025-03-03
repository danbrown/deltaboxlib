package com.dannbrown.deltaboxlib.registrate.builders

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.datagen.RegistrateBlockLootTables
import com.dannbrown.deltaboxlib.registrate.datagen.model.RegistrateBlockModelGenerator
import com.dannbrown.deltaboxlib.registrate.util.NonNullBiConsumer
import net.minecraft.tags.TagKey
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import java.util.function.BiFunction
import java.util.function.Function
import java.util.function.Supplier

class BlockBuilder(val _registrate: AbstractDeltaboxRegistrate, val blockId: String) : AbstractBuilder(_registrate) {
  protected var props: BlockBehaviour.Properties = BlockBehaviour.Properties.copy(Blocks.STONE)
  protected var blockFactory: Supplier<Block> = Supplier { Block(props) }
  protected var noItem: Boolean = false
  protected var itemBuilder: ItemBuilder = defaultItemBuilder()

  var lootTableFactory: NonNullBiConsumer<RegistrateBlockLootTables, Supplier<Block>> = { lt, b -> lt.dropSelf(b.get()) }
  var blockstateFactory: NonNullBiConsumer<RegistrateBlockModelGenerator, Supplier<Block>> = { g, b -> g.cubeAll(b.get()) }

  lateinit var blockInstance: Supplier<Block>

  private fun defaultItemBuilder(): ItemBuilder {
    return _registrate.item(blockId, this)
      .factory { props -> BlockItem(blockInstance.get(), props) }
      .model({ g, i -> g.blockItem(blockInstance.get()) })
  }

  fun factory(_factoryFunction: Function<BlockBehaviour.Properties, Block>): BlockBuilder {
    this.blockFactory = Supplier { _factoryFunction.apply(props) }
    return this
  }

  fun copyFrom(_referenceBlock: Supplier<Block>): BlockBuilder {
    props = BlockBehaviour.Properties.copy(_referenceBlock.get())
    return this
  }

  fun properties(_factoryFunction: Function<BlockBehaviour.Properties, BlockBehaviour.Properties>): BlockBuilder {
    this.props = _factoryFunction.apply(props)
    return this
  }

  fun item(_factoryFunction: BiFunction<Item.Properties, Block, Item>): ItemBuilder {
    this.noItem = true // disables default block item creation, but returns a new item builder
    return registrate.item(blockId, this).factory(_factoryFunction)
  }

  fun noItem(): BlockBuilder {
    this.noItem = true // disables default block item creation
    lootTableFactory = { lt, b -> lt.noLoot(b) }
    return this
  }

  fun loot(_lootFactory: NonNullBiConsumer<RegistrateBlockLootTables, Supplier<Block>>): BlockBuilder {
    this.lootTableFactory = _lootFactory
    return this
  }

  fun blockstate(_blockstateFactory: NonNullBiConsumer<RegistrateBlockModelGenerator, Supplier<Block>>): BlockBuilder {
    this.blockstateFactory = _blockstateFactory
    return this
  }

  @SafeVarargs
  fun tag(vararg tag: TagKey<Block>): BlockBuilder {
    for (blockTagKey in tag) {
      // TODO
    }
    return this
  }

  fun register(): Supplier<Block> {
    val block = registrate.blockRegistry.register(blockId, blockFactory, this)
    blockInstance = block
    if (!noItem) itemBuilder.build()
    return block
  }
}