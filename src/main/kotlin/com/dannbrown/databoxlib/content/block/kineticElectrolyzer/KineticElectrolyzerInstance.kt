package com.dannbrown.databoxlib.content.block.kineticElectrolyzer

import com.jozufozu.flywheel.api.Instancer
import com.jozufozu.flywheel.api.MaterialManager
import com.simibubi.create.AllPartialModels
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData
import net.minecraft.core.Direction

class KineticElectrolyzerInstance(modelManager: MaterialManager, tile: KineticElectrolyzerTileEntity) : SingleRotatingInstance<KineticElectrolyzerTileEntity>(modelManager, tile) {
  override fun getModel(): Instancer<RotatingData> {
    return rotatingMaterial.getModel(AllPartialModels.SHAFT_HALF, blockState, Direction.UP)
  }
}

