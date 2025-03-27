package com.dannbrown.deltaboxlib.registrate.network.forge

import com.dannbrown.deltaboxlib.registrate.network.NetworkChannelHandler
import com.dannbrown.deltaboxlib.registrate.network.NetworkPacket
import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraftforge.network.NetworkDirection
import net.minecraftforge.network.NetworkEvent
import net.minecraftforge.network.NetworkRegistry
import net.minecraftforge.network.PacketDistributor
import net.minecraftforge.network.simple.SimpleChannel
import net.minecraftforge.server.ServerLifecycleHooks
import java.util.Optional
import java.util.function.IntSupplier
import java.util.function.Supplier

class NetworkChannelHandlerImpl(modId: String, version: IntSupplier) : NetworkChannelHandler(modId) {
  private val channel: SimpleChannel
  private var id = 0

  init {
    val ver = Supplier { version.asInt.toString() }
    channel = NetworkRegistry.newSimpleChannel(
      ResourceLocation(modId, "channel"),
      ver, { it == ver.get() }, { it == ver.get() }
    )
  }

  override fun <M : NetworkPacket> register(
    direction: com.dannbrown.deltaboxlib.registrate.network.NetworkDirection,
    messageClass: Class<M>,
    decoder: (FriendlyByteBuf) -> M
  ) {
    val dir = when (direction) {
      com.dannbrown.deltaboxlib.registrate.network.NetworkDirection.BOTH -> Optional.empty()
      com.dannbrown.deltaboxlib.registrate.network.NetworkDirection.PLAY_TO_CLIENT -> Optional.of(NetworkDirection.PLAY_TO_CLIENT)
      com.dannbrown.deltaboxlib.registrate.network.NetworkDirection.PLAY_TO_SERVER -> Optional.of(NetworkDirection.PLAY_TO_SERVER)
    }

    channel.registerMessage(id++, messageClass, NetworkPacket::writeToBuffer, decoder, ::consumer, dir)
  }

  private fun <M : NetworkPacket> consumer(message: M, contextSupplier: Supplier<NetworkEvent.Context>) {
    val context = contextSupplier.get()
    context.enqueueWork { message.handle(Wrapper(context)) }
    context.packetHandled = true
  }

  private class Wrapper(private val context: NetworkEvent.Context) : Context {
    override val direction: com.dannbrown.deltaboxlib.registrate.network.NetworkDirection =
      when (context.direction) {
        NetworkDirection.PLAY_TO_CLIENT -> com.dannbrown.deltaboxlib.registrate.network.NetworkDirection.PLAY_TO_CLIENT
        else -> com.dannbrown.deltaboxlib.registrate.network.NetworkDirection.PLAY_TO_SERVER
      }
    override val sender: Player? = context.sender

    override fun disconnect(reason: Component) {
      context.networkManager.disconnect(reason)
    }
  }

  override fun sendToClientPlayer(serverPlayer: ServerPlayer, networkPacket: NetworkPacket) {
    channel.send(PacketDistributor.PLAYER.with { serverPlayer }, networkPacket)
  }

  override fun sendToAllClientPlayers(networkPacket: NetworkPacket) {
    channel.send(PacketDistributor.ALL.noArg(), networkPacket)
  }

  override fun sendToServer(networkPacket: NetworkPacket) {
    channel.sendToServer(networkPacket)
  }

  override fun sendToAllClientPlayersInRange(
    level: Level,
    pos: BlockPos,
    radius: Double,
    networkPacket: NetworkPacket
  ) {
    val server = ServerLifecycleHooks.getCurrentServer()
    if (server != null && !level.isClientSide) {
      val distributor = PacketDistributor.NEAR.with {
        PacketDistributor.TargetPoint(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), radius, level.dimension())
      }
      channel.send(distributor, networkPacket)
    }
  }

  override fun sendToAllClientPlayersTrackingEntity(target: Entity, networkPacket: NetworkPacket) {
    if (!target.level().isClientSide) {
      channel.send(PacketDistributor.TRACKING_ENTITY.with { target }, networkPacket)
    }
  }

  override fun sendToAllClientPlayersTrackingEntityAndSelf(target: Entity, networkPacket: NetworkPacket) {
    if (!target.level().isClientSide) {
      channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with { target }, networkPacket)
    }
  }
}
