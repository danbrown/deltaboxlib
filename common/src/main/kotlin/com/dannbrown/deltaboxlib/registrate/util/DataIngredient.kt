package com.dannbrown.deltaboxlib.registrate.util


import net.minecraft.advancements.critereon.EnchantmentPredicate
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.advancements.critereon.MinMaxBounds
import net.minecraft.advancements.critereon.NbtPredicate
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import java.util.function.Supplier
import javax.management.BadAttributeValueExpException

class DataIngredient() {
  enum class Type {
    ITEM,
    TAG,
    ITEMSTACK
  }

  private var items: MutableList<ItemLike> = mutableListOf()
  private var tag: TagKey<Item>? = null
  private var itemstacks: MutableList<ItemStack> = mutableListOf()
  lateinit var type: Type

  constructor(_tag: TagKey<Item>) : this() {
    this.tag = _tag
    type = Type.TAG
  }

  constructor(vararg _items: ItemLike) : this() {
    this.items.addAll(_items)
    type = Type.ITEM
  }

  constructor(vararg _items: ItemStack) : this() {
    this.itemstacks.addAll(_items)
    type = Type.ITEMSTACK
  }

  fun ingredient(): Ingredient {
    return if (items.isNotEmpty()) Ingredient.of(*items.toTypedArray())
    else if (tag != null) Ingredient.of(tag!!)
    else if (itemstacks.isNotEmpty()) Ingredient.of(*itemstacks.toTypedArray())
    else Ingredient.EMPTY
  }

  fun items(): MutableList<ItemLike> {
    return items
  }

  fun tag(): TagKey<Item>? {
    return tag
  }

  fun itemStacks(): MutableList<ItemStack> {
    return itemstacks
  }

  fun getTrigger(): InventoryChangeTrigger.TriggerInstance {
    return if (items.isNotEmpty()) getTriggerFromItems(*items.toTypedArray())
    else if (tag != null) getTriggerFromTag(tag!!)
    else if (itemstacks.isNotEmpty()) getTriggerFromItemStacks(*itemstacks.toTypedArray())
    else throw BadAttributeValueExpException("DataIngredient is empty, can't create a trigger")
  }

  companion object {
    fun getTriggerFromItems(vararg items: ItemLike): InventoryChangeTrigger.TriggerInstance {
      return InventoryChangeTrigger.TriggerInstance.hasItems(*items.map { it.asItem() }.toTypedArray())
    }

    fun getTriggerFromItemStacks(vararg itemstacks: ItemStack): InventoryChangeTrigger.TriggerInstance {
      return InventoryChangeTrigger.TriggerInstance.hasItems(*itemstacks.map { it.item }.toTypedArray())
    }

    fun getTriggerFromTag(tag: TagKey<Item>): InventoryChangeTrigger.TriggerInstance {
      return InventoryChangeTrigger.TriggerInstance.hasItems(
        ItemPredicate(
          tag,
          null,
          MinMaxBounds.Ints.ANY,
          MinMaxBounds.Ints.ANY,
          EnchantmentPredicate.NONE,
          EnchantmentPredicate.NONE,
          null,
          NbtPredicate.ANY
        )
      )
    }

    fun getTagCriterionName(tag: TagKey<Item>): String {
      val tagname = DeltaboxUtil.asId(tag.location.path.replace("/", "_"))
      return "has_${tag.location.namespace}_${tagname}"
    }

    fun <T : RecipeBuilder> addIngredientsRecipeCriterions(
      builder: T,
      ingredients: List<Supplier<DataIngredient>>,
      recipeName: String
    ) {
      // get all items from ingredients and build a item has_ingredients criterion
      val itemIngredients: MutableList<ItemLike> = mutableListOf()
      for (i in ingredients) {
        if (i.get().type == DataIngredient.Type.ITEM) itemIngredients.addAll(i.get().items())
        if (i.get().type == DataIngredient.Type.ITEMSTACK) itemIngredients.addAll(i.get().itemStacks().map { it.item })
      }
      builder.unlockedBy("has_ingredients", DataIngredient.getTriggerFromItems(*itemIngredients.toTypedArray()))

      // get all tags form ingredients and add has tag
      for (i in ingredients) {
        if (i.get().type != DataIngredient.Type.TAG) continue
        val tag = i.get().tag() ?: continue
        try {
          builder.unlockedBy(DataIngredient.getTagCriterionName(tag), DataIngredient.getTriggerFromTag(tag))
        } catch (e: Throwable) {
          kotlin.io.println("Possible duplicate criterion '${DataIngredient.getTagCriterionName(tag)}' for recipe '${recipeName}', skipping...")
        }
      }
    }
  }
}