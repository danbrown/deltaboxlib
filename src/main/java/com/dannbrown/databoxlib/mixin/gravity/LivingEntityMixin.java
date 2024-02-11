package com.dannbrown.databoxlib.mixin.gravity;

import com.dannbrown.databoxlib.content.utils.GravityManager;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

  @Inject(method = "net.minecraft.world.entity.LivingEntity.travel", at = @At("TAIL"))
  public void databoxlib$travel(Vec3 pTravelVector, CallbackInfo ci) {
    LivingEntity entity = (LivingEntity) (Object) this;

    if (!entity.isNoGravity() && !entity.isInWater() && !entity.isInLava() && !entity.isFallFlying() && !entity.hasEffect(MobEffects.SLOW_FALLING)) {
      GravityManager.applyEntityTickGravity(entity, -0.08);
    }
  }

  @Inject(method = "net.minecraft.world.entity.LivingEntity.calculateFallDamage", at = @At("HEAD"), cancellable = true)
  public void databoxlib$calculateFallDamage(float pFallDistance, float pDamageMultiplier, CallbackInfoReturnable<Integer> cir) {
    LivingEntity entity = (LivingEntity) (Object) this;
    if (entity.getType().is(EntityTypeTags.FALL_DAMAGE_IMMUNE)) {
      cir.setReturnValue(0);
    } else {
      MobEffectInstance mobeffectinstance = entity.getEffect(MobEffects.JUMP);
      float f = mobeffectinstance == null ? 0.0F : (float) (mobeffectinstance.getAmplifier() + 1);
      cir.setReturnValue((int) (Mth.ceil((pFallDistance - 3.0F - f) * pDamageMultiplier) * GravityManager.INSTANCE.getEntityGravity(entity)));
    }
  }

}


//  // Make fall damage gravity-dependant
//  @ModifyVariable(method = "causeFallDamage", at = @At("HEAD"), ordinal = 1, argsOnly = true)
//  private float databoxlib$causeFallDamage(float fallDistance, float damageMultiplier) {
//    LivingEntity entity = ((LivingEntity) (Object) this);
//    float tolerableFallDistance = entity.fallDistance;
//    float gravitationalFallDistance = tolerableFallDistance * 0.8F;
//
//    // nullify the damage if the fall distance is less than the gravitational fall distance
//    if (fallDistance < gravitationalFallDistance) {
//      return 0;
//    }
//
//    return damageMultiplier * GravityManager.INSTANCE.getEntityGravity(entity);
//  }
//}
