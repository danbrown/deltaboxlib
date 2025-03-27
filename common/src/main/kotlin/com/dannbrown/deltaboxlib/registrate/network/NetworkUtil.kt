package com.dannbrown.deltaboxlib.registrate.network

import dev.architectury.injectables.annotations.ExpectPlatform
import java.util.function.IntSupplier

object NetworkUtil {
  fun builder(modId: String): NetworkChannelBuilder = NetworkChannelBuilder(modId)

  fun createChannel(modId: String): NetworkChannelHandler = createChannel(modId) { 0 }

  @JvmStatic
  @ExpectPlatform
  fun createChannel(modId: String, version: IntSupplier): NetworkChannelHandler {
    throw AssertionError()
  }
}