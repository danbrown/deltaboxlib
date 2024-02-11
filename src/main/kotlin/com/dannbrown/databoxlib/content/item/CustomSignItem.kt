package com.dannbrown.databoxlib.content.item

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SignBlock
import net.minecraft.world.level.block.entity.SignBlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext

class CustomSignItem(props: Properties, signBlock: Block, wallSignBlock: Block): BlockItem(signBlock, props) {
  protected val wallBlock: Block = wallSignBlock



  override fun updateCustomBlockEntityTag(
    blockPos: BlockPos,
    level: Level,
    player: Player?,
    itemStack: ItemStack,
    blockState: BlockState
  ): Boolean {
    val flag = super.updateCustomBlockEntityTag(blockPos, level, player, itemStack, blockState)
    if (!level.isClientSide && !flag && player != null) {
      val blockentity = level.getBlockEntity(blockPos)
      if (blockentity is SignBlockEntity) {
        val block = level.getBlockState(blockPos).block
        if (block is SignBlock) {
          block.openTextEdit(player, blockentity, true)
        }
      }
    }
    return flag
  }


  override fun getPlacementState(placeContext: BlockPlaceContext): BlockState? {
    val blockstate = wallBlock.getStateForPlacement(placeContext)
    var blockstate1: BlockState? = null
    val levelreader: LevelReader = placeContext.level
    val blockpos = placeContext.clickedPos
    for (direction in placeContext.getNearestLookingDirections()) {
      if (direction != Direction.UP) {
        val blockstate2 = if (direction == Direction.DOWN) block.getStateForPlacement(placeContext) else blockstate
        if (blockstate2 != null && blockstate2.canSurvive(levelreader, blockpos)) {
          blockstate1 = blockstate2
          break
        }
      }
    }
    return if (blockstate1 != null && levelreader.isUnobstructed(
        blockstate1,
        blockpos,
        CollisionContext.empty()
      )
    ) blockstate1 else null
  }

//  override fun registerBlocks(mutableMap: MutableMap<Block, Item>, item: Item) {
//    super.registerBlocks(mutableMap, item)
//    mutableMap[wallBlock] = item
//  }
//
//  override fun removeFromBlockToItemMap(blockToItemMap: MutableMap<Block, Item>, itemIn: Item) {
//    super.removeFromBlockToItemMap(blockToItemMap, itemIn)
//    blockToItemMap.remove(wallBlock)
//  }
}