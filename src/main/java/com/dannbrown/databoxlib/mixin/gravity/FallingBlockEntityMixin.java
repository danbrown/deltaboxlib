package com.dannbrown.databoxlib.mixin.gravity;

import com.dannbrown.databoxlib.content.utils.GravityManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(value = {FallingBlockEntity.class})
public abstract class FallingBlockEntityMixin {

  @Inject(method = "tick", at = @At("TAIL"))
  public void databoxlib$tick(CallbackInfo ci) {
    Entity entity = (Entity) (Object) this;
    GravityManager.applyEntityTickGravity(entity, -0.03);
  }
}