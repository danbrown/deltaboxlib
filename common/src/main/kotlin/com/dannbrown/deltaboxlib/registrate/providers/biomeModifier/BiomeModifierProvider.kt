package com.dannbrown.deltaboxlib.registrate.providers.biomeModifier

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

class BiomeModifierProvider(
  private val registrate: AbstractDeltaboxRegistrate,
  private val packOutput: PackOutput
) : DataProvider {

  // Path providers for both Forge and NeoForge directories
  private val forgePathProvider =
    packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "forge/biome_modifier")

  private val neoforgePathProvider =
    packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "neoforge/biome_modifier")

  override fun getName(): String = "Biome Modifiers Datagen for: ${registrate.modId}"

  override fun run(cachedOutput: CachedOutput): CompletableFuture<*> {
    val futures = mutableListOf<CompletableFuture<*>>()

    // Iterate over your biome modifiers and generate JSON
    for ((modifierName, biomeModifier) in registrate.biomeModifierRegistry.getBiomeModifiers()) {
      // Paths for both forge and neoforge
      val forgeModifierPath = forgePathProvider.json(DeltaboxUtil.resourceLocation(registrate.modId, modifierName))
      val neoforgeModifierPath =
        neoforgePathProvider.json(DeltaboxUtil.resourceLocation(registrate.modId, modifierName))

      // Add tasks for saving in both directories
      futures.add(saveBiomeModifierData(cachedOutput, forgeModifierPath, biomeModifier, "forge"))
      futures.add(saveBiomeModifierData(cachedOutput, neoforgeModifierPath, biomeModifier, "neoforge"))
    }

    // Wait for all save tasks to complete
    return CompletableFuture.allOf(*futures.toTypedArray())
  }

  private fun saveBiomeModifierData(
    cachedOutput: CachedOutput,
    path: Path,
    biomeModifier: BiomeModifierCodec,
    modLoader: String
  ): CompletableFuture<*> {
    return try {
      // Use the custom serializer to generate the JSON format
      val jsonObject = BiomeModifierCodec.serializeToJson(biomeModifier, modLoader)

      // Save the JSON structure to the correct path
      DataProvider.saveStable(cachedOutput, jsonObject, path)
    } catch (e: Exception) {
      // Handle the error (e.g., log it)
      throw e
    }
  }
}
