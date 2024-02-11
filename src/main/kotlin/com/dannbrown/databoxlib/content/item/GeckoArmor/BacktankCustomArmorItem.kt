package com.dannbrown.databoxlib.content.item.GeckoArmor

import com.simibubi.create.content.equipment.armor.BacktankItem
import com.simibubi.create.foundation.item.LayeredArmorItem
import net.minecraft.client.model.HumanoidModel
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.ItemStack
import net.minecraftforge.client.extensions.common.IClientItemExtensions
import software.bernie.geckolib.animatable.GeoItem
import software.bernie.geckolib.core.animatable.GeoAnimatable
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.core.animation.AnimatableManager
import software.bernie.geckolib.core.animation.Animation
import software.bernie.geckolib.core.animation.AnimationController
import software.bernie.geckolib.core.animation.AnimationState
import software.bernie.geckolib.core.animation.RawAnimation
import software.bernie.geckolib.core.`object`.PlayState
import software.bernie.geckolib.renderer.GeoArmorRenderer
import software.bernie.geckolib.util.GeckoLibUtil
import java.util.*
import java.util.function.Consumer
import java.util.function.Supplier

open class BacktankCustomArmorItem(private val armorMaterial: ArmorMaterial, properties: Properties, textureLoc: ResourceLocation, placeable: Supplier<BacktankBlockItem>) : BacktankItem(armorMaterial, properties, textureLoc, placeable), GeoItem {
  private val cache = GeckoLibUtil.createInstanceCache(this)

  // Create our armor model/renderer for forge and return it
  override fun initializeClient(consumer: Consumer<IClientItemExtensions>) {
    consumer.accept(object : IClientItemExtensions {
      private var renderer: GeoArmorRenderer<*>? = null
      override fun getHumanoidArmorModel(livingEntity: LivingEntity, itemStack: ItemStack, equipmentSlot: EquipmentSlot, original: HumanoidModel<*>): HumanoidModel<*> {
        if (renderer == null) renderer = CustomArmorRenderer<BacktankCustomArmorItem>(armorMaterial.name.split(":")[1])
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
  override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
    controllers.add(AnimationController(this, "controller", 0, ::predicate))
  }

  override fun getAnimatableInstanceCache(): AnimatableInstanceCache {
    return cache
  }

  class Layered(material: ArmorMaterial, properties: Properties, textureLoc: ResourceLocation, placeable: Supplier<BacktankBlockItem>) : BacktankCustomArmorItem(material, properties, textureLoc, placeable), LayeredArmorItem {
    override fun getArmorTextureLocation(entity: LivingEntity, slot: EquipmentSlot, stack: ItemStack, layer: Int): String {
      return String.format(Locale.ROOT, "%s:textures/models/armor/%s_layer_%d.png", textureLoc.namespace, textureLoc.path, layer)
    }
  }
}