package com.dannbrown.deltaboxlib.registrate.registry

class BoatVariantRegistry(modId: String) {
  private val BOAT_VARIANTS: MutableList<String> = mutableListOf()

  fun add(name: String) {
    BOAT_VARIANTS.add(name)
  }

  fun getBoatVariants(): MutableList<String> {
    return BOAT_VARIANTS
  }
}