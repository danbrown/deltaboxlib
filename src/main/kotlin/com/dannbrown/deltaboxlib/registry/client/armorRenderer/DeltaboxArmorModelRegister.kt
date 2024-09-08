package com.dannbrown.deltaboxlib.registry.client.armorRenderer

import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.world.entity.EquipmentSlot
import net.minecraftforge.client.event.EntityRenderersEvent
import net.minecraftforge.eventbus.api.IEventBus
import java.util.function.Supplier

abstract class DeltaboxArmorModelRegister(private val modId: String) {
  val REGISTRY: HashMap<String, ArmorModelProvider> = HashMap()

  protected fun register(armorName: String, slot: EquipmentSlot, layerDefinition: Supplier<LayerDefinition>, dogArmorModelSupplier: ArmorModelSupplier) {
    val provider = ArmorModelProvider(modId, armorName, slot, layerDefinition, dogArmorModelSupplier)
    REGISTRY[armorName + "_" + slot.name] = provider
  }

  fun register(bus: IEventBus){
    // init class
  }

  /**
   * Registers the LayerDefinitions. Must be client side only !
   * @param event Event called.
   */
  fun registerLayerDefinitions(event: EntityRenderersEvent.RegisterLayerDefinitions) {
    REGISTRY.forEach { (name: String?, provider: ArmorModelProvider) -> event.registerLayerDefinition(provider.layerLocation) { provider.createLayer() } }
  }
}