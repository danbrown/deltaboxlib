package com.dannbrown.databoxlib.datagen.recipes

import com.dannbrown.databoxlib.init.ProjectBlocks
import com.tterrag.registrate.util.DataIngredient
import net.minecraft.data.DataGenerator
import net.minecraft.world.level.block.Blocks

class CookingRecipeGen(generator: DataGenerator) : ProjectRecipeGen(generator) {
  override val recipeName = "Cooking"

  val SILT_TO_GLASS = cooking({ DataIngredient.items(ProjectBlocks.SILT.get()) }, { Blocks.GLASS }) { b ->
    b
      .prefix("silt_to_glass_")
      .inFurnace(100, 0.1f)
  }

  val ROSEATE_GRAINS_TO_GLASS =
    cooking({ DataIngredient.items(ProjectBlocks.ROSEATE_GRAINS.get()) }, { Blocks.GLASS }) { b ->
      b
        .prefix("roseate_grains_to_glass_")
        .inFurnace(100, 0.1f)
    }

  val MOON_REGOLITH_TO_GLASS =
    cooking({ DataIngredient.items(ProjectBlocks.MOON_REGOLITH.get()) }, { Blocks.GLASS }) { b ->
      b
        .prefix("moon_regolith_to_glass_")
        .inFurnace(100, 0.1f)
    }


  val BROWN_SLATE_FROM_COBBLESTONE =
    cooking({ DataIngredient.items(ProjectBlocks.COBBLED_BROWN_SLATE.get()) }, { ProjectBlocks.BROWN_SLATE.get() }) { b ->
      b
        .prefix("brown_slate_from_cobblestone_")
        .comboOreSmelting(100, 0.1f)
    }

  val RED_HEMATITE_FROM_COBBLESTONE =
    cooking(
      { DataIngredient.items(ProjectBlocks.COBBLED_RED_HEMATITE.get()) },
      { ProjectBlocks.RED_HEMATITE.get() }) { b ->
      b
        .prefix("red_hematite_from_cobblestone_")
        .comboOreSmelting(100, 0.1f)
    }


}