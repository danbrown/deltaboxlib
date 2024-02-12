package com.dannbrown.databoxlib.registry

import com.dannbrown.databoxlib.DataboxLib
import com.dannbrown.databoxlib.content.block.GenericSaplingBlock
import com.dannbrown.databoxlib.content.block.GenericStandingSignBlock
import com.dannbrown.databoxlib.content.block.GenericWallSignBlock
import com.dannbrown.databoxlib.content.block.FlammableBlock
import com.dannbrown.databoxlib.content.block.FlammableLeavesBlock
import com.dannbrown.databoxlib.content.block.FlammablePillarBlock
import com.dannbrown.databoxlib.content.block.FlammableWallBlock
import com.dannbrown.databoxlib.content.item.GenericSignItem
import com.dannbrown.databoxlib.datagen.transformers.BlockItemFactory
import com.dannbrown.databoxlib.datagen.transformers.BlockLootPresets
import com.dannbrown.databoxlib.datagen.transformers.BlockTagPresets
import com.dannbrown.databoxlib.datagen.transformers.BlockstatePresets
import com.dannbrown.databoxlib.datagen.transformers.ItemModelPresets
import com.dannbrown.databoxlib.datagen.transformers.RecipePresets
import com.dannbrown.databoxlib.lib.LibUtils
import com.dannbrown.databoxlib.datagen.content.BlockFamily
import com.dannbrown.databoxlib.lib.LibTags
import com.tterrag.registrate.providers.RegistrateRecipeProvider
import com.tterrag.registrate.util.DataIngredient
import com.tterrag.registrate.util.entry.BlockEntry
import net.minecraft.core.BlockPos
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Items
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.ButtonBlock
import net.minecraft.world.level.block.DoorBlock
import net.minecraft.world.level.block.FenceBlock
import net.minecraft.world.level.block.FenceGateBlock
import net.minecraft.world.level.block.FlowerPotBlock
import net.minecraft.world.level.block.PressurePlateBlock
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.StandingSignBlock
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

class BlockFamilyGen(name: String, private val registrate: DataboxRegistrate = DataboxLib.REGISTRATE) {
  private val _name = name
  private var _sharedProps: (BlockBehaviour.Properties) -> BlockBehaviour.Properties = { p: BlockBehaviour.Properties -> p }
  private var _toolType: TagKey<Block>? = null
  private var _toolTier: TagKey<Block>? = null
  private var _color: MapColor = MapColor.COLOR_GRAY
  private var _accentColor: MapColor = MapColor.COLOR_GRAY
  private var _copyFrom: Supplier<Block> = Supplier { Blocks.STONE }
  private val _denyList = mutableListOf<BlockFamily.Type>()
  private val _blockFamily: BlockFamily = BlockFamily()
  fun sharedProps(props: (BlockBehaviour.Properties) -> BlockBehaviour.Properties = { p: BlockBehaviour.Properties -> p }): BlockFamilyGen {
    _sharedProps = { p -> props(p) }
    return this
  }

  fun toolAndTier(
    tool: TagKey<Block>? = null,
    tier: TagKey<Block>? = null,
    requiredForDrops: Boolean = true
  ): BlockFamilyGen {
    _toolTier = tier
    _toolType = tool
    if (requiredForDrops) {
      sharedProps { p -> p.requiresCorrectToolForDrops() }
    }
    return this
  }

  fun color(
    color: MapColor = MapColor.COLOR_GRAY,
    accentColor: MapColor = MapColor.COLOR_GRAY
  ): BlockFamilyGen {
    this._color = color
    this._accentColor = accentColor
    return this
  }

  fun copyFrom(block: Supplier<Block>): BlockFamilyGen {
    _copyFrom = block
    return this
  }

  fun denyList(vararg deny: BlockFamily.Type): BlockFamilyGen {
    this._denyList.addAll(deny)
    return this
  }

  fun getColor(): MapColor {
    return _color
  }

  fun getAccentColor(): MapColor {
    return _accentColor
  }

  fun getCopyFrom(): Supplier<Block> {
    return _copyFrom
  }
  fun getDenyList(): List<BlockFamily.Type> {
    return _denyList
  }

  fun getSharedProps(): (BlockBehaviour.Properties) -> BlockBehaviour.Properties {
    return _sharedProps
  }

  fun getToolTier(): TagKey<Block>? {
    return _toolTier
  }

  fun getToolType(): TagKey<Block>? {
    return _toolType
  }

  fun getName(): String {
    return _name
  }

  fun getBlockFamily(): BlockFamily {
    return _blockFamily
  }

  fun getRegistrate(): DataboxRegistrate {
    return registrate
  }


  /**
   * Allow for custom block family generation
   */
  fun custom(functionToExecute: (BlockFamilyGen) -> BlockFamily): BlockFamily {
    return functionToExecute(this)
  }

  /**
   * Returns a Long block family composed by Normals, Polished, Bricks, Cut, Chiseled variants (stairs, slabs, walls)
   */
  fun longBlockFamily(mainBlock: BlockEntry<out Block>? = null, isRotatedBlock: Boolean = false): BlockFamily {
    val MATERIAL_TAG = LibTags.modItemTag(registrate.modid, _name + "_blocks")

    if (mainBlock == null) {
      _blockFamily.setVariant(BlockFamily.Type.MAIN) {
        BlockGen<Block>(_name)
          .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .blockTags(listOf(*BlockTagPresets.caveReplaceableTags().first))
          .register(registrate)
      }
    }
    else {
      _blockFamily.setVariant(BlockFamily.Type.MAIN) { mainBlock }
    }

    if (!_denyList.contains(BlockFamily.Type.MAIN)) {
      if (!_denyList.contains(BlockFamily.Type.STAIRS)) {
        _blockFamily.setVariant(BlockFamily.Type.STAIRS) {
          BlockGen<StairBlock>(_name)
            .stairsBlock({ _blockFamily.MAIN!!.defaultState }, isRotatedBlock)
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.MAIN!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.stairsCraftingRecipe(c, p) {
                DataIngredient.items(
                  _blockFamily.MAIN!!.get()
                    .asItem()
                )
              }
            }
            .register(registrate)
        }
      }

      if (!_denyList.contains(BlockFamily.Type.SLAB)) {
        _blockFamily.setVariant(BlockFamily.Type.SLAB) {
          BlockGen<SlabBlock>(_name)
            .slabBlock(isRotatedBlock)
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.MAIN!!.get()
                    .asItem())
                },
                2
              )

