package com.dannbrown.deltaboxlib.registrate.providers.sounds

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import com.google.gson.JsonObject
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

class SoundsJsonProvider(
  private val registrate: AbstractDeltaboxRegistrate,
  private val packOutput: PackOutput
) : DataProvider {
  private val pathProvider = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "")

  override fun getName(): String = "Sounds JSON Provider for: ${registrate.soundRegistry.modId}"

  override fun run(cachedOutput: CachedOutput): CompletableFuture<*> {
    val soundsJson = JsonObject()

    for ((id, variants) in registrate.soundRegistry.getVariants()) {
      val soundObject = JsonObject()
      soundObject.addProperty("subtitle", "sounds.${registrate.soundRegistry.modId}.$id")

      val soundsArray = mutableListOf<String>()
      for (i in 1..variants) {
        soundsArray.add("${registrate.soundRegistry.modId}:${id}_$i")
      }

      val soundsJsonArray = com.google.gson.JsonArray()
      soundsArray.forEach { soundsJsonArray.add(it) }
      soundObject.add("sounds", soundsJsonArray)

      soundsJson.add(id, soundObject)
    }

    val outputPath: Path = pathProvider.json(DeltaboxUtil.resourceLocation(registrate.soundRegistry.modId, "sounds"))
    return DataProvider.saveStable(cachedOutput, soundsJson, outputPath)
  }
}