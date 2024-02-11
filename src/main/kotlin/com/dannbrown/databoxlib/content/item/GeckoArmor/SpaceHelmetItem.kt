package com.dannbrown.databoxlib.content.item.GeckoArmor

import com.simibubi.create.content.equipment.armor.BaseArmorItem
import net.minecraft.client.model.HumanoidModel
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
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
import java.util.function.Consumer

class SpaceHelmetItem(private val armorMaterial: ArmorMaterial, properties: Properties, textureLoc: ResourceLocation) : BaseArmorItem(armorMaterial, TYPE, properties, textureLoc), GeoItem {
  // GeckoLib
  private val cache = GeckoLibUtil.createInstanceCache(this)

  // Create our armor model/renderer for forge and return it
  override fun initializeClient(consumer: Consumer<IClientItemExtensions>) {
    consumer.accept(object : IClientItemExtensions {
      private var renderer: GeoArmorRenderer<*>? = null
      override fun getHumanoidArmorModel(livingEntity: LivingEntity, itemStack: ItemStack, equipmentSlot: EquipmentSlot, original: HumanoidModel<*>): HumanoidModel<*> {
        if (renderer == null) renderer = CustomArmorRenderer<SpaceHelmetItem>(armorMaterial.name.split(":")[1])
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

  // Armor Methods
  override fun canApplyAtEnchantingTable(stack: ItemStack, enchantment: Enchantment): Boolean {
    return if (enchantment === Enchantments.AQUA_AFFINITY) {
      false
    }
    else super.canApplyAtEnchantingTable(stack, enchantment)
  }

  override fun getEnchantmentLevel(stack: ItemStack, enchantment: Enchantment): Int {
    return if (enchantment === Enchantments.AQUA_AFFINITY) {
      1
    }
    else super.getEnchantmentLevel(stack, enchantment)
  }

  override fun getAllEnchantments(stack: ItemStack): Map<Enchantment, Int> {
    val map = super.getAllEnchantments(stack)
    map[Enchantments.AQUA_AFFINITY] = 1
    return map
  }

  companion object {
    val SLOT = EquipmentSlot.HEAD
    val TYPE = Type.HELMET
    fun isWornBy(entity: Entity): Boolean {
      return !getWornItem(entity).isEmpty
    }

    fun getWornItem(entity: Entity): ItemStack {
      if (entity !is LivingEntity) {
        return ItemStack.EMPTY
      }
      val stack: ItemStack = entity.getItemBySlot(SLOT)
      return if (stack.item !is SpaceHelmetItem) {
        ItemStack.EMPTY
      }
      else stack
    }
  }
}
