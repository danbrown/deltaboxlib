package com.dannbrown.deltaboxlib.registrate.network.fabric

import com.dannbrown.deltaboxlib.registrate.network.NetworkChannelHandler
import com.dannbrown.deltaboxlib.registrate.network.NetworkDirection
import com.dannbrown.deltaboxlib.registrate.network.NetworkPacket
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.Packet
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.network.ServerGamePacketListenerImpl
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

class NetworkChannelHandlerImpl(modId: String) : NetworkChannelHandler(modId) {

  companion object {
    private val ID_MAP = mutableMapOf<Class<*>, ResourceLocation>()
  }

  private var id = 0

  override fun <M : NetworkPacket> register(
    direction: NetworkDirection,
    messageClass: Class<M>,
    decoder: (FriendlyByteBuf) -> M
  ) {
    val res = ResourceLocation(name, id++.toString())
    ID_MAP[messageClass] = res

    if (direction != NetworkDirection.PLAY_TO_CLIENT) {
      ServerPlayNetworking.registerGlobalReceiver(res) { server, player, handler, buf, _ ->
        val message = decoder(buf)
        server.execute { message.handle(Wrapper(player, NetworkDirection.PLAY_TO_SERVER, handler)) }
      }
    }

    if (direction != NetworkDirection.PLAY_TO_SERVER) {
      if (DeltaboxUtil.getSide().isClient()) {
        FabricClientNetwork.register(res, decoder)
      }
    }
  }

  class Wrapper(
    private val player: Player,
    private val dir: NetworkDirection,
    private val packetListener: ServerGamePacketListenerImpl?
  ) : Context {
    override val direction: NetworkDirection = dir
    override val sender: Player = player

    override fun disconnect(reason: Component) {
      packetListener?.disconnect(reason)
    }
  }

  override fun sendToClientPlayer(serverPlayer: ServerPlayer, networkPacket: NetworkPacket) {
    val buf = PacketByteBufs.create()
    networkPacket.writeToBuffer(buf)
    ServerPlayNetworking.send(serverPlayer, ID_MAP[networkPacket::class.java]!!, buf)
  }

  override fun sendToAllClientPlayers(networkPacket: NetworkPacket) {
    DeltaboxUtil.getCurrentServer()?.playerList?.players?.forEach { sendToClientPlayer(it, networkPacket) }
  }

  override fun sendToServer(networkPacket: NetworkPacket) {
    val buf = PacketByteBufs.create()
    networkPacket.writeToBuffer(buf)
    ClientPlayNetworking.send(ID_MAP[networkPacket::class.java]!!, buf)
  }

  override fun sendToAllClientPlayersInRange(
    level: Level,
    pos: BlockPos,
    radius: Double,
    networkPacket: NetworkPacket
  ) {
    val currentServer = DeltaboxUtil.getCurrentServer()
    if (!level.isClientSide && currentServer != null) {
      val players = currentServer.playerList
      players.broadcast(
        null, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(),
        radius, level.dimension(), toVanillaPacket(networkPacket)
      )
    }
  }

  override fun sendToAllClientPlayersTrackingEntity(target: Entity, networkPacket: NetworkPacket) {
    (target.level() as? ServerLevel)?.chunkSource?.broadcast(target, toVanillaPacket(networkPacket))

  }

  override fun sendToAllClientPlayersTrackingEntityAndSelf(target: Entity, networkPacket: NetworkPacket) {
    (target.level() as? ServerLevel)?.let { serverLevel ->
      val packet = toVanillaPacket(networkPacket)
      serverLevel.chunkSource.broadcast(target, packet)
      if (target is ServerPlayer) sendToClientPlayer(target, networkPacket)
    }
  }

  private fun toVanillaPacket(networkPacket: NetworkPacket): Packet<*> {
    val buf = PacketByteBufs.create()
    networkPacket.writeToBuffer(buf)
    return ServerPlayNetworking.createS2CPacket(ID_MAP[networkPacket::class.java]!!, buf)
  }
}