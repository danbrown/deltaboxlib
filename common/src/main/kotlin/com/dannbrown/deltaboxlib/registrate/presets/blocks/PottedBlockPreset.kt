package com.dannbrown.deltaboxlib.registrate.presets.blocks

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.builders.BlockBuilder
import com.dannbrown.deltaboxlib.registrate.registry.BlockEntry
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FlowerPotBlock

class PottedBlockPreset(
  val registrate: AbstractDeltaboxRegistrate,
  val blockId: String,
  val plantBlock: BlockEntry<*>,
  val suffix: String = ""
) {
  fun <T : Block> create(): BlockBuilder<T> {
    return registrate.block<T>("potted_${blockId}${suffix}")
      .factory { c, p -> FlowerPotBlock(plantBlock.get(), p) }
      .copyFrom { Blocks.POTTED_POPPY }
      .noItem()
      .properties { c, p -> p.noOcclusion() }
      .loot { g, b -> g.pottedBlock(b.get(), plantBlock.supplier()) }
      .blockstate { g, b -> g.pottedPlantBlock(b.get(), plantBlock.get()) }
      .potted { plantBlock.get() }
      .cutoutRender()
  }
}