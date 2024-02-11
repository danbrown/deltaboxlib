package com.dannbrown.databoxlib.content.block.invertedClutch

import com.simibubi.create.content.kinetics.transmission.ClutchBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties

class InvertedClutchBlockEntity(type: BlockEntityType<InvertedClutchBlockEntity>, pos: BlockPos, state: BlockState) : ClutchBlockEntity(type, pos, state) {
  override fun getRotationSpeedModifier(face: Direction): Float {
    if (hasSource()) {
      if (face != sourceFacing)
        return if (blockState.getValue(BlockStateProperties.POWERED)) 1f else 0f
    }
    return 1f
  }
}