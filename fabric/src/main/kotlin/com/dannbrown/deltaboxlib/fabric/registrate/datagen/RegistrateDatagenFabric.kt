package com.dannbrown.deltaboxlib.fabric.registrate.datagen

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.datagen.RegistrateBlockLootTables
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLootTableProvider
import net.fabricmc.fabric.impl.datagen.loot.FabricLootTableProviderImpl
import net.minecraft.data.CachedOutput
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import java.util.concurrent.CompletableFuture

object RegistrateDatagenFabric {
  fun buildDatagenResources(pack: FabricDataGenerator.Pack, registrate: AbstractDeltaboxRegistrate) {

    // Block Loot Tables
    pack.addProvider { dataOutput, _ ->
      object : RegistrateBlockLootTables(registrate), FabricLootTableProvider {
        override fun generate() {
          for (block in registrate.blockRegistry.entries) {
            block.lootTableFactory?.invoke(this, block.blockInstance!!)
            println("Generated loot table for ${block.blockInstance!!.get().name}")
          }
        }

        override fun run(cachedOutput: CachedOutput): CompletableFuture<*> {
          return FabricLootTableProviderImpl.run(cachedOutput, this, LootContextParamSets.BLOCK, dataOutput)
        }

        override fun getName(): String {
          return "Block Loot Tables"
        }
      }
    }
    // ----
  }
}