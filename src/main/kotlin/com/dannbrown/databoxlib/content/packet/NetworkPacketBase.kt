package com.dannbrown.databoxlib.content.packet

import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkEvent

abstract class NetworkPacketBase {
  abstract fun write(buffer: FriendlyByteBuf)
  abstract fun handle(context: NetworkEvent.Context): Boolean
}