package com.dannbrown.databoxlib.mixin.gravity;

import com.dannbrown.databoxlib.content.utils.GravityManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Boat.class)
public abstract class BoatMixin {


  @Inject(method = "floatBoat", at = @At("TAIL"))
  public void databoxlib$updateVelocity(CallbackInfo ci) {
    Entity entity = (Entity) (Object) this;
    GravityManager.applyEntityTickGravity(entity, -0.04);
//    if (!entity.isNoGravity()) {
//      Vec3 velocity = entity.getDeltaMovement();
//      double newGravity = CONSTANT * GravityManager.INSTANCE.getEntityGravity(entity);
//      entity.setDeltaMovement(velocity.x(), velocity.y() - CONSTANT + newGravity, velocity.z());
//    }
  }
}