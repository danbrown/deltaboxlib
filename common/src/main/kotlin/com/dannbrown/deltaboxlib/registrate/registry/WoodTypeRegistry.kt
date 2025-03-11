package com.dannbrown.deltaboxlib.registrate.registry

import com.dannbrown.deltaboxlib.mixin.woodType.WoodTypeMixin
import net.minecraft.world.level.block.state.properties.BlockSetType
import net.minecraft.world.level.block.state.properties.WoodType

class WoodTypeRegistry(val modId: String) {
  private val blocksets: MutableMap<String, BlockSetType> = mutableMapOf()
  private val woodTypes: MutableMap<String, WoodType> = mutableMapOf()

  fun addBlockSet(name: String): BlockSetType {
    val newBlockSet = BlockSetType("${modId}:${name}")
    blocksets[name] = newBlockSet
    return newBlockSet
  }

  fun addWoodType(name: String, blockSet: BlockSetType): WoodType {
    val newWoodType = WoodTypeMixin.invokeRegister(WoodType("${modId}:${name}", blockSet))
    woodTypes[name] = newWoodType
    return newWoodType
  }

  fun getAllBlockSets(): Map<String, BlockSetType> {
    return blocksets
  }

  fun getAllWoodTypes(): Map<String, WoodType> {
    return woodTypes
  }
}