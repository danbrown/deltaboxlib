package com.dannbrown.deltaboxlib.registrate.util

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

object DeltaboxUtil {
  fun resourceLocation(namespace: String, path: String): ResourceLocation{
    return ResourceLocation(namespace, path) // example: resourceLocation("minecraft", "block") -> "minecraft:block"
  }

  fun resourceLocation(path: String): ResourceLocation {
    return ResourceLocation(path) // example: resourceLocation("block") -> "block"
  }

  fun resourceLocation(namespace: String, path: String, string: String): ResourceLocation {
    return ResourceLocation(namespace, path).withPath { string2: String -> "$string2$string" } // example: resourceLocation("minecraft", "block/", "stone") -> "minecraft:block/stone"
  }

  fun getBlockTexture(block: Block): ResourceLocation {
    val resourceLocation = BuiltInRegistries.BLOCK.getKey(block)
    return resourceLocation.withPrefix("block/")
  }

  fun getBlockTexture(block: Block, string: String): ResourceLocation {
    val resourceLocation = BuiltInRegistries.BLOCK.getKey(block)
    return resourceLocation.withPath { string2: String -> "block/$string2$string" }
  }

  fun getItemTexture(item: Item): ResourceLocation {
    val resourceLocation = BuiltInRegistries.ITEM.getKey(item)
    return resourceLocation.withPrefix("item/")
  }

  fun getItemTexture(item: Item, string: String): ResourceLocation {
    val resourceLocation = BuiltInRegistries.ITEM.getKey(item)
    return resourceLocation.withPath { string2: String -> "item/$string2$string" }
  }

  fun getBlockModId(block: Block): String {
    return BuiltInRegistries.BLOCK.getKey(block).namespace
  }

  fun getItemModId(item: Item): String {
    return BuiltInRegistries.ITEM.getKey(item).namespace
  }
}