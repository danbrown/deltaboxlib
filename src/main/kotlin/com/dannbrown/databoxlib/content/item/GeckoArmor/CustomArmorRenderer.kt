package com.dannbrown.databoxlib.content.item.GeckoArmor

import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import software.bernie.geckolib.animatable.GeoItem
import software.bernie.geckolib.renderer.GeoArmorRenderer

class CustomArmorRenderer<T>(armorName: String) : GeoArmorRenderer<T>(CustomArmorModel(armorName)) where T : Item, T : GeoItem {
  override fun getRenderType(animatable: T, texture: ResourceLocation, bufferSource: MultiBufferSource?, partialTick: Float): RenderType {
    return RenderType.entityTranslucent(texture, false)
  }
}