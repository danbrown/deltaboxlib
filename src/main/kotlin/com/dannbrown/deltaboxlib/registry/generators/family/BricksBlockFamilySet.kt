package com.dannbrown.deltaboxlib.registry.generators.family

import com.dannbrown.deltaboxlib.lib.LibTags
import com.dannbrown.deltaboxlib.registry.generators.BlockFamily
import com.dannbrown.deltaboxlib.registry.generators.BlockGenerator
import com.dannbrown.deltaboxlib.registry.transformers.BlockTagPresets
import com.dannbrown.deltaboxlib.registry.transformers.RecipePresets
import com.tterrag.registrate.util.DataIngredient
import com.tterrag.registrate.util.entry.BlockEntry
import net.minecraft.tags.TagKey
import net.minecraft.world.level.ItemLike
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
class BricksBlockFamilySet(
  private val generator: BlockGenerator,
  private val _name: String,
  private val _sharedProps: (BlockBehaviour.Properties) -> BlockBehaviour.Properties = { p: BlockBehaviour.Properties -> p },
  private val _toolType: TagKey<Block>? = null,
  private val _toolTier: TagKey<Block>? = null,
  private val _color: MapColor? = null,
  private val _accentColor: MapColor? = null,
  private val _copyFrom: Supplier<Block> = Supplier { Blocks.STONE },
  private val _denyList: List<BlockFamily.Type> = mutableListOf(),
  private val bricksMaterial: Supplier<out ItemLike>,
): AbstractBlockFamilySet() {
  init {
    val MATERIAL_TAG = LibTags.modItemTag(generator.registrate.modid, _name + "_blocks")

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
              { DataIngredient.items(bricksMaterial.get()) },
              4
            )
          }
          .register()
      }

      if (!_denyList.contains(BlockFamily.Type.BRICK_STAIRS)) {
        _blockFamily.setVariant(BlockFamily.Type.BRICK_STAIRS) {
          generator.create<StairBlock>("${_name}_brick")
            .textureName("${_name}_bricks")
            .stairsBlock({ _blockFamily.blocks[BlockFamily.Type.BRICKS]!!.defaultState })
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets.simpleStonecuttingRecipe(
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.BRICKS]!!.get()
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
                c,
                p,
                {
                  DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.BRICKS]!!.get()
                    .asItem())
                },
                2
              )
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
  }
}