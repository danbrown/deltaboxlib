package com.dannbrown.databoxlib.init

import com.dannbrown.databoxlib.DataboxLib
import com.dannbrown.databoxlib.datagen.transformers.BlockstatePresets
import com.dannbrown.databoxlib.registry.BlockGen
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block

object DataboxBlocks {
  fun register(){
    DataboxLib.LOGGER.info("Registering blocks")
  }

  val SAMPLE_BLOCK2 = BlockGen<Block>("adamantium_block")
    .oreBlock({Items.IRON_INGOT}, "basalt", false)
    .properties { it.strength(1.0f, 1.0f) }
    .register(DataboxLib.REGISTRATE)
}