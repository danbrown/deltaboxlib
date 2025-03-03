package com.dannbrown.deltaboxlib.mixin.registrate;

import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Consumer;

@Mixin(BlockModelGenerators.class)
public interface BlockModelGeneratorsMixin {
  @Accessor("skippedAutoModelsOutput")
  Consumer<Item> getSkippedAutoModelsOutput();
}