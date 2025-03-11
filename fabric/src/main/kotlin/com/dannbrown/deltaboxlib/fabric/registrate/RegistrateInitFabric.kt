package com.dannbrown.deltaboxlib.fabric.registrate

import com.dannbrown.deltaboxlib.content.block.eyeblossom.EyeBlossomRenderer
import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.registry.ParticleRegistry
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry
import net.minecraft.client.model.BoatModel
import net.minecraft.client.model.ChestBoatModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.renderer.BiomeColors
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.FoliageColor
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import java.util.function.Function
import javax.management.BadAttributeValueExpException
import kotlin.reflect.jvm.internal.impl.resolve.calls.inference.CapturedType

class RegistrateInitFabric(val registrate: AbstractDeltaboxRegistrate) {
  fun init() {
    registerFlammableBlocks()
    registerStrippableBlocks()
    registerCompostableBlocks()
    registerBiomeModifiers()
  }

  fun initClient() {
    registerCutoutRenders()
    registerBiomeColors()
    registerParticleRenders()
    registerModelLayers()
    registerEntityRenderers()
    registerBlockEntityRenderers()
  }

  // register flammable block
  private fun registerFlammableBlocks() {
    for (block in registrate.blockRegistry.entries) {
      if (block.getContext().flammabilityBurnChance == 0 || block.getContext().flammabilitySpreadChance == 0) continue
      FlammableBlockRegistry.getDefaultInstance().add(
        block.getBlock().get(),
        block.getContext().flammabilityBurnChance,
        block.getContext().flammabilitySpreadChance
      )
    }
  }

  // register strippable blocks
  private fun registerStrippableBlocks() {
    for (block in registrate.blockRegistry.entries) {
      val other = block.getContext().strippableOther
      if (other == null) continue
      if (!other.get().defaultBlockState().hasProperty(BlockStateProperties.AXIS)
      ) throw BadAttributeValueExpException("Output stripped block should have 'axis' property!")
      if (!block.getBlock().get().defaultBlockState().hasProperty(BlockStateProperties.AXIS)
      ) throw BadAttributeValueExpException("Input stripped block should have 'axis' property!")
      StrippableBlockRegistry.register(block.getBlock().get(), other.get())
    }
  }

  // register compostable blocks
  private fun registerCompostableBlocks() {
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

  // register cutout renders
  private fun registerCutoutRenders() {
    BlockRenderLayerMap.INSTANCE.putBlocks(
      net.minecraft.client.renderer.RenderType.cutout(),
      *registrate.blockRegistry.entries.filter { it.getContext().hasCutoutRender }.map { it.getBlock().get() }
        .toTypedArray()
    )
  }

  // register biome colors
  private fun registerBiomeColors() {
    for (block in registrate.blockRegistry.entries) {
      if (!block.getContext().hasBiomeColors) continue
      try {
        ColorProviderRegistry.BLOCK.register(
          { state, level, pos, tint ->
            if (level != null && pos != null) BiomeColors.getAverageFoliageColor(
              level,
              pos
            ) else FoliageColor.getDefaultColor()
          }, block.getBlock().get()
        )
        ColorProviderRegistry.ITEM.register(
          { stack, layer ->
            val provider = ColorProviderRegistry.ITEM.get(Blocks.TALL_GRASS);
            return@register provider?.getColor(stack, layer) ?: -1
          }, block.getBlock().get().asItem()
        )
      } catch (e: Exception) {
        println("Failed to add block ${block.getBlock().get().name} to compostables")
      }
    }
  }

  private fun registerParticleRenders() {
    for (particle in registrate.particleRegistry.getParticles()) {
      handleParticleRegistration(particle)
    }
  }

  private fun registerModelLayers() {
    for (boatVariant in registrate.boatVariantRegistry.getBoatVariants()) {
      EntityModelLayerRegistry.registerModelLayer(
        ModelLayerLocation(
          DeltaboxUtil.resourceLocation(
            registrate.modId,
            "boat/${boatVariant}"
          ), "main"
        ), BoatModel::createBodyModel
      )
      EntityModelLayerRegistry.registerModelLayer(
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
      EntityModelLayerRegistry.registerModelLayer(modelLayer, { model.get() })
    }
  }

  private fun registerEntityRenderers() {
    for (entityBuilder in registrate.entityTypeRegistry.entries) {
      EntityRendererRegistry.register(entityBuilder.getEntity().get()) { ctx ->
        entityBuilder.getRenderer(ctx)
      }
    }
  }

  private fun registerBlockEntityRenderers() {
    for (entityBuilder in registrate.blockEntityRegistry.entries) {
      BlockEntityRenderers.register(entityBuilder.getBlockEntity().get()) { ctx ->
        entityBuilder.getRenderer(ctx)
      }
    }
  }

  private fun <T : ParticleOptions> handleParticleRegistration(
    registration: ParticleRegistry.ParticleRegistration<T>
  ) {
    ParticleFactoryRegistry.getInstance().register(registration.type.get(), registration.provider)
  }

  private fun registerBiomeModifiers() {
    for ((modifierName, biomeModifier) in registrate.biomeModifierRegistry.getBiomeModifiers()) {
      val biomeSelector = BiomeSelectors.tag(biomeModifier.biomeTag)
      BiomeModifications.addFeature(
        biomeSelector,
        biomeModifier.step,
        biomeModifier.feature
      )
    }

    for ((modifierName, biomeModifier) in registrate.biomeModifierRegistry.getBiomeSpawns()) {
      val biomeSelector = BiomeSelectors.tag(biomeModifier.biomeTag)
      val entity = BuiltInRegistries.ENTITY_TYPE.get(biomeModifier.type) ?: continue
      BiomeModifications.addSpawn(
        biomeSelector,
        entity.category,
        entity,
        biomeModifier.weight,
        biomeModifier.minCount,
        biomeModifier.maxCount
      )
    }
  }
}