package com.dannbrown.databoxlib.content.item.GeckoArmor

import com.dannbrown.databoxlib.ProjectContent
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.core.animatable.GeoAnimatable
import software.bernie.geckolib.model.GeoModel

class CustomArmorModel<T : GeoAnimatable>(private val armorName: String) : GeoModel<T>() {
  override fun getModelResource(animatable: T): ResourceLocation {
    return ResourceLocation(ProjectContent.MOD_ID, "geo/${armorName}_armor.geo.json")
  }

  override fun getTextureResource(animatable: T): ResourceLocation {
    return ResourceLocation(ProjectContent.MOD_ID, "textures/armor/${armorName}_armor.png")
  }

  override fun getAnimationResource(animatable: T): ResourceLocation {
    return ResourceLocation(ProjectContent.MOD_ID, "animations/${armorName}_armor.animation.json")
  }
}

