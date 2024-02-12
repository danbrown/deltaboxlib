package com.dannbrown.databoxlib.registry

import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import java.util.function.Supplier

interface BlockFamilyProperties {
  val name: String
  val sharedProps: (BlockBehaviour.Properties) -> BlockBehaviour.Properties
  val toolType: TagKey<Block>?
  val toolTier: TagKey<Block>?
  val color: MapColor
  val accentColor: MapColor
  val copyFrom: Supplier<Block>
  val denyList: MutableList<com.dannbrown.databoxlib.datagen.content.BlockFamily.Type>
  val blockFamily: com.dannbrown.databoxlib.datagen.content.BlockFamily
}