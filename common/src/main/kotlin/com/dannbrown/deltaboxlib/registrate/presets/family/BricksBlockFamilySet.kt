package com.dannbrown.deltaboxlib.registrate.presets.family

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.builders.BlockBuilderContext
import com.dannbrown.deltaboxlib.registrate.types.BlockPropertiesFactory
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import net.minecraft.tags.TagKey
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.WallBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import java.util.function.BiFunction
import java.util.function.Supplier

/**
 * Returns a Long block family composed by Normals, Polished, Bricks, Cut, Chiseled variants (stairs, slabs, walls)
 */
class BricksBlockFamilySet(
  private val registrate: AbstractDeltaboxRegistrate,
  private val _name: String,
  private val _sharedProps: BlockPropertiesFactory = BiFunction { c: BlockBuilderContext<out Block>, p: BlockBehaviour.Properties -> p },
  private val _toolType: TagKey<Block>? = null,
  private val _toolTier: TagKey<Block>? = null,
  private val _color: MapColor? = null,
  private val _accentColor: MapColor? = null,
  private val _copyFrom: Supplier<Block> = Supplier { Blocks.STONE },
  private val _denyList: List<BlockFamily.Type> = mutableListOf(),
  private val bricksMaterial: Supplier<out ItemLike>,
) : AbstractBlockFamilySet() {
  init {
    val MATERIAL_TAG = DeltaboxUtil.TAGS.modItemTag(registrate.modId, _name + "_blocks")

    // start bricks chain
    if (!_denyList.contains(BlockFamily.Type.BRICKS)) {
      _blockFamily.setVariant(BlockFamily.Type.BRICKS) {
        registrate.block<Block>("${_name}_bricks")
          .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
          .itemTags(MATERIAL_TAG)
          .recipe { c, p ->
            c.polishedCraftingRecipe({ p.get() }, { Ingredient.of(bricksMaterial.get()) }, 4)
          }
          .register()
      }

      if (!_denyList.contains(BlockFamily.Type.BRICK_STAIRS)) {
        _blockFamily.setVariant(BlockFamily.Type.BRICK_STAIRS) {
          registrate.blockPreset<StairBlock>("${_name}_brick")
            .stairs("${_name}_bricks")
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(MATERIAL_TAG)
            .recipe { c, p ->
              c.simpleStonecuttingRecipe(
                { p.get() },
                { _blockFamily.blocks[BlockFamily.Type.BRICKS]!!.getItem() },
                1
              )
              c.stairsCraftingRecipe({ p.get() }) {
                Ingredient.of(_blockFamily.blocks[BlockFamily.Type.BRICKS]!!.getItem())
              }
            }
            .register()
        }
      }

      if (!_denyList.contains(BlockFamily.Type.BRICK_SLAB)) {
        _blockFamily.setVariant(BlockFamily.Type.BRICK_SLAB) {
          registrate.blockPreset<SlabBlock>("${_name}_brick")
            .slab("${_name}_bricks")
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(MATERIAL_TAG)
            .recipe { c, p ->
              c.slabCraftingRecipe({ p.get() }) {
                Ingredient.of(_blockFamily.blocks[BlockFamily.Type.BRICKS]!!.getItem())
              }
              c.simpleStonecuttingRecipe(
                { p.get() },
                { _blockFamily.blocks[BlockFamily.Type.BRICKS]!!.getItem() },
                2
              )
            }
            .register()
        }
      }

      if (!_denyList.contains(BlockFamily.Type.BRICK_WALL)) {
        _blockFamily.setVariant(BlockFamily.Type.BRICK_WALL) {
          registrate.blockPreset<WallBlock>("${_name}_brick")
            .wall("${_name}_bricks")
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(MATERIAL_TAG)
            .recipe { c, p ->
              c.simpleStonecuttingRecipe(
                { p.get() },
                { _blockFamily.blocks[BlockFamily.Type.BRICKS]!!.getItem() },
                1
              )
              c.wallCraftingRecipe({ p.get() }) {
                Ingredient.of(_blockFamily.blocks[BlockFamily.Type.BRICKS]!!.getItem())
              }
            }
            .register()
        }
      }
    }
  }
}