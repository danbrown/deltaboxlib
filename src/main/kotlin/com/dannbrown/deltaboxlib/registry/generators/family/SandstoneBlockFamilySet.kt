package com.dannbrown.deltaboxlib.registry.generators.family

import com.dannbrown.deltaboxlib.lib.LibTags
import com.dannbrown.deltaboxlib.registry.generators.BlockFamily
import com.dannbrown.deltaboxlib.registry.generators.BlockGenerator
import com.dannbrown.deltaboxlib.registry.transformers.BlockTagPresets
import com.dannbrown.deltaboxlib.registry.transformers.RecipePresets
import com.tterrag.registrate.util.DataIngredient
import com.tterrag.registrate.util.entry.BlockEntry
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.WallBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import java.util.function.Supplier

/**
 * Returns a SandStone block family composed by
 */
class SandstoneBlockFamilySet(
  private val generator: BlockGenerator,
  private val _name: String,
  private val _sharedProps: (BlockBehaviour.Properties) -> BlockBehaviour.Properties = { p: BlockBehaviour.Properties -> p },
  private val _toolType: TagKey<Block>? = null,
  private val _toolTier: TagKey<Block>? = null,
  private val _color: MapColor? = null,
  private val _accentColor: MapColor? = null,
  private val _copyFrom: Supplier<Block> = Supplier { Blocks.STONE },
  private val _denyList: List<BlockFamily.Type> = mutableListOf(),
  private var mainBlock: Supplier<out Block>? = null
): AbstractBlockFamilySet() {
  init{
    val MATERIAL_TAG = LibTags.modItemTag(generator.registrate.modid, _name + "_blocks")

    if (mainBlock == null) {
      _blockFamily.setVariant(BlockFamily.Type.MAIN) {
        generator.create<Block>(_name)
          .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .blockTags(listOf())
          .register()
      }
      mainBlock = _blockFamily.blocks[BlockFamily.Type.MAIN]!!
    }

    if (!_denyList.contains(BlockFamily.Type.SANDSTONE)) {
      _blockFamily.setVariant(BlockFamily.Type.SANDSTONE) {
        generator.create<Block>(_name + "_sandstone")
          .bottomTopBlock()
          .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .recipe { c, p ->
            RecipePresets.polishedCraftingRecipe(
              c,
              p,
              {
                DataIngredient.items(mainBlock!!.get().asItem())
              })
          }
          .register()
      }

      if (!_denyList.contains(BlockFamily.Type.SANDSTONE_STAIRS)) {
        _blockFamily.setVariant(BlockFamily.Type.SANDSTONE_STAIRS) {
          generator.create<StairBlock>(_name + "_sandstone")
            .stairsBlock({ _blockFamily.blocks[BlockFamily.Type.SANDSTONE]!!.defaultState }, true)
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.SANDSTONE]!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.stairsCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.SANDSTONE]!!.get()
                  .asItem())
              }
            }
            .register()
        }
      }

      if (!_denyList.contains(BlockFamily.Type.SANDSTONE_SLAB)) {
        _blockFamily.setVariant(BlockFamily.Type.SANDSTONE_SLAB) {
          generator.create<SlabBlock>(_name + "_sandstone")
            .slabBlock(true)
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.SANDSTONE]!!.get()
                    .asItem())
                },
                2
              )
              RecipePresets.slabCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.SANDSTONE]!!.get()
                  .asItem())
              }
            }
            .register()
        }
      }

      if (!_denyList.contains(BlockFamily.Type.SANDSTONE_WALL)) {
        _blockFamily.setVariant(BlockFamily.Type.SANDSTONE_WALL) {
          generator.create<WallBlock>(_name + "_sandstone")
            .wallBlock()
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.SANDSTONE]!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.wallCraftingRecipe(
                c,
                p
              ) {
                DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.SANDSTONE]!!.get()
                  .asItem())
              }
            }
            .register()
        }
      }
      // CHISELED
      if (!_denyList.contains(BlockFamily.Type.CHISELED)) {
        _blockFamily.setVariant(BlockFamily.Type.CHISELED) {
          generator.create<Block>("chiseled_$_name" + "_sandstone")
            .bottomTopBlock(_name + "_sandstone_bottom", _name + "_sandstone_top")
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.SANDSTONE]!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.slabToChiseledRecipe(c, p) {
                DataIngredient.items(
                  _blockFamily.blocks[BlockFamily.Type.SANDSTONE_SLAB]!!.get()
                    .asItem()
                )
              }
            }
            .register()
        }
      }
      // CUT
      if (!_denyList.contains(BlockFamily.Type.CUT)) {
        _blockFamily.setVariant(BlockFamily.Type.CUT) {
          generator.create<Block>("cut_$_name" + "_sandstone")
            .bottomTopBlock(_name + "_sandstone_bottom", _name + "_sandstone_top")
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.SANDSTONE]!!.get()
                    .asItem())
                },
                1
              )
              RecipePresets.polishedCraftingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.SANDSTONE]!!.get()
                    .asItem())
                },
                4
              )
            }
            .register()
        }
      }
      // SMOOTH
      if (!_denyList.contains(BlockFamily.Type.SMOOTH)) {
        _blockFamily.setVariant(BlockFamily.Type.SMOOTH) {
          generator.create<Block>("smooth_$_name" + "_sandstone")
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
                DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.SANDSTONE]!!.get()
                  .asItem()),
                RecipeCategory.BUILDING_BLOCKS,
                { c.get() },
                0.1f,
                200
              )
            }
            .register()
        }

        if (!_denyList.contains(BlockFamily.Type.SMOOTH_STAIRS)) {
          _blockFamily.setVariant(BlockFamily.Type.SMOOTH_STAIRS) {
            generator.create<StairBlock>("smooth_$_name" + "_sandstone")
              .stairsBlock({ _blockFamily.blocks[BlockFamily.Type.SMOOTH]!!.defaultState })
              .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
              .textureName(_name + "_sandstone_top")
              .itemTags(listOf(MATERIAL_TAG))
              .recipe { c, p ->
                RecipePresets.simpleStonecuttingRecipe(
                  c,
                  p,
                  {
                    DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.SMOOTH]!!.get()
                      .asItem())
                  },
                  1
                )
                RecipePresets.stairsCraftingRecipe(
                  c,
                  p
                ) {
                  DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.SMOOTH]!!.get()
                    .asItem())
                }
              }
              .register()
          }
        }

        if (!_denyList.contains(BlockFamily.Type.SMOOTH_SLAB)) {
          _blockFamily.setVariant(BlockFamily.Type.SMOOTH_SLAB) {
            generator.create<SlabBlock>("smooth_$_name" + "_sandstone")
              .slabBlock()
              .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
              .textureName(_name + "_sandstone_top")
              .itemTags(listOf(MATERIAL_TAG))
              .recipe { c, p ->
                RecipePresets.simpleStonecuttingRecipe(
                  c,
                  p,
                  {
                    DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.SMOOTH]!!.get()
                      .asItem())
                  },
                  2
                )
                RecipePresets.slabRecycleRecipe(c, p) {
                  _blockFamily.blocks[BlockFamily.Type.SMOOTH]!!.get()
                    .asItem()
                }
                RecipePresets.slabCraftingRecipe(
                  c,
                  p
                ) {
                  DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.SMOOTH]!!.get()
                    .asItem())
                }
              }
              .register()
          }
        }

        if (!_denyList.contains(BlockFamily.Type.SMOOTH_WALL)) {
          _blockFamily.setVariant(BlockFamily.Type.SMOOTH_WALL) {
            generator.create<WallBlock>("smooth_$_name" + "_sandstone")
              .wallBlock()
              .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
              .textureName(_name + "_sandstone_top")
              .itemTags(listOf(MATERIAL_TAG))
              .recipe { c, p ->
                RecipePresets.simpleStonecuttingRecipe(
                  c,
                  p,
                  {
                    DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.SMOOTH]!!.get()
                      .asItem())
                  },
                  1
                )
                RecipePresets.wallCraftingRecipe(
                  c,
                  p
                ) {
                  DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.SMOOTH]!!.get()
                    .asItem())
                }
              }
              .register()
          }
        }
      }
    }

  }
}