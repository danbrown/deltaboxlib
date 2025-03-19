package com.dannbrown.deltaboxlib.registrate.presets.blocks

import com.dannbrown.deltaboxlib.content.block.GenericSaplingBlock
import com.dannbrown.deltaboxlib.content.worldgen.tree.DeltaboxTreeGrower
import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.builders.BlockBuilder
import net.minecraft.core.BlockPos
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState

class SaplingBlockPreset(
  val registrate: AbstractDeltaboxRegistrate,
  val blockId: String,
  val treeGrower: DeltaboxTreeGrower,
  val placeOn: ((BlockState, BlockGetter, BlockPos) -> Boolean)? = null
) : IBlockBuilderPreset(registrate, blockId) {
  fun <T : Block> create(): BlockBuilder<T> {
    val nameWithSuffix = "${blockId}_sapling"
    return registrate.block<T>(nameWithSuffix)
      .factory { c, p -> GenericSaplingBlock(treeGrower, p, placeOn) }
      .copyFrom { Blocks.OAK_SAPLING }
      .properties { c, p ->
        p
          .sound(SoundType.GRASS)
          .strength(0.0f)
          .randomTicks()
          .noCollission()
          .noOcclusion()
      }
      .cutoutRender()
      .compostable(0.3f)
      .blockstate { g, b -> g.crossBlock(b.get(), nameWithSuffix) }
      .blockTags(BlockTags.SAPLINGS)
      .item()
      .itemTags(ItemTags.SAPLINGS)
      .model { g, i -> g.flatItemBlock(i.get()) }
      .build() as BlockBuilder<T>
  }
}