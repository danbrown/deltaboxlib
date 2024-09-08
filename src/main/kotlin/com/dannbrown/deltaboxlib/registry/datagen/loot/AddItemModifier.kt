package com.dannbrown.deltaboxlib.registry.datagen.loot

import com.google.common.base.Suppliers
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraftforge.common.loot.IGlobalLootModifier
import net.minecraftforge.common.loot.LootModifier
import net.minecraftforge.registries.ForgeRegistries
import java.util.function.Supplier

class AddItemModifier(conditionsIn: Array<LootItemCondition>, private val item: Item) : LootModifier(conditionsIn) {
  override fun doApply(generatedLoot: ObjectArrayList<ItemStack>, context: LootContext): ObjectArrayList<ItemStack> {
    for (condition in this.conditions) {
      if (!condition.test(context)) {
        return generatedLoot
      }
    }

    generatedLoot.add(ItemStack(this.item))

    return generatedLoot
  }

  override fun codec(): Codec<out IGlobalLootModifier> {
    return CODEC.get()
  }

  companion object {
    val CODEC: Supplier<Codec<AddItemModifier>> = Suppliers.memoize {
      RecordCodecBuilder.create { inst: RecordCodecBuilder.Instance<AddItemModifier> ->
        codecStart(inst)
          .and(ForgeRegistries.ITEMS.codec.fieldOf("item").forGetter { m: AddItemModifier -> m.item })
          .apply(inst
          ) { conditionsIn: Array<LootItemCondition>, item: Item -> AddItemModifier(conditionsIn, item) }
      }
    }
  }
}