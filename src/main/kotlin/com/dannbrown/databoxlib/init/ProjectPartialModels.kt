package com.dannbrown.databoxlib.init

import com.dannbrown.databoxlib.lib.LibUtils
import com.jozufozu.flywheel.core.PartialModel

object ProjectPartialModels {
  fun init() {}
  val SMALL_GAUGE_DIAL = PartialModel(LibUtils.resourceLocation("block/pressure_chamber/gauge_dial"))
  val STEEL_BACKTANK_SHAFT = PartialModel(LibUtils.resourceLocation("block/steel_backtank/block_shaft_input"))
  val STEEL_BACKTANK_COGS = PartialModel(LibUtils.resourceLocation("block/steel_backtank/block_cogs"))
}