package com.dannbrown.deltaboxlib.fabric.init

import com.dannbrown.deltaboxlib.fabric.registrate.RegistrateDatagenFabric
import com.dannbrown.deltaboxlib.init.DeltaboxLibMod
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.core.RegistrySetBuilder

class DeltaboxLibDatagenFabric : DataGeneratorEntrypoint {
  override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
    val pack = fabricDataGenerator.createPack()
    RegistrateDatagenFabric.buildDatagenResources(pack, DeltaboxLibMod.REGISTRATE)
  }

  override fun buildRegistry(registryBuilder: RegistrySetBuilder) {
    RegistrateDatagenFabric.buildRegistry(registryBuilder, DeltaboxLibMod.REGISTRATE)
  }
}