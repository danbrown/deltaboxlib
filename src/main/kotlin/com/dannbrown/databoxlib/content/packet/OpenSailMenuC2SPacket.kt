package com.dannbrown.databoxlib.content.packet

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.content.ship.block.sail.SailBlockScreenMenu
import com.simibubi.create.content.contraptions.AbstractContraptionEntity
import com.simibubi.create.content.contraptions.Contraption
import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkEvent

class OpenSailMenuC2SPacket : NetworkPacketBase {
  private var contraptionId: Int? = null
  private var containerId: Int? = null
  private var contraptionPos: BlockPos? = null

  constructor()
  constructor (buffer: FriendlyByteBuf) : this() {
    contraptionId = buffer.readInt()
    contraptionPos = buffer.readBlockPos()
    containerId = buffer.readUnsignedByte()
      .toInt()
  }

  constructor (contraption: Contraption, contraptionPos: BlockPos, containerId: Int) : this() {
    this.contraptionId = contraption.entity.id
    this.contraptionPos = contraptionPos
    this.containerId = containerId
  }

  override fun write(buffer: FriendlyByteBuf) {
    buffer.writeInt(contraptionId!!)
    buffer.writeBlockPos(contraptionPos!!)
    buffer.writeByte(containerId!!)
  }

  override fun handle(context: NetworkEvent.Context): Boolean {
    context.enqueueWork {
      // HERE WE ARE ON THE SERVER!
//      val player = context.getSender()
//      val level: ServerLevel = player!!.level() as ServerLevel
      val player = context.getSender()

      ProjectContent.LOGGER.info("Received packet to open sail menu")

      if (player != null) {
        val level = player.level()
        val contraptionEntity = level.getEntity(contraptionId!!)
        if (contraptionEntity != null && contraptionEntity is AbstractContraptionEntity) {
          val contraption = contraptionEntity.contraption
          val sailMenu = SailBlockScreenMenu(containerId!!, player.inventory)
          player.containerMenu = sailMenu
          player.initMenu(sailMenu)
//          Minecraft.getInstance()
//            .setScreen(AssemblerBlockScreen(sailMenu, player.inventory))
        }
      }
//      if (player != null && player.uuid == contraptionUUID) {
//        val level = player.level()
//        val blockEntity = level.getBlockEntity(contraptionPos!!)
//
//        if (blockEntity != null && contraptionPos != null && blockEntity is SailBlockEntity) {
//          blockEntity.openGui(player, contraptionPos!!)
//        }
//      }
    }
    return true
  }
}