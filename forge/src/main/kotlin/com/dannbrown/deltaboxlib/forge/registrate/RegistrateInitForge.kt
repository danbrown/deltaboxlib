package com.dannbrown.deltaboxlib.forge.registrate

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.helpers.StripHelper
import com.dannbrown.deltaboxlib.registrate.providers.trades.WandererTradeRarity
import com.dannbrown.deltaboxlib.registrate.registry.ParticleRegistry
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import net.minecraft.client.model.BoatModel
import net.minecraft.client.model.ChestBoatModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.renderer.BiomeColors
import net.minecraft.client.renderer.Sheets
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.EntityRenderers
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.BlockItem
import net.minecraft.world.level.FoliageColor
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.ComposterBlock
import net.minecraft.world.level.block.FlowerPotBlock
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraftforge.client.event.EntityRenderersEvent
import net.minecraftforge.client.event.RegisterColorHandlersEvent
import net.minecraftforge.client.event.RegisterParticleProvidersEvent
import javax.management.BadAttributeValueExpException

class RegistrateInitForge(val registrate: AbstractDeltaboxRegistrate) {
  fun init() {
  }

  fun setup() {
    registerStrippableBlocks()
    registerPottedBlocks()
    registerComposterBlocks()
  }

  fun clientSetup() {
    registerWoodTypes()
    registerEntityRenderers()
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
            DeltaboxUtil.getBlockModId(plant.get()),
            DeltaboxUtil.getBlockId(plant.get())
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

  private fun registerWoodTypes() {
    for ((key, woodType) in registrate.woodTypesRegistry.getAllWoodTypes()) {
      Sheets.addWoodType(woodType)
    }
  }

  private fun registerEntityRenderers() {
    for (entityBuilder in registrate.entityTypeRegistry.entries) {
      EntityRenderers.register(entityBuilder.getEntity().get()) { ctx ->
        entityBuilder.getRenderer(ctx)
      }
    }
  }

  fun onRegisterParticleRenders(event: RegisterParticleProvidersEvent) {
    for (particle in registrate.particleRegistry.getParticles()) {
      handleParticleRegistration(event, particle)
    }
  }

  fun onRegisterLayerDefinitions(event: EntityRenderersEvent.RegisterLayerDefinitions) {
    for (boatVariant in registrate.boatVariantRegistry.getBoatVariants()) {
      event.registerLayerDefinition(
        ModelLayerLocation(
          DeltaboxUtil.resourceLocation(registrate.modId, "boat/${boatVariant}"),
          "main"
        ), BoatModel::createBodyModel
      );
      event.registerLayerDefinition(
        ModelLayerLocation(
          DeltaboxUtil.resourceLocation(
            registrate.modId,
            "chest_boat/${boatVariant}"
          ), "main"
        ), ChestBoatModel::createBodyModel
      );
    }
    for ((path, data) in registrate.modelLayersRegistry.getModelLayers()) {
      val (model, folder) = data
      event.registerLayerDefinition(
        ModelLayerLocation(DeltaboxUtil.resourceLocation(registrate.modId, path), folder),
        model
      );
    }
  }

  private fun <T : ParticleOptions> handleParticleRegistration(
    event: RegisterParticleProvidersEvent,
    registration: ParticleRegistry.ParticleRegistration<T>
  ) {
    event.registerSpriteSet(registration.type.get(), registration.provider)
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