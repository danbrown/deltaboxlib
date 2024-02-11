package com.dannbrown.databoxlib.content.worldgen.structures

import com.dannbrown.databoxlib.content.core.utils.BlendingFunction
import com.dannbrown.databoxlib.content.worldgen.ProjectStructureTypes
import com.dannbrown.databoxlib.content.worldgen.featureConfiguration.BoulderConfig
import com.dannbrown.databoxlib.datagen.worldgen.ProjectConfiguredFeatures
import com.dannbrown.databoxlib.init.ProjectBlocks
import com.dannbrown.databoxlib.init.ProjectTags
import com.mojang.serialization.codecs.RecordCodecBuilder
import it.unimi.dsi.fastutil.longs.Long2ObjectFunction
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import net.minecraft.Util
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderGetter
import net.minecraft.core.QuartPos
import net.minecraft.core.SectionPos
import net.minecraft.data.worldgen.features.CaveFeatures
import net.minecraft.tags.TagKey
import net.minecraft.util.Mth
import net.minecraft.util.random.SimpleWeightedRandomList
import net.minecraft.util.valueproviders.ConstantFloat
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.LevelHeightAccessor
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.levelgen.RandomState
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider
import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement
import net.minecraft.world.level.levelgen.placement.RarityFilter
import net.minecraft.world.level.levelgen.structure.BoundingBox
import net.minecraft.world.level.levelgen.structure.Structure
import net.minecraft.world.level.levelgen.structure.StructureType
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder
import java.util.*
import java.util.Map
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.math.cos
import kotlin.math.sin

