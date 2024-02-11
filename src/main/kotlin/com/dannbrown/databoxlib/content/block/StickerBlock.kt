package com.dannbrown.databoxlib.content.block

import com.dannbrown.databoxlib.content.misc.utils.RotationState
import com.simibubi.create.content.equipment.wrench.IWrenchable
import com.simibubi.create.foundation.block.ProperWaterloggedBlock
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Items
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock
import net.minecraft.world.level.block.LadderBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.AttachFace
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class StickerBlock(props: Properties) : FaceAttachedHorizontalDirectionalBlock(props.lightLevel { state -> if (state.getValue(GLOWING)) 1 else 0 }
  .hasPostProcess { state, _, _ -> state.getValue(GLOWING) }
  .emissiveRendering { state, _, _ -> state.getValue(GLOWING) }), ProperWaterloggedBlock, IWrenchable {
  init {
    registerDefaultState(defaultBlockState().setValue(ProperWaterloggedBlock.WATERLOGGED, false)
      .setValue(ROTATION, RotationState.PI_0)
      .setValue(GLOWING, false))
  }

  override fun createBlockStateDefinition(pBuilder: StateDefinition.Builder<Block, BlockState>) {
    super.createBlockStateDefinition(pBuilder.add(FACE, FACING, ROTATION, GLOWING, ProperWaterloggedBlock.WATERLOGGED))
  }

  override fun onPlace(blockState: BlockState, level: Level, blockPos: BlockPos, blockState2: BlockState, p_60570_: Boolean
  ) { //    DataboxContent.LOGGER.info("FACE ${blockState.getValue(FACE)}")
    //    DataboxContent.LOGGER.info("FACING ${blockState.getValue(FACING)}")
    //    DataboxContent.LOGGER.info("ROTATION ${blockState.getValue(ROTATION)}")
    super.onPlace(blockState, level, blockPos, blockState2, p_60570_)
  }

  override fun getStateForPlacement(pContext: BlockPlaceContext): BlockState? {
    var stateForPlacement = super.getStateForPlacement(pContext) ?: return null
    if (stateForPlacement.getValue(FACE) == AttachFace.FLOOR) stateForPlacement = stateForPlacement.setValue(FACING, stateForPlacement.getValue(FACING).opposite)
    stateForPlacement.setValue(ROTATION, RotationState.PI_0) // always start with 0 rotation
    return withWater(stateForPlacement, pContext)
  }

  override fun getFluidState(pState: BlockState): FluidState {
    return fluidState(pState)
  }

  override fun use(blockState: BlockState, level: Level, blockPos: BlockPos, player: Player, hand: InteractionHand, hitResult: BlockHitResult
  ): InteractionResult { // if holding a glow ink sac, toggle glowing
    if ((player.mainHandItem.item == Items.GLOW_INK_SAC || player.offhandItem.item == Items.GLOW_INK_SAC) && !level.isClientSide && !blockState.getValue(GLOWING)) {
      level.setBlock(blockPos, blockState.setValue(GLOWING, true), 3) // consume the ink sac if not in creative mode
      if (!player.isCreative) {
        val itemStack = if (player.mainHandItem.item == Items.GLOW_INK_SAC) player.mainHandItem else player.offhandItem
        itemStack.shrink(1)
      }
      return InteractionResult.SUCCESS
    } // when clicks with empty hand, rotate the block 90 degrees
    if (!level.isClientSide && hand == InteractionHand.MAIN_HAND) {
      val direction: Direction = blockState.getValue(FACING)
      val face: AttachFace = blockState.getValue(FACE)
      val newDirection: Direction = when (face) {
        AttachFace.FLOOR -> {
          if (direction.axis === Direction.Axis.X) {
            if (direction == Direction.EAST) Direction.SOUTH
            else if (direction == Direction.SOUTH) Direction.WEST
            else if (direction == Direction.WEST) Direction.NORTH
            else Direction.EAST
          }
          else {
            if (direction == Direction.EAST) Direction.SOUTH
            else if (direction == Direction.SOUTH) Direction.WEST
            else if (direction == Direction.WEST) Direction.NORTH
            else Direction.EAST
          }
        }

        AttachFace.CEILING -> {
          if (direction.axis === Direction.Axis.X) {
            if (direction == Direction.EAST) Direction.NORTH
            else if (direction == Direction.NORTH) Direction.WEST
            else if (direction == Direction.WEST) Direction.SOUTH
            else Direction.EAST
          }
          else {
            if (direction == Direction.EAST) Direction.NORTH
            else if (direction == Direction.NORTH) Direction.WEST
            else if (direction == Direction.WEST) Direction.SOUTH
            else Direction.EAST
          }
        } // face is wall, remain the same direction
        else -> {
          direction
        }
      } // ROTATION
      val newRotation: RotationState = when (face) {
        AttachFace.WALL -> { // change the rotation to turn the block 45 degrees
          val rotation: RotationState = blockState.getValue(ROTATION)
          val newRotation: RotationState = when (rotation) {
            RotationState.PI_0 -> RotationState.PI_90
            RotationState.PI_90 -> RotationState.PI_180
            RotationState.PI_180 -> RotationState.PI_270
            RotationState.PI_270 -> RotationState.PI_0
          }
          newRotation
        } // face is not wall, remain the same rotation
        else -> {
          blockState.getValue(ROTATION)
        }
      }


      level.setBlock(blockPos,
        blockState.setValue(FACING, newDirection)
          .setValue(ROTATION, newRotation),
        3)
    }

    return InteractionResult.SUCCESS
  }

  override fun getShape(blockState: BlockState, blockGetter: BlockGetter?, blockPos: BlockPos?, collisionContext: CollisionContext?
  ): VoxelShape {
    val direction: Direction = blockState.getValue(FACING)

    return when (blockState.getValue(FACE) as AttachFace) {
      AttachFace.FLOOR -> {
        if (direction.axis === Direction.Axis.X) {
          return FLOOR_AABB_X
        }
        FLOOR_AABB_Z
      }

      AttachFace.WALL -> when (direction) {
        Direction.EAST -> EAST_AABB
        Direction.WEST -> WEST_AABB
        Direction.SOUTH -> SOUTH_AABB
        Direction.NORTH -> NORTH_AABB
        else -> NORTH_AABB
      }

      AttachFace.CEILING -> if (direction.axis === Direction.Axis.X) {
        CEILING_AABB_X
      }
      else {
        CEILING_AABB_Z
      }

      else -> if (direction.axis === Direction.Axis.X) {
        CEILING_AABB_X
      }
      else {
        CEILING_AABB_Z
      }
    }
    return when (blockState.getValue(FACING) as Direction) {
      Direction.NORTH -> NORTH_AABB
      Direction.SOUTH -> SOUTH_AABB
      Direction.WEST -> WEST_AABB
      Direction.EAST -> EAST_AABB
      Direction.UP -> UP_AABB
      Direction.DOWN -> DOWN_AABB
    }
  }

  override fun updateShape(blockState: BlockState, direction: Direction, blockState2: BlockState, levelAccessor: LevelAccessor, blockPos: BlockPos, blockPos2: BlockPos
  ): BlockState {
    return if (direction.opposite == blockState.getValue(LadderBlock.FACING) && !blockState.canSurvive(levelAccessor, blockPos)) {
      Blocks.AIR.defaultBlockState()
    }
    else {
      if (blockState.getValue(LadderBlock.WATERLOGGED)) {
        levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor))
      }
      super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2)
    }
  }

  companion object {
    val ROTATION = EnumProperty.create("rotation", RotationState::class.java)
    val GLOWING = BooleanProperty.create("glowing")
    protected val EAST_AABB = box(0.0, 0.0, 0.0, 1.0, 16.0, 16.0)
    protected val WEST_AABB = box(15.0, 0.0, 0.0, 16.0, 16.0, 16.0)
    protected val SOUTH_AABB = box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0)
    protected val NORTH_AABB = box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0)
    protected val UP_AABB = box(2.0, 15.0, 0.0, 12.0, 16.0, 11.0)
    protected val DOWN_AABB = box(0.0, 0.0, 0.0, 16.0, 3.0, 16.0)
    protected val CEILING_AABB_X = box(0.0, 15.0, 0.0, 16.0, 16.0, 16.0)
    protected val FLOOR_AABB_X = box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0)
    protected val CEILING_AABB_Z = box(0.0, 15.0, 0.0, 16.0, 16.0, 16.0)
    protected val FLOOR_AABB_Z = box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0)
  }
}


