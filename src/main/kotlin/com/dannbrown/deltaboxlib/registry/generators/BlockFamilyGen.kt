package com.dannbrown.deltaboxlib.registry.generators

import com.dannbrown.deltaboxlib.registry.generators.family.BricksBlockFamilySet
import com.dannbrown.deltaboxlib.registry.generators.family.LongBlockFamilySet
import com.dannbrown.deltaboxlib.registry.generators.family.MineralBlockFamilySet
import com.dannbrown.deltaboxlib.registry.generators.family.SandstoneBlockFamilySet
import com.dannbrown.deltaboxlib.registry.generators.family.StoneBricksBlockFamilySet
import com.dannbrown.deltaboxlib.registry.generators.family.WoodBlockFamilySet
import com.tterrag.registrate.util.entry.BlockEntry
import net.minecraft.core.BlockPos
import net.minecraft.tags.TagKey
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.grower.AbstractTreeGrower
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.WoodType
import net.minecraft.world.level.material.MapColor
import java.util.function.Supplier

class BlockFamilyGen(name: String, private val generator: BlockGenerator) {
  private val _name = name
  private var _sharedProps: (BlockBehaviour.Properties) -> BlockBehaviour.Properties = { p: BlockBehaviour.Properties -> p }
  private var _toolType: TagKey<Block>? = null
  private var _toolTier: TagKey<Block>? = null
  private var _color: MapColor? = null
  private var _accentColor: MapColor? = null
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
    color: MapColor,
    accentColor: MapColor? = null
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

  fun getColor(): MapColor? {
    return _color
  }

  fun getAccentColor(): MapColor? {
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

  fun getGenerator(): BlockGenerator {
    return generator
  }


  /**
   * Allow for custom block family generation
   */
  fun custom(functionToExecute: (BlockFamilyGen) -> BlockFamily): BlockFamily {
    return functionToExecute(this)
  }

  fun longBlockFamily(mainBlock: Supplier<out Block>? = null, isRotatedBlock: Boolean = false): BlockFamily {
    return LongBlockFamilySet(
      generator,
      _name,
      _sharedProps,
      _toolType,
      _toolTier,
      _color,
      _accentColor,
      _copyFrom,
      _denyList,
      mainBlock,
      isRotatedBlock
    ).getFamily()
  }

  fun mineralFamily(mainBlock: Supplier<out Block>? = null, isRotatedBlock: Boolean = false): BlockFamily {
    return MineralBlockFamilySet(
      generator,
      _name,
      _sharedProps,
      _toolType,
      _toolTier,
      _color,
      _accentColor,
      _copyFrom,
      _denyList,
      mainBlock,
      isRotatedBlock
    ).getFamily()
  }

  fun sandstoneFamily(baseBlock: Supplier<out Block>): BlockFamily {
    return SandstoneBlockFamilySet(
      generator,
      _name,
      _sharedProps,
      _toolType,
      _toolTier,
      _color,
      _accentColor,
      _copyFrom,
      _denyList,
      baseBlock
    ).getFamily()
  }

  fun bricksBlockFamily(mainBlock: Supplier<out Block>, polishedBlock: Supplier<out Block>): BlockFamily {
    return StoneBricksBlockFamilySet(
      generator,
      _name,
      _sharedProps,
      _toolType,
      _toolTier,
      _color,
      _accentColor,
      _copyFrom,
      _denyList,
      mainBlock,
      polishedBlock
    ).getFamily()
  }

  fun bricksBlockFamily(mainBlock: Supplier<out ItemLike>): BlockFamily {
    return BricksBlockFamilySet(
      generator,
      _name,
      _sharedProps,
      _toolType,
      _toolTier,
      _color,
      _accentColor,
      _copyFrom,
      _denyList,
      mainBlock,
    ).getFamily()
  }


  fun woodFamily(
    woodType: WoodType,
    grower: AbstractTreeGrower,
    placeOn: ((blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos) -> Boolean)? = null
  ): BlockFamily {
    return WoodBlockFamilySet(
      generator,
      _name,
      _sharedProps,
      _toolType,
      _toolTier,
      _color,
      _accentColor,
      _copyFrom,
      _denyList,
      woodType,
      grower,
      placeOn
    ).getFamily()
  }
}


