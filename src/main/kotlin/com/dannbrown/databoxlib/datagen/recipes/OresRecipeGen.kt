package com.dannbrown.databoxlib.datagen.recipes

import com.dannbrown.databoxlib.init.ProjectBlocks
import com.dannbrown.databoxlib.init.ProjectItems
import com.dannbrown.databoxlib.lib.LibData
import com.simibubi.create.AllItems
import net.minecraft.data.DataGenerator
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks

class OresRecipeGen(generator: DataGenerator) : ProjectRecipeGen(generator) {
  override val recipeName = "Ores"

  // TIN
//  val TIN = oreRecipes(
//    LibData.NAMES.TIN,
//    DataboxItems.TIN_INGOT.get(),
//    DataboxBlocks.TIN_BLOCK.get(),
//    DataboxItems.TIN_NUGGET.get(),
//    null,
//    null,
//    DataboxItems.RAW_TIN.get(),
//    DataboxItems.CRUSHED_RAW_TIN.get(),
//    DataboxBlocks.RAW_TIN_BLOCK.get(),
//    listOf(
//      OreBlockProcessor(DataboxBlocks.TIN_ORE.get(), Blocks.COBBLESTONE, 1),
//      OreBlockProcessor(DataboxBlocks.DEEPSLATE_TIN_ORE.get(), Blocks.COBBLED_DEEPSLATE, 2),
//    ),
//    Items.GUNPOWDER
//  )
  // ALUMINIUM
  val ALUMINIUM = oreRecipes(
    LibData.NAMES.ALUMINIUM,
    ProjectItems.ALUMINIUM_INGOT.get(),
    ProjectBlocks.ALUMINIUM_BLOCK.get(),
    ProjectItems.ALUMINIUM_NUGGET.get(),
    null,
    ProjectItems.ALUMINIUM_SHEET.get(),
    ProjectItems.RAW_ALUMINIUM.get(),
    ProjectItems.CRUSHED_RAW_ALUMINIUM.get(),
    ProjectBlocks.RAW_ALUMINIUM_BLOCK.get(),
    listOf(
      OreBlockProcessor(ProjectBlocks.ALUMINIUM_ORE.get(), Blocks.COBBLESTONE, 1),
      OreBlockProcessor(ProjectBlocks.DEEPSLATE_ALUMINIUM_ORE.get(), Blocks.COBBLED_DEEPSLATE, 2),
      OreBlockProcessor(ProjectBlocks.ANORTHOSITE_ALUMINIUM_ORE.get(), ProjectBlocks.COBBLED_ANORTHOSITE.get(), 1),
      OreBlockProcessor(ProjectBlocks.MOONSLATE_ALUMINIUM_ORE.get(), ProjectBlocks.COBBLED_MOONSLATE.get(), 2),
    ),
    ProjectItems.PHOSPHORUS_POWDER.get()
  )

  // TUNGSTEN
  val TUNGSTEN = oreRecipes(
    LibData.NAMES.TUNGSTEN,
    ProjectItems.TUNGSTEN_INGOT.get(),
    ProjectBlocks.TUNGSTEN_BLOCK.get(),
    ProjectItems.TUNGSTEN_NUGGET.get(),
    null,
    null,
    ProjectItems.RAW_TUNGSTEN.get(),
    ProjectItems.CRUSHED_RAW_TUNGSTEN.get(),
    ProjectBlocks.RAW_TUNGSTEN_BLOCK.get(),
    listOf(
      OreBlockProcessor(ProjectBlocks.TUNGSTEN_ORE.get(), Blocks.COBBLESTONE, 1),
      OreBlockProcessor(ProjectBlocks.DEEPSLATE_TUNGSTEN_ORE.get(), Blocks.COBBLED_DEEPSLATE, 2),
      OreBlockProcessor(ProjectBlocks.ANORTHOSITE_TUNGSTEN_ORE.get(), ProjectBlocks.COBBLED_ANORTHOSITE.get(), 1),
      OreBlockProcessor(ProjectBlocks.MOONSLATE_TUNGSTEN_ORE.get(), ProjectBlocks.COBBLED_MOONSLATE.get(), 2),
    ),
    null
  )

