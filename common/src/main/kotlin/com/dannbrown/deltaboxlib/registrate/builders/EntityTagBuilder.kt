package com.dannbrown.deltaboxlib.registrate.builders

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import java.util.function.Supplier

class EntityTagBuilder(private val registrate: AbstractDeltaboxRegistrate, private val hostTag: TagKey<EntityType<*>>) {
  fun add(tagKey: Supplier<EntityType<*>>): EntityTagBuilder {
    registrate.tagRegistry.addEntity(hostTag, tagKey)
    return this
  }
}