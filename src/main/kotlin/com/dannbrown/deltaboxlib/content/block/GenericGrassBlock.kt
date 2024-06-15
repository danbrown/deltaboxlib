package com.dannbrown.deltaboxlib.content.block

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.BonemealableBlock
import net.minecraft.world.level.block.BushBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import net.minecraftforge.common.IForgeShearable

class GenericGrassBlock(
  props: Properties,
  private val placeOn: ((blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos) -> Boolean)? = null,
  private val isSticky: Boolean = false,
  private val isHarmful: Boolean = false,
) : BushBlock(props), IForgeShearable, BonemealableBlock {

  override fun mayPlaceOn(blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos): Boolean {
    if (placeOn != null) {
      return placeOn.invoke(blockState, blockGetter, blockPos)
    }
    return super.mayPlaceOn(blockState, blockGetter, blockPos)
  }

  override fun entityInside(pState: BlockState, pLevel: Level, pPos: BlockPos, pEntity: Entity) {
    super.entityInside(pState, pLevel, pPos, pEntity)
    if (isSticky) pEntity.makeStuckInBlock(pState, Vec3(0.9, 1.5, 0.9))
    if (isHarmful) pEntity.hurt(pLevel.damageSources().cactus(), 1.0f)
  }


  override fun getShape(
    blockState: BlockState,
    blockGetter: BlockGetter,
    blockPos: BlockPos,
    collisionContext: CollisionContext
  ): VoxelShape {
    return SHAPE
  }

  override fun isValidBonemealTarget(
    levelReader: LevelReader,
    blockPos: BlockPos,
    blockState: BlockState,
    isClient: Boolean
  ): Boolean {
    return false
  }

  override fun isBonemealSuccess(
    level: Level,
    randomSource: RandomSource,
    blockPos: BlockPos,
    blockState: BlockState
  ): Boolean {
    return false
  }

  override fun performBonemeal(
    serverLevel: ServerLevel,
    randomSource: RandomSource,
    blockPos: BlockPos,
    blockState: BlockState
  ) {

  }

  companion object {
    protected const val AABB_OFFSET = 6.0f
    protected val SHAPE = box(2.0, 0.0, 2.0, 14.0, 13.0, 14.0)
  }

}