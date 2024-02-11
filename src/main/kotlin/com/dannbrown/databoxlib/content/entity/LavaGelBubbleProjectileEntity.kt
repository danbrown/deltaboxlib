package com.dannbrown.databoxlib.content.entity

import com.dannbrown.databoxlib.init.ProjectEntityTypes
import com.dannbrown.databoxlib.init.ProjectItems
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

class LavaGelBubbleProjectileEntity : ThrowableItemProjectile {
  constructor(pEntityType: EntityType<out ThrowableItemProjectile>, pLevel: Level) : super(pEntityType, pLevel)
  constructor(pLevel: Level) : super(ProjectEntityTypes.LAVA_GEL_BUBBLE_PROJECTILE.get(), pLevel)
  constructor(pLevel: Level, livingEntity: LivingEntity) : super(
    ProjectEntityTypes.LAVA_GEL_BUBBLE_PROJECTILE.get(),
    livingEntity,
    pLevel
  )
  constructor(level: Level, x: Double, y: Double, z: Double) : super(
    ProjectEntityTypes.LAVA_GEL_BUBBLE_PROJECTILE.get(), x, y, z, level
  )

  override fun getDefaultItem(): Item {
    return ProjectItems.LAVA_GEL_BUBBLE.get()
  }

  override fun onHitBlock(pResult: BlockHitResult) {
    // ensure server side
    if (!this.level().isClientSide()) {
      if(!this.isRemoved
        && pResult.type == HitResult.Type.BLOCK
        && this.level().getBlockState(blockPosition()).isAir
      ) {
        this.level().broadcastEntityEvent(this, 3.toByte())
        // play sound
        this.level().playSound(null, pResult.blockPos.x.toDouble(), pResult.blockPos.y.toDouble(), pResult.blockPos.z.toDouble(),
          SoundEvents.BUCKET_EMPTY_LAVA, SoundSource.NEUTRAL, 0.5f, 0.4f / (this.level().getRandom().nextFloat() * 0.4f + 0.8f)
        )
        this.level().setBlock(blockPosition(), Blocks.LAVA.defaultBlockState(), 3)
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