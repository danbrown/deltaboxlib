package com.dannbrown.deltaboxlib.init

import com.dannbrown.deltaboxlib.content.block.eyeblossom.EyeBlossomRenderer
import net.minecraft.client.model.geom.ModelLayerLocation

object DeltaboxModelLayers {

  val EYE_BLOSSOM: ModelLayerLocation =
    DeltaboxLibMod.REGISTRATE.modelLayer("eyeblossom/eye", EyeBlossomRenderer::createEyeLayer)

  fun register() {
    // init
  }
}