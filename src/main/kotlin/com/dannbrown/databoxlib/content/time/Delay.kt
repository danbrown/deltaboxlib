package com.dannbrown.databoxlib.content.time

import java.util.function.Consumer;

class Delay(private val callback: Consumer<Delay>, ticks: Int) {
  private var ticksLeft = ticks
  fun tick() {
    this.ticksLeft -= 1
  }

  fun finish() {
    callback.accept(this)
  }

  fun start() {}

  val isFinished: Boolean
    get() = this.ticksLeft == 0
}