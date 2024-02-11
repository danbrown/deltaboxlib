package com.dannbrown.databoxlib.lib

import com.dannbrown.databoxlib.ProjectContent
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.IForgeRegistry

object LibTags {
  fun <T> optionalTag(
    registry: IForgeRegistry<T>,
    id: ResourceLocation
  ): TagKey<T> {
    return registry.tags()!!
      .createOptionalTagKey(id, emptySet())
  }

  // VANILLA
  fun <T> vanillaTag(registry: IForgeRegistry<T>, path: String): TagKey<T> {
    return optionalTag(registry, ResourceLocation("minecraft", path))
  }

  fun vanillaBlockTag(path: String): TagKey<Block> {
    return vanillaTag(ForgeRegistries.BLOCKS, path)
  }

  fun vanillaItemTag(path: String): TagKey<Item> {
    return vanillaTag(ForgeRegistries.ITEMS, path)
  }

  // FORGE
  fun <T> forgeTag(registry: IForgeRegistry<T>, path: String): TagKey<T> {
    return optionalTag(registry, ResourceLocation("forge", path))
  }

  fun forgeBlockTag(path: String): TagKey<Block> {
    return forgeTag(ForgeRegistries.BLOCKS, path)
  }

  fun forgeItemTag(path: String): TagKey<Item> {
    return forgeTag(ForgeRegistries.ITEMS, path)
  }

  fun forgeFluidTag(path: String): TagKey<Fluid> {
    return forgeTag(ForgeRegistries.FLUIDS, path)
  }

  // DATABOX
  fun <T> databoxlibTag(registry: IForgeRegistry<T>, path: String): TagKey<T> {
    return optionalTag(registry, ResourceLocation(ProjectContent.MOD_ID, path))
  }

  fun databoxlibBlockTag(path: String): TagKey<Block> {
    return databoxlibTag(ForgeRegistries.BLOCKS, path)
  }

  fun databoxlibItemTag(path: String): TagKey<Item> {
    return databoxlibTag(ForgeRegistries.ITEMS, path)
  }

  fun databoxlibFluidTag(path: String): TagKey<Fluid> {
    return databoxlibTag(ForgeRegistries.FLUIDS, path)
  }

  fun databoxlibBiomeTag(path: String): TagKey<Biome> {
    return databoxlibTag(ForgeRegistries.BIOMES, path)
  }

  fun databoxlibEntityTag(path: String): TagKey<EntityType<*>> {
    return databoxlibTag(ForgeRegistries.ENTITY_TYPES, path)
  }

  // CREATE
  fun <T> createTag(registry: IForgeRegistry<T>, path: String): TagKey<T> {
    return optionalTag(registry, ResourceLocation("create", path))
  }

  fun createBlockTag(path: String): TagKey<Block> {
    return createTag(ForgeRegistries.BLOCKS, path)
  }

  fun createItemTag(path: String): TagKey<Item> {
    return createTag(ForgeRegistries.ITEMS, path)
  }

  fun createFluidTag(path: String): TagKey<Fluid> {
    return createTag(ForgeRegistries.FLUIDS, path)
  }
}