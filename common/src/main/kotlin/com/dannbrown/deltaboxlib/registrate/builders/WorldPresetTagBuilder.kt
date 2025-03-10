package com.dannbrown.deltaboxlib.registrate.builders

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import net.minecraft.tags.TagKey
import net.minecraft.world.level.levelgen.presets.WorldPreset
import java.util.function.Supplier

class WorldPresetTagBuilder(
  private val registrate: AbstractDeltaboxRegistrate,
  private val hostTag: TagKey<WorldPreset>
) {
  fun add(tagKey: Supplier<WorldPreset>): WorldPresetTagBuilder {
    registrate.tagRegistry.addWorldPreset(hostTag, tagKey)
    return this
  }
}