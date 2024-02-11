package com.dannbrown.databoxlib.content.ship

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerChunkCache
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Clearable
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.chunk.LevelChunk
import org.joml.Vector2i
import org.joml.Vector3d
import org.joml.Vector3i
import org.valkyrienskies.core.api.ships.LoadedServerShip
import org.valkyrienskies.core.api.ships.LoadedShip
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.api.ships.Ship
import org.valkyrienskies.core.api.world.ShipWorld
import org.valkyrienskies.core.apigame.world.ServerShipWorldCore
import org.valkyrienskies.core.apigame.world.properties.DimensionId
import org.valkyrienskies.core.impl.game.ships.ShipData
import org.valkyrienskies.core.impl.game.ships.ShipTransformImpl
import org.valkyrienskies.core.impl.hooks.VSEvents
import org.valkyrienskies.core.impl.networking.simple.sendToClient
import org.valkyrienskies.core.util.datastructures.DenseBlockPosSet
import org.valkyrienskies.mod.common.IShipObjectWorldServerProvider
import org.valkyrienskies.mod.common.ValkyrienSkiesMod
import org.valkyrienskies.mod.common.networking.PacketRestartChunkUpdates
import org.valkyrienskies.mod.common.networking.PacketStopChunkUpdates
import org.valkyrienskies.mod.common.util.DimensionIdProvider
import org.valkyrienskies.mod.common.util.MinecraftPlayer
import org.valkyrienskies.mod.mixinducks.world.entity.PlayerDuck

object VSUtils {
  // Level
  fun getDimensionId(level: Level): DimensionId {
    return (level as DimensionIdProvider).dimensionId
  }

  private fun getShipObjectManagingPosImpl(world: ServerLevel?, chunkX: Int, chunkZ: Int): LoadedShip? {
    if (world != null && world is ShipWorld) {
      val shipWorld = world as ShipWorld
      val worldDimensionId: DimensionId = getDimensionId(world)
      if (shipWorld.isChunkInShipyard(chunkX, chunkZ, worldDimensionId)) {
        val ship = shipWorld.allShips.getByChunkPos(chunkX, chunkZ, worldDimensionId)
        if (ship != null) {
          return shipWorld.loadedShips.getById(ship.id)
        }
      }
    }
    return null
  }

  private fun getShipManagingPosImpl(world: ServerLevel?, chunkX: Int, chunkZ: Int): Ship? {
    if (world != null) {
      val shipWorld = world as ShipWorld
      val worldDimensionId: DimensionId = getDimensionId(world)
      if (shipWorld.isChunkInShipyard(chunkX, chunkZ, worldDimensionId)) {
        val ship = shipWorld.allShips.getByChunkPos(chunkX, chunkZ, worldDimensionId)
        if (ship != null && ship.chunkClaimDimension == worldDimensionId) {
          return ship
        }
      }
    }
    return null
  }

  fun getShipObjectManagingPos(world: ServerLevel?, blockPos: BlockPos): LoadedShip? {
    return getShipObjectManagingPosImpl(world, blockPos.x shr 4, blockPos.z shr 4)
  }

  fun getShipManagingPos(world: ServerLevel?, blockPos: BlockPos): Ship? {
    return getShipManagingPosImpl(world, blockPos.x shr 4, blockPos.z shr 4)
  }

  fun getShipObject(world: ServerLevel?, blockPos: BlockPos): LoadedServerShip? {
    return getShipObjectManagingPos(world, blockPos) as LoadedServerShip? ?: getShipManagingPos(world, blockPos) as LoadedServerShip?
  }

  fun isTickingChunk(world: ServerLevel?, chunkX: Int, chunkZ: Int): Boolean {
    if (world == null) return false
    return (world.chunkSource as ServerChunkCache).isPositionTicking(ChunkPos.asLong(chunkX, chunkZ))
  }

  fun isTickingChunk(world: ServerLevel?, chunkPos: ChunkPos): Boolean {
    return isTickingChunk(world, chunkPos.x, chunkPos.z)
  }

  // Player
  fun getPlayerWrapper(player: Player): MinecraftPlayer {
    return (player as PlayerDuck).vs_getPlayer()
  }

  // MinecraftServer
  fun getServerShipObjectWorld(server: MinecraftServer): ServerShipWorldCore {
    return (server as IShipObjectWorldServerProvider).shipObjectWorld ?: ValkyrienSkiesMod.vsCore.dummyShipWorldServer
  }

