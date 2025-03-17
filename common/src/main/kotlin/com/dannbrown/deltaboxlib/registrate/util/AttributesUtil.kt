package com.dannbrown.deltaboxlib.registrate.util

import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes

object AttributesUtil {
  fun createLivingAttributes(): AttributeSupplier.Builder {
    return AttributeSupplier.builder()
      .add(Attributes.MAX_HEALTH)
      .add(Attributes.KNOCKBACK_RESISTANCE)
      .add(Attributes.MOVEMENT_SPEED)
      .add(Attributes.ARMOR)
      .add(Attributes.ARMOR_TOUGHNESS)
  }

  fun createMobAttributes(): AttributeSupplier.Builder {
    return createLivingAttributes().add(Attributes.FOLLOW_RANGE, 16.0).add(Attributes.ATTACK_KNOCKBACK)
  }

  fun createMonsterAttributes(): AttributeSupplier.Builder {
    return createMobAttributes().add(Attributes.ATTACK_DAMAGE);
  }
}