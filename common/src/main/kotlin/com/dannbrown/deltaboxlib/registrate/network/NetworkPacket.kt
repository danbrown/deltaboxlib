package com.dannbrown.deltaboxlib.registrate.network


import net.minecraft.network.FriendlyByteBuf

interface NetworkPacket {
  fun writeToBuffer(buf: FriendlyByteBuf)
  fun handle(context: NetworkChannelHandler.Context)
}