package com.dannbrown.databoxlib.content.block

import com.dannbrown.databoxlib.content.blockEntity.CustomSignBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.StandingSignBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.WoodType

class CustomStandingSignBlock(props:Properties, woodType: WoodType): StandingSignBlock(props, woodType) {

  override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity? {
    return CustomSignBlockEntity(blockPos, blockState)
  }
}