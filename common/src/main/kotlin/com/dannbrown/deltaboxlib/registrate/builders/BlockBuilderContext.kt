package com.dannbrown.deltaboxlib.registrate.builders

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.registry.BlockEntry

class BlockBuilderContext(val registrate: AbstractDeltaboxRegistrate, val builder: BlockBuilder) {
  var noItem = false
  var flammabilityBurnChance: Int = 0
  var flammabilitySpreadChance: Int = 0
  var strippableOther: BlockEntry? = null
  var pottedOther: BlockEntry? = null
  var hasCutoutRender: Boolean = false
}