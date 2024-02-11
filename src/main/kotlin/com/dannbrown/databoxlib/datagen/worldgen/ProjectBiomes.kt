package com.dannbrown.databoxlib.datagen.worldgen

import com.dannbrown.databoxlib.content.utils.AbstractBiome
import com.dannbrown.databoxlib.content.worldgen.biome.LushRedSandArchesBiome
import com.dannbrown.databoxlib.content.worldgen.biome.MossySlatesBiome
import com.dannbrown.databoxlib.content.worldgen.biome.RedSandArchesBiome
import com.dannbrown.databoxlib.content.worldgen.biome.RoseateDesertBiome
import com.dannbrown.databoxlib.content.worldgen.biome.ScrapWastelandsBiome
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.world.level.biome.Biome

object ProjectBiomes {
  val BIOMES: MutableList<AbstractBiome> = ArrayList()

  init {
    // Planet Zero
    BIOMES.add(RedSandArchesBiome)
    BIOMES.add(LushRedSandArchesBiome)
    BIOMES.add(ScrapWastelandsBiome)
    BIOMES.add(RoseateDesertBiome)
    BIOMES.add(MossySlatesBiome)
  }

  fun registerBiome(biome: AbstractBiome) {
    BIOMES.add(biome)
  }

  fun bootstrap(context: BootstapContext<Biome>) {
    val placedFeatures = context.lookup(Registries.PLACED_FEATURE)
    val worldCarvers = context.lookup(Registries.CONFIGURED_CARVER)

    for (biome in BIOMES) {
      context.register(biome.BIOME_KEY, biome.createBiome(placedFeatures, worldCarvers))
    }
  }
}