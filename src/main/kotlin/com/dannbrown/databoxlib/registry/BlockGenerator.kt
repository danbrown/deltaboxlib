package com.dannbrown.databoxlib.registry

import net.minecraft.world.level.block.Block

class BlockGenerator(val registrate: DataboxRegistrate) {
  fun  <T: Block> create(name: String): BlockGen<T> {
    return BlockGen(name, registrate)
  }
}