  fun executeIf(server: MinecraftServer, condition: () -> Boolean, toExecute: Runnable) {
    // todo: don't use random vs-core internal stuff
    val serverWorld = getServerShipObjectWorld(server)
    VSEvents.TickEndEvent.on { (shipWorld), handler ->
      if (shipWorld == serverWorld && condition()) {
        toExecute.run()
        handler.unregister()
      }
    }
  }

  object RelocationUtil {
    private var AIR: BlockState = Blocks.AIR.defaultBlockState()

    /**
     * Relocate block
     *
     * @param fromChunk
     * @param from coordinate (can be local or global coord)
     * @param toChunk
     * @param to coordinate (can be local or global coord)
     * @param toShip should be set when you're relocating to a ship
     * @param rotation Rotation.NONE is no change in direction, Rotation.CLOCKWISE_90 is 90 degrees clockwise, etc.
     */
    fun relocateBlock(
      fromChunk: LevelChunk, from: BlockPos, toChunk: LevelChunk, to: BlockPos, doUpdate: Boolean, toShip: ServerShip?, rotation: Rotation? = Rotation.NONE, facing: Direction? = null
    ) {
      var state = fromChunk.getBlockState(from)
      val entity = fromChunk.getBlockEntity(from)
      val tag = entity?.let {
        val tag = it.saveWithFullMetadata()
        tag.putInt("x", to.x)
        tag.putInt("y", to.y)
        tag.putInt("z", to.z)
        // so that it won't drop its contents
        if (it is Clearable) {
          it.clearContent()
        }
        // so loot containers dont drop its content
        if (it is RandomizableContainerBlockEntity) {
          it.setLootTable(null, 0)
        }

        tag
      }
      val level = toChunk.level
      if (rotation !== null) {
        state = state.rotate(level, to, rotation)
      }
//      else if (facing !== null) {
//        state = state.setValue(DirectionalBlock.FACING, facing)
//      }
      fromChunk.setBlockState(from, AIR, false)
      toChunk.setBlockState(to, state, false)

      if (doUpdate) {
        updateBlock(level, from, to, state)
      }

      tag?.let {
        val be = level.getBlockEntity(to)!!

        be.load(it)
      }
    }

    /**
     * Update block after relocate
     *
     * @param level
     * @param fromPos old position coordinate
     * @param toPos new position coordinate
     * @param toState new blockstate at toPos
     */
    fun updateBlock(level: Level, fromPos: BlockPos, toPos: BlockPos, toState: BlockState) {
      // 75 = flag 1 (block update) & flag 2 (send to clients) + flag 8 (force rerenders)
      val flags = 11
      //updateNeighbourShapes recurses through nearby blocks, recursionLeft is the limit
      val recursionLeft = 511

      level.setBlocksDirty(fromPos, toState, AIR)
      level.sendBlockUpdated(fromPos, toState, AIR, flags)
      level.blockUpdated(fromPos, AIR.block)
      // This handles the update for neighboring blocks in worldspace
      AIR.updateIndirectNeighbourShapes(level, fromPos, flags, recursionLeft - 1)
      AIR.updateNeighbourShapes(level, fromPos, flags, recursionLeft)
      AIR.updateIndirectNeighbourShapes(level, fromPos, flags, recursionLeft)
      //This updates lighting for blocks in worldspace
      level.chunkSource.lightEngine.checkBlock(fromPos)

      level.setBlocksDirty(toPos, AIR, toState)
      level.sendBlockUpdated(toPos, AIR, toState, flags)
      level.blockUpdated(toPos, toState.block)
      if (!level.isClientSide && toState.hasAnalogOutputSignal()) {
        level.updateNeighbourForOutputSignal(toPos, toState.block)
      }
      //This updates lighting for blocks in shipspace
      level.chunkSource.lightEngine.checkBlock(toPos)
    }

    /**
     * Relocate block
     *
     * @param level level
     * @param from coordinate (can be local or global coord)
     * @param to coordinate (can be local or global coord)
     * @param doUpdate update blocks after moving
     * @param toShip should be set when you're relocating to a ship
     * @param rotation Rotation.NONE is no change in direction, Rotation.CLOCKWISE_90 is 90 degrees clockwise, etc.
     */
    fun relocateBlock(level: Level, from: BlockPos, to: BlockPos, doUpdate: Boolean, toShip: ServerShip?, rotation: Rotation?, facing: Direction?) =
      relocateBlock(level.getChunkAt(from), from, level.getChunkAt(to), to, doUpdate, toShip, rotation, facing)
  }

