package com.dannbrown.deltaboxlib.content.item.arrow

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.AbstractArrow
import net.minecraft.world.item.ArrowItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

open class BaseArrowItem(
  props: Properties,
  val arrowInstance: (Level, LivingEntity, itemsStack: ItemStack) -> AbstractArrow
) : ArrowItem(props) {
  override fun createArrow(level: Level, itemStack: ItemStack, livingEntity: LivingEntity): AbstractArrow {
    return arrowInstance(level, livingEntity, itemStack)
  }
}
