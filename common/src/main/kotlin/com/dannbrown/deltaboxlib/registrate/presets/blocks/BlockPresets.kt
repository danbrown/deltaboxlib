package com.dannbrown.deltaboxlib.registrate.presets.blocks

import com.dannbrown.deltaboxlib.content.worldgen.tree.DeltaboxTreeGrower
import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.builders.BlockBuilder
import com.dannbrown.deltaboxlib.registrate.registry.BlockEntry
import net.minecraft.core.BlockPos
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
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

  fun saplingBlock(
    treeGrower: Supplier<DeltaboxTreeGrower>,
    placeOn: ((BlockState, BlockGetter, BlockPos) -> Boolean)? = null
  ): BlockBuilder<T> {
    return SaplingBlockPreset(registrate, blockId, treeGrower, placeOn).create()
  }

  fun pottedBlock(
    plantBlock: BlockEntry<*>,
    suffix: String = ""
  ): BlockBuilder<T> {
    return PottedBlockPreset(registrate, blockId, plantBlock, suffix).create()
  }
}