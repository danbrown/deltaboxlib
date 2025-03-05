package com.dannbrown.deltaboxlib.registrate.types

import com.dannbrown.deltaboxlib.registrate.datagen.RegistrateRecipes
import net.minecraft.world.level.block.Block
import java.util.function.Supplier

typealias BlockRecipeFactory = NonNullBiConsumer<RegistrateRecipes, Supplier<Block>>