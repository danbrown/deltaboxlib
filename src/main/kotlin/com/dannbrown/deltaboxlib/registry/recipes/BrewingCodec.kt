package com.dannbrown.deltaboxlib.registry.recipes

import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraft.world.item.alchemy.Potions

class BrewingCodec(
  val inputItem: ItemStack,
  val ingredientItem: ItemStack,
  val outputItem: ItemStack,
) {
  companion object {
    val CODEC = RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<BrewingCodec> ->
      instance.group(
        ItemStack.CODEC
          .fieldOf("input")
          .forGetter(BrewingCodec::inputItem),
        ItemStack.CODEC
          .fieldOf("ingredient")
          .forGetter(BrewingCodec::ingredientItem),
        ItemStack.CODEC
          .fieldOf("output")
          .forGetter(BrewingCodec::outputItem)
      )
        .apply(instance, ::BrewingCodec)
    }
    val RECIPES: MutableList<BrewingCodec> = mutableListOf(
      BrewingCodec(
        PotionUtils.setPotion(ItemStack(Items.POTION), Potions.AWKWARD),
        ItemStack(Items.SHULKER_SHELL),
        PotionUtils.setCustomEffects(ItemStack(Items.POTION), listOf(MobEffectInstance(MobEffects.SLOW_FALLING, 6000, 0)))
      )
    )
    fun updateData(heads: List<BrewingCodec>) {
      RECIPES.clear()
      RECIPES.addAll(heads)
    }
  }
}