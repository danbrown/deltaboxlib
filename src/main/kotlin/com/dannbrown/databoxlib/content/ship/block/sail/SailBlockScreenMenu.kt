package com.dannbrown.databoxlib.content.ship.block.sail

import com.dannbrown.databoxlib.init.ProjectConfig
import com.dannbrown.databoxlib.init.ProjectScreens
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack

class SailBlockScreenMenu(syncId: Int, playerInv: Inventory) :
  AbstractContainerMenu(ProjectScreens.SAIL_BLOCK.get(), syncId) {
  constructor(syncId: Int, playerInv: Inventory, blockEntity: SailBlockEntity?) : this(syncId, playerInv) {
    this.blockEntity = blockEntity
  }

  constructor(pContainerId: Int, inv: Inventory, extraData: FriendlyByteBuf?) : this(pContainerId, inv)

  var blockEntity: SailBlockEntity? = null
  override fun stillValid(player: Player): Boolean = true
  override fun clickMenuButton(player: Player, id: Int): Boolean {
    if (blockEntity == null) return false
    if (player.level().isClientSide) return super.clickMenuButton(player, id) // Only run on server
    val assembled = blockEntity?.assembled ?: false
    // Assemble the ship
    if (id == 0 && !assembled) {
      blockEntity?.assemble(player)
      player.closeContainer()
      return true
    }
    // Assemble the drone
    if (id == 4 && !assembled) {
      blockEntity?.assembleDrone(player)
      player.closeContainer()
      return true
    }
    // Align the ship
    if (id == 1 && assembled) {
      blockEntity?.align()
      player.closeContainer()
      return true
    }
    // Disassemble the ship
    if (id == 3 && assembled && ProjectConfig.SERVER.allowDisassembly) {
      blockEntity?.disassemble()
      player.closeContainer()
      return true
    }

    return super.clickMenuButton(player, id)
  }

  override fun quickMoveStack(player: Player, index: Int): ItemStack {
    return ItemStack.EMPTY // Do nothing
  }
}
