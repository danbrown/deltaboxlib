package com.dannbrown.deltaboxlib.registrate.registry

import com.dannbrown.deltaboxlib.registrate.builders.BlockEntityBuilder
import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.entity.BlockEntityType
import java.util.function.Supplier

class BlockEntityRegistry(modId: String) {
  private val blockEntities = DeferredRegister.create(modId, Registries.BLOCK_ENTITY_TYPE)
  val entries = mutableListOf<BlockEntityBuilder<out BlockEntityType<*>>>()

  fun <T : BlockEntityType<*>> register(
    id: String,
    blockSupplier: Supplier<T>,
    blockBuilder: BlockEntityBuilder<out BlockEntityType<*>>
  ): Supplier<T> {
    entries.add(blockBuilder)
    return blockEntities.register(id, blockSupplier)
  }

  fun build() {
    blockEntities.register()
  }
}
