package com.dannbrown.databoxlib.content.item

import net.minecraft.world.item.ItemNameBlockItem
import net.minecraft.world.level.block.Block

class FuelItemFromBlock(block:  Block, props: Properties, private val burnTime: Int): ItemNameBlockItem(block, props) {
  override fun getBurnTime(itemStack: net.minecraft.world.item.ItemStack?, recipeType: net.minecraft.world.item.crafting.RecipeType<*>?): Int {
    return burnTime
  }
}