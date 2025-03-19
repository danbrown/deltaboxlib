package com.dannbrown.deltaboxlib.init.test

import com.dannbrown.deltaboxlib.content.entity.boat.BaseBoatEntity
import com.dannbrown.deltaboxlib.content.entity.boat.BaseBoatRenderer
import com.dannbrown.deltaboxlib.content.entity.boat.BaseChestBoatEntity
import com.dannbrown.deltaboxlib.content.item.BoatItem
import net.minecraft.world.item.Item
import com.dannbrown.deltaboxlib.init.DeltaboxLibMod.REGISTRATE
import com.dannbrown.deltaboxlib.registrate.registry.ItemEntry
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import java.util.function.Supplier

object DeltaboxItems {
//  val ADAMANTIUM_INGOT = REGISTRATE
//    .item<Item>("adamantium_ingot")
//    .register()
//
//  val WARP_CRYSTAL = REGISTRATE.item<Item>("warp_crystal")
//    .recipe { r, i ->
//      r.simpleShapedRecipe(
//        { i.get() },
//        arrayOf("GSG", "SBS", "GSG"),
//        mapOf(
//          'G' to Supplier { Ingredient.of(Items.GLASS) },
//          'S' to Supplier { Ingredient.of(Items.STICK) },
//          'B' to Supplier { Ingredient.of(Items.BLAZE_POWDER) }
//        )
//      )
//    }
//    .register()
//
//  val BEAN_POD = REGISTRATE.item<Item>("bean_pod")
//    .compostable(0.3f)
//    .register()

  fun register() {
    REGISTRATE.buildItems()
  }
}