package com.dannbrown.databoxlib.mixin.oxygen;

import com.dannbrown.databoxlib.content.utils.OxygenManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

//  @Shadow
//   public abstract Level level();


//  public abstract ServerLevel serverLevel();

  @Inject(method = {"tick"}, at = {@At("TAIL")}, require = 1)
  public void tick(CallbackInfo ci) throws InstantiationException, IllegalAccessException {
    LivingEntity entity = (LivingEntity) (Object) this;
    Level level = entity.level();
    OxygenManager.spaceBreathing(entity, level);
  }
}
