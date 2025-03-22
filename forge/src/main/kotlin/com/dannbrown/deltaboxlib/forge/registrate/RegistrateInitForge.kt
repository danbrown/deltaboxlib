package com.dannbrown.deltaboxlib.forge.registrate

import com.dannbrown.deltaboxlib.content.item.DeltaboxSpawnEggItem
import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.helpers.StripHelper
import com.dannbrown.deltaboxlib.registrate.registry.ParticleRegistry
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import net.minecraft.client.model.BoatModel
import net.minecraft.client.model.ChestBoatModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.renderer.BiomeColors
import net.minecraft.client.renderer.Sheets
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.BlockItem
import net.minecraft.world.level.FoliageColor
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.ComposterBlock
import net.minecraft.world.level.block.DispenserBlock
import net.minecraft.world.level.block.FlowerPotBlock
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraftforge.client.event.EntityRenderersEvent
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers
import net.minecraftforge.client.event.RegisterColorHandlersEvent
import net.minecraftforge.client.event.RegisterParticleProvidersEvent
import net.minecraftforge.common.ForgeMod
import net.minecraftforge.event.entity.EntityAttributeCreationEvent
import javax.management.BadAttributeValueExpException

class RegistrateInitForge(val registrate: AbstractDeltaboxRegistrate) {
  fun init() {
  }

  fun setup() {
    registerStrippableBlocks()
    registerPottedBlocks()
    registerComposterBlocks()
    registerDispenserBehaviors()
  }

  fun clientSetup() {
    registerWoodTypes()
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

  private fun registerDispenserBehaviors() {
    DeltaboxSpawnEggItem.MOD_EGGS.forEach { egg ->
      egg.createDispenseBehavior().let { behavior ->
        DispenserBlock.registerBehavior(egg, behavior)
      }
      DeltaboxSpawnEggItem.TYPE_MAP[egg.typeSupplier.get()] = egg
    }
    // Register items dispenser behaviors
    registrate.dispenserBehaviorRegistry.getRegistries().forEach { (t, u) ->
      DispenserBlock.registerBehavior(t.get(), u)
    }
  }


  private fun registerWoodTypes() {
    for ((key, woodType) in registrate.woodTypesRegistry.getAllWoodTypes()) {
      Sheets.addWoodType(woodType)
    }
  }

  fun onRegisterEntityRenderers(event: RegisterRenderers) {
    for (entityBuilder in registrate.entityTypeRegistry.entries) {
      if (entityBuilder.entityRenderer == null) continue
      event.registerEntityRenderer(
        entityBuilder.getEntity().get(),
        entityBuilder::getRenderer
      )
    }

    for (entityBuilder in registrate.blockEntityRegistry.entries) {
      if (entityBuilder.blockEntityRenderer == null) continue
      event.registerBlockEntityRenderer(
        entityBuilder.getBlockEntity().get(),
        entityBuilder::getRenderer
      )
    }
  }

  fun onRegisterSpawnEggColors(event: RegisterColorHandlersEvent.Item) {
    DeltaboxSpawnEggItem.MOD_EGGS.forEach { egg ->
      event.itemColors.register({ stack, layer -> egg.getColor(layer) }, egg as ItemLike)
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
      )
      event.registerLayerDefinition(
        ModelLayerLocation(
          DeltaboxUtil.resourceLocation(
            registrate.modId,
            "chest_boat/${boatVariant}"
          ), "main"
        ), ChestBoatModel::createBodyModel
      )
    }
    for ((path, data) in registrate.modelLayersRegistry.getModelLayers()) {
      val (model, modelLayer) = data
      event.registerLayerDefinition(modelLayer, model)
    }
  }

  fun onRegisterEntityAttributes(event: EntityAttributeCreationEvent) {
    for (entityBuilder in registrate.entityTypeRegistry.entries) {
      if (entityBuilder.attributeBuilderFactory == null) continue
      try {

        event.put(
          entityBuilder.getEntity().get() as EntityType<out LivingEntity>,
          entityBuilder.attributeBuilderFactory!!
            .add(ForgeMod.SWIM_SPEED.get())
            .add(ForgeMod.NAMETAG_DISTANCE.get())
            .add(ForgeMod.ENTITY_GRAVITY.get())
            .add(ForgeMod.STEP_HEIGHT_ADDITION.get())
            .build()
        )
      } catch (e: Exception) {
        println("Failed to register entity ${entityBuilder.entityId} attributs, it may not be a living entity")
      }
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