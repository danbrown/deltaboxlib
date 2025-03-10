package com.dannbrown.deltaboxlib.registrate.registry

import com.mojang.serialization.Codec
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType
import java.util.function.Supplier

class PlacerTypeRegistry(modId: String) {
  private val foliageTypes = DeferredRegister.create(modId, Registries.FOLIAGE_PLACER_TYPE)
  private val trunkTypes = DeferredRegister.create(modId, Registries.TRUNK_PLACER_TYPE)
  private val treeDecorators = DeferredRegister.create(modId, Registries.TREE_DECORATOR_TYPE)

  fun registerTrunk(
    id: String,
    codec: Supplier<Codec<out TrunkPlacer>>
  ): RegistrySupplier<TrunkPlacerType<out TrunkPlacer>> {
    return trunkTypes.register(id) { TrunkPlacerType(codec.get()) }
  }

  fun registerFoliage(
    id: String,
    codec: Supplier<Codec<out FoliagePlacer>>
  ): RegistrySupplier<FoliagePlacerType<out FoliagePlacer>> {
    return foliageTypes.register(id) { FoliagePlacerType(codec.get()) }
  }

  fun registerTreeDecorator(
    id: String, codec: Supplier<Codec<out TreeDecorator>>
  ): RegistrySupplier<TreeDecoratorType<out TreeDecorator>> {
    return treeDecorators.register(id) { TreeDecoratorType(codec.get()) }
  }

  fun build() {
    trunkTypes.register()
    foliageTypes.register()
    treeDecorators.register()
  }
}