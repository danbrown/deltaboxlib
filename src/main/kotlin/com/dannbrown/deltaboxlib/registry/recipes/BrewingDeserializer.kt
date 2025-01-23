package com.dannbrown.deltaboxlib.registry.recipes

import com.dannbrown.deltaboxlib.DeltaboxLib
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.mojang.serialization.JsonOps
import com.tterrag.registrate.util.DataIngredient
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener
import net.minecraft.util.GsonHelper
import net.minecraft.util.profiling.ProfilerFiller
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraftforge.common.brewing.BrewingRecipe
import net.minecraftforge.common.brewing.BrewingRecipeRegistry
import java.util.ArrayList

class BrewingDeserializer: SimpleJsonResourceReloadListener(GSON, BrewingGenerator.PATH) {
  companion object {
    private val GSON = GsonBuilder()
      .setPrettyPrinting()
      .disableHtmlEscaping()
      .create()
  }
  // This will load the brewing data from the json files
  override fun apply(pObject: MutableMap<ResourceLocation, JsonElement>, pResourceManager: ResourceManager, pProfiler: ProfilerFiller) {
    pProfiler.push("DeltaboxLib's brewing data deserialization")
    val brewingCodecs: MutableList<BrewingCodec> = ArrayList()
    for ((resourceLocation, jsonElement) in pObject.entries) {
      val jsonObject: JsonObject = GsonHelper.convertToJsonObject(jsonElement, "brewing")
      val brewingCodec = BrewingCodec.CODEC
        .parse(JsonOps.INSTANCE, jsonObject)
        .getOrThrow(false, DeltaboxLib.LOGGER::error)
      brewingCodecs.add(brewingCodec)
      pProfiler.pop()
    }
    BrewingCodec.updateData(brewingCodecs)
    brewingCodecs.forEach { recipe ->
      BrewingRecipeRegistry.addRecipe(DataIngredient.of(recipe.inputItem), DataIngredient.of(ItemStack(recipe.ingredientItem.item)), recipe.outputItem)
    }
  }
}