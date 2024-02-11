package com.dannbrown.databoxlib.content.utils

import com.dannbrown.databoxlib.datagen.planets.ProjectPlanets
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level

object GravityManager {
  const val DEFAULT_GRAVITY = 9.806f
  const val LOW_GRAVITY = 1.625f
  const val HIGH_GRAVITY = 12.0f
  fun getLevelGravity(level: Level): Float {
    val planet = ProjectPlanets.PlanetManager.getPlanetFromLevel(level.dimension())
    val levelGravity = if (planet !== null) planet.gravity else DEFAULT_GRAVITY

    return levelGravity / DEFAULT_GRAVITY
  }

  fun getEntityGravity(entity: Entity): Float {
    return getLevelGravity(entity.level())
  }

  @JvmStatic
  fun applyEntityTickGravity(entity: Entity, force: Double = -0.08) {
    if (!entity.isNoGravity) {
      val velocity = entity.deltaMovement
      val newGravity = force * getEntityGravity(entity)
      entity.setDeltaMovement(velocity.x(), velocity.y() - force + newGravity, velocity.z())
    }
  }
}