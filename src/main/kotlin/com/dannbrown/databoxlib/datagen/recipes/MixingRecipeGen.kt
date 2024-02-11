package com.dannbrown.databoxlib.datagen.recipes

import com.dannbrown.databoxlib.content.utils.ProjectHeat
import com.dannbrown.databoxlib.init.ProjectBlocks
import com.dannbrown.databoxlib.init.ProjectFluids
import com.dannbrown.databoxlib.init.ProjectItems
import com.dannbrown.databoxlib.lib.LibTags
import com.simibubi.create.AllItems
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes
import com.simibubi.create.content.processing.recipe.HeatCondition
import net.minecraft.data.DataGenerator
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.material.Fluids

class MixingRecipeGen(generator: DataGenerator) : ProjectRecipeGen(generator) {
  override val recipeName = "Mixing"

  //  val BRONZE_INGOT = create("bronze_ingot") { b ->
//    b
//      .mixing { b ->
//        b
//          .require(LibTags.forgeItemTag("ingots/copper"))
//          .require(LibTags.forgeItemTag("ingots/copper"))
//          .require(LibTags.forgeItemTag("ingots/copper"))
//          .require(LibTags.forgeItemTag("ingots/tin"))
//          .requiresHeat(HeatCondition.HEATED)
//          .output(DataboxItems.BRONZE_INGOT.get(), 1)
//      }
//  }
  val SULFURIC_ACID = create("sulfuric_acid") { b ->
    b
      .mixing { b ->
        b
          .require(LibTags.forgeItemTag("dusts/sulphur"))
          .require(LibTags.forgeItemTag("dusts/sulphur"))
          .require(LibTags.forgeFluidTag("oxygen"), 1000)
          .require(LibTags.forgeFluidTag("hydrogen"), 500)
          .requiresHeat(HeatCondition.HEATED)
          .output(ProjectFluids.SULFURIC_ACID.get(), 1000)
      }
  }
  val MAGMATIC_SILICA_GEL = create("magmatic_silica_gel") { b ->
    b
      .mixing { b ->
        b
          .require(LibTags.forgeItemTag("storage_blocks/silica"))
          .require(Items.MAGMA_CREAM)
          .require(Items.MAGMA_CREAM)
          .require(Items.MAGMA_CREAM)
          .require(Items.MAGMA_CREAM)
          .require(LibTags.forgeFluidTag("sulfuric_acid"), 1000)
          .requiresHeat(HeatCondition.HEATED)
          .output(ProjectBlocks.MAGMATIC_SILICA_GEL.get(), 1)
      }
  }
  val SILICA_GEL = create("silica_gel") { b ->
    b
      .mixing { b ->
        b
          .require(LibTags.forgeItemTag("storage_blocks/silica"))
          .require(Items.SLIME_BALL)
          .require(Items.SLIME_BALL)
          .require(Items.SLIME_BALL)
          .require(Items.SLIME_BALL)
          .require(LibTags.forgeFluidTag("sulfuric_acid"), 1000)
          .requiresHeat(HeatCondition.HEATED)
          .output(ProjectBlocks.SILICA_GEL.get(), 1)
      }
  }
  val SUGAR_FROM_BEETROOT = create("sugar_from_beetroot") { b ->
    b
      .mixing { b ->
        b
          .require(Items.BEETROOT)
          .requiresHeat(ProjectHeat.PASSIVEHEATED)
          .output(0.125f, Items.SUGAR, 1)
      }
  }
  val COAL_COKE_CHARCOAL = create("coal_coke_from_charcoal") { b ->
    b
      .mixing { b ->
        b
          .require(Items.CHARCOAL)
          .require(Items.CHARCOAL)
          .require(Items.CHARCOAL)
          .require(Items.CHARCOAL)
          .require(Items.CHARCOAL)
          .require(Items.CHARCOAL)
          .require(Items.CHARCOAL)
          .require(Items.CHARCOAL)
          .require(Items.CHARCOAL)
          .require(Fluids.WATER, 500)
          .requiresHeat(ProjectHeat.PASSIVEHEATED)
          .output(ProjectItems.COAL_COKE.get(), 1)
          .output(ProjectFluids.CREOSOTE_OIL.get(), 25)
      }
  }
  val COAL_COKE_COAL = create("coal_coke_from_coal") { b ->
    b
      .mixing { b ->
        b
          .require(Items.COAL)
          .require(Items.COAL)
          .require(Items.COAL)
          .require(Items.COAL)
          .require(Items.COAL)
          .require(Items.COAL)
          .require(Items.COAL)
          .require(Items.COAL)
          .require(Items.COAL)
          .require(Fluids.WATER, 500)
          .requiresHeat(ProjectHeat.PASSIVEHEATED)
          .output(ProjectItems.COAL_COKE.get(), 1)
          .output(ProjectFluids.CREOSOTE_OIL.get(), 25)
      }
  }
  val STEEL_BLEND = create("steel_blend") { b ->
    b
      .mixing { b ->
        b
          .require(AllPaletteStoneTypes.LIMESTONE.baseBlock.get())
          .require(AllItems.CRUSHED_IRON.get())
          .require(LibTags.forgeItemTag("dusts/carbon"))
          .requiresHeat(ProjectHeat.PASSIVEHEATED)
          .output(ProjectItems.STEEL_BLEND.get(), 1)
      }
  }
  val RUBBER_FROM_CONDENSED_LATEX = create("rubber_from_condensed_latex") { b ->
    b
      .mixing { b ->
        b
          .require(ProjectItems.CONDENSED_LATEX.get())
          .require(Items.BLACK_DYE)
          .output(ProjectItems.RUBBER.get(), 1)
      }
  }
  val SLIME_BALL_FROM_CONDENSED_LATEX = create("slimeball_from_condensed_latex") { b ->
    b
      .mixing { b ->
        b
          .require(ProjectItems.CONDENSED_LATEX.get())
          .require(Items.LIME_DYE)
          .output(Items.SLIME_BALL, 1)
      }
  }
  val GLOW_INK_FROM_INK = create("glow_ink_from_ink") { b ->
    b
      .mixing { b ->
        b
          .require(LibTags.forgeFluidTag("ink"), 1000)
          .require(Items.GLOWSTONE_DUST)
          .require(Items.GLOWSTONE_DUST)
          .require(Items.GLOWSTONE_DUST)
          .require(Items.GLOWSTONE_DUST)
          .output(ProjectFluids.GLOW_INK.get(), 1000)
      }
  }
  val SANDSTONE_PEBBLES = create("sandstone_pebbles") { b ->
    b
      .mixing { b ->
        b
          .require(Blocks.SAND)
          .require(Blocks.SAND)
          .require(Blocks.GRAVEL)
          .require(Blocks.GRAVEL)
          .output(ProjectBlocks.SANDSTONE_PEBBLES.get(), 4)
      }
  }
  val RED_SANDSTONE_PEBBLES = create("red_sandstone_pebbles") { b ->
    b
      .mixing { b ->
        b
          .require(Blocks.RED_SAND)
          .require(Blocks.RED_SAND)
          .require(Blocks.GRAVEL)
          .require(Blocks.GRAVEL)
          .output(ProjectBlocks.RED_SANDSTONE_PEBBLES.get(), 4)
      }
  }
  val ROSEATE_SANDSTONE_PEBBLES = create("roseate_sandstone_pebbles") { b ->
    b
      .mixing { b ->
        b
          .require(ProjectBlocks.ROSEATE_GRAINS.get())
          .require(ProjectBlocks.ROSEATE_GRAINS.get())
          .require(Blocks.GRAVEL)
          .require(Blocks.GRAVEL)
          .output(ProjectBlocks.ROSEATE_SANDSTONE_PEBBLES.get(), 4)
      }
  }
}