              RecipePresets.slabCraftingRecipe(c, p) {
                DataIngredient.items(
                  _blockFamily.MAIN!!.get()
                    .asItem()
                )
              }
            }
            .register(registrate)
        }
      }

      if (!_denyList.contains(BlockFamily.Type.WALL)) {
        _blockFamily.setVariant(BlockFamily.Type.WALL) {
          BlockGen<WallBlock>(_name)
            .wallBlock(isRotatedBlock)
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.MAIN!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.wallCraftingRecipe(c, p) {
                DataIngredient.items(
                  _blockFamily.MAIN!!.get()
                    .asItem()
                )
              }
            }
            .register(registrate)
        }
      }
    }
    // start polished chain
    if (!_denyList.contains(BlockFamily.Type.POLISHED)) {
      _blockFamily.setVariant(BlockFamily.Type.POLISHED) {
        BlockGen<Block>("polished_$_name")
          .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .recipe { c, p ->
            RecipePresets.polishedCraftingRecipe(
              c,
              p,
              { DataIngredient.items(_blockFamily.MAIN!!.get()) },
              4
            )
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              { DataIngredient.items(_blockFamily.MAIN!!.get()) },
              1
            )
          }
          .register(registrate)
      }

      if (!_denyList.contains(BlockFamily.Type.POLISHED_STAIRS)) {
        _blockFamily.setVariant(BlockFamily.Type.POLISHED_STAIRS) {
          BlockGen<StairBlock>("polished_$_name")
            .stairsBlock({ _blockFamily.POLISHED!!.defaultState })
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.MAIN!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.POLISHED!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.stairsCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(_blockFamily.POLISHED!!.get()
                  .asItem())
              }
            }
            .register(registrate)
        }
      }

      if (!_denyList.contains(BlockFamily.Type.POLISHED_SLAB)) {
        _blockFamily.setVariant(BlockFamily.Type.POLISHED_SLAB) {
          BlockGen<SlabBlock>("polished_$_name")
            .slabBlock()
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c, p,
                {
                  DataIngredient.items(_blockFamily.MAIN!!.get()
                    .asItem())
                },
                2
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.POLISHED!!.get()
                    .asItem())
                },
                2
              )
              RecipePresets.slabRecycleRecipe(c, p) {
                _blockFamily.POLISHED!!.get()
                  .asItem()
              }
              RecipePresets.slabCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(_blockFamily.POLISHED!!.get()
                  .asItem())
              }
            }
            .register(registrate)
        }
      }

      if (!_denyList.contains(BlockFamily.Type.POLISHED_WALL)) {
        _blockFamily.setVariant(BlockFamily.Type.POLISHED_WALL) {
          BlockGen<WallBlock>("polished_$_name")
            .wallBlock()
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.MAIN!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.POLISHED!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.wallCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(_blockFamily.POLISHED!!.get()
                  .asItem())
              }
            }
            .register(registrate)
        }
      }
    }
    // start bricks chain
    if (!_denyList.contains(BlockFamily.Type.BRICKS)) {
      _blockFamily.setVariant(BlockFamily.Type.BRICKS) {
        BlockGen<Block>("${_name}_bricks")
          .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .recipe { c, p ->
            RecipePresets.polishedCraftingRecipe(
              c,
              p,
              { DataIngredient.items(_blockFamily.POLISHED!!.get()) },
              4
            )
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              { DataIngredient.items(_blockFamily.MAIN!!.get()) },
              1
            )
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              { DataIngredient.items(_blockFamily.POLISHED!!.get()) },
              1
            )
          }
          .register(registrate)
      }

      if (!_denyList.contains(BlockFamily.Type.BRICK_STAIRS)) {
        _blockFamily.setVariant(BlockFamily.Type.BRICK_STAIRS) {
          BlockGen<StairBlock>("${_name}_bricks")
            .stairsBlock({ _blockFamily.BRICKS!!.defaultState })
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.MAIN!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.BRICKS!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.POLISHED!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.stairsCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(_blockFamily.BRICKS!!.get()
                  .asItem())
              }
            }
            .register(registrate)
        }
      }

      if (!_denyList.contains(BlockFamily.Type.BRICK_SLAB)) {
        _blockFamily.setVariant(BlockFamily.Type.BRICK_SLAB) {
          BlockGen<SlabBlock>("${_name}_bricks")
            .slabBlock()
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c, p,
                {
                  DataIngredient.items(_blockFamily.MAIN!!.get()
                    .asItem())
                },
                2
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.POLISHED!!.get()
                    .asItem())
                },
                2
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.BRICKS!!.get()
                    .asItem())
                },
                2
              )
              RecipePresets.slabRecycleRecipe(c, p) {
                _blockFamily.BRICKS!!.get()
                  .asItem()
              }
              RecipePresets.slabCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(_blockFamily.BRICKS!!.get()
                  .asItem())
              }
            }
            .register(registrate)
        }
      }

      if (!_denyList.contains(BlockFamily.Type.BRICK_WALL)) {
        _blockFamily.setVariant(BlockFamily.Type.BRICK_WALL) {
          BlockGen<WallBlock>("${_name}_bricks")
            .wallBlock()
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.MAIN!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.POLISHED!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.BRICKS!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.wallCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(_blockFamily.BRICKS!!.get()
                  .asItem())
              }
            }
            .register(registrate)
        }
      }
    }
    // start chiseled chain
    if (!_denyList.contains(BlockFamily.Type.CHISELED)) {
      _blockFamily.setVariant(BlockFamily.Type.CHISELED) {
        BlockGen<Block>("chiseled_$_name")
          .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .recipe { c, p ->
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              {
                DataIngredient.items(_blockFamily.MAIN!!.get()
                  .asItem())
              },
              1
            )
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              {
                DataIngredient.items(_blockFamily.POLISHED!!.get()
                  .asItem())
              },
              1
            )
            RecipePresets.slabToChiseledRecipe(c, p) {
              DataIngredient.items(_blockFamily.SLAB!!.get()
                .asItem())
            }
          }
          .register(registrate)
      }
    }
    // PILLAR
    if (!_denyList.contains(BlockFamily.Type.PILLAR)) {
      _blockFamily.setVariant(BlockFamily.Type.PILLAR) {
        BlockGen<RotatedPillarBlock>("${_name}_pillar")
          .rotatedPillarBlock("${_name}_pillar_top", "${_name}_pillar_side")
          .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .recipe { c, p ->
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              {
                DataIngredient.items(_blockFamily.MAIN!!.get()
                  .asItem())
              },
              1
            )
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              {
                DataIngredient.items(_blockFamily.POLISHED!!.get()
                  .asItem())
              },
              1
            )
            RecipePresets.slabToChiseledRecipe(c, p) {
              DataIngredient.items(_blockFamily.MAIN!!.get()
                .asItem())
            }
          }
          .register(registrate)
      }
    }

    return _blockFamily
  }

  /**
   * Returns a Mineral block family composed by Normals and Polished variants (stairs, slabs, walls)
   */
  fun mineralFamily(mainBlock: BlockEntry<out Block>? = null, isRotatedBlock: Boolean = false): BlockFamily {
    val MATERIAL_TAG = LibTags.modItemTag(registrate.modid, _name + "_blocks")

    if (mainBlock == null) {
      _blockFamily.setVariant(BlockFamily.Type.MAIN) {
        BlockGen<Block>(_name)
          .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .blockTags(listOf(*BlockTagPresets.caveReplaceableTags().first))
          .register(registrate)
      }
    }
    else {
      _blockFamily.setVariant(BlockFamily.Type.MAIN) { mainBlock }
    }

    if (!_denyList.contains(BlockFamily.Type.STAIRS)) {
      _blockFamily.setVariant(BlockFamily.Type.STAIRS) {
        BlockGen<StairBlock>(_name)
          .stairsBlock({ _blockFamily.MAIN!!.defaultState }, isRotatedBlock)
          .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .recipe { c, p ->
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              {
                DataIngredient.items(_blockFamily.MAIN!!.get()
                  .asItem())
              },
              1
            )
            RecipePresets.stairsCraftingRecipe(c, p) {
              DataIngredient.items(
                _blockFamily.MAIN!!.get()
                  .asItem()
              )
            }
          }
          .register(registrate)
      }
    }

    if (!_denyList.contains(BlockFamily.Type.SLAB)) {
      _blockFamily.setVariant(BlockFamily.Type.SLAB) {
        BlockGen<SlabBlock>(_name)
          .slabBlock(isRotatedBlock)
          .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .recipe { c, p ->
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              {
                DataIngredient.items(_blockFamily.MAIN!!.get()
                  .asItem())
              },
              2
            )
            RecipePresets.slabRecycleRecipe(c, p) {
              _blockFamily.MAIN!!.get()
                .asItem()
            }
            RecipePresets.slabCraftingRecipe(c, p) {
              DataIngredient.items(
                _blockFamily.MAIN!!.get()
                  .asItem()
              )
            }
          }
          .register(registrate)
      }
    }

    if (!_denyList.contains(BlockFamily.Type.WALL)) {
      _blockFamily.setVariant(BlockFamily.Type.WALL) {
        BlockGen<WallBlock>(_name)
          .wallBlock(isRotatedBlock)
          .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .recipe { c, p ->
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              {
                DataIngredient.items(_blockFamily.MAIN!!.get()
                  .asItem())
              },
              1
            )
            RecipePresets.wallCraftingRecipe(c, p) {
              DataIngredient.items(
                _blockFamily.MAIN!!.get()
                  .asItem()
              )
            }
          }
          .register(registrate)
      }
    }
    // start polished chain
    if (!_denyList.contains(BlockFamily.Type.POLISHED)) {
      _blockFamily.setVariant(BlockFamily.Type.POLISHED) {
        BlockGen<Block>("polished_$_name")
          .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .recipe { c, p ->
            RecipePresets.polishedCraftingRecipe(
              c,
              p,
              { DataIngredient.items(_blockFamily.MAIN!!.get()) },
              4
            )
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              { DataIngredient.items(_blockFamily.MAIN!!.get()) },
              1
            )
          }
          .register(registrate)
      }

      if (!_denyList.contains(BlockFamily.Type.POLISHED_STAIRS)) {
        _blockFamily.setVariant(BlockFamily.Type.POLISHED_STAIRS) {
          BlockGen<StairBlock>("polished_$_name")
            .stairsBlock({ _blockFamily.POLISHED!!.defaultState })
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.MAIN!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.POLISHED!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.stairsCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(_blockFamily.POLISHED!!.get()
                  .asItem())
              }
            }
            .register(registrate)
        }
      }

      if (!_denyList.contains(BlockFamily.Type.POLISHED_SLAB)) {
        _blockFamily.setVariant(BlockFamily.Type.POLISHED_SLAB) {
          BlockGen<SlabBlock>("polished_$_name")
            .slabBlock()
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c, p,
                {
                  DataIngredient.items(_blockFamily.MAIN!!.get()
                    .asItem())
                },
                2
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.POLISHED!!.get()
                    .asItem())
                },
                2
              )
              RecipePresets.slabRecycleRecipe(c, p) {
                _blockFamily.POLISHED!!.get()
                  .asItem()
              }
              RecipePresets.slabCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(_blockFamily.POLISHED!!.get()
                  .asItem())
              }
            }
            .register(registrate)
        }
      }

      if (!_denyList.contains(BlockFamily.Type.POLISHED_WALL)) {
        _blockFamily.setVariant(BlockFamily.Type.POLISHED_WALL) {
          BlockGen<WallBlock>("polished_$_name")
            .wallBlock()
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.MAIN!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.POLISHED!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.wallCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(_blockFamily.POLISHED!!.get()
                  .asItem())
              }
            }
            .register(registrate)
        }
      }
    }
    // start bricks chain
    if (!_denyList.contains(BlockFamily.Type.BRICKS)) {
      _blockFamily.setVariant(BlockFamily.Type.BRICKS) {
        BlockGen<Block>("${_name}_bricks")
          .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .recipe { c, p ->
            RecipePresets.polishedCraftingRecipe(
              c,
              p,
              { DataIngredient.items(_blockFamily.POLISHED!!.get()) },
              4
            )
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              { DataIngredient.items(_blockFamily.MAIN!!.get()) },
              1
            )
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              { DataIngredient.items(_blockFamily.POLISHED!!.get()) },
              1
            )
          }
          .register(registrate)
      }

      if (!_denyList.contains(BlockFamily.Type.BRICK_STAIRS)) {
        _blockFamily.setVariant(BlockFamily.Type.BRICK_STAIRS) {
          BlockGen<StairBlock>("${_name}_bricks")
            .stairsBlock({ _blockFamily.BRICKS!!.defaultState })
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.MAIN!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.BRICKS!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.POLISHED!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.stairsCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(_blockFamily.BRICKS!!.get()
                  .asItem())
              }
            }
            .register(registrate)
        }
      }

      if (!_denyList.contains(BlockFamily.Type.BRICK_SLAB)) {
        _blockFamily.setVariant(BlockFamily.Type.BRICK_SLAB) {
          BlockGen<SlabBlock>("${_name}_bricks")
            .slabBlock()
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c, p,
                {
                  DataIngredient.items(_blockFamily.MAIN!!.get()
                    .asItem())
                },
                2
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.POLISHED!!.get()
                    .asItem())
                },
                2
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.BRICKS!!.get()
                    .asItem())
                },
                2
              )
              RecipePresets.slabRecycleRecipe(c, p) {
                _blockFamily.BRICKS!!.get()
                  .asItem()
              }
              RecipePresets.slabCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(_blockFamily.BRICKS!!.get()
                  .asItem())
              }
            }
            .register(registrate)
        }
      }

      if (!_denyList.contains(BlockFamily.Type.BRICK_WALL)) {
        _blockFamily.setVariant(BlockFamily.Type.BRICK_WALL) {
          BlockGen<WallBlock>("${_name}_bricks")
            .wallBlock()
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.MAIN!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.POLISHED!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.BRICKS!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.wallCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(_blockFamily.BRICKS!!.get()
                  .asItem())
              }
            }
            .register(registrate)
        }
      }
    }

    return _blockFamily
  }

  /**
   * Returns a SandStone block family composed by
   */
  fun sandstoneFamily(baseBlock: BlockEntry<out Block>): BlockFamily {
    val MATERIAL_TAG = LibTags.modItemTag(registrate.modid, _name + "_blocks")
    _blockFamily.setVariant(BlockFamily.Type.MAIN) { baseBlock }

    if (!_denyList.contains(BlockFamily.Type.SANDSTONE)) {
      _blockFamily.setVariant(BlockFamily.Type.SANDSTONE) {
        BlockGen<Block>(_name + "_sandstone")
          .bottomTopBlock()
          .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .recipe { c, p ->
            RecipePresets.polishedCraftingRecipe(
              c,
              p,
              {
                DataIngredient.items(_blockFamily.MAIN!!.get()
                  .asItem())
              })
          }
          .register(registrate)
      }

      if (!_denyList.contains(BlockFamily.Type.SANDSTONE_STAIRS)) {
        _blockFamily.setVariant(BlockFamily.Type.SANDSTONE_STAIRS) {
          BlockGen<StairBlock>(_name + "_sandstone")
            .stairsBlock({ _blockFamily.SANDSTONE!!.defaultState }, true)
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.SANDSTONE!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.stairsCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(_blockFamily.SANDSTONE!!.get()
                  .asItem())
              }
            }
            .register(registrate)
        }
      }

      if (!_denyList.contains(BlockFamily.Type.SANDSTONE_SLAB)) {
        _blockFamily.setVariant(BlockFamily.Type.SANDSTONE_SLAB) {
          BlockGen<SlabBlock>(_name + "_sandstone")
            .slabBlock(true)
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.SANDSTONE!!.get()
                    .asItem())
                },
                2
              )
              RecipePresets.slabCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(_blockFamily.SANDSTONE!!.get()
                  .asItem())
              }
            }
            .register(registrate)
        }
      }

      if (!_denyList.contains(BlockFamily.Type.SANDSTONE_WALL)) {
        _blockFamily.setVariant(BlockFamily.Type.SANDSTONE_WALL) {
          BlockGen<WallBlock>(_name + "_sandstone")
            .wallBlock()
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.SANDSTONE!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.wallCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(_blockFamily.SANDSTONE!!.get()
                  .asItem())
              }
            }
            .register(registrate)
        }
      }
      // CHISELED
      if (!_denyList.contains(BlockFamily.Type.CHISELED)) {
        _blockFamily.setVariant(BlockFamily.Type.CHISELED) {
          BlockGen<Block>("chiseled_$_name" + "_sandstone")
            .bottomTopBlock(_name + "_sandstone_bottom", _name + "_sandstone_top")
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.SANDSTONE!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.slabToChiseledRecipe(c, p) {
                DataIngredient.items(
                  _blockFamily.SANDSTONE_SLAB!!.get()
                    .asItem()
                )
              }
            }
            .register(registrate)
        }
      }
      // CUT
      if (!_denyList.contains(BlockFamily.Type.CUT)) {
        _blockFamily.setVariant(BlockFamily.Type.CUT) {
          BlockGen<Block>("cut_$_name" + "_sandstone")
            .bottomTopBlock(_name + "_sandstone_bottom", _name + "_sandstone_top")
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.SANDSTONE!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.polishedCraftingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.SANDSTONE!!.get()
                    .asItem())
                },
                4
              )
            }
            .register(registrate)
        }
      }
      // SMOOTH
      if (!_denyList.contains(BlockFamily.Type.SMOOTH)) {
        _blockFamily.setVariant(BlockFamily.Type.SMOOTH) {
          BlockGen<Block>("smooth_$_name" + "_sandstone")
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .textureName(_name + "_sandstone_top")
            .transform { t ->
              t.blockstate { c, p ->
                p.simpleBlockWithItem(
                  c.get(),
                  p.models()
                    .withExistingParent(
                      c.name,
                      p.mcLoc("block/cube_all")
                    )
                    .texture("all", p.modLoc("block/" + _name + "_sandstone_top"))
                )
              }
            }
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              p.smelting(
                DataIngredient.items(_blockFamily.SANDSTONE!!.get()
                  .asItem()),
                RecipeCategory.BUILDING_BLOCKS,
                { c.get() },
                0.1f,
                200
              )
            }
            .register(registrate)
        }

        if (!_denyList.contains(BlockFamily.Type.SMOOTH_STAIRS)) {
          _blockFamily.setVariant(BlockFamily.Type.SMOOTH_STAIRS) {
            BlockGen<StairBlock>("smooth_$_name" + "_sandstone")
              .stairsBlock({ _blockFamily.SMOOTH!!.defaultState })
              .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
              .textureName(_name + "_sandstone_top")
              .itemTags(listOf(MATERIAL_TAG))
              .recipe { c, p ->
                RecipePresets.simpleStonecuttingRecipe(
                  c,
                  p,
                  {
                    DataIngredient.items(_blockFamily.SMOOTH!!.get()
                      .asItem())
                  },
                  1
                )
                RecipePresets.stairsCraftingRecipe(
                  c,
                  p
                ) {
                  DataIngredient.items(_blockFamily.SMOOTH!!.get()
                    .asItem())
                }
              }
              .register(registrate)
          }
        }

        if (!_denyList.contains(BlockFamily.Type.SMOOTH_SLAB)) {
          _blockFamily.setVariant(BlockFamily.Type.SMOOTH_SLAB) {
            BlockGen<SlabBlock>("smooth_$_name" + "_sandstone")
              .slabBlock()
              .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
              .textureName(_name + "_sandstone_top")
              .itemTags(listOf(MATERIAL_TAG))
              .recipe { c, p ->
                RecipePresets.simpleStonecuttingRecipe(
                  c,
                  p,
                  {
                    DataIngredient.items(_blockFamily.SMOOTH!!.get()
                      .asItem())
                  },
                  2
                )
                RecipePresets.slabRecycleRecipe(c, p) {
                  _blockFamily.SMOOTH!!.get()
                    .asItem()
                }
                RecipePresets.slabCraftingRecipe(
                  c,
                  p
                ) {
                  DataIngredient.items(_blockFamily.SMOOTH!!.get()
                    .asItem())
                }
              }
              .register(registrate)
          }
        }

        if (!_denyList.contains(BlockFamily.Type.SMOOTH_WALL)) {
          _blockFamily.setVariant(BlockFamily.Type.SMOOTH_WALL) {
            BlockGen<WallBlock>("smooth_$_name" + "_sandstone")
              .wallBlock()
              .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
              .textureName(_name + "_sandstone_top")
              .itemTags(listOf(MATERIAL_TAG))
              .recipe { c, p ->
                RecipePresets.simpleStonecuttingRecipe(
                  c,
                  p,
                  {
                    DataIngredient.items(_blockFamily.SMOOTH!!.get()
                      .asItem())
                  },
                  1
                )
                RecipePresets.wallCraftingRecipe(
                  c,
                  p
                ) {
                  DataIngredient.items(_blockFamily.SMOOTH!!.get()
                    .asItem())
                }
              }
              .register(registrate)
          }
        }
      }
    }

    return _blockFamily
  }

  fun stalkWoodFamily(
    woodType: WoodType,
    grower: AbstractTreeGrower,
    placeOn: ((blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos) -> Boolean)? = null
  ): BlockFamily {
    val LOG_TAG_BLOCK = LibTags.modBlockTag(registrate.modid,_name + "_log_blocks")
    val LOG_TAG_ITEM = LibTags.modItemTag(registrate.modid, _name + "_log_blocks")
    val FORGE_LEAVES_TAG_BLOCK = LibTags.forgeBlockTag("leaves")
    val FORGE_LEAVES_TAG_ITEM = LibTags.forgeItemTag("leaves")
    val FORGE_STRIPPED_LOGS_TAG_BLOCK = LibTags.forgeBlockTag("stripped_logs")
    val FORGE_STRIPPED_LOGS_TAG_ITEM = LibTags.forgeItemTag("stripped_logs")
    // Stalks
    _blockFamily.setVariant(BlockFamily.Type.STALK) {
      BlockGen<FlammableWallBlock>(_name + "_stalk")
        .wallBlock(true, false)
        .blockFactory { p -> FlammableWallBlock(p) }
        .copyFrom({ Blocks.OAK_LOG })
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .blockTags(listOf(LOG_TAG_BLOCK, BlockTags.LOGS))
        .itemTags(listOf(LOG_TAG_ITEM, ItemTags.LOGS))
        .transform { b ->
          b
            .loot(BlockLootPresets.dropItselfLoot())
            .item(BlockItemFactory.fuelBlockItem(75))
            .lang("${
              _name.replace("_", " ")
                .replaceFirstChar { it.uppercase() }
            } Stalk")
            .model(ItemModelPresets.bottomTopWallItem(_name + "_stalk"))
            .tag(LOG_TAG_ITEM)
            .build()
        }
        .noItem()
        .register(registrate)
    }

    _blockFamily.setVariant(BlockFamily.Type.STRIPPED_STALK) {
      BlockGen<FlammableWallBlock>("stripped_$_name" + "_stalk")
        .wallBlock(true, false)
        .blockFactory { p -> FlammableWallBlock(p) }
        .copyFrom({ Blocks.STRIPPED_OAK_LOG })
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .blockTags(listOf(LOG_TAG_BLOCK, BlockTags.LOGS))
        .transform { b ->
          b
            .loot(BlockLootPresets.dropItselfLoot())
            .item(BlockItemFactory.fuelBlockItem(75))
            .lang("Stripped ${
              _name.replace("_", " ")
                .replaceFirstChar { it.uppercase() }
            } Stalk")
            .model(ItemModelPresets.bottomTopWallItem("stripped_$_name" + "_stalk"))
            .tag(LOG_TAG_ITEM)
            .build()
        }
        .register(registrate)
    }
    // Logs
    _blockFamily.setVariant(BlockFamily.Type.LOG) {
      BlockGen<FlammablePillarBlock>(_name + "_logs")
        .rotatedPillarBlock(_name + "_logs_top", _name + "_logs")
        .blockFactory { p -> FlammablePillarBlock(p) }
        .copyFrom({ Blocks.OAK_LOG })
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
//        .fromFamily(Blocks.OAK_LOG, sharedProps, color, toolType, toolTier, false)
        .blockTags(listOf(BlockTags.LOGS, LOG_TAG_BLOCK, BlockTags.LOGS_THAT_BURN))
        .itemTags(listOf(ItemTags.LOGS, LOG_TAG_ITEM, ItemTags.LOGS_THAT_BURN))
        .recipe { c, p ->
          RecipePresets.polishedCraftingRecipe(c, p, { DataIngredient.items(_blockFamily.STALK!!.get()) }, 1)
        }
        .register(registrate)
    }

    _blockFamily.setVariant(BlockFamily.Type.WOOD) {
      BlockGen<FlammablePillarBlock>(_name + "_wood")
        .rotatedPillarBlock(_name + "_logs", _name + "_logs")
        .blockFactory { p -> FlammablePillarBlock(p) }
        .copyFrom({ Blocks.OAK_WOOD })
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        //        .fromFamily(Blocks.OAK_WOOD, sharedProps, accentColor, toolType, toolTier, false)
        .blockTags(listOf(BlockTags.LOGS, LOG_TAG_BLOCK, BlockTags.LOGS_THAT_BURN))
        .itemTags(listOf(ItemTags.LOGS, LOG_TAG_ITEM, ItemTags.LOGS_THAT_BURN))
        .recipe { c, p ->
          RecipePresets.polishedCraftingRecipe(c, p, { DataIngredient.items(_blockFamily.LOG!!.get()) }, 3)
        }
        .register(registrate)
    }
    // Stripped Logs
    _blockFamily.setVariant(BlockFamily.Type.STRIPPED_LOG) {
      BlockGen<FlammablePillarBlock>("stripped_$_name" + "_logs")
        .rotatedPillarBlock("stripped_$_name" + "_logs_top", "stripped_$_name" + "_logs")
        .blockFactory { p -> FlammablePillarBlock(p) }
        .copyFrom({ Blocks.STRIPPED_OAK_LOG })
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
//        .fromFamily(Blocks.STRIPPED_OAK_LOG, sharedProps, color, toolType, toolTier, false)
        .blockTags(listOf(BlockTags.LOGS, LOG_TAG_BLOCK, FORGE_STRIPPED_LOGS_TAG_BLOCK, BlockTags.LOGS_THAT_BURN))
        .itemTags(listOf(ItemTags.LOGS, LOG_TAG_ITEM, FORGE_STRIPPED_LOGS_TAG_ITEM, ItemTags.LOGS_THAT_BURN))
        .recipe { c, p ->
          RecipePresets.polishedCraftingRecipe(c, p, { DataIngredient.items(_blockFamily.STRIPPED_STALK!!.get()) }, 1)
        }
        .register(registrate)
    }

    _blockFamily.setVariant(BlockFamily.Type.STRIPPED_WOOD) {
      BlockGen<FlammablePillarBlock>("stripped_$_name" + "_wood")
        .rotatedPillarBlock("stripped_$_name" + "_logs", "stripped_$_name" + "_logs")
        .blockFactory { p -> FlammablePillarBlock(p) }
        .copyFrom({ Blocks.STRIPPED_OAK_WOOD })
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
//        .fromFamily(Blocks.STRIPPED_OAK_WOOD, sharedProps, accentColor, toolType, toolTier, false)
        .blockTags(listOf(BlockTags.LOGS, LOG_TAG_BLOCK, FORGE_STRIPPED_LOGS_TAG_BLOCK, BlockTags.LOGS_THAT_BURN))
        .itemTags(listOf(ItemTags.LOGS, LOG_TAG_ITEM, FORGE_STRIPPED_LOGS_TAG_ITEM, ItemTags.LOGS_THAT_BURN))
        .recipe { c, p ->
          RecipePresets.polishedCraftingRecipe(c, p, { DataIngredient.items(_blockFamily.STRIPPED_LOG!!.get()) }, 3)
        }
        .register(registrate)
    }

    _blockFamily.setVariant(BlockFamily.Type.SAPLING) {
      BlockGen<GenericSaplingBlock>("$_name" + "_sapling")
        .blockFactory { p -> GenericSaplingBlock(grower, p, placeOn) }
        .blockTags(listOf(BlockTags.SAPLINGS))
        .itemTags(listOf(ItemTags.SAPLINGS))
        .copyFrom({ Blocks.OAK_SAPLING })
        .properties { p ->
          p.mapColor(_accentColor)
            .sound(SoundType.GRASS)
            .strength(0.0f)
            .randomTicks()
            .noCollission()
            .noOcclusion()
        }
        .transform { t ->
          t
            .blockstate(BlockstatePresets.simpleCrossBlock(_name + "_sapling"))
            .item()
            .model(ItemModelPresets.simpleLayerItem(_name + "_sapling"))
            .build()
        }
        .register(registrate)
    }

    _blockFamily.setVariant(BlockFamily.Type.POTTED_SAPLING) {
      BlockGen<FlowerPotBlock>("potted_$_name" + "_sapling")
        .blockFactory { p ->
          FlowerPotBlock(
            { Blocks.FLOWER_POT as FlowerPotBlock },
            { _blockFamily.SAPLING!!.get() },
            p
          )
        }
        .copyFrom({ Blocks.POTTED_POPPY })
        .noItem()
        .properties { p ->
          p.mapColor(_color)
            .noOcclusion()
        }
        .transform { t ->
          t
            .blockstate(BlockstatePresets.pottedPlantBlock(_name + "_sapling"))
            .loot(BlockLootPresets.pottedPlantLoot { _blockFamily.SAPLING!!.get() })
        }
        .register(registrate)
    }

    _blockFamily.setVariant(BlockFamily.Type.LEAVES) {
      BlockGen<FlammableLeavesBlock>(_name + "_leaves")
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
            .blockstate(BlockstatePresets.simpleTransparentBlock(_name + "_leaves"))
            .loot(BlockLootPresets.leavesLoot { _blockFamily.SAPLING!!.get() })
        }
        .register(registrate)
    }
    // Main Block
    _blockFamily.setVariant(BlockFamily.Type.MAIN) {
      BlockGen<FlammableBlock>(_name + "_planks")
        .blockFactory { p -> FlammableBlock(p, 20, 5) }
//        .fromFamily(Blocks.OAK_PLANKS, sharedProps, accentColor, toolType, toolTier, false)
        .copyFrom({ Blocks.OAK_PLANKS })
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .blockTags(listOf(BlockTags.PLANKS))
        .itemTags(listOf(ItemTags.PLANKS))
        .recipe { c, p ->
          RecipePresets.directShapelessRecipe(c, p, { DataIngredient.items(_blockFamily.STALK!!.get()) }, 1)
          RecipePresets.directShapelessRecipe(c, p, { DataIngredient.items(_blockFamily.STRIPPED_STALK!!.get()) }, 1)
          RecipePresets.directShapelessRecipe(c, p, { DataIngredient.items(_blockFamily.LOG!!.get()) }, 4)
          RecipePresets.directShapelessRecipe(c, p, { DataIngredient.items(_blockFamily.STRIPPED_LOG!!.get()) }, 4)
          RecipePresets.directShapelessRecipe(c, p, { DataIngredient.items(_blockFamily.WOOD!!.get()) }, 4)
          RecipePresets.directShapelessRecipe(c, p, { DataIngredient.items(_blockFamily.STRIPPED_WOOD!!.get()) }, 4)
          // make the boat as oak, as I am sleep-deprived and bored enough to not do it
          ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, Items.OAK_BOAT, 1)
            .define('X', c.get())
            .pattern("X X")
            .pattern("XXX")
            .unlockedBy("has_" + p.safeName(c.get()), RegistrateRecipeProvider.has(c.get()))
            .save(p, LibUtils.resourceLocation("oak_boat_from_" + p.safeName(c.get())))
        }
        .register(registrate)
    }
    // Stairs
    _blockFamily.setVariant(BlockFamily.Type.STAIRS) {
      BlockGen<StairBlock>(_name)
        .textureName(_name + "_planks")
//        .fromFamily(Blocks.OAK_STAIRS, sharedProps, accentColor, toolType, toolTier, false)
        .copyFrom({ Blocks.OAK_STAIRS })
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .stairsBlock({ _blockFamily.MAIN!!.defaultState }, false, true)
        .blockTags(listOf(BlockTags.STAIRS, BlockTags.WOODEN_STAIRS))
        .itemTags(listOf(ItemTags.STAIRS, ItemTags.WOODEN_STAIRS))
        .recipe { c, p ->
          RecipePresets.stairsCraftingRecipe(c, p) {
            DataIngredient.items(_blockFamily.MAIN!!.get()
              .asItem())
          }
        }
        .register(registrate)
    }
    // Slab
    _blockFamily.setVariant(BlockFamily.Type.SLAB) {
      BlockGen<SlabBlock>(_name)
        .textureName(_name + "_planks")
        .slabBlock(false, true)
//        .fromFamily(Blocks.OAK_SLAB, sharedProps, accentColor, toolType, toolTier, false)
        .copyFrom({ Blocks.OAK_SLAB })
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .recipe { c, p ->
          RecipePresets.slabRecycleRecipe(c, p) {
            _blockFamily.MAIN!!.get()
              .asItem()
          }
          RecipePresets.slabCraftingRecipe(c, p) {
            DataIngredient.items(_blockFamily.MAIN!!.get()
              .asItem())
          }
        }
        .register(registrate)
    }
    // Fence
    _blockFamily.setVariant(BlockFamily.Type.FENCE) {
      BlockGen<FenceBlock>(_name)
        .textureName(_name + "_planks")
        .fenceBlock(true)
        .copyFrom({ Blocks.OAK_FENCE })
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .blockTags(listOf(BlockTags.FENCES, BlockTags.WOODEN_FENCES))
        .itemTags(listOf(ItemTags.FENCES, ItemTags.WOODEN_FENCES))
        .recipe { c, p ->
          RecipePresets.fenceCraftingRecipe(c, p) {
            DataIngredient.items(_blockFamily.MAIN!!.get()
              .asItem())
          }
        }
        .register(registrate)
    }
    // Fence Gate
    _blockFamily.setVariant(BlockFamily.Type.FENCE_GATE) {
      BlockGen<FenceGateBlock>(_name)
        .textureName(_name + "_planks")
        .fenceGateBlock(woodType)
        .copyFrom({ Blocks.OAK_FENCE_GATE })
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .blockTags(listOf(BlockTags.FENCE_GATES))
        .itemTags(listOf(ItemTags.FENCE_GATES))
        .recipe { c, p ->
          RecipePresets.fenceGateCraftingRecipe(c, p) {
            DataIngredient.items(_blockFamily.MAIN!!.get()
              .asItem())
          }
        }
        .register(registrate)
    }
    // Pressure Plate
    _blockFamily.setVariant(BlockFamily.Type.PRESSURE_PLATE) {
      BlockGen<PressurePlateBlock>(_name)
        .textureName(_name + "_planks")
        .pressurePlateBlock(BlockSetType.OAK)
        .copyFrom({ Blocks.OAK_PRESSURE_PLATE })
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
//        .fromFamily(Blocks.OAK_PRESSURE_PLATE, sharedProps, accentColor, toolType, toolTier, false)
        .blockTags(listOf(BlockTags.WOODEN_PRESSURE_PLATES))
        .itemTags(listOf(ItemTags.WOODEN_PRESSURE_PLATES))
        .recipe { c, p ->
          RecipePresets.pressurePlateCraftingRecipe(c, p) {
            DataIngredient.items(_blockFamily.MAIN!!.get()
              .asItem())
          }
        }
        .register(registrate)
    }
    // Button
    _blockFamily.setVariant(BlockFamily.Type.BUTTON) {
      BlockGen<ButtonBlock>(_name)
        .textureName(_name + "_planks")
        .buttonBlock(BlockSetType.OAK, true)
        .copyFrom({ Blocks.OAK_BUTTON })
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
//        .fromFamily(Blocks.OAK_BUTTON, sharedProps, accentColor, toolType, toolTier, false)
        .recipe { c, p ->
          RecipePresets.directShapelessRecipe(c,
            p,
            {
              DataIngredient.items(_blockFamily.MAIN!!.get()
                .asItem())
            },
            1)
        }
        .register(registrate)
    }
    // Door
    _blockFamily.setVariant(BlockFamily.Type.DOOR) {
      BlockGen<DoorBlock>(_name)
        .woodenDoorBlock(BlockSetType.OAK)
        .copyFrom({ Blocks.OAK_DOOR })
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .color(_accentColor)
        .recipe { c, p ->
          RecipePresets.doorCraftingRecipe(c, p) {
            DataIngredient.items(_blockFamily.MAIN!!.get()
              .asItem())
          }
        }
        .transform { b ->
          b
            .loot(BlockLootPresets.doorLoot())
        }
        .register(registrate)
    }
    // Trapdoor
