package com.dannbrown.databoxlib.content.ship.block.sail

import com.dannbrown.databoxlib.content.ship.SpaceShipControl
import com.dannbrown.databoxlib.content.ship.extensions.getShipManagingPos
import com.dannbrown.databoxlib.content.ship.extensions.getShipObjectManagingPos
import com.dannbrown.databoxlib.init.ProjectBlockEntities
import com.simibubi.create.foundation.block.IBE
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.pathfinder.PathComputationType
import net.minecraft.world.phys.BlockHitResult
import org.valkyrienskies.core.api.ships.getAttachment

class SailBlock(props: Properties) : HorizontalDirectionalBlock(props), IBE<SailBlockEntity> {

  init {
    registerDefaultState(this.stateDefinition.any()
      .setValue(FACING, Direction.NORTH))
  }

  override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, isMoving: Boolean) {
    super.onPlace(state, level, pos, oldState, isMoving)

    if (level.isClientSide) return
    level as ServerLevel
    val ship = level.getShipObjectManagingPos(pos) ?: level.getShipManagingPos(pos) ?: return
    val shipControl = SpaceShipControl.getOrCreate(ship)
    shipControl.addSail(pos)
  }

  override fun destroy(level: LevelAccessor, pos: BlockPos, state: BlockState) {
    super.destroy(level, pos, state)

    if (level.isClientSide) return
    level as ServerLevel
    val ship = level.getShipObjectManagingPos(pos) ?: level.getShipManagingPos(pos) ?: return
    val shipControl = ship.getAttachment<SpaceShipControl>() ?: return
    shipControl.removeSail(pos)
  }

  override fun use(
    state: BlockState,
    level: Level,
    pos: BlockPos,
    player: Player,
    hand: InteractionHand,
    blockHitResult: BlockHitResult
  ): InteractionResult {
    if (level.isClientSide) return InteractionResult.SUCCESS
    val blockEntity = level.getBlockEntity(pos) as SailBlockEntity

    return if (hand == InteractionHand.MAIN_HAND && !player.isShiftKeyDown) {
      player.openMenu(blockEntity)
      InteractionResult.CONSUME
    }
    else InteractionResult.PASS
  }

  override fun getStateForPlacement(pContext: BlockPlaceContext): BlockState? {
    return defaultBlockState().setValue(FACING, pContext.horizontalDirection)
  }

  override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
    builder.add(FACING)
    super.createBlockStateDefinition(builder)
  }

  override fun newBlockEntity(blockPos: BlockPos, state: BlockState): BlockEntity {
    return SailBlockEntity(ProjectBlockEntities.SAIL_BLOCK.get(), blockPos, state)
  }

  override fun useShapeForLightOcclusion(blockState: BlockState): Boolean {
    return true
  }

  override fun isPathfindable(
    blockState: BlockState,
    blockGetter: BlockGetter,
    blockPos: BlockPos,
    pathComputationType: PathComputationType
  ): Boolean {
    return false
  }

  override fun rotate(state: BlockState, rotation: Rotation): BlockState {
    return state.setValue(FACING, rotation.rotate(state.getValue(FACING) as Direction)) as BlockState
  }

  override fun <T : BlockEntity?> getTicker(
    level: Level,
    state: BlockState,
    type: BlockEntityType<T>
  ): BlockEntityTicker<T> = BlockEntityTicker { pLevel, pPos, pState, blockEntity ->
    if (pLevel.isClientSide) return@BlockEntityTicker

    if (blockEntity is SailBlockEntity) {
      blockEntity.tick()
    }
  }

  override fun getBlockEntityClass(): Class<SailBlockEntity> {
    return SailBlockEntity::class.java
  }

  override fun getBlockEntityType(): BlockEntityType<out SailBlockEntity> {
    return ProjectBlockEntities.SAIL_BLOCK.get()
  }
}
