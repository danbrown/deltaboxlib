package com.dannbrown.deltaboxlib.registrate.builders

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import net.minecraft.world.level.block.entity.BlockEntityType

class BlockEntityBuilder<T : BlockEntityType<*>>(registrate: AbstractDeltaboxRegistrate, val blockId: String) :
  AbstractBuilder(registrate) {

}