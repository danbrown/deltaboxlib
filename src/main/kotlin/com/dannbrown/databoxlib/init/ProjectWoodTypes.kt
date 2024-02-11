package com.dannbrown.databoxlib.init

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.lib.LibData
import net.minecraft.client.renderer.Sheets
import net.minecraft.world.level.block.state.properties.BlockSetType
import net.minecraft.world.level.block.state.properties.WoodType

object ProjectWoodTypes {
  val JOSHUA = WoodType.register(WoodType(ProjectContent.MOD_ID + ":" + LibData.NAMES.JOSHUA, BlockSetType.OAK))

  fun register() {
    Sheets.addWoodType(JOSHUA)
  }
}