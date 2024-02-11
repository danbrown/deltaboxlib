package com.dannbrown.databoxlib.content.ship.block.thruster

import com.dannbrown.databoxlib.content.ship.SpaceShipControl
import com.dannbrown.databoxlib.content.ship.extensions.getShipManagingPos
import com.dannbrown.databoxlib.content.ship.extensions.getShipObjectManagingPos
import com.dannbrown.databoxlib.datagen.fuel.ProjectFuel
import com.simibubi.create.content.kinetics.base.KineticBlockEntity
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour
import com.simibubi.create.foundation.utility.animation.LerpedFloat
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ForgeCapabilities
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fluids.FluidStack
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.api.ships.getAttachment

class ThrusterBlockEntity(type: BlockEntityType<ThrusterBlockEntity>, pos: BlockPos, state: BlockState) : KineticBlockEntity(type, pos, state) {
  private val ship: ServerShip? get() = (level as ServerLevel).getShipObjectManagingPos(this.blockPos)
  private val control: SpaceShipControl? get() = ship?.getAttachment<SpaceShipControl>()
  var offset: LerpedFloat = LerpedFloat.linear()
    .startWithValue(0.0)
  var running = false
  var runningTicks = 0

  //  var tank: SmartFluidTankBehaviour? = null
  var internalTank: SmartFluidTankBehaviour? = null
  override fun <T : Any?> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
    return if (cap === ForgeCapabilities.FLUID_HANDLER && ThrusterBlock.hasPipeTowards(level, worldPosition, blockState, side)) internalTank!!.capability
      .cast()
    else super.getCapability(cap, side)
  }

  override fun addBehaviours(behaviours: MutableList<BlockEntityBehaviour?>) {
    internalTank = ThrusterFluidTankBehaviour.create(1500, ::onTankContentsChanged, this)
    behaviours.add(internalTank)
    registerAwardables(behaviours)
  }

  override fun isSource(): Boolean {
    return false
  }

  private fun onTankContentsChanged(contents: FluidStack?) {
  }

  override fun read(compound: CompoundTag, clientPacket: Boolean) {
    running = compound.getBoolean("Running")
    runningTicks = compound.getInt("Ticks")
    super.read(compound, clientPacket)
  }

  public override fun write(compound: CompoundTag, clientPacket: Boolean) {
    compound.putBoolean("Running", running)
    compound.putInt("Ticks", runningTicks)
    super.write(compound, clientPacket)
  }

  fun getFuel(): SmartFluidTankBehaviour.TankSegment? {
    return internalTank?.tanks?.get(0)
  }

  fun getFuelData(): Pair<ProjectFuel.FuelData, SmartFluidTankBehaviour.TankSegment?>? {
    val fuel = this.getFuel() ?: return null  // No fuel
    if (fuel.renderedFluid.isEmpty) return null // Empty tank
    val fuelFluid = fuel.renderedFluid.fluid
    if (!ProjectFuel.FuelManager.isFuel(fuelFluid)) return null // Not a valid fuel inside the tank
    val fuelData = ProjectFuel.FuelManager.getFuelData(fuelFluid) ?: return null // Not a valid fuel data from the data pack manager

    return Pair(fuelData, fuel)
  }

  override fun onSpeedChanged(previousSpeed: Float) {
    super.onSpeedChanged(previousSpeed)
    if (level == null || level!!.isClientSide) return // No level or client side
//    val fuelData = this.getFuelData()?.first ?: return // No fuel
    val block = this.blockState.block as ThrusterBlock
    // calculate the supposed thruster force and update the running and burning state for animation purposes
    val thrusterForce = block.getThrusterForce(this.blockState, this.getSpeedCoerce())
    val hasForce = thrusterForce.lengthSquared() > 0
    running = hasForce
    val newState = (level as ServerLevel).getBlockState(worldPosition)
      .setValue(ThrusterBlock.BURNING, hasForce)
    (level as ServerLevel).setBlock(worldPosition, newState, 2)
    // get the ship, if it exists update the thruster data
    val ship = (level as ServerLevel).getShipObjectManagingPos(this.blockPos) ?: (level as ServerLevel).getShipManagingPos(this.blockPos) ?: return
    val shipControl = ship.getAttachment<SpaceShipControl>() ?: return
    shipControl.updateThrusterForce(this.blockPos, thrusterForce, block.tier)
  }

  override fun onLoad() {
    super.onLoad()
    if (level == null || level!!.isClientSide) return // No level or client side
    if (speed != 0f) {
      val block = this.blockState.block as ThrusterBlock
      // calculate the supposed thruster force and update the running and burning state for animation purposes
      val thrusterForce = block.getThrusterForce(this.blockState, this.getSpeedCoerce())
      val hasForce = thrusterForce.lengthSquared() > 0
      running = hasForce
      val newState = (level as ServerLevel).getBlockState(worldPosition)
        .setValue(ThrusterBlock.BURNING, hasForce)
      (level as ServerLevel).setBlock(worldPosition, newState, 2)
      // get the ship, if it exists update the thruster data
      val ship = (level as ServerLevel).getShipObjectManagingPos(this.blockPos) ?: (level as ServerLevel).getShipManagingPos(this.blockPos) ?: return
      val shipControl = ship.getAttachment<SpaceShipControl>() ?: return
      shipControl.forceStopThruster(this.blockPos)
      shipControl.addThruster(this.blockPos, thrusterForce, block.tier)
    }
  }

  fun ticker() {
////    val fuelData = this.getFuelData()?.first ?: return // No fuel
//    val block = this.blockState.block as ThrusterBlock
//    val thrusterForce = block.getThrusterForce(this.blockState, speed)
////    running = thrusterForce.lengthSquared() > 0
    // shut of the thruster if speed is 0 or the fuel is empty
//    if (level == null || level!!.isClientSide) return
//    val ship = (level as ServerLevel).getShipObjectManagingPos(this.blockPos) ?: (level as ServerLevel).getShipManagingPos(this.blockPos) ?: return
//    val shipControl = ship.getAttachment<DataboxShipControl>() ?: return
//    val block = this.blockState.block as ThrusterBlock
//
//    if (speed == 0f) {
//      shipControl.updateThrusterForce(this.blockPos, Vector3d(0.0, 0.0, 0.0), block.tier)
//      running = false
//      return
//    }
//
//    shipControl.updateThrusterForce(this.blockPos, thrusterForce, block.tier)
  }

  fun updated() {
    sendData()
  }

  // return the speed value in a range of 0 to 1, 0 being no speed and 1 being max speed and all the values in between
  fun getSpeedCoerce(): Double {
    return (if (speed != 0f) kotlin.math.abs(speed)
      .coerceIn(0f, 256f) / 256f
    else 0f).toDouble()
  }
}
