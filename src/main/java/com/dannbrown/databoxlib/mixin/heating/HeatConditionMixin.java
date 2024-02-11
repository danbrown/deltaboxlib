package com.dannbrown.databoxlib.mixin.heating;

import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;

/*
 * Thanks to https://github.com/SpongePowered/Mixin/issues/387#issuecomment-888408556 for the tip on how to mixin enum values!
 */
@Mixin(value = HeatCondition.class, remap = false)
public abstract class HeatConditionMixin {
  @Shadow
  @Final
  @Mutable
  private static HeatCondition[] $VALUES;

  @Shadow public abstract int getColor();

  @Shadow public abstract String serialize();

  private static final HeatCondition PASSIVEHEATED = heatExpansion$addVariant("PASSIVEHEATED",  0xED9C33);
  private static final HeatCondition HYPERHEATED = heatExpansion$addVariant("HYPERHEATED", 0xED9C33);

  @Invoker("<init>")
  public static HeatCondition heatExpansion$invokeInit(String internalName, int internalId, int color) {
    throw new AssertionError();
  }

  private static HeatCondition heatExpansion$addVariant(String internalName, int color) {
    ArrayList<HeatCondition> variants = new ArrayList<>(Arrays.asList(HeatConditionMixin.$VALUES));
    HeatCondition heat = heatExpansion$invokeInit(internalName, variants.get(variants.size() - 1).ordinal() + 1, color);
    variants.add(heat);
    HeatConditionMixin.$VALUES = variants.toArray(new HeatCondition[0]);
    return heat;
  }

  /**
   * @author DannBrown & ZehMaria
   * @reason Adds HYPER and PASSIVE to the basin recipes, thanks to ZehMaria for how to implement new heat levels!
   */
  @Overwrite

    public boolean testBlazeBurner(HeatLevel level) {

    if (this.equals(PASSIVEHEATED)) return level.isAtLeast(HeatLevel.FADING);
    if (this.equals(HeatCondition.SUPERHEATED)) return level == HeatLevel.SEETHING || level == HeatLevel.valueOf("HYPER");
    if (this.equals(HeatCondition.HEATED)) {
      return level == HeatLevel.KINDLED || level == HeatLevel.FADING || level == HeatLevel.SEETHING || level == HeatLevel.valueOf("HYPER");
    }
    if (this.equals(HYPERHEATED)) return level == HeatLevel.valueOf("HYPER");
    return true;
  }

  /**
   * @author DannBrown & ZehMaria
   * @reason Adds HYPER and PASSIVE to the basin recipes, thanks to ZehMaria for how to implement new heat levels!
   */
  @Overwrite
  public HeatLevel visualizeAsBlazeBurner() {
    if (this.equals(PASSIVEHEATED)) return HeatLevel.valueOf("PASSIVE");
    if (this.equals(HYPERHEATED)) return HeatLevel.valueOf("HYPER");
    if (this.equals(HeatCondition.SUPERHEATED)) return HeatLevel.SEETHING;
    if (this.equals(HeatCondition.HEATED)) return HeatLevel.KINDLED;
    return HeatLevel.NONE;
  }
}