class ArchStructure(structureSettings: StructureSettings, archConfiguration: ArchConfiguration) :
  Structure(structureSettings) {
  private val archConfiguration: ArchConfiguration

  init {
    this.archConfiguration = archConfiguration
  }

  public override fun findGenerationPoint(generationContext: GenerationContext): Optional<GenerationStub> {
    return onTopOfChunkCenter(
      generationContext, Heightmap.Types.WORLD_SURFACE_WG
    ) { piecesBuilder: StructurePiecesBuilder ->
      generatePieces(
        piecesBuilder,
        generationContext,
        archConfiguration
      )
    }
  }

  override fun type(): StructureType<*> {
    return ProjectStructureTypes.ARCH.get()
  }

  companion object {
    val CODEC =
      RecordCodecBuilder.mapCodec { archStructureInstance: RecordCodecBuilder.Instance<ArchStructure> ->
        archStructureInstance.group(
          settingsCodec(
            archStructureInstance
          ),
          ArchConfiguration.CODEC.fieldOf("settings")
            .forGetter { p_227656_ -> p_227656_.archConfiguration })
          .apply(
            archStructureInstance
          ) { structureSettings, archConfiguration ->
            ArchStructure(
              structureSettings,
              archConfiguration
            )
          }
      }
        .codec()
    const val PIECE_BB_EXPANSION = 5
    private fun generatePieces(
      piecesBuilder: StructurePiecesBuilder,
      context: GenerationContext,
      config: ArchConfiguration
    ) {
      val random = context.random()
      val randomState = context.randomState()
      val fullRange = Math.PI * 2
      val ninetyDegrees = fullRange / 4.0
      val angle = random.nextDouble() * fullRange
      val chunkPos = context.chunkPos()
      val blockX = chunkPos.getBlockX(random.nextInt(16))
      val blockZ = chunkPos.getBlockZ(random.nextInt(16))
      val generator = context.chunkGenerator()
      val length: Int = config.length.sample(random) / 2
      val archHeight: Int = config.height.sample(random)
      val center = BlockPos(
        blockX,
        generator.getBaseHeight(
          blockX,
          blockZ,
          Heightmap.Types.OCEAN_FLOOR_WG,
          context.heightAccessor(),
          randomState
        ) + archHeight,
        blockZ
      )
      val xOffset = sin(angle) * length
      val zOffset = cos(angle) * length
      val chunkSortedPositions = Long2ObjectOpenHashMap<MutableSet<BlockPos>>()
      val percentageDestroyed: Float = 1.0f - config.percentageDestroyed.sample(random)
      val percentageDestroyed2: Float = 1.0f - config.percentageDestroyed.sample(random)
      run {
        var start = center.offset(-xOffset.toInt(), 0, -zOffset.toInt())
        start = BlockPos(
          start.x,
          generator.getBaseHeight(
            start.x,
            start.z,
            Heightmap.Types.OCEAN_FLOOR_WG,
            context.heightAccessor(),
            randomState
          ) - 5,
          start.z
        )
        var end = center.offset(xOffset.toInt(), 0, zOffset.toInt())
        end = BlockPos(
          end.x,
          generator.getBaseHeight(
            end.x,
            end.z,
            Heightmap.Types.OCEAN_FLOOR_WG,
            context.heightAccessor(),
            randomState
          ) - 5,
          end.z
        )
        val points = 1000
        if (config.biomeEnforcement !== ArchConfiguration.EMPTY) {
          if (!matchesBiome(
              start,
              generator,
              config.biomeEnforcement,
              randomState
            ) || !matchesBiome(end, generator, config.biomeEnforcement, randomState)
          ) {
            return
          }
        }
        val blendingFunction: BlendingFunction =
          config.blendingFunction.getRandomValue(random)
            .orElseThrow()
        val blendingFunction2 =
          if (random.nextFloat() < config.matchingBlendingFunctionChance
              .sample(random)
          ) blendingFunction
          else config.blendingFunction.getRandomValue(random)
            .orElseThrow()
        var startToCenterLastPos: BlockPos? = null
        var endToCenterLastPos: BlockPos? = null
        for (pointCount in points downTo 1) {
          val factor = pointCount.toDouble() / points
          val squareDistance = 2
          run {
            var startToCenterLerpPos = BlockPos.containing(
              Mth.lerp(factor, start.x.toDouble(), center.x.toDouble()),
              BlendingFunction.applyFunction(blendingFunction, factor, start.y.toDouble(), center.y.toDouble()),
              Mth.lerp(factor, start.z.toDouble(), center.z.toDouble())
            )
            if (startToCenterLastPos == null || startToCenterLastPos!!.distSqr(startToCenterLerpPos) > squareDistance) {
              if (factor > percentageDestroyed) {
                startToCenterLerpPos = BlockPos(
                  startToCenterLerpPos.x,
                  Int.MIN_VALUE,
                  startToCenterLerpPos.z
                )
              }
              else {
                startToCenterLastPos = startToCenterLerpPos
              }
              val chunkKey = ChunkPos.asLong(
                SectionPos.blockToSectionCoord(startToCenterLerpPos.x),
                SectionPos.blockToSectionCoord(startToCenterLerpPos.z)
              )
              chunkSortedPositions.computeIfAbsent(
                chunkKey,
                Long2ObjectFunction<MutableSet<BlockPos>> { key: Long -> HashSet() }
              )
                .add(startToCenterLerpPos)
            }
          }
          run {
            var centerToEndLerpPos = BlockPos.containing(
              Mth.lerp(factor, end.x.toDouble(), center.x.toDouble()),
              BlendingFunction.applyFunction(blendingFunction2, factor, end.y.toDouble(), center.y.toDouble()),
              Mth.lerp(factor, end.z.toDouble(), center.z.toDouble())
            )
            if (endToCenterLastPos == null || endToCenterLastPos!!.distSqr(centerToEndLerpPos) > squareDistance) {
              if (factor > percentageDestroyed2) {
                centerToEndLerpPos =
                  BlockPos(centerToEndLerpPos.x, Int.MIN_VALUE, centerToEndLerpPos.z)
              }
              else {
                endToCenterLastPos = centerToEndLerpPos
              }
              val centerToEndChunkKey = ChunkPos.asLong(
                SectionPos.blockToSectionCoord(centerToEndLerpPos.x),
                SectionPos.blockToSectionCoord(centerToEndLerpPos.z)
              )
              chunkSortedPositions.computeIfAbsent(
                centerToEndChunkKey,
                Long2ObjectFunction<MutableSet<BlockPos>> { key: Long -> HashSet() }
              )
                .add(centerToEndLerpPos)
            }
          }
        }
      }
      run {
        val width: Int = config.width.sample(random)
        val totalThicknessPoints = width.toDouble() / 3
        val newSortedPositions: Long2ObjectOpenHashMap<MutableSet<BlockPos>>
        if (totalThicknessPoints > 1) {
          newSortedPositions = Long2ObjectOpenHashMap(chunkSortedPositions.size * totalThicknessPoints.toInt())
          val capture = chunkSortedPositions.values
          val wideXOffset = sin(angle + ninetyDegrees)
          val wideZOffset = cos(angle + ninetyDegrees)
          val widthXOffset = wideXOffset * width
          val widthZOffset = wideZOffset * width
          for (value in capture) {
            for (pos in value) {
              val start = pos.offset(-widthXOffset.toInt(), 0, -widthZOffset.toInt())
              val end = pos.offset(widthXOffset.toInt(), 0, widthZOffset.toInt())
              for (thickness in totalThicknessPoints.toInt() downTo 1) {
                val factor = thickness.toDouble() / totalThicknessPoints
                run {
                  var startToCenterLerpPos = BlockPos.containing(
                    Mth.lerp(factor, start.x.toDouble(), pos.x.toDouble()),
                    pos.y.toDouble(),
                    Mth.lerp(factor, start.z.toDouble(), pos.z.toDouble())
                  )
                  startToCenterLerpPos =
                    BlockPos(startToCenterLerpPos.x, pos.y, startToCenterLerpPos.z)
                  val chunkKey = ChunkPos.asLong(
                    SectionPos.blockToSectionCoord(startToCenterLerpPos.x),
                    SectionPos.blockToSectionCoord(startToCenterLerpPos.z)
                  )
                  newSortedPositions.computeIfAbsent(
                    chunkKey,
                    Long2ObjectFunction<MutableSet<BlockPos>> { key: Long -> HashSet() }
                  )
                    .add(startToCenterLerpPos)
                }
                run {
                  var centerToEndLerpPos = BlockPos.containing(
                    Mth.lerp(factor, end.x.toDouble(), pos.x.toDouble()),
                    pos.y.toDouble(),
                    Mth.lerp(factor, end.z.toDouble(), pos.z.toDouble())
                  )
                  centerToEndLerpPos =
                    BlockPos(centerToEndLerpPos.x, pos.y, centerToEndLerpPos.z)
                  val centerToEndChunkKey = ChunkPos.asLong(
                    SectionPos.blockToSectionCoord(centerToEndLerpPos.x),
                    SectionPos.blockToSectionCoord(centerToEndLerpPos.z)
                  )
                  newSortedPositions.computeIfAbsent(
                    centerToEndChunkKey,
                    Long2ObjectFunction<MutableSet<BlockPos>> { key: Long -> HashSet() }
                  )
                    .add(centerToEndLerpPos)
                }
              }
            }
          }
        }
        else {
          newSortedPositions = chunkSortedPositions
        }
        newSortedPositions.forEach { (offsetChunkPos: Long?, set: Set<BlockPos>?) ->
          val movingChunkPos = ChunkPos(
            offsetChunkPos!!
          )
          piecesBuilder.addPiece(
            ArchPiece(
              set,
              config.sphereConfig,
              0,
              getWritableArea(movingChunkPos, context.heightAccessor())
            )
          )
        }
      }
    }

    fun getWritableArea(chunkPos: ChunkPos, accessor: LevelHeightAccessor): BoundingBox {
      val i = chunkPos.minBlockX
      val j = chunkPos.minBlockZ
      val k = accessor.minBuildHeight + 1
      val l = accessor.maxBuildHeight - 1
      return BoundingBox(i, k, j, i + 15, l, j + 15)
    }

    private fun matchesBiome(
      pos: BlockPos,
      generator: ChunkGenerator,
      biomeTagKey: TagKey<Biome>,
      randomState: RandomState
    ): Boolean {
      return generator.biomeSource.getNoiseBiome(
        QuartPos.fromBlock(pos.x),
        QuartPos.fromBlock(pos.y),
        QuartPos.fromBlock(pos.z),
        randomState.sampler()
      )
        .`is`(biomeTagKey)
    }

    // preset structures
    class PRESETS(biomes: HolderGetter<Biome>, lookup: HolderGetter<ConfiguredFeature<*, *>>) {
      val LUSH_RED_ROCK_ARCH_PRESET = ArchStructure(
        StructureSettings(
          biomes.getOrThrow(ProjectTags.BIOME.HAS_LUSH_RED_ARCHES),
          Map.of(),
          GenerationStep.Decoration.RAW_GENERATION,
          TerrainAdjustment.NONE
        ),
        Util.make(ArchConfiguration.Builder()) { builder ->
          val blockProvider = WeightedStateProvider(
            SimpleWeightedRandomList.builder<BlockState>()
              .add(Blocks.TERRACOTTA.defaultBlockState(), 6)
              .add(Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 4)
              .add(Blocks.SMOOTH_RED_SANDSTONE.defaultBlockState(), 3)
              .add(Blocks.RED_SANDSTONE.defaultBlockState(), 3)
              .add(Blocks.GRANITE.defaultBlockState(), 3)
              .add(ProjectBlocks.RED_SANDSTONE_PEBBLES.get()
                .defaultBlockState(), 1)
          )

          builder.withSphereConfig(
            BoulderConfig.Builder()
              .setRadiusSettings(
                BoulderConfig.RadiusSettings(
                  UniformInt.of(5, 10),
                  UniformInt.of(5, 10), 0,
                  UniformInt.of(5, 10)
                )
              )
              .setBlockProvider(blockProvider)
              .setMiddleBlockProvider(blockProvider)
              .setTopBlockProvider(blockProvider)
              .withNoiseFrequency(0.1f)
              .withSpawningFeatures(
                listOf(
                  ProjectConfiguredFeatures.directPlacedFeature(
                    lookup.getOrThrow(CaveFeatures.GLOW_LICHEN),
                    RarityFilter.onAverageOnceEvery(50)
                  ),
                  ProjectConfiguredFeatures.directPlacedFeature(
                    lookup.getOrThrow(CaveFeatures.MOSS_PATCH),
                    RarityFilter.onAverageOnceEvery(10)
                  ),
                  ProjectConfiguredFeatures.directPlacedFeature(
                    lookup.getOrThrow(CaveFeatures.MOSS_PATCH_CEILING),
                    RarityFilter.onAverageOnceEvery(30),
                    RandomOffsetPlacement.vertical(UniformInt.of(-15, -10))
                  ),
                )
              )
              .build()
          )

          builder.withMatchingBlendingFunctionChance(ConstantFloat.of(0.2f))
          builder.withPercentageDestroyed(ConstantFloat.of(0f))
          builder.withBlendingFunctionType(
            SimpleWeightedRandomList.builder<BlendingFunction>()
              .add(BlendingFunction.EASE_OUT_CUBIC, 16)
              .add(BlendingFunction.EAST_IN_OUT_CIRC, 8)
              .add(BlendingFunction.EASE_OUT_BOUNCE, 1)
              .build()
          )
        }
          .build()
      )
      val RED_ROCK_ARCH_PRESET = ArchStructure(
        StructureSettings(
          biomes.getOrThrow(ProjectTags.BIOME.HAS_RED_ARCHES),
          Map.of(),
          GenerationStep.Decoration.RAW_GENERATION,
          TerrainAdjustment.NONE
        ),
        Util.make(ArchConfiguration.Builder()) { builder ->
          val blockProvider = WeightedStateProvider(
            SimpleWeightedRandomList.builder<BlockState>()
//              .add(Blocks.RED_SANDSTONE.defaultBlockState(), 4)
//              .add(Blocks.SMOOTH_RED_SANDSTONE.defaultBlockState(), 4)
              .add(ProjectBlocks.RED_HEMATITE.get()
                .defaultBlockState(), 3)
              .add(ProjectBlocks.COBBLED_RED_HEMATITE.get()
                .defaultBlockState(), 1)
//              .add(DataboxBlocks.RED_SANDSTONE_PEBBLES.get().defaultBlockState(), 3)
          )

          builder.withSphereConfig(
            BoulderConfig.Builder()
              .setRadiusSettings(
                BoulderConfig.RadiusSettings(
                  UniformInt.of(5, 12),
                  UniformInt.of(5, 12), 0,
                  UniformInt.of(5, 12)
                )
              )
              .setBlockProvider(blockProvider)
              .setMiddleBlockProvider(blockProvider)
              .setTopBlockProvider(blockProvider)
              .withNoiseFrequency(0.1f)
              .withSpawningFeatures(
                listOf(
                  ProjectConfiguredFeatures.directPlacedFeature(
                    lookup.getOrThrow(ProjectConfiguredFeatures.RED_HEMATITE_IRON_ORE_KEY),
                    RarityFilter.onAverageOnceEvery(512)
                  ),
                )
              )
              .build()
          )

          builder.withMatchingBlendingFunctionChance(ConstantFloat.of(0.2f))
          builder.withPercentageDestroyed(ConstantFloat.of(0f))
          builder.withBlendingFunctionType(
            SimpleWeightedRandomList.builder<BlendingFunction>()
              .add(BlendingFunction.EASE_OUT_CUBIC, 16)
              .add(BlendingFunction.EAST_IN_OUT_CIRC, 8)
              .add(BlendingFunction.EASE_OUT_BOUNCE, 1)
              .build()
          )
        }
          .build()
      )
      val GRANITE_RED_ROCK_ARCH_PRESET = ArchStructure(
        StructureSettings(
          biomes.getOrThrow(ProjectTags.BIOME.HAS_GRANITE_ARCHES),
          Map.of(),
          GenerationStep.Decoration.RAW_GENERATION,
          TerrainAdjustment.NONE
        ),
        Util.make(ArchConfiguration.Builder()) { builder ->
          val blockProvider = WeightedStateProvider(
            SimpleWeightedRandomList.builder<BlockState>()
              .add(Blocks.TERRACOTTA.defaultBlockState(), 12)
              .add(Blocks.GRANITE.defaultBlockState(), 10)
          )

          builder.withSphereConfig(
            BoulderConfig.Builder()
              .setRadiusSettings(
                BoulderConfig.RadiusSettings(
                  UniformInt.of(5, 15),
                  UniformInt.of(5, 15), 0,
                  UniformInt.of(5, 15)
                )
              )
              .setBlockProvider(blockProvider)
              .setMiddleBlockProvider(blockProvider)
              .setTopBlockProvider(blockProvider)
              .withNoiseFrequency(0.1f)
              .build()
          )

          builder.withMatchingBlendingFunctionChance(ConstantFloat.of(0.2f))
          builder.withPercentageDestroyed(ConstantFloat.of(0f))
          builder.withBlendingFunctionType(
            SimpleWeightedRandomList.builder<BlendingFunction>()
              .add(BlendingFunction.EASE_OUT_CUBIC, 16)
              .add(BlendingFunction.EAST_IN_OUT_CIRC, 8)
              .add(BlendingFunction.EASE_OUT_BOUNCE, 1)
              .build()
          )
        }
          .build()
      )
      // ----
    }
  }
  // ----
}

