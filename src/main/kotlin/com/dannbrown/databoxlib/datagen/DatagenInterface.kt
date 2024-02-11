package com.dannbrown.databoxlib.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.data.PackOutput
import net.minecraftforge.data.event.GatherDataEvent
import java.util.concurrent.CompletableFuture





interface DatagenInterface {
  val modIds: MutableSet<String>
  val BUILDER: RegistrySetBuilder

  fun gatherData(event: GatherDataEvent)
}