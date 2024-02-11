package com.dannbrown.databoxlib.datagen.recipes

import com.dannbrown.databoxlib.init.ProjectBlocks
import com.dannbrown.databoxlib.init.ProjectItems
import com.dannbrown.databoxlib.init.ProjectTags
import com.dannbrown.databoxlib.lib.LibTags
import com.simibubi.create.AllBlocks
import com.simibubi.create.AllItems
import com.simibubi.create.content.decoration.palettes.AllPaletteBlocks
import com.tterrag.registrate.util.DataIngredient
import net.minecraft.data.DataGenerator
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks

class StandardRecipeGen(generator: DataGenerator) : ProjectRecipeGen(generator) {
  override val recipeName = "Standards"

  //  val FURNACE_TEST = cooking(
  //    { DataIngredient.tag(LibTags.forgeItemTag("stripped_logs")) },
  //    { Items.GOLD_INGOT }
  //  ) { b -> b
  //    .prefix("furnace_test_")
  //    .comboFoodCooking(300, 4f)
  //  }
//  val PASSIVE_HEATER_FROM_CAMPFIRE = crafting({ DataboxBlocks.PASSIVE_HEATER.get() }) { b -> b
//    .shapeless( 1, "", "_from_campfire", listOf(DataIngredient.items(Blocks.CAMPFIRE)))
//  }
//  val CAMPFIRE_FROM_PASSIVE_HEATER = crafting({ Blocks.CAMPFIRE }) { b -> b
//    .shapeless( 1, "", "_from_passive_heater", listOf(DataIngredient.items(DataboxBlocks.PASSIVE_HEATER.get())))
//  }
  val INVERTED_CLUTCH = crafting({ ProjectBlocks.INVERTED_CLUTCH.get() }) { b ->
    b
      .shapeless(1, "", "_from_clutch", listOf(DataIngredient.items(AllBlocks.CLUTCH.get())))
  }
  val CLUTCH = crafting({ AllBlocks.CLUTCH.get() }) { b ->
    b
      .shapeless(1, "", "_from_inverted_clutch", listOf(DataIngredient.items(ProjectBlocks.INVERTED_CLUTCH.get())))
  }
  val RESISTOR = crafting({ ProjectBlocks.RESISTOR.get() }) { b ->
    b
      .shapeless(1, "", "_from_clutch", listOf(DataIngredient.items(AllBlocks.CLUTCH.get()), DataIngredient.items(Items.COMPARATOR)))
  }
  val KINETIC_ELECTROLYZER = crafting({ ProjectBlocks.KINETIC_ELECTROLYZER.get() }) { b ->
    b
      .shaped(1) { c ->
        c
          .pattern("csc")
          .pattern("APA")
          .pattern("Z C")
          .define('s', AllBlocks.SHAFT.get())
          .define('c', AllBlocks.ANDESITE_CASING.get())
          .define('P', AllItems.PRECISION_MECHANISM.get())
          .define('C', LibTags.forgeItemTag("ingots/copper"))
          .define('Z', LibTags.forgeItemTag("ingots/zinc"))
          .define('A', LibTags.forgeItemTag("ingots/aluminium"))
      }
  }
  val MECHANICAL_COOLER = crafting({ ProjectBlocks.MECHANICAL_COOLER.get() }) { b ->
    b
      .shaped(1) { c ->
        c
          .pattern("AsA")
          .pattern("SBS")
          .pattern("GSG")
          .define('s', AllBlocks.SHAFT.get())
          .define('B', Blocks.BLUE_ICE)
          .define('G', AllPaletteBlocks.FRAMED_GLASS.get())
          .define('S', LibTags.forgeItemTag("plates/steel"))
          .define('A', LibTags.forgeItemTag("storage_blocks/aluminium"))
      }
  }
  val PRESSURE_CHAMBER = crafting({ ProjectBlocks.PRESSURE_CHAMBER_VALVE.get() }) { b ->
    b
      .shaped(1) { c ->
        c
          .pattern("CXC")
          .pattern("III")
          .define('X', Items.COMPASS)
          .define('C', ProjectItems.CATHODE_TUBE.get())
          .define('I', Blocks.IRON_BLOCK)
      }
  }
  val CATHODE_TUBE = crafting({ ProjectItems.CATHODE_TUBE.get() }) { b ->
    b
      .shaped(1) { c ->
        c
          .pattern("G")
          .pattern("C")
          .pattern("I")
          .define('G', AllPaletteBlocks.FRAMED_GLASS.get())
          .define('C', LibTags.forgeItemTag("ingots/copper"))
          .define('I', LibTags.forgeItemTag("plates/iron"))
      }
  }
  val DEPLOYER_CATHODE_TUBE = crafting({ AllBlocks.DEPLOYER.get() }) { b ->
    b
      .shaped(1, "", "_from_cathode_tube") { c ->
        c
          .pattern("CCC")
          .pattern(" A ")
          .pattern(" B ")
          .define('C', ProjectItems.CATHODE_TUBE.get())
          .define('A', AllBlocks.ANDESITE_CASING.get())
          .define('B', AllItems.BRASS_HAND.get())
      }
  }
  val DISPLAY_BOARD_CATHODE_TUBE = crafting({ AllBlocks.DISPLAY_BOARD.get() }) { b ->
    b
      .shaped(1, "", "_from_cathode_tube") { c ->
        c
          .pattern("ACA")
          .define('C', ProjectItems.CATHODE_TUBE.get())
          .define('A', AllItems.ANDESITE_ALLOY.get())
      }
  }
  val NIXIE_TUBE_CATHODE_TUBE = crafting({ AllBlocks.ORANGE_NIXIE_TUBE.get() }) { b ->
    b
      .shaped(2, "", "_from_cathode_tube") { c ->
        c
          .pattern("CC")
          .define('C', ProjectItems.CATHODE_TUBE.get())
      }
  }
  val SMART_FLUID_PIPE_CATHODE_TUBE = crafting({ AllBlocks.SMART_FLUID_PIPE.get() }) { b ->
    b
      .shaped(1, "", "_from_cathode_tube") { c ->
        c
          .pattern(" B ")
          .pattern(" F ")
          .pattern("CCC")
          .define('C', ProjectItems.CATHODE_TUBE.get())
          .define('B', LibTags.forgeItemTag("plates/brass"))
          .define('F', AllBlocks.FLUID_PIPE.get())
      }
  }
  val CART_ASSEMBLER_CATHODE_TUBE = crafting({ AllBlocks.CART_ASSEMBLER.get() }) { b ->
    b
      .shaped(1, "", "_from_cathode_tube") { c ->
        c
          .pattern("ACA")
          .pattern("S S")
          .define('C', ProjectItems.CATHODE_TUBE.get())
          .define('A', AllItems.ANDESITE_ALLOY.get())
          .define('S', LibTags.vanillaItemTag("logs"))
      }
  }
  val COMPASS_CATHODE_TUBE = crafting({ Items.COMPASS }) { b ->
    b
      .shaped(1, "", "_from_cathode_tube") { c ->
        c
          .pattern(" I ")
          .pattern("ICI")
          .pattern(" I ")
          .define('C', ProjectItems.CATHODE_TUBE.get())
          .define('I', LibTags.forgeItemTag("ingots/iron"))
      }
  }
  val CONTROLLER_RAIL_CATHODE_TUBE = crafting({ AllBlocks.CONTROLLER_RAIL.get() }) { b ->
    b
      .shaped(1, "", "_from_cathode_tube") { c ->
        c
          .pattern("G G")
          .pattern("GSG")
          .pattern("GCG")
          .define('C', ProjectItems.CATHODE_TUBE.get())
          .define('G', LibTags.forgeItemTag("ingots/gold"))
          .define('S', Items.STICK)
      }
  }
  val BRASS_FUNNEL_CATHODE_TUBE = crafting({ AllBlocks.BRASS_FUNNEL.get() }) { b ->
    b
      .shaped(1, "", "_from_cathode_tube") { c ->
        c
          .pattern("C")
          .pattern("B")
          .pattern("K")
          .define('C', ProjectItems.CATHODE_TUBE.get())
          .define('B', LibTags.forgeItemTag("ingots/brass"))
          .define('K', ProjectTags.ITEM.RUBBER_REPLACEMENTS)
      }
  }
  val BRASS_TUNNEL_CATHODE_TUBE = crafting({ AllBlocks.BRASS_TUNNEL.get() }) { b ->
    b
      .shaped(1, "", "_from_cathode_tube") { c ->
        c
          .pattern("CC")
          .pattern("BB")
          .pattern("KK")
          .define('C', ProjectItems.CATHODE_TUBE.get())
          .define('B', LibTags.forgeItemTag("ingots/brass"))
          .define('K', ProjectTags.ITEM.RUBBER_REPLACEMENTS)
      }
  }
  val SMART_CHUTE_CATHODE_TUBE = crafting({ AllBlocks.SMART_CHUTE.get() }) { b ->
    b
      .shaped(1, "", "_from_cathode_tube") { c ->
        c
          .pattern(" B ")
          .pattern(" c ")
          .pattern("CCC")
          .define('C', ProjectItems.CATHODE_TUBE.get())
          .define('c', AllBlocks.CHUTE.get())
          .define('B', LibTags.forgeItemTag("plates/brass"))
      }
  }
  val SEQUENCED_GEARSHIFT_CATHODE_TUBE = crafting({ AllBlocks.SEQUENCED_GEARSHIFT.get() }) { b ->
    b
      .shapeless(1, "", "_from_cathode_tube", listOf(DataIngredient.items(AllBlocks.BRASS_CASING.get()), DataIngredient.items(AllBlocks.COGWHEEL.get()), DataIngredient.items(ProjectItems.CATHODE_TUBE.get())))
  }
  val BLACK_DYE_FROM_GRAPHITE = crafting({ Items.BLACK_DYE }) { b ->
    b
      .shapeless(1, "", "_from_graphite", listOf(DataIngredient.items(ProjectItems.RAW_GRAPHITE.get())))
  }
  val BLACK_DYE_FROM_CARBON_GRIT = crafting({ Items.BLACK_DYE }) { b ->
    b
      .shapeless(2, "", "_from_carbon_grit", listOf(DataIngredient.items(ProjectItems.CARBON_GRIT.get())))
  }
  val STRING_FROM_COTTON_PULPS = crafting({ Items.STRING }) { b ->
    b
      .shaped(2, "", "_from_cotton_pulps") { c ->
        c
          .pattern("PPP")
          .define('P', ProjectItems.COTTON_PULP.get())
      }
  }
  val ORANGE_DYE_FROM_OCOTILLO = crafting({ Items.ORANGE_DYE }) { b ->
    b
      .shapeless(1, "", "_from_ocotillo", listOf(DataIngredient.items(ProjectBlocks.OCOTILLO.get())))
  }

