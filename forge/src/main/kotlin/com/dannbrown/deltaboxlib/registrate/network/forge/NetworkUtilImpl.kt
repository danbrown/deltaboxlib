package com.dannbrown.deltaboxlib.registrate.network.forge

import com.dannbrown.deltaboxlib.registrate.network.NetworkChannelHandler
import java.util.function.IntSupplier

object NetworkUtilImpl {
  @JvmStatic
  fun createChannel(modId: String, version: IntSupplier): NetworkChannelHandler =
    NetworkChannelHandlerImpl(modId, version)
}