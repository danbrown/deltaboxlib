package com.dannbrown.databoxlib.content.worldgen.structures


import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.content.worldgen.ProjectStructureTypes
import com.dannbrown.databoxlib.content.worldgen.featureConfiguration.BoulderConfig
import com.dannbrown.databoxlib.datagen.worldgen.ProjectFeatures
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.DataResult.PartialResult
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap
import net.minecraft.core.BlockPos
import net.minecraft.core.BlockPos.MutableBlockPos
import net.minecraft.nbt.*
import net.minecraft.resources.RegistryOps
import net.minecraft.util.RandomSource
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.StructureManager
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.levelgen.structure.BoundingBox
import net.minecraft.world.level.levelgen.structure.StructurePiece
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext
import kotlin.math.max
import kotlin.math.min


class ArchPiece : StructurePiece {
  private val positions: MutableSet<BlockPos> = HashSet()
  private val config: BoulderConfig

  constructor(positions: Set<BlockPos>, config: BoulderConfig, pGenDepth: Int, generatingBB: BoundingBox) : super(
    ProjectStructureTypes.ARCH_PIECE.get(),
    pGenDepth,
    generatingBB
  ) {
    this.config = config
    this.positions.addAll(positions!!)
  }

  constructor(
    context: StructurePieceSerializationContext,
    tag: CompoundTag
  ) : super(ProjectStructureTypes.ARCH_PIECE.get(), tag) {
    val tagRegistryOps = RegistryOps.create(NbtOps.INSTANCE, context.registryAccess())
    val positions = tag.getList("positions", Tag.TAG_COMPOUND.toInt())
    for (position in positions) {
      this.positions.add(NbtUtils.readBlockPos(position as CompoundTag))
    }
    if (!tag.contains("config")) {
      ProjectContent.LOGGER.error("No arch config info was present.")
    }
    val config1 = BoulderConfig.CODEC.decode(tagRegistryOps, tag["config"])
    config1.error().ifPresent { tagPartialResult: PartialResult<Pair<BoulderConfig, Tag>> ->
      ProjectContent.LOGGER.error(
        "Databox Arch piece config deserialization error: $tagPartialResult"
      )
    }
    config = config1.result().orElseThrow().first
  }

  override fun addAdditionalSaveData(context: StructurePieceSerializationContext, compoundTag: CompoundTag) {
    val positions = ListTag()
    val tagRegistryOps = RegistryOps.create(NbtOps.INSTANCE, context.registryAccess())
    for (position in this.positions) {
      positions.add(NbtUtils.writeBlockPos(position))
    }
    compoundTag.put("positions", positions)
    val encodeStart = BoulderConfig.CODEC.encodeStart(tagRegistryOps, config)
    val error = encodeStart.error()
    error.ifPresent { tagPartialResult: PartialResult<Tag> ->
      ProjectContent.LOGGER.error(
        "Databox Arch piece serialization error: $tagPartialResult"
      )
    }
    compoundTag.put("config", encodeStart.result().orElseThrow())
  }

  override fun postProcess(
    worldGenLevel: WorldGenLevel,
    structureFeatureManager: StructureManager,
    chunkGenerator: ChunkGenerator,
    random: RandomSource,
    boundingBox: BoundingBox,
    chunkPos: ChunkPos,
    blockPos: BlockPos
  ) {
    val toPlace = Long2ObjectLinkedOpenHashMap<BlockState>(1000)
    for (p in positions) {
      var position = p
      if (position.y == Int.MIN_VALUE) {
        position = BlockPos(
          position.x,
          worldGenLevel.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, position.x, position.z) + 1,
          position.z
        )
      }
      if (!boundingBox.isInside(position)) {
        continue
      }
      if (DEBUG) {
        worldGenLevel.setBlock(position, Blocks.GLOWSTONE.defaultBlockState(), 2)
      } else {
        ProjectFeatures.BOULDER.get().fillList(toPlace, worldGenLevel, worldGenLevel.seed, random, position, config)
      }
    }
    if (toPlace.isEmpty()) {
      return
    }
    val min = MutableBlockPos().set(Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE)
    val max = MutableBlockPos().set(Int.MIN_VALUE, Int.MIN_VALUE, Int.MIN_VALUE)
    toPlace.forEach { (aLong: Long?, state: BlockState?) ->
      val pos = BlockPos.of(aLong!!)
      min[min(min.x.toDouble(), pos.x.toDouble()), min(min.y.toDouble(), pos.y.toDouble())] =
        min(min.z.toDouble(), pos.z.toDouble())
      max[max(max.x.toDouble(), pos.x.toDouble()), max(max.y.toDouble(), pos.y.toDouble())] =
        max(max.z.toDouble(), pos.z.toDouble())
      worldGenLevel.setBlock(pos, state, 2)
    }
    for (aLong in toPlace.keys) {
      for (spawningFeature in config.spawningFeatures) {
        spawningFeature.value().place(worldGenLevel, chunkGenerator, random, BlockPos.of(aLong))
      }
    }
    val minX: Int = min.x - ArchStructure.PIECE_BB_EXPANSION
    val minY: Int = min.y - ArchStructure.PIECE_BB_EXPANSION
    val minZ: Int = min.z - ArchStructure.PIECE_BB_EXPANSION
    val maxX: Int = max.x + ArchStructure.PIECE_BB_EXPANSION
    val maxY: Int = max.y + ArchStructure.PIECE_BB_EXPANSION
    val maxZ: Int = max.z + ArchStructure.PIECE_BB_EXPANSION
    this.boundingBox = BoundingBox(minX, minY, minZ, maxX, maxY, maxZ)
  }

  companion object {
    const val DEBUG = false
  }
}