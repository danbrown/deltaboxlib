package com.dannbrown.databoxlib.init

import com.dannbrown.databoxlib.content.packet.ExampleC2SPacket
import com.dannbrown.databoxlib.content.packet.NetworkPacketBase
import com.dannbrown.databoxlib.content.packet.OpenSailMenuC2SPacket
import com.dannbrown.databoxlib.content.ship.packet.UpdateShipControlS2CPacket
import com.dannbrown.databoxlib.lib.LibUtils
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.level.ServerPlayer
import net.minecraftforge.network.NetworkDirection
import net.minecraftforge.network.NetworkEvent.Context
import net.minecraftforge.network.NetworkRegistry
import net.minecraftforge.network.PacketDistributor
import net.minecraftforge.network.simple.SimpleChannel
import java.util.function.BiConsumer
import java.util.function.Function
import java.util.function.Supplier

object ProjectNetworking {
  private var INSTANCE: SimpleChannel? = null
  private var packetId = 0
  private fun id(): Int {
    return packetId++
  }

  fun register() {
    val net = NetworkRegistry.ChannelBuilder
      .named(LibUtils.resourceLocation("main"))
      .networkProtocolVersion { "1.0" }
      .clientAcceptedVersions { s: String? -> true }
      .serverAcceptedVersions { s: String? -> true }
      .simpleChannel()
    INSTANCE = net
    // register messages
    PacketType(net, ExampleC2SPacket::class.java, ::ExampleC2SPacket, NetworkDirection.PLAY_TO_SERVER).register()
    PacketType(net, OpenSailMenuC2SPacket::class.java, ::OpenSailMenuC2SPacket, NetworkDirection.PLAY_TO_SERVER).register()
    PacketType(net, UpdateShipControlS2CPacket::class.java, ::UpdateShipControlS2CPacket, NetworkDirection.PLAY_TO_CLIENT).register()
//    net.messageBuilder(ExampleC2SPacket::class.java, id(), NetworkDirection.PLAY_TO_SERVER)
//      .decoder { ExampleC2SPacket() }
//      .consumerMainThread { packet: ExampleC2SPacket, ctx: Supplier<Context> ->
//        packet.handle(ctx.get())
//      }
//      .add()
  }

  fun <MSG> sendToServer(message: MSG) {
    INSTANCE!!.sendToServer(message)
  }

  fun <MSG> sendToPlayer(message: MSG, player: ServerPlayer?) {
    INSTANCE!!.send(PacketDistributor.PLAYER.with { player }, message)
  }

  fun <MSG> sendToAllClients(message: MSG) {
    INSTANCE!!.send(PacketDistributor.ALL.noArg(), message)
  }

  private class PacketType<T : NetworkPacketBase>(private val channel: SimpleChannel, private val type: Class<T>, private val factory: Function<FriendlyByteBuf, T>, private val direction: NetworkDirection) {
    private val encoder: BiConsumer<T, FriendlyByteBuf>
    private val decoder: Function<FriendlyByteBuf, T>
    private val handler: BiConsumer<T, Supplier<Context>>

    init {
      encoder = BiConsumer<T, FriendlyByteBuf> { obj: T, buffer: FriendlyByteBuf -> obj.write(buffer) }
      decoder = factory
      handler = BiConsumer<T, Supplier<Context>> { packet, contextSupplier ->
        val context: Context = contextSupplier.get()
        if (packet.handle(context)) {
          context.packetHandled = true
        }
      }
    }

    fun register() {
      channel.messageBuilder(type, index++, direction)
        .encoder(encoder)
        .decoder(decoder)
        .consumerNetworkThread(handler)
        .add()
    }

    companion object {
      private var index = 0
    }
  }
}