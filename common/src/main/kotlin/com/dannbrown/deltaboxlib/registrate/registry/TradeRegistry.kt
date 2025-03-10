package com.dannbrown.deltaboxlib.registrate.registry

import com.dannbrown.deltaboxlib.registrate.providers.VillagerTradeCodec

class TradeRegistry(modId: String) {
  private val TRADES: MutableList<VillagerTradeCodec> = ArrayList()
//  val WANDERER_TRADES: MutableList<WandererTradeCodec> = ArrayList()

  fun add(trade: VillagerTradeCodec) {
    TRADES.add(trade)
  }

  fun update(trade: MutableList<VillagerTradeCodec>) {
    TRADES.clear()
    TRADES.addAll(trade)
  }


  fun getTrades(): MutableList<VillagerTradeCodec> {
    return TRADES
  }
}