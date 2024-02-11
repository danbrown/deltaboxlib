package com.dannbrown.databoxlib.content.ship.block

import com.dannbrown.databoxlib.content.ship.SpaceShipControl
import com.dannbrown.databoxlib.content.ship.extensions.getShipManagingPos
import com.dannbrown.databoxlib.content.ship.extensions.getShipObjectManagingPos
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import org.valkyrienskies.core.api.ships.getAttachment

class AntimatterBlock(properties: Properties) : Block(properties) {
  override fun fallOn(level: Level, state: BlockState, blockPos: BlockPos, entity: Entity, f: Float) {
    entity.causeFallDamage(f,
      0.2f,
      entity.damageSources()
        .fall())
  }

  override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, isMoving: Boolean) {
    super.onPlace(state, level, pos, oldState, isMoving)

    if (level.isClientSide) return
    level as ServerLevel
    val ship = level.getShipObjectManagingPos(pos) ?: level.getShipManagingPos(pos) ?: return
    val shipControl = SpaceShipControl.getOrCreate(ship)
    shipControl.addAntimatter(pos)
  }

  override fun destroy(level: LevelAccessor, pos: BlockPos, state: BlockState) {
    super.destroy(level, pos, state)

    if (level.isClientSide) return
    level as ServerLevel
    val ship = level.getShipObjectManagingPos(pos) ?: level.getShipManagingPos(pos) ?: return
    val shipControl = ship.getAttachment<SpaceShipControl>() ?: return
    shipControl.removeAntimatter(pos)
  }
//  override fun onRemove(pState: BlockState, pLevel: Level, pPos: BlockPos, pNewState: BlockState, pMovedByPiston: Boolean) {
//    super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston)
//
//    if (pLevel.isClientSide) return
//    pLevel as ServerLevel
//    val ship = pLevel.getShipObjectManagingPos(pPos) ?: pLevel.getShipManagingPos(pPos) ?: return
//    val shipControl = ship.getAttachment<DataboxShipControl>()
//    shipControl?.removeAntimatter()
//  }
//  override fun onProjectileHit(level: Level, state: BlockState, hit: BlockHitResult, projectile: Projectile) {
//    if (level.isClientSide) return
//
//    level.destroyBlock(hit.blockPos, false)
//    Direction.values()
//      .forEach {
//        val neighbor = hit.blockPos.relative(it)
//        if (level.getBlockState(neighbor).block == this &&
//          level.random.nextFloat() < DataboxConfig.SERVER.popSideBalloonChance
//        ) {
//          level.destroyBlock(neighbor, false)
//        }
//      }
//  }
}
