package com.dannbrown.deltaboxlib.mixin.event;

import com.dannbrown.deltaboxlib.content.event.OnServerTick;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin( MinecraftServer.class )
public abstract class MinecraftServerMixin {
  @Inject(
    at = @At(shift = At.Shift.AFTER, target = "Lnet/minecraft/server/MinecraftServer;tickChildren (Ljava/util/function/BooleanSupplier;)V", value = "INVOKE"),
    method = "tickServer (Ljava/util/function/BooleanSupplier;)V"
  )
  private void tickServer(BooleanSupplier haveTime, CallbackInfo callback) {
    OnServerTick.INSTANCE.dispatch();
  }
}