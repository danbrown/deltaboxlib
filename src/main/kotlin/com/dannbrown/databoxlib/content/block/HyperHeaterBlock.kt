package com.dannbrown.databoxlib.content.block

import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.CampfireBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition.Builder
import net.minecraft.world.level.block.state.properties.EnumProperty

class HyperHeaterBlock(p: Properties) : CampfireBlock(true, 2, p) {
    companion object {
        @kotlin.jvm.JvmField
        val HEAT_LEVEL: EnumProperty<HeatLevel> = EnumProperty.create("hyper", HeatLevel::class.java)
    }

    init {
        registerDefaultState(
            defaultBlockState()
                .setValue(HEAT_LEVEL, HeatLevel.valueOf("HYPER")),
        )
    }

    override fun createBlockStateDefinition(builder: Builder<Block, BlockState?>) {
        builder.add(HEAT_LEVEL)
        super.createBlockStateDefinition(builder)
    }
}
