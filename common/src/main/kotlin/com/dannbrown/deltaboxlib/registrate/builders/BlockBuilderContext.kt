package com.dannbrown.deltaboxlib.registrate.builders

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.MapColor
import java.util.function.Supplier

class BlockBuilderContext<T : Block>(val registrate: AbstractDeltaboxRegistrate, val builder: BlockBuilder<T>) {
  var noItem = false
  var flammabilityBurnChance: Int = 0
  var flammabilitySpreadChance: Int = 0
  var strippableOther: Supplier<out Block>? = null
  var pottedOther: Supplier<out Block>? = null
  var hasCutoutRender: Boolean = false
  var textureName: String = builder.blockId
  var color: MapColor = MapColor.COLOR_GRAY
  var hasBiomeColors = false
  var compostableAmount = 0f
}