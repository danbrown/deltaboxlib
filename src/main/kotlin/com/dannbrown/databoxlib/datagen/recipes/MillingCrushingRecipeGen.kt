package com.dannbrown.databoxlib.datagen.recipes

import com.dannbrown.databoxlib.init.ProjectBlocks
import com.dannbrown.databoxlib.init.ProjectItems
import net.minecraft.data.DataGenerator
import net.minecraft.world.item.Items

class MillingCrushingRecipeGen(generator: DataGenerator) : ProjectRecipeGen(generator) {
  override val recipeName = "Milling and Crushing"

  val SAND_TO_SILT = create("sand_to_silt") { b ->
    b
      .milling { b ->
        b
          .require(Items.SAND)
          .duration(60)
          .output(ProjectBlocks.SILT.get(), 1)
      }
      .crushing { b ->
        b
          .require(Items.SAND)
          .duration(60)
          .output(ProjectBlocks.SILT.get(), 1)
      }
  }

  val HIMALAYAN_SALT_TO_MAGNESIUM = create("himalayan_salt_to_magnesium") { b ->
    b
      .milling { b ->
        b
          .require(ProjectItems.HIMALAYAN_SALT.get())
          .duration(60)
          .output(ProjectItems.MAGNESIUM_DUST.get(), 1)
          .output(ProjectItems.SALT.get(), 1)
      }
      .crushing { b ->
        b
          .require(ProjectItems.HIMALAYAN_SALT.get())
          .duration(60)
          .output(ProjectItems.MAGNESIUM_DUST.get(), 1)
          .output(ProjectItems.SALT.get(), 1)
      }
  }


  val PHOSPHORUS_PROCESSING = create("phosphorus_processing") { b ->
    b
      .milling { b ->
        b
          .require(ProjectBlocks.PHOSPHORITE_FAMILY.MAIN!!.get())
          .duration(60)
          .output(ProjectItems.PHOSPHORUS_POWDER.get(), 1)
      }
      .crushing { b ->
        b
          .require(ProjectBlocks.PHOSPHORITE_FAMILY.MAIN!!.get())
          .duration(60)
          .output(ProjectItems.PHOSPHORUS_POWDER.get(), 2)
          .output(0.33f, ProjectItems.PHOSPHORUS_POWDER.get(), 1)
      }
  }

  val SULPHUR_PROCESSING = create("sulphur_processing") { b ->
    b
      .milling { b ->
        b
          .require(ProjectBlocks.PYRITE_FAMILY.MAIN!!.get())
          .duration(60)
          .output(ProjectItems.SULPHUR.get(), 1)
      }
      .crushing { b ->
        b
          .require(ProjectBlocks.PYRITE_FAMILY.MAIN!!.get())
          .duration(60)
          .output(ProjectItems.SULPHUR.get(), 2)
          .output(0.33f, ProjectItems.SULPHUR.get(), 1)
          .output(0.15f, Items.IRON_NUGGET, 1)
      }
  }

  val CARBON_GRIT = create("carbon_grit_processing") { b ->
    b
      .milling { b ->
        b
          .require(ProjectItems.COAL_COKE.get())
          .duration(60)
          .output(ProjectItems.CARBON_GRIT.get(), 1)
          .output(0.5f, Items.BLACK_DYE, 1)
      }
      .crushing { b ->
        b
          .require(ProjectItems.COAL_COKE.get())
          .duration(60)
          .output(ProjectItems.CARBON_GRIT.get(), 1)
          .output(0.25f, ProjectItems.CARBON_GRIT.get(), 1)
          .output(0.5f, Items.BLACK_DYE, 1)
      }
  }


  val DYES_FROM_OCOTILLO = create("dyes_from_ocotillo") { b ->
    b
      .milling { b ->
        b
          .require(ProjectBlocks.OCOTILLO.get())
          .duration(60)
          .output(Items.ORANGE_DYE, 1)
      }
      .crushing { b ->
        b
          .require(ProjectBlocks.OCOTILLO.get())
          .duration(60)
          .output(Items.ORANGE_DYE, 1)
          .output(0.50f, Items.RED_DYE, 1)
          .output(0.125f, Items.GREEN_DYE, 1)
      }
  }


  val SANDSTONE_PEBBLES_TO_SAND = create("sandstone_pebbles_to_sand") { b ->
    b
      .milling { b ->
        b
          .require(ProjectBlocks.SANDSTONE_PEBBLES.get())
          .duration(60)
          .output(Items.SAND, 1)
      }
      .crushing { b ->
        b
          .require(ProjectBlocks.SANDSTONE_PEBBLES.get())
          .duration(60)
          .output(Items.SAND, 1)
      }
  }

  val RED_SANDSTONE_PEBBLES_TO_SAND = create("red_sandstone_pebbles_to_sand") { b ->
    b
      .milling { b ->
        b
          .require(ProjectBlocks.RED_SANDSTONE_PEBBLES.get())
          .duration(60)
          .output(Items.RED_SAND, 1)
      }
      .crushing { b ->
        b
          .require(ProjectBlocks.RED_SANDSTONE_PEBBLES.get())
          .duration(60)
          .output(Items.RED_SAND, 1)
      }
  }

  val ROSEATE_SANDSTONE_PEBBLES_TO_SAND = create("roseate_sandstone_pebbles_to_sand") { b ->
    b
      .milling { b ->
        b
          .require(ProjectBlocks.ROSEATE_SANDSTONE_PEBBLES.get())
          .duration(60)
          .output(ProjectBlocks.ROSEATE_GRAINS.get(), 1)
      }
      .crushing { b ->
        b
          .require(ProjectBlocks.ROSEATE_SANDSTONE_PEBBLES.get())
          .duration(60)
          .output(ProjectBlocks.ROSEATE_GRAINS.get(), 1)
      }
  }

  val RED_HEMATITE_TO_COBBLE = create("red_hematite_to_cobble") { b ->
    b
      .milling { b ->
        b
          .require(ProjectBlocks.RED_HEMATITE.get())
          .duration(60)
          .output(ProjectBlocks.COBBLED_RED_HEMATITE.get(), 1)
      }
      .crushing { b ->
        b
          .require(ProjectBlocks.RED_HEMATITE.get())
          .duration(60)
          .output(ProjectBlocks.COBBLED_RED_HEMATITE.get(), 1)
      }
  }

  val COBBLE_RED_HEMATITE_PROCESSING = create("cobble_red_hematite_processing") { b ->
    b
      .milling { b ->
        b
          .require(ProjectBlocks.COBBLED_RED_HEMATITE.get())
          .duration(60)
          .output(Items.IRON_NUGGET, 1)
      }
      .crushing { b ->
        b
          .require(ProjectBlocks.COBBLED_RED_HEMATITE.get())
          .duration(60)
          .output(Items.IRON_NUGGET, 1)
          .output(0.25f, Items.IRON_NUGGET, 1)
      }
  }

}