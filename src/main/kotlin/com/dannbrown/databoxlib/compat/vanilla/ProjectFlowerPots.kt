package com.dannbrown.databoxlib.compat.vanilla

import com.dannbrown.databoxlib.init.ProjectBlocks
import com.tterrag.registrate.util.entry.BlockEntry
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FlowerPotBlock

object ProjectFlowerPots {
  private val FLOWER_POTS: MutableMap<BlockEntry<out Block>, BlockEntry<out Block>> = mutableMapOf()

  //  DataboxBlocks.SIMPLE_FLOWER to DataboxBlocks.POTTED_SIMPLE_FLOWER,
//  DataboxBlocks.JOSHUA_FAMILY.SAPLING!! to DataboxBlocks.JOSHUA_FAMILY.POTTED_SAPLING!!
  init {
    FLOWER_POTS[ProjectBlocks.SIMPLE_FLOWER] = ProjectBlocks.POTTED_SIMPLE_FLOWER
    FLOWER_POTS[ProjectBlocks.JOSHUA_FAMILY.SAPLING!!] = ProjectBlocks.JOSHUA_FAMILY.POTTED_SAPLING!!
  }

  fun register() {
    FLOWER_POTS.forEach { (plant, pot) ->
      try {
        if (plant != null && pot != null) {
          (Blocks.FLOWER_POT as FlowerPotBlock).addPlant(plant.id, pot)
        }
      } catch (e: Exception) {
        println("Failed to add plant ${plant.id} to flower pot ${pot.id}")
      }
    }
  }
}