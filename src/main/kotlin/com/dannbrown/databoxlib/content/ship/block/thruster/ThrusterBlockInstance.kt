package com.dannbrown.databoxlib.content.ship.block.thruster

import com.jozufozu.flywheel.api.MaterialManager
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance
import net.minecraft.world.level.block.state.BlockState

class ThrusterBlockInstance(modelManager: MaterialManager, tile: ThrusterBlockEntity) : SingleRotatingInstance<ThrusterBlockEntity>(modelManager, tile) {
  override fun getRenderedBlockState(): BlockState {
    return shaft()
  }
}