package com.dannbrown.databoxlib.datagen.recipes

import com.dannbrown.databoxlib.init.ProjectFluids
import net.minecraft.data.DataGenerator
import net.minecraft.world.level.material.Fluids

class ElectrolysisRecipeGen(generator: DataGenerator): ProjectRecipeGen(generator) {
  override val recipeName = "Electrolysis"

  val OXYGEN_HYDROGEN_FROM_WATER = databoxlib("h_o_from_water"){ b -> b
    .electrolysis { b -> b
      .require(Fluids.WATER, 1000)
      .output(ProjectFluids.HYDROGEN.get(), 100)
      .output(ProjectFluids.OXYGEN.get(), 100)
    }
  }
}