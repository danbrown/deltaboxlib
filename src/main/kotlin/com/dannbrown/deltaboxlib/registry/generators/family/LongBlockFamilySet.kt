package com.dannbrown.deltaboxlib.registry.generators.family

import com.dannbrown.deltaboxlib.lib.LibTags
import com.dannbrown.deltaboxlib.registry.generators.BlockFamily
import com.dannbrown.deltaboxlib.registry.generators.BlockGenerator
import com.dannbrown.deltaboxlib.registry.transformers.BlockTagPresets
import com.dannbrown.deltaboxlib.registry.transformers.RecipePresets
import com.tterrag.registrate.util.DataIngredient
import com.tterrag.registrate.util.entry.BlockEntry
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.WallBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import java.util.function.Supplier

/**
 * Returns a Long block family composed by Normals, Polished, Bricks, Cut, Chiseled variants (stairs, slabs, walls)
 */
class LongBlockFamilySet(
  private val generator: BlockGenerator,
  private val _name: String,
  private val _sharedProps: (BlockBehaviour.Properties) -> BlockBehaviour.Properties = { p: BlockBehaviour.Properties -> p },
  private val _toolType: TagKey<Block>? = null,
  private val _toolTier: TagKey<Block>? = null,
  private val _color: MapColor? = null,
  private val _accentColor: MapColor? = null,
  private val _copyFrom: Supplier<Block> = Supplier { Blocks.STONE },
  private val _denyList: List<BlockFamily.Type> = mutableListOf(),
  private var mainBlock: Supplier<out Block>? = null,
  isRotatedBlock: Boolean = false
): AbstractBlockFamilySet() {
  init {
    val MATERIAL_TAG = LibTags.modItemTag(generator.registrate.modid, _name + "_blocks")

    if (mainBlock == null) {
      _blockFamily.setVariant(BlockFamily.Type.MAIN) {
        generator.create<Block>(_name)
          .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .blockTags(listOf(*BlockTagPresets.caveReplaceableTags().first))
          .register()
      }
      mainBlock = _blockFamily.blocks[BlockFamily.Type.MAIN]!!
    }

    if (!_denyList.contains(BlockFamily.Type.MAIN)) {
      if (!_denyList.contains(BlockFamily.Type.STAIRS)) {
        _blockFamily.setVariant(BlockFamily.Type.STAIRS) {
          generator.create<StairBlock>(_name)
            .stairsBlock({ mainBlock!!.get().defaultBlockState() }, isRotatedBlock)
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(mainBlock!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.stairsCraftingRecipe(c, p) {
                DataIngredient.items(
                  mainBlock!!.get()
                    .asItem()
                )
              }
            }
            .register()
        }
      }

      if (!_denyList.contains(BlockFamily.Type.SLAB)) {
        _blockFamily.setVariant(BlockFamily.Type.SLAB) {
          generator.create<SlabBlock>(_name)
            .slabBlock(isRotatedBlock)
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(mainBlock!!.get()
                    .asItem())
                },
                2
              )

              RecipePresets.slabCraftingRecipe(c, p) {
                DataIngredient.items(
                  mainBlock!!.get()
                    .asItem()
                )
              }
            }
            .register()
        }
      }

      if (!_denyList.contains(BlockFamily.Type.WALL)) {
        _blockFamily.setVariant(BlockFamily.Type.WALL) {
          generator.create<WallBlock>(_name)
            .wallBlock(isRotatedBlock)
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(mainBlock!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.wallCraftingRecipe(c, p) {
                DataIngredient.items(
                  mainBlock!!.get()
                    .asItem()
                )
              }
            }
            .register()
        }
      }
    }
    // start polished chain
    if (!_denyList.contains(BlockFamily.Type.POLISHED)) {
      _blockFamily.setVariant(BlockFamily.Type.POLISHED) {
        generator.create<Block>("polished_$_name")
          .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .recipe { c, p ->
            RecipePresets.polishedCraftingRecipe(
              c,
              p,
              { DataIngredient.items(mainBlock!!.get()) },
              4
            )
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              { DataIngredient.items(mainBlock!!.get()) },
              1
            )
          }
          .register()
      }

      if (!_denyList.contains(BlockFamily.Type.POLISHED_STAIRS)) {
        _blockFamily.setVariant(BlockFamily.Type.POLISHED_STAIRS) {
          generator.create<StairBlock>("polished_$_name")
            .stairsBlock({ _blockFamily.blocks[BlockFamily.Type.POLISHED]!!.defaultState })
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(mainBlock!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.POLISHED]!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.stairsCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.POLISHED]!!.get()
                  .asItem())
              }
            }
            .register()
        }
      }

      if (!_denyList.contains(BlockFamily.Type.POLISHED_SLAB)) {
        _blockFamily.setVariant(BlockFamily.Type.POLISHED_SLAB) {
          generator.create<SlabBlock>("polished_$_name")
            .slabBlock()
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c, p,
                {
                  DataIngredient.items(mainBlock!!.get()
                    .asItem())
                },
                2
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.POLISHED]!!.get()
                    .asItem())
                },
                2
              )
              RecipePresets.slabRecycleRecipe(c, p) {
                _blockFamily.blocks[BlockFamily.Type.POLISHED]!!.get()
                  .asItem()
              }
              RecipePresets.slabCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.POLISHED]!!.get()
                  .asItem())
              }
            }
            .register()
        }
      }

      if (!_denyList.contains(BlockFamily.Type.POLISHED_WALL)) {
        _blockFamily.setVariant(BlockFamily.Type.POLISHED_WALL) {
          generator.create<WallBlock>("polished_$_name")
            .wallBlock()
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(mainBlock!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.POLISHED]!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.wallCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.POLISHED]!!.get()
                  .asItem())
              }
            }
            .register()
        }
      }
    }
    // start bricks chain
    if (!_denyList.contains(BlockFamily.Type.BRICKS)) {
      _blockFamily.setVariant(BlockFamily.Type.BRICKS) {
        generator.create<Block>("${_name}_bricks")
          .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .recipe { c, p ->
            RecipePresets.polishedCraftingRecipe(
              c,
              p,
              { DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.POLISHED]!!.get()) },
              4
            )
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              { DataIngredient.items(mainBlock!!.get()) },
              1
            )
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              { DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.POLISHED]!!.get()) },
              1
            )
          }
          .register()
      }

      if (!_denyList.contains(BlockFamily.Type.BRICK_STAIRS)) {
        _blockFamily.setVariant(BlockFamily.Type.BRICK_STAIRS) {
          generator.create<StairBlock>("${_name}_brick")
            .stairsBlock({ _blockFamily.blocks[BlockFamily.Type.BRICKS]!!.defaultState })
            .textureName("${_name}_bricks")
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(mainBlock!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.BRICKS]!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.POLISHED]!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.stairsCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.BRICKS]!!.get()
                  .asItem())
              }
            }
            .register()
        }
      }

      if (!_denyList.contains(BlockFamily.Type.BRICK_SLAB)) {
        _blockFamily.setVariant(BlockFamily.Type.BRICK_SLAB) {
          generator.create<SlabBlock>("${_name}_brick")
            .slabBlock()
            .textureName("${_name}_bricks")
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c, p,
                {
                  DataIngredient.items(mainBlock!!.get()
                    .asItem())
                },
                2
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.POLISHED]!!.get()
                    .asItem())
                },
                2
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.BRICKS]!!.get()
                    .asItem())
                },
                2
              )
              RecipePresets.slabRecycleRecipe(c, p) {
                _blockFamily.blocks[BlockFamily.Type.BRICKS]!!.get()
                  .asItem()
              }
              RecipePresets.slabCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.BRICKS]!!.get()
                  .asItem())
              }
            }
            .register()
        }
      }

      if (!_denyList.contains(BlockFamily.Type.BRICK_WALL)) {
        _blockFamily.setVariant(BlockFamily.Type.BRICK_WALL) {
          generator.create<WallBlock>("${_name}_brick")
            .wallBlock()
            .textureName("${_name}_bricks")
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(mainBlock!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.POLISHED]!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.BRICKS]!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.wallCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.BRICKS]!!.get()
                  .asItem())
              }
            }
            .register()
        }
      }
    }
    // start chiseled chain
    if (!_denyList.contains(BlockFamily.Type.CHISELED)) {
      _blockFamily.setVariant(BlockFamily.Type.CHISELED) {
        generator.create<Block>("chiseled_$_name")
          .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .recipe { c, p ->
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              {
                DataIngredient.items(mainBlock!!.get()
                  .asItem())
              },
              1
            )
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              {
                DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.POLISHED]!!.get()
                  .asItem())
              },
              1
            )
            RecipePresets.slabToChiseledRecipe(c, p) {
              DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.SLAB]!!.get()
                .asItem())
            }
          }
          .register()
      }
    }
    // PILLAR
    if (!_denyList.contains(BlockFamily.Type.PILLAR)) {
      _blockFamily.setVariant(BlockFamily.Type.PILLAR) {
        generator.create<RotatedPillarBlock>("${_name}_pillar")
          .rotatedPillarBlock("${_name}_pillar_top", "${_name}_pillar_side")
          .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .recipe { c, p ->
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              {
                DataIngredient.items(mainBlock!!.get()
                  .asItem())
              },
              1
            )
            RecipePresets.simpleStonecuttingRecipe(
              c,
              p,
              {
                DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.POLISHED]!!.get()
                  .asItem())
              },
              1
            )
            RecipePresets.slabToChiseledRecipe(c, p) {
              DataIngredient.items(mainBlock!!.get()
                .asItem())
            }
          }
          .register()
      }
    }
  }
}