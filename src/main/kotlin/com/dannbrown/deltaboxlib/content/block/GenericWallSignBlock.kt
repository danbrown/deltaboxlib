package com.dannbrown.deltaboxlib.content.block

import com.dannbrown.deltaboxlib.content.blockEntity.GenericSignBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.WallSignBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.WoodType
import net.minecraft.world.phys.HitResult
import java.util.function.Supplier

class GenericWallSignBlock(private val type: Supplier<BlockEntityType<GenericSignBlockEntity>>, props: Properties, woodType: WoodType, private val baseSign: Supplier<out Block>): WallSignBlock(props, woodType) {

  override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity {
    return GenericSignBlockEntity(type, blockPos, blockState)
  }

  override fun getCloneItemStack(
    state: BlockState?,
    target: HitResult?,
    level: BlockGetter?,
    pos: BlockPos?,
    player: Player?
  ): ItemStack {
    return ItemStack(baseSign.get())
  }
}