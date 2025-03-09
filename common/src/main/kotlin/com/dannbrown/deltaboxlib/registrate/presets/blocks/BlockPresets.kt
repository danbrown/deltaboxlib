package com.dannbrown.deltaboxlib.registrate.presets.blocks

import com.dannbrown.deltaboxlib.content.block.GenericDoublePlantBlock
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
import net.minecraft.world.level.block.state.properties.BlockSetType
import net.minecraft.world.level.block.state.properties.WoodType
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
    suffix: String = "_leaves"
  ): BlockBuilder<T> {
    return LeavesBlockPreset(registrate, blockId, sapling, suffix).createPalmLeaves()
  }

  fun buddingLeavesBlock(
    sapling: Supplier<GenericSaplingBlock>,
    fruitBlock: Supplier<Block>,
    suffix: String = "_leaves"
  ): BlockBuilder<T> {
    return LeavesBlockPreset(registrate, blockId, sapling, suffix).createBuddingLeaves(fruitBlock)
  }

  fun cropLeavesBlock(
    sapling: Supplier<GenericSaplingBlock>,
    itemToDrop: Supplier<ItemLike>,
    suffix: String = "_leaves"
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

  fun fence(
    textureName: String,
    addSuffix: Boolean = true
  ): BlockBuilder<T> {
    return CommonBlockPreset(registrate, blockId).createFence(textureName, addSuffix)
  }

  fun fenceGate(
    textureName: String,
    woodType: WoodType,
    addSuffix: Boolean = true
  ): BlockBuilder<T> {
    return CommonBlockPreset(registrate, blockId).createFenceGate(textureName, woodType, addSuffix)
  }

  fun pressurePlate(
    textureName: String,
    blockSet: BlockSetType,
    isWooden: Boolean = true,
    addSuffix: Boolean = true
  ): BlockBuilder<T> {
    return CommonBlockPreset(registrate, blockId).createPressurePlate(textureName, blockSet, isWooden, addSuffix)
  }

  fun button(
    textureName: String,
    blockSet: BlockSetType,
    isWooden: Boolean = true,
    addSuffix: Boolean = true
  ): BlockBuilder<T> {
    return CommonBlockPreset(registrate, blockId).createButton(textureName, blockSet, isWooden, addSuffix)
  }

  fun woodenTrapdoor(
    blockSet: BlockSetType,
    addSuffix: Boolean = true
  ): BlockBuilder<T> {
    return CommonBlockPreset(registrate, blockId).createWoodenTrapdoor(blockSet, addSuffix)
  }

  fun door(
    blockSet: BlockSetType,
    isWooden: Boolean = true,
    addSuffix: Boolean = true
  ): BlockBuilder<T> {
    return CommonBlockPreset(registrate, blockId).createDoor(blockSet, isWooden, addSuffix)
  }

  fun grassBlock(
    dropItem: Supplier<ItemLike>? = null,
    isSticky: Boolean = false,
    isHarmful: Boolean = false,
    isBonemealable: Boolean = false,
    chance: Float = 0.6f,
    multiplier: Int = 2,
    placeOn: ((blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos) -> Boolean)? = null
  ): BlockBuilder<T> {
    return GrassBlockPreset(
      registrate,
      blockId,
      dropItem,
      isSticky,
      isHarmful,
      isBonemealable,
      chance,
      multiplier,
      placeOn
    ).create()
  }

  fun flowerBlock(
    dropItem: Supplier<ItemLike>? = null,
    isSticky: Boolean = false,
    isHarmful: Boolean = false,
    isBonemealable: Boolean = false,
    chance: Float = 0.6f,
    multiplier: Int = 2,
    placeOn: ((blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos) -> Boolean)? = null
  ): BlockBuilder<T> {
    return GrassBlockPreset(
      registrate,
      blockId,
      dropItem,
      isSticky,
      isHarmful,
      isBonemealable,
      chance,
      multiplier,
      placeOn
    ).createFlower()
  }


  fun smallTallGrassBlock(
    doubleBlock: BlockEntry<GenericDoublePlantBlock>,
    dropItem: Supplier<ItemLike>,
    needBonemeal: Boolean = false,
    placeOn: ((blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos) -> Boolean)? = null,
    chance: Float = 0.25f,
    multiplier: Int = 2,
  ): BlockBuilder<T> {
    return GrassBlockPreset(
      registrate,
      blockId,
      dropItem,
      false,
      false,
      false,
      chance,
      multiplier,
      placeOn
    ).createSmallTallGrassBlock(doubleBlock, needBonemeal)
  }

  fun doubleTallGrassBlock(
    dropItem: Supplier<ItemLike>,
    seedItem: Supplier<ItemLike>? = null,
    placeOn: ((blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos) -> Boolean)? = null,
    prefix: String = "tall_",
    chance: Float = 0.25f,
    multiplier: Int = 2,
  ): BlockBuilder<T> {
    return GrassBlockPreset(
      registrate,
      blockId,
      dropItem,
      false,
      false,
      false,
      chance,
      multiplier,
      placeOn
    ).createDoubleTallGrassBlock(seedItem, prefix)
  }

  fun leaves(
    sapling: Supplier<GenericSaplingBlock>,
    suffix: String = "_leaves"
  ): BlockBuilder<T> {
    return LeavesBlockPreset(
      registrate,
      blockId,
      sapling,
      suffix
    ).create()
  }

  fun palmLeaves(
    sapling: Supplier<GenericSaplingBlock>,
    suffix: String = "_leaves"
  ): BlockBuilder<T> {
    return LeavesBlockPreset(
      registrate,
      blockId,
      sapling,
      suffix
    ).createPalmLeaves()
  }

  fun cropLeaves(
    sapling: Supplier<GenericSaplingBlock>,
    itemToDrop: Supplier<ItemLike>,
    suffix: String = "_leaves"
  ): BlockBuilder<T> {
    return LeavesBlockPreset(
      registrate,
      blockId,
      sapling,
      suffix
    ).createCropLeaves(itemToDrop)
  }

  fun buddingLeaves(
    sapling: Supplier<GenericSaplingBlock>,
    fruitBlock: Supplier<Block>,
    suffix: String = "_leaves"
  ): BlockBuilder<T> {
    return LeavesBlockPreset(
      registrate,
      blockId,
      sapling,
      suffix
    ).createBuddingLeaves(fruitBlock)
  }

  fun crop(
    seedName: String,
    cropLang: String,
    seedLang: String,
    dropItem: Supplier<ItemLike>?,
    isBush: Boolean = true,
    includeSeedOnDrop: Boolean = true,
    chance: Float = 1f,
    multiplier: Int = 1,
  ): BlockBuilder<T> {
    return CropBlockPreset(
      registrate,
      blockId,
      seedName,
      cropLang,
      seedLang,
      dropItem,
      isBush,
      includeSeedOnDrop,
      chance,
      multiplier
    ).create()
  }

  fun doubleCrop(
    seedName: String,
    cropLang: String,
    seedLang: String,
    dropItem: Supplier<ItemLike>?,
    isBush: Boolean = true,
    includeSeedOnDrop: Boolean = true,
    chance: Float = 1f,
    multiplier: Int = 1,
  ): BlockBuilder<T> {
    return CropBlockPreset(
      registrate,
      blockId,
      seedName,
      cropLang,
      seedLang,
      dropItem,
      isBush,
      includeSeedOnDrop,
      chance,
      multiplier
    ).createDouble()
  }
}