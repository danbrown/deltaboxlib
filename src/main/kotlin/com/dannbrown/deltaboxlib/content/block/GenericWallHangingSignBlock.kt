package com.dannbrown.deltaboxlib.content.block;

import com.dannbrown.deltaboxlib.content.blockEntity.GenericHangingSignBlockEntity
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.HitResult
import java.util.function.Supplier

class GenericWallHangingSignBlock(private val type: Supplier<BlockEntityType<GenericHangingSignBlockEntity>>, props: Properties, woodType: WoodType, private val baseSign: Supplier<out Block>): WallHangingSignBlock(props, woodType) {
    override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity {
        return GenericHangingSignBlockEntity(type, blockPos, blockState)
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