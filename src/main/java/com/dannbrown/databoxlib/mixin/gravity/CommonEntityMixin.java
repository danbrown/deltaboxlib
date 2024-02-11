package com.dannbrown.databoxlib.mixin.gravity;

import com.dannbrown.databoxlib.content.utils.GravityManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {AbstractMinecart.class, ItemEntity.class, PrimedTnt.class})
public abstract class CommonEntityMixin {

  @Inject(method = "tick", at = @At("TAIL"))
  public void databoxlib$tick(CallbackInfo ci) {
    Entity entity = (Entity) (Object) this;
    GravityManager.applyEntityTickGravity(entity, -0.04);
//    if (!entity.isNoGravity()) {
//      Vec3 velocity = entity.getDeltaMovement();
//      double newGravity = CONSTANT * GravityManager.INSTANCE.getEntityGravity(entity);
//      entity.setDeltaMovement(velocity.x(), velocity.y() - CONSTANT + newGravity, velocity.z());
//    }
  }
}