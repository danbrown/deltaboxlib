package com.dannbrown.deltaboxlib.forge.registrate

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.helpers.StripHelper
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.ComposterBlock
import net.minecraft.world.level.block.FlowerPotBlock
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import javax.management.BadAttributeValueExpException

class RegistrateInitForge(val registrate: AbstractDeltaboxRegistrate) {
  fun init() {
  }

  fun setup() {
    // register strippable blocks
    for (block in registrate.blockRegistry.entries) {
      val other = block.getContext().strippableOther ?: continue
      if (!other.get().defaultBlockState().hasProperty(BlockStateProperties.AXIS)
      ) throw BadAttributeValueExpException("Output stripped block should have 'axis' property!")
      if (!block.getBlock().get().defaultBlockState().hasProperty(BlockStateProperties.AXIS)
      ) throw BadAttributeValueExpException("Input stripped block should have 'axis' property!")
      StripHelper.registerStrippable(block.getBlock().get(), other.get())
    }

    // register potted blocks
    for (block in registrate.blockRegistry.entries) {
      val plant = block.getContext().pottedOther
      if (plant === null) continue
      try {
        (Blocks.FLOWER_POT as FlowerPotBlock).addPlant(
          DeltaboxUtil.resourceLocation(
            DeltaboxUtil.getItemModId(plant.getItem()),
            DeltaboxUtil.getItemId(plant.getItem())
          ), block.getBlock()
        )
      } catch (e: Exception) {
        println("Failed to add plant ${plant.get().name} to flower pot ${block.getBlock().get().name}")
      }
    }

    // register composter blocks
    for (block in registrate.blockRegistry.entries) {
      val amount = block.getContext().compostableAmount
      if (amount <= 0) continue
      try {
        ComposterBlock.COMPOSTABLES.put(block.getBlock().get().asItem(), amount)
      } catch (e: Exception) {
        println("Failed to add block ${block.getBlock().get().name} to compostables")
      }
    }
    for (item in registrate.itemRegistry.entries) {
      val amount = item.compostableAmount
      if (amount <= 0) continue
      try {
        ComposterBlock.COMPOSTABLES.put(item.getItem().get(), amount)
      } catch (e: Exception) {
        println("Failed to add block ${item.getItem().get().descriptionId} to compostables")
      }
    }
  }
}