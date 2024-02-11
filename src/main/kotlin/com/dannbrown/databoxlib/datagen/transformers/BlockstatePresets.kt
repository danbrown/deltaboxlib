package com.dannbrown.databoxlib.datagen.transformers

import com.dannbrown.databoxlib.content.block.StickerBlock
import com.dannbrown.databoxlib.content.block.pressureChamberValve.PressureChamberCapBlock
import com.dannbrown.databoxlib.content.misc.utils.RotationState
import com.dannbrown.databoxlib.content.ship.block.thruster.ThrusterBlock
import com.dannbrown.databoxlib.lib.LibUtils
import com.simibubi.create.content.decoration.MetalScaffoldingBlock
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock
import com.simibubi.create.foundation.data.AssetLookup
import com.simibubi.create.foundation.data.MetalBarsGen
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
import net.minecraft.world.level.block.IronBarsBlock
import net.minecraft.world.level.block.PressurePlateBlock
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.TrapDoorBlock
import net.minecraft.world.level.block.WallBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.AttachFace
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
          .texture("side", LibUtils.resourceLocation("block/$name"))
          .texture("bottom", LibUtils.resourceLocation("block/$bottomName"))
          .texture("top", LibUtils.resourceLocation("block/$topName")))
    }
  }

  fun <B : Block> directionalBlock(name: String, bottomName: String = name, topName: String = name
  ): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      val angleOffset = 180
      val baseModel = p.models()
        .withExistingParent(c.name, p.mcLoc("block/cube_bottom_top"))
        .texture("side", LibUtils.resourceLocation("block/$name"))
        .texture("bottom", LibUtils.resourceLocation("block/$bottomName"))
        .texture("top", LibUtils.resourceLocation("block/$topName"))

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
          .texture("side", LibUtils.resourceLocation("block/$name"))
          .texture("bottom", LibUtils.resourceLocation("block/$bottomName"))
          .texture("top", LibUtils.resourceLocation("block/$topName")))
    }
  }

  fun <B : Block> orientableBlock(name: String, bottomName: String = name, topName: String = name, frontName: String = name): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.simpleBlock(c.get(),
        p.models()
          .withExistingParent(c.name, p.mcLoc("block/orientable"))
          .texture("side", LibUtils.resourceLocation("block/$name"))
          .texture("front", LibUtils.resourceLocation("block/$frontName"))
          .texture("bottom", LibUtils.resourceLocation("block/$bottomName"))
          .texture("top", LibUtils.resourceLocation("block/$topName")))
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
            .texture("side", LibUtils.resourceLocation("block/$name"))
            .texture("front", LibUtils.resourceLocation("block/$frontName"))
            .texture("bottom", LibUtils.resourceLocation("block/$bottomName"))
            .texture("top", LibUtils.resourceLocation("block/$topName"))


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
            .texture("side", LibUtils.resourceLocation("block/$name"))
            .texture("front", LibUtils.resourceLocation("block/$frontName"))
            .texture("bottom", LibUtils.resourceLocation("block/$bottomName"))
            .texture("top", LibUtils.resourceLocation("block/$topName"))


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
          .texture("wool", LibUtils.resourceLocation("block/$name"))
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
      p.stairsBlock(c.entry as StairBlock, LibUtils.resourceLocation("block/$name"))
    }
  }

  fun <B : Block> bottomTopStairsBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.stairsBlock(c.entry as StairBlock, LibUtils.resourceLocation("block/$name"), LibUtils.resourceLocation("block/$name" + "_bottom"), LibUtils.resourceLocation("block/$name" + "_top"))
    }
  }

  fun <B : Block> slabBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      val mainTexture = LibUtils.resourceLocation("block/$name")
      val sideTexture = LibUtils.resourceLocation("block/$name")
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
      val topTexture = LibUtils.resourceLocation("block/$name" + "_top")
      val bottomTexture = LibUtils.resourceLocation("block/$name" + "_bottom")
      val sideTexture = LibUtils.resourceLocation("block/$name")
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
      val topTexture = LibUtils.resourceLocation("block/$name")
      val bottomTexture = LibUtils.resourceLocation("block/$name")
      val sideTexture = LibUtils.resourceLocation("block/$name")
      val bottom = p.models()
        .slab(c.name, sideTexture, bottomTexture, topTexture)
        .renderType("cutout_mipped")
      p.simpleBlock(c.get(), bottom)
    }
  }

  fun <B : Block> wallBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.wallBlock(c.get() as WallBlock, LibUtils.resourceLocation("block/$name"))
    }
  }

  fun <B : Block> bottomTopWallBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      val postModel = p.models()
        .withExistingParent(c.name, p.modLoc("block/wall_special_post"))
        .texture("wall", LibUtils.resourceLocation("block/$name"))
        .texture("bottom", LibUtils.resourceLocation("block/$name" + "_top"))
        .texture("top", LibUtils.resourceLocation("block/$name" + "_top"))
      val sideModel = p.models()
        .withExistingParent(c.name + "_side", p.mcLoc("block/template_wall_side"))
        .texture("wall", LibUtils.resourceLocation("block/$name"))
      val tallSideModel = p.models()
        .withExistingParent(c.name + "_tall_side", p.mcLoc("block/template_wall_side_tall"))
        .texture("wall", LibUtils.resourceLocation("block/$name"))
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

  fun <B : Block> scaffoldingBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.getVariantBuilder(c.get())
        .forAllStatesExcept({ s: BlockState ->
          val suffix = if (s.getValue(MetalScaffoldingBlock.BOTTOM)) "_horizontal" else ""
          ConfiguredModel.builder()
            .modelFile(p.models()
              .withExistingParent(c.name + suffix, p.modLoc("block/scaffold/block$suffix"))
              .texture("top", p.modLoc("block/scaffold/" + name + "_scaffold_frame"))
              .texture("inside", p.modLoc("block/scaffold/" + name + "_scaffold_inside"))
              .texture("side", p.modLoc("block/scaffold/" + name + "_scaffold"))
              .texture("casing", p.modLoc("block/" + name + "_casing"))
              .texture("particle", p.modLoc("block/scaffold/" + name + "_scaffold")))
            .build()
        }, MetalScaffoldingBlock.WATERLOGGED, MetalScaffoldingBlock.DISTANCE)
    }
  }

  fun <B : Block> barsBlock(name: String, specialEdge: Boolean = true): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return MetalBarsGen.barsBlockState<IronBarsBlock>(name, specialEdge) as NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider>
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
      p.trapdoorBlockWithRenderType(c.entry as TrapDoorBlock, name, LibUtils.resourceLocation("block/$name" + "_trapdoor"), orientable, "cutout_mipped")
    }
  }

  fun <B : Block> stickerBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      val baseModel = p.models()
        .withExistingParent(c.name + "_base", p.modLoc("block/sticker/base"))
        .texture("texture", p.modLoc("block/stickers/$name" + "_sticker"))
        .texture("particle", p.modLoc("block/stickers/$name" + "_sticker"))
      val wallModel90 = p.models()
        .withExistingParent(c.name + "_wall_r90", p.modLoc("block/sticker/wall_r90"))
        .texture("texture", p.modLoc("block/stickers/$name" + "_sticker"))
        .texture("particle", p.modLoc("block/stickers/$name" + "_sticker"))
      val wallModel180 = p.models()
        .withExistingParent(c.name + "_wall_r180", p.modLoc("block/sticker/wall_r180"))
        .texture("texture", p.modLoc("block/stickers/$name" + "_sticker"))
        .texture("particle", p.modLoc("block/stickers/$name" + "_sticker"))
      val wallModel270 = p.models()
        .withExistingParent(c.name + "_wall_r270", p.modLoc("block/sticker/wall_r270"))
        .texture("texture", p.modLoc("block/stickers/$name" + "_sticker"))
        .texture("particle", p.modLoc("block/stickers/$name" + "_sticker"))

      p.getVariantBuilder(c.get())
        .forAllStatesExcept({ state ->
          ConfiguredModel.builder()
            .modelFile(if (state.getValue(BlockStateProperties.ATTACH_FACE) == AttachFace.WALL) when (state.getValue(StickerBlock.ROTATION)) {
              RotationState.PI_0 -> baseModel
              RotationState.PI_90 -> wallModel90
              RotationState.PI_180 -> wallModel180
              RotationState.PI_270 -> wallModel270
            }
            else baseModel)
            .rotationX(state.getValue(BlockStateProperties.ATTACH_FACE).ordinal * 90)
            .rotationY(((state.getValue(BlockStateProperties.HORIZONTAL_FACING)
              .toYRot()
              .toInt() + 180) + if (state.getValue(BlockStateProperties.ATTACH_FACE) == AttachFace.CEILING) 180 else 0) % 360)
            .build()
        }, BlockStateProperties.WATERLOGGED, StickerBlock.GLOWING)
    }
  }

  fun <B : Block> fenceBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.fenceBlock(c.get() as FenceBlock, LibUtils.resourceLocation("block/${name}")
      )
    }
  }

  fun <B : Block> fenceGateBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.fenceGateBlock(c.get() as FenceGateBlock, LibUtils.resourceLocation("block/${name}"))
    }
  }

  fun <B : Block> pressurePlateBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.pressurePlateBlock(c.get() as PressurePlateBlock, LibUtils.resourceLocation("block/${name}"))
    }
  }

  fun <B : Block> buttonBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.buttonBlock(c.get() as ButtonBlock, LibUtils.resourceLocation("block/${name}"))
    }
  }

  fun <B : Block> doorTransparentBlock(): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.doorBlockWithRenderType(c.get() as DoorBlock, LibUtils.resourceLocation("block/${c.name}" + "_back"), LibUtils.resourceLocation("block/${c.name}" + "_top"), "cutout_mipped")
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

  //
  //  fun <B : Block> wallBlock(name: String): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> { //    return NonNullBiConsumer { c, p -> //      p.wallBlock(c.get() as WallBlock, LibUtils.resourceLocation("block/$name"))
  //    }
  //  }
