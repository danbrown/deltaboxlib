package com.dannbrown.databoxlib.datagen.content

import com.dannbrown.databoxlib.content.block.CustomSaplingBlock
import com.dannbrown.databoxlib.content.block.CustomStandingSignBlock
import com.dannbrown.databoxlib.content.block.CustomWallSignBlock
import com.dannbrown.databoxlib.content.block.FlammableBlock
import com.dannbrown.databoxlib.content.block.FlammableLeavesBlock
import com.dannbrown.databoxlib.content.block.FlammablePillarBlock
import com.dannbrown.databoxlib.content.block.FlammableWallBlock
import com.dannbrown.databoxlib.content.item.CustomSignItem
import com.dannbrown.databoxlib.content.utils.toolTiers.SetTier
import com.dannbrown.databoxlib.content.utils.toolTiers.SetTool
import com.dannbrown.databoxlib.datagen.transformers.BlockItemFactory
import com.dannbrown.databoxlib.datagen.transformers.BlockLootPresets
import com.dannbrown.databoxlib.datagen.transformers.BlockTagPresets
import com.dannbrown.databoxlib.datagen.transformers.BlockstatePresets
import com.dannbrown.databoxlib.datagen.transformers.ItemModelPresets
import com.dannbrown.databoxlib.datagen.transformers.RecipePresets
import com.dannbrown.databoxlib.lib.LibTags
import com.dannbrown.databoxlib.lib.LibUtils
import com.simibubi.create.content.decoration.MetalLadderBlock
import com.simibubi.create.content.decoration.MetalScaffoldingBlock
import com.simibubi.create.content.decoration.TrainTrapdoorBlock
import com.simibubi.create.content.decoration.encasing.CasingBlock
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry
import com.tterrag.registrate.providers.RegistrateRecipeProvider
import com.tterrag.registrate.util.DataIngredient
import com.tterrag.registrate.util.entry.BlockEntry
import net.minecraft.core.BlockPos
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.ButtonBlock
import net.minecraft.world.level.block.DoorBlock
import net.minecraft.world.level.block.FenceBlock
import net.minecraft.world.level.block.FenceGateBlock
import net.minecraft.world.level.block.FlowerPotBlock
import net.minecraft.world.level.block.IronBarsBlock
import net.minecraft.world.level.block.PressurePlateBlock
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.StandingSignBlock
import net.minecraft.world.level.block.TrapDoorBlock
import net.minecraft.world.level.block.WallBlock
import net.minecraft.world.level.block.WallSignBlock
import net.minecraft.world.level.block.grower.AbstractTreeGrower
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockSetType
import net.minecraft.world.level.block.state.properties.WoodType
import net.minecraft.world.level.material.MapColor
import net.minecraftforge.client.model.generators.ModelFile
import java.util.function.Supplier

class BlockFamilyGen(name: String) {
  private val name = name
  private var sharedProps: (BlockBehaviour.Properties) -> BlockBehaviour.Properties =
    { p: BlockBehaviour.Properties -> p }
  private var toolTier: SetTier = SetTier.WOOD
  private var toolType: SetTool = SetTool.NONE
  private var color: MapColor = MapColor.COLOR_GRAY
  private var accentColor: MapColor = MapColor.COLOR_GRAY
  private var copyFrom: Supplier<Block> = Supplier { Blocks.STONE }
  private val denyList = mutableListOf<BlockFamily.Type>()
  private val blockFamily: BlockFamily = BlockFamily()
  fun sharedProps(props: (BlockBehaviour.Properties) -> BlockBehaviour.Properties = { p: BlockBehaviour.Properties -> p }): BlockFamilyGen {
    sharedProps = { p -> props(p) }
    return this
  }

  fun toolAndTier(
    tool: SetTool = SetTool.NONE,
    tier: SetTier = SetTier.WOOD,
    requiredForDrops: Boolean = true
  ): BlockFamilyGen {
    toolTier = tier
    toolType = tool
    if (requiredForDrops) {
      sharedProps { p -> p.requiresCorrectToolForDrops() }
    }
    return this
  }

  fun color(
    color: MapColor = MapColor.COLOR_GRAY,
    accentColor: MapColor = MapColor.COLOR_GRAY
  ): BlockFamilyGen {
    this.color = color
    this.accentColor = accentColor
    return this
  }

  fun copyFrom(block: Supplier<Block>): BlockFamilyGen {
    copyFrom = block
    return this
  }

  fun denyList(vararg deny: BlockFamily.Type): BlockFamilyGen {
    this.denyList.addAll(deny)
    return this
  }

  fun metalCasingFamily(
    ingredient: Supplier<DataIngredient>,
    casingShift: CTSpriteShiftEntry,
    panelShift: CTSpriteShiftEntry,
    scaffoldShift: CTSpriteShiftEntry,
    scaffoldInsideShift: CTSpriteShiftEntry,
  ): BlockFamily {
    copyFrom { Blocks.CUT_COPPER }

    blockFamily.setVariant(BlockFamily.Type.MAIN) {
      BlockGen<CasingBlock>(name)
        .metalCasing(casingShift)
        .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
        .register()
    }

    if (!denyList.contains(BlockFamily.Type.LAMP)) {
      blockFamily.setVariant(BlockFamily.Type.LAMP) {
        BlockGen<CasingBlock>(name)
          .metalPanelCasing(panelShift)
          .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
          .properties { p -> p.lightLevel { 10 } }
          .register()
      }
    }

    if (!denyList.contains(BlockFamily.Type.SCAFFOLDING)) {
      blockFamily.setVariant(BlockFamily.Type.SCAFFOLDING) {
        BlockGen<MetalScaffoldingBlock>(name)
          .metalScaffoldingBlock(ingredient, scaffoldShift, scaffoldInsideShift, casingShift)
          .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
          .register()
      }
    }

    if (!denyList.contains(BlockFamily.Type.BARS)) {
      blockFamily.setVariant(BlockFamily.Type.BARS) {
        BlockGen<IronBarsBlock>(name)
          .metalBarsBlock(ingredient)
          .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
          .register()
      }
    }

    if (!denyList.contains(BlockFamily.Type.TRAPDOOR)) {
      blockFamily.setVariant(BlockFamily.Type.TRAPDOOR) {
        BlockGen<TrainTrapdoorBlock>(name)
          .metalTrapdoorBlock(ingredient)
          .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
          .register()
      }
    }

    if (!denyList.contains(BlockFamily.Type.LADDER)) {
      blockFamily.setVariant(BlockFamily.Type.LADDER) {
        BlockGen<MetalLadderBlock>(name)
          .metalLadderBlock(ingredient)
          .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
          .register()
      }
    }

    return blockFamily
  }

