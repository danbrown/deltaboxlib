package com.dannbrown.databoxlib.datagen.worldgen

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.compat.ProjectModIntegrations
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BiomeTags
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraftforge.common.world.BiomeModifier
import net.minecraftforge.common.world.ForgeBiomeModifiers.AddFeaturesBiomeModifier
import net.minecraftforge.common.world.ForgeBiomeModifiers.AddSpawnsBiomeModifier
import net.minecraftforge.registries.ForgeRegistries

// This file is to add features to existing biomes
// To add features on new biomes, use the own biome's class
object ProjectBiomeModifiers {
  // Registering
  fun registerKey(name: String): ResourceKey<BiomeModifier> {
    return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation(ProjectContent.MOD_ID, name))
  }

  // Content
  val ADD_ALUMINIUM_ORE: ResourceKey<BiomeModifier> = registerKey("add_aluminium_ore")
  val ADD_TIN_ORE: ResourceKey<BiomeModifier> = registerKey("add_tin_ore")
  val ADD_TUNGSTEN_ORE: ResourceKey<BiomeModifier> = registerKey("add_tungsten_ore")
  val ADD_BASALT_ADAMANTIUM_ORE: ResourceKey<BiomeModifier> = registerKey("add_basalt_adamantium_ore")
  val ADD_JOSHUA_TREE: ResourceKey<BiomeModifier> = registerKey("add_joshua_tree")
  val ADD_MOON_BLOB_BOULDER: ResourceKey<BiomeModifier> = registerKey("add_moon_blob_boulder")
  val ADD_MOON_HOLES: ResourceKey<BiomeModifier> = registerKey("add_moon_holes")
  fun bootstrap(context: BootstapContext<BiomeModifier>) {
    val biomeLookup: HolderGetter<Biome> = context.lookup(Registries.BIOME)
    val isOverworld: HolderSet<Biome> = biomeLookup.getOrThrow(BiomeTags.IS_OVERWORLD)
    val isNether: HolderSet<Biome> = biomeLookup.getOrThrow(BiomeTags.IS_NETHER)
    val featureLookup: HolderGetter<PlacedFeature> = context.lookup(Registries.PLACED_FEATURE)
    // add aluminium ore to overworld
    val aluminiumOrePlaced: Holder<PlacedFeature> = featureLookup.getOrThrow(ProjectPlacedFeatures.ALUMINIUM_ORE_PLACED)
    context.register(ADD_ALUMINIUM_ORE, addOre(isOverworld, aluminiumOrePlaced))
    // add tin ore to overworld
//    val tinOrePlaced: Holder<PlacedFeature> = featureLookup.getOrThrow(DataboxPlacedFeatures.TIN_ORE_PLACED)
//    context.register(ADD_TIN_ORE, addOre(isOverworld, tinOrePlaced))
    // add tungsten ore to overworld
    val tungstenOrePlaced: Holder<PlacedFeature> = featureLookup.getOrThrow(ProjectPlacedFeatures.TUNGSTEN_ORE_PLACED)
    context.register(ADD_TUNGSTEN_ORE, addOre(isOverworld, tungstenOrePlaced))
    // add basalt adamantium ore to nether
//    val basaltAdamantiumOrePlaced: Holder<PlacedFeature> =
//      featureLookup.getOrThrow(DataboxPlacedFeatures.BASALT_ADAMANTIUM_ORE_PLACED)
//    context.register(ADD_BASALT_ADAMANTIUM_ORE, addOre(isNether, basaltAdamantiumOrePlaced))
    // add joshua tree to plains biome
//    val joshuaTreePlaced: Holder<PlacedFeature> = featureLookup.getOrThrow(DataboxPlacedFeatures.JOSHUA_PLACED)
//    context.register(ADD_JOSHUA_TREE, addVegetation(biomeLookup.getOrThrow(Tags.Biomes.IS_PLAINS), joshuaTreePlaced))
    // add moon blob boulder to overworld
    // TODO: use it to make placed meteorites on the overworld?
//    val moonBlobBoulderPlaced: Holder<PlacedFeature> = featureLookup.getOrThrow(DataboxPlacedFeatures.BOULDER_PLACED)
//    context.register(ADD_MOON_BLOB_BOULDER, addBlobs(biomeLookup.getOrThrow(BiomeTags.IS_OVERWORLD), moonBlobBoulderPlaced))
//    val moonHolePlaced: Holder<PlacedFeature> = featureLookup.getOrThrow(DataboxPlacedFeatures.MOON_HOLE_PLACED)
//    context.register(ADD_MOON_HOLES, addBlobs(biomeLookup.getOrThrow(BiomeTags.IS_OVERWORLD), moonHolePlaced))
    // mod compatibility
    ProjectModIntegrations.bootstrapBiomeModifiers(context)
    // ----
  }

  // @ Utils
  fun addOre(biomes: HolderSet<Biome>, feature: Holder<PlacedFeature>): AddFeaturesBiomeModifier {
    return AddFeaturesBiomeModifier(biomes, HolderSet.direct(feature), GenerationStep.Decoration.UNDERGROUND_ORES)
  }

  fun addVegetation(biomes: HolderSet<Biome>, feature: Holder<PlacedFeature>): AddFeaturesBiomeModifier {
    return AddFeaturesBiomeModifier(biomes, HolderSet.direct(feature), GenerationStep.Decoration.VEGETAL_DECORATION)
  }

  fun addRawGeneration(biomes: HolderSet<Biome>, feature: Holder<PlacedFeature>): AddFeaturesBiomeModifier {
    return AddFeaturesBiomeModifier(biomes, HolderSet.direct(feature), GenerationStep.Decoration.RAW_GENERATION)
  }

  fun addMob(biomes: HolderSet<Biome>, spawnerData: List<SpawnerData>): AddSpawnsBiomeModifier {
    return AddSpawnsBiomeModifier(biomes, spawnerData)
  }
}