package com.dannbrown.databoxlib.datagen.valkyrienSkies

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.lib.LibUtils
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
import net.minecraft.world.level.block.Block
import java.io.IOException
import java.util.*
import java.util.concurrent.CompletableFuture

class ProjectMass(private val generator: DataGenerator) : DataProvider {
  companion object {
    val PATH = "vs_mass"
    private val LOGGER = LogUtils.getLogger()
    private val MASS_REGISTRY: MutableMap<ResourceKey<Block>, Float> = HashMap()
    fun register(block: Block, mass: Float) {
      val key = block.builtInRegistryHolder()
        .key()
      MASS_REGISTRY[key] = mass
    }

    fun <B : Block> register(mass: Float): NonNullConsumer<in B> {
      return NonNullConsumer { b: B -> register(b, mass) }
    }
  }

  protected val pathProvider: PackOutput.PathProvider = generator.packOutput.createPathProvider(PackOutput.Target.DATA_PACK, PATH)
  override fun getName(): String {
    return "Databox's Valkyrien Skies Mass Datagen"
  }

  override fun run(cachedOutput: CachedOutput): CompletableFuture<*> {
    val list: MutableList<CompletableFuture<*>> = ArrayList()
    val path = pathProvider.json(LibUtils.resourceLocation(ProjectContent.MOD_ID))

    try {
      val listOfObjects = mutableListOf<JsonElement>()
      for (entry in MASS_REGISTRY) {
        val codecInstance = VSObject(entry.key, Optional.of(entry.value))
        val jsonObject = VSObject.CODEC
          .encodeStart(JsonOps.INSTANCE, codecInstance)
          .getOrThrow(true, ProjectContent.LOGGER::error)
        listOfObjects.add(jsonObject)
      }
      val listJson = JsonOps.INSTANCE.createList(listOfObjects.stream())
      val completable = DataProvider.saveStable(cachedOutput, listJson, path)
      list.add(completable)
    } catch (ioexception: IOException) {
      ProjectContent.LOGGER.error("Couldn't save mass data {}", path, ioexception)
      throw ioexception
    }

    return CompletableFuture.allOf(*list.toArray { data: Int -> arrayOfNulls(data) })
  }

  class VSObject(val block: ResourceKey<Block>, val mass: Optional<Float> = Optional.empty()) {
    companion object {
      val CODEC = RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<VSObject> ->
        instance.group(
          ResourceKey.codec(Registries.BLOCK)
            .fieldOf("block")
            .forGetter(VSObject::block),
          Codec.FLOAT.optionalFieldOf("mass")
            .forGetter(VSObject::mass)
        )
          .apply(instance, ::VSObject)
      }
    }
  }
}