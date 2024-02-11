package com.dannbrown.databoxlib.content.ship.block.shipConsole

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class ShipConsoleBlockEntity(type: BlockEntityType<ShipConsoleBlockEntity>, pos: BlockPos, state: BlockState) : BlockEntity(type, pos, state)