//    blockFamily.setVariant(BlockFamily.Type.TRAPDOOR) {
//      BlockGen<TrapDoorBlock>(name)
//        .woodenTrapdoorBlock({ DataIngredient.items(blockFamily.MAIN!!.get()) }, BlockSetType.OAK)
//        .copyFrom({ Blocks.OAK_TRAPDOOR })
//        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
//        .color(accentColor)
//        .properties { p ->
//          p.sound(SoundType.WOOD)
//            .noOcclusion()
//        }
//        .register(registrate)
//    }
    // Sign
    _blockFamily.setVariant(BlockFamily.Type.WALL_SIGN) {
      BlockGen<GenericWallSignBlock>(_name + "_wall_sign")
        .noItem()
        .copyFrom({ Blocks.OAK_WALL_SIGN })
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .blockFactory { p -> GenericWallSignBlock(p, woodType) { _blockFamily.SIGN!!.get() } }
        .color(_accentColor)
        .properties { p ->
          p.strength(1.0F)
            .sound(SoundType.WOOD)
            .noOcclusion()
        }
        .blockTags(listOf(BlockTags.WALL_SIGNS, BlockTags.SIGNS))
        .transform { t ->
          t
            .blockstate(BlockstatePresets.noBlockState())
            .loot(BlockLootPresets.dropOtherLoot { _blockFamily.SIGN!!.get() })
        }
        .register(registrate)
    }

    _blockFamily.setVariant(BlockFamily.Type.SIGN) {
      BlockGen<GenericStandingSignBlock>(_name + "_sign")
        .blockFactory { p -> GenericStandingSignBlock(p, woodType) }
        .copyFrom({ Blocks.OAK_SIGN })
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .color(_accentColor)
        .noItem()
        .recipe { c, p ->
          RecipePresets.signCraftingRecipe(c, p) {
            DataIngredient.items(_blockFamily.MAIN!!.get()
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
                .sign(c.name, LibUtils.resourceLocation("block/${_name + "_planks"}"))
              p.simpleBlock(c.get() as StandingSignBlock, signModel)
              p.simpleBlock(_blockFamily.WALL_SIGN!!.get() as WallSignBlock, signModel)
            }
            .item { b, p -> GenericSignItem(p.stacksTo(16), b, _blockFamily.WALL_SIGN!!.get()) }
            .tag(ItemTags.SIGNS)
            .model { c, p ->
              p.withExistingParent(c.name, p.mcLoc("item/generated"))
                .texture("layer0", LibUtils.resourceLocation("item/${c.name}"))
            }
            .build()
        }
        .register(registrate)
    }
    // DONE: BLOCK, LOG, STRIPPED LOG, WOOD, STRIPPED WOOD
    // DONE: STAIRS, SLAB, FENCE, FENCE GATE, BUTTON, PRESSURE PLATE
    // DONE: STALK, STRIPPED STALK, LEAVES, DOOR, TRAPDOOR, SIGN
    // TODO: SAPLING, WINDOW, WINDOW PANE, BOAT, CHEST BOAT
    return _blockFamily
  }


}