//
  fun <B : Block> pressureChamberValveBlock(): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      val blockModel = p.models()
        .getExistingFile(p.modLoc("block/pressure_chamber/block"))
      val blockOnBasinModel = p.models()
        .getExistingFile(p.modLoc("block/pressure_chamber/block_on_a_basin"))
      val blockOpenModel = p.models()
        .getExistingFile(p.modLoc("block/pressure_chamber/block_open"))
      val blockOnBasinOpenModel = p.models()
        .getExistingFile(p.modLoc("block/pressure_chamber/block_on_a_basin_open"))

      p.getVariantBuilder(c.get()) //"on_a_basin=false,facing=north,open=false"
        .partialState()
        .with(PressureChamberCapBlock.ON_A_BASIN, false)
        .with(PressureChamberCapBlock.FACING, Direction.NORTH)
        .with(PressureChamberCapBlock.OPEN, false)
        .setModels(*ConfiguredModel.builder()
          .modelFile(blockModel)
          .rotationY(180)
          .build()) // "on_a_basin=false,facing=south,open=false"
        .partialState()
        .with(PressureChamberCapBlock.ON_A_BASIN, false)
        .with(PressureChamberCapBlock.FACING, Direction.SOUTH)
        .with(PressureChamberCapBlock.OPEN, false)
        .setModels(*ConfiguredModel.builder()
          .modelFile(blockModel)
          .build()) // "on_a_basin=false,facing=west,open=false"
        .partialState()
        .with(PressureChamberCapBlock.ON_A_BASIN, false)
        .with(PressureChamberCapBlock.FACING, Direction.WEST)
        .with(PressureChamberCapBlock.OPEN, false)
        .setModels(*ConfiguredModel.builder()
          .modelFile(blockModel)
          .rotationY(90)
          .build()) // "on_a_basin=false,facing=east,open=false"
        .partialState()
        .with(PressureChamberCapBlock.ON_A_BASIN, false)
        .with(PressureChamberCapBlock.FACING, Direction.EAST)
        .with(PressureChamberCapBlock.OPEN, false)
        .setModels(*ConfiguredModel.builder()
          .modelFile(blockModel)
          .rotationY(270)
          .build()) // "on_a_basin=true,facing=north,open=false"
        .partialState()
        .with(PressureChamberCapBlock.ON_A_BASIN, true)
        .with(PressureChamberCapBlock.FACING, Direction.NORTH)
        .with(PressureChamberCapBlock.OPEN, false)
        .setModels(*ConfiguredModel.builder()
          .modelFile(blockOnBasinModel)
          .rotationY(180)
          .build()) // "on_a_basin=true,facing=south,open=false"
        .partialState()
        .with(PressureChamberCapBlock.ON_A_BASIN, true)
        .with(PressureChamberCapBlock.FACING, Direction.SOUTH)
        .with(PressureChamberCapBlock.OPEN, false)
        .setModels(*ConfiguredModel.builder()
          .modelFile(blockOnBasinModel)
          .build()) // "on_a_basin=true,facing=west,open=false"
        .partialState()
        .with(PressureChamberCapBlock.ON_A_BASIN, true)
        .with(PressureChamberCapBlock.FACING, Direction.WEST)
        .with(PressureChamberCapBlock.OPEN, false)
        .setModels(*ConfiguredModel.builder()
          .modelFile(blockOnBasinModel)
          .rotationY(90)
          .build()) // "on_a_basin=true,facing=east,open=false"
        .partialState()
        .with(PressureChamberCapBlock.ON_A_BASIN, true)
        .with(PressureChamberCapBlock.FACING, Direction.EAST)
        .with(PressureChamberCapBlock.OPEN, false)
        .setModels(*ConfiguredModel.builder()
          .modelFile(blockOnBasinModel)
          .rotationY(270)
          .build()) // "on_a_basin=false,facing=north,open=true"
        .partialState()
        .with(PressureChamberCapBlock.ON_A_BASIN, false)
        .with(PressureChamberCapBlock.FACING, Direction.NORTH)
        .with(PressureChamberCapBlock.OPEN, true)
        .setModels(*ConfiguredModel.builder()
          .modelFile(blockOpenModel)
          .rotationY(180)
          .build()) // "on_a_basin=false,facing=south,open=true"
        .partialState()
        .with(PressureChamberCapBlock.ON_A_BASIN, false)
        .with(PressureChamberCapBlock.FACING, Direction.SOUTH)
        .with(PressureChamberCapBlock.OPEN, true)
        .setModels(*ConfiguredModel.builder()
          .modelFile(blockOpenModel)
          .build()) // "on_a_basin=false,facing=west,open=true"
        .partialState()
        .with(PressureChamberCapBlock.ON_A_BASIN, false)
        .with(PressureChamberCapBlock.FACING, Direction.WEST)
        .with(PressureChamberCapBlock.OPEN, true)
        .setModels(*ConfiguredModel.builder()
          .modelFile(blockOpenModel)
          .rotationY(90)
          .build()) // "on_a_basin=false,facing=east,open=true"
        .partialState()
        .with(PressureChamberCapBlock.ON_A_BASIN, false)
        .with(PressureChamberCapBlock.FACING, Direction.EAST)
        .with(PressureChamberCapBlock.OPEN, true)
        .setModels(*ConfiguredModel.builder()
          .modelFile(blockOpenModel)
          .rotationY(270)
          .build()) // "on_a_basin=true,facing=north,open=true"
        .partialState()
        .with(PressureChamberCapBlock.ON_A_BASIN, true)
        .with(PressureChamberCapBlock.FACING, Direction.NORTH)
        .with(PressureChamberCapBlock.OPEN, true)
        .setModels(*ConfiguredModel.builder()
          .modelFile(blockOnBasinOpenModel)
          .rotationY(180)
          .build()) // "on_a_basin=true,facing=south,open=true"
        .partialState()
        .with(PressureChamberCapBlock.ON_A_BASIN, true)
        .with(PressureChamberCapBlock.FACING, Direction.SOUTH)
        .with(PressureChamberCapBlock.OPEN, true)
        .setModels(*ConfiguredModel.builder()
          .modelFile(blockOnBasinOpenModel)
          .build()) // "on_a_basin=true,facing=west,open=true"
        .partialState()
        .with(PressureChamberCapBlock.ON_A_BASIN, true)
        .with(PressureChamberCapBlock.FACING, Direction.WEST)
        .with(PressureChamberCapBlock.OPEN, true)
        .setModels(*ConfiguredModel.builder()
          .modelFile(blockOnBasinOpenModel)
          .rotationY(90)
          .build()) // "on_a_basin=true,facing=east,open=true"
        .partialState()
        .with(PressureChamberCapBlock.ON_A_BASIN, true)
        .with(PressureChamberCapBlock.FACING, Direction.EAST)
        .with(PressureChamberCapBlock.OPEN, true)
        .setModels(*ConfiguredModel.builder()
          .modelFile(blockOnBasinOpenModel)
          .rotationY(270)
          .build())
    }
  }

  fun <B : Block> thrusterBlock(uvLock: Boolean = false): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.getVariantBuilder(c.entry)
        .forAllStates { state: BlockState ->
          val isBurning = state.getValue(ThrusterBlock.BURNING)
          val burningSuffix = if (isBurning) "_burning" else ""
          val alongFirst = state.getValue(DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE)
          val direction = state.getValue(DirectionalAxisKineticBlock.FACING)
          val isVertical = direction.axis.isHorizontal && direction.axis === Direction.Axis.X == alongFirst
          val isZed = (!alongFirst && (direction == Direction.NORTH || direction == Direction.SOUTH)) || (alongFirst && (direction == Direction.EAST || direction == Direction.WEST))
          val xRot = if (direction == Direction.DOWN) 270 else if (direction == Direction.UP) 90 else if (isZed) 90 else 0
          val yRot = if (direction.axis
              .isVertical) if (alongFirst) 0 else 90
          else direction.toYRot()
            .toInt()
          val axisSuffix = if (isZed) "_vertical_z" else if (isVertical) "_vertical" else "_horizontal"
          val model = p.models()
            .withExistingParent(c.name + axisSuffix + burningSuffix, p.modLoc("block/thrusters/thruster$axisSuffix"))
            .texture("0", LibUtils.resourceLocation("block/thrusters/${c.name + burningSuffix}"))
            .texture("particle", LibUtils.resourceLocation("block/thrusters/${c.name + burningSuffix}"))


          ConfiguredModel.builder()
            .modelFile(model.apply { state })
            .rotationX(xRot)
            .rotationY(yRot)
            .build()
        }
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

  fun <B : Block> resistorBlock(): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
    return NonNullBiConsumer { c, p ->
      p.getVariantBuilder(c.entry)
        .forAllStatesExcept({ state: BlockState ->
          val axis = state.getValue(BlockStateProperties.AXIS)
          val powerLevel = state.getValue(BlockStateProperties.POWER)
          val powerLevelSuffix = if (powerLevel == 0) "" else "_${powerLevel}"
//          val model = p.models()
//            .cubeAll(c.name + powerLevelSuffix, p.modLoc("block/${c.name}" + powerLevelSuffix))
          val model = p.models()
            .withExistingParent(c.name + powerLevelSuffix, p.modLoc("block/resistor/block"))
            .texture("0", p.modLoc("block/resistor/${c.name}" + powerLevelSuffix))
            .texture("particle", p.modLoc("block/resistor/${c.name}" + powerLevelSuffix))

          ConfiguredModel.builder()
            .modelFile(model.apply({ state }))
            .uvLock(false)
            .rotationX(if (axis === Direction.Axis.Y) 0 else 90)
            .rotationY(if (axis === Direction.Axis.X) 90 else if (axis === Direction.Axis.Z) 180 else 0)
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
            .texture("side", LibUtils.resourceLocation("block/${name}_side"))
            .texture("front", LibUtils.resourceLocation("block/${name}_front_${axis}"))
            .texture("south", LibUtils.resourceLocation("block/${name}_back${if (powered) "_on" else ""}"))
            .texture("bottom", LibUtils.resourceLocation("block/${name}_side"))
            .texture("top", LibUtils.resourceLocation("block/${name}_side"))


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