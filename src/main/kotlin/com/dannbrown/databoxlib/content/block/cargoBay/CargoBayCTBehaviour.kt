package com.dannbrown.databoxlib.content.block.cargoBay

import com.dannbrown.databoxlib.content.utils.spriteShifts.CreateBlockSpriteShifts
import com.simibubi.create.api.connectivity.ConnectivityHandler
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Direction.Axis
import net.minecraft.core.Direction.AxisDirection
import net.minecraft.world.level.BlockAndTintGetter
import net.minecraft.world.level.block.state.BlockState
import org.jetbrains.annotations.Nullable

class CargoBayCTBehaviour : ConnectedTextureBehaviour.Base() {
  override fun getShift(
    state: BlockState,
    direction: Direction,
    @Nullable sprite: TextureAtlasSprite?
  ): CTSpriteShiftEntry? {
    val vaultBlockAxis: Axis? = CargoBayBlock.getVaultBlockAxis(state)
    val small = !CargoBayBlock.isLarge(state)
    if (vaultBlockAxis == null) return null
    if (direction.axis === vaultBlockAxis) return CreateBlockSpriteShifts.CARGO_FRONT[small]
    if (direction === Direction.UP) return CreateBlockSpriteShifts.CARGO_TOP[small]
    return if (direction === Direction.DOWN) CreateBlockSpriteShifts.CARGO_BOTTOM[small] else CreateBlockSpriteShifts.CARGO_SIDE[small]
  }

  override fun getUpDirection(
    reader: BlockAndTintGetter,
    pos: BlockPos,
    state: BlockState,
    face: Direction
  ): Direction {
    val vaultBlockAxis: Axis? = CargoBayBlock.getVaultBlockAxis(state)
    val alongX = vaultBlockAxis === Axis.X
    if (face.axis.isVertical && alongX
    ) return super.getUpDirection(reader, pos, state, face)
      .getClockWise()
    return if (face.axis === vaultBlockAxis || face.axis.isVertical
    ) super.getUpDirection(reader, pos, state, face)
    else Direction.fromAxisAndDirection(
      vaultBlockAxis,
      if (alongX) AxisDirection.POSITIVE else AxisDirection.NEGATIVE
    )
  }

  override fun getRightDirection(
    reader: BlockAndTintGetter?,
    pos: BlockPos,
    state: BlockState,
    face: Direction
  ): Direction {
    val vaultBlockAxis: Axis? = CargoBayBlock.getVaultBlockAxis(state)
    if (face.axis.isVertical && vaultBlockAxis === Axis.X
    ) return super.getRightDirection(reader, pos, state, face)
      .getClockWise()
    return if (face.axis === vaultBlockAxis || face.axis.isVertical
    ) super.getRightDirection(reader, pos, state, face)
    else Direction.fromAxisAndDirection(
      Axis.Y,
      face.axisDirection
    )
  }

  override fun connectsTo(
    state: BlockState, other: BlockState, reader: BlockAndTintGetter?, pos: BlockPos?,
    otherPos: BlockPos?, face: Direction?
  ): Boolean {
    return state === other && ConnectivityHandler.isConnected<CargoBayBlockEntity>(
      reader,
      pos,
      otherPos
    )
  }
}