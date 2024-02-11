package com.dannbrown.databoxlib.content.utils.toolTiers

import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

enum class SetTool {
  NONE,
  PICKAXE,
  AXE,
  SHOVEL,
  HOE,
  ;

  fun getTag(): TagKey<Block>? {
    return when (this) {
      NONE -> null
      PICKAXE -> BlockTags.MINEABLE_WITH_PICKAXE
      AXE -> BlockTags.MINEABLE_WITH_AXE
      SHOVEL -> BlockTags.MINEABLE_WITH_SHOVEL
      HOE -> BlockTags.MINEABLE_WITH_HOE
    }
  }
}