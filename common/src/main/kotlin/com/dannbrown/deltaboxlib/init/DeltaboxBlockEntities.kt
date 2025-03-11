package com.dannbrown.deltaboxlib.init

import com.dannbrown.deltaboxlib.content.block.eyeblossom.EyeBlossomBlockEntity
import com.dannbrown.deltaboxlib.content.block.eyeblossom.EyeBlossomRenderer
import com.dannbrown.deltaboxlib.init.DeltaboxLibMod.REGISTRATE

object DeltaboxBlockEntities {

  val EYEBLOSSOM_BLOCK_ENTITY = REGISTRATE
    .blockEntity<EyeBlossomBlockEntity>("eyeblossom")
    .factory({ t, p, s -> EyeBlossomBlockEntity(t.get(), p, s) })
    .validBlocks(DeltaboxBlocks.EYE_BLOSSOM, DeltaboxBlocks.CLOSED_EYE_BLOSSOM)
    .renderer { ctx -> EyeBlossomRenderer(ctx) }
    .register()

  fun register() {
    REGISTRATE.buildBlockEntities()
  }
}