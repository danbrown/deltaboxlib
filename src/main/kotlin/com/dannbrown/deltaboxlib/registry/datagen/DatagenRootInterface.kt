package com.dannbrown.deltaboxlib.registry.datagen


import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistryAccess
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraftforge.data.event.GatherDataEvent

interface DatagenRootInterface {
  val modIds: MutableSet<String>
  val BUILDER: RegistrySetBuilder
  fun gatherData(event: GatherDataEvent)
  fun append(original: HolderLookup.Provider, builder: RegistrySetBuilder): HolderLookup.Provider {
    return builder.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), original)
  }
}