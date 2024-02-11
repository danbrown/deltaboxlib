package com.dannbrown.databoxlib.init

import com.dannbrown.databoxlib.DataboxLib
import com.dannbrown.databoxlib.registry.BlockGen
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block

object DataboxBlocks {
  fun register(){
    DataboxLib.LOGGER.info("Registering blocks")
  }

  val SAMPLE_BLOCK = DataboxLib.REGISTRATE.block<Block>("adamantium_block") { Block(it) }
    .properties { it.strength(1.0f, 1.0f) }
    .item().build()
    .register()

  val SAMPLE_BLOCK2 = BlockGen<Block>("basalt_adamantium")
    .oreBlock({Items.IRON_INGOT}, "basalt")
    .properties { it.strength(1.0f, 1.0f) }
    .register(DataboxLib.REGISTRATE)
}