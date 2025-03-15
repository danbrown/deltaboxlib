package com.dannbrown.deltaboxlib.init

import com.dannbrown.deltaboxlib.content.particle.trail.TrailParticle
import com.dannbrown.deltaboxlib.content.particle.trail.TrailParticleObject

object DeltaboxParticles {
  val TRAIL =
    DeltaboxLibMod.REGISTRATE.particleType("trail", { TrailParticleObject(false) }, { TrailParticle.Provider(it) })


  fun register() {
    // init class
  }
}