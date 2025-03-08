package com.dannbrown.deltaboxlib.registrate.datagen.model

import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import com.google.gson.JsonElement
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.models.ItemModelGenerators
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import java.util.function.BiConsumer
import java.util.function.Supplier

class RegistrateItemModelGenerator(val output: BiConsumer<ResourceLocation, Supplier<JsonElement>>) :
  ItemModelGenerators(output) {
  fun flatItem(item: Item, texture: String = "") {
    RegistrateModelTemplates.FLAT_ITEM.create(
      BuiltInRegistries.ITEM.getKey(item).withPrefix("item/"),
      TextureMapping().put(TextureSlot.LAYER0, optionalTexture(item, texture, "", "item/")),
      this.output
    )
  }

  fun flatItemBlock(item: Item, texture: String = "") {
    RegistrateModelTemplates.FLAT_ITEM.create(
      BuiltInRegistries.ITEM.getKey(item).withPrefix("item/"),
      TextureMapping().put(TextureSlot.LAYER0, optionalTexture(item, texture, "", "block/")),
      this.output
    )
  }

  fun flatHandheldItem(item: Item, texture: String = "") {
    RegistrateModelTemplates.FLAT_HANDHELD_ITEM.create(
      BuiltInRegistries.ITEM.getKey(item).withPrefix("item/"),
      TextureMapping().put(TextureSlot.LAYER0, optionalTexture(item, texture, "", "item/")),
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

  fun fenceInventory(item: Item, texture: String) {
    ModelTemplates.FENCE_INVENTORY.create(
      BuiltInRegistries.ITEM.getKey(item).withPrefix("item/"),
      TextureMapping().put(TextureSlot.TEXTURE, optionalTexture(item, texture, "", "block/")),
      this.output
    )
  }

  fun buttonInventory(item: Item, texture: String) {
    ModelTemplates.BUTTON_INVENTORY.create(
      BuiltInRegistries.ITEM.getKey(item).withPrefix("item/"),
      TextureMapping().put(TextureSlot.TEXTURE, optionalTexture(item, texture, "", "block/")),
      this.output
    )
  }

  fun blockItem(block: Block) {
    RegistrateModelTemplates.create(TextureMapping.getBlockTexture(block))
  }


  // Util

  fun optionalTexture(item: Item, texture: String, suffix: String = "", path: String = "block/"): ResourceLocation {
    return if (texture.isEmpty())
      BuiltInRegistries.ITEM.getKey(item).withPath { str -> path + str + suffix }
    else DeltaboxUtil.resourceLocation(
      DeltaboxUtil.getItemModId(item),
      path,
      texture
    )
  }
}