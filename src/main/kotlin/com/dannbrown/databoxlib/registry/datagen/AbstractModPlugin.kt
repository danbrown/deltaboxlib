package com.dannbrown.databoxlib.registry.datagen

import net.minecraft.data.tags.IntrinsicHolderTagsProvider
import net.minecraft.data.tags.TagsProvider
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.Block
import net.minecraftforge.common.world.BiomeModifier
import net.minecraftforge.registries.ForgeRegistries
import org.apache.logging.log4j.LogManager

// here we define mod compatibility to add features into discover
abstract class AbstractModPlugin(val ownerModId: String, val targetModId: String, val isLoaded: Boolean) {
  init {
    val LOGGER = LogManager.getLogger()
    if (isLoaded) LOGGER.info("${targetModId} is loaded, adding features to ${ownerModId}")
    else LOGGER.info("${targetModId} is not loaded, skipping")
  }

  // biome tags
  abstract fun addModBiomeTags(tag: (pTag: TagKey<Biome>) -> TagsProvider.TagAppender<Biome>)

  abstract fun addModBlockTags(tag: (pTag: TagKey<Block>) -> IntrinsicHolderTagsProvider.IntrinsicTagAppender<Block>)

  // biome modifiers
  fun registerBiomeModifierKey(name: String): ResourceKey<BiomeModifier> {
    return ResourceKey.create(
      ForgeRegistries.Keys.BIOME_MODIFIERS,
      ResourceLocation(targetModId, "${targetModId}_${ownerModId}_$name")
    )
  }

  abstract fun bootstrapBiomeModifiers(context: BootstapContext<BiomeModifier>)

  // ----
}