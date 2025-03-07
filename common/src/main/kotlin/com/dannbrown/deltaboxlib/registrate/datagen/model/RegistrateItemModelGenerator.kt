package com.dannbrown.deltaboxlib.registrate.datagen.model

import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import com.google.gson.JsonElement
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.models.ItemModelGenerators
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import java.util.function.BiConsumer
import java.util.function.Supplier

class RegistrateItemModelGenerator(val output: BiConsumer<ResourceLocation, Supplier<JsonElement>>) :
  ItemModelGenerators(output) {
  fun flatItem(item: Item) {
    RegistrateModelTemplates.FLAT_ITEM.create(
      BuiltInRegistries.ITEM.getKey(item).withPrefix("item/"),
      TextureMapping().put(TextureSlot.LAYER0, BuiltInRegistries.ITEM.getKey(item).withPrefix("item/")),
      this.output
    )
  }

  fun flatItemBlock(item: Item) {
    RegistrateModelTemplates.FLAT_ITEM.create(
      BuiltInRegistries.ITEM.getKey(item).withPrefix("item/"),
      TextureMapping().put(TextureSlot.LAYER0, BuiltInRegistries.ITEM.getKey(item).withPrefix("block/")),
      this.output
    )
  }

  fun flatHandheldItem(item: Item) {
    RegistrateModelTemplates.FLAT_HANDHELD_ITEM.create(
      BuiltInRegistries.ITEM.getKey(item).withPrefix("item/"),
      TextureMapping().put(TextureSlot.LAYER0, BuiltInRegistries.ITEM.getKey(item).withPrefix("item/")),
      this.output
    )
  }

  fun wallInventory(item: Item, texture: String) {
    bottomTopWallInventory(item, texture, texture)
  }

  fun bottomTopWallInventory(item: Item, wallTexture: String, topTexture: String) {
    RegistrateModelTemplates.BOTTOM_TOP_WALL_INVENTORY.create(
      BuiltInRegistries.ITEM.getKey(item).withPrefix("item/"),
      TextureMapping()
        .put(TextureSlot.WALL, optionalTexture(item, wallTexture, "", "block/"))
        .put(TextureSlot.TOP, optionalTexture(item, topTexture, "_top", "block/")),
      this.output
    )
  }

  fun blockItem(block: Block) {
    RegistrateModelTemplates.create(TextureMapping.getBlockTexture(block))
  }


  // Util

  fun optionalTexture(item: Item, texture: String, suffix: String = "", path: String = "block/"): ResourceLocation {
    return if (texture.isEmpty()) TextureMapping.getItemTexture(item, suffix) else DeltaboxUtil.resourceLocation(
      DeltaboxUtil.getItemModId(item),
      path,
      texture
    )
  }
}