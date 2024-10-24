package com.dannbrown.deltaboxlib.registry.transformers

import com.dannbrown.deltaboxlib.registry.utils.AssetLookup
import com.tterrag.registrate.providers.DataGenContext
import com.tterrag.registrate.providers.RegistrateBlockstateProvider
import com.tterrag.registrate.util.nullness.NonNullBiConsumer
import net.minecraft.core.Direction
import net.minecraft.world.level.block.AmethystClusterBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.ButtonBlock
import net.minecraft.world.level.block.DoorBlock
import net.minecraft.world.level.block.DoublePlantBlock
import net.minecraft.world.level.block.FenceBlock
import net.minecraft.world.level.block.FenceGateBlock
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.PressurePlateBlock
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.TrapDoorBlock
import net.minecraft.world.level.block.WallBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import net.minecraft.world.level.block.state.properties.Property
import net.minecraft.world.level.block.state.properties.WallSide
import net.minecraftforge.client.model.generators.BlockStateProvider
import net.minecraftforge.client.model.generators.ConfiguredModel

object BlockstatePresets {
  fun <B : Block> noBlockState(): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { _, _ -> }
  }

  fun <B : Block> simpleBlock(): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.simpleBlock(c.get())
    }
  }

  fun <B : Block> simpleTransparentBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.simpleBlock(
        c.get(), p.models()
          .cubeAll(c.name, p.modLoc("block/$name"))
          .renderType("cutout_mipped")
      )
    }
  }

  fun <B : Block> leavesBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.simpleBlock(
        c.get(), p.models()
          .withExistingParent(c.name, p.mcLoc("block/leaves"))
          .texture("all", p.modLoc("block/$name"))
          .renderType("cutout_mipped")
      )
    }
  }

  /**
   * Add a custom model to the block, the model file must exist in MOD_ID/models/block/BLOCK_ID.json
   */
  fun <B : Block> customStandardModel(): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.simpleBlock(c.entry, AssetLookup.standardModel(c, p))
    }
  }

  /**
   * Add a custom model to the block, the model file must exist in MOD_ID/models/block/BLOCK_ID/block.json
   */
  fun <B : Block> customPartialModel(): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.simpleBlock(c.entry, AssetLookup.partialBaseModel(c, p))
    }
  }

  fun <B : Block> bottomTopBlock(name: String, bottomName: String = name, topName: String = name
  ): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.simpleBlock(c.get(),
        p.models()
          .withExistingParent(c.name, p.mcLoc("block/cube_bottom_top"))
          .texture("side", p.modLoc("block/$name"))
          .texture("bottom", p.modLoc("block/$bottomName"))
          .texture("top", p.modLoc("block/$topName")))
    }
  }

  fun <B : Block> directionalBlock(name: String, bottomName: String = name, topName: String = name
  ): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      val angleOffset = 180
      val baseModel = p.models()
        .withExistingParent(c.name, p.mcLoc("block/cube_bottom_top"))
        .texture("side", p.modLoc("block/$name"))
        .texture("bottom", p.modLoc("block/$bottomName"))
        .texture("top", p.modLoc("block/$topName"))

      p.getVariantBuilder(c.get())
        .forAllStatesExcept({ state ->
          val dir = state.getValue(BlockStateProperties.FACING)
          ConfiguredModel.builder()
            .modelFile(baseModel)
            .rotationX(if (dir == Direction.DOWN) 180
            else if (dir.axis
                .isHorizontal) 90
            else 0)
            .rotationY(if (dir.axis
                .isVertical) 0
            else ((dir.toYRot()
              .toInt()) + angleOffset) % 360)
            .build()
        }, BlockStateProperties.WATERLOGGED)
    }
  }

  fun <B : Block> cubeBottomTopBlock(name: String, bottomName: String = name, topName: String = name
  ): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.simpleBlock(c.get(),
        p.models()
          .withExistingParent(c.name, p.mcLoc("block/cube_bottom_top"))
          .texture("side", p.modLoc("block/$name"))
          .texture("bottom", p.modLoc("block/$bottomName"))
          .texture("top", p.modLoc("block/$topName")))
    }
  }

  fun <B : Block> orientableBlock(name: String, bottomName: String = name, topName: String = name, frontName: String = name): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.simpleBlock(c.get(),
        p.models()
          .withExistingParent(c.name, p.mcLoc("block/orientable"))
          .texture("side", p.modLoc("block/$name"))
          .texture("front", p.modLoc("block/$frontName"))
          .texture("bottom", p.modLoc("block/$bottomName"))
          .texture("top", p.modLoc("block/$topName")))
    }
  }

  fun <B : Block> horizontalAxisBlock(name: String, bottomName: String = name, topName: String = name, frontName: String = name): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.getVariantBuilder(c.entry)
        .forAllStates { state: BlockState ->
          val direction = state.getValue(HorizontalDirectionalBlock.FACING)
          val isZed = ((direction == Direction.NORTH || direction == Direction.SOUTH))
          val xRot = 0
          val yRot = if (direction.axis.isVertical) 90
          else direction.toYRot()
            .toInt()
          val model = p.models()
            .withExistingParent(c.name, p.mcLoc("block/orientable"))
            .texture("side", p.modLoc("block/$name"))
            .texture("front", p.modLoc("block/$frontName"))
            .texture("bottom", p.modLoc("block/$bottomName"))
            .texture("top", p.modLoc("block/$topName"))


          ConfiguredModel.builder()
            .modelFile(model.apply { state })
            .rotationX(xRot)
            .rotationY(yRot)
            .build()
        }
    }
  }

  fun <B : Block> axisBlock(name: String, bottomName: String = name, topName: String = name, frontName: String = name): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.getVariantBuilder(c.entry)
        .forAllStates { state: BlockState ->
          val direction = state.getValue(BlockStateProperties.FACING)
          val isZed = ((direction == Direction.NORTH || direction == Direction.SOUTH))
          val xRot = if (direction == Direction.DOWN) 270 else if (direction == Direction.UP) 90 else 0
          val yRot = if (direction.axis.isVertical) 90
          else direction.toYRot()
            .toInt()
          val model = p.models()
            .withExistingParent(c.name, p.mcLoc("block/orientable"))
            .texture("side", p.modLoc("block/$name"))
            .texture("front", p.modLoc("block/$frontName"))
            .texture("bottom", p.modLoc("block/$bottomName"))
            .texture("top", p.modLoc("block/$topName"))


          ConfiguredModel.builder()
            .modelFile(model.apply { state })
            .rotationX(xRot)
            .rotationY(yRot)
            .build()
        }
    }
  }

  fun <B : Block> carpetBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.simpleBlock(c.get(),
        p.models()
          .withExistingParent(c.name, p.mcLoc("block/carpet"))
          .texture("wool", p.modLoc("block/$name"))
          .renderType("cutout_mipped"))
    }
  }

  fun <B : Block> simpleCrossBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.getVariantBuilder(c.get())
        .partialState()
        .setModels(*ConfiguredModel.builder()
          .modelFile(p.models()
            .withExistingParent(c.name, p.mcLoc("block/cross"))
            .texture("cross", p.modLoc("block/$name"))
            .texture("particle", p.modLoc("block/$name"))
            .renderType("cutout_mipped"))
          .build())
    }
  }

  fun <B : Block> simpleCarpetBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.getVariantBuilder(c.get())
        .partialState()
        .setModels(*ConfiguredModel.builder()
          .modelFile(p.models()
            .withExistingParent(c.name, p.mcLoc("block/carpet"))
            .texture("wool", p.modLoc("block/$name"))
            .texture("particle", p.modLoc("block/$name")))
          .build())
    }
  }

  fun <B : Block> simpleRootBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.getVariantBuilder(c.get())
        .partialState()
        .setModels(*ConfiguredModel.builder()
          .modelFile(p.models()
            .withExistingParent(c.name, p.mcLoc("block/mangrove_roots"))
            .texture("side", p.modLoc("block/${name}_side"))
            .texture("top", p.modLoc("block/${name}_top"))
            .renderType("cutout_mipped"))
          .build())
    }
  }

  fun <B : Block> simpleBushBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.getVariantBuilder(c.get())
        .partialState()
        .setModels(*ConfiguredModel.builder()
          .modelFile(p.models()
            .withExistingParent(c.name, p.mcLoc("block/template_azalea"))
            .texture("side", p.modLoc("block/${name}_side"))
            .texture("top", p.modLoc("block/${name}_top"))
            .texture("plant", p.modLoc("block/${name}_plant"))
            .renderType("cutout_mipped"))
          .build())
    }
  }

  fun <B : Block> pottedPlantBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.getVariantBuilder(c.get())
        .partialState()
        .setModels(*ConfiguredModel.builder()
          .modelFile(p.models()
            .withExistingParent(c.name, p.mcLoc("block/flower_pot_cross"))
            .texture("plant", p.modLoc("block/$name"))
            .texture("particle", p.modLoc("block/$name"))
            .renderType("cutout_mipped"))
          .build())
    }
  }

  fun <B : Block> pottedBushBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.getVariantBuilder(c.get())
        .partialState()
        .setModels(*ConfiguredModel.builder()
          .modelFile(p.models()
            .withExistingParent(c.name, p.mcLoc("block/template_potted_azalea_bush"))
            .texture("plant", p.modLoc("block/${name}_plant"))
            .texture("side", p.modLoc("block/${name}_side"))
            .texture("top", p.modLoc("block/${name}_top"))
            .renderType("cutout_mipped"))
          .build())
    }
  }

  fun <B : Block> tallPlantTopBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.getVariantBuilder(c.get())
        .partialState()
        .with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)
        .setModels(*ConfiguredModel.builder()
          .modelFile(p.models()
            .withExistingParent(c.name + "_top", p.mcLoc("block/cross"))
            .texture("cross", p.modLoc("block/" + name + "_top"))
            .texture("particle", p.modLoc("block/" + name + "_top"))
            .renderType("cutout_mipped"))
          .build())
        .partialState()
        .with(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)
        .setModels(*ConfiguredModel.builder()
          .modelFile(p.models()
            .withExistingParent(c.name + "_bottom", p.mcLoc("block/cross"))
            .texture("cross", p.modLoc("block/" + name + "_bottom"))
            .texture("particle", p.modLoc("block/" + name + "_bottom"))
            .renderType("cutout_mipped"))
          .build())
    }
  }

  fun <B : Block> tallPlantBottomBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.getVariantBuilder(c.get())
        .partialState()
        .setModels(*ConfiguredModel.builder()
          .modelFile(p.models()
            .withExistingParent(c.name, p.mcLoc("block/cross"))
            .texture("cross", p.modLoc("block/$name"))
            .texture("particle", p.modLoc("block/$name"))
            .renderType("cutout_mipped"))
          .build())
    }
  }

  fun <B : Block> stairsBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.stairsBlock(c.entry as StairBlock, p.modLoc("block/$name"))
    }
  }

  fun <B : Block> bottomTopStairsBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.stairsBlock(c.entry as StairBlock, p.modLoc("block/$name"), p.modLoc("block/$name" + "_bottom"), p.modLoc("block/$name" + "_top"))
    }
  }

  fun <B : Block> slabBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      val mainTexture = p.modLoc("block/$name")
      val sideTexture = p.modLoc("block/$name")
      val bottom = p.models()
        .slab(c.name, sideTexture, mainTexture, mainTexture)
      val top = p.models()
        .slabTop(c.name + "_top", sideTexture, mainTexture, mainTexture)
      val doubleSlab = p.models()
        .cubeColumn(c.name + "_double", sideTexture, mainTexture)

      p.slabBlock(c.get() as SlabBlock, bottom, top, doubleSlab)
    }
  }

  fun <B : Block> bottomTopSlabBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      val topTexture = p.modLoc("block/$name" + "_top")
      val bottomTexture = p.modLoc("block/$name" + "_bottom")
      val sideTexture = p.modLoc("block/$name")
      val bottom = p.models()
        .slab(c.name, sideTexture, bottomTexture, topTexture)
      val top = p.models()
        .slabTop(c.name + "_top", sideTexture, bottomTexture, topTexture)
      val doubleSlab = p.models()
        .cubeBottomTop(c.name + "_double", sideTexture, bottomTexture, topTexture)

      p.slabBlock(c.get() as SlabBlock, bottom, top, doubleSlab)
    }
  }

  fun <B : Block> halfBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      val topTexture = p.modLoc("block/$name")
      val bottomTexture = p.modLoc("block/$name")
      val sideTexture = p.modLoc("block/$name")
      val bottom = p.models()
        .slab(c.name, sideTexture, bottomTexture, topTexture)
        .renderType("cutout_mipped")
      p.simpleBlock(c.get(), bottom)
    }
  }

  fun <B : Block> wallBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.wallBlock(c.get() as WallBlock, p.modLoc("block/$name"))
    }
  }

  fun <B : Block> bottomTopWallBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      val postModel = p.models()
        .withExistingParent(c.name, p.modLoc("block/wall_special_post"))
        .texture("wall", p.modLoc("block/$name"))
        .texture("bottom", p.modLoc("block/$name" + "_top"))
        .texture("top", p.modLoc("block/$name" + "_top"))
      val sideModel = p.models()
        .withExistingParent(c.name + "_side", p.mcLoc("block/template_wall_side"))
        .texture("wall", p.modLoc("block/$name"))
      val tallSideModel = p.models()
        .withExistingParent(c.name + "_tall_side", p.mcLoc("block/template_wall_side_tall"))
        .texture("wall", p.modLoc("block/$name"))
      val builder = p.getMultipartBuilder(c.get())
        .part()
        .modelFile(postModel)
        .addModel()
        .condition(WallBlock.UP, true)
        .end()

      BlockStateProvider.WALL_PROPS.entries.stream()
        .filter { (key): Map.Entry<Direction, Property<WallSide?>?> ->
          key.axis.isHorizontal
        }
        .forEach { e: Map.Entry<Direction, Property<WallSide>> ->
          builder.part()
            .modelFile(sideModel)
            .rotationY((e.key.toYRot()
              .toInt() + 180) % 360)
            .uvLock(true)
            .addModel()
            .condition(e.value, WallSide.LOW)
          builder.part()
            .modelFile(tallSideModel)
            .rotationY((e.key.toYRot()
              .toInt() + 180) % 360)
            .uvLock(true)
            .addModel()
            .condition(e.value, WallSide.TALL)
        }
    }
  }


  fun <B : Block> ladderBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.horizontalBlock(c.get(),
        p.models()
          .withExistingParent(c.name, p.modLoc("block/ladder"))
          .texture("texture", p.modLoc("block/ladder_$name"))
          .texture("particle", p.modLoc("block/ladder_$name"))
          .renderType("cutout_mipped"))
    }
  }

  fun <B : Block> trapdoorBlock(name: String, orientable: Boolean = true): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.trapdoorBlockWithRenderType(c.entry as TrapDoorBlock, name, p.modLoc("block/$name" + "_trapdoor"), orientable, "cutout_mipped")
    }
  }


  fun <B : Block> fenceBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.fenceBlock(c.get() as FenceBlock, p.modLoc("block/${name}")
      )
    }
  }

  fun <B : Block> fenceGateBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.fenceGateBlock(c.get() as FenceGateBlock, p.modLoc("block/${name}"))
    }
  }

  fun <B : Block> pressurePlateBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.pressurePlateBlock(c.get() as PressurePlateBlock, p.modLoc("block/${name}"))
    }
  }

  fun <B : Block> buttonBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.buttonBlock(c.get() as ButtonBlock, p.modLoc("block/${name}"))
    }
  }

  fun <B : Block> doorTransparentBlock(): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.doorBlockWithRenderType(c.get() as DoorBlock, p.modLoc("block/${c.name}" + "_bottom"), p.modLoc("block/${c.name}" + "_top"), "cutout_mipped")
    }
  }

  fun <B : Block> clusterCrossBlock(): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      val baseModel = p.models()
        .withExistingParent(c.name, p.mcLoc("block/cross"))
        .texture("cross", p.modLoc("block/${c.name}"))
        .texture("particle", p.modLoc("block/${c.name}"))
        .renderType("cutout_mipped")
      p.getVariantBuilder(c.get())
        .forAllStatesExcept({ state ->
          ConfiguredModel.builder()
            .modelFile(baseModel)
            .rotationX(when (state.getValue(AmethystClusterBlock.FACING)) {
              Direction.DOWN -> 180
              Direction.EAST -> 90
              Direction.NORTH -> 90
              Direction.SOUTH -> 90
              Direction.UP -> 0
              Direction.WEST -> 90
              else -> 0
            })
            .rotationY(when (state.getValue(AmethystClusterBlock.FACING)) {
              Direction.DOWN -> 0
              Direction.EAST -> 90
              Direction.NORTH -> 0
              Direction.SOUTH -> 180
              Direction.UP -> 0
              Direction.WEST -> 270
              else -> 0
            })
            .build()
        }, AmethystClusterBlock.WATERLOGGED)
    }
  }

  fun <B: Block> simpleMultifaceBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      val model = p.models()
        .getBuilder(name)
        .ao(true)
        // texture
        .texture("particle", p.modLoc("block/${name}"))
        .texture("face", p.modLoc("block/${name}"))
        // texture elements
        .element()
        .from(0f, 0f, 0.1f)
        .to(16f, 16f, 0.1f)
        .face(Direction.NORTH).uvs(16f, 0f, 0f, 16f).texture("#face").end()
        .face(Direction.SOUTH).uvs(0f, 0f, 16f, 16f).texture("#face").end()
        .end()
        // transparent elements
        .renderType("translucent")


      p.getMultipartBuilder(c.get())
        // north
        .part().modelFile(model)
        .addModel()
        .condition(BlockStateProperties.NORTH, true)
        .end()
        // east
        .part().modelFile(model)
        .addModel()
        .condition(BlockStateProperties.DOWN, false)
        .condition(BlockStateProperties.EAST, false)
        .condition(BlockStateProperties.NORTH, false)
        .condition(BlockStateProperties.SOUTH, false)
        .condition(BlockStateProperties.UP, false)
        .condition(BlockStateProperties.WEST, false)
        .end()
        // east
        .part().modelFile(model)
        .rotationY(90)
        .uvLock(true)
        .addModel()
        .condition(BlockStateProperties.EAST, true)
        .end()
        // east
        .part().modelFile(model)
        .rotationY(90)
        .uvLock(true)
        .addModel()
        .condition(BlockStateProperties.DOWN, false)
        .condition(BlockStateProperties.EAST, false)
        .condition(BlockStateProperties.NORTH, false)
        .condition(BlockStateProperties.SOUTH, false)
        .condition(BlockStateProperties.UP, false)
        .condition(BlockStateProperties.WEST, false)
        .end()
        // south
        .part().modelFile(model)
        .rotationY(180)
        .uvLock(true)
        .addModel()
        .condition(BlockStateProperties.SOUTH, true)
        .end()
        // south
        .part().modelFile(model)
        .rotationY(180)
        .uvLock(true)
        .addModel()
        .condition(BlockStateProperties.DOWN, false)
        .condition(BlockStateProperties.EAST, false)
        .condition(BlockStateProperties.NORTH, false)
        .condition(BlockStateProperties.SOUTH, false)
        .condition(BlockStateProperties.UP, false)
        .condition(BlockStateProperties.WEST, false)
        .end()
        // west
        .part().modelFile(model)
        .rotationY(270)
        .uvLock(true)
        .addModel()
        .condition(BlockStateProperties.WEST, true)
        .end()
        // west
        .part().modelFile(model)
        .rotationY(270)
        .uvLock(true)
        .addModel()
        .condition(BlockStateProperties.DOWN, false)
        .condition(BlockStateProperties.EAST, false)
        .condition(BlockStateProperties.NORTH, false)
        .condition(BlockStateProperties.SOUTH, false)
        .condition(BlockStateProperties.UP, false)
        .condition(BlockStateProperties.WEST, false)
        .end()
        // up
        .part().modelFile(model)
        .rotationX(270)
        .uvLock(true)
        .addModel()
        .condition(BlockStateProperties.UP, true)
        .end()
        // up
        .part().modelFile(model)
        .rotationX(270)
        .uvLock(true)
        .addModel()
        .condition(BlockStateProperties.DOWN, false)
        .condition(BlockStateProperties.EAST, false)
        .condition(BlockStateProperties.NORTH, false)
        .condition(BlockStateProperties.SOUTH, false)
        .condition(BlockStateProperties.UP, false)
        .condition(BlockStateProperties.WEST, false)
        .end()
        // down
        .part().modelFile(model)
        .rotationX(90)
        .uvLock(true)
        .addModel()
        .condition(BlockStateProperties.DOWN, true)
        .end()
        // down
        .part().modelFile(model)
        .rotationX(90)
        .uvLock(true)
        .addModel()
        .condition(BlockStateProperties.DOWN, false)
        .condition(BlockStateProperties.EAST, false)
        .condition(BlockStateProperties.NORTH, false)
        .condition(BlockStateProperties.SOUTH, false)
        .condition(BlockStateProperties.UP, false)
        .condition(BlockStateProperties.WEST, false)
        .end()
    }
  }


  fun <B : Block> poweredBlock(): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.getVariantBuilder(c.get())
        .forAllStatesExcept({ state ->
          val powered = state.getValue(BlockStateProperties.POWERED)
          val poweredSuffix = if (powered) "_on" else ""
          ConfiguredModel.builder()
            .modelFile(p.models()
              .cubeAll(c.name + poweredSuffix, p.modLoc("block/${c.name}" + poweredSuffix)))
            .build()
        }, BlockStateProperties.WATERLOGGED)
    }
  }


  // Angular sensor
  fun <B : Block> angularSensor(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.getVariantBuilder(c.entry)
        .forAllStates { state: BlockState ->
          val direction = state.getValue(BlockStateProperties.FACING)
          val powered = state.getValue(BlockStateProperties.POWERED)
          val axis = direction.axis.toString()
          val isZed = ((direction == Direction.NORTH || direction == Direction.SOUTH))
          val xRot = if (direction == Direction.DOWN) 270 else if (direction == Direction.UP) 90 else 0
          val yRot = if (direction.axis.isVertical) 90
          else direction.toYRot()
            .toInt()
          val model = p.models()
            .withExistingParent(c.name + if (isZed) "" else "_$axis" + if (powered) "_on" else "", p.mcLoc("block/orientable"))
            .texture("side", p.modLoc("block/${name}_side"))
            .texture("front", p.modLoc("block/${name}_front_${axis}"))
            .texture("south", p.modLoc("block/${name}_back${if (powered) "_on" else ""}"))
            .texture("bottom", p.modLoc("block/${name}_side"))
            .texture("top", p.modLoc("block/${name}_side"))


          ConfiguredModel.builder()
            .modelFile(model.apply { state })
            .rotationX(xRot)
            .rotationY(yRot)
            .build()
        }
    }
  }
//
}