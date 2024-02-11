package com.dannbrown.databoxlib.content.utils.spriteShifts

enum class CreateCTType {
  EMPTY() {
    override fun toString(): String {
      return ""
    }
  },
  SCAFFOLDING() {
    override fun toString(): String {
      return "scaffold/"
    }
  },
}