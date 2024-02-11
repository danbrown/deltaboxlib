package com.dannbrown.databoxlib

import com.dannbrown.databoxlib.compat.vanilla.ProjectCompostables
import com.dannbrown.databoxlib.compat.vanilla.ProjectDispenserBehaviors
import com.dannbrown.databoxlib.compat.vanilla.ProjectFlowerPots
import com.dannbrown.databoxlib.content.worldgen.ProjectStructureTypes
import com.dannbrown.databoxlib.datagen.ProjectDatagen
import com.dannbrown.databoxlib.datagen.fuel.ProjectFuel
import com.dannbrown.databoxlib.datagen.planets.ProjectPlanets
import com.dannbrown.databoxlib.datagen.worldgen.ProjectFeatures
import com.dannbrown.databoxlib.init.ProjectAddons
import com.dannbrown.databoxlib.init.ProjectBlockEntities
import com.dannbrown.databoxlib.init.ProjectBlocks
import com.dannbrown.databoxlib.init.ProjectCarvers
import com.dannbrown.databoxlib.init.ProjectClientEvents
import com.dannbrown.databoxlib.init.ProjectCreativeTabs
import com.dannbrown.databoxlib.init.ProjectEntityTypes
import com.dannbrown.databoxlib.init.ProjectFluids
import com.dannbrown.databoxlib.init.ProjectItems
import com.dannbrown.databoxlib.init.ProjectNetworking
import com.dannbrown.databoxlib.init.ProjectPartialModels
import com.dannbrown.databoxlib.init.ProjectRecipeTypes
import com.dannbrown.databoxlib.init.ProjectScreens
import com.dannbrown.databoxlib.init.ProjectTrunkPlacerType
import com.dannbrown.databoxlib.init.ProjectWoodTypes
import com.simibubi.create.foundation.data.CreateRegistrate
import com.simibubi.create.foundation.item.ItemDescription
import com.simibubi.create.foundation.item.KineticStats
import com.simibubi.create.foundation.item.TooltipHelper
import com.simibubi.create.foundation.item.TooltipModifier
import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.client.renderer.blockentity.SignRenderer
import net.minecraft.client.renderer.entity.EntityRenderers
import net.minecraft.client.renderer.entity.ThrownItemRenderer
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.state.properties.WoodType
import net.minecraft.world.level.material.Fluid
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions
import net.minecraftforge.client.event.InputEvent
import net.minecraftforge.client.event.RegisterKeyMappingsEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.data.event.GatherDataEvent
import net.minecraftforge.event.AddReloadListenerEvent
import net.minecraftforge.event.server.ServerStartingEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.ModList
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import org.apache.logging.log4j.LogManager
import software.bernie.geckolib.GeckoLib
import java.util.function.BiConsumer

@Mod(ProjectContent.MOD_ID)
class ProjectContent {
  companion object {
    const val MOD_ID = "databoxlib"
    const val NAME = "Databox"
    val LOGGER = LogManager.getLogger()

    // Create mod registrate instance
    val REGISTRATE: CreateRegistrate = instanceRegistrate(MOD_ID)

    fun instanceRegistrate(modId: String): CreateRegistrate {
      return CreateRegistrate.create(modId)
        .setTooltipModifierFactory { item: Item? ->
          ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE)
            .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
        }
    }

    private var hasInitializedRenderers = false

    // mod compatibility
    val isAlexsMobsLoaded: Boolean = ModList.get()
      .isLoaded("alexsmobs")
    val isEnemyExpansionLoaded: Boolean = ModList.get()
      .isLoaded("enemyexpansion")

