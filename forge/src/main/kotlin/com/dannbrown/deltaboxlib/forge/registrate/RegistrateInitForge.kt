package com.dannbrown.deltaboxlib.forge.registrate

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.helpers.StripHelper
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import javax.management.BadAttributeValueExpException

class RegistrateInitForge(val registrate: AbstractDeltaboxRegistrate) {
  fun init() {
  }

  fun setup() {
    // register strippable blocks
    for (block in registrate.blockRegistry.entries) {
      val other = block.getContext().strippableOther ?: continue
      if (!other.get().defaultBlockState().hasProperty(BlockStateProperties.AXIS)
      ) throw BadAttributeValueExpException("Output stripped block should have 'axis' property!")
      if (!block.getBlock().get().defaultBlockState().hasProperty(BlockStateProperties.AXIS)
      ) throw BadAttributeValueExpException("Input stripped block should have 'axis' property!")
      StripHelper.registerStrippable(block.getBlock().get(), other.get())
    }
  }
}