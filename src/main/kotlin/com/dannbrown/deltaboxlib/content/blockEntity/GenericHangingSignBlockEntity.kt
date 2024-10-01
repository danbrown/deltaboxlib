package com.dannbrown.deltaboxlib.content.blockEntity

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.entity.SignBlockEntity
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Supplier

class GenericHangingSignBlockEntity(type: Supplier<BlockEntityType<GenericHangingSignBlockEntity>>, blockPos: BlockPos, blockstate: BlockState): SignBlockEntity(type.get(), blockPos, blockstate) {
  override fun getTextLineHeight(): Int {
    return 9
  }

  override fun getMaxTextLineWidth(): Int {
    return 60
  }
}