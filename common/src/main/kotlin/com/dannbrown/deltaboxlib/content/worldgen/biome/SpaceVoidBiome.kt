package com.dannbrown.deltaboxlib.content.worldgen.biome

import com.dannbrown.deltaboxlib.init.DeltaboxLibMod
import com.dannbrown.deltaboxlib.registrate.util.AbstractBiome
import com.dannbrown.deltaboxlib.registrate.util.BiomeFeaturePresets
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.biome.AmbientMoodSettings
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.BiomeGenerationSettings
import net.minecraft.world.level.biome.BiomeSpecialEffects
import net.minecraft.world.level.biome.MobSpawnSettings

object SpaceVoidBiome : AbstractBiome() {
  override val biomeId: String = "space_void"
  override val BIOME_KEY: ResourceKey<Biome> =
    ResourceKey.create(Registries.BIOME, ResourceLocation(DeltaboxLibMod.MOD_ID, biomeId))

  override fun createBiome(context: BootstapContext<Biome>): Biome {
    val placedFeatures = context.lookup(Registries.PLACED_FEATURE)
    val caveGetter = context.lookup(Registries.CONFIGURED_CARVER)

    val generationSettings = BiomeGenerationSettings.Builder(placedFeatures, caveGetter)
    // raw generation
//      .addFeature(GenerationStep.Decoration.RAW_GENERATION, MoonPlacedFeatures.CRATER_HOLES_PLACED)
    // lakes
//      .addFeature(GenerationStep.Decoration.LAKES, MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND)
    // local modifcations
    // .....
    // underground structures
//      .addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, CavePlacements.MONSTER_ROOM)
//      .addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, CavePlacements.MONSTER_ROOM_DEEP)
    // surface structures
    // .....
    // strongholds
    // .....
    // underground decorations
    // fluid springs
    // vegetal decorations
    // carvers
    val mobSpawnSettings: MobSpawnSettings = MobSpawnSettings.EMPTY
    val biomeSpecialEffects: BiomeSpecialEffects =
      BiomeFeaturePresets.generateColors(BiomeSpecialEffects.Builder(), 262, 4473667)
        .fogColor(0)
        .waterColor(4159204)
        .waterFogColor(329011)
        .skyColor(0)
        .foliageColorOverride(4477251)
        .grassColorOverride(4473667)
        .ambientMoodSound(AmbientMoodSettings(SoundEvents.AMBIENT_CAVE, 6000, 8, 2.0))
        .build()

    return Biome.BiomeBuilder()
      .generationSettings(generationSettings.build())
      .mobSpawnSettings(mobSpawnSettings)
      .specialEffects(biomeSpecialEffects)
      .hasPrecipitation(false)
      .downfall(0.0F)
      .temperature(0.5F)
      .build()
  }
}