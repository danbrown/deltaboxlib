package com.dannbrown.deltaboxlib.registrate.builders

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.registry.BlockEntry
import com.dannbrown.deltaboxlib.registrate.registry.ItemEntry
import com.dannbrown.deltaboxlib.registrate.types.BlockLootTableFactory
import com.dannbrown.deltaboxlib.registrate.types.BlockstateFactory
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import net.minecraft.tags.TagKey
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import java.util.function.BiFunction
import java.util.function.Supplier

class BlockBuilder(registrate: AbstractDeltaboxRegistrate, val blockId: String) : AbstractBuilder(registrate) {
  protected val ctx: BlockBuilderContext = BlockBuilderContext(registrate, this)
  protected var props: BlockBehaviour.Properties = BlockBehaviour.Properties.copy(Blocks.STONE)
  protected var blockFactory: Supplier<Block> = Supplier { Block(props) }
  protected var blockName: String = DeltaboxUtil.asName(blockId)
  protected var itemBuilder: ItemBuilder = defaultItemBuilder()
  protected var itemEntry: ItemEntry? = null
  protected lateinit var blockInstance: Supplier<Block>

  var lootTableFactory: BlockLootTableFactory = defaultLootTableFactory()
  var blockstateFactory: BlockstateFactory = defaultBlockstateFactory()

  // @ Default factories
  private fun defaultItemBuilder(): ItemBuilder {
    return registrate.item(blockId, this)
      .factory { props -> BlockItem(blockInstance.get(), props) }
      .model({ g, i -> g.blockItem(blockInstance.get()) })
  }

  private fun defaultLootTableFactory(): BlockLootTableFactory {
    return { lt, b -> lt.dropSelf(b.get()) }
  }

  private fun defaultBlockstateFactory(): BlockstateFactory {
    return { g, b -> g.cubeAll(b.get()) }
  }

  // @ Get Functions
  fun getBlock(): Supplier<Block> {
    return blockInstance
  }

  fun getContext(): BlockBuilderContext {
    return ctx
  }

  fun getName(): String {
    return blockName
  }

  // @ Builder Functions
  fun factory(_factoryFunction: BiFunction<BlockBuilderContext, BlockBehaviour.Properties, Block>): BlockBuilder {
    this.blockFactory = Supplier { _factoryFunction.apply(ctx, props) }
    return this
  }

  fun copyFrom(_referenceBlock: Supplier<Block>): BlockBuilder {
    props = BlockBehaviour.Properties.copy(_referenceBlock.get())
    return this
  }

  fun properties(_factoryFunction: BiFunction<BlockBuilderContext, BlockBehaviour.Properties, BlockBehaviour.Properties>): BlockBuilder {
    this.props = _factoryFunction.apply(ctx, props)
    return this
  }

  fun item(_factoryFunction: BiFunction<Item.Properties, Block, Item>): ItemBuilder {
    this.ctx.noItem = true // disables default block item creation, but returns a new item builder
    return registrate.item(blockId, this).factory(_factoryFunction)
  }

  fun noItem(): BlockBuilder {
    this.ctx.noItem = true // disables default block item creation
    lootTableFactory =
      { lt, b -> lt.noLoot(b) } // remove loot as it doesn't have an item to drop, this can be replaced to drop other stuff
    return this
  }

  fun loot(_lootFactory: BlockLootTableFactory): BlockBuilder {
    this.lootTableFactory = _lootFactory
    return this
  }

  fun blockstate(_blockstateFactory: BlockstateFactory): BlockBuilder {
    this.blockstateFactory = _blockstateFactory
    return this
  }

  fun lang(langKey: String): BlockBuilder {
    this.blockName = langKey
    return this
  }

  fun flammable(burnChance: Int = 20, spreadChance: Int = 5): BlockBuilder {
    this.ctx.flammabilityBurnChance = burnChance
    this.ctx.flammabilitySpreadChance = spreadChance
    return this
  }

  fun strippable(otherBlock: BlockEntry): BlockBuilder {
    this.ctx.strippableOther = otherBlock
    return this
  }

  fun potted(otherBlock: BlockEntry): BlockBuilder {
    this.ctx.pottedOther = otherBlock
    return this
  }

  fun cutoutRender(): BlockBuilder {
    this.ctx.hasCutoutRender = true
    return this
  }

  @SafeVarargs
  fun tag(vararg tag: TagKey<Block>): BlockBuilder {
    for (blockTagKey in tag) {
      // TODO
    }
    return this
  }

  // @ Registering
  private fun asEntry(): BlockEntry {
    return BlockEntry(this, itemEntry)
  }

  fun buildItemEntry(_itemEntry: ItemEntry) {
    itemEntry = _itemEntry
  }

  fun register(): BlockEntry {
    blockInstance = registrate.blockRegistry.register(blockId, blockFactory, this)
    if (!this.ctx.noItem) itemBuilder.build()
    return asEntry()
  }
}