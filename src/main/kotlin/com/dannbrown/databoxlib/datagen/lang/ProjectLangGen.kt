package com.dannbrown.databoxlib.datagen.lang

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.content.ship.SpaceShipControl
import com.dannbrown.databoxlib.content.ship.block.distanceSensor.DistanceSensorBlockEntity
import com.dannbrown.databoxlib.content.ship.block.sail.SailBlockEntity
import com.dannbrown.databoxlib.content.ship.block.sail.SailBlockScreenRender
import com.dannbrown.databoxlib.content.worldgen.biome.LushRedSandArchesBiome
import com.dannbrown.databoxlib.content.worldgen.biome.MossySlatesBiome
import com.dannbrown.databoxlib.content.worldgen.biome.RedSandArchesBiome
import com.dannbrown.databoxlib.content.worldgen.biome.RoseateDesertBiome
import com.dannbrown.databoxlib.content.worldgen.biome.ScrapWastelandsBiome
import com.dannbrown.databoxlib.init.ProjectKeyBinds
import com.dannbrown.databoxlib.lib.LibData
import com.dannbrown.databoxlib.lib.LibLang

object ProjectLangGen {
  fun addStaticLangs(doRun: Boolean) {
    if (!doRun) return
    // KeyBinds
    ProjectContent.REGISTRATE.addRawLang(ProjectKeyBinds.KEY_CATEGORY, ProjectContent.NAME)
    ProjectContent.REGISTRATE.addRawLang(ProjectKeyBinds.KEY_SPACESHIP_ASSEMBLE, "Assemble Spaceship")
    // GUI
    ProjectContent.REGISTRATE.addRawLang(SpaceShipControl.STOP_CRUISING_TRANSLATION_KEY, "Cruising deactivated")
    ProjectContent.REGISTRATE.addRawLang(SpaceShipControl.START_CRUISING_TRANSLATION_KEY, "Cruising activated")
    ProjectContent.REGISTRATE.addRawLang(SailBlockScreenRender.ALIGN_TEXT, "Align")
    ProjectContent.REGISTRATE.addRawLang(SailBlockScreenRender.ASSEMBLE_TEXT, "Assemble")
    ProjectContent.REGISTRATE.addRawLang(SailBlockScreenRender.ASSEMBLE_DRONE_TEXT, "Assemble Drone")
    ProjectContent.REGISTRATE.addRawLang(SailBlockScreenRender.DISSEMBLE_TEXT, "Disassemble")
    ProjectContent.REGISTRATE.addRawLang(SailBlockScreenRender.ALIGNING_TEXT, "Aligning...")
    ProjectContent.REGISTRATE.addRawLang(SailBlockEntity.TRANSLATION_KEY, "Sail v1.0.2")
    ProjectContent.REGISTRATE.addRawLang(DistanceSensorBlockEntity.DISTANCE_SCROLL_TITLE, "Detection Distance")
    ProjectContent.REGISTRATE.addRawLang(DistanceSensorBlockEntity.MODE_SCROLL_TITLE + LibLang.asId(DistanceSensorBlockEntity.DetectionMode.SHIP.name), "Detect Ships")
    ProjectContent.REGISTRATE.addRawLang(DistanceSensorBlockEntity.MODE_SCROLL_TITLE + LibLang.asId(DistanceSensorBlockEntity.DetectionMode.WORLD.name), "Detect Blocks")
    ProjectContent.REGISTRATE.addRawLang(DistanceSensorBlockEntity.MODE_SCROLL_TITLE + LibLang.asId(DistanceSensorBlockEntity.DetectionMode.WORLD_AND_SHIP.name), "Detect Ships and Blocks")
    // World Generator
    ProjectContent.REGISTRATE.addRawLang("generator.databoxlib.databoxlib_world", "Databox World")
    // Creative tabs
    addCreativeTabLang("databoxlib_main_tab", ProjectContent.NAME)
    addCreativeTabLang("databoxlib_stickers_tab", ProjectContent.NAME + ": Stickers")
    // Recipe Lang Gens
    ProjectContent.REGISTRATE.addRawLang("create.recipe.cooling", "Cooling")
    ProjectContent.REGISTRATE.addRawLang("create.recipe.electrolysis", "Electrolysis")
    ProjectContent.REGISTRATE.addRawLang("create.recipe.pressure_chamber", "Pressure Chamber")
    ProjectContent.REGISTRATE.addRawLang("create.recipe.heat_requirement.passiveheated", "Passive Heated")
    ProjectContent.REGISTRATE.addRawLang("create.recipe.heat_requirement.hyperheated", "Hyper Heated")
    // Death Messages
    addDeathMessageLang(LibData.DAMAGE_SOURCES.GAS_SUFFOCATION, "%1\$s suffocated in a gas")
    addDeathMessageLang(LibData.DAMAGE_SOURCES.ACID_CORROSION, "%1\$s was corroded into a pile of bones")
    addDeathMessageLang(LibData.DAMAGE_SOURCES.MOLTEN_METAL, "%1\$s melt down into a metal statue")
    // Goggles Tooltips
    addGogglesLang("kinetics_tank_no_oxygen", "No oxygen is available")
    // Biomes
    addBiomeLang(RoseateDesertBiome.biomeId, "Roseate Desert")
    addBiomeLang(ScrapWastelandsBiome.biomeId, "Scrap Wastelands")
    addBiomeLang(RedSandArchesBiome.biomeId, "Red Sand Arches")
    addBiomeLang(LushRedSandArchesBiome.biomeId, "Lush Red Sand Arches")
    addBiomeLang(MossySlatesBiome.biomeId, "Mossy Slates")
//    addBiomeLang(MoonPlainsBiome.biomeId, "Moon Plains")
    // Chemical Formulas
    ProjectContent.REGISTRATE.addRawLang("menu.databoxlib.formula_format", "Chemical Formula: %1\$s")
    // @ CHEMICALS
    // Databox Chemicals
    addFormulaLang(LibData.ITEMS.SULPHUR, "S\u2088")
    addFormulaLang(LibData.ITEMS.SILICA_CRYSTAL, "SiO\u2082")
    addFormulaLang(LibData.ITEMS.SILICA_GRAINS, "SiO\u2082")
    addFormulaLang(LibData.ITEMS.REFINED_SILICON, "Si")
    addFormulaLang(LibData.ITEMS.PHOSPHORUS_POWDER, "P\u2084")
    addFormulaLang(LibData.ITEMS.STEEL_BLEND, "Fe + C")
    addFormulaLang(LibData.ITEMS.CARBON_GRIT, "C")
    addFormulaLang(LibData.ITEMS.IRON_DUST, "Fe")
    addFormulaLang(LibData.ITEMS.RAW_CARBON, "C")
    addFormulaLang(LibData.ITEMS.ROSE_QUARTZ_SHARD, "SiO\u2082 + Mn\u00B2\u207A")
    addFormulaLang(LibData.ITEMS.TIN_INGOT, "Sn")
    addFormulaLang(LibData.ITEMS.TIN_NUGGET, "Sn")
    addFormulaLang(LibData.ITEMS.ALUMINIUM_INGOT, "Al")
    addFormulaLang(LibData.ITEMS.ALUMINIUM_NUGGET, "Al")
    addFormulaLang(LibData.ITEMS.BRONZE_INGOT, "Cu + Sn")
    addFormulaLang(LibData.ITEMS.BRONZE_NUGGET, "Cu + Sn")
    addFormulaLang(LibData.ITEMS.ADAMANTIUM_INGOT, "Unknown")
    addFormulaLang(LibData.ITEMS.ADAMANTIUM_FRAGMENT, "Unknown")
    // Databox Minerals
    addFormulaLang(LibData.NAMES.PYRITE, "Fe\u2082S\u2082")
    addFormulaLang(LibData.NAMES.PHOSPHORITE, "Ca\u2085(PO\u2084)\u2083F")
    // Databox Fluids
    addFormulaLang(LibData.FLUIDS.HYDROGEN + "_bucket", "H\u2082")
    addFormulaLang(LibData.FLUIDS.SULFURIC_ACID + "_bucket", "H\u2082SO\u2084")
    addFormulaLang(LibData.FLUIDS.NITRIC_ACID + "_bucket", "HNO\u2083")
    // Compat Items
    addFormulaLang("steel_ingot", "Fe + C")
    addFormulaLang("steel_nugget", "Fe + C")
    addFormulaLang("zinc_ingot", "Zn")
    addFormulaLang("zinc_nugget", "Zn")
    addFormulaLang("lead_ingot", "Pb")
    addFormulaLang("lead_nugget", "Pb")
    addFormulaLang("brass_ingot", "Cu + Zn")
    addFormulaLang("brass_nugget", "Cu + Zn")
    addFormulaLang("rose_quartz", "SiO\u2082 + Mn\u00B2\u207A")
    addFormulaLang("polished_rose_quartz", "SiO\u2082 + Mn\u00B2\u207A")
    // Vanilla Items
    addFormulaLang("diamond", "C")
    addFormulaLang("amethyst_shard", "SiO\u2082 + Fe\u00B3\u207A")
    addFormulaLang("redstone", "SiP\u2084Al")
    addFormulaLang("quartz", "SiO\u2082")
    addFormulaLang("emerald", "Be\u2083Al\u2082(SiO\u2083)\u2086")
    addFormulaLang("lapis_lazuli", "(Na,Ca)\u2088[(S,Cl,SO\u2084,OH)\u2082|(Al,Si)\u2086O\u2082\u2084]")
    addFormulaLang("coal", "C")
    addFormulaLang("charcoal", "C + (H,S)")
    addFormulaLang("iron_ingot", "Fe")
    addFormulaLang("iron_nugget", "Fe")
    addFormulaLang("gold_ingot", "Au")
    addFormulaLang("gold_nugget", "Au")
    addFormulaLang("copper_ingot", "Cu")
    addFormulaLang("copper_nugget", "Cu")
    addFormulaLang("netherite_ingot", "Ti\u2083N\u2082 + Au")
    addFormulaLang("netherite_nugget", "Ti\u2083N\u2082 + Au")
    addFormulaLang("flint", "SiO\u2082 + CaCO\u2083")
    addFormulaLang("calcite", "CaCO\u2083")
    addFormulaLang("granite", "SiO\u2082 + Al\u2082Si\u2082O\u2088")
  }

  private fun addFormulaLang(
    formula: String,
    name: String,
  ) {
    ProjectContent.REGISTRATE.addRawLang("formula.databoxlib.$formula", name)
  }

  private fun addCreativeTabLang(
    tab: String,
    name: String,
  ) {
    ProjectContent.REGISTRATE.addRawLang("itemGroup.$tab", name)
  }

  private fun addDeathMessageLang(
    name: String,
    phrase: String,
  ) {
    ProjectContent.REGISTRATE.addRawLang("death.attack.$name", phrase)
  }

  private fun addGogglesLang(
    name: String,
    phrase: String,
  ) {
    ProjectContent.REGISTRATE.addRawLang("databoxlib.gui.goggles.$name", phrase)
  }

  private fun addBiomeLang(
    name: String,
    phrase: String,
  ) {
    ProjectContent.REGISTRATE.addRawLang("biome.databoxlib.$name", phrase)
  }
}
