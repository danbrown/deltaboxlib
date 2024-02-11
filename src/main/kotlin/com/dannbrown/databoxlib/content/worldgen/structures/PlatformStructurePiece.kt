package com.dannbrown.databoxlib.content.worldgen.structures

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.content.worldgen.ProjectStructureTypes
import com.mojang.serialization.Dynamic
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.*
import net.minecraft.util.RandomSource
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.StructureManager
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.structure.BoundingBox
import net.minecraft.world.level.levelgen.structure.StructurePiece
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext
import java.util.function.Consumer
import javax.annotation.Nonnull

class PlatformStructurePiece : StructurePiece {
  private val positions: MutableSet<BlockPos> = HashSet()
  private val blocks: BlockStateProvider

  constructor(
    positions: Set<BlockPos>,
    blocks: BlockStateProvider,
    bounds: BoundingBox,
    direction: Direction
  ) : super(ProjectStructureTypes.PLATFORM_PIECE.get(), 0, bounds) {
    setOrientation(direction)
    this.positions.addAll(positions!!)
    this.blocks = blocks
  }

  constructor(
    context: StructurePieceSerializationContext,
    tag: CompoundTag
  ) : super(ProjectStructureTypes.PLATFORM_PIECE.get(), tag) {
    val positions = tag.getList("Positions", Tag.TAG_COMPOUND.toInt())
    for (position in positions) {
      this.positions.add(NbtUtils.readBlockPos(position as CompoundTag))
    }
    blocks = BlockStateProvider.CODEC.parse(
      Dynamic(
        NbtOps.INSTANCE,
        tag["Blocks"]
      )
    ).getOrThrow(true, ProjectContent.LOGGER::error)
  }

  override fun addAdditionalSaveData(@Nonnull context: StructurePieceSerializationContext, @Nonnull tag: CompoundTag) {
    val positions = ListTag()
    for (position in this.positions) {
      positions.add(NbtUtils.writeBlockPos(position))
    }
    tag.put("Positions", positions)
    BlockStateProvider.CODEC.encodeStart(
      NbtOps.INSTANCE,
      blocks
    )
    .resultOrPartial(ProjectContent.LOGGER::error).ifPresent { value: Tag ->
      tag.put(
        "Blocks",
        value
      )
    }
  }

  override fun postProcess(
    @Nonnull level: WorldGenLevel,
    @Nonnull manager: StructureManager,
    @Nonnull generator: ChunkGenerator,
    @Nonnull random: RandomSource,
    @Nonnull bounds: BoundingBox,
    @Nonnull chunkPos: ChunkPos,
    @Nonnull blockPos: BlockPos
  ) {
    if (positions.isNotEmpty()) {
      positions.forEach(Consumer { pos: BlockPos ->
        placeBlock(
          level,
          blocks.getState(random, pos),
          pos.x,
          pos.y,
          pos.z,
          bounds
        )
      })
    }
  }

  override fun placeBlock(
    @Nonnull level: WorldGenLevel,
    @Nonnull state: BlockState,
    x: Int,
    y: Int,
    z: Int,
    bounds: BoundingBox
  ) {
    val pos = BlockPos(x, y, z)
    if (bounds.isInside(pos)) {
      if (canBeReplaced(level, x, y, z, bounds)) {
        level.setBlock(pos, state, 2)
      }
    }
  }

  override fun canBeReplaced(level: LevelReader, x: Int, y: Int, z: Int, @Nonnull bounds: BoundingBox): Boolean {
    return true
//    return level.getBlockState(BlockPos(x, y, z)).isAir
  }
}