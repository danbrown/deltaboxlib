package com.dannbrown.deltaboxlib.registrate.network

import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

// Inspired by https://github.com/MehVahdJukaar/Moonlight/blob/1.20/common/src/main/java/net/mehvahdjukaar/moonlight/api/platform/network/ChannelHandler.java
abstract class NetworkChannelHandler(protected val name: String) {
  abstract fun <M : NetworkPacket> register(
    direction: NetworkDirection,
    messageClass: Class<M>,
    decoder: (FriendlyByteBuf) -> M
  )

  interface Context {
    val direction: NetworkDirection
    val sender: Player?
    fun disconnect(reason: Component)
  }

  abstract fun sendToClientPlayer(serverPlayer: ServerPlayer, networkPacket: NetworkPacket)
  abstract fun sendToAllClientPlayers(networkPacket: NetworkPacket)
  abstract fun sendToAllClientPlayersInRange(
    level: Level,
    pos: BlockPos,
    radius: Double,
    networkPacket: NetworkPacket
  )

  fun sendToAllClientPlayersInDefaultRange(level: Level, pos: BlockPos, networkPacket: NetworkPacket) {
    sendToAllClientPlayersInRange(level, pos, 64.0, networkPacket)
  }

  fun sendToAllClientPlayersInParticleRange(level: Level, pos: BlockPos, networkPacket: NetworkPacket) {
    sendToAllClientPlayersInRange(level, pos, 32.0, networkPacket)
  }

  fun sendToAllClientPlayersInDistantParticleRange(level: Level, pos: BlockPos, networkPacket: NetworkPacket) {
    sendToAllClientPlayersInRange(level, pos, 512.0, networkPacket)
  }

  abstract fun sendToAllClientPlayersTrackingEntity(target: Entity, networkPacket: NetworkPacket)
  abstract fun sendToAllClientPlayersTrackingEntityAndSelf(target: Entity, networkPacket: NetworkPacket)
  abstract fun sendToServer(networkPacket: NetworkPacket)
}