    // init coupled addons (other addons should be loaded in their respective mods)
    fun register(modBus: IEventBus, forgeEventBus: IEventBus) {
      LOGGER.info("$MOD_ID has started!")
      // databoxlib registers
      ProjectItems.register(modBus)
      ProjectBlocks.register(modBus)
      ProjectFluids.register(modBus)
      ProjectEntityTypes.register(modBus)
      ProjectBlockEntities.register(modBus)
      ProjectRecipeTypes.register(modBus)
      ProjectCarvers.register(modBus)
      ProjectFeatures.register(modBus)
      ProjectStructureTypes.register(modBus)
      ProjectTrunkPlacerType.register(modBus)
      ProjectScreens.register(modBus)
      ProjectCreativeTabs.register(modBus)
      // initialize addons
      ProjectAddons.init(modBus, forgeEventBus)
      // initialize Geckolib
      GeckoLib.initialize()
      // register create content
      REGISTRATE.registerEventListeners(modBus)
      // client and server setup
      forgeEventBus.addListener(EventPriority.HIGH) { event: AddReloadListenerEvent -> onServerReloadListeners(event) }
      forgeEventBus.addListener { event: InputEvent.Key -> ProjectClientEvents.onKeyInput(event) }
      modBus.addListener(::commonSetup)
      modBus.addListener { event: RegisterKeyMappingsEvent -> ProjectClientEvents.onKeyRegister(event) }
      modBus.addListener(EventPriority.LOWEST) { event: GatherDataEvent -> ProjectDatagen.gatherData(event) }
      // execute client only code
      DistExecutor.unsafeRunWhenOn(Dist.CLIENT) {
        Runnable {
          onClient(modBus, forgeEventBus)
        }
      }
    }

    // RUN SETUP
    private fun commonSetup(event: FMLCommonSetupEvent) {
      event.enqueueWork {
        // NETWORKING MESSAGES
        ProjectNetworking.register()
        // WOOD TYPES SHEETS
        ProjectWoodTypes.register()
        // FLOWER POTS
        ProjectFlowerPots.register()
        // COMPOSTABLES
        ProjectCompostables.register()
        // DISPENSER BEHAVIORS
        ProjectDispenserBehaviors.registerAll()
        // MENUS
        ProjectClientEvents.onRegisterMenuScreens()
      }
    }

    // RUN CLIENT
    fun onClient(modBus: IEventBus, forgeEventBus: IEventBus?) {
      if (hasInitializedRenderers) return
      //      ItemBlockRenderTypes.setRenderLayer(DataboxFluids.HYDROGEN.get(), RenderType.translucent())
      //      ItemBlockRenderTypes.setRenderLayer(DataboxFluids.HYDROGEN.getSource(), RenderType.translucent())
      onRegisterFluidRenderTypes()
      //      PonderIndex.register()
      hasInitializedRenderers = true
    }

    fun onClientSetup(event: FMLCommonSetupEvent) {
      ProjectPartialModels.init()

      WoodType.register(ProjectWoodTypes.JOSHUA)

      BlockEntityRenderers.register(ProjectBlockEntities.CUSTOM_SIGN.get(), ::SignRenderer)

      EntityRenderers.register(ProjectEntityTypes.WATER_GEL_BUBBLE_PROJECTILE.get()) { ctx -> ThrownItemRenderer(ctx) }
      EntityRenderers.register(ProjectEntityTypes.LAVA_GEL_BUBBLE_PROJECTILE.get()) { ctx -> ThrownItemRenderer(ctx) }
    }

    @SubscribeEvent
    fun onServerStarting(event: ServerStartingEvent) {
    }

    fun onServerReloadListeners(event: AddReloadListenerEvent) {
      val registry = BiConsumer<ResourceLocation, PreparableReloadListener> { id, listener -> event.addListener(listener) }
      registry.accept(ResourceLocation(MOD_ID, ProjectPlanets.PATH), ProjectPlanets.PlanetManager())
      registry.accept(ResourceLocation(MOD_ID, ProjectFuel.PATH), ProjectFuel.FuelManager())
    }

    fun hasInitializedRenderers(): Boolean {
      return hasInitializedRenderers
    }

    // OTHERS
    private fun onRegisterFluidRenderTypes() {
      for (fluid in ProjectFluids.TRANSPARENT_RENDERING) {
        ItemBlockRenderTypes.setRenderLayer(fluid.get() as Fluid, RenderType.translucent())
        ItemBlockRenderTypes.setRenderLayer(fluid.getSource() as Fluid, RenderType.translucent())
      }
    }
  }

  init {
    val eventBus = FMLJavaModLoadingContext.get().modEventBus
    val forgeEventBus = MinecraftForge.EVENT_BUS
    register(eventBus, forgeEventBus)
  }

  @EventBusSubscriber(modid = ProjectContent.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
  object ClientEvents {
    @SubscribeEvent
    fun onClientSetup(event: FMLCommonSetupEvent?) {
      ProjectContent.onClientSetup(event!!)
    }

    @SubscribeEvent
    fun registerLayers(event: RegisterLayerDefinitions?) {
    }
  }
}



