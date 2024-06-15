package com.dannbrown.deltaboxlib.content.event

import java.util.function.Consumer

object OnClientTick {
  private val EVENTS: MutableList<Consumer<OnClientTick>> = mutableListOf()

  fun listen(runnable: Consumer<OnClientTick>) {
    EVENTS.add(runnable)
  }

  fun dispatch() {
    for (event in EVENTS) {
      event.accept(this)
    }
  }
}