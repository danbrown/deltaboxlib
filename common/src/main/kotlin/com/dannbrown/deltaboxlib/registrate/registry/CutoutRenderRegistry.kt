package com.dannbrown.deltaboxlib.registrate.registry

class CutoutRenderRegistry {
  private val CUTOUT_RENDERS: MutableList<BlockEntry> = mutableListOf()

  fun addCutoutRender(block: BlockEntry) {
    CUTOUT_RENDERS.add(block)
  }

  fun getCutoutRenders(): List<BlockEntry> {
    return CUTOUT_RENDERS
  }
}