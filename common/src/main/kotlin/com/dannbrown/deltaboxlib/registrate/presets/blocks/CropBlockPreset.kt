package com.dannbrown.deltaboxlib.registrate.presets.blocks

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
  private val cropLang: String,
  private val chance: Float = 1f,
  private val multiplier: Int = 1,
) : IBlockBuilderPreset(registrate, blockId) {
  fun <T : Block> create(
    seedName: String,
    seedLang: String,
    dropItem: Supplier<ItemLike>?,
    isBush: Boolean = true,
    includeSeedOnDrop: Boolean = true,
  ): BlockBuilder<T> {
    return registrate.block<T>(seedName)
      .factory { c, p ->
        GenericCropBlock(
          p,
          false,
          null,
          false,
          isBush,
          includeSeedOnDrop,
          dropItem,
          chance,
          multiplier
        )
      }
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

  fun <T : Block> createBudding(
    seedName: String,
    seedLang: String,
    grownBlock: Supplier<out Block>,
    includeSeedOnDrop: Boolean = true,
  ): BlockBuilder<T> {
    return registrate.block<T>(seedName)
      .factory { c, p ->
        GenericCropBlock(
          p,
          true,
          grownBlock,
          false,
          false,
          includeSeedOnDrop,
          null,
          chance,
          multiplier
        )
      }
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
      .blockstate { g, b -> g.buddingCropBlock(b.get(), blockId) }
      .lang(cropLang)
      .item { b, p -> ItemNameBlockItem(p, b) }
      .model { g, i -> g.flatItem(i.get()) }
      .lang(seedLang)
      .build()
      .loot { g, b -> g.noLoot(b.get()) } as BlockBuilder<T>
  }

  fun <T : Block> createDouble(
    seedItem: Supplier<ItemLike>,
    dropItem: Supplier<ItemLike>?,
    isBush: Boolean = true,
    includeSeedOnDrop: Boolean = true,
  ): BlockBuilder<T> {
    return registrate.block<T>(blockId)
      .factory { c, p ->
        GenericCropBlock(
          p,
          false,
          null,
          true,
          isBush,
          includeSeedOnDrop,
          dropItem,
          chance,
          multiplier
        )
      }
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
      .noItem()
      .loot { g, b ->
        g.dropDoubleCropLoot(
          b.get(),
          dropItem,
          seedItem,
          includeSeedOnDrop,
          chance,
          multiplier
        )
      }
  }
}
