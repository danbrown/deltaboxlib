package com.dannbrown.databoxlib.content.block

import com.dannbrown.databoxlib.init.ProjectBlockEntities
import com.simibubi.create.content.equipment.armor.BacktankBlock
import com.simibubi.create.content.equipment.armor.BacktankBlockEntity
import com.simibubi.create.foundation.block.IBE
import net.minecraft.world.level.block.SimpleWaterloggedBlock
import net.minecraft.world.level.block.entity.BlockEntityType

class CustomBacktankBlock(properties: Properties) : BacktankBlock(properties), IBE<BacktankBlockEntity>, SimpleWaterloggedBlock {
  override fun getBlockEntityType(): BlockEntityType<out BacktankBlockEntity> {
    return ProjectBlockEntities.BACKTANK.get()
  }
}

