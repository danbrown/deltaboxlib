package com.dannbrown.databoxlib.content.ship.packet

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.content.packet.NetworkPacketBase
import com.dannbrown.databoxlib.content.ship.SpaceShipControl
import com.dannbrown.databoxlib.content.ship.block.sail.SailBlockEntity
import com.dannbrown.databoxlib.content.ship.extensions.getShipByShipId
import net.minecraft.client.Minecraft
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkDirection
import net.minecraftforge.network.NetworkEvent
import org.valkyrienskies.core.api.ships.properties.ShipId
import thedarkcolour.kotlinforforge.forge.vectorutil.v3d.toVector3d

class UpdateShipControlS2CPacket : NetworkPacketBase {
  private var shipId: ShipId? = null
  private var stabilizersCount: Int = 0
  private var sails: Set<SailBlockEntity> = emptySet()

  constructor()
  constructor (buffer: FriendlyByteBuf) : this() {
    shipId = buffer.readLong()
    stabilizersCount = buffer.readInt()
    val sailsString = buffer.readByteArray()
      .map { it.toString() }

    sails = emptySet()
  }

  constructor (shipControl: SpaceShipControl) : this() {
    if (shipControl.ship == null) {
      ProjectContent.LOGGER.error("Ship control has no ship, this should not happen!")
      return
    }
    this.shipId = shipControl.ship?.id
//    this.stabilizersCount = shipControl.stabilizers.size
//    this.sails = shipControl.sails
  }

  override fun write(buffer: FriendlyByteBuf) {
    buffer.writeLong(shipId!!.toLong())
    buffer.writeInt(stabilizersCount)
    buffer.writeByteArray(sails.map {
      it.blockPos.toVector3d()
        .toString()
        .toByteArray()
    }
      .reduce { acc, bytes -> acc + bytes })
  }

  override fun handle(context: NetworkEvent.Context): Boolean {
    context.enqueueWork {
      // reject if we are on the server
      if (context.direction.equals(NetworkDirection.PLAY_TO_SERVER)) {
        context.packetHandled = false
        return@enqueueWork
      }
      val level = Minecraft.getInstance().level

      ProjectContent.LOGGER.info("Received packet to update client ship control")

      if (level != null) {
        val ship = level.getShipByShipId(shipId!!)
      }

      return@enqueueWork
    }

    return true
  }
}