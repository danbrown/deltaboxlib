package com.dannbrown.deltaboxlib.registrate.util

import net.minecraft.sounds.SoundEvent
import net.minecraft.world.level.block.SoundType
import java.util.function.Supplier

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SoundTypeSupplier(
  volume: Float,
  pitch: Float,
  private val breakSound: Supplier<SoundEvent>,
  private val stepSound: Supplier<SoundEvent>,
  private val placeSound: Supplier<SoundEvent>,
  private val hitSound: Supplier<SoundEvent>,
  private val fallSound: Supplier<SoundEvent>
) : SoundType(volume, pitch, null, null, null, null, null) {
  override fun getBreakSound(): SoundEvent = breakSound.get()
  override fun getStepSound(): SoundEvent = stepSound.get()
  override fun getPlaceSound(): SoundEvent = placeSound.get()
  override fun getHitSound(): SoundEvent = hitSound.get()
  override fun getFallSound(): SoundEvent = fallSound.get()

  fun get(): SoundType {
    return this
  }
}
