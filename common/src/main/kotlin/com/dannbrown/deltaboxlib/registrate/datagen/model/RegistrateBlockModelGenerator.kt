package com.dannbrown.deltaboxlib.registrate.datagen.model

import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import com.google.gson.JsonElement
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.blockstates.BlockStateGenerator
import net.minecraft.data.models.blockstates.MultiVariantGenerator
import net.minecraft.data.models.blockstates.Variant
import net.minecraft.data.models.blockstates.VariantProperties
import net.minecraft.data.models.model.ModelLocationUtils
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.Supplier

class RegistrateBlockModelGenerator(consumer: Consumer<BlockStateGenerator>, biConsumer: BiConsumer<ResourceLocation, Supplier<JsonElement>>, consumer2: Consumer<Item>) : BlockModelGenerators(consumer, biConsumer, consumer2) {
  fun noBlockState() {
    // do nothing
  }

  fun cubeAll(block: Block, texture: String = "") {
    val location = RegistrateModelTemplates.CUBE_ALL.create(ModelLocationUtils.getModelLocation(block), TextureMapping().put(RegistrateTextureSlots.ALL_SLOT, optionalTexture(block, texture)), this.modelOutput)
    this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block, Variant.variant().with(VariantProperties.MODEL, location)))
  }

  fun bottomTopBlock(block: Block, bottomTexture: String = "", topTexture: String = "", sideTexture: String = "") {
    val location = RegistrateModelTemplates.BOTTOM_TOP.create(
      ModelLocationUtils.getModelLocation(block),
      TextureMapping()
        .put(TextureSlot.BOTTOM, optionalTexture(block, bottomTexture, "_bottom", "block/"))
        .put(TextureSlot.TOP, optionalTexture(block, topTexture, "_top", "block/"))
        .put(TextureSlot.SIDE, optionalTexture(block, sideTexture, "_side", "block/")),
      this.modelOutput)
    this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block, Variant.variant().with(VariantProperties.MODEL, location)))
  }

  fun crossBlock(block: Block, crossTexture: String = "") {
    val location = RegistrateModelTemplates.CROSS.create(
      ModelLocationUtils.getModelLocation(block),
      TextureMapping().put(TextureSlot.CROSS, optionalTexture(block, crossTexture, "", "item/")),
      this.modelOutput)
    this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block, Variant.variant().with(VariantProperties.MODEL, location)))
  }

  fun flowerPotPlant(plant: Block, pottedPlant: Block) {
    val location = RegistrateModelTemplates.POTTED_FLOWER.create(pottedPlant, TextureMapping.plant(plant), this.modelOutput)
    this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(pottedPlant, Variant.variant().with(VariantProperties.MODEL, location)))
  }

  // utils

  // returns the path of a texture rather it is given or it uses the block id with an optional suffix
  fun optionalTexture(block: Block, texture: String, suffix: String = "", path: String = "block/"): ResourceLocation {
    return if (texture.isEmpty()) TextureMapping.getBlockTexture(block, suffix) else DeltaboxUtil.resourceLocation(DeltaboxUtil.getBlockModId(block), path, texture)
  }
}