package com.dannbrown.deltaboxlib.lib

import com.dannbrown.deltaboxlib.DeltaboxLib
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
  fun <T> databoxTag(registry: IForgeRegistry<T>, path: String): TagKey<T> {
    return optionalTag(registry, ResourceLocation(DeltaboxLib.MOD_ID, path))
  }

  fun databoxBlockTag(path: String): TagKey<Block> {
    return databoxTag(ForgeRegistries.BLOCKS, path)
  }

  fun databoxItemTag(path: String): TagKey<Item> {
    return databoxTag(ForgeRegistries.ITEMS, path)
  }

  fun databoxFluidTag(path: String): TagKey<Fluid> {
    return databoxTag(ForgeRegistries.FLUIDS, path)
  }

  fun databoxBiomeTag(path: String): TagKey<Biome> {
    return databoxTag(ForgeRegistries.BIOMES, path)
  }

  fun databoxEntityTag(path: String): TagKey<EntityType<*>> {
    return databoxTag(ForgeRegistries.ENTITY_TYPES, path)
  }

  // ANY MOD
  fun <T> modTag(modId: String, registry: IForgeRegistry<T>, path: String): TagKey<T> {
    return optionalTag(registry, ResourceLocation(modId, path))
  }

  fun modBlockTag(modId: String, path: String): TagKey<Block> {
    return modTag(modId, ForgeRegistries.BLOCKS, path)
  }

  fun modItemTag(modId: String, path: String): TagKey<Item> {
    return modTag(modId, ForgeRegistries.ITEMS, path)
  }

  fun modBiomeTag(modId: String, path: String): TagKey<Biome> {
    return modTag(modId, ForgeRegistries.BIOMES, path)
  }

  fun modEntityTag(modId: String, path: String): TagKey<EntityType<*>> {
    return modTag(modId, ForgeRegistries.ENTITY_TYPES, path)
  }

  fun modFluidTag(modId: String, path: String): TagKey<Fluid> {
    return modTag(modId, ForgeRegistries.FLUIDS, path)
  }
}