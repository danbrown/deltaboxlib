package com.dannbrown.deltaboxlib.sample.datagen

import com.dannbrown.deltaboxlib.sample.datagen.lang.SampleLangGen
import com.dannbrown.deltaboxlib.DeltaboxLib
import com.dannbrown.deltaboxlib.registry.datagen.DatagenRootInterface
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.data.PackOutput
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider
import net.minecraftforge.data.event.GatherDataEvent
import java.util.concurrent.CompletableFuture

class SampleDatagen(output: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : DatapackBuiltinEntriesProvider(output, future, BUILDER, modIds){
  companion object: DatagenRootInterface{
    override val modIds: MutableSet<String> = mutableSetOf(
      DeltaboxLib.MOD_ID
    )
    override val BUILDER: RegistrySetBuilder = RegistrySetBuilder()

    override fun gatherData(event: GatherDataEvent) {
      val generator = event.generator
      val packOutput = generator.packOutput
      val lookupProvider = event.lookupProvider
      val existingFileHelper = event.existingFileHelper
      // Builder generators above
      generator.addProvider(event.includeServer(), SampleDatagen(packOutput, lookupProvider))
      // Langs
      SampleLangGen.addStaticLangs(event.includeClient())
    }
  }
}