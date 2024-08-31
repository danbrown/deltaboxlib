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
  fun <T> optionalTag(registry: IForgeRegistry<T>, id: ResourceLocation): TagKey<T> {
    return registry.tags()!!.createOptionalTagKey(id, emptySet())
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

  // DELTABOX
  fun <T> deltaboxTag(registry: IForgeRegistry<T>, path: String): TagKey<T> {
    return optionalTag(registry, ResourceLocation(DeltaboxLib.MOD_ID, path))
  }

  fun deltaboxBlockTag(path: String): TagKey<Block> {
    return deltaboxTag(ForgeRegistries.BLOCKS, path)
  }

  fun deltaboxItemTag(path: String): TagKey<Item> {
    return deltaboxTag(ForgeRegistries.ITEMS, path)
  }

  fun deltaboxFluidTag(path: String): TagKey<Fluid> {
    return deltaboxTag(ForgeRegistries.FLUIDS, path)
  }

  fun deltaboxBiomeTag(path: String): TagKey<Biome> {
    return deltaboxTag(ForgeRegistries.BIOMES, path)
  }

  fun deltaboxEntityTag(path: String): TagKey<EntityType<*>> {
    return deltaboxTag(ForgeRegistries.ENTITY_TYPES, path)
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
