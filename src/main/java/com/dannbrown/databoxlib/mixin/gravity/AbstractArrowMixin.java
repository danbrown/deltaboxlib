package com.dannbrown.databoxlib.mixin.gravity;

import com.dannbrown.databoxlib.content.utils.GravityManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {
//  @Unique
//  private static final double CONSTANT = 0.05;

  @Inject(method = "tick", at = @At("TAIL"))
  public void databoxlib$tick(CallbackInfo ci) {
    Entity entity = (Entity) (Object) this;
    GravityManager.applyEntityTickGravity(entity, -0.05);
//    if (!entity.isNoGravity()) {
//      Vec3 velocity = entity.getDeltaMovement();
//      double newGravity = CONSTANT * GravityManager.INSTANCE.getEntityGravity(entity);
//      entity.setDeltaMovement(velocity.x(), velocity.y() + CONSTANT - newGravity, velocity.z());
//    }
  }
}