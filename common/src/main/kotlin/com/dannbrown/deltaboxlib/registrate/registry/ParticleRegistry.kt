package com.dannbrown.deltaboxlib.registrate.registry

import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.registries.Registries
import java.util.function.Supplier

class ParticleRegistry(modId: String) {
  private val particleTypes = DeferredRegister.create(modId, Registries.PARTICLE_TYPE)
  private val PARTICLE_REGISTRATIONS = mutableListOf<ParticleRegistration<out ParticleOptions>>()

  class ParticleRegistration<T : ParticleOptions>(
    val type: RegistrySupplier<ParticleType<T>>,
    val provider: (sprite: SpriteSet) -> ParticleProvider<T>
  )

  fun <T : ParticleOptions> particleType(
    name: String, supplier: Supplier<ParticleType<T>>,
    provider: (sprite: SpriteSet) -> ParticleProvider<T>
  ): RegistrySupplier<ParticleType<T>> {
    val type = particleTypes.register(name, supplier)
    PARTICLE_REGISTRATIONS.add(ParticleRegistration(type, provider))
    return type
  }

  fun getParticles(): List<ParticleRegistration<out ParticleOptions>> {
    return PARTICLE_REGISTRATIONS
  }

  fun build() {
    particleTypes.register()
  }
}