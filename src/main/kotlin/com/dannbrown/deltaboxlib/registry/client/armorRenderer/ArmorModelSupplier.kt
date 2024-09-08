package com.dannbrown.deltaboxlib.registry.client.armorRenderer

import net.minecraft.client.model.geom.ModelPart
import net.minecraft.world.entity.LivingEntity

fun interface ArmorModelSupplier {
  fun create(root: ModelPart): AbstractArmorModel<LivingEntity>
}