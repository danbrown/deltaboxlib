package com.dannbrown.databoxlib;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class Project {
  public Project() {
    var eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    var forgeEventBus = MinecraftForge.EVENT_BUS;
    DataboxLib.Companion.register(eventBus, forgeEventBus);
  }

  public static void onServerStarting(ServerStartingEvent event) {
    DataboxLib.Companion.onServerStarting(event);
  }

  public static class ClientEvents {
    public static void onClientSetup(FMLCommonSetupEvent event) {
      DataboxLib.Companion.onClientSetup(event);
    }

    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
    }
  }
}
