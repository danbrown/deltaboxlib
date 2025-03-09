package com.dannbrown.deltaboxlib.registrate.datagen.model

import com.dannbrown.deltaboxlib.content.block.CropLeavesBlock
import com.dannbrown.deltaboxlib.content.block.GenericCropBlock
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import com.google.gson.JsonElement
import net.minecraft.core.Direction
import net.minecraft.core.Direction.Axis
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.blockstates.*
import net.minecraft.data.models.blockstates.VariantProperties.Rotation
import net.minecraft.data.models.model.ModelLocationUtils
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.block.DoublePlantBlock
import net.minecraft.world.level.block.state.properties.AttachFace
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DoorHingeSide
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import net.minecraft.world.level.block.state.properties.Half
import net.minecraft.world.level.block.state.properties.SlabType
import net.minecraft.world.level.block.state.properties.StairsShape
import net.minecraft.world.level.block.state.properties.WallSide
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

  fun rotatedPillarBlock(block: Block, topTexture: String = "", sideTexture: String = "") {
    val location = RegistrateModelTemplates.ROTATED_PILLAR.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/"),
      TextureMapping()
        .put(TextureSlot.END, optionalTexture(block, topTexture, "_top", "block/"))
        .put(TextureSlot.SIDE, optionalTexture(block, sideTexture, "", "block/")),
      this.modelOutput
    )

    val rotatedPillarState = PropertyDispatch.property(BlockStateProperties.AXIS)
      .select(Axis.Y, Variant.variant())
      .select(Axis.Z, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
      .select(
        Axis.X,
        Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
          .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
      )

    this.blockStateOutput.accept(
      MultiVariantGenerator.multiVariant(block, Variant.variant().with(VariantProperties.MODEL, location)).with(
        rotatedPillarState
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

  fun cropLeavesBlock(block: Block, texture: String) {
    val maxStages = CropLeavesBlock.MAX_AGE
    val stages = (0..maxStages).map { stage ->
      stage to if (stage == 0) "" else "_stage$stage"
    }

    val models = stages.associate { (stage, suffix) ->
      stage to RegistrateModelTemplates.LEAVES.create(
        BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix(suffix),
        TextureMapping.singleSlot(
          RegistrateTextureSlots.ALL_SLOT,
          optionalTexture(block, "${texture}${suffix}", suffix, "block/"),
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

  fun cropBlock(block: Block, texture: String) {
    val maxStages = CropBlock.MAX_AGE
    val stages = (0..maxStages).map { stage ->
      stage to "_stage$stage"
    }

    val models = stages.associate { (stage, suffix) ->
      stage to RegistrateModelTemplates.CROSS.create(
        BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix(suffix),
        TextureMapping.singleSlot(
          TextureSlot.CROSS,
          optionalTexture(block, "${texture}${suffix}", suffix, "block/${texture}/"),
        ),
        this.modelOutput
      )
    }

    this.blockStateOutput.accept(
      MultiVariantGenerator.multiVariant(block).with(
        PropertyDispatch.property(CropBlock.AGE).apply {
          stages.forEach { (stage, _) ->
            select(stage, Variant.variant().with(VariantProperties.MODEL, models[stage]))
          }
        }
      )
    )
  }

  fun buddingCropBlock(block: Block, texture: String) {
    val maxStages = CropBlock.MAX_AGE
    val stages = (0..maxStages).map { stage ->
      stage to "_budding_stage$stage"
    }

    val models = stages.associate { (stage, suffix) ->
      stage to RegistrateModelTemplates.CROSS.create(
        BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix(suffix),
        TextureMapping.singleSlot(
          TextureSlot.CROSS,
          optionalTexture(block, "${texture}${suffix}", suffix, "block/${texture}/"),
        ),
        this.modelOutput
      )
    }

    this.blockStateOutput.accept(
      MultiVariantGenerator.multiVariant(block).with(
        PropertyDispatch.property(CropBlock.AGE).apply {
          stages.forEach { (stage, _) ->
            select(stage, Variant.variant().with(VariantProperties.MODEL, models[stage]))
          }
        }
      )
    )
  }

  fun doubleCropBlock(block: Block, texture: String) {
    val maxStages = CropBlock.MAX_AGE
    fun stages(prefix: String) = (0..maxStages).map { stage ->
      stage to "${prefix}_stage${stage}"
    }

    val lowerModels = stages("_bottom").associate { (stage, suffix) ->
      stage to RegistrateModelTemplates.CROSS.create(
        BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix(suffix),
        TextureMapping.singleSlot(
          TextureSlot.CROSS,
          optionalTexture(block, "${texture}${suffix}", suffix, "block/${texture}/"),
        ),
        this.modelOutput
      )
    }

    val upperModels = stages("_top").associate { (stage, suffix) ->
      stage to RegistrateModelTemplates.CROSS.create(
        BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix(suffix),
        TextureMapping.singleSlot(
          TextureSlot.CROSS,
          optionalTexture(block, "${texture}${suffix}", suffix, "block/${texture}/"),
        ),
        this.modelOutput
      )
    }

    this.blockStateOutput.accept(
      MultiVariantGenerator.multiVariant(block).with(
        PropertyDispatch.properties(GenericCropBlock.HALF, CropBlock.AGE).apply {
          stages("").forEach { (stage, _) ->
            select(DoubleBlockHalf.LOWER, stage, Variant.variant().with(VariantProperties.MODEL, lowerModels[stage]))
            select(DoubleBlockHalf.UPPER, stage, Variant.variant().with(VariantProperties.MODEL, upperModels[stage]))
          }
        }
      )
    )
  }

  fun stairs(block: Block, texture: String = "") {
    bottomTopStairs(block, texture, texture, texture)
  }

  fun bottomTopStairs(block: Block, bottomTexture: String = "", topTexture: String = "", sideTexture: String = "") {
    val resourceLocation = ModelTemplates.STAIRS_INNER.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix("_inner"),
      TextureMapping()
        .put(TextureSlot.BOTTOM, optionalTexture(block, bottomTexture, "_bottom", "block/"))
        .put(TextureSlot.TOP, optionalTexture(block, topTexture, "_top", "block/"))
        .put(TextureSlot.SIDE, optionalTexture(block, sideTexture, "_side", "block/")),
      this.modelOutput
    )
    val resourceLocation2 = ModelTemplates.STAIRS_STRAIGHT.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix(""),
      TextureMapping()
        .put(TextureSlot.BOTTOM, optionalTexture(block, bottomTexture, "_bottom", "block/"))
        .put(TextureSlot.TOP, optionalTexture(block, topTexture, "_top", "block/"))
        .put(TextureSlot.SIDE, optionalTexture(block, sideTexture, "_side", "block/")),
      this.modelOutput
    )
    val resourceLocation3 = ModelTemplates.STAIRS_OUTER.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix("_outer"),
      TextureMapping()
        .put(TextureSlot.BOTTOM, optionalTexture(block, bottomTexture, "_bottom", "block/"))
        .put(TextureSlot.TOP, optionalTexture(block, topTexture, "_top", "block/"))
        .put(TextureSlot.SIDE, optionalTexture(block, sideTexture, "_side", "block/")),
      this.modelOutput
    )

    val stairsBlockstate = MultiVariantGenerator.multiVariant(block).with(
      PropertyDispatch.properties(
        BlockStateProperties.HORIZONTAL_FACING,
        BlockStateProperties.HALF,
        BlockStateProperties.STAIRS_SHAPE
      ).select(
        Direction.EAST,
        Half.BOTTOM,
        StairsShape.STRAIGHT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation2)
      ).select(
        Direction.WEST,
        Half.BOTTOM,
        StairsShape.STRAIGHT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.Y_ROT, Rotation.R180)
          .with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.SOUTH,
        Half.BOTTOM,
        StairsShape.STRAIGHT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.Y_ROT, Rotation.R90)
          .with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.NORTH,
        Half.BOTTOM,
        StairsShape.STRAIGHT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.Y_ROT, Rotation.R270)
          .with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.EAST,
        Half.BOTTOM,
        StairsShape.OUTER_RIGHT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation3)
      ).select(
        Direction.WEST,
        Half.BOTTOM,
        StairsShape.OUTER_RIGHT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.Y_ROT, Rotation.R180)
          .with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.SOUTH,
        Half.BOTTOM,
        StairsShape.OUTER_RIGHT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.Y_ROT, Rotation.R90)
          .with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.NORTH,
        Half.BOTTOM,
        StairsShape.OUTER_RIGHT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.Y_ROT, Rotation.R270)
          .with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.EAST,
        Half.BOTTOM,
        StairsShape.OUTER_LEFT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.Y_ROT, Rotation.R270)
          .with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.WEST,
        Half.BOTTOM,
        StairsShape.OUTER_LEFT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.Y_ROT, Rotation.R90)
          .with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.SOUTH,
        Half.BOTTOM,
        StairsShape.OUTER_LEFT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation3)
      ).select(
        Direction.NORTH,
        Half.BOTTOM,
        StairsShape.OUTER_LEFT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.Y_ROT, Rotation.R180)
          .with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.EAST,
        Half.BOTTOM,
        StairsShape.INNER_RIGHT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation)
      ).select(
        Direction.WEST,
        Half.BOTTOM,
        StairsShape.INNER_RIGHT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.Y_ROT, Rotation.R180)
          .with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.SOUTH,
        Half.BOTTOM,
        StairsShape.INNER_RIGHT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.Y_ROT, Rotation.R90)
          .with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.NORTH,
        Half.BOTTOM,
        StairsShape.INNER_RIGHT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.Y_ROT, Rotation.R270)
          .with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.EAST,
        Half.BOTTOM,
        StairsShape.INNER_LEFT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.Y_ROT, Rotation.R270)
          .with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.WEST,
        Half.BOTTOM,
        StairsShape.INNER_LEFT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.Y_ROT, Rotation.R90)
          .with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.SOUTH,
        Half.BOTTOM,
        StairsShape.INNER_LEFT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation)
      ).select(
        Direction.NORTH,
        Half.BOTTOM,
        StairsShape.INNER_LEFT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.Y_ROT, Rotation.R180)
          .with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.EAST,
        Half.TOP,
        StairsShape.STRAIGHT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.X_ROT, Rotation.R180)
          .with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.WEST,
        Half.TOP,
        StairsShape.STRAIGHT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.X_ROT, Rotation.R180)
          .with(VariantProperties.Y_ROT, Rotation.R180).with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.SOUTH,
        Half.TOP,
        StairsShape.STRAIGHT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.X_ROT, Rotation.R180)
          .with(VariantProperties.Y_ROT, Rotation.R90).with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.NORTH,
        Half.TOP,
        StairsShape.STRAIGHT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.X_ROT, Rotation.R180)
          .with(VariantProperties.Y_ROT, Rotation.R270).with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.EAST,
        Half.TOP,
        StairsShape.OUTER_RIGHT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.X_ROT, Rotation.R180)
          .with(VariantProperties.Y_ROT, Rotation.R90).with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.WEST,
        Half.TOP,
        StairsShape.OUTER_RIGHT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.X_ROT, Rotation.R180)
          .with(VariantProperties.Y_ROT, Rotation.R270).with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.SOUTH,
        Half.TOP,
        StairsShape.OUTER_RIGHT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.X_ROT, Rotation.R180)
          .with(VariantProperties.Y_ROT, Rotation.R180).with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.NORTH,
        Half.TOP,
        StairsShape.OUTER_RIGHT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.X_ROT, Rotation.R180)
          .with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.EAST,
        Half.TOP,
        StairsShape.OUTER_LEFT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.X_ROT, Rotation.R180)
          .with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.WEST,
        Half.TOP,
        StairsShape.OUTER_LEFT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.X_ROT, Rotation.R180)
          .with(VariantProperties.Y_ROT, Rotation.R180).with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.SOUTH,
        Half.TOP,
        StairsShape.OUTER_LEFT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.X_ROT, Rotation.R180)
          .with(VariantProperties.Y_ROT, Rotation.R90).with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.NORTH,
        Half.TOP,
        StairsShape.OUTER_LEFT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.X_ROT, Rotation.R180)
          .with(VariantProperties.Y_ROT, Rotation.R270).with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.EAST,
        Half.TOP,
        StairsShape.INNER_RIGHT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.X_ROT, Rotation.R180)
          .with(VariantProperties.Y_ROT, Rotation.R90).with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.WEST,
        Half.TOP,
        StairsShape.INNER_RIGHT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.X_ROT, Rotation.R180)
          .with(VariantProperties.Y_ROT, Rotation.R270).with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.SOUTH,
        Half.TOP,
        StairsShape.INNER_RIGHT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.X_ROT, Rotation.R180)
          .with(VariantProperties.Y_ROT, Rotation.R180).with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.NORTH,
        Half.TOP,
        StairsShape.INNER_RIGHT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.X_ROT, Rotation.R180)
          .with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.EAST,
        Half.TOP,
        StairsShape.INNER_LEFT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.X_ROT, Rotation.R180)
          .with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.WEST,
        Half.TOP,
        StairsShape.INNER_LEFT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.X_ROT, Rotation.R180)
          .with(VariantProperties.Y_ROT, Rotation.R180).with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.SOUTH,
        Half.TOP,
        StairsShape.INNER_LEFT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.X_ROT, Rotation.R180)
          .with(VariantProperties.Y_ROT, Rotation.R90).with(VariantProperties.UV_LOCK, true)
      ).select(
        Direction.NORTH,
        Half.TOP,
        StairsShape.INNER_LEFT,
        Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.X_ROT, Rotation.R180)
          .with(VariantProperties.Y_ROT, Rotation.R270).with(VariantProperties.UV_LOCK, true)
      )
    )

    this.blockStateOutput.accept(stairsBlockstate)
  }

  fun slab(block: Block, texture: String) {
    bottomTopSlab(block, texture, texture, texture)
  }

  fun bottomTopSlab(block: Block, bottomTexture: String = "", topTexture: String = "", sideTexture: String = "") {
    val resourceLocation = ModelTemplates.SLAB_BOTTOM.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix(""),
      TextureMapping()
        .put(TextureSlot.BOTTOM, optionalTexture(block, bottomTexture, "_bottom", "block/"))
        .put(TextureSlot.TOP, optionalTexture(block, topTexture, "_top", "block/"))
        .put(TextureSlot.SIDE, optionalTexture(block, sideTexture, "_side", "block/")),
      this.modelOutput
    )
    val resourceLocation2 = ModelTemplates.SLAB_TOP.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix("_top"),
      TextureMapping()
        .put(TextureSlot.BOTTOM, optionalTexture(block, bottomTexture, "_bottom", "block/"))
        .put(TextureSlot.TOP, optionalTexture(block, topTexture, "_top", "block/"))
        .put(TextureSlot.SIDE, optionalTexture(block, sideTexture, "_side", "block/")),
      this.modelOutput
    )
    val resourceLocation3 = RegistrateModelTemplates.BOTTOM_TOP.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix("_double"),
      TextureMapping()
        .put(TextureSlot.BOTTOM, optionalTexture(block, bottomTexture, "_bottom", "block/"))
        .put(TextureSlot.TOP, optionalTexture(block, topTexture, "_top", "block/"))
        .put(TextureSlot.SIDE, optionalTexture(block, sideTexture, "_side", "block/")),
      this.modelOutput
    )

    val slabBlockstate = MultiVariantGenerator.multiVariant(block).with(
      PropertyDispatch.property(BlockStateProperties.SLAB_TYPE)
        .select(SlabType.BOTTOM, Variant.variant().with(VariantProperties.MODEL, resourceLocation))
        .select(SlabType.TOP, Variant.variant().with(VariantProperties.MODEL, resourceLocation2))
        .select(SlabType.DOUBLE, Variant.variant().with(VariantProperties.MODEL, resourceLocation3))
    )

    this.blockStateOutput.accept(slabBlockstate)
  }

  fun wall(block: Block, texture: String) {
    return bottomTopWall(block, texture, texture, texture)
  }

  fun bottomTopWall(block: Block, bottomTexture: String, topTexture: String, sideTexture: String) {
    val resourceLocation = RegistrateModelTemplates.BOTTOM_TOP_WALL_POST.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix("_post"),
      TextureMapping()
        .put(TextureSlot.BOTTOM, optionalTexture(block, bottomTexture, "_bottom", "block/"))
        .put(TextureSlot.TOP, optionalTexture(block, topTexture, "_top", "block/"))
        .put(TextureSlot.WALL, optionalTexture(block, sideTexture, "", "block/")),
      this.modelOutput
    )
    val resourceLocation2 = ModelTemplates.WALL_LOW_SIDE.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix("_side"),
      TextureMapping()
        .put(TextureSlot.WALL, optionalTexture(block, sideTexture, "", "block/")),
      this.modelOutput
    )
    val resourceLocation3 = ModelTemplates.WALL_TALL_SIDE.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix("_side_tall"),
      TextureMapping()
        .put(TextureSlot.WALL, optionalTexture(block, sideTexture, "", "block/")),
      this.modelOutput
    )

    val wallBlockstate = MultiPartGenerator.multiPart(block).with(
      Condition.condition().term(BlockStateProperties.UP, true),
      Variant.variant().with(VariantProperties.MODEL, resourceLocation)
    ).with(
      Condition.condition().term(BlockStateProperties.NORTH_WALL, WallSide.LOW),
      Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.UV_LOCK, true)
    ).with(
      Condition.condition().term(BlockStateProperties.EAST_WALL, WallSide.LOW),
      Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.Y_ROT, Rotation.R90)
        .with(VariantProperties.UV_LOCK, true)
    ).with(
      Condition.condition().term(BlockStateProperties.SOUTH_WALL, WallSide.LOW),
      Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.Y_ROT, Rotation.R180)
        .with(VariantProperties.UV_LOCK, true)
    ).with(
      Condition.condition().term(BlockStateProperties.WEST_WALL, WallSide.LOW),
      Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.Y_ROT, Rotation.R270)
        .with(VariantProperties.UV_LOCK, true)
    ).with(
      Condition.condition().term(BlockStateProperties.NORTH_WALL, WallSide.TALL),
      Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.UV_LOCK, true)
    ).with(
      Condition.condition().term(BlockStateProperties.EAST_WALL, WallSide.TALL),
      Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.Y_ROT, Rotation.R90)
        .with(VariantProperties.UV_LOCK, true)
    ).with(
      Condition.condition().term(BlockStateProperties.SOUTH_WALL, WallSide.TALL),
      Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.Y_ROT, Rotation.R180)
        .with(VariantProperties.UV_LOCK, true)
    ).with(
      Condition.condition().term(BlockStateProperties.WEST_WALL, WallSide.TALL),
      Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.Y_ROT, Rotation.R270)
        .with(VariantProperties.UV_LOCK, true)
    )

    this.blockStateOutput.accept(wallBlockstate)
  }

  fun fence(block: Block, texture: String) {
    val resourceLocation = ModelTemplates.FENCE_POST.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix("_post"),
      TextureMapping().put(TextureSlot.TEXTURE, optionalTexture(block, texture, "", "block/")),
      this.modelOutput
    )
    val resourceLocation2 = ModelTemplates.FENCE_SIDE.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix("_side"),
      TextureMapping().put(TextureSlot.TEXTURE, optionalTexture(block, texture, "", "block/")),
      this.modelOutput
    )

    val fenceBlockstate =
      MultiPartGenerator.multiPart(block).with(Variant.variant().with(VariantProperties.MODEL, resourceLocation)).with(
        Condition.condition().term(BlockStateProperties.NORTH, true),
        Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.UV_LOCK, true)
      ).with(
        Condition.condition().term(BlockStateProperties.EAST, true),
        Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.Y_ROT, Rotation.R90)
          .with(VariantProperties.UV_LOCK, true)
      ).with(
        Condition.condition().term(BlockStateProperties.SOUTH, true),
        Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.Y_ROT, Rotation.R180)
          .with(VariantProperties.UV_LOCK, true)
      ).with(
        Condition.condition().term(BlockStateProperties.WEST, true),
        Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.Y_ROT, Rotation.R270)
          .with(VariantProperties.UV_LOCK, true)
      )

    this.blockStateOutput.accept(fenceBlockstate)
  }

  fun fenceGate(block: Block, texture: String) {
    val resourceLocation = ModelTemplates.FENCE_GATE_OPEN.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix("_open"),
      TextureMapping().put(TextureSlot.TEXTURE, optionalTexture(block, texture, "", "block/")),
      this.modelOutput
    )
    val resourceLocation2 = ModelTemplates.FENCE_GATE_CLOSED.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix(""),
      TextureMapping().put(TextureSlot.TEXTURE, optionalTexture(block, texture, "", "block/")),
      this.modelOutput
    )
    val resourceLocation3 = ModelTemplates.FENCE_GATE_WALL_OPEN.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix("_wall_open"),
      TextureMapping().put(TextureSlot.TEXTURE, optionalTexture(block, texture, "", "block/")),
      this.modelOutput
    )
    val resourceLocation4 = ModelTemplates.FENCE_GATE_WALL_CLOSED.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix("_wall"),
      TextureMapping().put(TextureSlot.TEXTURE, optionalTexture(block, texture, "", "block/")),
      this.modelOutput
    )

    val fencegateBlockstate =
      MultiVariantGenerator.multiVariant(block, Variant.variant().with(VariantProperties.UV_LOCK, true))
        .with(
          PropertyDispatch.property(BlockStateProperties.HORIZONTAL_FACING).select(Direction.SOUTH, Variant.variant())
            .select(Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, Rotation.R90))
            .select(Direction.NORTH, Variant.variant().with(VariantProperties.Y_ROT, Rotation.R180))
            .select(Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, Rotation.R270))
        ).with(
          PropertyDispatch.properties(BlockStateProperties.IN_WALL, BlockStateProperties.OPEN)
            .select(false, false, Variant.variant().with(VariantProperties.MODEL, resourceLocation2))
            .select(true, false, Variant.variant().with(VariantProperties.MODEL, resourceLocation4))
            .select(false, true, Variant.variant().with(VariantProperties.MODEL, resourceLocation))
            .select(true, true, Variant.variant().with(VariantProperties.MODEL, resourceLocation3))
        )
    this.blockStateOutput.accept(fencegateBlockstate)
  }

  fun pressurePlate(block: Block, texture: String) {
    val resourceLocation = ModelTemplates.PRESSURE_PLATE_UP.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix(""),
      TextureMapping().put(TextureSlot.TEXTURE, optionalTexture(block, texture, "", "block/")),
      this.modelOutput
    )
    val resourceLocation2 = ModelTemplates.PRESSURE_PLATE_DOWN.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix("_down"),
      TextureMapping().put(TextureSlot.TEXTURE, optionalTexture(block, texture, "", "block/")),
      this.modelOutput
    )

    val pressurePlateBlockstate = MultiVariantGenerator.multiVariant(block)
      .with(
        PropertyDispatch.property(BlockStateProperties.POWERED)
          .select(false, Variant.variant().with(VariantProperties.MODEL, resourceLocation))
          .select(true, Variant.variant().with(VariantProperties.MODEL, resourceLocation2))
      )

    this.blockStateOutput.accept(pressurePlateBlockstate)
  }

  fun button(block: Block, texture: String) {
    val resourceLocation = ModelTemplates.BUTTON.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix(""),
      TextureMapping().put(TextureSlot.TEXTURE, optionalTexture(block, texture, "", "block/")),
      this.modelOutput
    )
    val resourceLocation2 = ModelTemplates.BUTTON_PRESSED.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix("_down"),
      TextureMapping().put(TextureSlot.TEXTURE, optionalTexture(block, texture, "", "block/")),
      this.modelOutput
    )
    val buttonBlockstate = MultiVariantGenerator.multiVariant(block).with(
      PropertyDispatch.property(BlockStateProperties.POWERED)
        .select(false, Variant.variant().with(VariantProperties.MODEL, resourceLocation))
        .select(true, Variant.variant().with(VariantProperties.MODEL, resourceLocation2))
    ).with(
      PropertyDispatch.properties(BlockStateProperties.ATTACH_FACE, BlockStateProperties.HORIZONTAL_FACING)
        .select(AttachFace.FLOOR, Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, Rotation.R90))
        .select(AttachFace.FLOOR, Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, Rotation.R270))
        .select(AttachFace.FLOOR, Direction.SOUTH, Variant.variant().with(VariantProperties.Y_ROT, Rotation.R180))
        .select(AttachFace.FLOOR, Direction.NORTH, Variant.variant()).select(
          AttachFace.WALL,
          Direction.EAST,
          Variant.variant().with(VariantProperties.Y_ROT, Rotation.R90).with(VariantProperties.X_ROT, Rotation.R90)
            .with(VariantProperties.UV_LOCK, true)
        ).select(
          AttachFace.WALL,
          Direction.WEST,
          Variant.variant().with(VariantProperties.Y_ROT, Rotation.R270).with(VariantProperties.X_ROT, Rotation.R90)
            .with(VariantProperties.UV_LOCK, true)
        ).select(
          AttachFace.WALL,
          Direction.SOUTH,
          Variant.variant().with(VariantProperties.Y_ROT, Rotation.R180).with(VariantProperties.X_ROT, Rotation.R90)
            .with(VariantProperties.UV_LOCK, true)
        ).select(
          AttachFace.WALL,
          Direction.NORTH,
          Variant.variant().with(VariantProperties.X_ROT, Rotation.R90).with(VariantProperties.UV_LOCK, true)
        ).select(
          AttachFace.CEILING,
          Direction.EAST,
          Variant.variant().with(VariantProperties.Y_ROT, Rotation.R270).with(VariantProperties.X_ROT, Rotation.R180)
        ).select(
          AttachFace.CEILING,
          Direction.WEST,
          Variant.variant().with(VariantProperties.Y_ROT, Rotation.R90).with(VariantProperties.X_ROT, Rotation.R180)
        ).select(AttachFace.CEILING, Direction.SOUTH, Variant.variant().with(VariantProperties.X_ROT, Rotation.R180))
        .select(
          AttachFace.CEILING,
          Direction.NORTH,
          Variant.variant().with(VariantProperties.Y_ROT, Rotation.R180).with(VariantProperties.X_ROT, Rotation.R180)
        )
    )
    this.blockStateOutput.accept(buttonBlockstate)
  }

  fun trapdoor(block: Block, texture: String) {
    val resourceLocation = RegistrateModelTemplates.TRAPDOOR_TOP.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix("_top"),
      TextureMapping().put(TextureSlot.TEXTURE, optionalTexture(block, texture, "", "block/")),
      this.modelOutput
    )
    val resourceLocation2 = RegistrateModelTemplates.TRAPDOOR_BOTTOM.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix(""),
      TextureMapping().put(TextureSlot.TEXTURE, optionalTexture(block, texture, "", "block/")),
      this.modelOutput
    )
    val resourceLocation3 = RegistrateModelTemplates.TRAPDOOR_OPEN.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix("_open"),
      TextureMapping().put(TextureSlot.TEXTURE, optionalTexture(block, texture, "", "block/")),
      this.modelOutput
    )

    val trapdoorBlockstate = MultiVariantGenerator.multiVariant(block).with(
      PropertyDispatch.properties(
        BlockStateProperties.HORIZONTAL_FACING,
        BlockStateProperties.HALF,
        BlockStateProperties.OPEN
      )
        .select(Direction.NORTH, Half.BOTTOM, false, Variant.variant().with(VariantProperties.MODEL, resourceLocation2))
        .select(
          Direction.SOUTH,
          Half.BOTTOM,
          false,
          Variant.variant().with(VariantProperties.MODEL, resourceLocation2)
            .with(VariantProperties.Y_ROT, Rotation.R180)
        )
        .select(
          Direction.EAST,
          Half.BOTTOM,
          false,
          Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.Y_ROT, Rotation.R90)
        )
        .select(
          Direction.WEST,
          Half.BOTTOM,
          false,
          Variant.variant().with(VariantProperties.MODEL, resourceLocation2)
            .with(VariantProperties.Y_ROT, Rotation.R270)
        ).select(Direction.NORTH, Half.TOP, false, Variant.variant().with(VariantProperties.MODEL, resourceLocation))
        .select(
          Direction.SOUTH,
          Half.TOP,
          false,
          Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.Y_ROT, Rotation.R180)
        ).select(
          Direction.EAST,
          Half.TOP,
          false,
          Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.Y_ROT, Rotation.R90)
        ).select(
          Direction.WEST,
          Half.TOP,
          false,
          Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.Y_ROT, Rotation.R270)
        ).select(Direction.NORTH, Half.BOTTOM, true, Variant.variant().with(VariantProperties.MODEL, resourceLocation3))
        .select(
          Direction.SOUTH,
          Half.BOTTOM,
          true,
          Variant.variant().with(VariantProperties.MODEL, resourceLocation3)
            .with(VariantProperties.Y_ROT, Rotation.R180)
        ).select(
          Direction.EAST,
          Half.BOTTOM,
          true,
          Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.Y_ROT, Rotation.R90)
        ).select(
          Direction.WEST,
          Half.BOTTOM,
          true,
          Variant.variant().with(VariantProperties.MODEL, resourceLocation3)
            .with(VariantProperties.Y_ROT, Rotation.R270)
        ).select(
          Direction.NORTH,
          Half.TOP,
          true,
          Variant.variant().with(VariantProperties.MODEL, resourceLocation3)
            .with(VariantProperties.X_ROT, Rotation.R180)
            .with(VariantProperties.Y_ROT, Rotation.R180)
        ).select(
          Direction.SOUTH,
          Half.TOP,
          true,
          Variant.variant().with(VariantProperties.MODEL, resourceLocation3)
            .with(VariantProperties.X_ROT, Rotation.R180)
        ).select(
          Direction.EAST,
          Half.TOP,
          true,
          Variant.variant().with(VariantProperties.MODEL, resourceLocation3)
            .with(VariantProperties.X_ROT, Rotation.R180)
            .with(VariantProperties.Y_ROT, Rotation.R270)
        ).select(
          Direction.WEST,
          Half.TOP,
          true,
          Variant.variant().with(VariantProperties.MODEL, resourceLocation3)
            .with(VariantProperties.X_ROT, Rotation.R180)
            .with(VariantProperties.Y_ROT, Rotation.R90)
        )
    )
    this.blockStateOutput.accept(trapdoorBlockstate)
  }

  fun door(block: Block, textureBottom: String, textureTop: String) {
    val textureMapping = TextureMapping()
      .put(TextureSlot.BOTTOM, optionalTexture(block, textureBottom, "_bottom", "block/"))
      .put(TextureSlot.TOP, optionalTexture(block, textureTop, "_top", "block/"))

    val resourceLocation = RegistrateModelTemplates.DOOR_BOTTOM_LEFT.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix("_bottom_left"),
      textureMapping,
      this.modelOutput
    )
    val resourceLocation2 = RegistrateModelTemplates.DOOR_BOTTOM_LEFT_OPEN.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix("_bottom_left_open"),
      textureMapping,
      this.modelOutput
    )
    val resourceLocation3 = RegistrateModelTemplates.DOOR_BOTTOM_RIGHT.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix("_bottom_right"),
      textureMapping,
      this.modelOutput
    )
    val resourceLocation4 = RegistrateModelTemplates.DOOR_BOTTOM_RIGHT_OPEN.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix("_bottom_right_open"),
      textureMapping,
      this.modelOutput
    )
    val resourceLocation5 = RegistrateModelTemplates.DOOR_TOP_LEFT.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix("_top_left"),
      textureMapping,
      this.modelOutput
    )
    val resourceLocation6 = RegistrateModelTemplates.DOOR_TOP_LEFT_OPEN.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix("_top_left_open"),
      textureMapping,
      this.modelOutput
    )
    val resourceLocation7 = RegistrateModelTemplates.DOOR_TOP_RIGHT.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix("_top_right"),
      textureMapping,
      this.modelOutput
    )
    val resourceLocation8 = RegistrateModelTemplates.DOOR_TOP_RIGHT_OPEN.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix("_top_right_open"),
      textureMapping,
      this.modelOutput
    )

    fun configureDoorHalf(
      c4: PropertyDispatch.C4<Direction, DoubleBlockHalf, DoorHingeSide, Boolean>,
      doubleBlockHalf: DoubleBlockHalf,
      resourceLocation: ResourceLocation,
      resourceLocation2: ResourceLocation,
      resourceLocation3: ResourceLocation,
      resourceLocation4: ResourceLocation
    ): PropertyDispatch.C4<Direction, DoubleBlockHalf, DoorHingeSide, Boolean> {
      return c4
        .select(
          Direction.EAST,
          doubleBlockHalf,
          DoorHingeSide.LEFT,
          false,
          Variant.variant().with(VariantProperties.MODEL, resourceLocation)
        )
        .select(
          Direction.SOUTH,
          doubleBlockHalf,
          DoorHingeSide.LEFT,
          false,
          Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.Y_ROT, Rotation.R90)
        )
        .select(
          Direction.WEST,
          doubleBlockHalf,
          DoorHingeSide.LEFT,
          false,
          Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.Y_ROT, Rotation.R180)
        )
        .select(
          Direction.NORTH,
          doubleBlockHalf,
          DoorHingeSide.LEFT,
          false,
          Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.Y_ROT, Rotation.R270)
        )
        .select(
          Direction.EAST,
          doubleBlockHalf,
          DoorHingeSide.RIGHT,
          false,
          Variant.variant().with(VariantProperties.MODEL, resourceLocation3)
        )
        .select(
          Direction.SOUTH,
          doubleBlockHalf,
          DoorHingeSide.RIGHT,
          false,
          Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.Y_ROT, Rotation.R90)
        )
        .select(
          Direction.WEST,
          doubleBlockHalf,
          DoorHingeSide.RIGHT,
          false,
          Variant.variant().with(VariantProperties.MODEL, resourceLocation3)
            .with(VariantProperties.Y_ROT, Rotation.R180)
        )
        .select(
          Direction.NORTH,
          doubleBlockHalf,
          DoorHingeSide.RIGHT,
          false,
          Variant.variant().with(VariantProperties.MODEL, resourceLocation3)
            .with(VariantProperties.Y_ROT, Rotation.R270)
        )
        .select(
          Direction.EAST,
          doubleBlockHalf,
          DoorHingeSide.LEFT,
          true,
          Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.Y_ROT, Rotation.R90)
        )
        .select(
          Direction.SOUTH,
          doubleBlockHalf,
          DoorHingeSide.LEFT,
          true,
          Variant.variant().with(VariantProperties.MODEL, resourceLocation2)
            .with(VariantProperties.Y_ROT, Rotation.R180)
        )
        .select(
          Direction.WEST,
          doubleBlockHalf,
          DoorHingeSide.LEFT,
          true,
          Variant.variant().with(VariantProperties.MODEL, resourceLocation2)
            .with(VariantProperties.Y_ROT, Rotation.R270)
        )
        .select(
          Direction.NORTH,
          doubleBlockHalf,
          DoorHingeSide.LEFT,
          true,
          Variant.variant().with(VariantProperties.MODEL, resourceLocation2)
        )
        .select(
          Direction.EAST,
          doubleBlockHalf,
          DoorHingeSide.RIGHT,
          true,
          Variant.variant().with(VariantProperties.MODEL, resourceLocation4)
            .with(VariantProperties.Y_ROT, Rotation.R270)
        )
        .select(
          Direction.SOUTH,
          doubleBlockHalf,
          DoorHingeSide.RIGHT,
          true,
          Variant.variant().with(VariantProperties.MODEL, resourceLocation4)
        )
        .select(
          Direction.WEST,
          doubleBlockHalf,
          DoorHingeSide.RIGHT,
          true,
          Variant.variant().with(VariantProperties.MODEL, resourceLocation4).with(VariantProperties.Y_ROT, Rotation.R90)
        )
        .select(
          Direction.NORTH,
          doubleBlockHalf,
          DoorHingeSide.RIGHT,
          true,
          Variant.variant().with(VariantProperties.MODEL, resourceLocation4)
            .with(VariantProperties.Y_ROT, Rotation.R180)
        )
    }

    val doorBlockstate = MultiVariantGenerator.multiVariant(block).with(
      configureDoorHalf(
        configureDoorHalf(
          PropertyDispatch.properties(
            BlockStateProperties.HORIZONTAL_FACING,
            BlockStateProperties.DOUBLE_BLOCK_HALF,
            BlockStateProperties.DOOR_HINGE,
            BlockStateProperties.OPEN
          ), DoubleBlockHalf.LOWER, resourceLocation, resourceLocation2, resourceLocation3, resourceLocation4
        ), DoubleBlockHalf.UPPER, resourceLocation5, resourceLocation6, resourceLocation7, resourceLocation8
      )
    )
    this.blockStateOutput.accept(doorBlockstate)
  }

  fun crossDoubleBlock(block: Block, bottomTexture: String, topTexture: String) {
    val resourceLocation = RegistrateModelTemplates.CROSS.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix("_bottom"),
      TextureMapping().put(TextureSlot.CROSS, optionalTexture(block, bottomTexture, "_bottom", "block/")),
      this.modelOutput
    )
    val resourceLocation2 = RegistrateModelTemplates.CROSS.create(
      BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix("_top"),
      TextureMapping().put(TextureSlot.CROSS, optionalTexture(block, topTexture, "_top", "block/")),
      this.modelOutput
    )

    val crossDoubleState = MultiVariantGenerator.multiVariant(block).with(
      PropertyDispatch.property(BlockStateProperties.DOUBLE_BLOCK_HALF)
        .select(DoubleBlockHalf.UPPER, Variant.variant().with(VariantProperties.MODEL, resourceLocation2))
        .select(DoubleBlockHalf.LOWER, Variant.variant().with(VariantProperties.MODEL, resourceLocation))
    )
    this.blockStateOutput.accept(crossDoubleState)
  }

  // utils

  // returns the path of a texture rather it is given or it uses the block id with an optional suffix
  fun optionalTexture(block: Block, texture: String, suffix: String = "", path: String = "block/"): ResourceLocation {
    return if (texture.isEmpty())
      BuiltInRegistries.BLOCK.getKey(block).withPath { str -> path + str + suffix }
    else DeltaboxUtil.resourceLocation(
      DeltaboxUtil.getBlockModId(block),
      path,
      texture
    )
  }
}