  // START ADDING RUBBER AS A DRIED KELP SUBSTITUTE FOR CREATE RECIPES -------------------------------------------------
  val BRASS_TUNNEL_RUBBER = crafting({ AllBlocks.BRASS_TUNNEL.get() }) { b ->
    b
      .shaped(2, "", "_with_rubber") { c ->
        c
          .pattern("E ")
          .pattern("BB")
          .pattern("KK")
          .define('E', AllItems.ELECTRON_TUBE.get())
          .define('B', LibTags.forgeItemTag("ingots/brass"))
          .define('K', ProjectTags.ITEM.RUBBER_REPLACEMENTS)
      }
  }
  val BRASS_FUNNEL_RUBBER = crafting({ AllBlocks.BRASS_FUNNEL.get() }) { b ->
    b
      .shaped(2, "", "_with_rubber") { c ->
        c
          .pattern("E")
          .pattern("B")
          .pattern("K")
          .define('E', AllItems.ELECTRON_TUBE.get())
          .define('B', LibTags.forgeItemTag("ingots/brass"))
          .define('K', ProjectTags.ITEM.RUBBER_REPLACEMENTS)
      }
  }
  val SPOUT_RUBBER = crafting({ AllBlocks.SPOUT.get() }) { b ->
    b
      .shaped(1, "", "_with_rubber") { c ->
        c
          .pattern("C")
          .pattern("K")
          .define('C', AllBlocks.COPPER_CASING.get())
          .define('K', ProjectTags.ITEM.RUBBER_REPLACEMENTS)
      }
  }
  val ANDESITE_FUNNEL_RUBBER = crafting({ AllBlocks.ANDESITE_FUNNEL.get() }) { b ->
    b
      .shaped(2, "", "_with_rubber") { c ->
        c
          .pattern("A")
          .pattern("K")
          .define('A', AllItems.ANDESITE_ALLOY.get())
          .define('K', ProjectTags.ITEM.RUBBER_REPLACEMENTS)
      }
  }
  val ANDESITE_TUNNEL_RUBBER = crafting({ AllBlocks.ANDESITE_TUNNEL.get() }) { b ->
    b
      .shaped(2, "", "_with_rubber") { c ->
        c
          .pattern("AA")
          .pattern("KK")
          .define('A', AllItems.ANDESITE_ALLOY.get())
          .define('K', ProjectTags.ITEM.RUBBER_REPLACEMENTS)
      }
  }
  val BELT_RUBBER = crafting({ AllItems.BELT_CONNECTOR.get() }) { b ->
    b
      .shaped(1, "", "_with_rubber") { c ->
        c
          .pattern("KKK")
          .pattern("KKK")
          .define('K', ProjectTags.ITEM.RUBBER_REPLACEMENTS)
      }
  }
  val ELEVATOR_PULLEY_RUBBER = crafting({ AllBlocks.ELEVATOR_PULLEY.get() }) { b ->
    b
      .shaped(1, "", "_with_rubber") { c ->
        c
          .pattern(" A ")
          .pattern("RRR")
          .pattern(" I ")
          .define('A', AllBlocks.BRASS_CASING.get())
          .define('R', ProjectItems.RUBBER.get())
          .define('I', LibTags.forgeItemTag("plates/iron"))
      }
  }
  val HOSE_PULLEY_RUBBER = crafting({ AllBlocks.HOSE_PULLEY.get() }) { b ->
    b
      .shaped(1, "", "_with_rubber") { c ->
        c
          .pattern(" A ")
          .pattern("RRR")
          .pattern(" C ")
          .define('A', AllBlocks.COPPER_CASING.get())
          .define('R', ProjectItems.RUBBER.get())
          .define('C', LibTags.forgeItemTag("plates/copper"))
      }
  }

  // END ADDING RUBBER AS A DRIED KELP SUBSTITUTE FOR CREATE RECIPES ---------------------------------------------------
  val GUNPOWDER_FROM_SULPHUR = crafting({ Items.GUNPOWDER }) { b ->
    b
      .shapeless(1, "", "_from_sulphur", listOf(DataIngredient.items(Items.CHARCOAL), DataIngredient.items(ProjectItems.SULPHUR.get()), DataIngredient.items(ProjectItems.SILICA_GRAINS.get())))
  }
}