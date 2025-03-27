package com.dannbrown.deltaboxlib.registrate.network

import net.minecraft.network.FriendlyByteBuf

class NetworkChannelBuilder(private val modId: String) {
  private val instance: NetworkChannelHandler = NetworkUtil.createChannel(modId) { version }
  private var version: Int = 0

  fun <M : NetworkPacket> register(
    direction: NetworkDirection,
    messageClass: Class<M>,
    decoder: (FriendlyByteBuf) -> M
  ): NetworkChannelBuilder {
    instance.register(direction, messageClass, decoder)
    return this
  }

  fun version(version: Int): NetworkChannelBuilder {
    this.version = version
    return this
  }

  fun build(): NetworkChannelHandler = instance
}