package com.dannbrown.databoxlib.content.item.GeckoArmor

import net.minecraft.client.model.HumanoidModel
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.ItemStack
import net.minecraftforge.client.extensions.common.IClientItemExtensions
import software.bernie.geckolib.animatable.GeoItem
import software.bernie.geckolib.core.animatable.GeoAnimatable
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar
import software.bernie.geckolib.core.animation.Animation
import software.bernie.geckolib.core.animation.AnimationController
import software.bernie.geckolib.core.animation.AnimationState
import software.bernie.geckolib.core.animation.RawAnimation
import software.bernie.geckolib.core.`object`.PlayState
import software.bernie.geckolib.renderer.GeoArmorRenderer
import software.bernie.geckolib.util.GeckoLibUtil
import java.util.function.Consumer

class CustomArmorItem(private val armorMaterial: ArmorMaterial, type: Type, properties: Properties) : ArmorItem(armorMaterial, type, properties), GeoItem {
  private val cache = GeckoLibUtil.createInstanceCache(this)

  // Create our armor model/renderer for forge and return it
  override fun initializeClient(consumer: Consumer<IClientItemExtensions>) {
    consumer.accept(object : IClientItemExtensions {
      private var renderer: GeoArmorRenderer<*>? = null
      override fun getHumanoidArmorModel(livingEntity: LivingEntity, itemStack: ItemStack, equipmentSlot: EquipmentSlot, original: HumanoidModel<*>): HumanoidModel<*> {
        if (renderer == null) renderer = CustomArmorRenderer<CustomArmorItem>(armorMaterial.name.split(":")[1])
        // This prepares our GeoArmorRenderer for the current render frame.
        // These parameters may be null however, so we don't do anything further with them
        renderer!!.prepForRender(livingEntity, itemStack, equipmentSlot, original)
        return renderer!!
      }
    })
  }

  private fun <T : GeoAnimatable> predicate(animationState: AnimationState<T>): PlayState {
    animationState.controller.setAnimation(RawAnimation.begin()
      .then("idle", Animation.LoopType.LOOP))
    return PlayState.CONTINUE
  }

  // Let's add our animation controller
  override fun registerControllers(controllers: ControllerRegistrar) {
    controllers.add(AnimationController(this, "controller", 0, ::predicate))
  }

  override fun getAnimatableInstanceCache(): AnimatableInstanceCache {
    return cache
  }
}