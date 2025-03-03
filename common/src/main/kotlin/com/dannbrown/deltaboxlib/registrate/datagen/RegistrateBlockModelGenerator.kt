package com.dannbrown.deltaboxlib.registrate.datagen

import com.google.gson.JsonElement
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.blockstates.BlockStateGenerator
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.Supplier

class RegistrateBlockModelGenerator(consumer: Consumer<BlockStateGenerator>, biConsumer: BiConsumer<ResourceLocation, Supplier<JsonElement>>, consumer2: Consumer<Item>) : BlockModelGenerators(consumer, biConsumer, consumer2) {

}