package com.dannbrown.deltaboxlib.registry.client.armorRenderer

import net.minecraft.client.Minecraft
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import java.util.*
import java.util.function.Supplier

// Client side
class ArmorModelProvider(private val modId: String, armorName: String, slot: EquipmentSlot, private val layerDefinitionSupplier: Supplier<LayerDefinition>, private val armorModelSupplier: ArmorModelSupplier) {
  val layerLocation: ModelLayerLocation = ModelLayerLocation(ResourceLocation("minecraft:player"), armorName + "_" + slot.name.lowercase(Locale.getDefault()))
  private val resourceLocations = ResourceLocation(modId, "textures/armor/$armorName.png")

  fun getTexture(): ResourceLocation {
    return this.resourceLocations
  }

  fun createLayer(): LayerDefinition {
    return layerDefinitionSupplier.get()
  }

  fun createModel(): AbstractArmorModel<LivingEntity> {
    return armorModelSupplier.create(Minecraft.getInstance().entityModels.bakeLayer(this.layerLocation))
  }
}