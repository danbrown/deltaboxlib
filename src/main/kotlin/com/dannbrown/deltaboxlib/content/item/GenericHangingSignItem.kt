package com.dannbrown.deltaboxlib.content.item

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.HangingSignItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SignItem
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SignBlock
import net.minecraft.world.level.block.entity.SignBlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext

class GenericHangingSignItem(props: Properties, signBlock: Block, wallSignBlock: Block): HangingSignItem(signBlock, wallSignBlock, props) {
}