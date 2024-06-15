package com.dannbrown.deltaboxlib.registry.datagen

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.trading.MerchantOffer
import net.minecraftforge.event.village.WandererTradesEvent

/**
 * A class to handle the registration of wanderer trades.
 *
 * @param event The WandererTradesEvent to register trades to.
 * @param trades A mutable list of WandererTradeData to register.
 */
abstract class DeltaboxWandererTrades(private val event: WandererTradesEvent, private val trades: MutableList<WandererTradeData>) {
  fun register() {
    val genericTrades = event.genericTrades
    val rareTrades = event.rareTrades

    trades.forEach { trade ->
      if(trade.rarity == TradeRarity.GENERIC) {
        genericTrades.add { _, _ -> MerchantOffer(trade.tradeCost, trade.tradeResult, trade.maxUses, trade.xpAmount, trade.priceMultiplier) }
      } else {
        rareTrades.add { _, _ -> MerchantOffer(trade.tradeCost, trade.tradeResult, trade.maxUses, trade.xpAmount, trade.priceMultiplier) }
      }
    }
  }

  companion object {
    fun addTrade( rarity: TradeRarity, tradeCost: ItemStack, tradeResult: ItemStack, maxUses: Int, xpAmount: Int, priceMultiplier: Float): WandererTradeData {
      return WandererTradeData(rarity, tradeCost, tradeResult, maxUses, xpAmount, priceMultiplier)
    }
  }
  enum class TradeRarity {
    GENERIC,
    RARE,
  }

  class WandererTradeData(val rarity: TradeRarity, val tradeCost: ItemStack, val tradeResult: ItemStack, val maxUses: Int, val xpAmount: Int, val priceMultiplier: Float)
}