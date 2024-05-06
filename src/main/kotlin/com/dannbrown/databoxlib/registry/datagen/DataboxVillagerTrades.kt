package com.dannbrown.databoxlib.registry.datagen

import net.minecraft.world.entity.npc.VillagerProfession
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.trading.MerchantOffer
import net.minecraftforge.event.village.VillagerTradesEvent


/**
 * A class to handle the registration of villager trades.
 *
 * @param event The VillagerTradesEvent to register trades to.
 * @param trades A mutable list of VillagerTradeData to register.
 */
abstract class DataboxVillagerTrades(private val event: VillagerTradesEvent, private val trades: MutableList<VillagerTradeData>) {
  fun register() {
    trades.forEach { trade ->
      if(event.type == trade.profession){
        val trades = event.trades
        trades[trade.level.toInt()].add { _, _ -> MerchantOffer(trade.tradeCost, trade.tradeResult, trade.maxUses, trade.xpAmount, trade.priceMultiplier) }
      }
    }
  }

  companion object {
    fun addTrade(profession: VillagerProfession, level: VillagerLevel, tradeCost: ItemStack, tradeResult: ItemStack, maxUses: Int, xpAmount: Int, priceMultiplier: Float): VillagerTradeData {
      return VillagerTradeData(profession, level, tradeCost, tradeResult, maxUses, xpAmount, priceMultiplier)
    }
  }

  enum class VillagerLevel {
    NOVICE,
    APPRENTICE,
    JOURNEYMAN,
    EXPERT,
    MASTER;

    fun toInt(): Int {
      return when(this) {
        NOVICE -> 1
        APPRENTICE -> 2
        JOURNEYMAN -> 3
        EXPERT -> 4
        MASTER -> 5
      }
    }
  }
  class VillagerTradeData(val profession: VillagerProfession, val level: VillagerLevel, val tradeCost: ItemStack, val tradeResult: ItemStack, val maxUses: Int, val xpAmount: Int, val priceMultiplier: Float)
}