package com.dannbrown.databoxlib.datagen.recipes

import com.dannbrown.databoxlib.init.ProjectBlocks
import com.dannbrown.databoxlib.init.ProjectItems
import com.dannbrown.databoxlib.lib.LibData
import com.tterrag.registrate.util.DataIngredient
import net.minecraft.data.DataGenerator
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Blocks

class CasingsRecipeGen(generator: DataGenerator): ProjectRecipeGen(generator) {
  override val recipeName = "Casings"

  val STEEL_CASING = create(LibData.BLOCKS.STEEL_CASING) { b -> b
    .comboCasing(
      { ItemLike { ProjectBlocks.STEEL_FAMILY.MAIN!!.get().asItem() } },
      {DataIngredient.items(Blocks.IRON_BLOCK)},
      {DataIngredient.items(ProjectItems.STEEL_INGOT.get())},
    )
  }

  val STEEL_PURPLE_PANEL_CASING = create(LibData.BLOCKS.STEEL_PURPLE_PANEL_CASING) { b -> b
    .comboCasing(
      { ProjectBlocks.STEEL_PURPLE_PANEL_CASING.get() },
      {DataIngredient.items( ProjectBlocks.STEEL_FAMILY.MAIN!!.get().asItem())},
      {DataIngredient.items(Items.AMETHYST_SHARD)},
    )
  }

  val STEEL_BLUE_PANEL_CASING = create(LibData.BLOCKS.STEEL_BLUE_PANEL_CASING) { b -> b
    .comboCasing(
      { ProjectBlocks.STEEL_BLUE_PANEL_CASING.get() },
      {DataIngredient.items( ProjectBlocks.STEEL_FAMILY.MAIN!!.get().asItem())},
      {DataIngredient.items(Items.DIAMOND)},
    )
  }

  val STEEL_GREEN_PANEL_CASING = create(LibData.BLOCKS.STEEL_GREEN_PANEL_CASING) { b -> b
    .comboCasing(
      { ProjectBlocks.STEEL_GREEN_PANEL_CASING.get() },
      {DataIngredient.items( ProjectBlocks.STEEL_FAMILY.MAIN!!.get().asItem())},
      {DataIngredient.items(Items.EMERALD)},
    )
  }

  val STEEL_ORANGE_PANEL_CASING = create(LibData.BLOCKS.STEEL_ORANGE_PANEL_CASING) { b -> b
    .comboCasing(
      { ProjectBlocks.STEEL_ORANGE_PANEL_CASING.get() },
      {DataIngredient.items( ProjectBlocks.STEEL_FAMILY.MAIN!!.get().asItem())},
      {DataIngredient.items(Items.GOLD_INGOT)},

    )
  }

  val STEEL_RED_PANEL_CASING = create(LibData.BLOCKS.STEEL_RED_PANEL_CASING) { b -> b
    .comboCasing(
      { ProjectBlocks.STEEL_RED_PANEL_CASING.get() },
      {DataIngredient.items( ProjectBlocks.STEEL_FAMILY.MAIN!!.get().asItem())},
      {DataIngredient.items(Items.REDSTONE)},
    )
  }

//  val DESH_CASING = create(LibData.BLOCKS.DESH_CASING) { b -> b
//    .comboCasing(
//      { ItemLike {DataboxBlocks.DESH_FAMILY.MAIN!!.get().asItem()} },
//      {DataIngredient.items(ModBlocks.IRON_PLATING.get())},
//      {DataIngredient.items(ModItems.DESH_INGOT.get())},
//    )
//  }
//
//  val DESH_PANEL_CASING = create(LibData.BLOCKS.DESH_PANEL_CASING) { b -> b
//    .comboCasing(
//      { ItemLike {DataboxBlocks.DESH_FAMILY.LAMP!!.get().asItem()} },
//      {DataIngredient.items(DataboxBlocks.DESH_FAMILY.MAIN!!.get().asItem())},
//      {DataIngredient.items(Items.REDSTONE)},
//    )
//  }
//
//  val OSTRUM_CASING = create(LibData.BLOCKS.OSTRUM_CASING) { b -> b
//    .comboCasing(
//      { ItemLike {DataboxBlocks.OSTRUM_FAMILY.MAIN!!.get().asItem()} },
//      {DataIngredient.items(ModBlocks.IRON_PLATING.get())},
//      {DataIngredient.items(ModItems.OSTRUM_INGOT.get())},
//    )
//  }
//
//  val OSTRUM_PANEL_CASING = create(LibData.BLOCKS.OSTRUM_PANEL_CASING) { b -> b
//    .comboCasing(
//      { ItemLike {DataboxBlocks.OSTRUM_FAMILY.LAMP!!.get().asItem()} },
//      {DataIngredient.items(DataboxBlocks.OSTRUM_FAMILY.MAIN!!.get().asItem())},
//      {DataIngredient.items(Items.EMERALD)},
//    )
//  }
//
//  val CALORITE_CASING = create(LibData.BLOCKS.CALORITE_CASING) { b -> b
//    .comboCasing(
//      { ItemLike {DataboxBlocks.CALORITE_FAMILY.MAIN!!.get().asItem()} },
//      {DataIngredient.items(ModBlocks.IRON_PLATING.get())},
//      {DataIngredient.items(ModItems.CALORITE_INGOT.get())},
//    )
//  }
//
//  val CALORITE_PANEL_CASING = create(LibData.BLOCKS.CALORITE_PANEL_CASING) { b -> b
//    .comboCasing(
//      { ItemLike {DataboxBlocks.CALORITE_FAMILY.LAMP!!.get().asItem()} },
//      {DataIngredient.items(DataboxBlocks.CALORITE_FAMILY.MAIN!!.get().asItem())},
//      {DataIngredient.items(Items.GOLD_INGOT)},
//    )
//  }

}