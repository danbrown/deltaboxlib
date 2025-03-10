package com.dannbrown.deltaboxlib.forge.init

import com.dannbrown.deltaboxlib.forge.registrate.RegistrateInitForge
import com.dannbrown.deltaboxlib.init.DeltaboxLibMod
import dev.architectury.platform.forge.EventBuses
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import thedarkcolour.kotlinforforge.forge.DIST
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import com.dannbrown.deltaboxlib.registrate.providers.trades.WandererTradeRarity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.trading.MerchantOffer

@Mod(DeltaboxLibMod.MOD_ID)
object DeltaboxLibModForge {
  val registrateInit = RegistrateInitForge(DeltaboxLibMod.REGISTRATE)

  init {
    val modBus = MOD_BUS
    val forgeEventBus = MinecraftForge.EVENT_BUS
    register(modBus, forgeEventBus)
    // client
    if (DIST.isClient) {
      // register main mod client content
      registerClient(modBus, forgeEventBus)
    }
  }

  // RUN SETUP
  private fun commonSetup(event: FMLCommonSetupEvent) {
    event.enqueueWork {
      registrateInit.setup()
    }
  }

  private fun register(modBus: IEventBus, forgeEventBus: IEventBus) {
    // Submit our event bus to let architectury register our content on the right time
    EventBuses.registerModEventBus(DeltaboxLibMod.MOD_ID, MOD_BUS)
    DeltaboxLibMod.init()
    registrateInit.init()

    MOD_BUS.addListener(::commonSetup)

    forgeEventBus.addListener(::onRegisterVillagerTrades)
    forgeEventBus.addListener(::onRegisterWandererTrades)
  }

  private fun registerClient(modBus: IEventBus, forgeEventBus: IEventBus) {
    modBus.addListener(registrateInit::onRegisterBlockBiomeColors)
    modBus.addListener(registrateInit::onRegisterItemBiomeColors)
  }

  fun onRegisterVillagerTrades(event: net.minecraftforge.event.village.VillagerTradesEvent) {
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

  fun onRegisterWandererTrades(event: net.minecraftforge.event.village.WandererTradesEvent) {
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