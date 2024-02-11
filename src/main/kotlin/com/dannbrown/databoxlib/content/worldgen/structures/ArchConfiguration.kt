package com.dannbrown.databoxlib.content.worldgen.structures

import com.dannbrown.databoxlib.content.core.utils.BlendingFunction
import com.dannbrown.databoxlib.content.worldgen.featureConfiguration.BoulderConfig
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.util.random.SimpleWeightedRandomList
import net.minecraft.util.valueproviders.ConstantFloat
import net.minecraft.util.valueproviders.FloatProvider
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.util.valueproviders.UniformFloat
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration

@JvmRecord
data class ArchConfiguration(
  val height: IntProvider,
  val length: IntProvider,
  val width: IntProvider,
  val blendingFunction: SimpleWeightedRandomList<BlendingFunction>,
  val matchingBlendingFunctionChance: FloatProvider,
  val percentageDestroyed: FloatProvider,
  val sphereConfig: BoulderConfig,
  val biomeEnforcement: TagKey<Biome>
) :
  FeatureConfiguration {
  class Builder {
    private var height: IntProvider = UniformInt.of(35, 90)
    private var length: IntProvider = UniformInt.of(50, 250)
    private var width: IntProvider = UniformInt.of(1, 10)
    private var sphereConfig = BoulderConfig.Builder()
      .build()
    private var blendingFunction =
      SimpleWeightedRandomList.single(BlendingFunction.EASE_OUT_CUBIC)
    private var matchingBlendingFunctionChance: FloatProvider = ConstantFloat.of(0.5f)
    private var percentageDestroyed: FloatProvider = UniformFloat.of(0.45f, 0.75f)
    private var biomeEnforcement = EMPTY
    fun withHeight(height: IntProvider): Builder {
      this.height = height
      return this
    }

    fun withLength(length: IntProvider): Builder {
      this.length = length
      return this
    }

    fun withWidth(width: IntProvider): Builder {
      this.width = width
      return this
    }

    fun withSphereConfig(sphereConfig: BoulderConfig): Builder {
      this.sphereConfig = sphereConfig
      return this
    }

    fun withBiomeEnforcement(biomeEnforcement: TagKey<Biome>): Builder {
      this.biomeEnforcement = biomeEnforcement
      return this
    }

    fun withBlendingFunctionType(blendingFunction: SimpleWeightedRandomList<BlendingFunction>): Builder {
      this.blendingFunction = blendingFunction
      return this
    }

    fun withMatchingBlendingFunctionChance(matchingBlendingFunctionChance: FloatProvider): Builder {
      this.matchingBlendingFunctionChance = matchingBlendingFunctionChance
      return this
    }

    fun withPercentageDestroyed(percentageDestroyed: FloatProvider): Builder {
      this.percentageDestroyed = percentageDestroyed
      return this
    }

    fun build(): ArchConfiguration {
      return ArchConfiguration(
        height,
        length,
        width,
        blendingFunction, matchingBlendingFunctionChance, percentageDestroyed, sphereConfig, biomeEnforcement
      )
    }
  }

  companion object {
    val EMPTY = TagKey.create(Registries.BIOME, ResourceLocation("empty", "empty"))
    val CODEC: Codec<ArchConfiguration> =
      RecordCodecBuilder.create { builder: RecordCodecBuilder.Instance<ArchConfiguration> ->
        builder.group(
          IntProvider.CODEC.fieldOf("height")
            .forGetter { (height): ArchConfiguration -> height },
          IntProvider.POSITIVE_CODEC.fieldOf("length")
            .forGetter { (_, length): ArchConfiguration -> length },
          IntProvider.POSITIVE_CODEC.fieldOf("width")
            .forGetter { (_, _, width): ArchConfiguration -> width },
          SimpleWeightedRandomList.wrappedCodec(BlendingFunction.CODEC)
            .fieldOf("blending_functions")
            .forGetter { (_, _, _, blendingFunction): ArchConfiguration -> blendingFunction },
          FloatProvider.CODEC.fieldOf("matching_blending_function_chance")
            .forGetter { (_, _, _, _, matchingBlendingFunctionChance): ArchConfiguration -> matchingBlendingFunctionChance },
          FloatProvider.CODEC.fieldOf("percentage_destroyed")
            .forGetter { (_, _, _, _, _, percentageDestroyed): ArchConfiguration -> percentageDestroyed },
          BoulderConfig.CODEC.fieldOf("generation")
            .forGetter { (_, _, _, _, _, _, sphereConfig): ArchConfiguration -> sphereConfig },
          TagKey.codec(Registries.BIOME)
            .fieldOf("allowed_biomes")
            .orElse(EMPTY)
            .forGetter { (_, _, _, _, _, _, _, biomeEnforcement): ArchConfiguration -> biomeEnforcement }
        )
          .apply(
            builder
          ) { height: IntProvider, length: IntProvider, width: IntProvider, blendingFunction: SimpleWeightedRandomList<BlendingFunction>, matchingBlendingFunctionChance: FloatProvider, percentageDestroyed: FloatProvider, sphereConfig: BoulderConfig, biomeEnforcement: TagKey<Biome> ->
            ArchConfiguration(
              height,
              length,
              width,
              blendingFunction,
              matchingBlendingFunctionChance,
              percentageDestroyed,
              sphereConfig,
              biomeEnforcement
            )
          }
      }
  }
}

