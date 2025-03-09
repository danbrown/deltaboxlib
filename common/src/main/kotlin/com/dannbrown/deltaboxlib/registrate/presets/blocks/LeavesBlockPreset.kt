package com.dannbrown.deltaboxlib.registrate.presets.blocks

import com.dannbrown.deltaboxlib.content.block.BuddingLeavesBlock
import com.dannbrown.deltaboxlib.content.block.CropLeavesBlock
import com.dannbrown.deltaboxlib.content.block.FlammableLeavesBlock
import com.dannbrown.deltaboxlib.content.block.GenericSaplingBlock
import com.dannbrown.deltaboxlib.content.block.PalmLeavesBlock
import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.builders.BlockBuilder
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import java.util.function.Supplier

class LeavesBlockPreset(
  val registrate: AbstractDeltaboxRegistrate,
  val blockId: String,
  private val sapling: Supplier<GenericSaplingBlock>,
  private val suffix: String = "_leaves"
) : IBlockBuilderPreset(registrate, blockId) {
  fun <T : Block> create(): BlockBuilder<T> {
    return registrate
      .block<T>(blockId + suffix)
      .factory { c, p -> FlammableLeavesBlock(p, c.flammabilityBurnChance, c.flammabilitySpreadChance) }
      .flammable(30, 60)
      .cutoutRender()
      .copyFrom { Blocks.OAK_LEAVES }
      .properties { c, p ->
        p.randomTicks()
          .noOcclusion()
          .isSuffocating { s, b, p -> false }
          .isViewBlocking { s, b, p -> false }
          .isRedstoneConductor { s, b, p -> false }
          .ignitedByLava()
      }
      .blockTags(
        BlockTags.LEAVES,
        BlockTags.MINEABLE_WITH_HOE,
        *DeltaboxUtil.TAGS.modloaderBlockTag("leaves").toTypedArray()
      )
      .itemTags(ItemTags.LEAVES, *DeltaboxUtil.TAGS.modloaderItemTag("leaves").toTypedArray())
      .compostable(0.3f)
      .blockstate { g, b -> g.leavesBlock(b.get(), blockId + suffix) }
      .loot { g, b -> g.leaves(b.get(), sapling) }
  }

  fun <T : Block> createPalmLeaves(): BlockBuilder<T> {
    return registrate
      .block<T>(blockId + suffix)
      .factory { c, p -> PalmLeavesBlock(p, c.flammabilityBurnChance, c.flammabilitySpreadChance) }
      .flammable(30, 60)
      .cutoutRender()
      .copyFrom { Blocks.OAK_LEAVES }
      .properties { c, p ->
        p.randomTicks()
          .noOcclusion()
          .isSuffocating { s, b, p -> false }
          .isViewBlocking { s, b, p -> false }
          .isRedstoneConductor { s, b, p -> false }
          .ignitedByLava()
      }
      .blockTags(
        BlockTags.LEAVES,
        BlockTags.MINEABLE_WITH_HOE,
        *DeltaboxUtil.TAGS.modloaderBlockTag("leaves").toTypedArray()
      )
      .itemTags(ItemTags.LEAVES, *DeltaboxUtil.TAGS.modloaderItemTag("leaves").toTypedArray())
      .compostable(0.3f)
      .blockstate { g, b -> g.leavesBlock(b.get(), blockId + suffix) }
      .loot { g, b -> g.leaves(b.get(), sapling) }
  }

  fun <T : Block> createBuddingLeaves(
    fruitBlock: Supplier<Block>
  ): BlockBuilder<T> {
    return registrate
      .block<T>(blockId + suffix)
      .factory { c, p -> BuddingLeavesBlock(p, fruitBlock, c.flammabilityBurnChance, c.flammabilitySpreadChance) }
      .flammable(30, 60)
      .cutoutRender()
      .copyFrom { Blocks.OAK_LEAVES }
      .properties { c, p ->
        p.randomTicks()
          .noOcclusion()
          .isSuffocating { s, b, p -> false }
          .isViewBlocking { s, b, p -> false }
          .isRedstoneConductor { s, b, p -> false }
          .ignitedByLava()
      }
      .blockTags(
        BlockTags.LEAVES,
        BlockTags.MINEABLE_WITH_HOE,
        *DeltaboxUtil.TAGS.modloaderBlockTag("leaves").toTypedArray()
      )
      .blockstate { g, b -> g.leavesBlock(b.get(), blockId + suffix) }
      .noItem()
      .loot { g, b -> g.leaves(b.get(), sapling) }
  }

  fun <T : Block> createCropLeaves(
    itemToDrop: Supplier<ItemLike>
  ): BlockBuilder<T> {
    return registrate
      .block<T>(blockId + suffix)
      .factory { c, p -> CropLeavesBlock(p, itemToDrop) }
      .flammable(30, 60)
      .cutoutRender()
      .copyFrom { Blocks.OAK_LEAVES }
      .properties { c, p ->
        p.randomTicks()
          .noOcclusion()
          .isSuffocating { s, b, p -> false }
          .isViewBlocking { s, b, p -> false }
          .isRedstoneConductor { s, b, p -> false }
          .ignitedByLava()
      }
      .blockstate { g, b -> g.cropLeavesBlock(b.get(), blockId + suffix) }
      .blockTags(
        BlockTags.LEAVES,
        BlockTags.MINEABLE_WITH_HOE,
        *DeltaboxUtil.TAGS.modloaderBlockTag("leaves").toTypedArray()
      )
      .noItem()
      .loot { g, b -> g.dropLeafCropLoot(b.get(), { itemToDrop.get() }, { sapling.get().asItem() }) }
  }
}
