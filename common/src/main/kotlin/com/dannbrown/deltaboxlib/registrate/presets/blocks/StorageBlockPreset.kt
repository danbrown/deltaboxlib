package com.dannbrown.deltaboxlib.registrate.presets.blocks

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.builders.BlockBuilder
import com.dannbrown.deltaboxlib.registrate.presets.tags.BlockTagPresets
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import java.util.function.Supplier

class StorageBlockPreset(
  val registrate: AbstractDeltaboxRegistrate,
  val blockId: String,
  val ingotItem: Supplier<ItemLike>,
  val ingredient: Supplier<Ingredient>,
  val addSuffix: Boolean = true
) : IBlockBuilderPreset(registrate, blockId) {
  fun <T : Block> create(): BlockBuilder<T> {
    return registrate.block<T>(blockId)
      .suffix(
        if (addSuffix) {
          "_block"
        } else {
          ""
        }
      )
      .blockTags(*BlockTagPresets.storageBlockTags(blockId).first.toTypedArray())
      .recipe { r, b -> r.storageBlockRecipe({ b.get() }, ingotItem, ingredient) }
      .itemTags(*BlockTagPresets.storageBlockTags(blockId).second.toTypedArray())
  }

  fun <T : Block> createSmall(): BlockBuilder<T> {
    return registrate.block<T>(blockId)
      .suffix(
        if (addSuffix) {
          "_block"
        } else {
          ""
        }
      )
      .blockTags(*BlockTagPresets.storageBlockTags(blockId).first.toTypedArray())
      .recipe { r, b -> r.smallStorageBlockRecipe({ b.get() }, ingotItem, ingredient) }
      .itemTags(*BlockTagPresets.storageBlockTags(blockId).second.toTypedArray())
  }
}