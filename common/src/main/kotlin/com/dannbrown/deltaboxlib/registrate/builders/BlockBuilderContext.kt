package com.dannbrown.deltaboxlib.registrate.builders

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate

class BlockBuilderContext(val registrate: AbstractDeltaboxRegistrate, val builder: BlockBuilder) {
  var noItem = false
  var flammabilityBurnChance: Int = 0
  var flammabilitySpreadChance: Int = 0
}