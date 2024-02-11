package com.dannbrown.databoxlib.mixin.oxygen;

import com.dannbrown.databoxlib.ProjectContent;
import com.dannbrown.databoxlib.content.utils.OxygenManager;
import com.simibubi.create.content.equipment.armor.BacktankBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = BacktankBlockEntity.class, remap = false)
public class BacktankBlockEntityMixin extends KineticBlockEntity {

  public BacktankBlockEntityMixin(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
    super(typeIn, pos, state);
  }

  // if it is on a world that has no oxygen, then don't fill the tank (just return early)
  @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
  public void databoxlib$denyBacktankLoad(CallbackInfo ci) {
    BacktankBlockEntity backtank = (BacktankBlockEntity) (Object) this;
    Level level = backtank.getLevel();
    if (level != null && !OxygenManager.INSTANCE.levelHasOxygen(level)) {
      ci.cancel();
    }
  }

  // display a message in the tooltip warning the player when oxygen is not present
  @Override
  public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
    super.addToGoggleTooltip(tooltip, isPlayerSneaking);
    BacktankBlockEntity backtank = (BacktankBlockEntity) (Object) this;
    Level level = backtank.getLevel();
    float stressTotal = calculateStressApplied() * Math.abs(getTheoreticalSpeed());
    if (level != null && !OxygenManager.INSTANCE.levelHasOxygen(level) && stressTotal > 0.0f) {
      Lang.builder(ProjectContent.MOD_ID)
              .translate("gui.goggles.kinetics_tank_no_oxygen")
              .style(ChatFormatting.RED)
              .forGoggles(tooltip);
    }
    return true;
  }
}
