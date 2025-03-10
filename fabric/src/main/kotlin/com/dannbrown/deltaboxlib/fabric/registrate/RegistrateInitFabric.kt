package com.dannbrown.deltaboxlib.fabric.registrate

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.providers.trades.*
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.trade.TradeOfferHelper
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.client.renderer.BiomeColors
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
import net.minecraft.world.level.FoliageColor
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import javax.management.BadAttributeValueExpException

class RegistrateInitFabric(val registrate: AbstractDeltaboxRegistrate) {
  fun init() {
    registerFlammableBlocks()
    registerStrippableBlocks()
    registerCompostableBlocks()

    // register datapack entries, like villager trades
    onDatapackReload()
  }

  fun initClient() {
    registerCutoutRenders()
    registerBiomeColors()
  }

  // register flammable block
  private fun registerFlammableBlocks() {
    for (block in registrate.blockRegistry.entries) {
      if (block.getContext().flammabilityBurnChance == 0 || block.getContext().flammabilitySpreadChance == 0) continue
      FlammableBlockRegistry.getDefaultInstance().add(
        block.getBlock().get(),
        block.getContext().flammabilityBurnChance,
        block.getContext().flammabilitySpreadChance
      )
    }
  }

  // register strippable blocks
  private fun registerStrippableBlocks() {
    for (block in registrate.blockRegistry.entries) {
      val other = block.getContext().strippableOther
      if (other == null) continue
      if (!other.get().defaultBlockState().hasProperty(BlockStateProperties.AXIS)
      ) throw BadAttributeValueExpException("Output stripped block should have 'axis' property!")
      if (!block.getBlock().get().defaultBlockState().hasProperty(BlockStateProperties.AXIS)
      ) throw BadAttributeValueExpException("Input stripped block should have 'axis' property!")
      StrippableBlockRegistry.register(block.getBlock().get(), other.get())
    }
  }

  // register compostable blocks
  private fun registerCompostableBlocks() {
    for (block in registrate.blockRegistry.entries) {
      val amount = block.getContext().compostableAmount
      if (amount <= 0) continue
      try {
        CompostingChanceRegistry.INSTANCE.add(block.getBlock().get().asItem(), amount)
      } catch (e: Exception) {
        println("Failed to add block ${block.getBlock().get().name} to compostables")
      }
    }
    for (item in registrate.itemRegistry.entries) {
      val amount = item.compostableAmount
      if (amount <= 0) continue
      try {
        CompostingChanceRegistry.INSTANCE.add(item.getItem().get(), amount)
      } catch (e: Exception) {
        println("Failed to add block ${item.getItem().get().descriptionId} to compostables")
      }
    }
  }

  // register cutout renders
  private fun registerCutoutRenders() {
    BlockRenderLayerMap.INSTANCE.putBlocks(
      net.minecraft.client.renderer.RenderType.cutout(),
      *registrate.blockRegistry.entries.filter { it.getContext().hasCutoutRender }.map { it.getBlock().get() }
        .toTypedArray()
    )
  }

  // register biome colors
  private fun registerBiomeColors() {
    for (block in registrate.blockRegistry.entries) {
      if (!block.getContext().hasBiomeColors) continue
      try {
        ColorProviderRegistry.BLOCK.register(
          { state, level, pos, tint ->
            if (level != null && pos != null) BiomeColors.getAverageFoliageColor(
              level,
              pos
            ) else FoliageColor.getDefaultColor()
          }, block.getBlock().get()
        )
        ColorProviderRegistry.ITEM.register(
          { stack, layer ->
            val provider = ColorProviderRegistry.ITEM.get(Blocks.TALL_GRASS);
            return@register provider?.getColor(stack, layer) ?: -1
          }, block.getBlock().get().asItem()
        )
      } catch (e: Exception) {
        println("Failed to add block ${block.getBlock().get().name} to compostables")
      }
    }
  }

  // Bellow we deal with villager trades registering, fabric is quite complex to do it...
  private fun onDatapackReload() {
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
    handleLoadVillagerTrades()
  }

  private fun onEndDatapackReload(
    server: MinecraftServer,
    resourceManager: CloseableResourceManager,
    success: Boolean
  ) {
    handleLoadVillagerTrades()
  }

  private fun handleLoadVillagerTrades() {
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