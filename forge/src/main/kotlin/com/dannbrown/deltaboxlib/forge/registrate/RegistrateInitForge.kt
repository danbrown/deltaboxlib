package com.dannbrown.deltaboxlib.forge.registrate

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.helpers.StripHelper
import com.dannbrown.deltaboxlib.registrate.providers.trades.WandererTradeRarity
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import net.minecraft.client.renderer.BiomeColors
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.trading.MerchantOffer
import net.minecraft.world.level.FoliageColor
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.ComposterBlock
import net.minecraft.world.level.block.FlowerPotBlock
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraftforge.client.event.RegisterColorHandlersEvent
import javax.management.BadAttributeValueExpException

class RegistrateInitForge(val registrate: AbstractDeltaboxRegistrate) {
  fun init() {
  }

  fun setup() {
    registerStrippableBlocks()
    registerPottedBlocks()
    registerComposterBlocks()
  }

  // register strippable blocks
  private fun registerStrippableBlocks() {
    for (block in registrate.blockRegistry.entries) {
      val other = block.getContext().strippableOther ?: continue
      if (!other.get().defaultBlockState().hasProperty(BlockStateProperties.AXIS)
      ) throw BadAttributeValueExpException("Output stripped block should have 'axis' property!")
      if (!block.getBlock().get().defaultBlockState().hasProperty(BlockStateProperties.AXIS)
      ) throw BadAttributeValueExpException("Input stripped block should have 'axis' property!")
      StripHelper.registerStrippable(block.getBlock().get(), other.get())
    }
  }

  // register potted blocks
  private fun registerPottedBlocks() {
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
  }

  // register composter blocks
  private fun registerComposterBlocks() {
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

  fun onRegisterBlockBiomeColors(event: RegisterColorHandlersEvent.Block) {
    val blocks = registrate.blockRegistry.entries.filter { it.getContext().hasBiomeColors }.map { it.getBlock().get() }
    event.blockColors.register(
      { state, level, pos, tint ->
        if (level != null && pos != null) BiomeColors.getAverageFoliageColor(
          level,
          pos
        ) else FoliageColor.getDefaultColor()
      }, *blocks.toTypedArray()
    )
  }

  fun onRegisterItemBiomeColors(event: RegisterColorHandlersEvent.Item) {
    val blocks = registrate.blockRegistry.entries.filter { it.getContext().hasBiomeColors }.map { it.getBlock().get() }
    event.itemColors.register({ stack, tintIndex ->
      val state = (stack.item as BlockItem).block.defaultBlockState()
      return@register event.blockColors.getColor(state, null, null, tintIndex)
    }, *blocks.toTypedArray())
  }
}