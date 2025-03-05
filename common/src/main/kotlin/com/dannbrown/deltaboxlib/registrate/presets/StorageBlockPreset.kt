package com.dannbrown.deltaboxlib.registrate.presets

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.builders.BlockBuilder
import com.dannbrown.deltaboxlib.registrate.presets.tags.BlockTagPresets
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import java.util.function.Supplier

class StorageBlockPreset<T : Block>(
  val registrate: AbstractDeltaboxRegistrate,
  val _name: String,
  val ingotItem: Supplier<ItemLike>,
  val ingredient: Supplier<Ingredient>,
  val addSuffix: Boolean = true
) : IBlockBuilderPreset<T>(registrate) {
  fun create(): BlockBuilder<T> {
    return registrate.block<T>(_name)
      .suffix(
        if (addSuffix) {
          "_block"
        } else {
          ""
        }
      )
      .blockTags(*BlockTagPresets.storageBlockTags(_name).first.toTypedArray())
      .recipe { r, b -> r.storageBlockRecipe({ b.get() }, ingotItem, ingredient) }
      .itemTags(*BlockTagPresets.storageBlockTags(_name).second.toTypedArray())
  }

  fun createSmall(generator: AbstractDeltaboxRegistrate): BlockBuilder<T> {
    return generator.block<T>(_name)
      .suffix(
        if (addSuffix) {
          "_block"
        } else {
          ""
        }
      )
      .blockTags(*BlockTagPresets.storageBlockTags(_name).first.toTypedArray())
      .recipe { r, b -> r.smallStorageBlockRecipe({ b.get() }, ingotItem, ingredient) }
      .itemTags(*BlockTagPresets.storageBlockTags(_name).second.toTypedArray())
  }
}