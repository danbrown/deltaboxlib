package com.dannbrown.databoxlib.compat.vanilla

import com.dannbrown.databoxlib.init.ProjectBlocks
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.ComposterBlock

object ProjectCompostables {
  val COMPOSTABLES: MutableMap<Item, Float> = mutableMapOf()

  init {
    COMPOSTABLES[ProjectBlocks.JOSHUA_FAMILY.LEAVES!!.get()
      .asItem()] = 0.3f
    COMPOSTABLES[ProjectBlocks.JOSHUA_FAMILY.SAPLING!!.get()
      .asItem()] = 0.3f
    COMPOSTABLES[ProjectBlocks.GUAYULE_SHRUB.get()
      .asItem()] = 0.3f
    COMPOSTABLES[ProjectBlocks.AGAVE.get()
      .asItem()] = 0.3f
    COMPOSTABLES[ProjectBlocks.DEAD_GRASS.get()
      .asItem()] = 0.3f
    COMPOSTABLES[ProjectBlocks.SPARSE_DRY_GRASS.get()
      .asItem()] = 0.3f
    COMPOSTABLES[ProjectBlocks.TALL_SPARSE_DRY_GRASS.get()
      .asItem()] = 0.3f
    COMPOSTABLES[ProjectBlocks.OCOTILLO.get()
      .asItem()] = 0.3f
    COMPOSTABLES[ProjectBlocks.DRY_PATCHES.get()
      .asItem()] = 0.3f
    COMPOSTABLES[ProjectBlocks.DRY_SHRUB.get()
      .asItem()] = 0.3f
  }

  fun register() {
    ComposterBlock.COMPOSTABLES.putAll(COMPOSTABLES)
  }
}