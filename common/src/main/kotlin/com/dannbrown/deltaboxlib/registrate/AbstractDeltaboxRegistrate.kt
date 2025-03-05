package com.dannbrown.deltaboxlib.registrate

import com.dannbrown.deltaboxlib.registrate.builders.*
import com.dannbrown.deltaboxlib.registrate.registry.*
import com.dannbrown.deltaboxlib.registrate.types.RecipeFactory
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

abstract class AbstractDeltaboxRegistrate(val modId: String) {
  val blockRegistry: BlockRegistry = BlockRegistry(modId)
  val itemRegistry: ItemRegistry = ItemRegistry(modId)
  val langRegistry: LangRegistry = LangRegistry(modId)
  val tagRegistry: TagRegistry = TagRegistry(modId)
  val recipeRegistry: RecipeRegistry = RecipeRegistry(modId)


  fun block(blockId: String): BlockBuilder {
    return BlockBuilder(this, blockId)
  }

  fun item(blockId: String): ItemBuilder {
    return ItemBuilder(this, blockId)
  }

  fun item(blockId: String, blockBuilder: BlockBuilder): ItemBuilder {
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

  fun buildRegistries() {
    blockRegistry.build()
    itemRegistry.build()
  }
}