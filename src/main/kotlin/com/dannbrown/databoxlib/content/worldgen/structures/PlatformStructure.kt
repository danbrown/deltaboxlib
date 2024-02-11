package com.dannbrown.databoxlib.content.worldgen.structures

import com.dannbrown.databoxlib.content.worldgen.ProjectStructureTypes
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.structure.BoundingBox
import net.minecraft.world.level.levelgen.structure.Structure
import net.minecraft.world.level.levelgen.structure.StructureType
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder
import java.util.*
import javax.annotation.Nonnull
import kotlin.math.abs
import kotlin.math.max


class PlatformStructure(structureSettings: StructureSettings, val blocks: BlockStateProvider, val size: Int) :
  Structure(structureSettings) {



  companion object{
//    val CODEC: Codec<BoulderStructure> =
//      RecordCodecBuilder.mapCodec { structureInstance: RecordCodecBuilder.Instance<BoulderStructure> ->
//        structureInstance.group(
//          settingsCodec(
//            structureInstance
//          ), MultiBlockStateConfiguration.CODEC.fieldOf("settings").forGetter { config: BoulderStructure -> config.configuration })
//          .apply(structureInstance, ::BoulderStructure)
//      }.codec()

    val CODEC: Codec<PlatformStructure> = RecordCodecBuilder.create{ structureInstance: RecordCodecBuilder.Instance<PlatformStructure> ->
      structureInstance.group(
        settingsCodec(structureInstance),
        BlockStateProvider.CODEC.fieldOf("blocks")
          .forGetter { structure: PlatformStructure -> structure.blocks },
        Codec.INT.fieldOf("size")
          .forGetter { structure: PlatformStructure -> structure.size }
      ).apply(
        structureInstance, ::PlatformStructure)
    }
     val blocks: BlockStateProvider? = null
     const val size = 0
  }

  @Nonnull
  override fun findGenerationPoint(@Nonnull context: GenerationContext): Optional<GenerationStub> {
    return onTopOfChunkCenter(
      context, Heightmap.Types.MOTION_BLOCKING
    ) { builder: StructurePiecesBuilder ->
      generatePieces(
        builder,
        context,
        this.blocks,
        this.size
      )
    }
  }

  private fun generatePieces(
    builder: StructurePiecesBuilder,
    context: GenerationContext,
    blocks: BlockStateProvider,
    size: Int
  ) {
    val chunks: MutableMap<ChunkPos, MutableSet<BlockPos>> = LinkedHashMap()
    val positions: MutableSet<BlockPos> = LinkedHashSet()
    val random = context.random()
    val direction = random.nextBoolean()

    // this ensures that the spawn is always coming from the world bottom
//    val initialY = context.heightAccessor().minBuildHeight + context.random().nextInt(32)
//    val initialY = context.heightAccessor().height
    val initialY = context.heightAccessor().maxBuildHeight - context.random().nextInt(64)

    var x = context.chunkPos().minBlockX
    var y = initialY
    var z = context.chunkPos().minBlockZ
    val xTendency = random.nextInt(3) - 1
    val zTendency = random.nextInt(3) - 1
    for (amount in 0..63) {
      x += random.nextInt(3) - 1 + xTendency
      y += if (random.nextInt(10) == 0) random.nextInt(3) - 1 else 0
      z += if (direction) random.nextInt(3) - 1 + zTendency else -(random.nextInt(3) - 1 + zTendency)
      for (x1 in x until x + random.nextInt(4) + 3 * size) {
        for (y1 in y until y + random.nextInt(1) + 2) {
          for (z1 in z until z + random.nextInt(4) + 3 * size) {
            val newPosition = BlockPos(x1, y1, z1)
            if (abs((x1 - x).toDouble()) + abs((y1 - y).toDouble()) + abs((z1 - z).toDouble()) < 4 * size + random.nextInt(
                2
              )
            ) {
              positions.add(newPosition)
              chunks.putIfAbsent(ChunkPos(newPosition), HashSet())
            }
          }
        }
      }
    }

    chunks.forEach { (chunkPos: ChunkPos, blockPosSet: MutableSet<BlockPos>) ->
      val withinChunk: MutableSet<BlockPos> = LinkedHashSet(positions)
      withinChunk.removeIf { pos: BlockPos -> ChunkPos(pos) != chunkPos }
      blockPosSet.addAll(withinChunk)
    }

    val finalY = y
    val orientation: Direction = Direction.Plane.HORIZONTAL.getRandomDirection(context.random())
    chunks.forEach { (chunkPos: ChunkPos, blockPosSet: Set<BlockPos>?) ->
      val boundingBox = BoundingBox(
        chunkPos.minBlockX,
        max((initialY - 16).toDouble(), 0.0).toInt(),
        chunkPos.minBlockZ,
        chunkPos.maxBlockX,
        finalY + 16,
        chunkPos.maxBlockZ
      )
      builder.addPiece(PlatformStructurePiece(blockPosSet, blocks, boundingBox, orientation))
    }
  }

  override fun type(): StructureType<*> {
    return ProjectStructureTypes.PLATFORM.get()
  }
}