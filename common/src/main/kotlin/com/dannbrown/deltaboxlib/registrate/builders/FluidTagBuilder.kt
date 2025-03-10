package com.dannbrown.deltaboxlib.registrate.builders

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import java.util.function.Supplier

class FluidTagBuilder(private val registrate: AbstractDeltaboxRegistrate, private val hostTag: TagKey<Fluid>) {
  fun add(tagKey: Supplier<Fluid>): FluidTagBuilder {
    registrate.tagRegistry.addFluid(hostTag, tagKey)
    return this
  }
}