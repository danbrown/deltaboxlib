package com.dannbrown.databoxlib.content.block.pressureChamberValve

import com.dannbrown.databoxlib.content.recipe.PressureChamberRecipe
import com.dannbrown.databoxlib.init.ProjectRecipeTypes
import com.simibubi.create.content.processing.basin.BasinBlockEntity
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.Container
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import java.util.*

class PressureChamberTileEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
  BasinOperatingBlockEntity(type, pos, state) {
//  var runningTicks = 0
//  var processingTicks = 0
//  var running = false
//  override fun read(compound: CompoundTag, clientPacket: Boolean) {
//    running = compound.getBoolean("Running")
//    runningTicks = compound.getInt("Ticks")
//    super.read(compound, clientPacket)
//    if (clientPacket && hasLevel()) getBasin().ifPresent { bte: BasinBlockEntity ->
//      bte.setAreFluidsMoving(
//        running && runningTicks <= 20
//      )
//    }
//  }
//
//  private fun spawnSnowflakes(pLevel: Level, pPos: BlockPos) {
//    val random: RandomSource = pLevel.random
//
//    for (direction in Direction.values()) {
//      val blockPos = pPos.relative(direction)
//
//      if (!pLevel.getBlockState(blockPos).isSolidRender(pLevel, blockPos)) {
//        val axis = direction.axis
//        val d1 = if (axis === Direction.Axis.X) 0.5 + 0.25625 * direction.stepX.toDouble() else random.nextFloat()
//        val d2 = if (axis === Direction.Axis.Y) 0.5 + 0.25625 * direction.stepY.toDouble() else random.nextFloat()
//        val d3 = if (axis === Direction.Axis.Z) 0.5 + 0.25625 * direction.stepZ.toDouble() else random.nextFloat()
//
//        pLevel.addParticle(
//          ParticleTypes.SNOWFLAKE,
//          pPos.x + d1.toDouble(),
//          pPos.y + d2.toDouble(),
//          pPos.z + d3.toDouble(),
//          0.0,
//          0.0,
//          0.0
//        )
//      }
//    }
//  }
//
////  override fun getBasin(): Optional<BasinBlockEntity> {
////    if (level == null) return Optional.empty()
//////    val basinBE = level!!.getBlockEntity(worldPosition.below(1)) as BasinBlockEntity : return Optional.empty()
////    val basinBE = level!!.getBlockEntity(worldPosition.below(1))
////    if(basinBE is BasinBlockEntity){
////      return if (blockState.getValue(PressureChamberValveBlock.OPEN)) Optional.empty() else Optional.of(basinBE)
////    }
////    else {
////      return Optional.empty()
////    }
////  }
//
//
//  public override fun write(compound: CompoundTag, clientPacket: Boolean) {
//    compound.putBoolean("Running", running)
//    compound.putInt("Ticks", runningTicks)
//    super.write(compound, clientPacket)
//  }
//
//  override fun tick() {
//    super.tick()
//    if (runningTicks >= 40) {
//      running = false
//      runningTicks = 0
//      basinChecker.scheduleUpdate()
//      return
//    }
//
//    val speed = Mth.abs(getSpeed().toDouble().toFloat())
//
//    if (speed != 0f && level != null && level!!.random.nextInt(8) == 0) {
//      spawnSnowflakes(level!!, worldPosition.below());
//      spawnSnowflakes(level!!, worldPosition.below().below());
//    }
//
//
//    if (running && level != null) {
//      if (level!!.isClientSide && runningTicks == 20) renderParticles()
//      if ((!level!!.isClientSide || isVirtual) && runningTicks == 20) {
//        if (processingTicks < 0) {
//          var recipeSpeed = 1f
//          if (currentRecipe is ProcessingRecipe<*>) {
//            val t = (currentRecipe as ProcessingRecipe<*>).processingDuration
//            if (t != 0) recipeSpeed = t / 100f
//          }
//          processingTicks = Mth.clamp(Mth.log2((512 / speed).toInt()) * Mth.ceil(recipeSpeed * 15) + 1, 1, 512)
//          val basin = getBasin()
//          if (basin.isPresent) {
//            val tanks = basin.get()
//              .tanks
//            if (!tanks.first
//                .isEmpty()
//              || !tanks.second
//                .isEmpty()
//            ) level!!.playSound(
//              null, worldPosition, SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_AMBIENT,
//              SoundSource.BLOCKS, .75f, if (speed < 65) .75f else 1.5f
//            )
//          }
//        } else {
//          processingTicks--
//          if (processingTicks == 0) {
//            runningTicks++
//            processingTicks = -1
//            applyBasinRecipe()
//            sendData()
//          }
//        }
//      }
//      if (runningTicks != 20) runningTicks++
//    }
//  }
//
//  fun renderParticles() {
//    val basin = getBasin()
//    if (basin.isEmpty || level == null) return
//    for (inv in basin.get()
//      .invs) {
//      for (slot in 0 until inv.slots) {
//        val stackInSlot = inv.getItem(slot)
//        if (stackInSlot.isEmpty) continue
//        val data = ItemParticleOption(ParticleTypes.ITEM, stackInSlot)
//        spillParticle(data)
//      }
//    }
//    for (behaviour in basin.get()
//      .tanks) {
//      if (behaviour == null) continue
//      for (tankSegment in behaviour.tanks) {
//        if (tankSegment.isEmpty(0f)) continue
//        spillParticle(FluidFX.getFluidParticle(tankSegment.renderedFluid))
//      }
//    }
//  }
//
//  protected fun spillParticle(data: ParticleOptions?) {
//    assert(level != null)
//    val angle = level!!.random.nextFloat() * 360
//    var offset = Vec3(0.0, 0.0, 0.25)
//    offset = VecHelper.rotate(offset, angle.toDouble(), Direction.Axis.Y)
//    var target = VecHelper.rotate(offset, (if (getSpeed() > 0) 25 else -25).toDouble(), Direction.Axis.Y)
//      .add(0.0, .25, 0.0)
//    val center = offset.add(VecHelper.getCenterOf(worldPosition))
//    target = VecHelper.offsetRandomly(target.subtract(offset), level!!.random, 1 / 128f)
//    level!!.addParticle(data, center.x, center.y - 1.75f, center.z, target.x, target.y, target.z)
//  }
//
//  override fun < C : Container> matchStaticFilters(r: Recipe<C>): Boolean {
//    return r.type === DataboxRecipeTypes.PRESSURE_CHAMBER.getType<RecipeType<PressureChamberRecipe>>()
//  }
//
//  override fun startProcessingBasin() {
//    if (running && runningTicks <= 20) return
//    super.startProcessingBasin()
//    running = true
//    runningTicks = 0
//  }
//
//  override fun continueWithPreviousRecipe(): Boolean {
//    runningTicks = 20
//    return true
//  }
//
//  override fun onBasinRemoved() {
//    if (!running) return
//    runningTicks = 40
//    running = false
//  }
//
//  override fun getRecipeCacheKey(): Any {
//    return PressureChamberRecipesKey
//  }
//
//  override fun isRunning(): Boolean {
//    return running
//  }
//
//  @OnlyIn(Dist.CLIENT)
//  override fun tickAudio() {
//    super.tickAudio()
//
//    // SoundEvents.BLOCK_STONE_BREAK
//    val slow = Mth.abs(getSpeed()) < 65f
//    if (slow && AnimationTickHolder.getTicks() % 2 == 0) return
//    if (runningTicks == 20) AllSoundEvents.MIXING.playAt(level, worldPosition, .75f, 1f, true)
//  }
//
//  companion object {
//    private val PressureChamberRecipesKey = Any()
//  }
//}
  var processingTime = 0
  var running = false
  override fun write(compound: CompoundTag, clientPacket: Boolean) {
    super.write(compound, clientPacket)
    compound.putInt("ProcessingTime", processingTime)
    compound.putBoolean("Running", running)
    compound.putBoolean("SteamInside", steamInside)
  }

  override fun read(compound: CompoundTag, clientPacket: Boolean) {
    super.read(compound, clientPacket)
    processingTime = compound.getInt("ProcessingTime")
    running = compound.getBoolean("Running")
    steamInside = compound.getBoolean("SteamInside")
  }

  override fun onBasinRemoved() {
    if (!running) return
    processingTime = 0
    currentRecipe = null
    running = false
  }

  var steamInside = false
  override fun tick() {
    super.tick()
    if (!level!!.isClientSide && (currentRecipe == null || processingTime == -1) || blockState.getValue(
        PressureChamberCapBlock.OPEN
      ) || !blockState.getValue(
        PressureChamberCapBlock.ON_A_BASIN
      )
    ) {
      running = false
      processingTime = -1
      basinChecker.scheduleUpdate()
    }

    if (running) steamInside = true
    if (running && level != null) {
      if (!level!!.isClientSide && processingTime <= 0) {
        processingTime = -1
        applyBasinRecipe()
        sendData()
      }
      if (!level!!.isClientSide && processingTime % 20 == 0 && Random().nextInt() % 4 === 0) {
        level!!.playSound(
          null, worldPosition, SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT,
          SoundSource.BLOCKS, .15f, if (speed < 65) .75f else 1.5f
        )
      }
      if (processingTime == 1) level!!.playSound(
        null, worldPosition, SoundEvents.BREWING_STAND_BREW,
        SoundSource.BLOCKS, .15f, if (speed < 65) .75f else 1.5f
      )
      if (processingTime > 0) --processingTime
    }
  }

  override fun updateBasin(): Boolean {
    if (running) return true
    if (level == null || level!!.isClientSide) return true
//    if (getBasin().filter { obj: BasinBlockEntity -> obj.canContinueProcessing() }.isEmpty) return true
    val recipes = getMatchingRecipes()
    if (recipes.isEmpty()) return true
    currentRecipe = recipes[0]
    startProcessingBasin()
    sendData()
    return true
  }

  override fun startProcessingBasin() {
    if (running && processingTime > 0) return
    super.startProcessingBasin()
    running = true
    val processed: PressureChamberRecipe = getRecipe() ?: return
    processingTime = if (currentRecipe != null) processed.processingDuration else 20
  }

  fun getRecipe(): PressureChamberRecipe? {
    return if (currentRecipe == null) null else currentRecipe as PressureChamberRecipe
  }

  override fun isRunning(): Boolean {
    return running
  }

  override fun getBasin(): Optional<BasinBlockEntity> {
    if (level == null) return Optional.empty()
    val basinBE = level!!.getBlockEntity(worldPosition.below(1))
    return if (basinBE !is BasinBlockEntity) Optional.empty() else Optional.of(basinBE)
  }
//  override fun getBasin(): Optional<BasinBlockEntity> {
//    if (level == null) return Optional.empty()
////    val basinBE = level!!.getBlockEntity(worldPosition.below(1)) as BasinBlockEntity : return Optional.empty()
//    val basinBE = level!!.getBlockEntity(worldPosition.below(1))
//    if(basinBE is BasinBlockEntity){
//      return if (blockState.getValue(PressureChamberValveBlock.OPEN)) Optional.empty() else Optional.of(basinBE)
//    }
//    else {
//      return Optional.empty()
//    }
//  }
  override fun <C : Container> matchStaticFilters(r: Recipe<C>): Boolean {
    return r.type === ProjectRecipeTypes.PRESSURE_CHAMBER.getType<RecipeType<PressureChamberRecipe>>()
  }
//  override fun < C : Container> matchStaticFilters(r: Recipe<C>): Boolean {
//    return r.type === DataboxRecipeTypes.COOLING.getType<RecipeType<CoolingRecipe>>()
//  }
  override fun getRecipeCacheKey(): Any {
    return PressureChamberRecipeKey
  }

  companion object {
    private val PressureChamberRecipeKey = Any()
  }
}

