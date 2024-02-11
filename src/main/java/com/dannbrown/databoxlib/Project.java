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
  }


  public static class ClientEvents {

    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
    }
  }
}
