package com.dannbrown.databoxlib.content.ship.block.angularSensor

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class AngularSensorBlockEntity(type: BlockEntityType<AngularSensorBlockEntity>, pos: BlockPos, state: BlockState) : SmartBlockEntity(type, pos, state) {
  fun updated() {
    sendData()
  }

  override fun addBehaviours(behaviours: MutableList<BlockEntityBehaviour>?) {
    registerAwardables(behaviours)
  }
}