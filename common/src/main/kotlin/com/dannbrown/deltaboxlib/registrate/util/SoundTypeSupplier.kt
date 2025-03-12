package com.dannbrown.deltaboxlib.registrate.util

import net.minecraft.sounds.SoundEvent
import net.minecraft.world.level.block.SoundType
import java.util.function.Supplier

class SoundTypeSupplier(
  val f: Float,
  val g: Float,
  val soundEvent: Supplier<SoundEvent>,
  val soundEvent2: Supplier<SoundEvent>,
  val soundEvent3: Supplier<SoundEvent>,
  val soundEvent4: Supplier<SoundEvent>,
  val soundEvent5: Supplier<SoundEvent>
) {
  fun get(): SoundType {
    return SoundType(
      f,
      g,
      soundEvent.get(),
      soundEvent2.get(),
      soundEvent3.get(),
      soundEvent4.get(),
      soundEvent5.get()
    )
  }
}