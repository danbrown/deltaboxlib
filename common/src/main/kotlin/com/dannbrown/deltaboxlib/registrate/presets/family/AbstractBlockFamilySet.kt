package com.dannbrown.deltaboxlib.registrate.presets.family

abstract class AbstractBlockFamilySet {
  protected val _blockFamily: BlockFamily = BlockFamily()

  fun getFamily(): BlockFamily {
    return _blockFamily
  }
}