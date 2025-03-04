package com.dannbrown.deltaboxlib.registrate

import com.dannbrown.deltaboxlib.registrate.builders.BlockBuilder
import com.dannbrown.deltaboxlib.registrate.builders.ItemBuilder
import com.dannbrown.deltaboxlib.registrate.builders.LangBuilder
import com.dannbrown.deltaboxlib.registrate.registry.*

abstract class AbstractDeltaboxRegistrate(val modId: String) {
  val blockRegistry: BlockRegistry = BlockRegistry(modId)
  val itemRegistry: ItemRegistry = ItemRegistry(modId)
  val langRegistry: LangRegistry = LangRegistry(modId)
  val flammableBlockRegistry: FlammableBlockRegistry = FlammableBlockRegistry()
  val strippableBlockRegistry: StrippableBlockRegistry = StrippableBlockRegistry()
  val pottedBlockRegistry: PottedBlockRegistry = PottedBlockRegistry()
  val cutoutRenderRegistry: CutoutRenderRegistry = CutoutRenderRegistry()


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

  fun buildRegistries() {
    blockRegistry.build()
    itemRegistry.build()
  }
}