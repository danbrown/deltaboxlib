package com.dannbrown.deltaboxlib.init

import com.dannbrown.deltaboxlib.mixin.woodType.WoodTypeMixin
import net.minecraft.world.level.block.state.properties.BlockSetType
import net.minecraft.world.level.block.state.properties.WoodType

object DeltaboxWoodTypes {
  val PALE_OAK_SET: BlockSetType = BlockSetType(DeltaboxLibMod.MOD_ID + ":pale_oak")
  val PALE_OAK: WoodType = WoodTypeMixin.invokeRegister(WoodType(DeltaboxLibMod.MOD_ID + ":pale_oak", PALE_OAK_SET))

  fun register() {
    // init class
  }
}