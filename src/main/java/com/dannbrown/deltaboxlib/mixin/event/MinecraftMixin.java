package com.dannbrown.deltaboxlib.mixin.event;

import com.dannbrown.deltaboxlib.content.event.OnClientTick;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( Minecraft.class )
public class MinecraftMixin {
  @Inject(at = @At( "TAIL" ), method = "tick ()V")
  private void tick( CallbackInfo callback ) {
  }
}
