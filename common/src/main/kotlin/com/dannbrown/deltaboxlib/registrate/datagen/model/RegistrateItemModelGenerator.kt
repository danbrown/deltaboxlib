package com.dannbrown.deltaboxlib.registrate.datagen.model

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

  fun blockItem(block: Block) {
    RegistrateModelTemplates.create(TextureMapping.getBlockTexture(block))
  }
}