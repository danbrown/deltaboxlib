package com.dannbrown.databoxlib.content.utils

import com.dannbrown.databoxlib.content.item.GeckoArmor.SpaceHelmetItem
import com.dannbrown.databoxlib.datagen.planets.ProjectPlanets.PlanetManager
import com.dannbrown.databoxlib.datagen.planets.PlanetCodec
import com.dannbrown.databoxlib.init.ProjectTags
import com.simibubi.create.content.equipment.armor.BacktankUtil
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceKey
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

object OxygenManager {
  val OXYGEN_LOCATIONS: MutableMap<Pair<ResourceKey<Level>, BlockPos>, Set<BlockPos>> =
    HashMap<Pair<ResourceKey<Level>, BlockPos>, Set<BlockPos>>()

  fun addOxygenLocation(level: ResourceKey<Level>, pos: BlockPos, oxygenLocations: Set<BlockPos>) {
    OXYGEN_LOCATIONS[Pair(level, pos)] = oxygenLocations
  }

  fun getOxygenLocations(level: ResourceKey<Level>, pos: BlockPos): Set<BlockPos>? {
    return OXYGEN_LOCATIONS[Pair(level, pos)]
  }

  fun removeOxygenLocation(level: ResourceKey<Level>, pos: BlockPos) {
    OXYGEN_LOCATIONS.remove(Pair(level, pos))
  }

  fun clearOxygenLocations() {
    OXYGEN_LOCATIONS.clear()
  }

  fun levelHasOxygen(level: Level): Boolean {
    val planet = PlanetManager.getPlanetFromLevel(level.dimension())
    if (planet !== null) {
      return planet.atmosphere === PlanetCodec.AtmosphereType.OXYGEN // has a planet data, other atmospheres will return false
    }
    // if no planet data, return oxygen by default
    return true
  }

  // EQUIPMENT
  @JvmStatic
  fun spaceBreathing(entity: LivingEntity, world: Level) {
    val second = world.gameTime % 20 == 0L
    val drowning = entity.airSupply == 0
    val isInWater = entity.isInWater
    val levelHasOxygen = levelHasOxygen(world)
    val helmet = SpaceHelmetItem.getWornItem(entity)
    val backtanks = BacktankUtil.getAllWithAir(entity)
    val isPlayer = entity is Player
    val player = if (isPlayer) entity as Player else null
    // performs all oxygen checks on the server side
    if (!world.isClientSide) {
      // if the entity is a player, and they are in creative mode, they don't need oxygen
      if (isPlayer && player!!.isCreative) return
      // if is in water, have a backtank and is drowning, set air supply to 10 and consume air from backtanks
      if (isInWater && backtanks.isNotEmpty()) {
        if (drowning) entity.airSupply = 10
        if (!second) return
        BacktankUtil.consumeAir(entity, backtanks[0], 1f)
        entity.addEffect(MobEffectInstance(MobEffects.WATER_BREATHING, 30, 0, true, false, true))
        return
      }
      // level has no oxygen, check breathability
      if (!levelHasOxygen) {
        // by default all entities can't breathe in space, unless they have the tag
        // log the entity id and tags to the console
        println("Entity: ${entity.type.descriptionId} Tags: ${entity.type.tags}")
        if (entity.type.tags.anyMatch { tag -> ProjectTags.ENTITY.ATMOSPHERE_IMMUNE == tag }) return
        // the helmet or backtanks are empty, so it can't breathe
        if (helmet.isEmpty || backtanks.isEmpty()) {
          val levelDamageSources = world.damageSources()
          entity.hurt(levelDamageSources.drown(), 1.0f)
          // set the player to drown
          entity.airSupply = -10
        }
        // the helmet is not empty, breathe from the helmet
        else {
          if (!second) return
          BacktankUtil.consumeAir(entity, backtanks[0], 1f)
          entity.airSupply = Math.min(entity.maxAirSupply.toDouble(), (entity.airSupply + 10).toDouble())
            .toInt()
        }
      }
      // level has oxygen, just return
      else {
        return
      }
    }
    // the world is client side, perform player gui render here
  }
}