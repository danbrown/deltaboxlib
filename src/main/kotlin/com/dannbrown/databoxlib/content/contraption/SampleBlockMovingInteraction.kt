package com.dannbrown.databoxlib.content.contraption

import com.simibubi.create.content.contraptions.AbstractContraptionEntity
import com.simibubi.create.content.contraptions.behaviour.MovingInteractionBehaviour
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player

class SampleBlockMovingInteraction : MovingInteractionBehaviour() {
  override fun handlePlayerInteraction(player: Player, activeHand: InteractionHand, localPos: BlockPos, contraptionEntity: AbstractContraptionEntity): Boolean {
    val contraption = contraptionEntity.contraption
    val level = contraptionEntity.level()
    val actor = contraption.getActorAt(localPos) ?: return false
    val blockInfo = actor.left
    if (level.isClientSide) { // looks like contraption block entities are just available on the client side, sending a packet to the server to open the gui
      val blockEntity = contraption.presentBlockEntities[blockInfo.pos]

      return true
    }
    return true
  }
}