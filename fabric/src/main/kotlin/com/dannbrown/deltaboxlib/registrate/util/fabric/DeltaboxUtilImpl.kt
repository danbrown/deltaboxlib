package com.dannbrown.deltaboxlib.registrate.util.fabric

import com.dannbrown.deltaboxlib.fabric.init.DeltaboxLibModFabric
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.server.MinecraftServer

object DeltaboxUtilImpl {
  @JvmStatic
  fun getSide(): DeltaboxUtil.Side {
    return if (FabricLoader.getInstance().environmentType == EnvType.CLIENT) DeltaboxUtil.Side.CLIENT else DeltaboxUtil.Side.SERVER
  }

  @JvmStatic
  fun getCurrentServer(): MinecraftServer? {
    return DeltaboxLibModFabric.currentServer
  }
}