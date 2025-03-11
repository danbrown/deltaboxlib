package com.dannbrown.deltaboxlib.registrate.presets.blocks

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.builders.BlockBuilder
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FlowerPotBlock
import java.util.function.Supplier

class PottedBlockPreset(
  val registrate: AbstractDeltaboxRegistrate,
  val blockId: String,
  val plantBlock: Supplier<out Block>,
  val suffix: String = ""
) {
  fun <T : Block> create(): BlockBuilder<T> {
    return registrate.block<T>("potted_${blockId}${suffix}")
      .factory { c, p -> FlowerPotBlock(plantBlock.get(), p) }
      .copyFrom { Blocks.POTTED_POPPY }
      .noItem()
      .properties { c, p -> p.noOcclusion() }
      .loot { g, b -> g.pottedBlock(b.get(), plantBlock) }
      .blockstate { g, b -> g.pottedPlantBlock(b.get(), plantBlock) }
      .potted(plantBlock)
      .cutoutRender()
  }
}