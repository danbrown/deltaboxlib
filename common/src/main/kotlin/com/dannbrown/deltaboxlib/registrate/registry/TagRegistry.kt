package com.dannbrown.deltaboxlib.registrate.registry

import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.decoration.PaintingVariant
import net.minecraft.world.item.Item
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.levelgen.presets.WorldPreset
import net.minecraft.world.level.material.Fluid
import java.util.function.Supplier

class TagRegistry(modId: String) {
  private val BLOCK_TAGS: MutableMap<TagKey<Block>, MutableList<BlockEntry<*>>> = mutableMapOf()
  private val ITEM_TAGS: MutableMap<TagKey<Item>, MutableList<ItemEntry<*>>> = mutableMapOf()
  private val FLUID_TAGS: MutableMap<TagKey<Fluid>, MutableList<Supplier<Fluid>>> = mutableMapOf()
  private val BIOME_TAGS: MutableMap<TagKey<Biome>, MutableList<ResourceKey<Biome>>> = mutableMapOf()
  private val ENTITY_TAGS: MutableMap<TagKey<EntityType<*>>, MutableList<Supplier<EntityType<*>>>> = mutableMapOf()
  private val PAINTING_TAGS: MutableMap<TagKey<PaintingVariant>, MutableList<Supplier<PaintingVariant>>> =
    mutableMapOf()
  private val WORLD_PRESET_TAGS: MutableMap<TagKey<WorldPreset>, MutableList<Supplier<WorldPreset>>> = mutableMapOf()


  // BLOCK
  fun addBlock(tagKey: TagKey<Block>, block: BlockEntry<*>) {
    BLOCK_TAGS.computeIfAbsent(tagKey) { mutableListOf() }.add(block)
  }

  fun getBlockTags(): MutableMap<TagKey<Block>, MutableList<BlockEntry<*>>> {
    return BLOCK_TAGS
  }

  // ITEM
  fun addItem(tagKey: TagKey<Item>, item: ItemEntry<*>) {
    ITEM_TAGS.computeIfAbsent(tagKey) { mutableListOf() }.add(item)
  }

  fun getItemTags(): MutableMap<TagKey<Item>, MutableList<ItemEntry<*>>> {
    return ITEM_TAGS
  }


  // FLUID
  fun addFluid(tagKey: TagKey<Fluid>, fluid: Supplier<Fluid>) {
    FLUID_TAGS.computeIfAbsent(tagKey) { mutableListOf() }.add(fluid)
  }

  fun getFluidTags(): MutableMap<TagKey<Fluid>, MutableList<Supplier<Fluid>>> {
    return FLUID_TAGS
  }

  // BIOME
  fun addBiome(tagKey: TagKey<Biome>, biome: ResourceKey<Biome>) {
    BIOME_TAGS.computeIfAbsent(tagKey) { mutableListOf() }.add(biome)
  }

  fun getBiomeTags(): MutableMap<TagKey<Biome>, MutableList<ResourceKey<Biome>>> {
    return BIOME_TAGS
  }

  // ENTITY
  fun addEntity(tagKey: TagKey<EntityType<*>>, entity: Supplier<EntityType<*>>) {
    ENTITY_TAGS.computeIfAbsent(tagKey) { mutableListOf() }.add(entity)
  }

  fun getEntityTags(): MutableMap<TagKey<EntityType<*>>, MutableList<Supplier<EntityType<*>>>> {
    return ENTITY_TAGS
  }

  // PAINTING
  fun addPainting(tagKey: TagKey<PaintingVariant>, painting: Supplier<PaintingVariant>) {
    PAINTING_TAGS.computeIfAbsent(tagKey) { mutableListOf() }.add(painting)
  }

  fun getPaintingTags(): MutableMap<TagKey<PaintingVariant>, MutableList<Supplier<PaintingVariant>>> {
    return PAINTING_TAGS
  }

  // WORLD PRESET
  fun addWorldPreset(tagKey: TagKey<WorldPreset>, preset: Supplier<WorldPreset>) {
    WORLD_PRESET_TAGS.computeIfAbsent(tagKey) { mutableListOf() }.add(preset)
  }

  fun getWorldPresetTags(): MutableMap<TagKey<WorldPreset>, MutableList<Supplier<WorldPreset>>> {
    return WORLD_PRESET_TAGS
  }
}