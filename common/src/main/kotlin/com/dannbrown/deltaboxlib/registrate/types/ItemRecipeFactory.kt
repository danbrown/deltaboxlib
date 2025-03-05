package com.dannbrown.deltaboxlib.registrate.types

import com.dannbrown.deltaboxlib.registrate.datagen.RegistrateRecipes
import net.minecraft.world.item.Item
import java.util.function.Supplier

typealias ItemRecipeFactory = NonNullBiConsumer<RegistrateRecipes, Supplier<Item>>