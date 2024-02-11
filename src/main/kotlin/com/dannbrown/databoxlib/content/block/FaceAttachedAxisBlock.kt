package com.dannbrown.databoxlib.content.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.AttachFace
import net.minecraft.world.level.block.state.properties.BlockStateProperties

//class FaceAttachedAxisBlock(p: Properties): HorizontalDirectionalBlock(p) {
//}


open class FaceAttachedAxisBlock(p: Properties) : RotatedPillarBlock(p) {
  override fun canSurvive(blockState: BlockState, levelReader: LevelReader, blockPos: BlockPos): Boolean {
    return canAttach(levelReader, blockPos, getConnectedDirection(blockState).opposite)
  }

  override fun rotate(blockState: BlockState, rotation: Rotation): BlockState {
    return rotatePillar(blockState, rotation).setValue(
      FACING,
      rotation.rotate(blockState.getValue(FACING))
    )
  }

  override fun mirror(blockState: BlockState, mirror: Mirror): BlockState {
    return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)))
  }

  override fun getStateForPlacement(placeContext: BlockPlaceContext): BlockState? {
    for (direction in placeContext.getNearestLookingDirections()) {
      var blockstate: BlockState = if (direction.axis === Direction.Axis.Y) {
        defaultBlockState().setValue(FACE, if (direction == Direction.UP) AttachFace.CEILING else AttachFace.FLOOR)
          .setValue(
            FACING, placeContext.horizontalDirection
          )
      } else {
        defaultBlockState().setValue(FACE, AttachFace.WALL).setValue(FACING, direction.opposite)
      }
      if (blockstate.canSurvive(placeContext.level, placeContext.clickedPos)) {
        return blockstate
      }
    }
    defaultBlockState().setValue(AXIS, placeContext.clickedFace.axis);
    return null
  }

  override fun updateShape(
    blockState: BlockState,
    direction: Direction,
    blockState2: BlockState,
    levelAccessor: LevelAccessor,
    blockPos: BlockPos,
    blockPos2: BlockPos
  ): BlockState {
    return if (getConnectedDirection(blockState).opposite == direction && !blockState.canSurvive(
        levelAccessor,
        blockPos
      )
    ) Blocks.AIR.defaultBlockState() else super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2)
  }

  companion object {
    val FACE = BlockStateProperties.ATTACH_FACE
    val FACING = BlockStateProperties.HORIZONTAL_FACING
    fun canAttach(levelReader: LevelReader, blockPos: BlockPos, direction: Direction): Boolean {
      val blockpos = blockPos.relative(direction)
      return levelReader.getBlockState(blockpos).isFaceSturdy(levelReader, blockpos, direction.opposite)
    }

    protected fun getConnectedDirection(blockState: BlockState): Direction {
      return when (blockState.getValue(FACE) as AttachFace) {
        AttachFace.CEILING -> Direction.DOWN
        AttachFace.FLOOR -> Direction.UP
        else -> blockState.getValue(FACING)
      }
    }
  }
}