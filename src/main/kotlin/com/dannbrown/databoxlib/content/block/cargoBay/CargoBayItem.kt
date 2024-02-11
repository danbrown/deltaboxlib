package com.dannbrown.databoxlib.content.block.cargoBay

import com.dannbrown.databoxlib.init.ProjectBlockEntities
import com.simibubi.create.api.connectivity.ConnectivityHandler
import com.simibubi.create.foundation.utility.VecHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Direction.Axis
import net.minecraft.core.Direction.AxisDirection
import net.minecraft.server.MinecraftServer
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

class CargoBayItem(block: Block, props: Properties) :
  BlockItem(block, props) {
  override fun place(ctx: BlockPlaceContext): InteractionResult {
    val initialResult = super.place(ctx)
    if (!initialResult.consumesAction()) return initialResult
    tryMultiPlace(ctx)
    return initialResult
  }

  override fun updateCustomBlockEntityTag(
    blockPos: BlockPos, level: Level, player: Player?,
    itemStack: ItemStack, blockState: BlockState
  ): Boolean {
    val minecraftserver: MinecraftServer = level.server ?: return false
    val nbt = itemStack.getTagElement("BlockEntityTag")
    if (nbt != null) {
      nbt.remove("Length")
      nbt.remove("Size")
      nbt.remove("Controller")
      nbt.remove("LastKnownPos")
    }
    return super.updateCustomBlockEntityTag(blockPos, level, player, itemStack, blockState)
  }

  private fun tryMultiPlace(ctx: BlockPlaceContext) {
    val player = ctx.player ?: return
    if (player.isShiftKeyDown) return
    val face = ctx.clickedFace
    val stack = ctx.itemInHand
    val world: Level = ctx.level
    val pos = ctx.clickedPos
    val placedOnPos = pos.relative(face.opposite)
    val placedOnState: BlockState = world.getBlockState(placedOnPos)
    if (!CargoBayBlock.isVault(placedOnState)) return
    val tankAt: CargoBayBlockEntity =
      ConnectivityHandler.partAt(ProjectBlockEntities.CARGO_BAY.get(), world, placedOnPos)
        ?: return
    val controllerBE = tankAt.getControllerBE<CargoBayBlockEntity>()!!
    val width = controllerBE.radius1
    if (width == 1) return
    var tanksToPlace = 0
    val vaultBlockAxis = CargoBayBlock.getVaultBlockAxis(placedOnState) ?: return
    if (face.axis !== vaultBlockAxis) return
    val vaultFacing = Direction.fromAxisAndDirection(vaultBlockAxis, AxisDirection.POSITIVE)
    val startPos = if (face === vaultFacing.opposite) controllerBE.blockPos
      .relative(vaultFacing.opposite)
    else controllerBE.blockPos
      .relative(vaultFacing, controllerBE.length1)
    if (VecHelper.getCoordinate(startPos, vaultBlockAxis) !== VecHelper.getCoordinate(pos, vaultBlockAxis)) return
    for (xOffset in 0 until width) {
      for (zOffset in 0 until width) {
        val offsetPos =
          if (vaultBlockAxis === Axis.X) startPos.offset(0, xOffset, zOffset) else startPos.offset(xOffset, zOffset, 0)
        val blockState: BlockState = world.getBlockState(offsetPos)
        if (CargoBayBlock.isVault(blockState)) continue
        if (!blockState.canBeReplaced(ctx)
        ) return
        tanksToPlace++
      }
    }
    if (!player.isCreative && stack.count < tanksToPlace) return
    for (xOffset in 0 until width) {
      for (zOffset in 0 until width) {
        val offsetPos =
          if (vaultBlockAxis === Axis.X) startPos.offset(0, xOffset, zOffset) else startPos.offset(xOffset, zOffset, 0)
        val blockState: BlockState = world.getBlockState(offsetPos)
        if (CargoBayBlock.isVault(blockState)) continue
        val context = BlockPlaceContext.at(ctx, offsetPos, face)
        player.getPersistentData()
          .putBoolean("SilenceVaultSound", true)
        super.place(context)
        player.getPersistentData()
          .remove("SilenceVaultSound")
      }
    }
  }
}