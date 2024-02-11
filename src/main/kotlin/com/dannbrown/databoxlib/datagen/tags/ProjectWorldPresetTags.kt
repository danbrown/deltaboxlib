package com.dannbrown.databoxlib.datagen.tags



import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.datagen.worldgen.ProjectWorldPresets
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.WorldPresetTagsProvider
import net.minecraft.tags.TagKey
import net.minecraft.tags.WorldPresetTags
import net.minecraft.world.level.levelgen.presets.WorldPreset
import net.minecraftforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ProjectWorldPresetTags(
  output: PackOutput,
  future: CompletableFuture<HolderLookup.Provider>,
  fileHelper: ExistingFileHelper
) :
  WorldPresetTagsProvider(output, future, ProjectContent.MOD_ID, fileHelper) {
  override fun getName(): String {
    return "Databox World Presets Tags"
  }

  override public fun tag(tagKey: TagKey<WorldPreset>): TagAppender<WorldPreset> {
    return super.tag(tagKey)
  }

  override fun addTags(provider: HolderLookup.Provider) {
    // BEWARE: It can replace tags created by the REGISTRATE system
    tag(WorldPresetTags.NORMAL).add(ProjectWorldPresets.DATABOX_WORLD_PRESET)

  }
}