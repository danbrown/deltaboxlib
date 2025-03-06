package com.dannbrown.deltaboxlib.registrate.presets

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.builders.BlockBuilder
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import java.util.function.Supplier

class BlockPresets<T : Block>(val registrate: AbstractDeltaboxRegistrate, val blockId: String) {
  fun storageBlock(
    ingotItem: Supplier<ItemLike>,
    ingredient: Supplier<Ingredient>,
    addSuffix: Boolean = true
  ): BlockBuilder<T> {
    return StorageBlockPreset(registrate, blockId, ingotItem, ingredient, addSuffix).create()
  }

  fun smallStorageBlock(
    ingotItem: Supplier<ItemLike>,
    ingredient: Supplier<Ingredient>,
    addSuffix: Boolean = true
  ): BlockBuilder<T> {
    return StorageBlockPreset(registrate, blockId, ingotItem, ingredient, addSuffix).createSmall()
  }
}