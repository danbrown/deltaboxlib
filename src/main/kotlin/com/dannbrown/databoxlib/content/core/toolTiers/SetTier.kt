package com.dannbrown.databoxlib.content.utils.toolTiers

import com.dannbrown.databoxlib.init.ProjectTags
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

enum class SetTier {
  WOOD,
  STONE,
  IRON,
  DIAMOND,
  NETHERITE,
  TUNGSTEN
  ;

  fun getTag(): TagKey<Block>? {
    return when (this) {
      WOOD -> null
      STONE -> BlockTags.NEEDS_STONE_TOOL
      IRON -> BlockTags.NEEDS_IRON_TOOL
      DIAMOND -> BlockTags.NEEDS_DIAMOND_TOOL
      NETHERITE -> ProjectTags.BLOCK.NEEDS_NETHERITE_TOOL
      TUNGSTEN -> ProjectTags.BLOCK.NEEDS_TUNGSTEN_TOOL
    }
  }
}