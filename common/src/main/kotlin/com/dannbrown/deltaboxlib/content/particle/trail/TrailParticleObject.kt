package com.dannbrown.deltaboxlib.content.particle.trail

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.FriendlyByteBuf


/*? if <1.21 {*/
class TrailParticleObject(pOverrideLimiter: Boolean) :
  ParticleType<TrailParticleOption>(pOverrideLimiter, TrailParticleOption.DESERIALIZER), ParticleOptions {
  override fun getType(): TrailParticleObject {
    return this
  }

  override fun codec(): Codec<TrailParticleOption> {
    return TrailParticleOption.CODEC
  }

  override fun writeToNetwork(pBuffer: FriendlyByteBuf) {}

  override fun writeToString(): String {
    return BuiltInRegistries.PARTICLE_TYPE.getKey(this).toString()
  }
}
