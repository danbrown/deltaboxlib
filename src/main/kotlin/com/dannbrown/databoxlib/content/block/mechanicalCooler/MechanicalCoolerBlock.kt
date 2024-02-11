package com.dannbrown.databoxlib.content.block.mechanicalCooler

import com.dannbrown.databoxlib.init.ProjectBlockEntities
import com.simibubi.create.AllBlocks
import com.simibubi.create.content.kinetics.base.KineticBlock
import com.simibubi.create.foundation.block.IBE
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import org.jetbrains.annotations.NotNull

//class MechanicalCoolerBlock {
//}
class MechanicalCoolerBlock(properties: Properties?) : KineticBlock(properties),
                                                       IBE<MechanicalCoolerTileEntity> {
  override fun canSurvive(@NotNull state: BlockState, worldIn: LevelReader, pos: BlockPos): Boolean {
    return !AllBlocks.BASIN.has(worldIn.getBlockState(pos.below()))
  }

  override fun getRotationAxis(state: BlockState): Direction.Axis {
    return Direction.Axis.Y
  }

  override fun getBlockEntityClass(): Class<MechanicalCoolerTileEntity> {
    return MechanicalCoolerTileEntity::class.java
  }

  override fun getBlockEntityType(): BlockEntityType<out MechanicalCoolerTileEntity> {
    return ProjectBlockEntities.MECHANICAL_COOLER_TILE.get()
  }

  override fun hasShaftTowards(world: LevelReader?, pos: BlockPos?, state: BlockState?, face: Direction): Boolean {
    return face === Direction.UP
  }
}

