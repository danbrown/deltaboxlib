package com.dannbrown.databoxlib.datagen.recipes

import com.tterrag.registrate.util.DataIngredient
import net.minecraft.data.DataGenerator
import net.minecraft.world.level.block.Blocks

class PlaceholderRecipeGen(generator: DataGenerator): ProjectRecipeGen(generator) {
  override val recipeName = "Placeholder"

  // create a cobblestone smelt recipe
  val COMPAT = cooking({ DataIngredient.items(Blocks.COBBLESTONE) }, { Blocks.STONE }) { b -> b
    .suffix("_from_cobblestone_compat")
    .inBlastFurnace(200, 0f)
  }

}