  // IRON
  val IRON = oreRecipes(
    LibData.NAMES.IRON,
    Items.IRON_INGOT,
    null,
    Items.IRON_NUGGET,
    null,
    AllItems.IRON_SHEET.get(),
    null,
    AllItems.CRUSHED_IRON.get(),
    null,
    listOf(
      OreBlockProcessor(ProjectBlocks.RED_HEMATITE_IRON_ORE.get(), ProjectBlocks.COBBLED_RED_HEMATITE.get(), 1),
//      OreBlockProcessor(ModBlocks.GLACIO_IRON_ORE.get(), ModBlocks.GLACIO_COBBLESTONE.get(), 1),
//      OreBlockProcessor(ModBlocks.MERCURY_IRON_ORE.get(), ModBlocks.MERCURY_COBBLESTONE.get(), 1),
//      OreBlockProcessor(ModBlocks.MARS_IRON_ORE.get(), ModBlocks.MARS_COBBLESTONE.get(), 1),
    ),
    null,
  )

  // GOLD
  val GOLD = oreRecipes(
    LibData.NAMES.GOLD,
    Items.GOLD_INGOT,
    null,
    Items.GOLD_NUGGET,
    null,
    null,
    null,
    AllItems.CRUSHED_GOLD.get(),
    null,
    listOf(
//      OreBlockProcessor(ModBlocks.VENUS_GOLD_ORE.get(), ModBlocks.VENUS_COBBLESTONE.get(), 1),
    ),
    null,
  )

  // COPPER
  val COPPER = oreRecipes(
    LibData.NAMES.COPPER,
    Items.COPPER_INGOT,
    null,
    AllItems.COPPER_NUGGET.get(),
    null,
    null,
    null,
    AllItems.CRUSHED_COPPER.get(),
    null,
    listOf(
//      OreBlockProcessor(ModBlocks.GLACIO_COPPER_ORE.get(), ModBlocks.GLACIO_COBBLESTONE.get(), 1),
    ),
    null,
  )

  // COAL
  val COAL = oreRecipes(
    LibData.NAMES.COAL,
    null,
    null,
    null,
    null,
    null,
    null,
    Items.COAL,
    null,
    listOf(
//      OreBlockProcessor(ModBlocks.GLACIO_COAL_ORE.get(), ModBlocks.GLACIO_COBBLESTONE.get(), 5),
//      OreBlockProcessor(ModBlocks.VENUS_COAL_ORE.get(), ModBlocks.VENUS_COBBLESTONE.get(), 5),
    ),
    null,
  )

  // LAPIS_LAZULI
  val LAPIS_LAZULI = oreRecipes(
    LibData.NAMES.LAPIS_LAZULI,
    null,
    null,
    null,
    null,
    null,
    null,
    Items.LAPIS_LAZULI,
    null,
    listOf(
//      OreBlockProcessor(ModBlocks.GLACIO_LAPIS_ORE.get(), ModBlocks.GLACIO_COBBLESTONE.get(), 5),
    ),
    null,
  )

  // DIAMOND
  val DIAMOND = oreRecipes(
    LibData.NAMES.DIAMOND,
    null,
    null,
    null,
    null,
    null,
    null,
    Items.DIAMOND,
    null,
    listOf(
//      OreBlockProcessor(ModBlocks.VENUS_DIAMOND_ORE.get(), ModBlocks.VENUS_COBBLESTONE.get(), 1),
    ),
    null,
  )

