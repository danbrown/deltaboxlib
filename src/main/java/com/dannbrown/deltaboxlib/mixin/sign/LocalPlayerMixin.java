package com.dannbrown.deltaboxlib.mixin.sign;

import com.dannbrown.deltaboxlib.content.blockEntity.GenericHangingSignBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.HangingSignEditScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( LocalPlayer.class )
public class LocalPlayerMixin {

  @Shadow
  protected final Minecraft minecraft = Minecraft.getInstance();

  @Inject(at = @At( "HEAD" ), method = "openTextEdit", cancellable = true)
  private void openTextEdit(SignBlockEntity pSignEntity, boolean pIsFrontText, CallbackInfo ci) {
    if (pSignEntity instanceof GenericHangingSignBlockEntity hangingsignblockentity) {
      this.minecraft.setScreen(new HangingSignEditScreen(hangingsignblockentity, pIsFrontText, this.minecraft.isTextFilteringEnabled()));
      ci.cancel();
    }
  }
}
