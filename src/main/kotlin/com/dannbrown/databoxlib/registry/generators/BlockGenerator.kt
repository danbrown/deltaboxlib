package com.dannbrown.databoxlib.registry.generators

import com.dannbrown.databoxlib.registry.DataboxRegistrate
import net.minecraft.world.level.block.Block

class BlockGenerator(val registrate: DataboxRegistrate) {
  fun  <T: Block> create(name: String): BlockGen<T> {
    return BlockGen(name, registrate)
  }

  fun createFamily(name: String): BlockFamilyGen {
    return BlockFamilyGen(name, this)
  }
}