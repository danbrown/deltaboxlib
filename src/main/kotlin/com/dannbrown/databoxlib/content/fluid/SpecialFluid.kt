package com.dannbrown.databoxlib.content.fluid

import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.phys.Vec3
import net.minecraftforge.fluids.ForgeFlowingFluid
import java.util.*

abstract class SpecialFluid(properties: Properties, genProps: FluidPropertiesExtended) : ForgeFlowingFluid(properties) {

  val genProps: FluidPropertiesExtended = genProps

//  // Instantiations
//  var _isSticky: Boolean = false
//  fun sticky(): FluidGen {
//    _isSticky = true
//    return this
//  }

  // Overrides
  override fun isRandomlyTicking(): Boolean {
    return true
  }

  override fun getPickupSound(): Optional<SoundEvent> {
    return Optional.of(SoundEvents.BUCKET_FILL_POWDER_SNOW)
  }

  override fun move(state: FluidState?, entity: LivingEntity, movementVector: Vec3?, gravity: Double): Boolean {
    val level = entity!!.level()
    val entityPos = entity.blockPosition()
    val fluidBlockState: BlockState = state!!.createLegacyBlock()

    actionHandleSticky(entity, level, fluidBlockState)
    actionHandleCryogenic(entity, level, entityPos)
    actionHandleHarmful(entity, level)
    actionHandleHot(entity, level, entityPos)
    actionHandleGiveEffect(entity)

    return super.move(state, entity, movementVector, gravity)
  }

  // Actions
  private fun actionHandleSticky(entity: LivingEntity, level: Level, fluidBlockState: BlockState) {
    if(genProps._isSticky){
      if (entity is LivingEntity) {
        entity.makeStuckInBlock(fluidBlockState, Vec3(0.9, 1.5, 0.9))
        if (!level.isClientSide) {
          entity.setSharedFlagOnFire(false)
        }
      }
    }
  }
  private fun actionHandleCryogenic(entity: LivingEntity, level: Level, entityPos: BlockPos) {
    if(genProps._isCryogenic) {
      if (entity is LivingEntity) {
        if (level.isClientSide) {
          val random: RandomSource = level.getRandom()
          val bl = entity.xOld !== entity.x || entity.zOld !== entity.z
          if (bl && random.nextBoolean()) {
            level.addParticle(
              ParticleTypes.SNOWFLAKE,
              entity.x,
              (entityPos.y + 1).toDouble(),
              entity.z,
              (Mth.randomBetween(random, -1.0f, 1.0f) * 0.083333336f).toDouble(),
              0.05,
              (Mth.randomBetween(random, -1.0f, 1.0f) * 0.083333336f).toDouble()
            )
          }
        }
        entity.setIsInPowderSnow(true)
        entity.ticksFrozen = entity.ticksRequiredToFreeze.coerceAtMost(entity.ticksFrozen + 5)
        if (!level.isClientSide) {
          entity.setSharedFlagOnFire(false)
        }
      }
    }
  }
  private fun actionHandleHarmful(entity: LivingEntity, level: Level) {
    if(genProps._isHarmful) {
      if (entity is LivingEntity) {
        if (!level.isClientSide) {
          val damageSources = level.damageSources()
          entity.setSharedFlagOnFire(false)
          entity.hurt(damageSources.generic(), genProps._baseDamage)
        }
      }
    }
  }
  private fun actionHandleHot(entity: LivingEntity, level: Level, entityPos: BlockPos) {
    if(genProps._isHot) {
      if (entity is LivingEntity) {
        if (level.isClientSide) {
          val random: RandomSource = level.getRandom()
          val bl = entity.xOld !== entity.x || entity.zOld !== entity.z
          if (bl && random.nextBoolean()) {
            level.addParticle(
              ParticleTypes.SMOKE,
              entity.x,
              (entityPos.y + 1).toDouble(),
              entity.z,
              (Mth.randomBetween(random, -1.0f, 1.0f) * 0.083333336f).toDouble(),
              0.05,
              (Mth.randomBetween(random, -1.0f, 1.0f) * 0.083333336f).toDouble()
            )
          }
        }
        // set on fire
        entity.setSecondsOnFire(5)
        if (!level.isClientSide) {
          entity.setSharedFlagOnFire(true)
        }
      }
    }
  }
  private fun actionHandleGiveEffect(entity: LivingEntity){
    if(genProps._giveEffect){
      if (entity is LivingEntity) {
        for(effect in genProps._effect){
          entity.addEffect(MobEffectInstance(effect, genProps._effectDuration, genProps._effectAmplifier))
        }
      }
    }
  }

  class Flowing(properties: Properties, genProps: FluidPropertiesExtended) : SpecialFluid(properties, genProps) {
    init {
      registerDefaultState(getStateDefinition().any().setValue(LEVEL, 7))
    }

    override fun createFluidStateDefinition(builder: StateDefinition.Builder<Fluid, FluidState>) {
      super.createFluidStateDefinition(builder)
      builder.add(LEVEL)
    }

    override fun getAmount(state: FluidState): Int {
      return state.getValue(LEVEL)
    }

    override fun isSource(state: FluidState): Boolean {
      return false
    }
  }

  class Source(properties: Properties, genProps: FluidPropertiesExtended) : SpecialFluid(properties, genProps) {
    override fun getAmount(state: FluidState): Int {
      return 8
    }

    override fun isSource(state: FluidState): Boolean {
      return true
    }
  }
}