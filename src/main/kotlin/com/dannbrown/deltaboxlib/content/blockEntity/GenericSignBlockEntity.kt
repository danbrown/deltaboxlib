package com.dannbrown.deltaboxlib.content.blockEntity

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.entity.SignBlockEntity
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Supplier

class GenericSignBlockEntity(type: Supplier<BlockEntityType<GenericSignBlockEntity>>, blockPos: BlockPos, blockstate: BlockState): SignBlockEntity(type.get(), blockPos, blockstate) {

}