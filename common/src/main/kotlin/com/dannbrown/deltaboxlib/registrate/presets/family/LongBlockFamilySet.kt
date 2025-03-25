package com.dannbrown.deltaboxlib.registrate.presets.family


import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.builders.BlockBuilderContext
import com.dannbrown.deltaboxlib.registrate.presets.tags.BlockTagPresets
import com.dannbrown.deltaboxlib.registrate.registry.BlockEntry
import com.dannbrown.deltaboxlib.registrate.types.BlockPropertiesFactory
import com.dannbrown.deltaboxlib.registrate.util.DataIngredient
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.RotatedPillarBlock
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
class LongBlockFamilySet(
  private val registrate: AbstractDeltaboxRegistrate,
  private val _name: String,
  private val _sharedProps: BlockPropertiesFactory = BiFunction { c: BlockBuilderContext<out Block>, p: BlockBehaviour.Properties -> p },
  private val _toolType: TagKey<Block>? = null,
  private val _toolTier: TagKey<Block>? = null,
  private val _color: MapColor? = null,
  private val _accentColor: MapColor? = null,
  private val _copyFrom: Supplier<Block> = Supplier { Blocks.STONE },
  private val _denyList: List<BlockFamily.Type> = mutableListOf(),
  private var mainBlock: BlockEntry<out Block>? = null,
  isRotatedBlock: Boolean = false
) : AbstractBlockFamilySet() {
  init {
    val MATERIAL_TAG = DeltaboxUtil.TAGS.modItemTag(registrate.modId, _name + "_blocks")

    if (mainBlock == null) {
      _blockFamily.setVariant(BlockFamily.Type.MAIN) {
        registrate.block<Block>(_name)
          .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
          .itemTags(MATERIAL_TAG)
          .blockTags(*BlockTagPresets.caveReplaceableTags().first.toTypedArray())
          .register()
      }
      mainBlock = _blockFamily.blocks[BlockFamily.Type.MAIN]!!
    }
//
    if (!_denyList.contains(BlockFamily.Type.MAIN)) {
      if (!_denyList.contains(BlockFamily.Type.STAIRS)) {
        _blockFamily.setVariant(BlockFamily.Type.STAIRS) {
          registrate.blockPreset<StairBlock>(_name).stairs(_name, isRotatedBlock)
            .itemTags(MATERIAL_TAG)
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .recipe { c, p ->
              c.simpleStonecuttingRecipe(
                { p.get() },
                { mainBlock!!.getItem() },
                1
              )
              c.stairsCraftingRecipe({ p.get() }) {
                DataIngredient(mainBlock!!.getItem())
              }
            }
            .register()
        }
      }

      if (!_denyList.contains(BlockFamily.Type.SLAB)) {
        _blockFamily.setVariant(BlockFamily.Type.SLAB) {
          registrate.blockPreset<SlabBlock>(_name).slab(_name, isRotatedBlock)
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(MATERIAL_TAG)
            .recipe { c, p ->
              c.simpleStonecuttingRecipe(
                { p.get() },
                { mainBlock!!.getItem() },
                2
              )
              c.slabCraftingRecipe({ p.get() }) {
                DataIngredient(mainBlock!!.getItem())
              }
            }
            .register()
        }
      }

      if (!_denyList.contains(BlockFamily.Type.WALL)) {
        _blockFamily.setVariant(BlockFamily.Type.WALL) {
          registrate.blockPreset<WallBlock>(_name).wall(_name, isRotatedBlock)
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(MATERIAL_TAG)
            .recipe { c, p ->
              c.simpleStonecuttingRecipe(
                { p.get() },
                { mainBlock!!.getItem() },
                1
              )
              c.wallCraftingRecipe({ p.get() }) {
                DataIngredient(mainBlock!!.getItem())
              }
            }
            .register()
        }
      }
    }
    // start polished chain
    if (!_denyList.contains(BlockFamily.Type.POLISHED)) {
      _blockFamily.setVariant(BlockFamily.Type.POLISHED) {
        registrate.block<Block>("polished_$_name")
          .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
          .itemTags(MATERIAL_TAG)
          .recipe { c, p ->
            c.simpleStonecuttingRecipe(
              { p.get() },
              { mainBlock!!.getItem() },
              1
            )
            c.polishedCraftingRecipe({ p.get() }, {
              DataIngredient(mainBlock!!.getItem())
            })
          }
          .register()
      }

      if (!_denyList.contains(BlockFamily.Type.POLISHED_STAIRS)) {
        _blockFamily.setVariant(BlockFamily.Type.POLISHED_STAIRS) {
          registrate.blockPreset<StairBlock>("polished_$_name").stairs("polished_$_name")
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(MATERIAL_TAG)
            .recipe { c, p ->
              c.simpleStonecuttingRecipe(
                { p.get() },
                { _blockFamily.blocks[BlockFamily.Type.POLISHED]!!.getItem() },
                1,
              )
              c.simpleStonecuttingRecipe(
                { p.get() },
                { mainBlock!!.getItem() },
                1
              )
              c.stairsCraftingRecipe({ p.get() }) {
                DataIngredient(_blockFamily.blocks[BlockFamily.Type.POLISHED]!!.getItem())
              }
            }
            .register()
        }
      }

      if (!_denyList.contains(BlockFamily.Type.POLISHED_SLAB)) {
        _blockFamily.setVariant(BlockFamily.Type.POLISHED_SLAB) {
          registrate.blockPreset<SlabBlock>("polished_$_name").slab("polished_$_name")
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(MATERIAL_TAG)
            .recipe { c, p ->
              c.simpleStonecuttingRecipe(
                { p.get() },
                { _blockFamily.blocks[BlockFamily.Type.POLISHED]!!.getItem() },
                2
              )
              c.simpleStonecuttingRecipe(
                { p.get() },
                { mainBlock!!.getItem() },
                2
              )
              c.slabCraftingRecipe({ p.get() }) {
                DataIngredient(_blockFamily.blocks[BlockFamily.Type.POLISHED]!!.getItem())
              }
            }
            .register()
        }
      }

      if (!_denyList.contains(BlockFamily.Type.POLISHED_WALL)) {
        _blockFamily.setVariant(BlockFamily.Type.POLISHED_WALL) {
          registrate.blockPreset<WallBlock>("polished_$_name").wall("polished_$_name")
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(MATERIAL_TAG)
            .recipe { c, p ->
              c.simpleStonecuttingRecipe(
                { p.get() },
                { _blockFamily.blocks[BlockFamily.Type.POLISHED]!!.getItem() },
                1
              )
              c.simpleStonecuttingRecipe(
                { p.get() },
                { mainBlock!!.getItem() },
                1
              )
              c.wallCraftingRecipe({ p.get() }) {
                DataIngredient(_blockFamily.blocks[BlockFamily.Type.POLISHED]!!.getItem())
              }
            }
            .register()
        }
      }
    }
    // start bricks chain
    if (!_denyList.contains(BlockFamily.Type.BRICKS)) {
      _blockFamily.setVariant(BlockFamily.Type.BRICKS) {
        registrate.block<Block>("${_name}_bricks")
          .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
          .itemTags(MATERIAL_TAG)
          .recipe { c, p ->
            c.simpleStonecuttingRecipe(
              { p.get() },
              { _blockFamily.blocks[BlockFamily.Type.POLISHED]!!.getItem() },
              1
            )
            c.simpleStonecuttingRecipe(
              { p.get() },
              { mainBlock!!.getItem() },
              1
            )
            c.polishedCraftingRecipe({ p.get() }, {
              DataIngredient(_blockFamily.blocks[BlockFamily.Type.POLISHED]!!.getItem())
            })
          }
          .register()
      }

      if (!_denyList.contains(BlockFamily.Type.BRICK_STAIRS)) {
        _blockFamily.setVariant(BlockFamily.Type.BRICK_STAIRS) {
          registrate.blockPreset<StairBlock>("${_name}_brick").stairs("${_name}_bricks")
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(MATERIAL_TAG)
            .recipe { c, p ->
              c.simpleStonecuttingRecipe(
                { p.get() },
                { _blockFamily.blocks[BlockFamily.Type.POLISHED]!!.getItem() },
                1
              )
              c.simpleStonecuttingRecipe(
                { p.get() },
                { _blockFamily.blocks[BlockFamily.Type.BRICKS]!!.getItem() },
                1
              )
              c.simpleStonecuttingRecipe(
                { p.get() },
                { mainBlock!!.getItem() },
                1
              )
              c.stairsCraftingRecipe({ p.get() }) {
                DataIngredient(_blockFamily.blocks[BlockFamily.Type.BRICKS]!!.getItem())
              }
            }
            .register()
        }
      }

      if (!_denyList.contains(BlockFamily.Type.BRICK_SLAB)) {
        _blockFamily.setVariant(BlockFamily.Type.BRICK_SLAB) {
          registrate.blockPreset<SlabBlock>("${_name}_brick").slab("${_name}_bricks")
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(MATERIAL_TAG)
            .recipe { c, p ->
              c.simpleStonecuttingRecipe(
                { p.get() },
                { _blockFamily.blocks[BlockFamily.Type.POLISHED]!!.getItem() },
                2
              )
              c.simpleStonecuttingRecipe(
                { p.get() },
                { _blockFamily.blocks[BlockFamily.Type.BRICKS]!!.getItem() },
                2
              )
              c.simpleStonecuttingRecipe(
                { p.get() },
                { mainBlock!!.getItem() },
                2
              )
              c.slabCraftingRecipe({ p.get() }) {
                DataIngredient(_blockFamily.blocks[BlockFamily.Type.BRICKS]!!.getItem())
              }
            }
            .register()
        }
      }

      if (!_denyList.contains(BlockFamily.Type.BRICK_WALL)) {
        _blockFamily.setVariant(BlockFamily.Type.BRICK_WALL) {
          registrate.blockPreset<WallBlock>("${_name}_brick").wall("${_name}_bricks")
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(MATERIAL_TAG)
            .recipe { c, p ->
              c.simpleStonecuttingRecipe(
                { p.get() },
                { _blockFamily.blocks[BlockFamily.Type.POLISHED]!!.getItem() },
                1
              )
              c.simpleStonecuttingRecipe(
                { p.get() },
                { _blockFamily.blocks[BlockFamily.Type.BRICKS]!!.getItem() },
                1
              )
              c.simpleStonecuttingRecipe(
                { p.get() },
                { mainBlock!!.getItem() },
                1
              )
              c.wallCraftingRecipe({ p.get() }) {
                DataIngredient(_blockFamily.blocks[BlockFamily.Type.BRICKS]!!.getItem())
              }
            }
            .register()
        }
      }
    }
    // start chiseled chain
    if (!_denyList.contains(BlockFamily.Type.CHISELED)) {
      _blockFamily.setVariant(BlockFamily.Type.CHISELED) {
        registrate.block<Block>("chiseled_$_name")
          .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
          .itemTags(MATERIAL_TAG)
          .recipe { c, p ->
            c.simpleStonecuttingRecipe(
              { p.get() },
              { mainBlock!!.getItem() },
              1
            )
            c.simpleStonecuttingRecipe(
              { p.get() },
              { _blockFamily.blocks[BlockFamily.Type.POLISHED]!!.getItem() },
              1
            )
            c.slabToChiseledRecipe(
              { p.get() },
              { DataIngredient(_blockFamily.blocks[BlockFamily.Type.SLAB]!!.getItem()) }
            )
          }
          .register()
      }
    }
    // PILLAR
    if (!_denyList.contains(BlockFamily.Type.PILLAR)) {
      _blockFamily.setVariant(BlockFamily.Type.PILLAR) {
        registrate.blockPreset<RotatedPillarBlock>("${_name}_pillar").rotatedPillar()
          .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
          .itemTags(MATERIAL_TAG)
          .recipe { c, p ->
            c.simpleStonecuttingRecipe(
              { p.get() },
              { mainBlock!!.getItem() },
              1
            )
            c.simpleStonecuttingRecipe(
              { p.get() },
              { _blockFamily.blocks[BlockFamily.Type.POLISHED]!!.getItem() },
              1
            )
            c.slabToChiseledRecipe(
              { p.get() },
              { DataIngredient(mainBlock!!.getItem()) }
            )
          }
          .register()
      }
    }
  }
}