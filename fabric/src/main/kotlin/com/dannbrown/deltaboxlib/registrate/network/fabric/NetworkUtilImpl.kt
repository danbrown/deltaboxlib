package com.dannbrown.deltaboxlib.registrate.network.fabric

import com.dannbrown.deltaboxlib.registrate.network.NetworkChannelHandler
import java.util.function.IntSupplier

object NetworkUtilImpl {
  @JvmStatic
  fun createChannel(modId: String, version: IntSupplier): NetworkChannelHandler {
    return NetworkChannelHandlerImpl(modId)
  }
}