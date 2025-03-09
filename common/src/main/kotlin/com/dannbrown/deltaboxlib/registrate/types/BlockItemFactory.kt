package com.dannbrown.deltaboxlib.registrate.types

import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import java.util.function.BiFunction

typealias BlockItemFactory = BiFunction<Item.Properties, Block, out Item>