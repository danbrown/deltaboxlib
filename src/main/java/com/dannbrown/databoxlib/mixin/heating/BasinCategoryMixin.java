package com.dannbrown.databoxlib.mixin.heating;


import com.dannbrown.databoxlib.init.ProjectBlocks;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.compat.jei.category.BasinCategory;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;

@Mixin(value = BasinCategory.class, remap = false)
public abstract class BasinCategoryMixin {

  @Inject(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lcom/simibubi/create/content/processing/basin/BasinRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V",
          at = @At(value = "INVOKE_ASSIGN",
                  target = "Lcom/simibubi/create/content/processing/basin/BasinRecipe;getRequiredHeat()Lcom/simibubi/create/content/processing/recipe/HeatCondition;",
                  ordinal = 0),
          cancellable = true
  )
  private void onSetBurnerType(IRecipeLayoutBuilder builder, BasinRecipe recipe, IFocusGroup focuses, CallbackInfo ci) {
    HeatCondition requiredHeat = recipe.getRequiredHeat();
    if (!requiredHeat.testBlazeBurner(HeatLevel.NONE)) {

      // Is PASSIVE
      if (requiredHeat.testBlazeBurner(HeatLevel.valueOf("PASSIVE"))) {
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 134, 81).addItemStack(new ItemStack(Items.FLINT_AND_STEEL));
      }

      // Is HEATED or SUPERHEATED
      else if(requiredHeat.testBlazeBurner(HeatLevel.SEETHING)){
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 134, 81).addItemStack(AllBlocks.BLAZE_BURNER.asStack());

        if (!requiredHeat.testBlazeBurner(HeatLevel.KINDLED)) {
          builder.addSlot(RecipeIngredientRole.CATALYST, 153, 81).addItemStack(AllItems.BLAZE_CAKE.asStack());
        }
      }

      // Is HYPER
      else if(requiredHeat.testBlazeBurner(HeatLevel.valueOf("HYPER"))){
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 134, 81).addItemStack(ProjectBlocks.INSTANCE.getHYPER_HEATER().asStack());
      }
    }
    ci.cancel();
  }
}