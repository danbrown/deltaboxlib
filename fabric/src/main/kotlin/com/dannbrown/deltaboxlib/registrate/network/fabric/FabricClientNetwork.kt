package com.dannbrown.deltaboxlib.registrate.network.fabric

import com.dannbrown.deltaboxlib.registrate.network.NetworkDirection
import com.dannbrown.deltaboxlib.registrate.network.NetworkPacket
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientPacketListener
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player

object FabricClientNetwork {
  fun <M : NetworkPacket> register(res: ResourceLocation, decoder: (FriendlyByteBuf) -> M) {
    ClientPlayNetworking.registerGlobalReceiver(res) { client, handler, buf, r ->
      handlePacket(decoder, client, handler, buf, r)
    }
  }

  private fun <M : NetworkPacket> handlePacket(
    decoder: (FriendlyByteBuf) -> M,
    client: Minecraft,
    listener: ClientPacketListener,
    buf: FriendlyByteBuf,
    sender: PacketSender
  ) {
    val message = decoder(buf)
    client.execute {
      message.handle(
        NetworkChannelHandlerImpl.Wrapper(
          client.player as Player,
          NetworkDirection.PLAY_TO_CLIENT,
          null
        )
      )
    }
  }
}