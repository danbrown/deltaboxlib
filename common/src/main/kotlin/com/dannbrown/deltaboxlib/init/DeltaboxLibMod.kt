package com.dannbrown.deltaboxlib.init

import com.dannbrown.deltaboxlib.registrate.registry.BlockEntry
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.RotatedPillarBlock
import java.util.function.Supplier

object DeltaboxLibMod {
  const val MOD_ID = "deltaboxlib"
  var REGISTRATE = DeltaboxRegistrate(MOD_ID)

  val ADAMANTIUM_BLOCK = REGISTRATE
    .block("adamantium_block")
    .copyFrom { Blocks.OAK_PLANKS }
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

  val LANGS = REGISTRATE
    .langs()
    .genericTooltip("flint", "It's a Delta!")
    .register()

  fun init() {
    REGISTRATE.buildRegistries()
  }
}