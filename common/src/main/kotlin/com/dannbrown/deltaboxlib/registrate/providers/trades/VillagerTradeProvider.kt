package com.dannbrown.deltaboxlib.registrate.providers.trades

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import com.mojang.serialization.JsonOps
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import java.io.IOException
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

class VillagerTradeProvider(
  private val registrate: AbstractDeltaboxRegistrate,
  private val packOutput: PackOutput
) : DataProvider {
  private val pathProvider =
    packOutput.createPathProvider(PackOutput.Target.DATA_PACK, VillagerTradeDeserializer.PATH)

  override fun getName(): String = "Villager Trades Datagen for: ${registrate.modId}"

  override fun run(cachedOutput: CachedOutput): CompletableFuture<*> {
    val futures: MutableList<CompletableFuture<*>> = ArrayList()
    for (trade in registrate.tradesRegistry.getTrades()) {
      val tradeName =
        trade.profession.name + "_" + trade.level.toName() + "_" + DeltaboxUtil.getItemId({ trade.tradeCosts.first().item.get() }) + "_for_" + DeltaboxUtil.getItemId(
          { trade.tradeSells.first().item.get() })
      val tradePath = pathProvider.json(DeltaboxUtil.resourceLocation(registrate.modId, tradeName))
      futures.add(saveTradeData(cachedOutput, tradePath, trade))
    }
    return CompletableFuture.allOf(*futures.toTypedArray())
  }

  private fun saveTradeData(cachedOutput: CachedOutput, path: Path, trade: VillagerTradeCodec): CompletableFuture<*> {
    return try {
      val jsonObject = VillagerTradeCodec.CODEC
        .encodeStart(JsonOps.INSTANCE, trade)
        .getOrThrow(false) {}
        .asJsonObject
      DataProvider.saveStable(cachedOutput, jsonObject, path)
    } catch (ioException: IOException) {
      DeltaboxUtil.LOGGER.error("Couldn't save villager trade at {}", path, ioException)
      throw ioException
    }
  }
}