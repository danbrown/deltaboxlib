package com.dannbrown.deltaboxlib.lib

import net.minecraft.core.particles.ParticleType
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.material.Fluid
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.IForgeRegistry

object LibObjects {
  fun <V> getKeyOrThrow(registry: IForgeRegistry<V>, value: V): ResourceLocation {
    val key = registry.getKey(value) ?: throw IllegalArgumentException("Could not get key for value $value!")
    return key
  }

  fun getKeyOrThrow(value: Block): ResourceLocation {
    return getKeyOrThrow(ForgeRegistries.BLOCKS, value)
  }

  fun getKeyOrThrow(value: Item): ResourceLocation {
    return getKeyOrThrow(ForgeRegistries.ITEMS, value)
  }

  fun getKeyOrThrow(value: Fluid): ResourceLocation {
    return getKeyOrThrow(ForgeRegistries.FLUIDS, value)
  }

  fun getKeyOrThrow(value: EntityType<*>): ResourceLocation {
    return getKeyOrThrow(ForgeRegistries.ENTITY_TYPES, value)
  }

  fun getKeyOrThrow(value: BlockEntityType<*>): ResourceLocation {
    return getKeyOrThrow(ForgeRegistries.BLOCK_ENTITY_TYPES, value)
  }

  fun getKeyOrThrow(value: Potion): ResourceLocation {
    return getKeyOrThrow(ForgeRegistries.POTIONS, value)
  }

  fun getKeyOrThrow(value: ParticleType<*>): ResourceLocation {
    return getKeyOrThrow(ForgeRegistries.PARTICLE_TYPES, value)
  }

  fun getKeyOrThrow(value: RecipeSerializer<*>): ResourceLocation {
    return getKeyOrThrow(ForgeRegistries.RECIPE_SERIALIZERS, value)
  }
}