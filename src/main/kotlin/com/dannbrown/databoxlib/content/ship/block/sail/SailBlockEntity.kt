package com.dannbrown.databoxlib.content.ship.block.sail

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.content.ship.SpaceShipControl
import com.dannbrown.databoxlib.content.ship.ShipAssembler
import com.dannbrown.databoxlib.content.ship.extensions.getShipObjectManagingPos
import com.dannbrown.databoxlib.content.ship.extensions.toJOMLD
import com.dannbrown.databoxlib.init.ProjectConfig
import com.dannbrown.databoxlib.init.ProjectTags
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.TagKey
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.api.ships.getAttachment
import org.valkyrienskies.mod.common.entity.ShipMountingEntity

class SailBlockEntity(type: BlockEntityType<SailBlockEntity>, pos: BlockPos, state: BlockState) : BlockEntity(type, pos, state), MenuProvider {
  private val ship: ServerShip? get() = (level as ServerLevel).getShipObjectManagingPos(this.blockPos)
  private val control: SpaceShipControl? get() = ship?.getAttachment<SpaceShipControl>()
  private val seats = mutableListOf<ShipMountingEntity>()
  val assembled get() = ship != null
  val aligning get() = control?.aligning ?: false
  private var shouldDisassembleWhenPossible = false

  companion object {
    val TRANSLATION_KEY = "gui.${ProjectContent.MOD_ID}.sail_title"
  }

  override fun createMenu(pContainerId: Int, pPlayerInventory: Inventory, pPlayer: Player): AbstractContainerMenu {
    return SailBlockScreenMenu(pContainerId, pPlayerInventory, this)
  }

  override fun getDisplayName(): Component {
    return Component.translatable(TRANSLATION_KEY)
  }

  fun tick() {
    if (shouldDisassembleWhenPossible && ship?.getAttachment<SpaceShipControl>()?.canDisassemble == true) {
      this.disassemble()
    }
    control?.ship = ship
  }

  // Needs to get called server-side
  fun assemble(player: Player, scale: Double = 1.0) {
    val level = level as ServerLevel? ?: return
    // Check the block state before assembling to avoid creating an empty ship
    val blockState = level.getBlockState(blockPos)
    if (blockState.block !is SailBlock) return
    val builtShip = ShipAssembler.createShip(
      level,
      blockPos,
      {
        !it.isAir
          && (!ProjectConfig.SERVER.blockBlacklist.contains(BuiltInRegistries.BLOCK.getKey(it.block)
          .toString()))
          && !it.tags.anyMatch { blockTag: TagKey<Block> -> ProjectTags.BLOCK.SHIP_BLACKLIST == blockTag }
      },
      scale
//      VSGameConfig.SERVER.miniShipSize // scale the ship
    )
    if (builtShip == null) {
      player.displayClientMessage(Component.translatable("Ship is too big! Max size is ${ProjectConfig.SERVER.maxShipBlocks} blocks (changable in the config)"), true)
      ProjectContent.LOGGER.warn("Failed to assemble ship for ${player.name.string}")
    }
    else {
      player.displayClientMessage(Component.translatable("Ship assembled"), true)
    }
  }

  fun assembleDrone(player: Player) {
    return assemble(player, 0.5) // TODO: make this configurable
  }

  fun disassemble() {
    val ship = ship ?: return
    val level = level ?: return
    val control = control ?: return

    if (!control.canDisassemble) {
      shouldDisassembleWhenPossible = true
      control.disassembling = true
      control.aligning = true
      return
    }
    val inWorld = ship.shipToWorld.transformPosition(this.blockPos.toJOMLD())

    try {
      ShipAssembler.unfillShip(
        level as ServerLevel,
        ship,
        control.aligningTo,
        this.blockPos,
        BlockPos.containing(inWorld.x, inWorld.y, inWorld.z)
      )
//      player.displayClientMessage(Component.translatable("Ship disassembled"), true)
    } catch (e: Exception) {
      ProjectContent.LOGGER.error("Failed to disassemble ship for")
//      player.displayClientMessage(Component.translatable("Failed to disassemble ship"), true)
    }
    // ship.die() TODO i think we do need this no? or autodetecting on all air
    shouldDisassembleWhenPossible = false
  }

  fun align() {
    val control = control ?: return
    control.aligning = !control.aligning
  }

  override fun setRemoved() {
    if (level?.isClientSide == false) {
      for (i in seats.indices) {
        seats[i].kill()
      }
      seats.clear()
    }
    super.setRemoved()
  }
}
