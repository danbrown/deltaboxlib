package com.dannbrown.databoxlib.datagen.tags

import com.dannbrown.databoxlib.ProjectContent
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.FluidTagsProvider
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.minecraftforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ProjectFluidTags(
  output: PackOutput,
  future: CompletableFuture<HolderLookup.Provider>,
  fileHelper: ExistingFileHelper
) :
  FluidTagsProvider(output, future, ProjectContent.MOD_ID, fileHelper) {
  override fun getName(): String {
    return "Databox Fluid Tags"
  }

  public override fun tag(tagKey: TagKey<Fluid>): IntrinsicTagAppender<Fluid> {
    return super.tag(tagKey)
  }

  override fun addTags(provider: HolderLookup.Provider) {
    // BEWARE: It can replace tags created by the REGISTRATE system
  }
}