package com.dannbrown.deltaboxlib.content.block

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import java.util.function.Supplier

open class GenericCropBlock(
  props: Properties,
  protected val isBudding: Boolean = false,
  protected val grownBlock: Supplier<out Block>? = null,
  protected val isDouble: Boolean = false,
  protected val isBush: Boolean = false,
  protected val includeSeedOnDrop: Boolean = false,
  protected val fruitItem: Supplier<ItemLike>?,
  protected val chance: Float = 1f,
  protected val multiplier: Int = 1
) : CropBlock(props) {
  init {
    registerDefaultState(defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER).setValue(AGE, 0))
  }

  override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
    builder.add(*arrayOf(HALF, AGE))
  }

  override fun getStateForPlacement(blockPlaceContext: BlockPlaceContext): BlockState? {
    val blockPos = blockPlaceContext.clickedPos
    val level = blockPlaceContext.level
    return if (blockPos.y < level.maxBuildHeight - 1 && level.getBlockState(blockPos.above())
        .canBeReplaced(blockPlaceContext)
    ) super.getStateForPlacement(blockPlaceContext)
    else null
  }

  override fun getBaseSeedId(): ItemLike {
    return if (fruitItem !== null) fruitItem.get() else this.asItem()
  }

  override fun getCloneItemStack(arg: BlockGetter, pos: BlockPos, state: BlockState): ItemStack {
    return ItemStack(if (fruitItem !== null) fruitItem.get() else this.asItem())
  }

  fun getPlant(level: BlockGetter, pos: BlockPos): BlockState { // soft-override on forge
    return this.defaultBlockState()
  }

  override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
    return if (isDouble) {
      if (state.getValue(HALF) == DoubleBlockHalf.LOWER) Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)
      else SHAPE_BY_AGE[state.getValue(this.ageProperty)]
    } else {
      SHAPE_BY_AGE[state.getValue(this.ageProperty)]
    }
  }

  override fun use(
    blockState: BlockState,
    level: Level,
    blockPos: BlockPos,
    player: Player,
    interactionHand: InteractionHand,
    blockHitResult: BlockHitResult
  ): InteractionResult {
    val age = blockState.getValue(this.ageProperty)
    val isMaxAge = age == maxAge
    if (!isMaxAge && player.getItemInHand(interactionHand).`is`(Items.BONE_MEAL)) {
      return InteractionResult.PASS
    } else if (isMaxAge && !level.isClientSide && isBush) {
      dropResources(level as ServerLevel, blockPos)
      updateBlockState(level, blockPos, blockState.setValue(this.ageProperty, MID_STAGE), 2)
      return InteractionResult.SUCCESS
    } else {
      return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult)
    }
  }

  override fun setPlacedBy(
    level: Level,
    blockPos: BlockPos,
    blockState: BlockState,
    livingEntity: LivingEntity?,
    itemStack: ItemStack
  ) {
    if (isDouble) {
      val blockPos2 = blockPos.above()
      val blockAbove = level.getBlockState(blockPos2)
      if ((blockAbove.isAir || blockAbove.`is`(Blocks.WATER)) && !blockAbove.isSolid) {
        level.setBlock(
          blockPos2,
          copyWaterloggedFrom(level, blockPos2, defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER)),
          3
        )
      }
    } else {
      super.setPlacedBy(level, blockPos, blockState, livingEntity, itemStack)
    }
  }

  override fun canSurvive(state: BlockState, worldIn: LevelReader, pos: BlockPos): Boolean {
    if (isDouble) {
      return (worldIn.getRawBrightness(pos, 0) >= 8 || worldIn.canSeeSky(pos)) && canSurviveDouble(state, worldIn, pos)
    } else {
      return super.canSurvive(state, worldIn, pos)
    }
  }


  override fun playerWillDestroy(level: Level, blockPos: BlockPos, blockState: BlockState, player: Player) {
    if (isDouble && !level.isClientSide) {
      if (player.isCreative) {
        preventCreativeDropFromBottomPart(level, blockPos, blockState, player)
      } else {
        dropResources(blockState, level, blockPos, null, player, player.mainHandItem)
      }
    }
    super.playerWillDestroy(level, blockPos, blockState, player)
  }

  override fun getSeed(blockState: BlockState, blockPos: BlockPos): Long {
    if (isDouble) {
      return Mth.getSeed(
        blockPos.x,
        blockPos.below(if (blockState.getValue(HALF) == DoubleBlockHalf.LOWER) 0 else 1).y,
        blockPos.z
      )
    } else {
      return super.getSeed(blockState, blockPos)
    }
  }

  protected open fun canSurviveDouble(pState: BlockState, pLevel: LevelReader, pPos: BlockPos): Boolean {
    if (pState.getValue(HALF) != DoubleBlockHalf.UPPER) {
      val blockState2 = pLevel.getBlockState(pPos.above())
      return blockState2.`is`(this) && blockState2.getValue(HALF) == DoubleBlockHalf.UPPER && super.canSurvive(
        pState,
        pLevel,
        pPos
      )
    } else {
      val blockState2 = pLevel.getBlockState(pPos.below())
      return blockState2.`is`(this) && blockState2.getValue(HALF) == DoubleBlockHalf.LOWER
    }
  }

  override fun growCrops(level: Level, blockPos: BlockPos, blockState: BlockState) {
    var newState = this.getAge(blockState) + this.getBonemealAgeIncrease(level)
    if (newState > this.maxAge) newState = this.maxAge

    if (blockState.getValue(HALF) == DoubleBlockHalf.UPPER) {
      // Only update the lower half's age when bonemeal is used on the upper half
      val lowerPos = blockPos.below()
      val lowerState = level.getBlockState(lowerPos)
      if (lowerState.`is`(this) && lowerState.getValue(HALF) == DoubleBlockHalf.LOWER) {
        updateBlockState(level, lowerPos, this.getStateForAge(newState), 2)
      }
    } else {
      if (newState == this.maxAge && isBudding && !isDouble) growTall(level, blockPos)
      else updateBlockState(level, blockPos, this.getStateForAge(newState), 2)
    }
  }

  override fun isRandomlyTicking(blockState: BlockState): Boolean {
    return if (isBudding) true else super.isRandomlyTicking(blockState)
  }

  override fun randomTick(
    blockState: BlockState,
    serverLevel: ServerLevel,
    blockPos: BlockPos,
    randomSource: RandomSource
  ) {
    // if is the upper part, doesn't grow with random tick, depends on the lower part
    if (blockState.getValue(HALF) == DoubleBlockHalf.UPPER) return
    // grow normaly if its the lower part, should work for the normal and double variants

    if (serverLevel.getRawBrightness(blockPos, 0) >= 9) {
      val i = this.getAge(blockState)
      if (i == this.maxAge && isBudding) {
        growTall(serverLevel, blockPos)
        return
      }
      if (i < this.maxAge) {
        val f = getGrowthSpeed(this, serverLevel, blockPos);
        if (randomSource.nextInt((25.0F / f).toInt() + 1) == 0) {
          updateBlockState(serverLevel, blockPos, this.getStateForAge(i + 1), 2)
        }
      }
    }
  }

  override fun isValidBonemealTarget(
    levelReader: LevelReader,
    blockPos: BlockPos,
    blockState: BlockState,
    bl: Boolean
  ): Boolean {
    return if (isBudding) true
    else super.isValidBonemealTarget(levelReader, blockPos, blockState, bl)
  }

  protected open fun updateBlockState(level: Level, blockPos: BlockPos, blockstate: BlockState, i: Int = 3) {
    level.setBlock(blockPos, blockstate, i)
    if (isDouble) level.setBlock(getDoubleOtherPos(blockPos, blockstate), getOtherBlockstate(blockstate), i)
  }

  protected open fun growTall(level: Level, blockPos: BlockPos) {
    if (!isBudding || grownBlock == null || !level.getBlockState(blockPos.above()).canBeReplaced()) return
    val abovePos = blockPos.above()
    level.setBlock(blockPos, grownBlock.get().defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER), 3)
    level.setBlock(abovePos, grownBlock.get().defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER), 3)
  }

  protected open fun getDoubleOtherPos(blockPos: BlockPos, blockstate: BlockState): BlockPos {
    if (blockstate.getValue(HALF) == DoubleBlockHalf.LOWER) {
      return blockPos.above()
    } else if (blockstate.getValue(HALF) == DoubleBlockHalf.UPPER) {
      return blockPos.below()
    }
    return blockPos
  }

  protected open fun getOtherBlockstate(blockstate: BlockState): BlockState {
    if (blockstate.getValue(HALF) == DoubleBlockHalf.LOWER) {
      return blockstate.setValue(HALF, DoubleBlockHalf.UPPER)
    } else if (blockstate.getValue(HALF) == DoubleBlockHalf.UPPER) {
      return blockstate.setValue(HALF, DoubleBlockHalf.LOWER)
    }
    return blockstate
  }

  protected open fun dropResources(pLevel: ServerLevel, pPos: BlockPos) {
    // if no seed and drop item is set, at least one is required
    if (!isBush) return

    // if have a seed item, do 'multiplier' rows of 'chance' to pop a seed
    var seedsToDrop = 0
    for (i in 0 until multiplier) {
      if (pLevel.random.nextFloat() < chance) {
        seedsToDrop++
      }
    }
    if (seedsToDrop > 0 && includeSeedOnDrop) Block.popResource(pLevel, pPos, ItemStack(this.asItem(), seedsToDrop))

    // if have a drop item, do 'multiplier' rows of 'chance' to pop a drop item
    if (fruitItem !== null) {
      var dropsToDrop = 1
      for (i in 0 until multiplier - 1) {
        if (pLevel.random.nextFloat() < chance) {
          dropsToDrop++
        }
      }
      if (dropsToDrop > 0) Block.popResource(pLevel, pPos, ItemStack(fruitItem.get(), dropsToDrop))
    }

    pLevel.playSound(
      null,
      pPos,
      SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES,
      SoundSource.BLOCKS,
      1.0f,
      0.8f + pLevel.random.nextFloat() * 0.4f
    )
  }

  protected fun preventCreativeDropFromBottomPart(
    level: Level,
    blockPos: BlockPos,
    blockState: BlockState,
    player: Player?
  ) {
    val doubleBlockHalf = blockState.getValue(HALF)
    if (doubleBlockHalf == DoubleBlockHalf.UPPER) {
      val belowPos = blockPos.below()
      val belowState = level.getBlockState(belowPos)
      if (belowState.`is`(this) && belowState.getValue(HALF) == DoubleBlockHalf.LOWER) {
        val blockState3 =
          if (belowState.fluidState.`is`(Fluids.WATER)) Blocks.WATER.defaultBlockState() else Blocks.AIR.defaultBlockState()
        level.setBlock(belowPos, blockState3, 35)
        level.levelEvent(player, 2001, belowPos, getId(belowState))
      }
    }
  }

  protected open fun copyWaterloggedFrom(
    levelReader: LevelReader,
    blockPos: BlockPos,
    blockState: BlockState
  ): BlockState {
    return if (blockState.hasProperty(BlockStateProperties.WATERLOGGED)) blockState.setValue(
      BlockStateProperties.WATERLOGGED,
      levelReader.isWaterAt(blockPos)
    ) else blockState
  }

  companion object {
    protected val SHAPE_BY_AGE = arrayOf<VoxelShape>(
      Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
      Block.box(0.0, 0.0, 0.0, 16.0, 3.0, 16.0),
      Block.box(0.0, 0.0, 0.0, 16.0, 5.0, 16.0),
      Block.box(0.0, 0.0, 0.0, 16.0, 7.0, 16.0),
      Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
      Block.box(0.0, 0.0, 0.0, 16.0, 9.0, 16.0),
      Block.box(0.0, 0.0, 0.0, 16.0, 9.0, 16.0),
      Block.box(0.0, 0.0, 0.0, 16.0, 10.0, 16.0)
    )

    val HALF = BlockStateProperties.DOUBLE_BLOCK_HALF
    protected val MID_STAGE = 3
  }
}