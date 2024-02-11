package com.dannbrown.databoxlib.mixin.chemistry;

import com.dannbrown.databoxlib.ProjectContent;
import com.dannbrown.databoxlib.lib.LibLang;
import net.minecraft.ChatFormatting;
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


  @Inject(method = { "appendHoverText" }, at = { @At("HEAD") }, cancellable = true, require = 1)
  public void appendHoverText(ItemStack itemStack, Level level, List<Component> components, TooltipFlag tooltipFlag,
      CallbackInfo ci) throws InstantiationException, IllegalAccessException {

    // create or get an item description id, get the last key and add as suffix to
    // databoxlib mod id
    String itemDescription = this.getOrCreateDescriptionId();
    String itemDescriptionKey = itemDescription.split("\\.").length > 0
        ? itemDescription.split("\\.")[itemDescription.split("\\.").length - 1]
        : "";
    String databoxlibItemDescriptionKey = "formula." + ProjectContent.MOD_ID + "." + itemDescriptionKey;

    // here we use translations as a logic matter,
    // it is not recommended but is the way to dynamically add formula tooltips to
    // vanilla and other mod items
    boolean doExist = !LibLang.translateDirect(databoxlibItemDescriptionKey).getString().contains(".");

    // components.add(Component.literal(this.getDescriptionId()).withStyle(ChatFormatting.RED));

    // check if the translation exists and add it to the tooltip
    if (doExist) {
      components.add(LibLang.translateDirect(
          LibLang.TRANSLATIONS.FORMULA_FORMAT.getKey(),
          LibLang.translateDirect(databoxlibItemDescriptionKey).withStyle(ChatFormatting.GRAY))
          .withStyle(ChatFormatting.DARK_GRAY));
    }
  }
}
