package com.dannbrown.deltaboxlib.registrate.datagen.model

import com.dannbrown.deltaboxlib.content.block.CropLeavesBlock
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import com.google.gson.JsonElement
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.blockstates.BlockStateGenerator
import net.minecraft.data.models.blockstates.MultiVariantGenerator
import net.minecraft.data.models.blockstates.PropertyDispatch
import net.minecraft.data.models.blockstates.Variant
import net.minecraft.data.models.blockstates.VariantProperties
import net.minecraft.data.models.model.ModelLocationUtils
import net.minecraft.data.models.model.ModelTemplate
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.Supplier

class RegistrateBlockModelGenerator(
  consumer: Consumer<BlockStateGenerator>,
  biConsumer: BiConsumer<ResourceLocation, Supplier<JsonElement>>,
  consumer2: Consumer<Item>
) : BlockModelGenerators(consumer, biConsumer, consumer2) {
  fun noBlockState() {
    // do nothing
  }

  fun cubeAll(block: Block, texture: String = "") {
    val location = RegistrateModelTemplates.CUBE_ALL.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/"),
      TextureMapping().put(RegistrateTextureSlots.ALL_SLOT, optionalTexture(block, texture)),
      this.modelOutput
    )
    this.blockStateOutput.accept(
      MultiVariantGenerator.multiVariant(
        block,
        Variant.variant().with(VariantProperties.MODEL, location)
      )
    )
  }

  fun bottomTopBlock(block: Block, bottomTexture: String = "", topTexture: String = "", sideTexture: String = "") {
    val location = RegistrateModelTemplates.BOTTOM_TOP.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/"),
      TextureMapping()
        .put(TextureSlot.BOTTOM, optionalTexture(block, bottomTexture, "_bottom", "block/"))
        .put(TextureSlot.TOP, optionalTexture(block, topTexture, "_top", "block/"))
        .put(TextureSlot.SIDE, optionalTexture(block, sideTexture, "_side", "block/")),
      this.modelOutput
    )
    this.blockStateOutput.accept(
      MultiVariantGenerator.multiVariant(
        block,
        Variant.variant().with(VariantProperties.MODEL, location)
      )
    )
  }

  fun crossBlock(block: Block, crossTexture: String = "") {
    val location = RegistrateModelTemplates.CROSS.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/"),
      TextureMapping().put(TextureSlot.CROSS, optionalTexture(block, crossTexture, "", "block/")),
      this.modelOutput
    )
    this.blockStateOutput.accept(
      MultiVariantGenerator.multiVariant(
        block,
        Variant.variant().with(VariantProperties.MODEL, location)
      )
    )
  }

  fun pottedPlantBlock(pottedPlant: Block, plant: Block) {
    val location =
      RegistrateModelTemplates.POTTED_FLOWER.create(pottedPlant, TextureMapping.plant(plant), this.modelOutput)
    this.blockStateOutput.accept(
      MultiVariantGenerator.multiVariant(
        pottedPlant,
        Variant.variant().with(VariantProperties.MODEL, location)
      )
    )
  }

  fun leavesBlock(block: Block, texture: String = "") {
    val location = RegistrateModelTemplates.LEAVES.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/"),
      TextureMapping().put(RegistrateTextureSlots.ALL_SLOT, optionalTexture(block, texture)),
      this.modelOutput
    )
    this.blockStateOutput.accept(
      MultiVariantGenerator.multiVariant(
        block,
        Variant.variant().with(VariantProperties.MODEL, location)
      )
    )
  }

  fun cropLeavesBlock(block: Block, texture: String = "") {
    val maxStages = CropLeavesBlock.MAX_AGE
    val stages = (0..maxStages).map { stage ->
      stage to "_stage$stage"
    }

    val models = stages.associate { (stage, suffix) ->
      stage to RegistrateModelTemplates.LEAVES.create(
        ModelLocationUtils.getModelLocation(block, "${texture}$suffix"),
        TextureMapping.singleSlot(
          RegistrateTextureSlots.ALL_SLOT,
          ModelLocationUtils.getModelLocation(block, "${texture}$suffix")
        ),
        this.modelOutput
      )
    }

    this.blockStateOutput.accept(
      MultiVariantGenerator.multiVariant(block).with(
        PropertyDispatch.property(CropLeavesBlock.AGE).apply {
          stages.forEach { (stage, _) ->
            select(stage, Variant.variant().with(VariantProperties.MODEL, models[stage]))
          }
        }
      )
    )
  }

//  fun createCropLeavesBlock(
//    block: Block
//  ) {
//    val property = CropLeavesBlock.AGE
//    val maxStages = CropLeavesBlock.MAX_AGE
//    require(property.possibleValues.size == maxStages) { "Property values size must match stages size." }
//
//    val int2ObjectMap = Int2ObjectOpenHashMap<ResourceLocation>()
//    val propertyDispatch = PropertyDispatch.property(property).generate { index ->
//      val resourceLocation = int2ObjectMap.get(index) ?: createSuffixedVariant(
//        block,
//        "_stage$index",
//        RegistrateModelTemplates.CROP
//      ) { TextureMapping.crop(it) }.also { int2ObjectMap.put(index, it) }
//
//      Variant.variant().with(VariantProperties.MODEL, resourceLocation)
//    }
//
//    RegistrateModelTemplates.FLAT_ITEM.create(
//      ModelLocationUtils.getModelLocation(block.asItem()),
//      TextureMapping.layer0(block.asItem()),
//      this.modelOutput
//    )
//    blockStateOutput.accept(MultiVariantGenerator.multiVariant(block).with(propertyDispatch))
//  }
//
//  private fun createSuffixedVariant(
//    block: Block,
//    suffix: String,
//    modelTemplate: ModelTemplate,
//    textureMapper: (ResourceLocation) -> TextureMapping
//  ): ResourceLocation {
//    return modelTemplate.createWithSuffix(
//      block,
//      suffix,
//      textureMapper(TextureMapping.getBlockTexture(block, suffix)),
//      this.modelOutput
//    )
//  }


//
//  fun <B : Block> cropLeavesBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
//    return NonNullBiConsumer { c, p ->
//      p.getVariantBuilder(c.get()).forAllStates { state ->
//        val age: Int = state.getValue(CropLeavesBlock.AGE)
//        val suffix = if (age > 0) "_stage$age" else ""
//        ConfiguredModel.builder()
//          .modelFile(
//            p.models()
//              .withExistingParent(c.name + suffix, p.mcLoc("block/leaves"))
//              .texture("all", p.modLoc("block/${name}${suffix}"))
//              .renderType("cutout_mipped")
//          )
//          .build()
//      }
//    }
//  }


  // utils

  // returns the path of a texture rather it is given or it uses the block id with an optional suffix
  fun optionalTexture(block: Block, texture: String, suffix: String = "", path: String = "block/"): ResourceLocation {
    return if (texture.isEmpty()) TextureMapping.getBlockTexture(block, suffix) else DeltaboxUtil.resourceLocation(
      DeltaboxUtil.getBlockModId(block),
      path,
      texture
    )
  }
}