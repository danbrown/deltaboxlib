package com.dannbrown.deltaboxlib.registrate.registry

import com.dannbrown.deltaboxlib.registrate.builders.BlockBuilder
import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.Block
import java.util.function.Supplier

class BlockRegistry(modId: String) {
  private val blocks = DeferredRegister.create(modId, Registries.BLOCK)
  val entries = mutableListOf<BlockBuilder>()

  fun <T : Block> register(id: String, blockSupplier: Supplier<T>, blockBuilder: BlockBuilder): Supplier<T> {
    entries.add(blockBuilder)
    return blocks.register(id, blockSupplier)
  }

  fun build() {
    blocks.register()
  }
}