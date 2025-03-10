package com.dannbrown.deltaboxlib.registrate.registry

import com.dannbrown.deltaboxlib.registrate.providers.trades.VillagerTradeCodec
import com.dannbrown.deltaboxlib.registrate.providers.trades.WandererTradeCodec


class TradeRegistry(modId: String) {
  private val TRADES: MutableList<VillagerTradeCodec> = ArrayList()
  private val WANDERER_TRADES: MutableList<WandererTradeCodec> = ArrayList()

  fun addTrade(trade: VillagerTradeCodec) {
    TRADES.add(trade)
  }

  fun addWanderer(trade: WandererTradeCodec) {
    WANDERER_TRADES.add(trade)
  }

  fun updateTrades(trades: MutableList<VillagerTradeCodec>) {
    TRADES.clear()
    TRADES.addAll(trades)
  }

  fun updateWanderer(trades: MutableList<WandererTradeCodec>) {
    WANDERER_TRADES.clear()
    WANDERER_TRADES.addAll(trades)
  }

  fun getTrades(): MutableList<VillagerTradeCodec> {
    return TRADES
  }

  fun getWandererTrades(): MutableList<WandererTradeCodec> {
    return WANDERER_TRADES
  }
}