package com.dannbrown.databoxlib.content.block.resistor

import com.ibm.icu.text.DecimalFormat
import com.simibubi.create.content.kinetics.transmission.SplitShaftBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties

class ResistorBlockEntity(type: BlockEntityType<ResistorBlockEntity>, pos: BlockPos, state: BlockState) : SplitShaftBlockEntity(type, pos, state) {
  override fun getRotationSpeedModifier(face: Direction): Float {
    if (hasSource()) {
      if (face != sourceFacing)
        return if (blockState.getValue(BlockStateProperties.POWER) > 0) {
          val power = blockState.getValue(BlockStateProperties.POWER)
          val modifier = (power.toFloat() / 15f) // 0f to 1f based on power
          val decimalModifier = DecimalFormat("#.##").format(modifier)
            .toFloat()
          return decimalModifier
        }
        else 0f
    }
    return 1f
  }
}