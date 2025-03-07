package com.dannbrown.deltaboxlib.registrate.presets.blocks

import com.dannbrown.deltaboxlib.content.block.GenericSaplingBlock
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

  // Leaves block presets
  fun leavesBlock(
    sapling: Supplier<GenericSaplingBlock>,
    suffix: String = "_leaves"
  ): BlockBuilder<T> {
    return LeavesBlockPreset(registrate, blockId, sapling, suffix).create()
  }

  fun palmLeavesBlock(
    sapling: Supplier<GenericSaplingBlock>,
    suffix: String = "_palm_leaves"
  ): BlockBuilder<T> {
    return LeavesBlockPreset(registrate, blockId, sapling, suffix).createPalmLeaves()
  }

  fun buddingLeavesBlock(
    sapling: Supplier<GenericSaplingBlock>,
    fruitBlock: Supplier<Block>,
    suffix: String = "_budding_leaves"
  ): BlockBuilder<T> {
    return LeavesBlockPreset(registrate, blockId, sapling, suffix).createBuddingLeaves(fruitBlock)
  }

  fun cropLeavesBlock(
    sapling: Supplier<GenericSaplingBlock>,
    itemToDrop: Supplier<ItemLike>,
    suffix: String = "_crop_leaves"
  ): BlockBuilder<T> {
    return LeavesBlockPreset(registrate, blockId, sapling, suffix).createCropLeaves(itemToDrop)
  }

  fun bottomTop(
    bottomName: String = "",
    topName: String = "",
    sideName: String = ""
  ): BlockBuilder<T> {
    return CommonBlockPreset(registrate, blockId).createBottomTop(bottomName, topName, sideName)
  }

  fun rotatedPillar(
    topName: String = "",
    sideName: String = ""
  ): BlockBuilder<T> {
    return CommonBlockPreset(registrate, blockId).createRotatedPillar(topName, sideName)
  }

  fun stairs(
    textureName: String,
    bottomTop: Boolean = false,
    isWooden: Boolean = false,
    addSuffix: Boolean = true
  ): BlockBuilder<T> {
    return CommonBlockPreset(registrate, blockId).createStairs(textureName, bottomTop, isWooden, addSuffix)
  }

  fun slab(
    textureName: String,
    bottomTop: Boolean = false,
    isWooden: Boolean = false,
    addSuffix: Boolean = true
  ): BlockBuilder<T> {
    return CommonBlockPreset(registrate, blockId).createSlab(textureName, bottomTop, isWooden, addSuffix)
  }

  fun wall(
    textureName: String,
    bottomTop: Boolean = false,
    addSuffix: Boolean = true
  ): BlockBuilder<T> {
    return CommonBlockPreset(registrate, blockId).createWall(textureName, bottomTop, addSuffix)
  }
}