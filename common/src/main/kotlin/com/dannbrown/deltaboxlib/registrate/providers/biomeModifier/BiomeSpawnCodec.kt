package com.dannbrown.deltaboxlib.registrate.providers.biomeModifier

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.biome.Biome
import com.google.gson.JsonObject
import net.minecraft.tags.TagKey

data class BiomeSpawnCodec(
  val biomeTag: TagKey<Biome>,
  val type: ResourceKey<EntityType<*>>,
  val weight: Int,
  val minCount: Int,
  val maxCount: Int
) {
  companion object {
    val CODEC: Codec<BiomeSpawnCodec> = RecordCodecBuilder.create { instance ->
      instance.group(
        TagKey.codec(Registries.BIOME).fieldOf("biomes").forGetter(BiomeSpawnCodec::biomeTag),
        ResourceKey.codec(Registries.ENTITY_TYPE).fieldOf("type").forGetter(BiomeSpawnCodec::type),
        Codec.INT.fieldOf("weight").forGetter(BiomeSpawnCodec::weight),
        Codec.INT.fieldOf("minCount").forGetter(BiomeSpawnCodec::minCount),
        Codec.INT.fieldOf("maxCount").forGetter(BiomeSpawnCodec::maxCount)
      ).apply(instance, ::BiomeSpawnCodec)
    }

    fun serializeToJson(biomeSpawn: BiomeSpawnCodec, modLoader: String): JsonObject {
      val jsonObject = JsonObject()

      // Set the correct type based on mod loader
      jsonObject.addProperty("type", "${modLoader}:add_spawns")

      // Handling biomes (single biome tag)
      jsonObject.addProperty("biomes", "#${biomeSpawn.biomeTag.location()}")

      // Handling spawners (single spawner entry)
      val spawnerObject = JsonObject()
      spawnerObject.addProperty("type", biomeSpawn.type.location().toString())
      spawnerObject.addProperty("weight", biomeSpawn.weight)
      spawnerObject.addProperty("minCount", biomeSpawn.minCount)
      spawnerObject.addProperty("maxCount", biomeSpawn.maxCount)

      jsonObject.add("spawners", spawnerObject)

      return jsonObject
    }
  }
}
