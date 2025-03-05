package com.dannbrown.deltaboxlib.registrate.registry

import com.dannbrown.deltaboxlib.registrate.builders.BlockBuilder
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import java.util.function.Supplier

data class BlockEntry<T : Block>(private val builder: BlockBuilder<T>?, private val itemEntry: ItemEntry<*>?) {
  constructor(block: Supplier<Block>) : this(null, null) {
    setupBlock = block
  }

  private var setupBlock: Supplier<Block>? = null

  fun getBuilder(): BlockBuilder<T> {
    if (builder == null) throw NoSuchFieldError("Cannot get the builder from an external block entry.")
    return builder
  }

  fun get(): Block {
    return if (setupBlock !== null) setupBlock!!.get() else if (builder !== null) builder.getBlock()
      .get() else throw throw NoSuchFieldError("This block entry is invalid")
  }

  fun getItem(): Item {
    if (itemEntry == null) throw NoSuchFieldError("Cannot get the item from an external block entry.")
    return itemEntry.get()
  }

  fun getItemEntry(): ItemEntry<*> {
    if (itemEntry == null) throw NoSuchFieldError("Cannot get the item from an external block entry.")
    return itemEntry
  }

  companion object {
    fun from(block: Block): BlockEntry<*> {
      return BlockEntry<Block> { block }
    }

    fun from(block: Supplier<Block>): BlockEntry<*> {
      return BlockEntry<Block>(block)
    }
  }
}