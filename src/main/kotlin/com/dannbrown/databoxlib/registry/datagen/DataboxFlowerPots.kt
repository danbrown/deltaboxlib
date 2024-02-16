package com.dannbrown.databoxlib.registry.datagen

import com.tterrag.registrate.util.entry.BlockEntry
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FlowerPotBlock

/**
 * A class to handle the registration of flower pots.
 *
 * @param flowerPots A mutable map of plants and their corresponding flower pots.
 */
abstract class DataboxFlowerPots(private val flowerPots: MutableMap<BlockEntry<out Block>, BlockEntry<out Block>>) {
  fun register() {
    flowerPots.forEach { (plant, pot) ->
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