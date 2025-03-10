package com.dannbrown.deltaboxlib.fabric.init

import com.dannbrown.deltaboxlib.init.DeltaboxLibMod
import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.providers.trades.*
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import net.fabricmc.fabric.api.`object`.builder.v1.trade.TradeOfferHelper
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.CloseableResourceManager
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.server.packs.resources.PreparableReloadListener.PreparationBarrier
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.util.profiling.ProfilerFiller
import net.minecraft.world.entity.npc.VillagerProfession
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.trading.MerchantOffer
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

object DeltaboxLibLoadTradesFabric {
  // Bellow we deal with villager trades registering, fabric is quite complex to do it...
  fun onDatapackReload(registrate: AbstractDeltaboxRegistrate) {
    val registry: (ResourceLocation, PreparableReloadListener) -> Unit = { id, listener ->
      ResourceManagerHelper.get(PackType.SERVER_DATA)
        .registerReloadListener(object : IdentifiableResourceReloadListener {
          override fun getFabricId(): ResourceLocation = id

          override fun reload(
            synchronizer: PreparationBarrier,
            manager: ResourceManager,
            prepareProfiler: ProfilerFiller,
            applyProfiler: ProfilerFiller,
            prepareExecutor: Executor,
            applyExecutor: Executor
          ): CompletableFuture<Void> {
            return listener.reload(
              synchronizer,
              manager,
              prepareProfiler,
              applyProfiler,
              prepareExecutor,
              applyExecutor
            )
          }
        })
    }

    // registries
    registry(
      DeltaboxUtil.resourceLocation(registrate.modId, VillagerTradeDeserializer.PATH),
      VillagerTradeDeserializer(registrate)
    )
    registry(
      DeltaboxUtil.resourceLocation(registrate.modId, WandererTradeDeserializer.PATH),
      WandererTradeDeserializer(registrate)
    )

    // call other events
    net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STARTED.register(::onServerStarted);
    net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(::onEndDatapackReload);
  }

  private fun onServerStarted(server: MinecraftServer) {
    handleLoadVillagerTrades(DeltaboxLibMod.REGISTRATE)
  }

  private fun onEndDatapackReload(
    server: MinecraftServer,
    resourceManager: CloseableResourceManager,
    success: Boolean
  ) {
    handleLoadVillagerTrades(DeltaboxLibMod.REGISTRATE)
  }

  private fun handleLoadVillagerTrades(registrate: AbstractDeltaboxRegistrate) {
    val tradesByProfession: MutableMap<Pair<VillagerProfession, VillagerLevel>, MutableList<VillagerTradeCodec>> =
      mutableMapOf()
    val wandererTradesByRarity: MutableMap<WandererTradeRarity, MutableList<WandererTradeCodec>> = mutableMapOf()

    registrate.tradesRegistry.getTrades().map {
      val pair = Pair(it.profession, it.level)
      val currentList = (tradesByProfession[pair] ?: mutableListOf())
      currentList.add(it)
      tradesByProfession[pair] = currentList
    }

    registrate.tradesRegistry.getWandererTrades().forEach {
      val currentList = (wandererTradesByRarity[it.rarity] ?: mutableListOf())
      currentList.add(it)
      wandererTradesByRarity[it.rarity] = currentList
    }

    tradesByProfession.forEach { t, u ->
      TradeOfferHelper.registerVillagerOffers(t.first, t.second.toInt(), { factories ->
        u.forEach {
          factories.add({ e, r ->
            MerchantOffer(
              ItemStack(it.tradeCosts.first().item.get(), it.tradeCosts.first().amount),
              ItemStack(it.tradeSells.first().item.get(), it.tradeSells.first().amount),
              it.maxUses,
              it.xpAmount,
              it.priceMultiplier
            )
          })
        }
      })
    }

    wandererTradesByRarity.forEach { t, u ->
      TradeOfferHelper.registerWanderingTraderOffers(t.toInt(), { factories ->
        u.forEach {
          factories.add({ e, r ->
            MerchantOffer(
              ItemStack(it.tradeCosts.first().item.get(), it.tradeCosts.first().amount),
              ItemStack(it.tradeSells.first().item.get(), it.tradeSells.first().amount),
              it.maxUses,
              it.xpAmount,
              it.priceMultiplier
            )
          })
        }
      })
    }
  }
}