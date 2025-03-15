package com.dannbrown.deltaboxlib.forge.init.loaders

import com.dannbrown.deltaboxlib.init.DeltaboxLibMod
import com.dannbrown.deltaboxlib.registrate.providers.trades.WandererTradeRarity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.trading.MerchantOffer
import net.minecraftforge.eventbus.api.IEventBus

object DeltaboxLibLoadTradesForge {
  fun onRegisterTrades(forgeEventBus: IEventBus) {
    forgeEventBus.addListener(DeltaboxLibLoadTradesForge::onRegisterVillagerTrades)
    forgeEventBus.addListener(DeltaboxLibLoadTradesForge::onRegisterWandererTrades)
  }

  private fun onRegisterVillagerTrades(event: net.minecraftforge.event.village.VillagerTradesEvent) {
    DeltaboxLibMod.REGISTRATE.tradesRegistry.getTrades().forEach { trade ->
      if (event.type == trade.profession) {
        event.trades[trade.level.toInt()].add { _, _ ->
          MerchantOffer(
            ItemStack(
              trade.tradeCosts.first().item.get(),
              trade.tradeCosts.first().amount
            ),
            ItemStack(trade.tradeSells.first().item.get(), trade.tradeSells.first().amount),
            trade.maxUses,
            trade.xpAmount,
            trade.priceMultiplier
          )
        }
      }
    }
  }

  private fun onRegisterWandererTrades(event: net.minecraftforge.event.village.WandererTradesEvent) {
    val genericTrades = event.genericTrades
    val rareTrades = event.rareTrades
    DeltaboxLibMod.REGISTRATE.tradesRegistry.getWandererTrades().forEach { trade ->
      if (trade.rarity == WandererTradeRarity.GENERIC) {
        genericTrades.add { _, _ ->
          MerchantOffer(
            ItemStack(
              trade.tradeCosts.first().item.get(),
              trade.tradeCosts.first().amount
            ),
            ItemStack(trade.tradeSells.first().item.get(), trade.tradeSells.first().amount),
            trade.maxUses,
            trade.xpAmount,
            trade.priceMultiplier
          )
        }
      } else {
        rareTrades.add { _, _ ->
          MerchantOffer(
            ItemStack(
              trade.tradeCosts.first().item.get(),
              trade.tradeCosts.first().amount
            ),
            ItemStack(trade.tradeSells.first().item.get(), trade.tradeSells.first().amount),
            trade.maxUses,
            trade.xpAmount,
            trade.priceMultiplier
          )
        }
      }
    }
  }
}