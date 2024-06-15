package com.dannbrown.deltaboxlib.content.fluid

enum class FluidVariant() {
  LIGHT() {
    override fun toString(): String {
      return "light"
    }
  },
  DEFAULT() {
    override fun toString(): String {
      return "default"
    }
  },
  DENSE() {
    override fun toString(): String {
      return "dense"
    }
  },
  MOLTEN() {
    override fun toString(): String {
      return "molten"
    }
  },
}