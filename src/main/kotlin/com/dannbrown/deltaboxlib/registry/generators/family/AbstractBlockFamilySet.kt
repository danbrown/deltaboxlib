package com.dannbrown.deltaboxlib.registry.generators.family

import com.dannbrown.deltaboxlib.registry.generators.BlockFamily
import com.dannbrown.deltaboxlib.registry.generators.BlockGenerator
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import java.util.function.Supplier

abstract class AbstractBlockFamilySet {
  protected val _blockFamily: BlockFamily = BlockFamily()

  fun getFamily(): BlockFamily {
    return _blockFamily
  }
}