package com.dannbrown.databoxlib.content.block.kineticElectrolyzer

import com.dannbrown.databoxlib.content.recipe.ElectrolyzerRecipe
import com.dannbrown.databoxlib.init.ProjectRecipeTypes
import com.simibubi.create.AllSoundEvents
import com.simibubi.create.content.fluids.FluidFX
import com.simibubi.create.content.processing.basin.BasinBlockEntity
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity
import com.simibubi.create.content.processing.recipe.ProcessingRecipe
import com.simibubi.create.foundation.utility.AnimationTickHolder
import com.simibubi.create.foundation.utility.VecHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.ItemParticleOption
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.nbt.CompoundTag
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.world.Container
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

class KineticElectrolyzerTileEntity(typeIn: BlockEntityType<*>, pos: BlockPos?, state: BlockState?) :
  BasinOperatingBlockEntity(typeIn, pos, state) {
  var runningTicks = 0
  var processingTicks = 0
  var running = false
  override fun read(compound: CompoundTag, clientPacket: Boolean) {
    running = compound.getBoolean("Running")
    runningTicks = compound.getInt("Ticks")
    super.read(compound, clientPacket)
    if (clientPacket && hasLevel()) getBasin().ifPresent { bte: BasinBlockEntity ->
      bte.setAreFluidsMoving(
        running && runningTicks <= 20
      )
    }
  }

  public override fun write(compound: CompoundTag, clientPacket: Boolean) {
    compound.putBoolean("Running", running)
    compound.putInt("Ticks", runningTicks)
    super.write(compound, clientPacket)
  }

  override fun tick() {
    super.tick()

    if (runningTicks >= 40) {
      running = false
      runningTicks = 0
      basinChecker.scheduleUpdate()
      return
    }
    val speed = Mth.abs(getSpeed().toDouble()
      .toFloat())

    if (speed != 0f && level != null && level!!.random.nextInt(8) == 0) {
      spawnSparks(level!!, worldPosition.below())
      spawnSparks(level!!,
        worldPosition.below()
          .below())
    }

    if (running && level != null) {
      if (level!!.isClientSide && runningTicks == 20) {
        renderParticles()
      }

      if ((!level!!.isClientSide || isVirtual) && runningTicks == 20) {
        if (processingTicks < 0) {
          var recipeSpeed = 1f
          if (currentRecipe is ProcessingRecipe<*>) {
            val t = (currentRecipe as ProcessingRecipe<*>).processingDuration
            if (t != 0) recipeSpeed = t / 100f
          }
          processingTicks = Mth.clamp(Mth.log2((512 / speed).toInt()) * Mth.ceil(recipeSpeed * 15) + 1, 1, 512)
          val basin = getBasin()
          if (basin.isPresent) {
            val tanks = basin.get()
              .tanks
            if (!tanks.first
                .isEmpty()
              || !tanks.second
                .isEmpty()
            ) level!!.playSound(
              null, worldPosition, SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_AMBIENT,
              SoundSource.BLOCKS, .75f, if (speed < 65) .75f else 1.5f
            )
          }
        }
        else {
          processingTicks--
          if (processingTicks == 0) {
            runningTicks++
            processingTicks = -1
            applyBasinRecipe()
            sendData()
          }
        }
      }
      if (runningTicks != 20) runningTicks++
    }
  }

  fun renderParticles() {
    val basin = getBasin()
    if (basin.isEmpty || level == null) return
    for (inv in basin.get()
      .invs) {
      for (slot in 0 until inv.slots) {
        val stackInSlot = inv.getItem(slot)
        if (stackInSlot.isEmpty) continue
        val data = ItemParticleOption(ParticleTypes.ITEM, stackInSlot)
        spillParticle(data)
      }
    }
    for (behaviour in basin.get()
      .tanks) {
      if (behaviour == null) continue
      for (tankSegment in behaviour.tanks) {
        if (tankSegment.isEmpty(0f)) continue
        spillParticle(FluidFX.getFluidParticle(tankSegment.renderedFluid))
      }
    }
  }

  private fun spillParticle(data: ParticleOptions?) {
    assert(level != null)
    val angle = level!!.random.nextFloat() * 360
    var offset = Vec3(0.0, 0.0, 0.25)
    offset = VecHelper.rotate(offset, angle.toDouble(), Direction.Axis.Y)
    var target = VecHelper.rotate(offset, (if (getSpeed() > 0) 25 else -25).toDouble(), Direction.Axis.Y)
      .add(0.0, .25, 0.0)
    val center = offset.add(VecHelper.getCenterOf(worldPosition))

    target = VecHelper.offsetRandomly(target.subtract(offset), level!!.random, 1 / 128f)
    level!!.addParticle(data, center.x, center.y - 1.75f, center.z, target.x, target.y, target.z)
  }

  override fun <C : Container> matchStaticFilters(r: Recipe<C>): Boolean {
    return r.type === ProjectRecipeTypes.ELECTROLYSIS.getType<RecipeType<ElectrolyzerRecipe>>()
  }

  override fun startProcessingBasin() {
    if (running && runningTicks <= 20) return
    super.startProcessingBasin()
    running = true
    runningTicks = 0
  }

  override fun continueWithPreviousRecipe(): Boolean {
    runningTicks = 20
    return true
  }

  override fun onBasinRemoved() {
    if (!running) return
    runningTicks = 40
    running = false
  }

  override fun getRecipeCacheKey(): Any {
    return ElectrolizerRecipesKey
  }

  override fun isRunning(): Boolean {
    return running
  }

  private fun spawnSparks(pLevel: Level, pPos: BlockPos) {
    val random: RandomSource = pLevel.random

    for (direction in Direction.values()) {
      val blockPos = pPos.relative(direction)

      if (!pLevel.getBlockState(blockPos)
          .isSolidRender(pLevel, blockPos)) {
        val axis = direction.axis
        val d1 = if (axis === Direction.Axis.X) 0.5 + 0.25625 * direction.stepX.toDouble() else random.nextFloat()
        val d2 = if (axis === Direction.Axis.Y) 0.5 + 0.25625 * direction.stepY.toDouble() else random.nextFloat()
        val d3 = if (axis === Direction.Axis.Z) 0.5 + 0.25625 * direction.stepZ.toDouble() else random.nextFloat()

        pLevel.addParticle(
          ParticleTypes.ELECTRIC_SPARK,
          pPos.x + d1.toDouble(),
          pPos.y + d2.toDouble(),
          pPos.z + d3.toDouble(),
          0.0,
          0.0,
          0.0
        )
      }
    }
  }

  @OnlyIn(Dist.CLIENT)
  override fun tickAudio() {
    super.tickAudio()
    // SoundEvents.BLOCK_STONE_BREAK
    val slow = Mth.abs(getSpeed()) < 65f
    if (slow && AnimationTickHolder.getTicks() % 2 == 0) return
    if (runningTicks == 20) AllSoundEvents.MIXING.playAt(level, worldPosition, .75f, 1f, true)
  }

  companion object {
    private val ElectrolizerRecipesKey = Any()
  }
}

