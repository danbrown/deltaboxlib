package com.dannbrown.databoxlib.datagen.recipes

import com.dannbrown.databoxlib.init.ProjectBlocks
import com.dannbrown.databoxlib.init.ProjectItems
import com.simibubi.create.AllItems
import net.minecraft.data.DataGenerator
import net.minecraft.world.item.Items

class SplashingRecipeGen(generator: DataGenerator) : ProjectRecipeGen(generator) {
  override val recipeName = "Splashing"
  val PROCESS_MOON_REGOLITH = create("process_moon_regolith") { b ->
    b
      .splashing { b ->
        b
          .require(ProjectBlocks.MOON_REGOLITH.get())
          .output(0.5f, Items.FLINT, 1)
          .output(0.125f, ProjectItems.ALUMINIUM_NUGGET.get(), 1)
      }
  }
  val SILICA_GRAINS_FROM_SILT = create("silica_grains_from_silt") { b ->
    b
      .splashing { b ->
        b
          .require(ProjectBlocks.SILT.get())
          .output(0.05f, ProjectItems.SILICA_GRAINS.get(), 1)
          .output(0.125f, AllItems.COPPER_NUGGET.get(), 1)
          .output(0.5f, Items.CLAY_BALL, 1)
      }
  }
  val HIMALAYAN_SALT_FROM_ROSEATE_GRAINS = create("himalayan_salt_from_roseate_grains") { b ->
    b
      .splashing { b ->
        b
          .require(ProjectBlocks.ROSEATE_GRAINS.get())
          .output(0.5f, ProjectItems.HIMALAYAN_SALT.get(), 1)
          .output(0.5f, Items.CLAY_BALL, 1)
      }
  }
}