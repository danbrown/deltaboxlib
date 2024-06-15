package com.dannbrown.deltaboxlib.content.block


import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.monster.Ravager
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.BushBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.common.IPlantable
import net.minecraftforge.common.PlantType
import net.minecraftforge.event.ForgeEventFactory
import java.util.function.Supplier

/**
 * A bush which grows, representing the earlier stage of another plant.
 * Once mature, a budding bush can "grow past" it, and turn into something different.
 */
open class BuddingBushBlock(properties: Properties, private val seedItem: Supplier<ItemLike>) : BushBlock(properties) {
  override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
    return SHAPE_BY_AGE[state.getValue(AGE)]
  }

  override fun mayPlaceOn(state: BlockState, level: BlockGetter, pos: BlockPos): Boolean {
    return state.`is`(Blocks.FARMLAND)
  }

  override fun getPlantType(world: BlockGetter, pos: BlockPos): PlantType {
    return PlantType.CROP
  }

  protected fun getAge(state: BlockState): Int {
    return state.getValue<Int>(AGE)
  }

  fun getStateForAge(age: Int): BlockState {
    return defaultBlockState().setValue<Int, Int>(AGE, age)
  }

  fun isMaxAge(state: BlockState): Boolean {
    return state.getValue<Int>(AGE) >= MAX_AGE
  }

  fun getMaxAge(): Int {
    return MAX_AGE
  }

  override fun isRandomlyTicking(state: BlockState): Boolean {
    return canGrowPastMaxAge() || !isMaxAge(state)
  }

  override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
    if (!level.isAreaLoaded(pos, 1)) return
    if (level.getRawBrightness(pos, 0) >= 9) {
      val age = getAge(state)
      if (age <= MAX_AGE) {
        val growthSpeed = getGrowthSpeed(this, level, pos)
        if (ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt((25.0f / growthSpeed).toInt() + 1) == 0)) {
          if (isMaxAge(state)) {
            growPastMaxAge(state, level, pos, random)
          } else {
            level.setBlockAndUpdate(pos, getStateForAge(age + 1))
          }
          ForgeHooks.onCropsGrowPost(level, pos, state)
        }
      }
    }
  }

  /**
   * Determines if this bush should keep ticking at max age. If true, calls growPastMaxAge() on each growth success.
   */
  open fun canGrowPastMaxAge(): Boolean {
    return false
  }

  open fun growPastMaxAge(state: BlockState?, level: ServerLevel?, pos: BlockPos?, random: RandomSource?) {}
  override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean {
    return (level.getRawBrightness(pos, 0) >= 8 || level.canSeeSky(pos)) && super.canSurvive(state, level, pos)
  }

  override fun entityInside(state: BlockState, level: Level, pos: BlockPos, entity: Entity) {
    if (entity is Ravager && ForgeEventFactory.getMobGriefingEvent(level, entity)) {
      level.destroyBlock(pos, true, entity)
    }
    super.entityInside(state, level, pos, entity)
  }

  protected fun getBaseSeedId(): ItemLike {
    return seedItem.get()
  }

  override fun getCloneItemStack(level: BlockGetter, pos: BlockPos, state: BlockState): ItemStack {
    return ItemStack(getBaseSeedId())
  }

  override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
    builder.add(AGE)
  }

  companion object {
    const val MAX_AGE = 3
    val AGE = IntegerProperty.create("age", 0, 4)
    private val SHAPE_BY_AGE = arrayOf(
      box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
      box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0),
      box(0.0, 0.0, 0.0, 16.0, 10.0, 16.0),
      box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0),
      box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0)
    )

    protected fun getGrowthSpeed(block: Block?, level: BlockGetter, pos: BlockPos): Float {
      var speed = 1.0f
      val posBelow = pos.below()
      for (posX in -1..1) {
        for (posZ in -1..1) {
          var speedBonus = 0.0f
          val stateBelow = level.getBlockState(posBelow.offset(posX, 0, posZ))
          if (stateBelow.canSustainPlant(level, posBelow.offset(posX, 0, posZ), Direction.UP, block as IPlantable?)) {
            speedBonus = 1.0f
            if (stateBelow.isFertile(level, pos.offset(posX, 0, posZ))) {
              speedBonus = 3.0f
            }
          }
          if (posX != 0 || posZ != 0) {
            speedBonus /= 4.0f
          }
          speed += speedBonus
        }
      }
      val posNorth = pos.north()
      val posSouth = pos.south()
      val posWest = pos.west()
      val posEast = pos.east()
      val matchesEastWestRow = level.getBlockState(posWest).`is`(block) || level.getBlockState(posEast).`is`(block)
      val matchesNorthSouthRow = level.getBlockState(posNorth).`is`(block) || level.getBlockState(posSouth).`is`(block)
      if (matchesEastWestRow && matchesNorthSouthRow) {
        speed /= 2.0f
      } else {
        val matchesDiagonalRows =
          level.getBlockState(posWest.north()).`is`(block) || level.getBlockState(posEast.north())
            .`is`(block) || level.getBlockState(posEast.south()).`is`(block) || level.getBlockState(posWest.south())
            .`is`(block)
        if (matchesDiagonalRows) {
          speed /= 2.0f
        }
      }
      return speed
    }
  }
}

