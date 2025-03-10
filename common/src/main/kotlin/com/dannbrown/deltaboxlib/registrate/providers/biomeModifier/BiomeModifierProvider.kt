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
  private val forgeBiomeModifierPathProvider =
    packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "forge/biome_modifier")

  private val neoforgeBiomeModifierPathProvider =
    packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "neoforge/biome_modifier")


  override fun getName(): String = "Biome Modifiers and Spawns Datagen for: ${registrate.modId}"

  override fun run(cachedOutput: CachedOutput): CompletableFuture<*> {
    val futures = mutableListOf<CompletableFuture<*>>()

    // Iterate over biome modifiers and generate JSON for both biome modifiers and biome spawns
    for ((modifierName, biomeModifier) in registrate.biomeModifierRegistry.getBiomeModifiers()) {
      // Paths for both forge and neoforge biome modifiers
      val forgeModifierPath =
        forgeBiomeModifierPathProvider.json(DeltaboxUtil.resourceLocation(registrate.modId, modifierName))
      val neoforgeModifierPath =
        neoforgeBiomeModifierPathProvider.json(DeltaboxUtil.resourceLocation(registrate.modId, modifierName))

      // Add tasks for saving in both directories for biome modifiers
      futures.add(saveBiomeModifierData(cachedOutput, forgeModifierPath, biomeModifier, "forge"))
      futures.add(saveBiomeModifierData(cachedOutput, neoforgeModifierPath, biomeModifier, "neoforge"))
    }

    // Iterate over biome spawns and generate JSON
    for ((spawnName, biomeSpawn) in registrate.biomeModifierRegistry.getBiomeSpawns()) {
      // Paths for both forge and neoforge biome spawns
      val forgeSpawnPath =
        forgeBiomeModifierPathProvider.json(DeltaboxUtil.resourceLocation(registrate.modId, spawnName))
      val neoforgeSpawnPath =
        neoforgeBiomeModifierPathProvider.json(DeltaboxUtil.resourceLocation(registrate.modId, spawnName))

      // Add tasks for saving in both directories for biome spawns
      futures.add(saveBiomeSpawnData(cachedOutput, forgeSpawnPath, biomeSpawn, "forge"))
      futures.add(saveBiomeSpawnData(cachedOutput, neoforgeSpawnPath, biomeSpawn, "neoforge"))
    }

    return CompletableFuture.allOf(*futures.toTypedArray())
  }

  // Save biome modifier data in the correct path
  private fun saveBiomeModifierData(
    cachedOutput: CachedOutput,
    path: Path,
    biomeModifier: BiomeModifierCodec,
    modLoader: String
  ): CompletableFuture<*> {
    return try {
      val jsonObject = BiomeModifierCodec.serializeToJson(biomeModifier, modLoader)
      DataProvider.saveStable(cachedOutput, jsonObject, path)
    } catch (e: Exception) {
      throw e
    }
  }

  // Save biome spawn data in the correct path
  private fun saveBiomeSpawnData(
    cachedOutput: CachedOutput,
    path: Path,
    biomeSpawn: BiomeSpawnCodec,
    modLoader: String
  ): CompletableFuture<*> {
    return try {
      val jsonObject = BiomeSpawnCodec.serializeToJson(biomeSpawn, modLoader)
      DataProvider.saveStable(cachedOutput, jsonObject, path)
    } catch (e: Exception) {
      throw e
    }
  }
}
