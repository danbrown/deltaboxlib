package com.dannbrown.deltaboxlib.init

import com.dannbrown.deltaboxlib.init.DeltaboxLibMod.REGISTRATE

object DeltaboxConfig {
  val TEST = REGISTRATE.configBoolean("test", true, "this is a test config")
  val TEST2 = REGISTRATE.configBoolean("test2", true, "this is a test config2")

  fun register() {
    REGISTRATE.freezeConfig()
  }
}