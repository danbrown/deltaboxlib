package com.dannbrown.databoxlib.datagen.tags

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.init.ProjectTags
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.EntityTypeTagsProvider
import net.minecraft.world.entity.EntityType
import net.minecraftforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ProjectEntityTypeTags(
  val output: PackOutput,
  val future: CompletableFuture<HolderLookup.Provider>,
  val existingFileHelper: ExistingFileHelper?
) : EntityTypeTagsProvider(output, future, ProjectContent.MOD_ID, existingFileHelper) {
  override fun getName(): String {
    return "Databox Entity Type Tags"
  }

  override fun addTags(provider: HolderLookup.Provider) {
    // databoxlib dimensions
    tag(ProjectTags.ENTITY.ATMOSPHERE_IMMUNE)
      .add(EntityType.COW) // TODO: REMOVE THIS AFTER TESTING
      .add(EntityType.ZOMBIE)
      .add(EntityType.HUSK)
      .add(EntityType.GIANT)
      .add(EntityType.ZOMBIE_HORSE)
      .add(EntityType.ZOMBIE_VILLAGER)
      .add(EntityType.ZOGLIN)
      .add(EntityType.ZOMBIFIED_PIGLIN)
      .add(EntityType.SKELETON)
      .add(EntityType.SKELETON_HORSE)
      .add(EntityType.WITHER_SKELETON)
      .add(EntityType.WITHER_SKULL)
      .add(EntityType.WITHER)
      .add(EntityType.ENDERMAN)
      .add(EntityType.ENDER_DRAGON)
      .add(EntityType.ENDERMITE)
      .add(EntityType.SHULKER)
      .add(EntityType.PHANTOM)
      .add(EntityType.ALLAY)
      .add(EntityType.VEX)
      .add(EntityType.SLIME)
      .add(EntityType.MAGMA_CUBE)
  }
}