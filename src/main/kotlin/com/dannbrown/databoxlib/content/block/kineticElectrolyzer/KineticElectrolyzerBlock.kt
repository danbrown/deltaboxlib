package com.dannbrown.databoxlib.content.block.kineticElectrolyzer

import com.dannbrown.databoxlib.init.ProjectBlockEntities
import com.simibubi.create.AllBlocks
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock
import com.simibubi.create.foundation.block.IBE
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import org.jetbrains.annotations.NotNull

class KineticElectrolyzerBlock(properties: Properties?) : HorizontalKineticBlock(properties),
                                                          IBE<KineticElectrolyzerTileEntity> {
  override fun canSurvive(@NotNull state: BlockState, worldIn: LevelReader, pos: BlockPos): Boolean {
    return !AllBlocks.BASIN.has(worldIn.getBlockState(pos.below()))
  }

  override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
    val preferredSide = getPreferredHorizontalFacing(context)
    return if (preferredSide != null) defaultBlockState().setValue(
      HORIZONTAL_FACING,
      preferredSide
    )
    else super.getStateForPlacement(context)
  }

  override fun getRotationAxis(state: BlockState): Direction.Axis {
    return Direction.Axis.Y
  }

  override fun getBlockEntityClass(): Class<KineticElectrolyzerTileEntity> {
    return KineticElectrolyzerTileEntity::class.java
  }

  override fun getBlockEntityType(): BlockEntityType<out KineticElectrolyzerTileEntity> {
    return ProjectBlockEntities.KINETIC_ELECTROLYZER_TILE.get()
  }

  override fun hasShaftTowards(world: LevelReader?, pos: BlockPos?, state: BlockState?, face: Direction): Boolean {
    return face === Direction.UP
  }
}


