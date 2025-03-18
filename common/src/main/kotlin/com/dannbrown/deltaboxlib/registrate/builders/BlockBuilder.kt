package com.dannbrown.deltaboxlib.registrate.builders

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.registry.BlockEntry
import com.dannbrown.deltaboxlib.registrate.registry.ItemEntry
import com.dannbrown.deltaboxlib.registrate.types.*
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import net.minecraft.tags.TagKey
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import java.util.function.BiFunction
import java.util.function.Supplier

class BlockBuilder<T : Block>(registrate: AbstractDeltaboxRegistrate, val blockId: String) :
  AbstractBuilder(registrate) {
  protected val ctx: BlockBuilderContext<T> = BlockBuilderContext(registrate, this)
  protected var props: BlockBehaviour.Properties = BlockBehaviour.Properties.copy(Blocks.STONE)
  protected var blockFactory: Supplier<T> = Supplier { Block(props) as T }
  protected var blockName: String = DeltaboxUtil.asName(blockId)
  protected var itemBuilder: ItemBuilder<out Item> = defaultItemBuilder()
  protected var itemEntry: ItemEntry<*>? = null
  protected lateinit var blockInstance: Supplier<T>
  protected var hasCustomItemBuilder: Boolean = false

  var lootTableFactory: BlockLootTableFactory = defaultLootTableFactory()
  var blockstateFactory: BlockstateFactory = defaultBlockstateFactory()
  var recipeFactory: BlockRecipeFactory = defaultRecipeFactory()

  // @ Default factories
  private fun defaultItemBuilder(): ItemBuilder<out Item> {
    return registrate.item<T, BlockItem>(blockId, this)
      .factory { props -> BlockItem(blockInstance.get(), props) }
      .model({ g, i -> g.blockItem(blockInstance.get()) })
  }

  private fun defaultLootTableFactory(): BlockLootTableFactory {
    return { lt, b -> lt.dropItself(b.get()) }
  }

  private fun defaultBlockstateFactory(): BlockstateFactory {
    return { g, b -> g.cubeAll(b.get()) }
  }

  private fun defaultItemBlockFactory(): BlockItemFactory {
    return BlockItemFactory { p: Item.Properties, b: Block -> BlockItem(blockInstance.get(), p) }
  }

  private fun defaultRecipeFactory(): BlockRecipeFactory {
    return { r, b -> /* do nothing */ }
  }

  // @ Get Functions
  fun getBlock(): Supplier<T> {
    return blockInstance
  }

  fun getContext(): BlockBuilderContext<T> {
    return ctx
  }

  fun getName(): String {
    return blockName
  }

  // @ Builder Functions
  fun factory(_factoryFunction: BiFunction<BlockBuilderContext<T>, BlockBehaviour.Properties, Block>): BlockBuilder<T> {
    this.blockFactory = Supplier { _factoryFunction.apply(ctx, props) as T }
    return this
  }

  fun copyFrom(_referenceBlock: Supplier<Block>): BlockBuilder<T> {
    props = BlockBehaviour.Properties.copy(_referenceBlock.get())
    return this
  }

  fun properties(_factoryFunction: BlockPropertiesFactory): BlockBuilder<T> {
    this.props = _factoryFunction.apply(ctx, props)
    return this
  }

  fun item(_factoryFunction: BlockItemFactory = defaultItemBlockFactory()): ItemBuilder<out Item> {
    hasCustomItemBuilder = true
    return itemBuilder.factory(_factoryFunction)
  }

  fun noItem(): BlockBuilder<T> {
    this.ctx.noItem = true // disables default block item creation
    lootTableFactory =
      { lt, b -> lt.noLoot(b.get()) } // remove loot as it doesn't have an item to drop, this can be replaced to drop other stuff
    return this
  }

  fun loot(_lootFactory: BlockLootTableFactory): BlockBuilder<T> {
    this.lootTableFactory = _lootFactory
    return this
  }

  fun blockstate(_blockstateFactory: BlockstateFactory): BlockBuilder<T> {
    this.blockstateFactory = _blockstateFactory
    return this
  }

  fun lang(langKey: String): BlockBuilder<T> {
    this.blockName = langKey
    return this
  }

  fun flammable(burnChance: Int = 20, spreadChance: Int = 5): BlockBuilder<T> {
    this.ctx.flammabilityBurnChance = burnChance
    this.ctx.flammabilitySpreadChance = spreadChance
    return this
  }

  fun strippable(otherBlock: Supplier<out Block>): BlockBuilder<T> {
    this.ctx.strippableOther = otherBlock
    return this
  }

  fun potted(otherBlock: Supplier<out Block>): BlockBuilder<T> {
    this.ctx.pottedOther = otherBlock
    return this
  }

  fun cutoutRender(): BlockBuilder<T> {
    this.ctx.hasCutoutRender = true
    return this
  }

  @SafeVarargs
  fun blockTags(vararg tag: TagKey<Block>): BlockBuilder<T> {
    for (blockTagKey in tag) {
      registrate.tagRegistry.addBlock(blockTagKey, asEntry())
    }
    return this
  }

  @SafeVarargs
  fun itemTags(vararg tags: TagKey<Item>): BlockBuilder<T> {
    itemBuilder.itemTags(*tags)
    return this
  }

  // this is kinda useless, but I like to keep tool tags separate
  fun toolAndTier(tool: TagKey<Block>?, tier: TagKey<Block>?, correctToolForDrops: Boolean = true): BlockBuilder<T> {
    if (tool !== null) this.blockTags(tool)
    if (tier !== null) this.blockTags(tier)
    if (correctToolForDrops) this.props = props.requiresCorrectToolForDrops()
    return this
  }

  fun recipe(factory: BlockRecipeFactory): BlockBuilder<T> {
    this.recipeFactory = factory
    return this
  }

  fun color(color: MapColor): BlockBuilder<T> {
    this.ctx.color = color
    return this
  }

  fun textureName(textureName: String): BlockBuilder<T> {
    this.ctx.textureName = textureName
    return this
  }

  fun biomeColors(): BlockBuilder<T> {
    this.ctx.hasBiomeColors = true
    return this
  }

  fun compostable(amount: Float = 0.2f): BlockBuilder<T> {
    this.ctx.compostableAmount = amount
    return this
  }

  fun fromFamily(
    copyFrom: Supplier<Block>,
    propsFactory: BlockPropertiesFactory,
    color: MapColor? = null,
    tool: TagKey<Block>? = null,
    tier: TagKey<Block>? = null,
    correctToolForDrops: Boolean = true,
  ): BlockBuilder<T> {
    this.copyFrom(copyFrom)
    this.properties(propsFactory)
    if (color !== null) this.color(color)
    this.toolAndTier(tool, tier, correctToolForDrops)
    return this
  }

  // @ Registering
  private fun asEntry(): BlockEntry<T> {
    return BlockEntry(this, itemEntry)
  }

  fun buildItemEntry(_itemEntry: ItemEntry<*>) {
    itemEntry = _itemEntry
  }

  fun register(doRegister: Boolean = true): BlockEntry<T> {
    if (!doRegister) {
      return BlockEntry(null, null)
    }
    blockInstance = registrate.blockRegistry.register(blockId, blockFactory, this)
    if (!hasCustomItemBuilder && !this.ctx.noItem) itemBuilder.build()
    return asEntry()
  }
}