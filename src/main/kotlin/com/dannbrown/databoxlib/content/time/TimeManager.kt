package com.dannbrown.databoxlib.content.time

import com.dannbrown.databoxlib.content.event.OnServerTick
import java.util.function.Consumer

object TimeManager {
  private const val TICKS_IN_SECOND = 20
  private var PENDING_EXECUTIONS: MutableCollection<Delay> =  mutableListOf()
  private var EXECUTIONS: MutableCollection<Delay> =  mutableListOf()

  init {
    OnServerTick.listen { this.update() }
  }

  fun delay(ticks: Int, callback: Consumer<Delay>): Delay {
    return run(Delay(callback, ticks))
  }

  fun delay(seconds: Double, callback: Consumer<Delay>): Delay {
    return delay(toTicks(seconds), callback)
  }

  fun nextTick(callback: Consumer<Delay>): Delay {
    return delay(1, callback)
  }

  fun toTicks(seconds: Double): Int {
    return (seconds * TICKS_IN_SECOND).toInt()
  }

  fun toSeconds(ticks: Int): Double {
    return ticks.toDouble() / TICKS_IN_SECOND
  }

  private fun run(exec: Delay): Delay {
    PENDING_EXECUTIONS.add(exec)
    return exec
  }

  private fun update() {
    val pendingExecutionsCopy = mutableListOf<Delay>()
    synchronized(PENDING_EXECUTIONS) {
      pendingExecutionsCopy.addAll(PENDING_EXECUTIONS)
      PENDING_EXECUTIONS.clear()
    }

    synchronized(EXECUTIONS) {
      EXECUTIONS.addAll(pendingExecutionsCopy)
      val iterator = EXECUTIONS.iterator()
      while (iterator.hasNext()) {
        val delay = iterator.next()
        delay.tick()
        if (delay.isFinished) {
          delay.finish()
          iterator.remove()
        }
      }
    }
  }
}