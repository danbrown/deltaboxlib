package com.dannbrown.deltaboxlib.registrate.util.forge

import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import net.minecraft.server.MinecraftServer
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.loading.FMLEnvironment
import net.minecraftforge.server.ServerLifecycleHooks

object DeltaboxUtilImpl {
  @JvmStatic
  fun getSide(): DeltaboxUtil.Side {
    return if (FMLEnvironment.dist == Dist.CLIENT) DeltaboxUtil.Side.CLIENT else DeltaboxUtil.Side.SERVER
  }

  @JvmStatic
  fun getCurrentServer(): MinecraftServer? {
    return ServerLifecycleHooks.getCurrentServer()
  }
}