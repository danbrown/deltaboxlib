package com.dannbrown.deltaboxlib.registrate.types

import com.dannbrown.deltaboxlib.registrate.datagen.model.RegistrateBlockModelGenerator
import net.minecraft.world.level.block.Block
import java.util.function.Supplier

typealias BlockstateFactory = NonNullBiConsumer<RegistrateBlockModelGenerator, Supplier<Block>>