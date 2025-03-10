package com.dannbrown.deltaboxlib.registrate.providers

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.mojang.serialization.JsonOps
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener
import net.minecraft.util.GsonHelper
import net.minecraft.util.profiling.ProfilerFiller
import java.io.IOException

class VillagerTradeDeserializer(private val registrate: AbstractDeltaboxRegistrate) :
  SimpleJsonResourceReloadListener(GSON, PATH) {
  companion object {
    const val PATH = "villager_trades"
    private val GSON = GsonBuilder()
      .setPrettyPrinting()
      .disableHtmlEscaping()
      .create()
  }

  override fun apply(
    pObject: MutableMap<ResourceLocation, JsonElement>,
    pResourceManager: ResourceManager,
    pProfiler: ProfilerFiller
  ) {
    pProfiler.push("Villager Trades Deserialization")
    val villagerTrades: MutableList<VillagerTradeCodec> = ArrayList()
    for ((resourceLocation, jsonElement) in pObject.entries) {
      val jsonObject: JsonObject = GsonHelper.convertToJsonObject(jsonElement, PATH)
      try {
        val villagerTrade = VillagerTradeCodec.CODEC
          .parse(JsonOps.INSTANCE, jsonObject)
          .getOrThrow(false) {}
        villagerTrades.add(villagerTrade)
      } catch (ioException: IOException) {
        DeltaboxUtil.LOGGER.error("Couldn't load villager trade in {}", resourceLocation, ioException)
        throw ioException
      }
    }
    registrate.tradesRegistry.update(villagerTrades)
    pProfiler.pop()
  }
}