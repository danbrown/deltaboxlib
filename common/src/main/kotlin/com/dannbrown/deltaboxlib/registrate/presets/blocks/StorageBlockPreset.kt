package com.dannbrown.deltaboxlib.registrate.presets.blocks

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.builders.BlockBuilder
import com.dannbrown.deltaboxlib.registrate.presets.tags.BlockTagPresets
import com.dannbrown.deltaboxlib.registrate.util.DataIngredient
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import java.util.function.Supplier

class StorageBlockPreset(
  val registrate: AbstractDeltaboxRegistrate,
  val blockId: String,
  val ingotItem: Supplier<ItemLike>,
  val ingredient: Supplier<DataIngredient>,
  val suffix: String = "_block"
) : IBlockBuilderPreset(registrate, blockId) {
  fun <T : Block> create(): BlockBuilder<T> {
    val nameWithSuffix = "${blockId}$suffix"
    return registrate.block<T>(nameWithSuffix)
      .blockTags(*BlockTagPresets.storageBlockTags(nameWithSuffix).first.toTypedArray())
      .recipe { r, b -> r.storageBlockRecipe({ b.get() }, ingotItem, ingredient) }
      .itemTags(*BlockTagPresets.storageBlockTags(nameWithSuffix).second.toTypedArray())
  }

  fun <T : Block> createSmall(): BlockBuilder<T> {
    val nameWithSuffix = "${blockId}$suffix"
    return registrate.block<T>(nameWithSuffix)
      .blockTags(*BlockTagPresets.storageBlockTags(nameWithSuffix).first.toTypedArray())
      .recipe { r, b -> r.smallStorageBlockRecipe({ b.get() }, ingotItem, ingredient) }
      .itemTags(*BlockTagPresets.storageBlockTags(nameWithSuffix).second.toTypedArray())
  }
}