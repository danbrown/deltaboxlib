package com.dannbrown.deltaboxlib.registrate.builders

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.decoration.PaintingVariant
import java.util.function.Supplier

class PaintingTagBuilder(
  private val registrate: AbstractDeltaboxRegistrate,
  private val hostTag: TagKey<PaintingVariant>
) {
  fun add(tagKey: Supplier<PaintingVariant>): PaintingTagBuilder {
    registrate.tagRegistry.addPainting(hostTag, tagKey)
    return this
  }
}