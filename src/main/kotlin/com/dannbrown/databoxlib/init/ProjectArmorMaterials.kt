package com.dannbrown.databoxlib.init

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.lib.LibData
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.crafting.Ingredient
import java.util.function.Supplier

enum class ProjectArmorMaterials(
  private val _name: String,
  private val durabilityMultiplier: Int,
  private val protectionAmounts: IntArray,
  private val enchantmentValue: Int,
  private val equipSound: SoundEvent,
  private val toughness: Float,
  private val knockbackResistance: Float,
  private val repairIngredient: Supplier<Ingredient>
) : ArmorMaterial {
  STEEL(LibData.NAMES.STEEL, 25, intArrayOf(3, 8, 6, 3), 10, SoundEvents.ARMOR_EQUIP_GOLD, 1f, 0f, Supplier<Ingredient> { Ingredient.of(ProjectItems.STEEL_INGOT.get()) });

  //  TUNGSTEN(LibData.NAMES.TUNGSTEN, 26, intArrayOf(5, 7, 5, 4), 25, SoundEvents.ARMOR_EQUIP_GOLD, 1f, 0f, Supplier<Ingredient> { Ingredient.of(DataboxItems.STEEL_INGOT.get()) }),
//  TITANIUM(LibData.NAMES.TITANIUM, 26, intArrayOf(5, 7, 5, 4), 25, SoundEvents.ARMOR_EQUIP_GOLD, 1f, 0f, Supplier<Ingredient> { Ingredient.of(DataboxItems.STEEL_INGOT.get()) }),
//  ADAMANTIUM(LibData.NAMES.ADAMANTIUM, 26, intArrayOf(5, 7, 5, 4), 25, SoundEvents.ARMOR_EQUIP_GOLD, 1f, 0f, Supplier<Ingredient> { Ingredient.of(DataboxItems.STEEL_INGOT.get()) });
  override fun getDurabilityForType(pType: ArmorItem.Type): Int {
    return BASE_DURABILITY[pType.ordinal] * durabilityMultiplier
  }

  override fun getDefenseForType(pType: ArmorItem.Type): Int {
    return protectionAmounts[pType.ordinal]
  }

  override fun getEnchantmentValue(): Int {
    return enchantmentValue
  }

  override fun getEquipSound(): SoundEvent {
    return equipSound
  }

  override fun getRepairIngredient(): Ingredient {
    return repairIngredient.get()
  }

  override fun getName(): String {
    return ProjectContent.MOD_ID + ":" + _name
  }

  override fun getToughness(): Float {
    return toughness
  }

  override fun getKnockbackResistance(): Float {
    return knockbackResistance
  }

  companion object {
    private val BASE_DURABILITY = intArrayOf(11, 16, 16, 13)
  }
}