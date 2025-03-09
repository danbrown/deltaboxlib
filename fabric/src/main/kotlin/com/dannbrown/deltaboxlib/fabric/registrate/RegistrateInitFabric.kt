package com.dannbrown.deltaboxlib.fabric.registrate

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import javax.management.BadAttributeValueExpException

class RegistrateInitFabric(val registrate: AbstractDeltaboxRegistrate) {
  fun init() {
    // register flammable block
    for (block in registrate.blockRegistry.entries) {
      if (block.getContext().flammabilityBurnChance == 0 || block.getContext().flammabilitySpreadChance == 0) continue
      FlammableBlockRegistry.getDefaultInstance().add(
        block.getBlock().get(),
        block.getContext().flammabilityBurnChance,
        block.getContext().flammabilitySpreadChance
      )
    }

    // register strippable blocks
    for (block in registrate.blockRegistry.entries) {
      val other = block.getContext().strippableOther
      if (other == null) continue
      if (!other.get().defaultBlockState().hasProperty(BlockStateProperties.AXIS)
      ) throw BadAttributeValueExpException("Output stripped block should have 'axis' property!")
      if (!block.getBlock().get().defaultBlockState().hasProperty(BlockStateProperties.AXIS)
      ) throw BadAttributeValueExpException("Input stripped block should have 'axis' property!")
      StrippableBlockRegistry.register(block.getBlock().get(), other.get())
    }

    // register compostable blocks
    for (block in registrate.blockRegistry.entries) {
      val amount = block.getContext().compostableAmount
      if (amount <= 0) continue
      try {
        CompostingChanceRegistry.INSTANCE.add(block.getBlock().get().asItem(), amount)
      } catch (e: Exception) {
        println("Failed to add block ${block.getBlock().get().name} to compostables")
      }
    }
    for (item in registrate.itemRegistry.entries) {
      val amount = item.compostableAmount
      if (amount <= 0) continue
      try {
        CompostingChanceRegistry.INSTANCE.add(item.getItem().get(), amount)
      } catch (e: Exception) {
        println("Failed to add block ${item.getItem().get().descriptionId} to compostables")
      }
    }
  }

  fun initClient() {
    BlockRenderLayerMap.INSTANCE.putBlocks(
      net.minecraft.client.renderer.RenderType.cutout(),
      *registrate.blockRegistry.entries.filter { it.getContext().hasCutoutRender }.map { it.getBlock().get() }
        .toTypedArray()
    )
  }
}