package com.dannbrown.databoxlib.init

import com.dannbrown.databoxlib.lib.LibTags

object ProjectTags {
  // presets
  object BLOCK {

    // surface
    val IS_MOON_SURFACE = LibTags.databoxlibBlockTag("is_moon_surface")
    val IS_MARS_SURFACE = LibTags.databoxlibBlockTag("is_mars_surface")
    val IS_VENUS_SURFACE = LibTags.databoxlibBlockTag("is_venus_surface")
    val IS_MERCURY_SURFACE = LibTags.databoxlibBlockTag("is_mercury_surface")

    // tool tiers
    val NEEDS_NETHERITE_TOOL = LibTags.forgeBlockTag("needs_netherite_tool")
    val NEEDS_TUNGSTEN_TOOL = LibTags.databoxlibBlockTag("needs_tungsten_tool")

    // ores
    val TIN_ORES = LibTags.databoxlibBlockTag("tin_ores")
    val ALUMINIUM_ORES = LibTags.databoxlibBlockTag("aluminium_ores")
    val TUNGSTEN_ORES = LibTags.databoxlibBlockTag("tungsten_ores")
    val ADAMANTIUM_ORES = LibTags.databoxlibBlockTag("adamantium_ores")

    // replaceables
    val RED_HEMATITE_REPLACEABLES = LibTags.databoxlibBlockTag("red_hematite_replaceables")
    val DATABOX_CARVER_REPLACEABLES = LibTags.databoxlibBlockTag("carver_replaceables")
    val ANORTHOSITE_REPLACEABLES = LibTags.databoxlibBlockTag("anorthosite_replaceables")
    val MOONSLATE_REPLACEABLES = LibTags.databoxlibBlockTag("moonslate_replaceables")

    // cant be assembled by ships
    val SHIP_BLACKLIST = LibTags.databoxlibBlockTag("ship_blacklist")
    val DRONE_BLACKLIST = LibTags.databoxlibBlockTag("drone_blacklist")

    // ship parts
    val IS_THRUSTER = LibTags.databoxlibBlockTag("is_thruster")
  }

  object ITEM {
    val EXCLUDE_FROM_CREATIVE = LibTags.databoxlibItemTag("exclude_from_creative")
    val RUBBER_REPLACEMENTS = LibTags.databoxlibItemTag("rubber_replacements")

    // ship parts
    val IS_THRUSTER = LibTags.databoxlibItemTag("is_thruster")
  }

  object BIOME {
    val IS_DATABOX = LibTags.databoxlibBiomeTag("is_databoxlib")
    val IS_SPACE = LibTags.databoxlibBiomeTag("is_space")
    val IS_EARTH = LibTags.databoxlibBiomeTag("is_earth")
    val IS_MOON = LibTags.databoxlibBiomeTag("is_moon")
    val IS_RED_SAND_ARCHES = LibTags.databoxlibBiomeTag("is_red_sand_arches")
    val IS_MOSSY_SLATES = LibTags.databoxlibBiomeTag("is_mossy_slates")
    val IS_SCRAP_WASTELANDS = LibTags.databoxlibBiomeTag("is_scrap_wastelands")
    val IS_ROSEATE_DESERT = LibTags.databoxlibBiomeTag("is_roseate_desert")
    val HAS_SAMPLE_PLATFORM = LibTags.databoxlibBiomeTag("has_structure/sample_platform")
    val HAS_RED_ARCHES = LibTags.databoxlibBiomeTag("has_structure/red_arches")
    val HAS_LUSH_RED_ARCHES = LibTags.databoxlibBiomeTag("has_structure/lush_red_arches")
    val HAS_GRANITE_ARCHES = LibTags.databoxlibBiomeTag("has_structure/granite_arches")
  }

  object ENTITY {
    val ATMOSPHERE_IMMUNE = LibTags.databoxlibEntityTag("atmosphere_immune")
    val PRESSURE_IMMUNE = LibTags.databoxlibEntityTag("pressure_immune")
    val EXTREME_HEAT_IMMUNE = LibTags.databoxlibEntityTag("extreme_heat_immune")
    val EXTREME_COLD_IMMUNE = LibTags.databoxlibEntityTag("extreme_cold_immune")
    val TOXIC_IMMUNE = LibTags.databoxlibEntityTag("toxic_immune")
  }

  object FLUID
}