package com.dannbrown.databoxlib.datagen.planets

import com.dannbrown.databoxlib.content.core.utils.CodecUtils
import com.dannbrown.databoxlib.content.utils.GravityManager
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.level.Level
import org.joml.Vector3f
import java.util.*

class PlanetCodec(
  val dimension: ResourceKey<Level>,
  val atmosphere: AtmosphereType = AtmosphereType.NONE,
  val gravity: Float = GravityManager.DEFAULT_GRAVITY,
  val temperature: Short = 0,
  val pressure: Short = 0,
  val skybox: Optional<SkyBoxProperties> = Optional.empty(),
  val has_skybox: Optional<Boolean> = Optional.empty()
) {
  companion object {
    val CODEC = RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<PlanetCodec> ->
      instance.group(
        ResourceKey.codec(Registries.DIMENSION)
          .fieldOf("dimension")
          .forGetter(PlanetCodec::dimension),
        AtmosphereType.CODEC
          .fieldOf("atmosphere")
          .forGetter(PlanetCodec::atmosphere),
        Codec.FLOAT
          .fieldOf("gravity")
          .forGetter(PlanetCodec::gravity),
        Codec.SHORT
          .fieldOf("temperature")
          .forGetter(PlanetCodec::temperature),
        Codec.SHORT
          .fieldOf("pressure")
          .forGetter(PlanetCodec::pressure),
        SkyBoxProperties.CODEC
          .optionalFieldOf("skybox")
          .forGetter(PlanetCodec::skybox),
        Codec.BOOL.optionalFieldOf("has_skybox")
          .forGetter(PlanetCodec::has_skybox)
      )
        .apply(instance, ::PlanetCodec)
    }
  }

  enum class AtmosphereType {
    NONE,
    OXYGEN,
    FLAMMABLE,
    TOXIC,
    HIGH_PRESSURE,
    COLD;

    companion object {
      val CODEC: Codec<AtmosphereType> = CodecUtils.createEnumCodec(AtmosphereType::class.java)
    }
  }

  enum class WeatherEffects {
    NONE,
    VANILLA,
    VENUS;

    companion object {
      val CODEC: Codec<WeatherEffects> = CodecUtils.createEnumCodec(WeatherEffects::class.java)
    }
  }

  enum class RenderTypeProperty {
    STATIC, // Never moves.
    DYNAMIC, // Moves based on the time of day.
    SCALING, // Scales based on the position away from the player.
    DEBUG; // Only for testing while in a debug environment without restarting Minecraft.

    companion object {
      val CODEC: Codec<RenderTypeProperty> = CodecUtils.createEnumCodec(RenderTypeProperty::class.java)
    }
  }

  class ColorInstance(val r: Int, val g: Int, val b: Int, val a: Int) {
    companion object {
      val CODEC: Codec<ColorInstance> = RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<ColorInstance> ->
        instance.group(
          Codec.INT.fieldOf("r")
            .forGetter(ColorInstance::r),
          Codec.INT.fieldOf("g")
            .forGetter(ColorInstance::g),
          Codec.INT.fieldOf("b")
            .forGetter(ColorInstance::b),
          Codec.INT.fieldOf("a")
            .forGetter(ColorInstance::a)
        )
          .apply(instance, ::ColorInstance)
      }
    }
  }

  class StarsRenderer(val fancyStars: Int, val fastStars: Int, val randomColors: Boolean, val daylightVisible: Boolean) {
    companion object {
      val CODEC: Codec<StarsRenderer> = RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<StarsRenderer> ->
        instance.group(
          Codec.INT.fieldOf("fancy_count")
            .forGetter(StarsRenderer::fancyStars),
          Codec.INT.fieldOf("fast_count")
            .forGetter(StarsRenderer::fastStars),
          Codec.BOOL.fieldOf("random_colors")
            .forGetter(StarsRenderer::randomColors),
          Codec.BOOL.fieldOf("daylight_visible")
            .forGetter(StarsRenderer::daylightVisible)
        )
          .apply(instance, ::StarsRenderer)
      }
    }
  }

  class SkyObject(val texture: ResourceLocation, val blending: Boolean, val renderType: RenderTypeProperty, val rotation: Vector3f, val scale: Float, val color: ColorInstance = ColorInstance(255, 255, 255, 255)) {
    companion object {
      val CODEC: Codec<SkyObject> = RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<SkyObject> ->
        instance.group(
          ResourceLocation.CODEC.fieldOf("texture")
            .forGetter(SkyObject::texture),
          Codec.BOOL.fieldOf("blending")
            .forGetter(SkyObject::blending),
          RenderTypeProperty.CODEC.fieldOf("render_type")
            .forGetter(SkyObject::renderType),
          ExtraCodecs.VECTOR3F.fieldOf("rotation")
            .forGetter(SkyObject::rotation),
          Codec.FLOAT.fieldOf("scale")
            .forGetter(SkyObject::scale),
          ColorInstance.CODEC.fieldOf("color")
            .forGetter(SkyObject::color)
        )
          .apply(instance, ::SkyObject)
      }
    }
  }

  // Skybox properties
  class SkyBoxProperties {
    var weatherEffects: WeatherEffects = WeatherEffects.NONE
    var hasClouds: Boolean = false
    var cloudHeight: Float = 128f
    var skyObjects: List<SkyObject> = ArrayList()
    var starsRenderer: StarsRenderer = StarsRenderer(0, 0, false, false)
    var sunsetColor: ColorInstance = ColorInstance(255, 255, 255, 255)
    fun setWeatherEffects(weatherEffects: WeatherEffects): SkyBoxProperties {
      this.weatherEffects = weatherEffects
      return this
    }

    fun setHasClouds(hasClouds: Boolean = true): SkyBoxProperties {
      this.hasClouds = hasClouds
      return this
    }

    fun setCloudHeight(cloudHeight: Float): SkyBoxProperties {
      this.cloudHeight = cloudHeight
      return this
    }

    fun setSkyObjects(skyObjects: List<SkyObject>): SkyBoxProperties {
      this.skyObjects = skyObjects
      return this
    }

    fun setStarsRenderer(starsRenderer: StarsRenderer): SkyBoxProperties {
      this.starsRenderer = starsRenderer
      return this
    }

    fun setSunsetColor(sunsetColor: ColorInstance): SkyBoxProperties {
      this.sunsetColor = sunsetColor
      return this
    }

    companion object {
      val CODEC: Codec<SkyBoxProperties> = RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<SkyBoxProperties> ->
        instance.group(
          WeatherEffects.CODEC
            .fieldOf("weather_effects")
            .forGetter(SkyBoxProperties::weatherEffects),
          Codec.BOOL
            .fieldOf("has_clouds")
            .orElse(true)
            .forGetter(SkyBoxProperties::hasClouds),
          Codec.FLOAT
            .fieldOf("cloud_height")
            .orElse(128f)
            .forGetter(SkyBoxProperties::cloudHeight),
          SkyObject.CODEC.listOf()
            .fieldOf("sky_objects")
            .forGetter { skyBoxProperties: SkyBoxProperties -> skyBoxProperties.skyObjects },
          StarsRenderer.CODEC
            .fieldOf("stars_renderer")
            .forGetter { skyBoxProperties: SkyBoxProperties -> skyBoxProperties.starsRenderer },
          ColorInstance.CODEC
            .fieldOf("sunset_color")
            .forGetter { skyBoxProperties: SkyBoxProperties -> skyBoxProperties.sunsetColor },
        )
          .apply(instance) { weatherEffects: WeatherEffects, hasClouds: Boolean, cloudHeight: Float, skyObjects: MutableList<SkyObject>, starsRenderer: StarsRenderer, sunsetColor: ColorInstance ->
            SkyBoxProperties()
              .setWeatherEffects(weatherEffects)
              .setHasClouds(hasClouds)
              .setCloudHeight(cloudHeight)
              .setSkyObjects(skyObjects)
              .setStarsRenderer(starsRenderer)
              .setSunsetColor(sunsetColor)
          }
      }
    }
    // ----
  }
  // ----
}


