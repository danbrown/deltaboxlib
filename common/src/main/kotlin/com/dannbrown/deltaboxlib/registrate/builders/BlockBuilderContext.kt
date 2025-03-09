package com.dannbrown.deltaboxlib.registrate.builders

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.registry.BlockEntry
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.MapColor

class BlockBuilderContext<T : Block>(val registrate: AbstractDeltaboxRegistrate, val builder: BlockBuilder<T>) {
  var noItem = false
  var flammabilityBurnChance: Int = 0
  var flammabilitySpreadChance: Int = 0
  var strippableOther: BlockEntry<*>? = null
  var pottedOther: BlockEntry<*>? = null
  var hasCutoutRender: Boolean = false
  var textureName: String = builder.blockId
  var color: MapColor = MapColor.COLOR_GRAY
  var hasBiomeColors = false
  var compostableAmount = 0f
}