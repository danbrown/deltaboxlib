package com.dannbrown.deltaboxlib.registrate.registry

import com.dannbrown.deltaboxlib.registrate.builders.BlockEntityBuilder
import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.entity.BlockEntityType
import java.util.function.Supplier
import net.minecraft.world.level.block.entity.BlockEntity

class BlockEntityRegistry(modId: String) {
  private val blockEntities = DeferredRegister.create(modId, Registries.BLOCK_ENTITY_TYPE)
  val entries = mutableListOf<BlockEntityBuilder<out BlockEntity>>()
  var isRegistered = false

  fun <T : BlockEntity> register(
    id: String,
    supplier: Supplier<BlockEntityType<T>>,
    builder: BlockEntityBuilder<out BlockEntity>
  ): Supplier<BlockEntityType<T>> {
    entries.add(builder)
    return blockEntities.register(id, supplier)
  }

  fun build() {
    if (isRegistered) return
    isRegistered = true
    blockEntities.register()
  }
}