package com.dannbrown.databoxlib.registry.datagen

import net.minecraft.data.tags.IntrinsicHolderTagsProvider
import net.minecraft.data.tags.TagsProvider
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.Block
import net.minecraftforge.common.world.BiomeModifier

abstract class DataboxModIntegrations(private val integrations: List<AbstractModPlugin>) {

  private fun getLoadedMods(): List<AbstractModPlugin> {
    return integrations.filter { it.isLoaded }
  }

  fun getModIds(): Array<String> {
    return getLoadedMods().map { it.targetModId }
      .toTypedArray()
  }

  fun registerBlockTags(tag: (pTag: TagKey<Block>) -> IntrinsicHolderTagsProvider.IntrinsicTagAppender<Block>) {
    getLoadedMods().forEach { it.addModBlockTags(tag) }
  }

  fun registerBiomeTags(tag: (pTag: TagKey<Biome>) -> TagsProvider.TagAppender<Biome>) {
    getLoadedMods().forEach { it.addModBiomeTags(tag) }
  }

  fun bootstrapBiomeModifiers(context: BootstapContext<BiomeModifier>) {
    getLoadedMods().forEach { it.bootstrapBiomeModifiers(context) }
  }
}