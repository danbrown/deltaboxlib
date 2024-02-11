package com.dannbrown.databoxlib.lib

import com.dannbrown.databoxlib.DataboxLib
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraftforge.versions.forge.ForgeVersion

object LibUtils {
  fun resourceLocation(path: String, modId: String = DataboxLib.MOD_ID): ResourceLocation {
    return ResourceLocation(modId, path)
  }

  fun forgeResourceLocation(path: String): ResourceLocation {
    return ResourceLocation(ForgeVersion.MOD_ID, path)
  }

  // Item Props
  fun defaultItemProps(): Item.Properties {
    return Item.Properties()
  }

  // Block Props
  fun defaultBlockProps(): BlockBehaviour.Properties {
    return BlockBehaviour.Properties.copy(Blocks.STONE)
  }
}