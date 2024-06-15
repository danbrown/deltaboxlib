package com.dannbrown.deltaboxlib.content.block

import com.dannbrown.deltaboxlib.content.blockEntity.GenericSignBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.StandingSignBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.WoodType

class GenericStandingSignBlock(props:Properties, woodType: WoodType): StandingSignBlock(props, woodType) {

  override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity? {
    return GenericSignBlockEntity(blockPos, blockState)
  }
}