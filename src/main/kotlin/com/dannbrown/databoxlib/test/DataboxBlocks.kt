package com.dannbrown.databoxlib.test

import com.dannbrown.databoxlib.DataboxLib
import net.minecraft.world.level.block.Block

object DataboxBlocks {
  fun register(){
    DataboxLib.LOGGER.info("Registering blocks")
  }

  val SAMPLE_BLOCK = DataboxLib.REGISTRATE.block<Block>("adamantium_block") { Block(it) }
    .properties { it.strength(1.0f, 1.0f) }
    .register()

}