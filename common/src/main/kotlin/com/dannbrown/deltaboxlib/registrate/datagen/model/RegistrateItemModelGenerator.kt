package com.dannbrown.deltaboxlib.registrate.datagen.model

import com.google.gson.JsonElement
import net.minecraft.data.models.ItemModelGenerators
import net.minecraft.data.models.model.ModelLocationUtils
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import java.util.function.BiConsumer
import java.util.function.Supplier

class RegistrateItemModelGenerator(val output: BiConsumer<ResourceLocation, Supplier<JsonElement>>) : ItemModelGenerators(output) {
  fun flatItem(item: Item) {
    RegistrateModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(item), this.output)
  }

  fun flatHandheldItem(item: Item) {
    RegistrateModelTemplates.FLAT_HANDHELD_ITEM.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(item), this.output)
  }

  fun blockItem(block: Block) {
    RegistrateModelTemplates.create(TextureMapping.getBlockTexture(block))
  }
}