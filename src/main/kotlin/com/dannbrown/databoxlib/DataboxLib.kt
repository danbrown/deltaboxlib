package com.dannbrown.databoxlib

import com.dannbrown.databoxlib.registry.DataboxRegistrate
import com.dannbrown.databoxlib.test.DataboxBlocks
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.AddReloadListenerEvent
import net.minecraftforge.event.server.ServerStartingEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import org.apache.logging.log4j.LogManager
import java.util.function.BiConsumer

@Mod(DataboxLib.MOD_ID)
class DataboxLib {
  companion object {
    const val MOD_ID = "databoxlib"
    const val NAME = "Databox Lib"
    val LOGGER = LogManager.getLogger()

    // Create mod registrate instance
    val REGISTRATE: DataboxRegistrate = instanceRegistrate(MOD_ID)

    fun instanceRegistrate(modId: String): DataboxRegistrate {
      return DataboxRegistrate.create(modId)
    }

    private var hasInitializedRenderers = false

    // init coupled addons (other addons should be loaded in their respective mods)
    fun register(modBus: IEventBus, forgeEventBus: IEventBus) {
      LOGGER.info("$MOD_ID has started!")
      // register content
      DataboxBlocks.register()
      // register all registrate event listeners
      REGISTRATE.registerEventListeners(modBus)
      // client and server setup
      forgeEventBus.addListener(EventPriority.HIGH) { event: AddReloadListenerEvent -> onServerReloadListeners(event) }
      modBus.addListener(::commonSetup)
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
    }

    @SubscribeEvent
    fun onServerStarting(event: ServerStartingEvent) {
    }

    fun onServerReloadListeners(event: AddReloadListenerEvent) {
      val registry = BiConsumer<ResourceLocation, PreparableReloadListener> { id, listener -> event.addListener(listener) }
    }

    fun hasInitializedRenderers(): Boolean {
      return hasInitializedRenderers
    }

    // OTHERS
    private fun onRegisterFluidRenderTypes() {

    }
  }

  init {
    val eventBus = FMLJavaModLoadingContext.get().modEventBus
    val forgeEventBus = MinecraftForge.EVENT_BUS
    register(eventBus, forgeEventBus)
  }

  @EventBusSubscriber(modid = DataboxLib.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
  object ClientEvents {
    @SubscribeEvent
    fun onClientSetup(event: FMLCommonSetupEvent?) {
      DataboxLib.onClientSetup(event!!)
    }

    @SubscribeEvent
    fun registerLayers(event: RegisterLayerDefinitions?) {
    }
  }
}



