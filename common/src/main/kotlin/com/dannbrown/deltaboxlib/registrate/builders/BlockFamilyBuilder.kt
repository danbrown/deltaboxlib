package com.dannbrown.deltaboxlib.registrate.builders


import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.presets.family.BlockFamily
import com.dannbrown.deltaboxlib.registrate.presets.family.LongBlockFamilySet
import com.dannbrown.deltaboxlib.registrate.registry.BlockEntry
import com.dannbrown.deltaboxlib.registrate.types.BlockPropertiesFactory
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import java.util.function.BiFunction
import java.util.function.Supplier

class BlockFamilyGeneratorBuilder(private val registrate: AbstractDeltaboxRegistrate, name: String) {
  private val _name = name
  private var _sharedProps: BlockPropertiesFactory =
    BiFunction { c: BlockBuilderContext<out Block>, p: BlockBehaviour.Properties -> p }
  private var _toolType: TagKey<Block>? = null
  private var _toolTier: TagKey<Block>? = null
  private var _color: MapColor? = null
  private var _accentColor: MapColor? = null
  private var _copyFrom: Supplier<Block> = Supplier { Blocks.STONE }
  private val _denyList = mutableListOf<BlockFamily.Type>()
  private val _blockFamily: BlockFamily = BlockFamily()

  // @ Family Presets
  fun longBlockFamily(mainBlock: BlockEntry<out Block>? = null, isRotatedBlock: Boolean = false): BlockFamily {
    return LongBlockFamilySet(
      registrate,
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
//
//  fun woodFamily(
//    woodType: WoodType,
//    setType: BlockSetType,
//    grower: DeltaboxTreeGrower,
//    placeOn: ((blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos) -> Boolean)? = null
//  ): WoodBlockFamilySet.WoodFamilyComponents {
//    return WoodBlockFamilySet(
//      generator,
//      _name,
//      _sharedProps,
//      _toolType,
//      _toolTier,
//      _color,
//      _accentColor,
//      _copyFrom,
//      _denyList,
//      woodType,
//      setType,
//      grower,
//      placeOn
//    ).getContent()
//  }

  // @ Builder Chaining Methods
  fun sharedProps(props: BlockPropertiesFactory): BlockFamilyGeneratorBuilder {
    _sharedProps = props
    return this
  }

  fun toolAndTier(
    tool: TagKey<Block>? = null,
    tier: TagKey<Block>? = null,
    requiredForDrops: Boolean = true
  ): BlockFamilyGeneratorBuilder {
    _toolTier = tier
    _toolType = tool
    if (requiredForDrops) {
      sharedProps { c: BlockBuilderContext<out Block>, p: BlockBehaviour.Properties -> p.requiresCorrectToolForDrops() }
    }
    return this
  }

  fun color(
    color: MapColor,
    accentColor: MapColor? = null
  ): BlockFamilyGeneratorBuilder {
    this._color = color
    this._accentColor = accentColor
    return this
  }

  fun copyFrom(block: Supplier<Block>): BlockFamilyGeneratorBuilder {
    _copyFrom = block
    return this
  }

  fun denyList(vararg deny: BlockFamily.Type): BlockFamilyGeneratorBuilder {
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

  fun getSharedProps(): BlockPropertiesFactory {
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

  /**
   * Allow for custom block family generation
   */
  fun custom(functionToExecute: (BlockFamilyGeneratorBuilder) -> BlockFamily): BlockFamily {
    return functionToExecute(this)
  }
}