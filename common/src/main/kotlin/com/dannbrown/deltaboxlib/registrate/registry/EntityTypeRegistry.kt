package com.dannbrown.deltaboxlib.registrate.registry

import com.dannbrown.deltaboxlib.registrate.builders.EntityTypeBuilder
import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import java.util.function.Supplier

class EntityTypeRegistry(modId: String) {
  private val entities = DeferredRegister.create(modId, Registries.ENTITY_TYPE)
  val entries = mutableListOf<EntityTypeBuilder<out Entity>>()
  var isRegistered = false

  fun <T : Entity> register(
    id: String,
    supplier: Supplier<EntityType<T>>,
    builder: EntityTypeBuilder<out Entity>
  ): Supplier<EntityType<T>> {
    entries.add(builder)
    return entities.register(id, supplier)
  }

  fun build() {
    if (isRegistered) return
    isRegistered = true
    entities.register()
  }
}
