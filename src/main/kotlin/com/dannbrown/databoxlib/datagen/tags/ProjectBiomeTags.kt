package com.dannbrown.databoxlib.datagen.tags

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.compat.ProjectModIntegrations
import com.dannbrown.databoxlib.content.worldgen.biome.LushRedSandArchesBiome
import com.dannbrown.databoxlib.content.worldgen.biome.MossySlatesBiome
import com.dannbrown.databoxlib.content.worldgen.biome.RedSandArchesBiome
import com.dannbrown.databoxlib.content.worldgen.biome.RoseateDesertBiome
import com.dannbrown.databoxlib.content.worldgen.biome.ScrapWastelandsBiome
import com.dannbrown.databoxlib.init.ProjectTags
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.BiomeTagsProvider
import net.minecraft.tags.BiomeTags
import net.minecraftforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ProjectBiomeTags(
  val output: PackOutput,
  val future: CompletableFuture<HolderLookup.Provider>,
  val existingFileHelper: ExistingFileHelper?
) : BiomeTagsProvider(output, future, ProjectContent.MOD_ID, existingFileHelper) {
  override fun getName(): String {
    return "Databox Biome Tags"
  }

  override fun addTags(provider: HolderLookup.Provider) {
//    super.addTags(provider)
    // databoxlib dimensions
    tag(ProjectTags.BIOME.IS_EARTH)
      .add(RedSandArchesBiome.BIOME_KEY)
      .add(LushRedSandArchesBiome.BIOME_KEY)
      .add(ScrapWastelandsBiome.BIOME_KEY)
      .add(RoseateDesertBiome.BIOME_KEY)
      .add(MossySlatesBiome.BIOME_KEY)

//    tag(ProjectTags.BIOME.IS_MOON)
//      .add(MoonPlainsBiome.BIOME_KEY)

    tag(ProjectTags.BIOME.IS_DATABOX)
//      .addTag(DataboxTags.BIOME.IS_SPACE)
      .addTag(ProjectTags.BIOME.IS_EARTH)
//      .addTag(ProjectTags.BIOME.IS_MOON)
    // databoxlib biomes
    tag(ProjectTags.BIOME.IS_RED_SAND_ARCHES)
      .add(RedSandArchesBiome.BIOME_KEY)
      .add(LushRedSandArchesBiome.BIOME_KEY)

    tag(ProjectTags.BIOME.IS_MOSSY_SLATES)
      .add(MossySlatesBiome.BIOME_KEY)

    tag(ProjectTags.BIOME.IS_SCRAP_WASTELANDS)
      .add(ScrapWastelandsBiome.BIOME_KEY)

    tag(ProjectTags.BIOME.IS_ROSEATE_DESERT)
      .add(RoseateDesertBiome.BIOME_KEY)
    // structures
    tag(ProjectTags.BIOME.HAS_RED_ARCHES)
      .add(RedSandArchesBiome.BIOME_KEY)
      .add(LushRedSandArchesBiome.BIOME_KEY)

    tag(ProjectTags.BIOME.HAS_GRANITE_ARCHES)
      .add(RedSandArchesBiome.BIOME_KEY)

    tag(ProjectTags.BIOME.HAS_LUSH_RED_ARCHES)
      .add(LushRedSandArchesBiome.BIOME_KEY)
    // add mod compatibility tags
    ProjectModIntegrations.registerBiomeTags(this::tag)
    // vanilla
    tag(BiomeTags.HAS_MINESHAFT_MESA)
      .add(RedSandArchesBiome.BIOME_KEY)
      .add(LushRedSandArchesBiome.BIOME_KEY)

//    tag(BiomeTags.WITHOUT_ZOMBIE_SIEGES).addTag(ProjectTags.BIOME.IS_MOON)
//    tag(BiomeTags.WITHOUT_PATROL_SPAWNS).addTag(ProjectTags.BIOME.IS_MOON)
//    tag(BiomeTags.WITHOUT_WANDERING_TRADER_SPAWNS).addTag(ProjectTags.BIOME.IS_MOON)
  }
}