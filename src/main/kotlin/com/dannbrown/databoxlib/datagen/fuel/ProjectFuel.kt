package com.dannbrown.databoxlib.datagen.fuel

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.lib.LibUtils
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.mojang.logging.LogUtils
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.tterrag.registrate.util.nullness.NonNullConsumer
import net.minecraft.core.registries.Registries
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataGenerator
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener
import net.minecraft.util.GsonHelper
import net.minecraft.util.profiling.ProfilerFiller
import net.minecraft.world.level.material.Fluid
import net.minecraftforge.fluids.ForgeFlowingFluid
import java.io.IOException
import java.util.*
import java.util.concurrent.CompletableFuture

class ProjectFuel(private val generator: DataGenerator) : DataProvider {
  companion object {
    val PATH = "databoxlib_fuel"
    private val LOGGER = LogUtils.getLogger()
    private val FUEL_REGISTRY: MutableList<Triple<ResourceKey<Fluid>, Float, Float>> = ArrayList()
    fun register(fluid: ForgeFlowingFluid, efficiency: Float, force: Float) {
      FUEL_REGISTRY.add(Triple(fluid
        .builtInRegistryHolder()
        .key(), efficiency, force))
      FUEL_REGISTRY.add(Triple(fluid.source
        .builtInRegistryHolder()
        .key(), efficiency, force))
    }

    fun <B : ForgeFlowingFluid> register(efficiency: Float, force: Float): NonNullConsumer<in B> {
      return NonNullConsumer { b: B -> register(b, efficiency, force) }
    }
  }

  protected val pathProvider: PackOutput.PathProvider = generator.packOutput.createPathProvider(PackOutput.Target.DATA_PACK, PATH)
  override fun getName(): String {
    return "Databox's Fuel Datagen"
  }

  override fun run(cachedOutput: CachedOutput): CompletableFuture<*> {
    val list: MutableList<CompletableFuture<*>> = ArrayList()
    val path = pathProvider.json(LibUtils.resourceLocation(ProjectContent.MOD_ID))

    try {
      val listOfObjects = mutableListOf<JsonElement>()
      for (entry in FUEL_REGISTRY) {
        val codecInstance = FuelData(entry.first, Optional.of(entry.second), Optional.of(entry.third))
        val jsonObject = FuelData.CODEC
          .encodeStart(JsonOps.INSTANCE, codecInstance)
          .getOrThrow(true, ProjectContent.LOGGER::error)
        listOfObjects.add(jsonObject)
      }
      val listJson = JsonOps.INSTANCE.createList(listOfObjects.stream())
      val completable = DataProvider.saveStable(cachedOutput, listJson, path)
      list.add(completable)
    } catch (ioexception: IOException) {
      ProjectContent.LOGGER.error("Couldn't save fuel data {}", path, ioexception)
      throw ioexception
    }

    return CompletableFuture.allOf(*list.toArray { data: Int -> arrayOfNulls(data) })
  }

  class FuelData(val fluid: ResourceKey<Fluid>?, val efficiency: Optional<Float> = Optional.empty(), val force: Optional<Float> = Optional.empty()) {
    companion object {
      val CODEC = RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<FuelData> ->
        instance.group(
          ResourceKey.codec(Registries.FLUID)
            .fieldOf("fluid")
            .forGetter(FuelData::fluid),
          Codec.FLOAT.optionalFieldOf("efficiency")
            .forGetter(FuelData::efficiency),
          Codec.FLOAT.optionalFieldOf("force")
            .forGetter(FuelData::efficiency)
        )
          .apply(instance, ::FuelData)
      }

      fun empty(): FuelData {
        return FuelData(null, Optional.of(0f), Optional.of(0f))
      }
    }
  }

  // FUEL DATA
  class FuelManager : SimpleJsonResourceReloadListener(GSON, PATH) {
    companion object {
      private val GSON = GsonBuilder()
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .create()
      private val FUELS: MutableSet<FuelData> = HashSet()
      private fun updateFuelData(fuelRegistry: MutableList<FuelData>) {
        FUELS.clear()
        for (fuel in fuelRegistry) {
          FUELS.add(fuel)
        }
      }

      fun getFuels(): MutableSet<FuelData> {
        return FUELS
      }

      fun isFuel(fluid: Fluid): Boolean {
        for (fuel in FUELS) {
          if (fuel.fluid == fluid.builtInRegistryHolder()
              .key()) return true
        }
        return false
      }

      fun getFuelData(fluid: Fluid): FuelData? {
        for (fuel in FUELS) {
          if (fuel.fluid == fluid.builtInRegistryHolder()
              .key()) return fuel
        }
        return null
      }
    }

    override fun apply(pObject: MutableMap<ResourceLocation, JsonElement>, pResourceManager: ResourceManager, pProfiler: ProfilerFiller) {
      pProfiler.push("Databox fuel data deserialization")
      val fuels: MutableList<FuelData> = ArrayList()
//      pObject.values has only one element and it's a JsonArray
      val jsonObject = GsonHelper.convertToJsonArray(pObject.values.first(), "fuel")
      jsonObject.forEach { jsonElement ->
        val fuelInstance = FuelData.CODEC
          .parse(JsonOps.INSTANCE, jsonElement)
          .getOrThrow(false, ProjectContent.LOGGER::error)
        // remove the fluid from the list if it already exists
        fuels.removeIf { fuel: FuelData -> fuel.fluid == fuelInstance.fluid }
        fuels.add(fuelInstance)
        updateFuelData(fuels)
        pProfiler.pop()
      }
//      for ((resourceLocation, jsonElement) in pObject.entries) {
//        val jsonObject: JsonObject = GsonHelper.convertToJsonObject(jsonElement, "fuel")
//        val fuelInstance = FuelCodec.CODEC
//          .parse(JsonOps.INSTANCE, jsonObject)
//          .getOrThrow(false, DataboxContent.LOGGER::error)
//        // remove the fluid from the list if it already exists
//        fuels.removeIf { fuel: FuelCodec -> fuel.fluid == fuelInstance.fluid }
//        fuels.add(fuelInstance)
//        updateFuelData(fuels)
//        pProfiler.pop()
//      }
    }
    // ----- FUEL MANAGER -----
  }
}