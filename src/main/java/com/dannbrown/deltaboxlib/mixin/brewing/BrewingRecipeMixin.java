package com.dannbrown.deltaboxlib.mixin.brewing;

import jdk.jshell.EvalException;
import net.minecraft.nbt.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.brewing.BrewingRecipe;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(BrewingRecipe.class)
public class BrewingRecipeMixin {

  @Shadow @Final private @NotNull Ingredient input;

  @Inject(method = "isInput", at = @At("HEAD"), cancellable = true, remap = false)
  public void isInput(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
    Ingredient thisInput = this.input;

    if (thisInput.test(stack)) {
      // Check if the input item has all the tags that thisInput has
      for (ItemStack item : thisInput.getItems()) {
        Set<String> itemTags = item.serializeNBT().getAllKeys();

        for (String tag : itemTags) {
          Tag value = item.serializeNBT().get(tag);
          if (!stack.serializeNBT().contains(tag) || !stack.serializeNBT().get(tag).equals(value)) {
            cir.setReturnValue(false);
            return;
          }
        }
      }
      cir.setReturnValue(true);
    }
  }
}
