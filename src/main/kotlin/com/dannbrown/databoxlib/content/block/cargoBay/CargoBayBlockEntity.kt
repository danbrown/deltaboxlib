package com.dannbrown.databoxlib.content.block.cargoBay

import com.dannbrown.databoxlib.init.ProjectBlockEntities
//import com.simibubi.create.api.connectivity.ConnectivityHandler
//import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer
//import com.simibubi.create.foundation.blockEntity.behaviour.inventory.VersionedInventoryWrapper
//import net.minecraft.core.BlockPos
//import net.minecraft.core.Direction
//import net.minecraft.core.Direction.Axis
//import net.minecraft.world.level.block.entity.BlockEntity
//import net.minecraft.world.level.block.entity.BlockEntityType
//import net.minecraft.world.level.block.state.BlockState
//import net.minecraftforge.common.capabilities.Capability
//import net.minecraftforge.common.util.LazyOptional
//import net.minecraftforge.items.IItemHandler
//import net.minecraftforge.items.IItemHandlerModifiable
//import net.minecraftforge.items.ItemStackHandler
//import net.minecraftforge.items.wrapper.CombinedInvWrapper
import com.simibubi.create.api.connectivity.ConnectivityHandler
import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.VersionedInventoryWrapper
import com.simibubi.create.infrastructure.config.AllConfigs
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Direction.Axis
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.IItemHandlerModifiable
import net.minecraftforge.items.ItemStackHandler
import net.minecraftforge.items.wrapper.CombinedInvWrapper

