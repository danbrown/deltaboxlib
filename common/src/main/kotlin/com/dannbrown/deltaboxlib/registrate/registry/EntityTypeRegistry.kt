package com.dannbrown.deltaboxlib.registrate.registry

import com.dannbrown.deltaboxlib.registrate.builders.EntityTypeBuilder
import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.world.entity.EntityType
import java.util.function.Supplier

class EntityTypeRegistry(modId: String) {
  private val entities = DeferredRegister.create(modId, Registries.ENTITY_TYPE)
  val entries = mutableListOf<EntityTypeBuilder<out EntityType<*>>>()

  fun <T : EntityType<*>> register(
    id: String,
    blockSupplier: Supplier<T>,
    blockBuilder: EntityTypeBuilder<out EntityType<*>>
  ): Supplier<T> {
    entries.add(blockBuilder)
    return entities.register(id, blockSupplier)
  }

  fun build() {
    entities.register()
  }
}
