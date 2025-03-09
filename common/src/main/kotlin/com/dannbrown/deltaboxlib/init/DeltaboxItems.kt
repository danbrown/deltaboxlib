package com.dannbrown.deltaboxlib.init

import net.minecraft.world.item.Item
import com.dannbrown.deltaboxlib.init.DeltaboxLibMod.REGISTRATE
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import java.util.function.Supplier

object DeltaboxItems {
  val ADAMANTIUM_INGOT = REGISTRATE
    .item<Item>("adamantium_ingot")
    .register()

  val WARP_CRYSTAL = REGISTRATE.item<Item>("warp_crystal")
    .recipe { r, i ->
      r.simpleShapedRecipe(
        { i.get() },
        arrayOf("GSG", "SBS", "GSG"),
        mapOf(
          'G' to Supplier { Ingredient.of(Items.GLASS) },
          'S' to Supplier { Ingredient.of(Items.STICK) },
          'B' to Supplier { Ingredient.of(Items.BLAZE_POWDER) }
        )
      )
    }
    .register()

  val BEAN_POD = REGISTRATE.item<Item>("bean_pod").register()

  fun register() {
    // init
  }
}