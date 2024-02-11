package com.dannbrown.databoxlib.mixin.gravity;

import com.dannbrown.databoxlib.content.utils.GravityManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ThrownExperienceBottle.class)
public abstract class ThrownExperienceBottleMixin {
  @Inject(method = "getGravity", at = @At("HEAD"), cancellable = true)
  public void databoxlib$getGravity(CallbackInfoReturnable<Float> cir) {
    cir.setReturnValue(0.07f * GravityManager.INSTANCE.getEntityGravity((Entity) (Object) this));
  }
}