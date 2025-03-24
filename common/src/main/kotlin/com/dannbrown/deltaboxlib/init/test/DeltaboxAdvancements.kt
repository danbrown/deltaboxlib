package com.dannbrown.deltaboxlib.init.test

import com.dannbrown.deltaboxlib.init.DeltaboxLibMod
import com.dannbrown.deltaboxlib.init.DeltaboxLibMod.REGISTRATE
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import net.minecraft.advancements.RequirementsStrategy
import net.minecraft.world.item.Items

object DeltaboxAdvancements {
//
//  private val SAPLINGS = arrayOf(
//    Items.BAMBOO,
//    Items.ACACIA_BUTTON,
//    Items.AMETHYST_SHARD
//  )
//
//  val SAMPLE_ROOT = REGISTRATE.advancement("root", "Brazilian Delight", "Welcome to Brazilian Delight!", { k, u, b ->
//    u.hasItemsCriterion(
//      u.basicAdvancement(
//        DeltaboxItems.ADAMANTIUM_INGOT.get(),
//        k,
//        DeltaboxUtil.resourceLocation(DeltaboxLibMod.MOD_ID, "textures/block/lemon_leaves.png")
//      ),
//      k,
//      RequirementsStrategy.OR,
//      *SAPLINGS,
//    )
//  })
//
//  val TROPICAL_SEEDS =
//    REGISTRATE.advancement("tropical_seeds", "Tropical Seeds", "Obtain any seed from Brazilian Delight", { k, u, b ->
//      u.hasItemsCriterion(
//        u.basicAdvancement(
//          Items.AMETHYST_SHARD,
//          k
//        ).parent(SAMPLE_ROOT.get()),
//        k,
//        RequirementsStrategy.OR,
//        *SAPLINGS,
//      )
//    })

  fun register() {
    // init
  }
}