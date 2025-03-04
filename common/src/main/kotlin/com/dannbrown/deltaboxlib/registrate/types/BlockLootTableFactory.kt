package com.dannbrown.deltaboxlib.registrate.types

import com.dannbrown.deltaboxlib.registrate.datagen.RegistrateBlockLootTables
import net.minecraft.world.level.block.Block
import java.util.function.Supplier

typealias BlockLootTableFactory = NonNullBiConsumer<RegistrateBlockLootTables, Supplier<Block>> 