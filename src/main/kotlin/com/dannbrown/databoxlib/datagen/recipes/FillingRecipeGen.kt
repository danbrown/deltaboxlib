package com.dannbrown.databoxlib.datagen.recipes

import com.dannbrown.databoxlib.init.ProjectFluids
import com.dannbrown.databoxlib.init.ProjectItems
import com.dannbrown.databoxlib.lib.LibTags
import net.minecraft.data.DataGenerator
import net.minecraft.world.item.Items

class FillingRecipeGen(generator: DataGenerator) : ProjectRecipeGen(generator) {
  override val recipeName = "Filling"

  val INK_SAC_FROM_LIQUID_INK = create("ink_sac_from_liquid_ink") { b ->
    b.filling() { b ->
      b.require(LibTags.forgeFluidTag("ink"), 100)
        .require(ProjectItems.CONDENSED_LATEX.get())
        .output(Items.INK_SAC)
    }
  }

  val GLOW_INK_SAC_FROM_LIQUID_GLOW_INK = create("glow_ink_sac_from_liquid_glow_ink") { b ->
    b.filling() { b ->
      b.require(ProjectFluids.GLOW_INK.get(), 100)
        .require(ProjectItems.CONDENSED_LATEX.get())
        .output(Items.GLOW_INK_SAC)
    }
  }

}