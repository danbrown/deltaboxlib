package com.dannbrown.databoxlib.content.misc.utils

import net.minecraft.util.StringRepresentable


enum class RotationState(rotation: Int) : StringRepresentable {
  PI_0(0), // 0
  PI_90(90), // pi/2
  PI_180(180), // pi
  PI_270(270); // 3pi/2

  private var r: Int = rotation
  init{
    this.r = rotation
  }

  fun get(): Int {
    return r
  }

  override fun getSerializedName(): String {
    return r.toString()
  }
}