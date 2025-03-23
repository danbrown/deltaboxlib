package com.dannbrown.deltaboxlib.registrate.registry

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.builders.TagBuilder
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.decoration.PaintingVariant
import net.minecraft.world.item.Item
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.levelgen.presets.WorldPreset
import net.minecraft.world.level.material.Fluid

class TagRegistry(modId: String) {
  private val BLOCK_TAGS: MutableMap<TagKey<Block>, MutableList<TagBuilder<Block>>> = mutableMapOf()
  private val ITEM_TAGS: MutableMap<TagKey<Item>, MutableList<TagBuilder<Item>>> = mutableMapOf()
  private val FLUID_TAGS: MutableMap<TagKey<Fluid>, MutableList<TagBuilder<Fluid>>> = mutableMapOf()
  private val BIOME_TAGS: MutableMap<TagKey<Biome>, MutableList<TagBuilder<Biome>>> = mutableMapOf()
  private val ENTITY_TAGS: MutableMap<TagKey<EntityType<*>>, MutableList<TagBuilder<EntityType<*>>>> =
    mutableMapOf()
  private val PAINTING_TAGS: MutableMap<TagKey<PaintingVariant>, MutableList<TagBuilder<PaintingVariant>>> =
    mutableMapOf()
  private val WORLD_PRESET_TAGS: MutableMap<TagKey<WorldPreset>, MutableList<TagBuilder<WorldPreset>>> =
    mutableMapOf()


  // BLOCK
  fun addBlock(tagKey: TagKey<Block>, block: TagBuilder<Block>) {
    BLOCK_TAGS.computeIfAbsent(tagKey) { mutableListOf() }.add(block)
  }

  fun getBlockTags(): MutableMap<TagKey<Block>, MutableList<TagBuilder<Block>>> {
    return BLOCK_TAGS
  }

  // ITEM
  fun addItem(tagKey: TagKey<Item>, item: TagBuilder<Item>) {
    ITEM_TAGS.computeIfAbsent(tagKey) { mutableListOf() }.add(item)
  }

  fun getItemTags(): MutableMap<TagKey<Item>, MutableList<TagBuilder<Item>>> {
    return ITEM_TAGS
  }


  // FLUID
  fun addFluid(tagKey: TagKey<Fluid>, fluid: TagBuilder<Fluid>) {
    FLUID_TAGS.computeIfAbsent(tagKey) { mutableListOf() }.add(fluid)
  }

  fun getFluidTags(): MutableMap<TagKey<Fluid>, MutableList<TagBuilder<Fluid>>> {
    return FLUID_TAGS
  }

  // BIOME
  fun addBiome(tagKey: TagKey<Biome>, biome: TagBuilder<Biome>) {
    BIOME_TAGS.computeIfAbsent(tagKey) { mutableListOf() }.add(biome)
  }

  fun getBiomeTags(): MutableMap<TagKey<Biome>, MutableList<TagBuilder<Biome>>> {
    return BIOME_TAGS
  }

  // ENTITY
  fun addEntity(tagKey: TagKey<EntityType<*>>, entity: TagBuilder<EntityType<*>>) {
    ENTITY_TAGS.computeIfAbsent(tagKey) { mutableListOf() }.add(entity)
  }

  fun getEntityTags(): MutableMap<TagKey<EntityType<*>>, MutableList<TagBuilder<EntityType<*>>>> {
    return ENTITY_TAGS
  }

  // PAINTING
  fun addPainting(tagKey: TagKey<PaintingVariant>, painting: TagBuilder<PaintingVariant>) {
    PAINTING_TAGS.computeIfAbsent(tagKey) { mutableListOf() }.add(painting)
  }

  fun getPaintingTags(): MutableMap<TagKey<PaintingVariant>, MutableList<TagBuilder<PaintingVariant>>> {
    return PAINTING_TAGS
  }

  // WORLD PRESET
  fun addWorldPreset(tagKey: TagKey<WorldPreset>, preset: TagBuilder<WorldPreset>) {
    WORLD_PRESET_TAGS.computeIfAbsent(tagKey) { mutableListOf() }.add(preset)
  }

  fun getWorldPresetTags(): MutableMap<TagKey<WorldPreset>, MutableList<TagBuilder<WorldPreset>>> {
    return WORLD_PRESET_TAGS
  }
}