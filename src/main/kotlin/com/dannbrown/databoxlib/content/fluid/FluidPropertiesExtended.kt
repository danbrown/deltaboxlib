package com.dannbrown.databoxlib.content.fluid

import net.minecraft.resources.ResourceKey
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageSources
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.material.Fluid
import net.minecraftforge.fluids.FluidType
import net.minecraftforge.fluids.ForgeFlowingFluid
import java.time.Duration
import java.util.function.Supplier

class FluidPropertiesExtended() {
  var _isSticky: Boolean = false
  var _isCryogenic: Boolean = false
  var _isHot: Boolean = false
  var _isHarmful: Boolean = false
  var _baseDamage: Float = 0f
//  var _damageSource: DamageSource = DamageSources().generic()
  var _giveEffect: Boolean = false
  var _effect: List<MobEffect> = listOf()
  var _effectDuration: Int = 5 * 20 // 5 seconds
  var _effectAmplifier: Int = 0 // 0 = level 1
  fun sticky(): FluidPropertiesExtended {
    _isSticky = true
    return this
  }

  fun cryogenic(): FluidPropertiesExtended {
    _isCryogenic = true
    return this
  }

  fun harmful(
//    damageSource: DamageSource = _damageSource,
    damage: Float = _baseDamage): FluidPropertiesExtended {
    _isHarmful = true
    _baseDamage = damage
//    _damageSource = damageSource
    return this
  }

  fun hot(): FluidPropertiesExtended {
    _isHot = true
    return this
  }

  fun giveEffect(effect: List<MobEffect>, duration: Int = _effectDuration, amplifier: Int = _effectAmplifier): FluidPropertiesExtended {
    _giveEffect = true
    _effect = effect
    _effectDuration = duration
    _effectAmplifier = amplifier
    return this
  }


}

