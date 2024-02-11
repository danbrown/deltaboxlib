package com.dannbrown.databoxlib.mixin.heating;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Arrays;
@Mixin(value = HeatLevel.class, remap = false)
public abstract class HeatLevelMixin {
  @Shadow
  @Final
  @Mutable
  private static HeatLevel[] $VALUES;

  @Shadow public abstract boolean isAtLeast(HeatLevel heatLevel);

  @Shadow
  public static HeatLevel valueOf(String name) throws IllegalArgumentException {
    return null;
  }

  @Shadow
  public static HeatLevel[] values() {
    return new HeatLevel[0];
  }

  private static final HeatLevel PASSIVE = heatExpansion$addVariant("PASSIVE");
  private static final HeatLevel HYPER = heatExpansion$addVariant("HYPER");

  @Invoker("<init>")
  public static HeatLevel heatExpansion$invokeInit(String internalName, int internalId) {
    throw new AssertionError();
  }

  private static HeatLevel heatExpansion$addVariant(String internalName) {
    ArrayList<HeatLevel> variants = new ArrayList<>(Arrays.asList(HeatLevelMixin.$VALUES));
    HeatLevel heat = heatExpansion$invokeInit(internalName, variants.get(variants.size() - 1).ordinal() + 1);
    variants.add(heat);
    HeatLevelMixin.$VALUES = variants.toArray(new HeatLevel[0]);
    return heat;
  }

  @Inject(method = "isAtLeast", at = @At("HEAD"), cancellable = true)
  protected void setNewLevels(HeatLevel heatLevel, CallbackInfoReturnable<Boolean> cir) {
    if (heatLevel.getSerializedName() == "passive") { cir.setReturnValue(this.isAtLeast(HeatLevel.FADING)); }
    if (heatLevel.getSerializedName() == "hyper") {
      HeatLevel[] levels = this.values();
      Boolean isHyper = false;
      for (int i = 0; i < levels.length; i++) {
        if (levels[i] == HYPER) {
          isHyper = true;
        }
      }
      cir.setReturnValue(isHyper);
    }
  }
}