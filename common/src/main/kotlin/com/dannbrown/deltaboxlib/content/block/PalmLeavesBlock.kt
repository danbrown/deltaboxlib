package com.dannbrown.deltaboxlib.content.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BlockTags
import net.minecraft.util.RandomSource
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SimpleWaterloggedBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.level.material.Fluids
import java.util.*

open class PalmLeavesBlock(
    pProperties: Properties,
    private val flammability: Int = 20,
    private val fireSpread: Int = 5
) : FlammableLeavesBlock(pProperties, flammability, fireSpread), SimpleWaterloggedBlock {
    companion object {
        const val MAX_DISTANCE_12 = 12
        val DISTANCE_12: IntegerProperty = IntegerProperty.create("distance_9", 1, MAX_DISTANCE_12)
    }

    init {
        registerDefaultState(
            defaultBlockState()
                .setValue(DISTANCE, 1)
                .setValue(DISTANCE_12, MAX_DISTANCE_12)
                .setValue(PERSISTENT, false)
                .setValue(WATERLOGGED, false)
        )
    }

    override fun isRandomlyTicking(state: BlockState): Boolean {
        return state.getValue(DISTANCE_12) == MAX_DISTANCE_12 && !state.getValue(PERSISTENT)
    }

    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        if (!state.getValue(PERSISTENT) && state.getValue(DISTANCE_12) == MAX_DISTANCE_12) {
            dropResources(state, level, pos)
            level.removeBlock(pos, false)
        }
    }

    override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        level.setBlock(pos, updatePalmDistance(state, level, pos), 3)
    }

    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        level: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level))
        }

        val distance = getDistanceAtPalm(neighborState) + 1
        if (distance != 1 || state.getValue(DISTANCE_12) != distance) {
            level.scheduleTick(pos, this, 1)
        }

        return state
    }

    private fun updatePalmDistance(state: BlockState, level: LevelAccessor, pos: BlockPos): BlockState {
        var distance = MAX_DISTANCE_12
        val mutablePos = BlockPos.MutableBlockPos()

        for (dir in Direction.values()) {
            mutablePos.setWithOffset(pos, dir)
            distance = minOf(distance, getDistanceAtPalm(level.getBlockState(mutablePos)) + 1)
            if (distance == 1) break
        }

        return state.setValue(DISTANCE_12, distance)
    }

    private fun getDistanceAtPalm(state: BlockState): Int {
        return getOptionalDistanceAtPalm(state).orElse(MAX_DISTANCE_12)
    }

    private fun getOptionalDistanceAtPalm(state: BlockState): OptionalInt {
        return when {
            state.`is`(BlockTags.LOGS) -> OptionalInt.of(0)
            state.hasProperty(DISTANCE_12) -> OptionalInt.of(state.getValue(DISTANCE_12))
            state.hasProperty(DISTANCE) -> OptionalInt.of(state.getValue(DISTANCE))
            else -> OptionalInt.empty()
        }
    }

    override fun createBlockStateDefinition(stateBuilder: StateDefinition.Builder<Block?, BlockState?>) {
        super.createBlockStateDefinition(stateBuilder)
        stateBuilder.add(DISTANCE_12)
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState {
        val fluid = ctx.level.getFluidState(ctx.clickedPos)
        val state = defaultBlockState()
            .setValue(PERSISTENT, true)
            .setValue(WATERLOGGED, fluid.type == Fluids.WATER)

        return updatePalmDistance(state, ctx.level, ctx.clickedPos)
    }
}