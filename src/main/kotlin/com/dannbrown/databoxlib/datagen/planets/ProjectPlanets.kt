package com.dannbrown.databoxlib.datagen.planets

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.content.core.renderer.ProjectSkyRenderer
import com.dannbrown.databoxlib.content.utils.AbstractDimension
import com.dannbrown.databoxlib.datagen.worldgen.ProjectDimensions
import com.dannbrown.databoxlib.lib.LibUtils
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.serialization.JsonOps
import net.minecraft.client.Camera
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.DimensionSpecialEffects
import net.minecraft.client.renderer.LightTexture
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataGenerator
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.data.PackOutput.PathProvider
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener
import net.minecraft.util.GsonHelper
import net.minecraft.util.profiling.ProfilerFiller
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import org.joml.Matrix4f
import java.io.IOException
import java.nio.file.Path
import java.util.*
import java.util.concurrent.CompletableFuture

class ProjectPlanets(private val generator: DataGenerator) : DataProvider {
  companion object {
    val PATH = "databoxlib_planets"
  }

  protected val pathProvider: PathProvider = generator.packOutput.createPathProvider(PackOutput.Target.DATA_PACK, PATH)
  override fun getName(): String {
    return "Databox's Planet Datagen"
  }

  // save all databoxlib dimensions planet data to datapacks
  override fun run(cachedOutput: CachedOutput): CompletableFuture<*> {
    val list: MutableList<CompletableFuture<*>> = ArrayList()
    for (dimension in ProjectDimensions.DIMENSIONS) {
      list.add(savePlanetData(cachedOutput, pathProvider.json(LibUtils.resourceLocation(dimension.dimensionId)), dimension))
    }
    return CompletableFuture.allOf(*list.toArray { planet: Int -> arrayOfNulls(planet) })
  }

  // save the planet data to a json file
  private fun savePlanetData(cachedOutput: CachedOutput, path: Path, dimension: AbstractDimension): CompletableFuture<*> {
    try {
      val skybox: Optional<PlanetCodec.SkyBoxProperties> = if (dimension.SKYBOX != null) Optional.of(dimension.SKYBOX!!) else Optional.empty()
      val codecInstance = PlanetCodec(dimension.LEVEL, dimension.ATMOSPHERE, dimension.GRAVITY, dimension.TEMPERATURE, dimension.PRESSURE, skybox)
      // encode the planet data
      val jsonObject = PlanetCodec.CODEC
        .encodeStart(JsonOps.INSTANCE, codecInstance)
        .getOrThrow(false, ProjectContent.LOGGER::error)
        .asJsonObject

      return DataProvider.saveStable(cachedOutput, jsonObject, path)
    } catch (ioexception: IOException) {
      ProjectContent.LOGGER.error("Couldn't save planet {}", path, ioexception)
      throw ioexception
    }
  }

  // PLANET DATA
  class PlanetManager : SimpleJsonResourceReloadListener(GSON, PATH) {
    companion object {
      private val GSON = GsonBuilder()
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .create()
      private val PLANETS: MutableSet<PlanetCodec> = HashSet()
      private fun updatePlanetData(planets: MutableList<PlanetCodec>) {
        PLANETS.clear()
        for (planet in planets) {
          PLANETS.add(planet)
        }
      }

      fun getPlanets(): MutableSet<PlanetCodec> {
        return PLANETS
      }

      fun getPlanetFromLevel(level: ResourceKey<Level>): PlanetCodec? {
        for (planet in PLANETS) {
          if (planet.dimension == level) return planet
        }
        return null
      }
    }

    override fun apply(pObject: MutableMap<ResourceLocation, JsonElement>, pResourceManager: ResourceManager, pProfiler: ProfilerFiller) {
      pProfiler.push("Databox planet data deserialization")
      val planets: MutableList<PlanetCodec> = ArrayList()
      for ((resourceLocation, jsonElement) in pObject.entries) {
        val jsonObject: JsonObject = GsonHelper.convertToJsonObject(jsonElement, "planet")
        val planetInstance = PlanetCodec.CODEC
          .parse(JsonOps.INSTANCE, jsonObject)
          .getOrThrow(false, ProjectContent.LOGGER::error)
        // remove the planet from the list if it already exists
        planets.removeIf { planet: PlanetCodec -> planet.dimension == planetInstance.dimension }
        planets.add(planetInstance)
        updatePlanetData(planets)
        pProfiler.pop()
      }
    }
    // ----- PLANET MANAGER -----
  }

  class PlanetDimensionEffects(private val skyBox: PlanetCodec.SkyBoxProperties) : DimensionSpecialEffects(if (skyBox.hasClouds) skyBox.cloudHeight else Float.NaN, true, SkyType.NORMAL, false, false) {
    private val skyRenderer: ProjectSkyRenderer = ProjectSkyRenderer(skyBox)
    override fun getBrightnessDependentFogColor(fogColor: Vec3, brightness: Float): Vec3 {
      return fogColor.multiply(
        (brightness * 0.94f + 0.06f).toDouble(),
        (brightness * 0.94f + 0.06f).toDouble(),
        (brightness * 0.91f + 0.09f).toDouble()
      )
    }

    override fun isFoggyAt(x: Int, y: Int): Boolean {
      return false
    }

    override fun renderSky(level: ClientLevel, ticks: Int, tickDelta: Float, poseStack: PoseStack, camera: Camera, projectionMatrix: Matrix4f, foggy: Boolean, setupFog: Runnable): Boolean {
      setupFog.run()
      skyRenderer.render(level, ticks, tickDelta, poseStack, camera, projectionMatrix, foggy)
      return true
    }

    override fun renderSnowAndRain(level: ClientLevel, ticks: Int, partialTick: Float, lightTexture: LightTexture, camX: Double, camY: Double, camZ: Double): Boolean {
      return if (skyBox.weatherEffects === PlanetCodec.WeatherEffects.VANILLA) super.renderSnowAndRain(level, ticks, partialTick, lightTexture, camX, camY, camZ) else false
    }
    // ----- PLANET DIMENSION EFFECTS -----
  }
  // ----- DATABOX PLANETS -----
}