  object ShipAssembly {
    fun createNewShipWithBlocks(
      centerBlock: BlockPos, blocks: DenseBlockPosSet, level: ServerLevel, scale: Double = 1.0
    ): ServerShip {
      if (blocks.isEmpty()) throw IllegalArgumentException()
      val shipObjectWorld = getServerShipObjectWorld(level.server)
      val centerBlockVector3i = Vector3i(centerBlock.x, centerBlock.y, centerBlock.z)
      val ship = shipObjectWorld.createNewShipAtBlock(centerBlockVector3i, false, scale, getDimensionId(level))
      val shipChunkX = ship.chunkClaim.xMiddle
      val shipChunkZ = ship.chunkClaim.zMiddle
      val worldChunkX = centerBlock.x shr 4
      val worldChunkZ = centerBlock.z shr 4
      val deltaX = worldChunkX - shipChunkX
      val deltaZ = worldChunkZ - shipChunkZ
      val chunksToBeUpdated = mutableMapOf<ChunkPos, Pair<ChunkPos, ChunkPos>>()
      blocks.forEachChunk { x, _, z, _ ->
        val sourcePos = ChunkPos(x, z)
        val destPos = ChunkPos(x - deltaX, z - deltaZ)
        chunksToBeUpdated[sourcePos] = Pair(sourcePos, destPos)
      }
      val chunkPairs = chunksToBeUpdated.values.toList()
      val chunkPoses = chunkPairs.flatMap { it.toList() }
      val chunkPosesJOML = chunkPoses.map { it -> Vector2i(it.x, it.z) }
      // Send a list of all the chunks that we plan on updating to players, so that they
      // defer all updates until assembly is finished
      level.players()
        .forEach { player ->
          PacketStopChunkUpdates(chunkPosesJOML).sendToClient(getPlayerWrapper(player))
        }
      // Use relocateBlock to copy all the blocks into the ship
      blocks.forEachChunk { chunkX, chunkY, chunkZ, chunk ->
        val sourceChunk = level.getChunk(chunkX, chunkZ)
        val destChunk = level.getChunk(chunkX - deltaX, chunkZ - deltaZ)

        chunk.forEach { x, y, z ->
          val fromPos = BlockPos((sourceChunk.pos.x shl 4) + x, (chunkY shl 4) + y, (sourceChunk.pos.z shl 4) + z)
          val toPos = BlockPos((destChunk.pos.x shl 4) + x, (chunkY shl 4) + y, (destChunk.pos.z shl 4) + z)

          RelocationUtil.relocateBlock(sourceChunk, fromPos, destChunk, toPos, false, ship, Rotation.NONE, Direction.NORTH)
        }
      }
      // Use updateBlock to update blocks after copying
      blocks.forEachChunk { chunkX, chunkY, chunkZ, chunk ->
        val sourceChunk = level.getChunk(chunkX, chunkZ)
        val destChunk = level.getChunk(chunkX - deltaX, chunkZ - deltaZ)

        chunk.forEach { x, y, z ->
          val fromPos = BlockPos((sourceChunk.pos.x shl 4) + x, (chunkY shl 4) + y, (sourceChunk.pos.z shl 4) + z)
          val toPos = BlockPos((destChunk.pos.x shl 4) + x, (chunkY shl 4) + y, (destChunk.pos.z shl 4) + z)

          RelocationUtil.updateBlock(destChunk.level, fromPos, toPos, destChunk.getBlockState(toPos))
        }
      }
      // Calculate the position of the block that the player clicked after it has been assembled
      val centerInShip = Vector3d(
        ((shipChunkX shl 4) + (centerBlock.x and 15)).toDouble(),
        centerBlock.y.toDouble(),
        ((shipChunkZ shl 4) + (centerBlock.z and 15)).toDouble()
      )
      // The ship's position has shifted from the center block since we assembled the ship, compensate for that
      val centerBlockPosInWorld = ship.inertiaData.centerOfMassInShip.sub(centerInShip, Vector3d())
        .add(ship.transform.positionInWorld)
      // Put the ship into the compensated position, so that all the assembled blocks stay in the same place
      // TODO: AAAAAAAAA THIS IS HORRIBLE how can the API support this?
      (ship as ShipData).transform = (ship.transform as ShipTransformImpl).copy(positionInWorld = centerBlockPosInWorld)
      // This condition will return true if all modified chunks have been both loaded AND
      // chunk update packets were sent to players
      executeIf(level.server, { chunkPoses.all { chunkPos -> isTickingChunk(level, chunkPos) } }, {
        // Once all the chunk updates are sent to players, we can tell them to restart chunk updates
        level.players()
          .forEach { player ->
            PacketRestartChunkUpdates(chunkPosesJOML).sendToClient(getPlayerWrapper(player))
          }
      })


      return ship
    }
  }
}