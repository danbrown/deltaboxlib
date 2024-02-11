package com.dannbrown.databoxlib.datagen.recipes

import com.dannbrown.databoxlib.content.utils.ProjectHeat
import com.dannbrown.databoxlib.init.ProjectBlocks
import com.dannbrown.databoxlib.init.ProjectFluids
import com.dannbrown.databoxlib.init.ProjectItems
import com.dannbrown.databoxlib.lib.LibTags
import com.simibubi.create.AllItems
import net.minecraft.data.DataGenerator
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.material.Fluids

class CompactingRecipeGen(generator: DataGenerator) : ProjectRecipeGen(generator) {
  override val recipeName = "Compacting"
  val DIAMOND_CARBON_GRIT = create("diamond_from_carbon_grit") { b ->
    b
      .compacting { b ->
        b
          .require(ProjectItems.CARBON_GRIT.get())
          .require(ProjectItems.CARBON_GRIT.get())
          .require(ProjectItems.CARBON_GRIT.get())
          .require(ProjectItems.CARBON_GRIT.get())
          .require(ProjectItems.CARBON_GRIT.get())
          .require(ProjectItems.CARBON_GRIT.get())
          .require(ProjectItems.CARBON_GRIT.get())
          .require(ProjectItems.CARBON_GRIT.get())
          .require(ProjectItems.CARBON_GRIT.get())
          .require(ProjectItems.CARBON_GRIT.get())
          .require(ProjectItems.CARBON_GRIT.get())
          .require(ProjectItems.CARBON_GRIT.get())
          .require(ProjectItems.CARBON_GRIT.get())
          .require(ProjectItems.CARBON_GRIT.get())
          .require(ProjectItems.CARBON_GRIT.get())
          .require(ProjectItems.CARBON_GRIT.get())
          .require(ProjectItems.CARBON_GRIT.get())
          .require(ProjectItems.CARBON_GRIT.get())
          .require(ProjectItems.CARBON_GRIT.get())
          .require(ProjectItems.CARBON_GRIT.get())
          .require(ProjectItems.CARBON_GRIT.get())
          .require(ProjectItems.CARBON_GRIT.get())
          .require(ProjectItems.CARBON_GRIT.get())
          .require(ProjectItems.CARBON_GRIT.get())
          .require(ProjectItems.CARBON_GRIT.get())
          .require(ProjectItems.CARBON_GRIT.get())
          .require(ProjectItems.CARBON_GRIT.get())
          .require(ProjectItems.CARBON_GRIT.get())
          .require(ProjectItems.CARBON_GRIT.get())
          .require(ProjectItems.CARBON_GRIT.get())
          .require(ProjectItems.CARBON_GRIT.get())
          .require(ProjectItems.CARBON_GRIT.get())
          .require(Fluids.LAVA, 1000)
          .output(ProjectItems.RAW_GRAPHITE.get(), 1)
          .output(0.125f, Items.DIAMOND, 1)
      }
  }
  val PLANT_OIL_FROM_SAPLINGS = create("plant_oil_from_saplings") { b ->
    b
      .compacting() { b ->
        b
          .require(LibTags.vanillaItemTag("saplings"))
          .require(LibTags.vanillaItemTag("saplings"))
          .requiresHeat(ProjectHeat.PASSIVEHEATED)
          .output(ProjectFluids.PLANT_OIL.get(), 50)
      }
  }
  val PLANT_OIL_FROM_SEEDS = create("plant_oil_from_seeds") { b ->
    b
      .compacting() { b ->
        b
          .require(LibTags.forgeItemTag("seeds"))
          .require(LibTags.forgeItemTag("seeds"))
          .requiresHeat(ProjectHeat.PASSIVEHEATED)
          .output(ProjectFluids.PLANT_OIL.get(), 50)
      }
  }
  val PHOSPHORITE_RENEWABLE = create("phosphorite_renewable") { b ->
    b
      .compacting { b ->
        b
          .require(LibTags.forgeItemTag("dusts/phosphorus"))
          .require(LibTags.forgeItemTag("dusts/phosphorus"))
          .require(Blocks.COBBLESTONE)
          .output(ProjectBlocks.PHOSPHORITE_FAMILY.MAIN!!.get(), 1)
      }
  }
  val PYRITE_RENEWABLE = create("pyrite_renewable") { b ->
    b
      .compacting { b ->
        b
          .require(LibTags.forgeItemTag("dusts/sulphur"))
          .require(LibTags.forgeItemTag("dusts/sulphur"))
          .require(LibTags.forgeItemTag("nuggets/iron"))
          .require(Blocks.COBBLESTONE)
          .output(ProjectBlocks.PYRITE_FAMILY.MAIN!!.get(), 1)
      }
  }
  val INK_FLUID_FROM_DYE = create("ink_fluid_from_dye") { b ->
    b
      .compacting { b ->
        b
          .require(Items.BLACK_DYE)
          .output(ProjectFluids.INK.get(), 50)
      }
  }
  val LATEX_FROM_GUAYULE_SHRUBS = create("latex_from_guayule_shrubs") { b ->
    b
      .compacting { b ->
        b
          .require(ProjectBlocks.GUAYULE_SHRUB.get())
          .require(ProjectBlocks.GUAYULE_SHRUB.get())
          .require(ProjectBlocks.GUAYULE_SHRUB.get())
          .require(ProjectBlocks.GUAYULE_SHRUB.get())
          .require(ProjectBlocks.GUAYULE_SHRUB.get())
          .require(ProjectBlocks.GUAYULE_SHRUB.get())
          .require(ProjectBlocks.GUAYULE_SHRUB.get())
          .require(ProjectBlocks.GUAYULE_SHRUB.get())
          .require(ProjectBlocks.GUAYULE_SHRUB.get())
          .require(ProjectBlocks.GUAYULE_SHRUB.get())
          .requiresHeat(ProjectHeat.PASSIVEHEATED)
          .output(ProjectFluids.LATEX.get(), 100)
      }
  }
  val PAPER_FROM_COTTON_PULPS = create("paper_from_cotton_pulps") { b ->
    b
      .compacting { b ->
        b
          .require(ProjectItems.COTTON_PULP.get())
          .require(ProjectItems.COTTON_PULP.get())
          .require(ProjectItems.COTTON_PULP.get())
          .require(ProjectItems.COTTON_PULP.get())
          .output(Items.PAPER, 1)
      }
  }
  val SANDSTONE = create("sandstone_from_pebbles") { b ->
    b
      .compacting { b ->
        b
          .require(ProjectBlocks.SANDSTONE_PEBBLES.get())
          .require(ProjectBlocks.SANDSTONE_PEBBLES.get())
          .output(Blocks.SANDSTONE, 1)
      }
  }
  val RED_SANSTONE = create("red_sandstone_from_pebbles") { b ->
    b
      .compacting { b ->
        b
          .require(ProjectBlocks.RED_SANDSTONE_PEBBLES.get())
          .require(ProjectBlocks.RED_SANDSTONE_PEBBLES.get())
          .output(Blocks.RED_SANDSTONE, 1)
      }
  }
  val ROSEATE_SANDSTONE = create("roseate_sandstone_from_pebbles") { b ->
    b
      .compacting { b ->
        b
          .require(ProjectBlocks.ROSEATE_SANDSTONE_PEBBLES.get())
          .require(ProjectBlocks.ROSEATE_SANDSTONE_PEBBLES.get())
          .output(ProjectBlocks.ROSEATE_FAMILY.SANDSTONE!!.get(), 1)
      }
  }
  val RED_HEMATITE_RENEWABLE = create("red_hematite_renewable") { b ->
    b
      .compacting { b ->
        b
          .require(LibTags.forgeItemTag("nuggets/iron"))
          .require(Blocks.COBBLESTONE)
          .require(Fluids.LAVA, 100)
          .output(ProjectBlocks.RED_HEMATITE.get(), 1)
      }
  }
  val BROWN_SLATE_RENEWABLE = create("brown_slate_renewable") { b ->
    b
      .compacting { b ->
        b
          .require(Blocks.DEEPSLATE)
          .require(Blocks.DIRT)
          .require(Items.CLAY_BALL)
          .output(ProjectBlocks.BROWN_SLATE.get(), 1)
      }
  }
  val BRASS_NUGGET_PASSIVE_HEATED = create("brass_nugget_passive_heated") { b ->
    b
      .compacting { b ->
        b
          .require(AllItems.COPPER_NUGGET.get())
          .require(AllItems.ZINC_NUGGET.get())
          .requiresHeat(ProjectHeat.PASSIVEHEATED)
          .output(AllItems.BRASS_NUGGET.get(), 1)
      }
  }
}