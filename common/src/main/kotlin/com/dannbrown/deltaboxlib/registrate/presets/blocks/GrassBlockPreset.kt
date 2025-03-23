package com.dannbrown.deltaboxlib.registrate.presets.blocks

import com.dannbrown.deltaboxlib.content.block.GenericDoublePlantBlock
import com.dannbrown.deltaboxlib.content.block.GenericGrassBlock
import com.dannbrown.deltaboxlib.content.block.GenericTallGrassBlock
import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.builders.BlockBuilder
import com.dannbrown.deltaboxlib.registrate.registry.BlockEntry
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
  val isSticky: Boolean = false,
  val isHarmful: Boolean = false,
  val isBonemealable: Boolean = false,
  val chance: Float = 0.6f,
  val multiplier: Int = 2,
  val placeOn: ((blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos) -> Boolean)? = null
) : IBlockBuilderPreset(registrate, blockId) {
  fun <T : Block> create(dropItem: Supplier<ItemLike>? = null): BlockBuilder<T> {
    return registrate
      .block<T>(blockId)
      .factory { c, p -> GenericGrassBlock(p, placeOn, isSticky, isHarmful, isBonemealable) }
      .copyFrom { Blocks.FERN }
      .blockstate { g, b -> g.crossBlock(b.get()) }
      .properties { c, p ->
        p.sound(SoundType.GRASS)
          .strength(0.0f)
          .noCollission()
          .noOcclusion()
      }
      .cutoutRender()
      .item()
      .model { g, i -> g.flatItemBlock(i.get()) }
      .build()
      .compostable(0.3f)
      .loot { g, b -> g.dropItselfSilkShearsOtherLoot(b.get(), dropItem!!, chance, multiplier) } as BlockBuilder<T>
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
      .blockstate { g, b -> g.crossBlock(b.get()) }
      .cutoutRender()
      .item()
      .model { g, i -> g.flatItemBlock(i.get()) }
      .build()
      .compostable(0.3f)
      .loot { g, b -> g.dropItself(b.get()) } as BlockBuilder<T>
  }

  fun <T : Block> createSmallTallGrassBlock(
    dropItem: Supplier<ItemLike>? = null,
    doubleBlock: BlockEntry<GenericDoublePlantBlock>,
    needBonemeal: Boolean = false
  ): BlockBuilder<T> {
    return registrate
      .block<T>(blockId)
      .factory { c, p -> GenericTallGrassBlock(doubleBlock, p, needBonemeal, placeOn) }
      .copyFrom { Blocks.TALL_GRASS }
      .properties { c, p -> p.strength(0.0f).randomTicks().noCollission().noOcclusion() }
      .blockstate { g, b -> g.crossBlock(b.get()) }
      .cutoutRender()
      .item()
      .model { g, i -> g.flatItemBlock(i.get()) }
      .build()
      .compostable(0.3f)
      .loot { g, b -> g.dropItselfSilkShearsOtherLoot(b.get(), dropItem!!, chance, multiplier) } as BlockBuilder<T>
  }

  fun <T : Block> createDoubleTallGrassBlock(
    dropItem: Supplier<ItemLike>? = null,
    seedItem: Supplier<ItemLike>? = null,
    prefix: String = "tall_"
  ): BlockBuilder<T> {
    val blockNameWithPrefix = "${prefix}${blockId}"
    return registrate
      .block<T>(blockNameWithPrefix)
      .factory { c, p -> GenericDoublePlantBlock(p, placeOn, false) }
      .copyFrom { Blocks.TALL_GRASS }
      .properties { c, p -> p.strength(0.0f).randomTicks().noCollission().noOcclusion() }
      .cutoutRender()
      .blockstate { g, b -> g.crossDoubleBlock(b.get(), "${blockNameWithPrefix}_bottom", "${blockNameWithPrefix}_top") }
      .item()
      .model { g, i -> g.flatItemBlock(i.get(), "${blockNameWithPrefix}_bottom") }
      .build()
      .compostable(0.3f)
      .loot { g, b ->
        g.dropDoubleCropLoot(
          b.get(),
          dropItem!!,
          seedItem ?: dropItem,
          true,
          chance,
          multiplier
        )
      } as BlockBuilder<T>
  }

  fun <T : Block> createDoubleFlowerBlock(
    duplicateOnBoneMeal: Boolean
  ): BlockBuilder<T> {
    val blockNameWithPrefix = "${blockId}"
    return registrate
      .block<T>(blockNameWithPrefix)
      .factory { c, p -> GenericDoublePlantBlock(p, placeOn, duplicateOnBoneMeal) }
      .copyFrom { Blocks.ROSE_BUSH }
      .properties { c, p -> p.strength(0.0f).randomTicks().noCollission().noOcclusion() }
      .cutoutRender()
      .blockstate { g, b -> g.crossDoubleBlock(b.get(), "${blockNameWithPrefix}_bottom", "${blockNameWithPrefix}_top") }
      .item()
      .model { g, i -> g.flatItemBlock(i.get(), "${blockNameWithPrefix}_bottom") }
      .build()
      .compostable(0.3f)
      .loot { g, b -> g.dropItself(b.get()) } as BlockBuilder<T>
  }
}