package com.dannbrown.deltaboxlib.registrate.presets.blocks

import com.dannbrown.deltaboxlib.content.block.GenericDoublePlantBlock
import com.dannbrown.deltaboxlib.content.block.GenericGrassBlock
import com.dannbrown.deltaboxlib.content.block.GenericTallGrassBlock
import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.builders.BlockBuilder
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Supplier

class GrassBlockPreset(
  val registrate: AbstractDeltaboxRegistrate,
  val blockId: String,
  val dropItem: Supplier<ItemLike>? = null,
  val isSticky: Boolean = false,
  val isHarmful: Boolean = false,
  val isBonemealable: Boolean = false,
  val chance: Float = 0.6f,
  val multiplier: Int = 2,
  val placeOn: ((blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos) -> Boolean)? = null
) : IBlockBuilderPreset(registrate, blockId) {
  fun <T : Block> create(): BlockBuilder<T> {
    return registrate
      .block<T>(blockId)
      .factory { c, p -> GenericGrassBlock(p, placeOn, isSticky, isHarmful, isBonemealable) }
      .copyFrom { Blocks.FERN }
      .properties { c, p ->
        p.sound(SoundType.GRASS)
          .strength(0.0f)
          .noCollission()
          .noOcclusion()
      }
      .item()
//      .model(ItemModelPresets.simpleLayerItem(blockId))
      .build()
//      .blockstate(BlockstatePresets.simpleCrossBlock(blockId))
//      .loot(BlockLootPresets.dropSelfSilkShearsOtherLoot(dropItem!!, chance, multiplier))
      .cutoutRender() as BlockBuilder<T>
  }

  fun <T : Block> createFlower(): BlockBuilder<T> {
    return registrate
      .block<T>(blockId)
      .factory { c, p -> GenericGrassBlock(p, placeOn, isSticky, isHarmful, isBonemealable) }
      .copyFrom { Blocks.POPPY }
      .properties { c, p ->
        p.sound(SoundType.GRASS)
          .strength(0.0f)
          .noCollission()
          .noOcclusion()
      }
      .item()
//      .model(ItemModelPresets.simpleLayerItem(blockId))
      .build()
//      .blockstate(BlockstatePresets.simpleCrossBlock(blockId))
//      .loot(BlockLootPresets.dropItselfLoot())
      .cutoutRender() as BlockBuilder<T>
  }

  fun <T : Block> createSmallTallGrassBlock(
    doubleBlock: Supplier<GenericDoublePlantBlock>,
    needBonemeal: Boolean = false
  ): BlockBuilder<T> {
    return registrate
      .block<T>(blockId)
      .factory { c, p -> GenericTallGrassBlock(doubleBlock, p, needBonemeal, placeOn) }
      .copyFrom { Blocks.TALL_GRASS }
      .properties { c, p -> p.strength(0.0f).randomTicks().noCollission().noOcclusion() }
      .item()
//      .model(ItemModelPresets.simpleLayerItem(blockId))
      .build()
//      .blockstate(BlockstatePresets.simpleCrossBlock(blockId))
//      .loot(BlockLootPresets.dropSelfSilkShearsOtherLoot(dropItem!!, chance, multiplier))
      .cutoutRender() as BlockBuilder<T>
  }

  fun <T : Block> createDoubleTallGrassBlock(
    seedItem: Supplier<ItemLike>? = null,
    prefix: String = "tall_"
  ): BlockBuilder<T> {
    return registrate
      .block<T>("${prefix}${blockId}")
      .factory { c, p -> GenericDoublePlantBlock(p, placeOn) }
      .copyFrom { Blocks.TALL_GRASS }
      .properties { c, p -> p.strength(0.0f).randomTicks().noCollission().noOcclusion() }
      .item()
//      .model(ItemModelPresets.simpleLayerItem(blockId + "_top"))
      .build()
//      .loot(BlockLootPresets.dropDoubleCropLoot(dropItem!!, seedItem ?: dropItem, true, chance, multiplier))
//      .blockstate(BlockstatePresets.simpleDoubleCrossBlock(blockId))
      .cutoutRender() as BlockBuilder<T>
  }
}