package com.dannbrown.deltaboxlib.registrate.datagen

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
  fun noBlockState() { }

  fun cubeAll(block: Block) {
    val location = RegistrateModelTemplates.CUBE_ALL.create(ModelLocationUtils.getModelLocation(block), TextureMapping.defaultTexture(block).put(TextureSlot.ALL, TextureMapping.getBlockTexture(block)), this.modelOutput)
    this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block, Variant.variant().with(VariantProperties.MODEL, location)))
  }

  fun flowerPotPlant(plant: Block, pottedPlant: Block) {
    val location = RegistrateModelTemplates.POTTED_FLOWER.create(pottedPlant, TextureMapping.plant(plant), this.modelOutput)
    this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(pottedPlant, Variant.variant().with(VariantProperties.MODEL, location)))
  }
}