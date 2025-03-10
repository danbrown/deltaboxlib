package com.dannbrown.deltaboxlib.registrate.providers.biomeModifier

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import com.google.gson.JsonObject
import net.minecraft.tags.TagKey

data class BiomeModifierCodec(
  val biomeTag: TagKey<Biome>,
  val feature: ResourceKey<PlacedFeature>,
  val step: GenerationStep.Decoration
) {
  companion object {
    val CODEC: Codec<BiomeModifierCodec> = RecordCodecBuilder.create { instance ->
      instance.group(
        TagKey.codec(Registries.BIOME).fieldOf("biomes").forGetter(BiomeModifierCodec::biomeTag),
        ResourceKey.codec(Registries.PLACED_FEATURE).fieldOf("features").forGetter(BiomeModifierCodec::feature),
        GenerationStep.Decoration.CODEC.fieldOf("step").forGetter(BiomeModifierCodec::step)
      ).apply(instance, ::BiomeModifierCodec)
    }

    // Serializer for JSON output
    fun serializeToJson(biomeModifier: BiomeModifierCodec, modloader: String): JsonObject {
      val jsonObject = JsonObject()

      jsonObject.addProperty("type", "$modloader:add_features")

      // Handling biome tag (direct tag reference)
      jsonObject.addProperty("biomes", "#${biomeModifier.biomeTag.location()}")

      // Handling feature (single feature)
      jsonObject.addProperty("features", biomeModifier.feature.location().toString())

      // Handling step (convert to lowercase)
      jsonObject.addProperty("step", biomeModifier.step.name.lowercase())

      return jsonObject
    }
  }
}
