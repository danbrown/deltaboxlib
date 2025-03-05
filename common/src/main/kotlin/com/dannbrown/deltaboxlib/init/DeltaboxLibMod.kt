package com.dannbrown.deltaboxlib.init

import com.dannbrown.deltaboxlib.content.block.FlammableBlock
import com.dannbrown.deltaboxlib.registrate.registry.BlockEntry
import com.dannbrown.deltaboxlib.registrate.registry.ItemEntry
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.RotatedPillarBlock
import java.util.function.Supplier

object DeltaboxLibMod {
  const val MOD_ID = "deltaboxlib"
  var REGISTRATE = DeltaboxRegistrate(MOD_ID)

  val ADAMANTIUM_BLOCK = REGISTRATE
    .block("adamantium_block")
    .copyFrom { Blocks.STONE }
    .factory { c, p -> RotatedPillarBlock(p) }
    .loot { loot, block -> loot.dropSelf(block.get()) }
    .item { a, b -> BlockItem(b, a.food(FoodProperties.Builder().fast().build())) }
    .build()
    .register()

  val SECOND_BLOCK = REGISTRATE
    .block("second_block")
    .factory { c, p -> RotatedPillarBlock(p) }
    .strippable(ADAMANTIUM_BLOCK)
    .register()

  val ACAI_CRATE = REGISTRATE
    .block("acai_berries_crate")
    .lang("Acai Berries Crate AHA")
    .factory { c, p -> RotatedPillarBlock(p) }
    .strippable(BlockEntry.from(Blocks.ACACIA_LOG))
    .blockstate({ ctx, block -> ctx.bottomTopBlock(block.get(), "crate_bottom") })
    .register()

  val ADAMANTIUM_INGOT = REGISTRATE
    .item("adamantium_ingot")
    .register()

  val FLAMMABLE_BLOCK = REGISTRATE.block("flammable_block")
    .factory { c, p -> FlammableBlock(p, c.flammabilityBurnChance, c.flammabilitySpreadChance) }
    .flammable()
    .register()

  val LANGS = REGISTRATE
    .langs()
    .genericTooltip("flint", "It's a Delta!")
    .register()

  val TAGS = REGISTRATE.blockTags(BlockTags.DIRT)
    .add(ADAMANTIUM_BLOCK)
    .add(BlockEntry.from(Blocks.WHITE_WOOL))

  val ITEMTAGS = REGISTRATE.itemTags(ItemTags.NON_FLAMMABLE_WOOD)
    .add(ADAMANTIUM_BLOCK.getItemEntry())
    .add(FLAMMABLE_BLOCK.getItemEntry())
    .add(ItemEntry.from(Items.ACACIA_LOG))

  fun init() {
    REGISTRATE.buildRegistries()
  }
}