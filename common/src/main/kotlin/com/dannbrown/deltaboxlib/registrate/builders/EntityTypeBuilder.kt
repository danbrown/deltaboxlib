package com.dannbrown.deltaboxlib.registrate.builders

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import net.minecraft.world.entity.EntityType

class EntityTypeBuilder<T : EntityType<*>>(registrate: AbstractDeltaboxRegistrate, val blockId: String) :
  AbstractBuilder(registrate) {

}