  /**
   * Returns a Long block family composed by Normals, Polished, Bricks, Cut, Chiseled variants (stairs, slabs, walls)
   */
  fun longBlockFamily(mainBlock: BlockEntry<out Block>? = null, isRotatedBlock: Boolean = false): BlockFamily {
    val MATERIAL_TAG = LibTags.databoxlibItemTag(name + "_blocks")

    if (mainBlock == null) {
      blockFamily.setVariant(BlockFamily.Type.MAIN) {
        BlockGen<Block>(name)
          .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .blockTags(listOf(*BlockTagPresets.caveReplaceableTags().first))
          .register()
      }
    }
    else {
      blockFamily.setVariant(BlockFamily.Type.MAIN) { mainBlock }
    }

    if (!denyList.contains(BlockFamily.Type.MAIN)) {
      if (!denyList.contains(BlockFamily.Type.STAIRS)) {
        blockFamily.setVariant(BlockFamily.Type.STAIRS) {
          BlockGen<StairBlock>(name)
            .stairsBlock({ blockFamily.MAIN!!.defaultState }, isRotatedBlock)
            .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.MAIN!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.stairsCraftingRecipe(c, p) {
                DataIngredient.items(
                  blockFamily.MAIN!!.get()
                    .asItem()
                )
              }
            }
            .register()
        }
      }

      if (!denyList.contains(BlockFamily.Type.SLAB)) {
        blockFamily.setVariant(BlockFamily.Type.SLAB) {
          BlockGen<SlabBlock>(name)
            .slabBlock(isRotatedBlock)
            .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.MAIN!!.get()
                    .asItem())
                },
                2
              )

