package com.dannbrown.databoxlib.content.event

import com.google.common.util.concurrent.Runnables
import java.util.function.Consumer

object OnServerTick {
  private val EVENTS: MutableList<Consumer<OnServerTick>> = mutableListOf()

  fun listen(runnable: Consumer<OnServerTick>) {
    EVENTS.add(runnable)
  }

  fun dispatch() {
    for (event in EVENTS) {
      event.accept(this)
    }
  }
}