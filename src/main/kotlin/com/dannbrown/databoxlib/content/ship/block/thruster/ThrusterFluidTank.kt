package com.dannbrown.databoxlib.content.ship.block.thruster

import com.dannbrown.databoxlib.datagen.fuel.ProjectFuel
import com.simibubi.create.foundation.fluid.SmartFluidTank
import net.minecraftforge.fluids.FluidStack
import java.util.function.Consumer

// This class overrides the SmartFluidTank.isFluidValid method to only allow rocket fuel
class ThrusterFluidTank(capacity: Int, updateCallback: Consumer<FluidStack?>) : SmartFluidTank(capacity, updateCallback) {
  override fun isFluidValid(fluidStack: FluidStack): Boolean {
    return ProjectFuel.FuelManager.isFuel(fluidStack.fluid)
  }
}