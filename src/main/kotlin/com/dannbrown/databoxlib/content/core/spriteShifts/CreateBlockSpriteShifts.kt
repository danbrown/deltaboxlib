package com.dannbrown.databoxlib.content.utils.spriteShifts

import com.dannbrown.databoxlib.lib.LibData
import com.dannbrown.databoxlib.lib.LibUtils
import com.simibubi.create.foundation.block.connected.AllCTTypes
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry
import com.simibubi.create.foundation.block.connected.CTSpriteShifter
import com.simibubi.create.foundation.block.connected.CTType
import com.simibubi.create.foundation.utility.Couple

object CreateBlockSpriteShifts {
  // block texture references

  // @ CASINGS
  val STEEL_CASING = omni(LibData.BLOCKS.STEEL_CASING, CreateCTType.EMPTY)
  val STEEL_PURPLE_PANEL_CASING = omni(LibData.BLOCKS.STEEL_PURPLE_PANEL_CASING, CreateCTType.EMPTY)
  val STEEL_BLUE_PANEL_CASING = omni(LibData.BLOCKS.STEEL_BLUE_PANEL_CASING, CreateCTType.EMPTY)
  val STEEL_ORANGE_PANEL_CASING = omni(LibData.BLOCKS.STEEL_ORANGE_PANEL_CASING, CreateCTType.EMPTY)
  val STEEL_RED_PANEL_CASING = omni(LibData.BLOCKS.STEEL_RED_PANEL_CASING, CreateCTType.EMPTY)
  val STEEL_GREEN_PANEL_CASING = omni(LibData.BLOCKS.STEEL_GREEN_PANEL_CASING, CreateCTType.EMPTY)
  val DESH_CASING = omni(LibData.BLOCKS.DESH_CASING, CreateCTType.EMPTY)
  val DESH_PANEL_CASING = omni(LibData.BLOCKS.DESH_PANEL_CASING, CreateCTType.EMPTY)
  val OSTRUM_CASING = omni(LibData.BLOCKS.OSTRUM_CASING, CreateCTType.EMPTY)
  val OSTRUM_PANEL_CASING = omni(LibData.BLOCKS.OSTRUM_PANEL_CASING, CreateCTType.EMPTY)
  val CALORITE_CASING = omni(LibData.BLOCKS.CALORITE_CASING, CreateCTType.EMPTY)
  val CALORITE_PANEL_CASING = omni(LibData.BLOCKS.CALORITE_PANEL_CASING, CreateCTType.EMPTY)

  val WALL_PANEL = omni(LibData.BLOCKS.WALL_PANEL, CreateCTType.EMPTY)
  val HULL_PANEL = omni(LibData.BLOCKS.HULL_PANEL, CreateCTType.EMPTY)

  // @ SCAFFOLDING
  val STEEL_SCAFFOLD = horizontal(LibData.BLOCKS.STEEL_SCAFFOLDING, CreateCTType.SCAFFOLDING)
  val STEEL_SCAFFOLD_INSIDE = horizontal(LibData.BLOCKS.STEEL_SCAFFOLDING + "_inside", CreateCTType.SCAFFOLDING)
  val DESH_SCAFFOLD = horizontal(LibData.BLOCKS.DESH_SCAFFOLDING, CreateCTType.SCAFFOLDING)
  val DESH_SCAFFOLD_INSIDE = horizontal(LibData.BLOCKS.DESH_SCAFFOLDING + "_inside", CreateCTType.SCAFFOLDING)
  val OSTRUM_SCAFFOLD = horizontal(LibData.BLOCKS.OSTRUM_SCAFFOLDING, CreateCTType.SCAFFOLDING)
  val OSTRUM_SCAFFOLD_INSIDE = horizontal(LibData.BLOCKS.OSTRUM_SCAFFOLDING + "_inside", CreateCTType.SCAFFOLDING)
  val CALORITE_SCAFFOLD = horizontal(LibData.BLOCKS.CALORITE_SCAFFOLDING, CreateCTType.SCAFFOLDING)
  val CALORITE_SCAFFOLD_INSIDE = horizontal(LibData.BLOCKS.CALORITE_SCAFFOLDING + "_inside", CreateCTType.SCAFFOLDING)

  // @ CARGO BAY
  val CARGO_TOP = vault("top", CreateCTType.EMPTY)
  val CARGO_FRONT = vault("front", CreateCTType.EMPTY)
  val CARGO_SIDE = vault("side", CreateCTType.EMPTY)
  val CARGO_BOTTOM = vault("bottom", CreateCTType.EMPTY)

  // @ Utility functions
  private fun omni(name: String, directory: CreateCTType): CTSpriteShiftEntry {
    return getCT(AllCTTypes.OMNIDIRECTIONAL, name, directory)
  }

  private fun horizontal(name: String, directory: CreateCTType): CTSpriteShiftEntry {
    return getCT(AllCTTypes.HORIZONTAL, name, directory)
  }

  private fun vertical(name: String, directory: CreateCTType): CTSpriteShiftEntry {
    return getCT(AllCTTypes.VERTICAL, name, directory)
  }

  private fun vault(name: String, directory: CreateCTType): Couple<CTSpriteShiftEntry> {
    val prefixed = "cargo_bay/cargo_$name"

    return Couple.createWithContext { medium: Boolean ->
      getCT(
        AllCTTypes.RECTANGLE,
        prefixed + "_small",
//        prefixed + "_large",
        prefixed + if (medium) "_medium" else "_large",
        directory
      )
    }
  }

  // create foundation block connected
  private fun getCT(type: CTType, blockTextureName: String, connectedTextureName: String, directory: CreateCTType): CTSpriteShiftEntry {
    return CTSpriteShifter.getCT(
      type,
      LibUtils.resourceLocation("block/$directory$blockTextureName"),
      LibUtils.resourceLocation("block/$directory$connectedTextureName" + "_connected")
    )
  }
  private fun getCT(type: CTType, blockTextureName: String, directory: CreateCTType): CTSpriteShiftEntry {
    return getCT(type, blockTextureName, blockTextureName, directory)
  }
}