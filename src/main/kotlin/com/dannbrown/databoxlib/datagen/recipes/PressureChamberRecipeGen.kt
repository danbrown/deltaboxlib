package com.dannbrown.databoxlib.datagen.recipes

import com.dannbrown.databoxlib.content.utils.ProjectHeat
import com.dannbrown.databoxlib.init.ProjectFluids
import com.dannbrown.databoxlib.init.ProjectItems
import com.dannbrown.databoxlib.lib.LibTags
import com.simibubi.create.AllFluids
import net.minecraft.data.DataGenerator
import net.minecraft.world.item.Items

class PressureChamberRecipeGen(generator: DataGenerator) : ProjectRecipeGen(generator) {
  override val recipeName = "Pressure Chamber"
  val HONEY_FROM_SUGAR = databoxlib("honey_from_sugar_blocks") { b ->
    b
      .pressureChamber() { b ->
        b
          .require(LibTags.forgeItemTag("storage_blocks/sugar"))
          .require(LibTags.forgeItemTag("storage_blocks/sugar"))
          .require(LibTags.forgeItemTag("storage_blocks/sugar"))
          .duration(200)
          .requiresHeat(ProjectHeat.PASSIVEHEATED)
          .output(AllFluids.HONEY.get(), 25)
      }
  }
  val CONDENSED_LATEX_FROM_LATEX = databoxlib("condensed_latex_from_latex") { b ->
    b
      .pressureChamber() { b ->
        b
          .require(ProjectFluids.LATEX.get(), 250)
          .requiresHeat(ProjectHeat.PASSIVEHEATED)
          .duration(200)
          .output(ProjectItems.CONDENSED_LATEX.get(), 1)
      }
  }
  val QUARTZ_FROM_SILICA_CRYSTAL = databoxlib("quartz_from_silica_crystal") { b ->
    b
      .pressureChamber() { b ->
        b
          .require(ProjectItems.SILICA_CRYSTAL.get())
          .requiresHeat(ProjectHeat.PASSIVEHEATED)
          .duration(200)
          .output(Items.QUARTZ, 1)
      }
  }
}