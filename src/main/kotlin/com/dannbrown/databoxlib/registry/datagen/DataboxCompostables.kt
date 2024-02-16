package com.dannbrown.databoxlib.registry.datagen

import net.minecraft.world.item.Item
import net.minecraft.world.level.block.ComposterBlock

/**
 * A class to handle the registration of compostable items.
 *
 * @param compostables A mutable map of items and their compostable values.
 */
abstract class DataboxCompostables(private val compostables: MutableMap<Item, Float>) {
  fun register() {
    ComposterBlock.COMPOSTABLES.putAll(compostables)
  }
}