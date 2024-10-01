package com.dannbrown.deltaboxlib.content.block;

import com.dannbrown.deltaboxlib.content.blockEntity.GenericHangingSignBlockEntity
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import java.util.function.Supplier

class GenericHangingSignBlock(private val type: Supplier<BlockEntityType<GenericHangingSignBlockEntity>>, props: Properties, woodType: WoodType): CeilingHangingSignBlock(props, woodType) {
    override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity {
        return GenericHangingSignBlockEntity(type, blockPos, blockState)
    }
}