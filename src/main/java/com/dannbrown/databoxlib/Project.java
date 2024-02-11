package com.dannbrown.databoxlib;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class Project {
  public Project() {
    var eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    var forgeEventBus = MinecraftForge.EVENT_BUS;
    ProjectContent.Companion.register(eventBus, forgeEventBus);
  }

  public static void onServerStarting(ServerStartingEvent event) {
    ProjectContent.Companion.onServerStarting(event);
  }

  public static class ClientEvents {
    public static void onClientSetup(FMLCommonSetupEvent event) {
      ProjectContent.Companion.onClientSetup(event);
    }

    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
    }
  }

//  @Mod.EventBusSubscriber(modid = DataboxContent.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
//  object DataboxClientBusEvents {
//    @SubscribeEvent
//    fun registerLayer(event: EntityRenderersEvent.RegisterLayerDefinitions) {
//      event.registerLayerDefinition(DataboxModelLayers.TIER_5_ROCKET_LAYER) { RocketTier5Model.createBodyLayer() }
//      event.registerLayerDefinition(DataboxModelLayers.TIER_6_ROCKET_LAYER) { RocketTier5Model.createBodyLayer() }
//      event.registerLayerDefinition(DataboxModelLayers.TIER_7_ROCKET_LAYER) { RocketTier5Model.createBodyLayer() }
//    }
//  }
}
