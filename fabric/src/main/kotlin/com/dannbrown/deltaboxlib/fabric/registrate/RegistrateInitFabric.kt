package com.dannbrown.deltaboxlib.fabric.registrate

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import javax.management.BadAttributeValueExpException

class RegistrateInitFabric(val registrate: AbstractDeltaboxRegistrate) {
  fun init() {

    // register strippable blocks
    for (block in registrate.blockRegistry.entries) {
      if (block.getContext().strippableOther == null) continue
      if (!block.getContext().strippableOther!!.get().defaultBlockState()
          .hasProperty(BlockStateProperties.AXIS)
      ) throw BadAttributeValueExpException("Output stripped block should have 'axis' property!")
      if (!block.getBlock().get().defaultBlockState()
          .hasProperty(BlockStateProperties.AXIS)
      ) throw BadAttributeValueExpException("Input stripped block should have 'axis' property!")
      StrippableBlockRegistry.register(block.getBlock().get(), block.getContext().strippableOther!!.get())
    }
  }
}