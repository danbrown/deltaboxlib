package com.dannbrown.deltaboxlib.registrate.presets.blocks

import com.dannbrown.deltaboxlib.content.block.DoubleCropBlock
import com.dannbrown.deltaboxlib.content.block.GenericCropBlock
import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.builders.BlockBuilder
import net.minecraft.world.item.ItemNameBlockItem
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.material.PushReaction
import java.util.function.Supplier

class CropBlockPreset(
  val registrate: AbstractDeltaboxRegistrate,
  val blockId: String,
  private val seedName: String,
  private val cropLang: String,
  private val seedLang: String,
  private val dropItem: Supplier<ItemLike>?,
  private val isBush: Boolean = true,
  private val includeSeedOnDrop: Boolean = true,
  private val chance: Float = 1f,
  private val multiplier: Int = 1,
) : IBlockBuilderPreset(registrate, blockId) {
  fun <T : Block> create(): BlockBuilder<T> {
    return registrate.block<T>(seedName)
      .factory { c, p -> GenericCropBlock(p, false, isBush, includeSeedOnDrop, dropItem, chance, multiplier) }
      .copyFrom { Blocks.WHEAT }
      .properties { c, p ->
        p
          .noCollission()
          .randomTicks()
          .instabreak()
          .sound(SoundType.CROP)
          .pushReaction(PushReaction.DESTROY)
      }
      .cutoutRender()
      .blockstate { g, b -> g.cropBlock(b.get(), blockId) }
      .lang(cropLang)
      .item { b, p -> ItemNameBlockItem(p, b) }
      .model { g, i -> g.flatItem(i.get()) }
      .lang(seedLang)
      .build()
      .loot { g, b ->
        g.dropCropLoot(
          b.get(),
          dropItem,
          null,
          includeSeedOnDrop,
          chance,
          multiplier
        )
      } as BlockBuilder<T>
  }

  fun <T : Block> createDouble(): BlockBuilder<T> {
    return registrate.block<T>(seedName)
      .factory { c, p -> GenericCropBlock(p, true, isBush, includeSeedOnDrop, dropItem, chance, multiplier) }
      .copyFrom { Blocks.WHEAT }
      .properties { c, p ->
        p
          .noCollission()
          .randomTicks()
          .instabreak()
          .sound(SoundType.CROP)
          .pushReaction(PushReaction.DESTROY)
      }
      .cutoutRender()
      .blockstate { g, b -> g.doubleCropBlock(b.get(), blockId) }
      .lang(cropLang)
      .item { b, p -> ItemNameBlockItem(p, b) }
      .model { g, i -> g.flatItem(i.get()) }
      .lang(seedLang)
      .build()
      .loot { g, b ->
        g.dropDoubleCropLoot(
          b.get(),
          dropItem,
          null,
          includeSeedOnDrop,
          chance,
          multiplier
        )
      } as BlockBuilder<T>
  }
}
