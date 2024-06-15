package com.dannbrown.deltaboxlib.registry.generators

import com.dannbrown.deltaboxlib.registry.DeltaboxRegistrate
import net.minecraft.world.level.block.Block

class BlockGenerator(val registrate: DeltaboxRegistrate) {
  fun  <T: Block> create(name: String): BlockGen<T> {
    return BlockGen(name, registrate)
  }

  fun createFamily(name: String): BlockFamilyGen {
    return BlockFamilyGen(name, this)
  }
}