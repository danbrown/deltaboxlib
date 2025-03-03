package com.dannbrown.deltaboxlib.init

import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.BlockItem
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import java.util.function.Supplier

object DeltaboxLibMod {
    const val MOD_ID = "deltaboxlib"
    var REGISTRATE = DeltaboxRegistrate()

    val ADAMANTIUM_BLOCK: Supplier<Block> = REGISTRATE
      .block("adamantium_block")
      .copyFrom { Blocks.OAK_PLANKS }
      .factory { props -> Block(props) }
      .loot({ loot, block -> loot.dropSelf(block.get()) })
      .item({ a, b ->  BlockItem(b, a.food(FoodProperties.Builder().fast().build())) })
      .build()
      .register()
    val SECOND_BLOCK: Supplier<Block> = REGISTRATE
      .block("second_block")
      .noItem()
      .register()

    fun init() {
        REGISTRATE.buildRegistries()
    }
}