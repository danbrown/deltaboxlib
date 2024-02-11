package com.dannbrown.databoxlib.content.item

import net.minecraft.world.item.Item
import net.minecraft.world.item.Item.Properties
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import javax.annotation.Nullable


class FuelItem(properties: Properties?, private val burnTime: Int) : Item(properties) {
  override fun getBurnTime(itemStack: ItemStack?, @Nullable recipeType: RecipeType<*>?): Int {
    return burnTime
  }
}