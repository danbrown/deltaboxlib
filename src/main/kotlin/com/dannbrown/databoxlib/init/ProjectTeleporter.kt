package com.dannbrown.databoxlib.init

import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.portal.PortalInfo
import net.minecraftforge.common.util.ITeleporter
import java.util.function.Function

class ProjectTeleporter(private val target: PortalInfo) : ITeleporter {
  override fun getPortalInfo(entity: Entity, destWorld: ServerLevel, defaultPortalInfo: Function<ServerLevel, PortalInfo>): PortalInfo {
    return target
  }

  override fun isVanilla(): Boolean {
    return false
  }

  override fun playTeleportSound(player: ServerPlayer?, sourceWorld: ServerLevel?, destWorld: ServerLevel?): Boolean {
    return false
  }

  override fun placeEntity(entity: Entity, currentLevel: ServerLevel, destLevel: ServerLevel, yaw: Float, repositionEntity: Function<Boolean, Entity>): Entity {
    return repositionEntity.apply(false)
  }
}