//
////
////class CargoBayBlockEntity {
////}
//
//
//class CargoBayBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : ItemVaultBlockEntity(type, pos, state) {
//
//
//    override fun removeController(keepContents: Boolean) {
//    if (level!!.isClientSide()) return
//    updateConnectivity = true
//    controller = null
//    radius = 1
//    length = 1
//    var state = blockState
//    if (CargoBayBlock.isVault(state)) {
//      state = state.setValue(CargoBayBlock.LARGE, false)
//      getLevel()!!.setBlock(worldPosition, state, 22)
//    }
//    itemCapability.invalidate()
//    setChanged()
//    sendData()
//  }
//
//
//   public override fun updateConnectivity() {
//    updateConnectivity = false
//    if (level!!.isClientSide()) return
//    if (!isController) return
//    ConnectivityHandler.formMulti(this)
//  }
//
//
//  override fun getControllerBE(): CargoBayBlockEntity? {
//    if (isController) return this
//    val blockEntity = level!!.getBlockEntity(controller)
//    return if (blockEntity is CargoBayBlockEntity) blockEntity else null
//  }
//
//  override fun <T> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
//    if (isItemHandlerCap(cap)) {
//      initCapability()
//      return itemCapability.cast()
//    }
//    return super.getCapability(cap, side)
//  }
//
//  private fun initCapability() {
//    if (itemCapability.isPresent) return
//    if (!isController) {
//      val controllerBE = controllerBE ?: return
//      controllerBE.initCapability()
//      itemCapability = controllerBE.itemCapability
//      return
//    }
//    val alongZ = CargoBayBlock.getVaultBlockAxis(blockState) === Axis.Z
//    val invs = arrayOfNulls<IItemHandlerModifiable>(length * radius * radius)
//    for (yOffset in 0 until length) {
//      for (xOffset in 0 until radius) {
//        for (zOffset in 0 until radius) {
//          val vaultPos = if (alongZ) worldPosition.offset(xOffset, zOffset, yOffset) else worldPosition.offset(
//            yOffset,
//            xOffset,
//            zOffset
//          )
//          val vaultAt: CargoBayBlockEntity =
//            ConnectivityHandler.partAt(DataboxBlockEntities.CARGO_BAY.get(), level, vaultPos)!!
//          invs[yOffset * radius * radius + xOffset * radius + zOffset] = vaultAt?.inventoryOfBlock
//            ?: ItemStackHandler()
//        }
//      }
//    }
//    val itemHandler: IItemHandler = VersionedInventoryWrapper(CombinedInvWrapper(*invs))
//    itemCapability = LazyOptional.of { itemHandler }
//  }
//
//    override fun notifyMultiUpdated() {
//    val state = blockState
//    if (CargoBayBlock.isVault(state)) { // safety
//      level!!.setBlock(blockPos, state.setValue(CargoBayBlock.LARGE, radius > 2), 6)
//    }
//    itemCapability.invalidate()
//    setChanged()
//  }
//
//
//  }
open class CargoBayBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
  SmartBlockEntity(type, pos, state), IMultiBlockEntityContainer.Inventory {
  protected var itemCapability: LazyOptional<IItemHandler>
  var inventoryOfBlock: ItemStackHandler
  var controller1: BlockPos? = null
  var lastKnownPos1: BlockPos? = null
  var updateConnectivity1 = false
  var radius1 = 1
  var length1 = 1
  var axis1: Axis? = null

  init {
    inventoryOfBlock = object : ItemStackHandler(AllConfigs.server().logistics.vaultCapacity.get()) {
      override fun onContentsChanged(slot: Int) {
        super.onContentsChanged(slot)
        updateComparators()
      }
    }
    itemCapability = LazyOptional.empty()
  }

  override fun addBehaviours(behaviours: List<BlockEntityBehaviour>) {}
  fun updateConnectivity() {
    updateConnectivity1 = false
    if (level!!.isClientSide()) return
    if (!isController) return
    ConnectivityHandler.formMulti(this)
  }

  protected fun updateComparators() {
    val controllerBE = controllerBE ?: return
    level!!.blockEntityChanged(controllerBE.worldPosition)
    val pos = controllerBE.blockPos
    for (y in 0 until controllerBE.radius1) {
      for (z in 0 until if (controllerBE.axis1 === Axis.X) controllerBE.radius1 else controllerBE.length1) {
        for (x in 0 until if (controllerBE.axis1 === Axis.Z) controllerBE.radius1 else controllerBE.length1) {
          level!!.updateNeighbourForOutputSignal(pos.offset(x, y, z), blockState.block)
        }
      }
    }
  }

  override fun tick() {
    super.tick()
    if (lastKnownPos1 == null) lastKnownPos1 = blockPos
    else if (lastKnownPos1 != worldPosition && worldPosition != null) {
      onPositionChanged()
      return
    }
    if (updateConnectivity1) updateConnectivity()
  }

  override fun getLastKnownPos(): BlockPos {
    return lastKnownPos1!!
  }

  override fun isController(): Boolean {
    return controller1 == null || worldPosition.x == controller1!!.x && worldPosition.y == controller1!!.y && worldPosition.z == controller1!!.z
  }

  private fun onPositionChanged() {
    removeController(true)
    lastKnownPos1 = worldPosition
  }

  val controllerBE: CargoBayBlockEntity?
    get() {
      if (isController) return this
      val blockEntity = level!!.getBlockEntity(controller1)
      return if (blockEntity is CargoBayBlockEntity) blockEntity else null
    }

  override fun removeController(keepContents: Boolean) {
    if (level!!.isClientSide()) return
    updateConnectivity1 = true
    controller1 = null
    radius1 = 1
    length1 = 1
    var state = blockState
    if (CargoBayBlock.isVault(state)) {
      state = state.setValue(CargoBayBlock.LARGE, false)
      getLevel()!!.setBlock(worldPosition, state, 22)
    }
    itemCapability.invalidate()
    setChanged()
    sendData()
  }

  override fun setController(controller: BlockPos) {
    if (level!!.isClientSide && !isVirtual) return
    if (controller == this.controller1) return
    this.controller1 = controller
    itemCapability.invalidate()
    setChanged()
    sendData()
  }

  override fun getController(): BlockPos {
    return if (isController) worldPosition else controller1!!
  }

  override fun <T> getControllerBE(): T? where T : BlockEntity, T : IMultiBlockEntityContainer {
    if (isController) return (this as T)
    val blockEntity = level!!.getBlockEntity(controller1)
    return if (blockEntity is CargoBayBlockEntity) (blockEntity as T) else null
  }

  override fun read(compound: CompoundTag, clientPacket: Boolean) {
    super.read(compound, clientPacket)
    val controllerBefore = controller1
    val prevSize = radius1
    val prevLength = length1
    updateConnectivity1 = compound.contains("Uninitialized")
    controller1 = null
    lastKnownPos1 = null
    if (compound.contains("LastKnownPos")) lastKnownPos1 = NbtUtils.readBlockPos(compound.getCompound("LastKnownPos"))
    if (compound.contains("Controller")) controller1 = NbtUtils.readBlockPos(compound.getCompound("Controller"))
    if (isController) {
      radius1 = compound.getInt("Size")
      length1 = compound.getInt("Length")
    }
    if (!clientPacket) {
      inventoryOfBlock.deserializeNBT(compound.getCompound("Inventory"))
      return
    }
    val changeOfController = if (controllerBefore == null) controller1 != null else controllerBefore != controller1
    if (hasLevel() && (changeOfController || prevSize != radius1 || prevLength != length1)) level!!.setBlocksDirty(
      blockPos, Blocks.AIR.defaultBlockState(), blockState
    )
  }

  override fun write(compound: CompoundTag, clientPacket: Boolean) {
    if (updateConnectivity1) compound.putBoolean("Uninitialized", true)
    if (lastKnownPos1 != null) compound.put("LastKnownPos", NbtUtils.writeBlockPos(lastKnownPos1))
    if (!isController) compound.put("Controller", NbtUtils.writeBlockPos(controller1))
    if (isController) {
      compound.putInt("Size", radius1)
      compound.putInt("Length", length1)
    }
    super.write(compound, clientPacket)
    if (!clientPacket) {
      compound.putString("StorageType", "CombinedInv")
      compound.put("Inventory", inventoryOfBlock.serializeNBT())
    }
  }

  fun applyInventoryToBlock(handler: ItemStackHandler) {
    for (i in 0 until inventoryOfBlock.slots) inventoryOfBlock.setStackInSlot(
      i,
      if (i < handler.slots) handler.getStackInSlot(i) else ItemStack.EMPTY
    )
  }

  override fun <T> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
    if (isItemHandlerCap(cap)) {
      initCapability()
      return itemCapability.cast()
    }
    return super.getCapability(cap, side)
  }

  private fun initCapability() {
    if (itemCapability.isPresent) return
    if (!isController) {
      val controllerBE = controllerBE ?: return
      controllerBE.initCapability()
      itemCapability = controllerBE.itemCapability
      return
    }
    val alongZ = CargoBayBlock.getVaultBlockAxis(blockState) === Axis.Z
    val invs = arrayOfNulls<IItemHandlerModifiable>(length1 * radius1 * radius1)
    for (yOffset in 0 until length1) {
      for (xOffset in 0 until radius1) {
        for (zOffset in 0 until radius1) {
          val vaultPos = if (alongZ) worldPosition.offset(xOffset, zOffset, yOffset)
          else worldPosition.offset(
            yOffset,
            xOffset,
            zOffset
          )
          val vaultAt: CargoBayBlockEntity? = ConnectivityHandler.partAt(ProjectBlockEntities.CARGO_BAY.get(), level, vaultPos)
          invs[yOffset * radius1 * radius1 + xOffset * radius1 + zOffset] = vaultAt?.inventoryOfBlock ?: ItemStackHandler()
        }
      }
    }
    val itemHandler: IItemHandler = VersionedInventoryWrapper(CombinedInvWrapper(*invs))
    itemCapability = LazyOptional.of { itemHandler }
  }

  override fun preventConnectivityUpdate() {
    updateConnectivity1 = false
  }

  override fun notifyMultiUpdated() {
    val state = blockState
    if (CargoBayBlock.isVault(state)) { // safety
      level!!.setBlock(blockPos, state.setValue(CargoBayBlock.LARGE, radius1 > 2), 6)
    }
    itemCapability.invalidate()
    setChanged()
  }

  override fun getMainConnectionAxis(): Axis {
    return getMainAxisOf(this)
  }

  override fun getMaxLength(longAxis: Axis, width: Int): Int {
    return if (longAxis === Axis.Y) maxWidth else getMaxLength(width)
  }

  override fun getMaxWidth(): Int {
    return 3
  }

  override fun getHeight(): Int {
    return length1
  }

  override fun getWidth(): Int {
    return radius1
  }

  override fun setHeight(height: Int) {
    length1 = height
  }

  override fun setWidth(width: Int) {
    radius1 = width
  }

  override fun hasInventory(): Boolean {
    return true
  }

  companion object {
    fun getMaxLength(radius: Int): Int {
      return radius * 3
    }
  }
}