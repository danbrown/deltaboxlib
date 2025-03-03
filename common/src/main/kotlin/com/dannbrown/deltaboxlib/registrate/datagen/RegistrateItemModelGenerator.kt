package com.dannbrown.deltaboxlib.registrate.datagen

import com.google.gson.JsonElement
import net.minecraft.data.models.ItemModelGenerators
import net.minecraft.data.models.model.ModelLocationUtils
import net.minecraft.data.models.model.ModelTemplate
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import java.util.function.BiConsumer
import java.util.function.Supplier

class RegistrateItemModelGenerator(val output: BiConsumer<ResourceLocation, Supplier<JsonElement>>) : ItemModelGenerators(output) {
  private fun build(item: Item, modelTemplate: ModelTemplate) {
    modelTemplate.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(item), this.output)
  }

  fun flatItem(item: Item) {
    this.build(item, RegistrateModelTemplates.createItem("generated", TextureSlot.LAYER0))
  }

  fun flatHandheldItem(item: Item) {
    this.build(item, ModelTemplates.FLAT_HANDHELD_ITEM)
  }
}