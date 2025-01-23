package com.dannbrown.deltaboxlib.registry.recipes

import com.mojang.serialization.JsonOps
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataGenerator
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.data.PackOutput.PathProvider
import java.io.IOException
import java.nio.file.Path
import java.util.*
import java.util.concurrent.CompletableFuture
import net.minecraft.resources.ResourceLocation
import org.apache.logging.log4j.LogManager

class BrewingGenerator(private val modId: String,  private val generator: DataGenerator, private val brewingItemList: List<BrewingCodec>) : DataProvider {
  companion object {
    val PATH = "brewing"
    val LOGGER = LogManager.getLogger()
  }

  protected val pathProvider: PathProvider = generator.packOutput.createPathProvider(PackOutput.Target.DATA_PACK, PATH)
  override fun getName(): String {
    return "DeltaboxLib's Brewing Datagen for: $modId"
  }

  // save all brewing data to datapacks
  override fun run(cachedOutput: CachedOutput): CompletableFuture<*> {
    val list: MutableList<CompletableFuture<*>> = ArrayList()
    for (brewingItem in brewingItemList) {
      val entityId = brewingItem.inputItem.item.descriptionId.split(".").last()
      list.add(saveBrewingData(cachedOutput, pathProvider.json(ResourceLocation(modId, entityId)), brewingItem))
    }
    return CompletableFuture.allOf(*list.toArray { item: Int -> arrayOfNulls(item) })
  }

  // save the brewing data to a json file
  private fun saveBrewingData(cachedOutput: CachedOutput, path: Path, brewingItem: BrewingCodec): CompletableFuture<*> {
    try {
      val codecInstance = BrewingCodec( brewingItem.inputItem, brewingItem.ingredientItem, brewingItem.outputItem)
      // encode the brewing data
      val jsonObject = BrewingCodec.CODEC
        .encodeStart(JsonOps.INSTANCE, codecInstance)
        .getOrThrow(false, LOGGER::error)
        .asJsonObject

      return DataProvider.saveStable(cachedOutput, jsonObject, path)
    } catch (ioexception: IOException) {
      LOGGER.error("Couldn't save brewing {}", path, ioexception)
      throw ioexception
    }
  }
}