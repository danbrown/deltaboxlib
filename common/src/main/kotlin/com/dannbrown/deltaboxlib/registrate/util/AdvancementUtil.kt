package com.dannbrown.deltaboxlib.registrate.util

import net.minecraft.advancements.Advancement
import net.minecraft.advancements.DisplayInfo
import net.minecraft.advancements.FrameType
import net.minecraft.advancements.RequirementsStrategy
import net.minecraft.advancements.critereon.BlockPredicate
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.advancements.critereon.ItemUsedOnLocationTrigger
import net.minecraft.advancements.critereon.LocationPredicate
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import java.util.function.Consumer

class AdvancementUtil(val modId: String) {
  fun advancementTitle(key: String): Component {
    return Component.translatable("advancements.${modId}.${key}.title")
  }

  fun advancementDescription(key: String): Component {
    return Component.translatable("advancements.${modId}.${key}.description")
  }

  fun basicAdvancement(iconItem: Item, key: String, background: ResourceLocation? = null): Advancement.Builder {
    return Advancement.Builder.advancement()
      .display(
        DisplayInfo(
          ItemStack(iconItem),
          advancementTitle(key),
          advancementDescription(key),
          background,
          FrameType.TASK,
          true,
          true,
          false
        )
      )
  }

  fun hasItemsCriterion(
    builder: Advancement.Builder,
    savePath: String,
    requirementsStrategy: RequirementsStrategy,
    vararg items: ItemLike
  ): Advancement {
    for (item in items) {
      builder.addCriterion(
        "has_${item.asItem().descriptionId}",
        InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(item).build())
      )
    }
    return builder
      .requirements(requirementsStrategy)
      .build(DeltaboxUtil.resourceLocation(modId, savePath))
  }

  fun usedOnBlockCriterion(
    builder: Advancement.Builder,
    savePath: String,
    requirementsStrategy: RequirementsStrategy,
    itemOnBlock: Map<ItemLike, Block>
  ): Advancement {
    for ((item, block) in itemOnBlock) {
      builder.addCriterion(
        "used_${DeltaboxUtil.getItemId(item)}_on_${DeltaboxUtil.getBlockId(block)}",
        ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
          LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(block).build()),
          ItemPredicate.Builder.item().of(item)
        )
      )
    }
    return builder
      .requirements(requirementsStrategy)
      .build(DeltaboxUtil.resourceLocation(modId, savePath))
  }
}