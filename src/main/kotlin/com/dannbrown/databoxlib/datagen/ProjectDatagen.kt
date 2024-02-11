package com.dannbrown.databoxlib.datagen

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.compat.ProjectModIntegrations
import com.dannbrown.databoxlib.datagen.fuel.ProjectFuel
import com.dannbrown.databoxlib.datagen.lang.ProjectLangGen
import com.dannbrown.databoxlib.datagen.planets.ProjectPlanets
import com.dannbrown.databoxlib.datagen.recipes.ProjectRecipeGen
import com.dannbrown.databoxlib.datagen.tags.ProjectBiomeTags
import com.dannbrown.databoxlib.datagen.tags.ProjectBlockTags
import com.dannbrown.databoxlib.datagen.tags.ProjectEntityTypeTags
import com.dannbrown.databoxlib.datagen.tags.ProjectFluidTags
import com.dannbrown.databoxlib.datagen.tags.ProjectItemTags
import com.dannbrown.databoxlib.datagen.tags.ProjectWorldPresetTags
import com.dannbrown.databoxlib.datagen.valkyrienSkies.ProjectMass
import com.dannbrown.databoxlib.datagen.worldgen.ProjectBiomeModifiers
import com.dannbrown.databoxlib.datagen.worldgen.ProjectBiomes
import com.dannbrown.databoxlib.datagen.worldgen.ProjectConfiguredCarvers
import com.dannbrown.databoxlib.datagen.worldgen.ProjectConfiguredFeatures
import com.dannbrown.databoxlib.datagen.worldgen.ProjectDensityFunctions
import com.dannbrown.databoxlib.datagen.worldgen.ProjectDimensions
import com.dannbrown.databoxlib.datagen.worldgen.ProjectPlacedFeatures
import com.dannbrown.databoxlib.datagen.worldgen.ProjectStructures
import com.dannbrown.databoxlib.datagen.worldgen.ProjectWorldPresets
import com.simibubi.create.Create
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistryAccess
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider
import net.minecraftforge.data.event.GatherDataEvent
import net.minecraftforge.registries.ForgeRegistries
import java.util.concurrent.CompletableFuture

//@Mod.EventBusSubscriber(modid = DataboxContent.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
class ProjectDatagen(output: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : DatapackBuiltinEntriesProvider(output, future, BUILDER, modIds){
  companion object: DatagenInterface {
    val dependenciesIds: Array<String> = arrayOf(
      "minecraft", Create.ID, ProjectContent.MOD_ID,
    )
    override val modIds: MutableSet<String> = mutableSetOf(
      *dependenciesIds, // base mods
      *ProjectModIntegrations.getModIds(), // integrations
    )
    override val BUILDER: RegistrySetBuilder = RegistrySetBuilder()
      .add(Registries.CONFIGURED_FEATURE, ProjectConfiguredFeatures::bootstrap)
      .add(Registries.PLACED_FEATURE, ProjectPlacedFeatures::bootstrap)
      .add(Registries.DENSITY_FUNCTION, ProjectDensityFunctions::bootstrap)
      .add(Registries.BIOME, ProjectBiomes::bootstrap)
      .add(Registries.CONFIGURED_CARVER, ProjectConfiguredCarvers::bootstrap)
      .add(Registries.DIMENSION_TYPE, ProjectDimensions::bootstrapType)
      .add(Registries.LEVEL_STEM, ProjectDimensions::bootstrapStem)
      .add(Registries.NOISE_SETTINGS, ProjectDimensions::bootstrapNoise)
      .add(Registries.STRUCTURE, ProjectStructures::bootstrapStructures)
      .add(Registries.STRUCTURE_SET, ProjectStructures::bootstrapSets)
      .add(Registries.WORLD_PRESET, ProjectWorldPresets::bootstrap)
      .add(ForgeRegistries.Keys.BIOME_MODIFIERS, ProjectBiomeModifiers::bootstrap)

    override fun gatherData(event: GatherDataEvent) {
      val generator = event.generator
      val packOutput = generator.packOutput
      val lookupProvider = event.lookupProvider
      val existingFileHelper = event.existingFileHelper
      // Builder generators, mostly worldgen
      generator.addProvider(event.includeServer(), ProjectDatagen(packOutput, lookupProvider))
      // World preset tags
//      val datapackEntries = DatapackBuiltinEntriesProvider(packOutput, lookupProvider, modIds)
//      val newLookupProvider = datapackEntries.registryProvider
      generator.addProvider(
        event.includeServer(),
        ProjectWorldPresetTags(packOutput, lookupProvider.thenApply { r -> append(r, BUILDER) }, existingFileHelper)
      )
      // Biome Tags
      generator.addProvider(
        event.includeServer(),
        ProjectBiomeTags(packOutput, lookupProvider.thenApply { r -> append(r, BUILDER) }, existingFileHelper)
      )
      // Entity Type Tags
      generator.addProvider(
        event.includeServer(),
        ProjectEntityTypeTags(packOutput, lookupProvider.thenApply { r -> append(r, BUILDER) }, existingFileHelper)
      )
      // Langs
      ProjectLangGen.addStaticLangs(event.includeClient())
      // Recipes
      ProjectRecipeGen.registerAll(event.includeServer(), generator)
      generator.addProvider(event.includeServer(), ProjectPlanets(generator))
      generator.addProvider(event.includeServer(), ProjectFuel(generator))
      generator.addProvider(event.includeServer(), ProjectMass(generator))
      // FluidTags, BlockTags, ItemTag Gen
      generator.addProvider(event.includeServer(), ProjectFluidTags(packOutput, lookupProvider, existingFileHelper))
      val blockTags = ProjectBlockTags(packOutput, lookupProvider, existingFileHelper)
      generator.addProvider(event.includeServer(), blockTags)
      generator.addProvider(
        event.includeServer(),
        ProjectItemTags(packOutput, lookupProvider, blockTags.contentsGetter(), existingFileHelper)
      )
    }

    fun append(original: HolderLookup.Provider, builder: RegistrySetBuilder): HolderLookup.Provider {
      return builder.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), original)
    }
  }
}

