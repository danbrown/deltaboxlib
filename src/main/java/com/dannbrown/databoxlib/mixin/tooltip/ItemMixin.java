package com.dannbrown.databoxlib.mixin.tooltip;


import com.dannbrown.databoxlib.lib.LibLang;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Item.class)
public abstract class ItemMixin {
  @Shadow
  protected abstract String getOrCreateDescriptionId();

  @Shadow public abstract void verifyTagAfterLoad(CompoundTag pTag);

  @Inject(method = { "appendHoverText" }, at = { @At("HEAD") }, require = 1)
  public void appendHoverText(ItemStack itemStack, Level level, List<Component> components, TooltipFlag tooltipFlag, CallbackInfo ci) {
    // create or get an item description id, get the last key and add as suffix to
    String itemDescription = this.getOrCreateDescriptionId();
    String[] parts = itemDescription.split("\\.");
    String tooltipTranslationKey = parts.length > 1 ? LibLang.Companion.getTooltipKey(parts[parts.length - 2], parts[parts.length - 1]) : "";
    String genericTranslationKey = parts.length > 1 ? LibLang.Companion.getTooltipKey(null, parts[parts.length - 1]) : "";

    // here we use translations as a logic matter,
    // it is not recommended but is the way to dynamically add formula tooltips to
    // vanilla and other mod items
    boolean doExistTooltip = parts.length > 1 && !LibLang.translateDirect(tooltipTranslationKey).getString().contains(".");
    boolean doExistGeneric = parts.length > 1 && !LibLang.translateDirect(genericTranslationKey).getString().contains(".");

    // check if the translation exists and add it to the tooltip
    if (doExistTooltip) {
      components.add(LibLang.translateDirect(tooltipTranslationKey).withStyle(ChatFormatting.GRAY));
    }
    if (doExistGeneric) {
      components.add(LibLang.translateDirect(genericTranslationKey).withStyle(ChatFormatting.GRAY));
    }
  }
}
