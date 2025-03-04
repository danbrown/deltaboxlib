package com.dannbrown.deltaboxlib.registrate.registry

import com.dannbrown.deltaboxlib.registrate.builders.BlockBuilder
import net.minecraft.world.level.block.Block
import java.util.function.Supplier

data class BlockEntry(private val builder: BlockBuilder?) {
  constructor(block: Block) : this(null) {
    setupBlock = block
  }

  private var setupBlock: Block? = null

  fun getBuilder(): BlockBuilder {
    if (builder == null) throw NoSuchFieldError("Cannot get the builder from an external block entry.")
    return builder
  }

  fun get(): Block {
    return if (setupBlock !== null) setupBlock!! else if (builder !== null) builder.getBlock()
      .get() else throw throw NoSuchFieldError("This block entry is invalid")
  }

  companion object {
    fun from(block: Block): BlockEntry {
      return BlockEntry(block)
    }

    fun from(block: Supplier<Block>): BlockEntry {
      return BlockEntry(block.get())
    }
  }
}