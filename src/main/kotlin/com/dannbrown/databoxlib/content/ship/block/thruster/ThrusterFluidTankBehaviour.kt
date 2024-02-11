package com.dannbrown.databoxlib.content.ship.block.thruster

import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.IFluidHandler
import java.util.function.Consumer

// This helper class is used to create a SmartFluidTankBehaviour that only allows rocket fuel (from the DataboxTags.FLUID.IS_ROCKET_FUEL tag)
object ThrusterFluidTankBehaviour {
  fun create(capacity: Int, updateCallback: Consumer<FluidStack?>, blockEntity: ThrusterBlockEntity): SmartFluidTankBehaviour {
    return object : SmartFluidTankBehaviour(TYPE, blockEntity, 1, capacity, false) {
      override fun whenFluidUpdates(fluidUpdateCallback: Runnable?): SmartFluidTankBehaviour {
        val hasNoFluid = this.getTanks() == null || this.getTanks()[0].isEmpty(2f)
        updateCallback.accept(if (hasNoFluid) null else this.getTanks()[0].renderedFluid)
        return super.whenFluidUpdates(fluidUpdateCallback)
      }

      init {
        val handlers = arrayOfNulls<IFluidHandler>(1)
        val createdTank = ThrusterFluidTank(capacity, updateCallback)
        val tankSegment = object : TankSegment(capacity) {
          init {
            this.tank = createdTank
          }
        }
        tanks[0] = tankSegment
        handlers[0] = createdTank
        capability = LazyOptional.of { InternalFluidHandler(handlers, false) }
      }
    }
  }
}