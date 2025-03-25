package com.dannbrown.deltaboxlib.registrate.util

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.monster.Monster

object AttributesUtil {
  fun createLivingAttributes(): AttributeSupplier.Builder {
    return LivingEntity.createLivingAttributes()
  }

  fun createMobAttributes(): AttributeSupplier.Builder {
    return Mob.createMobAttributes()
  }

  fun createMonsterAttributes(): AttributeSupplier.Builder {
    return Monster.createMonsterAttributes()
  }
}