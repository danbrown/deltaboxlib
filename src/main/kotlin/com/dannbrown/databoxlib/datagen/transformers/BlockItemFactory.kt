package com.dannbrown.databoxlib.datagen.transformers

import com.dannbrown.databoxlib.content.item.FuelItemFromBlock
import com.simibubi.create.content.decoration.MetalScaffoldingBlockItem
import com.tterrag.registrate.util.nullness.NonNullBiFunction
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

object BlockItemFactory {
  fun <B : Block, P : BlockItem> metalScaffoldingItem(): NonNullBiFunction<B, Item.Properties, P> {
    return NonNullBiFunction { b, p: Item.Properties -> MetalScaffoldingBlockItem(b, p) as P }
  }

  fun <B : Block, P : BlockItem> fuelBlockItem(burnTime: Int): NonNullBiFunction<B, Item.Properties, P> {
    return NonNullBiFunction { b, p -> FuelItemFromBlock(b, p, burnTime) as P }
  }
}