package com.dannbrown.databoxlib.content.block

import com.simibubi.create.content.contraptions.actors.seat.SeatBlock
import com.simibubi.create.content.contraptions.actors.seat.SeatEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

class CustomSeatBlock(props: Properties) : SeatBlock(props, DyeColor.WHITE) {
  override fun use(state: BlockState, world: Level, pos: BlockPos, player: Player, hand: InteractionHand, blockHitResult: BlockHitResult): InteractionResult {
    if (player.isPassenger && player.vehicle is SeatEntity) {
      // Check for spacebar press
      if (isSpacebarPressed(player)) {
        // Execute your method when spacebar is pressed
        pressedSeat(world, pos, player)
      }
    }

    return super.use(state, world, pos, player, hand, blockHitResult)
  }

  private fun pressedSeat(world: Level, pos: BlockPos, player: Player) {
    if (!world.isClientSide) {
      // Log something or perform any other action when spacebar is pressed
      println("Spacebar pressed while sitting on the seat block!")
    }
  }

  companion object {
    fun isSpacebarPressed(player: Player): Boolean {
      return true
//      return player.input.jumping
    }
  }
}