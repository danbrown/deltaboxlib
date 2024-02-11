package com.dannbrown.databoxlib.content.entity

import com.dannbrown.databoxlib.init.ProjectEntityTypes
import com.dannbrown.databoxlib.init.ProjectItems
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.ThrowableItemProjectile
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult

class WaterGelBubbleProjectileEntity : ThrowableItemProjectile {
  constructor(pEntityType: EntityType<out ThrowableItemProjectile>, pLevel: Level) : super(pEntityType, pLevel)
  constructor(pLevel: Level) : super(ProjectEntityTypes.WATER_GEL_BUBBLE_PROJECTILE.get(), pLevel)
  constructor(pLevel: Level, livingEntity: LivingEntity) : super(
    ProjectEntityTypes.WATER_GEL_BUBBLE_PROJECTILE.get(),
    livingEntity,
    pLevel
  )

  constructor(level: Level, x: Double, y: Double, z: Double) : super(
    ProjectEntityTypes.WATER_GEL_BUBBLE_PROJECTILE.get(), x, y, z, level
  )

  override fun getDefaultItem(): Item {
    return ProjectItems.WATER_GEL_BUBBLE.get()
  }

  override fun onHitBlock(pResult: BlockHitResult) {
    // ensure server side
    if (!this.level().isClientSide()){

      // if the bubble hits a block and the block is air, replace it with water
      if (!this.isRemoved
        && pResult.type == HitResult.Type.BLOCK
        && this.level().getBlockState(blockPosition()).isAir
      ) {
        // dont place water in worlds where water is disabled (e.g. ultra warm)
        if (!this.level().dimensionType().ultraWarm){
          this.level().broadcastEntityEvent(this, 3.toByte())
          // play sound
          this.level().playSound(null, pResult.blockPos.x.toDouble(), pResult.blockPos.y.toDouble(), pResult.blockPos.z.toDouble(),
            SoundEvents.BUCKET_EMPTY, SoundSource.NEUTRAL, 0.5f, 0.4f / (this.level().getRandom().nextFloat() * 0.4f + 0.8f)
          )
          this.level().setBlock(blockPosition(), Blocks.WATER.defaultBlockState(), 3)
        }

        // play water evaporation sound and place particles
        else {
          val i: Int = blockPosition().x
          val j: Int = blockPosition().y
          val k: Int = blockPosition().z
          this.level().playSound(
            null,
            pResult.blockPos.x.toDouble(), pResult.blockPos.y.toDouble(), pResult.blockPos.z.toDouble(),
            SoundEvents.FIRE_EXTINGUISH,
            SoundSource.BLOCKS,
            0.5f,
            2.6f + (this.level().random.nextFloat() - this.level().random.nextFloat()) * 0.8f
          )

          // spawn steam cloud
          for (l in 0..7) {
            this.level().addParticle(
              ParticleTypes.LARGE_SMOKE,
              i.toDouble() + Math.random(),
              j.toDouble() + Math.random(),
              k.toDouble() + Math.random(),
              0.0,
              0.0,
              0.0
            )
          }
        }
      }
      // drop item if it hits any other block
      else {
        this.spawnAtLocation(this.defaultItem)
      }
    }

    discard()
    super.onHitBlock(pResult)
  }
}