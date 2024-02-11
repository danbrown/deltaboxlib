package com.dannbrown.databoxlib.init

import com.dannbrown.databoxlib.lib.LibData
import com.dannbrown.databoxlib.lib.LibUtils
import net.minecraft.world.item.Tiers
import net.minecraft.world.item.crafting.Ingredient
import net.minecraftforge.common.ForgeTier
import net.minecraftforge.common.TierSortingRegistry

object ProjectToolTiers {
  val TUNGSTEN = TierSortingRegistry.registerTier(
    ForgeTier(
      5, 2500, 12f, 4.5f, 15,
      ProjectTags.BLOCK.NEEDS_TUNGSTEN_TOOL,
    ) { Ingredient.of(ProjectItems.TUNGSTEN_INGOT.get()) },
    LibUtils.resourceLocation(LibData.NAMES.TUNGSTEN), listOf(Tiers.NETHERITE), listOf()
  )
}