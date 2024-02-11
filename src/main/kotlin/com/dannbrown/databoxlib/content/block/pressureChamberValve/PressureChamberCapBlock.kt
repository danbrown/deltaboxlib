package com.dannbrown.databoxlib.content.block.pressureChamberValve

import com.dannbrown.databoxlib.init.ProjectBlockEntities
import com.simibubi.create.AllSoundEvents
import com.simibubi.create.content.equipment.wrench.IWrenchable
import com.simibubi.create.content.processing.basin.BasinBlockEntity
import com.simibubi.create.foundation.block.IBE
import com.simibubi.create.foundation.block.ProperWaterloggedBlock
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import java.util.*

class PressureChamberCapBlock(properties: Properties) : Block(properties), ProperWaterloggedBlock, IBE<PressureChamberTileEntity>,
                                                        IWrenchable {
  init {
    registerDefaultState(super.defaultBlockState()
      .setValue(ON_A_BASIN, false))
    registerDefaultState(super.defaultBlockState()
      .setValue(WATERLOGGED, false))
    registerDefaultState(super.defaultBlockState()
      .setValue(OPEN, false))
    registerDefaultState(super.defaultBlockState()
      .setValue(POWERED, false))
  }

  override fun getShape(pState: BlockState, pLevel: BlockGetter, pPos: BlockPos, pContext: CollisionContext): VoxelShape {
    if (!pState.getValue<Boolean>(OPEN)) return Shapes.or(
      box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
      box(5.0, 2.0, 5.0, 11.0, 4.0, 11.0)
    )
    if (pState.getValue<Direction>(FACING) == Direction.SOUTH) return Shapes.or(
      box(0.0, 0.0, 14.0, 16.0, 16.0, 16.0),
      box(5.0, 5.0, 16.0, 11.0, 11.0, 18.0)
    )
    if (pState.getValue<Direction>(FACING) == Direction.WEST) return Shapes.or(
      box(0.0, 0.0, 0.0, 2.0, 16.0, 16.0),
      box(-2.0, 5.0, 5.0, 0.0, 11.0, 11.0)
    )
    return if (pState.getValue<Direction>(FACING) == Direction.NORTH) Shapes.or(
      box(0.0, 0.0, 0.0, 16.0, 16.0, 2.0),
      box(5.0, 5.0, -2.0, 11.0, 11.0, 0.0)
    )
    else Shapes.or(
      box(14.0, 0.0, 0.0, 16.0, 16.0, 16.0),
      box(16.0, 5.0, 5.0, 18.0, 11.0, 11.0)
    )
  }

  override fun onPlace(state: BlockState, level: Level, pos: BlockPos, p_60569_: BlockState, p_60570_: Boolean) {
    super.onPlace(state, level, pos, p_60569_, p_60570_)
    if (level.getBlockEntity(pos.below()) is BasinBlockEntity) level.setBlock(
      pos,
      state.setValue(ON_A_BASIN, true),
      2
    )
    else level.setBlock(
      pos, state.setValue(
        ON_A_BASIN, false
      ), 2
    )
  }

  override fun neighborChanged(
    state: BlockState,
    level: Level,
    pos: BlockPos,
    block: Block,
    blockPos2: BlockPos,
    p_57552_: Boolean
  ) {
    var state = state
    val flag: Boolean = level.hasNeighborSignal(pos)
    state =
      if (level.getBlockEntity(pos.below()) is BasinBlockEntity) state.setValue(ON_A_BASIN, true)
      else state.setValue(
        ON_A_BASIN, false
      )
    if (flag != state.getValue<Boolean>(POWERED)) {
      if (flag != state.getValue(OPEN)) {
        level.levelEvent(null, if (flag) 1037 else 1036, pos, 0)
      }
      level.setBlock(pos,
        state.setValue(POWERED, flag)
          .setValue(OPEN, flag),
        2)
      var a = level.getBlockEntity(pos) as PressureChamberTileEntity

      if (flag && level.getBlockEntity(pos) is PressureChamberTileEntity && a.steamInside) {
        level.playSound(null, pos, AllSoundEvents.STEAM.mainEvent, SoundSource.BLOCKS, 1.1f, 0.3f)
        a.steamInside = false
        for (i in 0..2) {
          (level as ServerLevel).sendParticles(
            ParticleTypes.CAMPFIRE_COSY_SMOKE,
            pos.x + 0.5f + Random().nextDouble(-0.3, 0.3),
            pos.y.toDouble(),
            pos.z + 0.5f + Random().nextDouble(-0.3, 0.3),
            0,
            0.0,
            1.0,
            0.0,
            0.01
          )
        }
      }
    }
    else {
      level.setBlock(pos, state, 2)
    }
  }

  override fun use(
    state: BlockState, level: Level, pos: BlockPos,
    player: Player, hand: InteractionHand, hit: BlockHitResult
  ): InteractionResult {
    val currentState = state.getValue(OPEN)
    var a = level.getBlockEntity(pos) as PressureChamberTileEntity

    if (!currentState && level.getBlockEntity(pos) is PressureChamberTileEntity && a.steamInside) {
      for (i in 0..2) {
        if (level is ServerLevel) {
          level.sendParticles(
            ParticleTypes.CAMPFIRE_COSY_SMOKE,
            pos.x + 0.5f + Random().nextDouble(-0.3, 0.3),
            pos.y.toDouble(),
            pos.z + 0.5f + Random().nextDouble(-0.3, 0.3),
            0,
            0.0,
            1.0,
            0.0,
            0.01
          )
          level.playSound(null, pos, AllSoundEvents.STEAM.mainEvent, SoundSource.BLOCKS, 0.1f, 0.3f)
          a.steamInside = false
        }
      }
    }
    if (!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
      level.setBlock(pos, state.setValue(OPEN, !currentState), 3)
      level.levelEvent(null, if (currentState) 1037 else 1036, pos, 0)
    }

    return InteractionResult.SUCCESS
  }

  override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
    builder.add(ON_A_BASIN)
    builder.add(FACING)
    builder.add(OPEN)
    builder.add(WATERLOGGED)
    builder.add(POWERED)
    super.createBlockStateDefinition(builder)
  }

  override fun getStateForPlacement(pContext: BlockPlaceContext): BlockState {
    return if (pContext.player!!.isShiftKeyDown) {
      this.defaultBlockState()
        .setValue(FACING, pContext.horizontalDirection.opposite)
    }
    else {
      this.defaultBlockState()
        .setValue(FACING, pContext.horizontalDirection)
    }
  }

  override fun getFluidState(pState: BlockState): FluidState {
    return fluidState(pState)
  }

  override fun updateShape(
    pState: BlockState, pDirection: Direction, pNeighborState: BlockState,
    pLevel: LevelAccessor, pCurrentPos: BlockPos, pNeighborPos: BlockPos
  ): BlockState {
    updateWater(pLevel, pState, pCurrentPos)
    return pState
  }

  override fun getBlockEntityClass(): Class<PressureChamberTileEntity> {
    return PressureChamberTileEntity::class.java
  }

  override fun getBlockEntityType(): BlockEntityType<out PressureChamberTileEntity> {
    return ProjectBlockEntities.PRESSURE_CHAMBER_VALVE.get()
  }

  companion object {
    val FACING = BlockStateProperties.HORIZONTAL_FACING
    val ON_A_BASIN = BooleanProperty.create("on_a_basin")
    val OPEN = BlockStateProperties.OPEN
    val POWERED = BlockStateProperties.POWERED
    val WATERLOGGED = BlockStateProperties.WATERLOGGED
  }
}