              RecipePresets.slabCraftingRecipe(c, p) {
                DataIngredient.items(
                  blockFamily.MAIN!!.get()
                    .asItem()
                )
              }
            }
            .register()
        }
      }

      if (!denyList.contains(BlockFamily.Type.WALL)) {
        blockFamily.setVariant(BlockFamily.Type.WALL) {
          BlockGen<WallBlock>(name)
            .wallBlock(isRotatedBlock)
            .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.MAIN!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.wallCraftingRecipe(c, p) {
                DataIngredient.items(
                  blockFamily.MAIN!!.get()
                    .asItem()
                )
              }
            }
            .register()
        }
      }
    }
    // start polished chain
    if (!denyList.contains(BlockFamily.Type.POLISHED)) {
      blockFamily.setVariant(BlockFamily.Type.POLISHED) {
        BlockGen<Block>("polished_$name")
          .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .recipe { c, p ->
            RecipePresets.polishedCraftingRecipe(
              c,
              p,
              { DataIngredient.items(blockFamily.MAIN!!.get()) },
              4
            )
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              { DataIngredient.items(blockFamily.MAIN!!.get()) },
              1
            )
          }
          .register()
      }

      if (!denyList.contains(BlockFamily.Type.POLISHED_STAIRS)) {
        blockFamily.setVariant(BlockFamily.Type.POLISHED_STAIRS) {
          BlockGen<StairBlock>("polished_$name")
            .stairsBlock({ blockFamily.POLISHED!!.defaultState })
            .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.MAIN!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.POLISHED!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.stairsCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(blockFamily.POLISHED!!.get()
                  .asItem())
              }
            }
            .register()
        }
      }

      if (!denyList.contains(BlockFamily.Type.POLISHED_SLAB)) {
        blockFamily.setVariant(BlockFamily.Type.POLISHED_SLAB) {
          BlockGen<SlabBlock>("polished_$name")
            .slabBlock()
            .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c, p,
                {
                  DataIngredient.items(blockFamily.MAIN!!.get()
                    .asItem())
                },
                2
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.POLISHED!!.get()
                    .asItem())
                },
                2
              )
              RecipePresets.slabRecycleRecipe(c, p) {
                blockFamily.POLISHED!!.get()
                  .asItem()
              }
              RecipePresets.slabCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(blockFamily.POLISHED!!.get()
                  .asItem())
              }
            }
            .register()
        }
      }

      if (!denyList.contains(BlockFamily.Type.POLISHED_WALL)) {
        blockFamily.setVariant(BlockFamily.Type.POLISHED_WALL) {
          BlockGen<WallBlock>("polished_$name")
            .wallBlock()
            .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.MAIN!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.POLISHED!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.wallCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(blockFamily.POLISHED!!.get()
                  .asItem())
              }
            }
            .register()
        }
      }
    }
    // start bricks chain
    if (!denyList.contains(BlockFamily.Type.BRICKS)) {
      blockFamily.setVariant(BlockFamily.Type.BRICKS) {
        BlockGen<Block>("${name}_bricks")
          .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .recipe { c, p ->
            RecipePresets.polishedCraftingRecipe(
              c,
              p,
              { DataIngredient.items(blockFamily.POLISHED!!.get()) },
              4
            )
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              { DataIngredient.items(blockFamily.MAIN!!.get()) },
              1
            )
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              { DataIngredient.items(blockFamily.POLISHED!!.get()) },
              1
            )
          }
          .register()
      }

      if (!denyList.contains(BlockFamily.Type.BRICK_STAIRS)) {
        blockFamily.setVariant(BlockFamily.Type.BRICK_STAIRS) {
          BlockGen<StairBlock>("${name}_bricks")
            .stairsBlock({ blockFamily.BRICKS!!.defaultState })
            .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.MAIN!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.BRICKS!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.POLISHED!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.stairsCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(blockFamily.BRICKS!!.get()
                  .asItem())
              }
            }
            .register()
        }
      }

      if (!denyList.contains(BlockFamily.Type.BRICK_SLAB)) {
        blockFamily.setVariant(BlockFamily.Type.BRICK_SLAB) {
          BlockGen<SlabBlock>("${name}_bricks")
            .slabBlock()
            .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c, p,
                {
                  DataIngredient.items(blockFamily.MAIN!!.get()
                    .asItem())
                },
                2
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.POLISHED!!.get()
                    .asItem())
                },
                2
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.BRICKS!!.get()
                    .asItem())
                },
                2
              )
              RecipePresets.slabRecycleRecipe(c, p) {
                blockFamily.BRICKS!!.get()
                  .asItem()
              }
              RecipePresets.slabCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(blockFamily.BRICKS!!.get()
                  .asItem())
              }
            }
            .register()
        }
      }

      if (!denyList.contains(BlockFamily.Type.BRICK_WALL)) {
        blockFamily.setVariant(BlockFamily.Type.BRICK_WALL) {
          BlockGen<WallBlock>("${name}_bricks")
            .wallBlock()
            .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.MAIN!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.POLISHED!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.BRICKS!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.wallCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(blockFamily.BRICKS!!.get()
                  .asItem())
              }
            }
            .register()
        }
      }
    }
    // start chiseled chain
    if (!denyList.contains(BlockFamily.Type.CHISELED)) {
      blockFamily.setVariant(BlockFamily.Type.CHISELED) {
        BlockGen<Block>("chiseled_$name")
          .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .recipe { c, p ->
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              {
                DataIngredient.items(blockFamily.MAIN!!.get()
                  .asItem())
              },
              1
            )
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              {
                DataIngredient.items(blockFamily.POLISHED!!.get()
                  .asItem())
              },
              1
            )
            RecipePresets.slabToChiseledRecipe(c, p) {
              DataIngredient.items(blockFamily.SLAB!!.get()
                .asItem())
            }
          }
          .register()
      }
    }
    // PILLAR
    if (!denyList.contains(BlockFamily.Type.PILLAR)) {
      blockFamily.setVariant(BlockFamily.Type.PILLAR) {
        BlockGen<RotatedPillarBlock>("${name}_pillar")
          .rotatedPillarBlock("${name}_pillar_top", "${name}_pillar_side")
          .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .recipe { c, p ->
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              {
                DataIngredient.items(blockFamily.MAIN!!.get()
                  .asItem())
              },
              1
            )
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              {
                DataIngredient.items(blockFamily.POLISHED!!.get()
                  .asItem())
              },
              1
            )
            RecipePresets.slabToChiseledRecipe(c, p) {
              DataIngredient.items(blockFamily.MAIN!!.get()
                .asItem())
            }
          }
          .register()
      }
    }

    return blockFamily
  }

  /**
   * Returns a Mineral block family composed by Normals and Polished variants (stairs, slabs, walls)
   */
  fun mineralFamily(mainBlock: BlockEntry<out Block>? = null, isRotatedBlock: Boolean = false): BlockFamily {
    val MATERIAL_TAG = LibTags.databoxlibItemTag(name + "_blocks")

    if (mainBlock == null) {
      blockFamily.setVariant(BlockFamily.Type.MAIN) {
        BlockGen<Block>(name)
          .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .blockTags(listOf(*BlockTagPresets.caveReplaceableTags().first))
          .register()
      }
    }
    else {
      blockFamily.setVariant(BlockFamily.Type.MAIN) { mainBlock }
    }

    if (!denyList.contains(BlockFamily.Type.STAIRS)) {
      blockFamily.setVariant(BlockFamily.Type.STAIRS) {
        BlockGen<StairBlock>(name)
          .stairsBlock({ blockFamily.MAIN!!.defaultState }, isRotatedBlock)
          .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .recipe { c, p ->
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              {
                DataIngredient.items(blockFamily.MAIN!!.get()
                  .asItem())
              },
              1
            )
            RecipePresets.stairsCraftingRecipe(c, p) {
              DataIngredient.items(
                blockFamily.MAIN!!.get()
                  .asItem()
              )
            }
          }
          .register()
      }
    }

    if (!denyList.contains(BlockFamily.Type.SLAB)) {
      blockFamily.setVariant(BlockFamily.Type.SLAB) {
        BlockGen<SlabBlock>(name)
          .slabBlock(isRotatedBlock)
          .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .recipe { c, p ->
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              {
                DataIngredient.items(blockFamily.MAIN!!.get()
                  .asItem())
              },
              2
            )
            RecipePresets.slabRecycleRecipe(c, p) {
              blockFamily.MAIN!!.get()
                .asItem()
            }
            RecipePresets.slabCraftingRecipe(c, p) {
              DataIngredient.items(
                blockFamily.MAIN!!.get()
                  .asItem()
              )
            }
          }
          .register()
      }
    }

    if (!denyList.contains(BlockFamily.Type.WALL)) {
      blockFamily.setVariant(BlockFamily.Type.WALL) {
        BlockGen<WallBlock>(name)
          .wallBlock(isRotatedBlock)
          .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .recipe { c, p ->
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              {
                DataIngredient.items(blockFamily.MAIN!!.get()
                  .asItem())
              },
              1
            )
            RecipePresets.wallCraftingRecipe(c, p) {
              DataIngredient.items(
                blockFamily.MAIN!!.get()
                  .asItem()
              )
            }
          }
          .register()
      }
    }
    // start polished chain
    if (!denyList.contains(BlockFamily.Type.POLISHED)) {
      blockFamily.setVariant(BlockFamily.Type.POLISHED) {
        BlockGen<Block>("polished_$name")
          .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .recipe { c, p ->
            RecipePresets.polishedCraftingRecipe(
              c,
              p,
              { DataIngredient.items(blockFamily.MAIN!!.get()) },
              4
            )
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              { DataIngredient.items(blockFamily.MAIN!!.get()) },
              1
            )
          }
          .register()
      }

      if (!denyList.contains(BlockFamily.Type.POLISHED_STAIRS)) {
        blockFamily.setVariant(BlockFamily.Type.POLISHED_STAIRS) {
          BlockGen<StairBlock>("polished_$name")
            .stairsBlock({ blockFamily.POLISHED!!.defaultState })
            .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.MAIN!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.POLISHED!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.stairsCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(blockFamily.POLISHED!!.get()
                  .asItem())
              }
            }
            .register()
        }
      }

      if (!denyList.contains(BlockFamily.Type.POLISHED_SLAB)) {
        blockFamily.setVariant(BlockFamily.Type.POLISHED_SLAB) {
          BlockGen<SlabBlock>("polished_$name")
            .slabBlock()
            .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c, p,
                {
                  DataIngredient.items(blockFamily.MAIN!!.get()
                    .asItem())
                },
                2
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.POLISHED!!.get()
                    .asItem())
                },
                2
              )
              RecipePresets.slabRecycleRecipe(c, p) {
                blockFamily.POLISHED!!.get()
                  .asItem()
              }
              RecipePresets.slabCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(blockFamily.POLISHED!!.get()
                  .asItem())
              }
            }
            .register()
        }
      }

      if (!denyList.contains(BlockFamily.Type.POLISHED_WALL)) {
        blockFamily.setVariant(BlockFamily.Type.POLISHED_WALL) {
          BlockGen<WallBlock>("polished_$name")
            .wallBlock()
            .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.MAIN!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.POLISHED!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.wallCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(blockFamily.POLISHED!!.get()
                  .asItem())
              }
            }
            .register()
        }
      }
    }
    // start bricks chain
    if (!denyList.contains(BlockFamily.Type.BRICKS)) {
      blockFamily.setVariant(BlockFamily.Type.BRICKS) {
        BlockGen<Block>("${name}_bricks")
          .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .recipe { c, p ->
            RecipePresets.polishedCraftingRecipe(
              c,
              p,
              { DataIngredient.items(blockFamily.POLISHED!!.get()) },
              4
            )
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              { DataIngredient.items(blockFamily.MAIN!!.get()) },
              1
            )
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              { DataIngredient.items(blockFamily.POLISHED!!.get()) },
              1
            )
          }
          .register()
      }

      if (!denyList.contains(BlockFamily.Type.BRICK_STAIRS)) {
        blockFamily.setVariant(BlockFamily.Type.BRICK_STAIRS) {
          BlockGen<StairBlock>("${name}_bricks")
            .stairsBlock({ blockFamily.BRICKS!!.defaultState })
            .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.MAIN!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.BRICKS!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.POLISHED!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.stairsCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(blockFamily.BRICKS!!.get()
                  .asItem())
              }
            }
            .register()
        }
      }

      if (!denyList.contains(BlockFamily.Type.BRICK_SLAB)) {
        blockFamily.setVariant(BlockFamily.Type.BRICK_SLAB) {
          BlockGen<SlabBlock>("${name}_bricks")
            .slabBlock()
            .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c, p,
                {
                  DataIngredient.items(blockFamily.MAIN!!.get()
                    .asItem())
                },
                2
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.POLISHED!!.get()
                    .asItem())
                },
                2
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.BRICKS!!.get()
                    .asItem())
                },
                2
              )
              RecipePresets.slabRecycleRecipe(c, p) {
                blockFamily.BRICKS!!.get()
                  .asItem()
              }
              RecipePresets.slabCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(blockFamily.BRICKS!!.get()
                  .asItem())
              }
            }
            .register()
        }
      }

      if (!denyList.contains(BlockFamily.Type.BRICK_WALL)) {
        blockFamily.setVariant(BlockFamily.Type.BRICK_WALL) {
          BlockGen<WallBlock>("${name}_bricks")
            .wallBlock()
            .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.MAIN!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.POLISHED!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.BRICKS!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.wallCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(blockFamily.BRICKS!!.get()
                  .asItem())
              }
            }
            .register()
        }
      }
    }

    return blockFamily
  }

  /**
   * Returns a SandStone block family composed by
   */
  fun sandstoneFamily(baseBlock: BlockEntry<out Block>): BlockFamily {
    val MATERIAL_TAG = LibTags.databoxlibItemTag(name + "_blocks")
    blockFamily.setVariant(BlockFamily.Type.MAIN) { baseBlock }

    if (!denyList.contains(BlockFamily.Type.SANDSTONE)) {
      blockFamily.setVariant(BlockFamily.Type.SANDSTONE) {
        BlockGen<Block>(name + "_sandstone")
          .bottomTopBlock()
          .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .recipe { c, p ->
            RecipePresets.polishedCraftingRecipe(
              c,
              p,
              {
                DataIngredient.items(blockFamily.MAIN!!.get()
                  .asItem())
              })
          }
          .register()
      }

      if (!denyList.contains(BlockFamily.Type.SANDSTONE_STAIRS)) {
        blockFamily.setVariant(BlockFamily.Type.SANDSTONE_STAIRS) {
          BlockGen<StairBlock>(name + "_sandstone")
            .stairsBlock({ blockFamily.SANDSTONE!!.defaultState }, true)
            .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.SANDSTONE!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.stairsCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(blockFamily.SANDSTONE!!.get()
                  .asItem())
              }
            }
            .register()
        }
      }

      if (!denyList.contains(BlockFamily.Type.SANDSTONE_SLAB)) {
        blockFamily.setVariant(BlockFamily.Type.SANDSTONE_SLAB) {
          BlockGen<SlabBlock>(name + "_sandstone")
            .slabBlock(true)
            .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.SANDSTONE!!.get()
                    .asItem())
                },
                2
              )
              RecipePresets.slabCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(blockFamily.SANDSTONE!!.get()
                  .asItem())
              }
            }
            .register()
        }
      }

      if (!denyList.contains(BlockFamily.Type.SANDSTONE_WALL)) {
        blockFamily.setVariant(BlockFamily.Type.SANDSTONE_WALL) {
          BlockGen<WallBlock>(name + "_sandstone")
            .wallBlock()
            .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.SANDSTONE!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.wallCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(blockFamily.SANDSTONE!!.get()
                  .asItem())
              }
            }
            .register()
        }
      }
      // CHISELED
      if (!denyList.contains(BlockFamily.Type.CHISELED)) {
        blockFamily.setVariant(BlockFamily.Type.CHISELED) {
          BlockGen<Block>("chiseled_$name" + "_sandstone")
            .bottomTopBlock(name + "_sandstone_bottom", name + "_sandstone_top")
            .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.SANDSTONE!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.slabToChiseledRecipe(c, p) {
                DataIngredient.items(
                  blockFamily.SANDSTONE_SLAB!!.get()
                    .asItem()
                )
              }
            }
            .register()
        }
      }
      // CUT
      if (!denyList.contains(BlockFamily.Type.CUT)) {
        blockFamily.setVariant(BlockFamily.Type.CUT) {
          BlockGen<Block>("cut_$name" + "_sandstone")
            .bottomTopBlock(name + "_sandstone_bottom", name + "_sandstone_top")
            .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.SANDSTONE!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.polishedCraftingRecipe(
                c,
                p,
                {
                  DataIngredient.items(blockFamily.SANDSTONE!!.get()
                    .asItem())
                },
                4
              )
            }
            .register()
        }
      }
      // SMOOTH
      if (!denyList.contains(BlockFamily.Type.SMOOTH)) {
        blockFamily.setVariant(BlockFamily.Type.SMOOTH) {
          BlockGen<Block>("smooth_$name" + "_sandstone")
            .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
            .textureName(name + "_sandstone_top")
            .transform { t ->
              t.blockstate { c, p ->
                p.simpleBlockWithItem(
                  c.get(),
                  p.models()
                    .withExistingParent(
                      c.name,
                      p.mcLoc("block/cube_all")
                    )
                    .texture("all", p.modLoc("block/" + name + "_sandstone_top"))
                )
              }
            }
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              p.smelting(
                DataIngredient.items(blockFamily.SANDSTONE!!.get()
                  .asItem()),
                RecipeCategory.BUILDING_BLOCKS,
                { c.get() },
                0.1f,
                200
              )
            }
            .register()
        }

        if (!denyList.contains(BlockFamily.Type.SMOOTH_STAIRS)) {
          blockFamily.setVariant(BlockFamily.Type.SMOOTH_STAIRS) {
            BlockGen<StairBlock>("smooth_$name" + "_sandstone")
              .stairsBlock({ blockFamily.SMOOTH!!.defaultState })
              .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
              .textureName(name + "_sandstone_top")
              .itemTags(listOf(MATERIAL_TAG))
              .recipe { c, p ->
                RecipePresets.simpleStonecuttingRecipe(
                  c,
                  p,
                  {
                    DataIngredient.items(blockFamily.SMOOTH!!.get()
                      .asItem())
                  },
                  1
                )
                RecipePresets.stairsCraftingRecipe(
                  c,
                  p
                ) {
                  DataIngredient.items(blockFamily.SMOOTH!!.get()
                    .asItem())
                }
              }
              .register()
          }
        }

        if (!denyList.contains(BlockFamily.Type.SMOOTH_SLAB)) {
          blockFamily.setVariant(BlockFamily.Type.SMOOTH_SLAB) {
            BlockGen<SlabBlock>("smooth_$name" + "_sandstone")
              .slabBlock()
              .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
              .textureName(name + "_sandstone_top")
              .itemTags(listOf(MATERIAL_TAG))
              .recipe { c, p ->
                RecipePresets.simpleStonecuttingRecipe(
                  c,
                  p,
                  {
                    DataIngredient.items(blockFamily.SMOOTH!!.get()
                      .asItem())
                  },
                  2
                )
                RecipePresets.slabRecycleRecipe(c, p) {
                  blockFamily.SMOOTH!!.get()
                    .asItem()
                }
                RecipePresets.slabCraftingRecipe(
                  c,
                  p
                ) {
                  DataIngredient.items(blockFamily.SMOOTH!!.get()
                    .asItem())
                }
              }
              .register()
          }
        }

        if (!denyList.contains(BlockFamily.Type.SMOOTH_WALL)) {
          blockFamily.setVariant(BlockFamily.Type.SMOOTH_WALL) {
            BlockGen<WallBlock>("smooth_$name" + "_sandstone")
              .wallBlock()
              .fromFamily(copyFrom, sharedProps, color, toolType, toolTier)
              .textureName(name + "_sandstone_top")
              .itemTags(listOf(MATERIAL_TAG))
              .recipe { c, p ->
                RecipePresets.simpleStonecuttingRecipe(
                  c,
                  p,
                  {
                    DataIngredient.items(blockFamily.SMOOTH!!.get()
                      .asItem())
                  },
                  1
                )
                RecipePresets.wallCraftingRecipe(
                  c,
                  p
                ) {
                  DataIngredient.items(blockFamily.SMOOTH!!.get()
                    .asItem())
                }
              }
              .register()
          }
        }
      }
    }

    return blockFamily
  }

  fun stalkWoodFamily(
    woodType: WoodType,
    grower: AbstractTreeGrower,
    placeOn: ((blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos) -> Boolean)? = null
  ): BlockFamily {
    val LOG_TAG_BLOCK = LibTags.databoxlibBlockTag(name + "_log_blocks")
    val LOG_TAG_ITEM = LibTags.databoxlibItemTag(name + "_log_blocks")
    val FORGE_LEAVES_TAG_BLOCK = LibTags.forgeBlockTag("leaves")
    val FORGE_LEAVES_TAG_ITEM = LibTags.forgeItemTag("leaves")
    val FORGE_STRIPPED_LOGS_TAG_BLOCK = LibTags.forgeBlockTag("stripped_logs")
    val FORGE_STRIPPED_LOGS_TAG_ITEM = LibTags.forgeItemTag("stripped_logs")
    // Stalks
    blockFamily.setVariant(BlockFamily.Type.STALK) {
      BlockGen<FlammableWallBlock>(name + "_stalk")
        .wallBlock(true, false)
        .blockFactory { p -> FlammableWallBlock(p) }
        .copyFrom({ Blocks.OAK_LOG })
        .toolAndTier(SetTool.AXE, SetTier.WOOD, false)
        .blockTags(listOf(LOG_TAG_BLOCK, BlockTags.LOGS))
        .itemTags(listOf(LOG_TAG_ITEM, ItemTags.LOGS))
        .transform { b ->
          b
            .loot(BlockLootPresets.dropItselfLoot())
            .item(BlockItemFactory.fuelBlockItem(75))
            .lang("${
              name.replace("_", " ")
                .replaceFirstChar { it.uppercase() }
            } Stalk")
            .model(ItemModelPresets.bottomTopWallItem(name + "_stalk"))
            .tag(LOG_TAG_ITEM)
            .build()
        }
        .noItem()
        .register()
    }

    blockFamily.setVariant(BlockFamily.Type.STRIPPED_STALK) {
      BlockGen<FlammableWallBlock>("stripped_$name" + "_stalk")
        .wallBlock(true, false)
        .blockFactory { p -> FlammableWallBlock(p) }
        .copyFrom({ Blocks.STRIPPED_OAK_LOG })
        .toolAndTier(SetTool.AXE, SetTier.WOOD, false)
        .blockTags(listOf(LOG_TAG_BLOCK, BlockTags.LOGS))
        .transform { b ->
          b
            .loot(BlockLootPresets.dropItselfLoot())
            .item(BlockItemFactory.fuelBlockItem(75))
            .lang("Stripped ${
              name.replace("_", " ")
                .replaceFirstChar { it.uppercase() }
            } Stalk")
            .model(ItemModelPresets.bottomTopWallItem("stripped_$name" + "_stalk"))
            .tag(LOG_TAG_ITEM)
            .build()
        }
        .register()
    }
    // Logs
    blockFamily.setVariant(BlockFamily.Type.LOG) {
      BlockGen<FlammablePillarBlock>(name + "_logs")
        .rotatedPillarBlock(name + "_logs_top", name + "_logs")
        .blockFactory { p -> FlammablePillarBlock(p) }
        .copyFrom({ Blocks.OAK_LOG })
        .toolAndTier(SetTool.AXE, SetTier.WOOD, false)
//        .fromFamily(Blocks.OAK_LOG, sharedProps, color, toolType, toolTier, false)
        .blockTags(listOf(BlockTags.LOGS, LOG_TAG_BLOCK, BlockTags.LOGS_THAT_BURN))
        .itemTags(listOf(ItemTags.LOGS, LOG_TAG_ITEM, ItemTags.LOGS_THAT_BURN))
        .recipe { c, p ->
          RecipePresets.polishedCraftingRecipe(c, p, { DataIngredient.items(blockFamily.STALK!!.get()) }, 1)
        }
        .register()
    }

    blockFamily.setVariant(BlockFamily.Type.WOOD) {
      BlockGen<FlammablePillarBlock>(name + "_wood")
        .rotatedPillarBlock(name + "_logs", name + "_logs")
        .blockFactory { p -> FlammablePillarBlock(p) }
        .copyFrom({ Blocks.OAK_WOOD })
        .toolAndTier(SetTool.AXE, SetTier.WOOD, false)
        //        .fromFamily(Blocks.OAK_WOOD, sharedProps, accentColor, toolType, toolTier, false)
        .blockTags(listOf(BlockTags.LOGS, LOG_TAG_BLOCK, BlockTags.LOGS_THAT_BURN))
        .itemTags(listOf(ItemTags.LOGS, LOG_TAG_ITEM, ItemTags.LOGS_THAT_BURN))
        .recipe { c, p ->
          RecipePresets.polishedCraftingRecipe(c, p, { DataIngredient.items(blockFamily.LOG!!.get()) }, 3)
        }
        .register()
    }
    // Stripped Logs
    blockFamily.setVariant(BlockFamily.Type.STRIPPED_LOG) {
      BlockGen<FlammablePillarBlock>("stripped_$name" + "_logs")
        .rotatedPillarBlock("stripped_$name" + "_logs_top", "stripped_$name" + "_logs")
        .blockFactory { p -> FlammablePillarBlock(p) }
        .copyFrom({ Blocks.STRIPPED_OAK_LOG })
        .toolAndTier(SetTool.AXE, SetTier.WOOD, false)
//        .fromFamily(Blocks.STRIPPED_OAK_LOG, sharedProps, color, toolType, toolTier, false)
        .blockTags(listOf(BlockTags.LOGS, LOG_TAG_BLOCK, FORGE_STRIPPED_LOGS_TAG_BLOCK, BlockTags.LOGS_THAT_BURN))
        .itemTags(listOf(ItemTags.LOGS, LOG_TAG_ITEM, FORGE_STRIPPED_LOGS_TAG_ITEM, ItemTags.LOGS_THAT_BURN))
        .recipe { c, p ->
          RecipePresets.polishedCraftingRecipe(c, p, { DataIngredient.items(blockFamily.STRIPPED_STALK!!.get()) }, 1)
        }
        .register()
    }

    blockFamily.setVariant(BlockFamily.Type.STRIPPED_WOOD) {
      BlockGen<FlammablePillarBlock>("stripped_$name" + "_wood")
        .rotatedPillarBlock("stripped_$name" + "_logs", "stripped_$name" + "_logs")
        .blockFactory { p -> FlammablePillarBlock(p) }
        .copyFrom({ Blocks.STRIPPED_OAK_WOOD })
        .toolAndTier(SetTool.AXE, SetTier.WOOD, false)
//        .fromFamily(Blocks.STRIPPED_OAK_WOOD, sharedProps, accentColor, toolType, toolTier, false)
        .blockTags(listOf(BlockTags.LOGS, LOG_TAG_BLOCK, FORGE_STRIPPED_LOGS_TAG_BLOCK, BlockTags.LOGS_THAT_BURN))
        .itemTags(listOf(ItemTags.LOGS, LOG_TAG_ITEM, FORGE_STRIPPED_LOGS_TAG_ITEM, ItemTags.LOGS_THAT_BURN))
        .recipe { c, p ->
          RecipePresets.polishedCraftingRecipe(c, p, { DataIngredient.items(blockFamily.STRIPPED_LOG!!.get()) }, 3)
        }
        .register()
    }

    blockFamily.setVariant(BlockFamily.Type.SAPLING) {
      BlockGen<CustomSaplingBlock>("$name" + "_sapling")
        .blockFactory { p -> CustomSaplingBlock(grower, p, placeOn) }
        .blockTags(listOf(BlockTags.SAPLINGS))
        .itemTags(listOf(ItemTags.SAPLINGS))
        .copyFrom({ Blocks.OAK_SAPLING })
        .properties { p ->
          p.mapColor(accentColor)
            .sound(SoundType.GRASS)
            .strength(0.0f)
            .randomTicks()
            .noCollission()
            .noOcclusion()
        }
        .transform { t ->
          t
            .blockstate(BlockstatePresets.simpleCrossBlock(name + "_sapling"))
            .item()
            .model(ItemModelPresets.simpleLayerItem(name + "_sapling"))
            .build()
        }
        .register()
    }

    blockFamily.setVariant(BlockFamily.Type.POTTED_SAPLING) {
      BlockGen<FlowerPotBlock>("potted_$name" + "_sapling")
        .blockFactory { p ->
          FlowerPotBlock(
            { Blocks.FLOWER_POT as FlowerPotBlock },
            { blockFamily.SAPLING!!.get() },
            p
          )
        }
        .copyFrom({ Blocks.POTTED_POPPY })
        .noItem()
        .properties { p ->
          p.mapColor(color)
            .noOcclusion()
        }
        .transform { t ->
          t
            .blockstate(BlockstatePresets.pottedPlantBlock(name + "_sapling"))
            .loot(BlockLootPresets.pottedPlantLoot { blockFamily.SAPLING!!.get() })
        }
        .register()
    }

    blockFamily.setVariant(BlockFamily.Type.LEAVES) {
      BlockGen<FlammableLeavesBlock>(name + "_leaves")
        .blockFactory { p -> FlammableLeavesBlock(p, 60, 30) }
        .color(MapColor.COLOR_GREEN)
        .copyFrom({ Blocks.OAK_LEAVES })
        .properties { p ->
          p
            .randomTicks()
            .noOcclusion()
            .isSuffocating { s, b, p -> false }
            .isViewBlocking { s, b, p -> false }
            .isRedstoneConductor { s, b, p -> false }
            .ignitedByLava()
        }
        .blockTags(listOf(BlockTags.LEAVES, FORGE_LEAVES_TAG_BLOCK))
        .itemTags(listOf(ItemTags.LEAVES, FORGE_LEAVES_TAG_ITEM))
        .transform { t ->
          t
            .blockstate(BlockstatePresets.simpleTransparentBlock(name + "_leaves"))
            .loot(BlockLootPresets.leavesLoot { blockFamily.SAPLING!!.get() })
        }
        .register()
    }
    // Main Block
    blockFamily.setVariant(BlockFamily.Type.MAIN) {
      BlockGen<FlammableBlock>(name + "_planks")
        .blockFactory { p -> FlammableBlock(p, 20, 5) }
//        .fromFamily(Blocks.OAK_PLANKS, sharedProps, accentColor, toolType, toolTier, false)
        .copyFrom({ Blocks.OAK_PLANKS })
        .toolAndTier(SetTool.AXE, SetTier.WOOD, false)
        .blockTags(listOf(BlockTags.PLANKS))
        .itemTags(listOf(ItemTags.PLANKS))
        .recipe { c, p ->
          RecipePresets.directShapelessRecipe(c, p, { DataIngredient.items(blockFamily.STALK!!.get()) }, 1)
          RecipePresets.directShapelessRecipe(c, p, { DataIngredient.items(blockFamily.STRIPPED_STALK!!.get()) }, 1)
          RecipePresets.directShapelessRecipe(c, p, { DataIngredient.items(blockFamily.LOG!!.get()) }, 4)
          RecipePresets.directShapelessRecipe(c, p, { DataIngredient.items(blockFamily.STRIPPED_LOG!!.get()) }, 4)
          RecipePresets.directShapelessRecipe(c, p, { DataIngredient.items(blockFamily.WOOD!!.get()) }, 4)
          RecipePresets.directShapelessRecipe(c, p, { DataIngredient.items(blockFamily.STRIPPED_WOOD!!.get()) }, 4)
          // make the boat as oak, as I am sleep-deprived and bored enough to not do it
          ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, Items.OAK_BOAT, 1)
            .define('X', c.get())
            .pattern("X X")
            .pattern("XXX")
            .unlockedBy("has_" + p.safeName(c.get()), RegistrateRecipeProvider.has(c.get()))
            .save(p, LibUtils.resourceLocation("oak_boat_from_" + p.safeName(c.get())))
        }
        .register()
    }
    // Stairs
    blockFamily.setVariant(BlockFamily.Type.STAIRS) {
      BlockGen<StairBlock>(name)
        .textureName(name + "_planks")
//        .fromFamily(Blocks.OAK_STAIRS, sharedProps, accentColor, toolType, toolTier, false)
        .copyFrom({ Blocks.OAK_STAIRS })
        .toolAndTier(SetTool.AXE, SetTier.WOOD, false)
        .stairsBlock({ blockFamily.MAIN!!.defaultState }, false, true)
        .blockTags(listOf(BlockTags.STAIRS, BlockTags.WOODEN_STAIRS))
        .itemTags(listOf(ItemTags.STAIRS, ItemTags.WOODEN_STAIRS))
        .recipe { c, p ->
          RecipePresets.stairsCraftingRecipe(c, p) {
            DataIngredient.items(blockFamily.MAIN!!.get()
              .asItem())
          }
        }
        .register()
    }
    // Slab
    blockFamily.setVariant(BlockFamily.Type.SLAB) {
      BlockGen<SlabBlock>(name)
        .textureName(name + "_planks")
        .slabBlock(false, true)
//        .fromFamily(Blocks.OAK_SLAB, sharedProps, accentColor, toolType, toolTier, false)
        .copyFrom({ Blocks.OAK_SLAB })
        .toolAndTier(SetTool.AXE, SetTier.WOOD, false)
        .recipe { c, p ->
          RecipePresets.slabRecycleRecipe(c, p) {
            blockFamily.MAIN!!.get()
              .asItem()
          }
          RecipePresets.slabCraftingRecipe(c, p) {
            DataIngredient.items(blockFamily.MAIN!!.get()
              .asItem())
          }
        }
        .register()
    }
    // Fence
    blockFamily.setVariant(BlockFamily.Type.FENCE) {
      BlockGen<FenceBlock>(name)
        .textureName(name + "_planks")
        .fenceBlock(true)
        .copyFrom({ Blocks.OAK_FENCE })
        .toolAndTier(SetTool.AXE, SetTier.WOOD, false)
        .blockTags(listOf(BlockTags.FENCES, BlockTags.WOODEN_FENCES))
        .itemTags(listOf(ItemTags.FENCES, ItemTags.WOODEN_FENCES))
        .recipe { c, p ->
          RecipePresets.fenceCraftingRecipe(c, p) {
            DataIngredient.items(blockFamily.MAIN!!.get()
              .asItem())
          }
        }
        .register()
    }
    // Fence Gate
    blockFamily.setVariant(BlockFamily.Type.FENCE_GATE) {
      BlockGen<FenceGateBlock>(name)
        .textureName(name + "_planks")
        .fenceGateBlock(woodType)
        .copyFrom({ Blocks.OAK_FENCE_GATE })
        .toolAndTier(SetTool.AXE, SetTier.WOOD, false)
        .blockTags(listOf(BlockTags.FENCE_GATES))
        .itemTags(listOf(ItemTags.FENCE_GATES))
        .recipe { c, p ->
          RecipePresets.fenceGateCraftingRecipe(c, p) {
            DataIngredient.items(blockFamily.MAIN!!.get()
              .asItem())
          }
        }
        .register()
    }
    // Pressure Plate
    blockFamily.setVariant(BlockFamily.Type.PRESSURE_PLATE) {
      BlockGen<PressurePlateBlock>(name)
        .textureName(name + "_planks")
        .pressurePlateBlock(BlockSetType.OAK)
        .copyFrom({ Blocks.OAK_PRESSURE_PLATE })
        .toolAndTier(SetTool.AXE, SetTier.WOOD, false)
//        .fromFamily(Blocks.OAK_PRESSURE_PLATE, sharedProps, accentColor, toolType, toolTier, false)
        .blockTags(listOf(BlockTags.WOODEN_PRESSURE_PLATES))
        .itemTags(listOf(ItemTags.WOODEN_PRESSURE_PLATES))
        .recipe { c, p ->
          RecipePresets.pressurePlateCraftingRecipe(c, p) {
            DataIngredient.items(blockFamily.MAIN!!.get()
              .asItem())
          }
        }
        .register()
    }
    // Button
    blockFamily.setVariant(BlockFamily.Type.BUTTON) {
      BlockGen<ButtonBlock>(name)
        .textureName(name + "_planks")
        .buttonBlock(BlockSetType.OAK, true)
        .copyFrom({ Blocks.OAK_BUTTON })
        .toolAndTier(SetTool.AXE, SetTier.WOOD, false)
//        .fromFamily(Blocks.OAK_BUTTON, sharedProps, accentColor, toolType, toolTier, false)
        .recipe { c, p ->
          RecipePresets.directShapelessRecipe(c,
            p,
            {
              DataIngredient.items(blockFamily.MAIN!!.get()
                .asItem())
            },
            1)
        }
        .register()
    }
    // Door
    blockFamily.setVariant(BlockFamily.Type.DOOR) {
      BlockGen<DoorBlock>(name)
        .woodenDoorBlock(BlockSetType.OAK)
        .copyFrom({ Blocks.OAK_DOOR })
        .toolAndTier(SetTool.AXE, SetTier.WOOD, false)
        .color(accentColor)
        .recipe { c, p ->
          RecipePresets.doorCraftingRecipe(c, p) {
            DataIngredient.items(blockFamily.MAIN!!.get()
              .asItem())
          }
        }
        .transform { b ->
          b
            .loot(BlockLootPresets.doorLoot())
        }
        .register()
    }
    // Trapdoor
    blockFamily.setVariant(BlockFamily.Type.TRAPDOOR) {
      BlockGen<TrapDoorBlock>(name)
        .woodenTrapdoorBlock({ DataIngredient.items(blockFamily.MAIN!!.get()) }, BlockSetType.OAK)
        .copyFrom({ Blocks.OAK_TRAPDOOR })
        .toolAndTier(SetTool.AXE, SetTier.WOOD, false)
        .color(accentColor)
        .properties { p ->
          p.sound(SoundType.WOOD)
            .noOcclusion()
        }
        .register()
    }
    // Sign
    blockFamily.setVariant(BlockFamily.Type.WALL_SIGN) {
      BlockGen<CustomWallSignBlock>(name + "_wall_sign")
        .noItem()
        .copyFrom({ Blocks.OAK_WALL_SIGN })
        .toolAndTier(SetTool.AXE, SetTier.WOOD, false)
        .blockFactory { p -> CustomWallSignBlock(p, woodType) { blockFamily.SIGN!!.get() } }
        .color(accentColor)
        .properties { p ->
          p.strength(1.0F)
            .sound(SoundType.WOOD)
            .noOcclusion()
        }
        .blockTags(listOf(BlockTags.WALL_SIGNS, BlockTags.SIGNS))
        .transform { t ->
          t
            .blockstate(BlockstatePresets.noBlockState())
            .loot(BlockLootPresets.dropOtherLoot { blockFamily.SIGN!!.get() })
        }
        .register()
    }

    blockFamily.setVariant(BlockFamily.Type.SIGN) {
      BlockGen<CustomStandingSignBlock>(name + "_sign")
        .blockFactory { p -> CustomStandingSignBlock(p, woodType) }
        .copyFrom({ Blocks.OAK_SIGN })
        .toolAndTier(SetTool.AXE, SetTier.WOOD, false)
        .color(accentColor)
        .noItem()
        .recipe { c, p ->
          RecipePresets.signCraftingRecipe(c, p) {
            DataIngredient.items(blockFamily.MAIN!!.get()
              .asItem())
          }
        }
        .transform { b ->
          b
            .properties { p ->
              p.strength(1.0F)
                .sound(SoundType.WOOD)
                .noOcclusion()
            }
            .tag(BlockTags.STANDING_SIGNS, BlockTags.SIGNS)
            .blockstate { c, p ->
              val signModel: ModelFile = p.models()
                .sign(c.name, LibUtils.resourceLocation("block/${name + "_planks"}"))
              p.simpleBlock(c.get() as StandingSignBlock, signModel)
              p.simpleBlock(blockFamily.WALL_SIGN!!.get() as WallSignBlock, signModel)
            }
            .item { b, p -> CustomSignItem(p.stacksTo(16), b, blockFamily.WALL_SIGN!!.get()) }
            .tag(ItemTags.SIGNS)
            .model { c, p ->
              p.withExistingParent(c.name, p.mcLoc("item/generated"))
                .texture("layer0", LibUtils.resourceLocation("item/${c.name}"))
            }
            .build()
        }
        .register()
    }
    // DONE: BLOCK, LOG, STRIPPED LOG, WOOD, STRIPPED WOOD
    // DONE: STAIRS, SLAB, FENCE, FENCE GATE, BUTTON, PRESSURE PLATE
    // DONE: STALK, STRIPPED STALK, LEAVES, DOOR, TRAPDOOR, SIGN
    // TODO: SAPLING, WINDOW, WINDOW PANE, BOAT, CHEST BOAT
    return blockFamily
  }
}