  // STEEL
  val STEEL = oreRecipes(
    LibData.NAMES.STEEL,
    ProjectItems.STEEL_INGOT.get(),
    null,
    ProjectItems.STEEL_NUGGET.get(),
    null,
    ProjectItems.STEEL_SHEET.get(),
    null,
    null,
    null,
    null,
    null,
  )
  // TIN
//  val TIN_FROM_RAW = cooking({DataIngredient.tag(
//    LibTags.forgeItemTag("raw_materials/tin"))},
//    {DataboxItems.TUNGSTEN_INGOT.get()}){ b -> b
//    .suffix("_from_raw")
//    .comboOreSmelting(200, 0.5f)
//  }
//
//  val TIN_FROM_RAW_BLOCK = cooking({DataIngredient.tag(
//    LibTags.forgeItemTag("storage_blocks/raw_tin"))},
//    {DataboxBlocks.ALUMINIUM_BLOCK.get()}){ b -> b
//    .suffix("_from_raw_block")
//    .comboOreSmelting(400, 4.5f)
//  }
//
//  val SMELT_TIN_FROM_CRUSHED = cooking({DataIngredient.tag(
//    LibTags.createItemTag("crushed_raw_tin"))},
//    {DataboxItems.ALUMINIUM_INGOT.get()}){ b -> b
//    .suffix("_from_crushed_raw")
//    .comboOreSmelting(200, 1f)
//  }
//
//  val WASH_TIN_FROM_CRUSHED = create("tin_from_crushed_raw") { b -> b
//    .splashing() { b -> b
//      .require(DataIngredient.tag(LibTags.createItemTag("crushed_raw_tin")))
//      .output(DataboxItems.TIN_NUGGET.get(), 9)
//      .output(0.25f, Items.GUNPOWDER, 1)
//    }
//  }
//
//
//  val ALUMINIUM_FROM_RAW = cooking({DataIngredient.tag(
//    LibTags.forgeItemTag("raw_materials/aluminium"))},
//    {DataboxItems.TIN_INGOT.get()}){ b -> b
//    .suffix("_from_raw")
//    .comboOreSmelting(200, 0.5f)
//  }
//
//  val SMELT_ALUMINIUM_FROM_CRUSHED = cooking({DataIngredient.tag(
//    LibTags.createItemTag("crushed_raw_aluminium"))},
//    {DataboxItems.TIN_INGOT.get()}){ b -> b
//    .suffix("_from_crushed_raw")
//    .comboOreSmelting(200, 1f)
//  }
//
//
//  val WASH_ALUMINIUM_FROM_CRUSHED = create("aluminium_from_crushed_raw") { b -> b
//    .splashing() { b -> b
//      .require(DataIngredient.tag(LibTags.createItemTag("crushed_raw_aluminium")))
//      .output(DataboxItems.ALUMINIUM_NUGGET.get(), 9)
//      .output(0.25f, DataboxItems.PHOSPHORUS_POWDER.get(), 1)
//    }
//  }
//
//  // CRUSHED DESH, OSTRUM, CALORITE
//
//  val SMELT_DESH_FROM_CRUSHED = cooking({DataIngredient.tag(
//    LibTags.createItemTag("crushed_raw_desh"))},
//    {ModItems.DESH_INGOT.get()}){ b -> b
//    .suffix("_from_crushed_raw")
//    .comboOreSmelting(200, 1f)
//  }
//
//  val SMELT_OSTRUM_FROM_CRUSHED = cooking({DataIngredient.tag(
//    LibTags.createItemTag("crushed_raw_ostrum"))},
//    {ModItems.OSTRUM_INGOT.get()}){ b -> b
//    .suffix("_from_crushed_raw")
//    .comboOreSmelting(200, 1f)
//  }
//
//  val SMELT_CALORITE_FROM_CRUSHED = cooking({DataIngredient.tag(
//    LibTags.createItemTag("crushed_raw_calorite"))},
//    {ModItems.CALORITE_INGOT.get()}){ b -> b
//    .suffix("_from_crushed_raw")
//    .comboOreSmelting(200, 1f)
//  }
//
//  val WASH_DESH_FROM_CRUSHED = create("desh_from_crushed_raw") { b -> b
//    .splashing() { b -> b
//      .require(DataIngredient.tag(LibTags.createItemTag("crushed_raw_desh")))
//      .output(ModItems.DESH_NUGGET.get(), 9)
//      .output(0.25f, Items.GLOWSTONE_DUST, 1)
//    }
//  }
//
//  val WASH_OSTRUM_FROM_CRUSHED = create("ostrum_from_crushed_raw") { b -> b
//    .splashing() { b -> b
//      .require(DataIngredient.tag(LibTags.createItemTag("crushed_raw_ostrum")))
//      .output(ModItems.OSTRUM_NUGGET.get(), 9)
//      .output(0.25f, AllItems.CINDER_FLOUR.get(), 1)
//    }
//  }
//
//  val WASH_CALORITE_FROM_CRUSHED = create("calorite_from_crushed_raw") { b -> b
//    .splashing() { b -> b
//      .require(DataIngredient.tag(LibTags.createItemTag("crushed_raw_calorite")))
//      .output(ModItems.CALORITE_NUGGET.get(), 9)
//      .output(0.25f, Items.BLAZE_POWDER, 1)
//    }
//  }
//
//  // DUST TO INGOT
//
//  val SMELT_IRON_DUST = cooking({DataIngredient.tag(
//    LibTags.forgeItemTag("dusts/iron"))},
//    {Items.IRON_INGOT}){ b -> b
//    .suffix("_from_dust")
//    .comboOreSmelting(200, 0.5f)
//  }
//
//  val SMELT_STEEL_BLEND = cooking({DataIngredient.tag(
//    LibTags.forgeItemTag("dusts/steel"))},
//    {ModItems.STEEL_INGOT.get()}){ b -> b
//    .suffix("_from_dust")
//    .inBlastFurnace(200, 1f)
//  }
}