package com.dannbrown.databoxlib.lib

import com.dannbrown.databoxlib.ProjectContent
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraftforge.versions.forge.ForgeVersion

object LibUtils {
  fun resourceLocation(path: String): ResourceLocation {
    return ResourceLocation(ProjectContent.MOD_ID, path)
  }

  fun forgeResourceLocation(path: String): ResourceLocation {
    return ResourceLocation(ForgeVersion.MOD_ID, path)
  }

  // Item PRops
  fun defaultItemProps(): Item.Properties {
    return Item.Properties()
  }

  // Block Props
  fun defaultBlockProps(): BlockBehaviour.Properties {
    return BlockBehaviour.Properties.copy(Blocks.STONE)
  }

  fun damageItem(item: Item, amount: Int) {
    item.defaultInstance.hurtAndBreak(amount, null, null)
  }
}