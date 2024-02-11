package com.dannbrown.databoxlib.datagen.content

import com.tterrag.registrate.util.entry.BlockEntry
import net.minecraft.world.level.block.Block
import java.util.function.Supplier


data class BlockFamily(
  var MAIN: BlockEntry<out Block>? = null,

  var SLAB: BlockEntry<out Block>? = null,
  var STAIRS: BlockEntry<out Block>? = null,
  var WALL: BlockEntry<out Block>? = null,
  var BARS: BlockEntry<out Block>? = null,

  var TRAPDOOR: BlockEntry<out Block>? = null,
  var FENCE: BlockEntry<out Block>? = null,
  var FENCE_GATE: BlockEntry<out Block>? = null,
  var DOOR: BlockEntry<out Block>? = null,

  var BUTTON: BlockEntry<out Block>? = null,
  var PRESSURE_PLATE: BlockEntry<out Block>? = null,
  var LAMP: BlockEntry<out Block>? = null,

  var SCAFFOLDING: BlockEntry<out Block>? = null,
  var LADDER: BlockEntry<out Block>? = null,
  var SIGN: BlockEntry<out Block>? = null,
  var WALL_SIGN: BlockEntry<out Block>? = null,
  var CHAIN: BlockEntry<out Block>? = null,

  var POLISHED: BlockEntry<out Block>? = null,
  var POLISHED_SLAB: BlockEntry<out Block>? = null,
  var POLISHED_STAIRS: BlockEntry<out Block>? = null,
  var POLISHED_WALL: BlockEntry<out Block>? = null,
  var POLISHED_BUTTON: BlockEntry<out Block>? = null,
  var POLISHED_PRESSURE_PLATE: BlockEntry<out Block>? = null,

  var SANDSTONE: BlockEntry<out Block>? = null,
  var SANDSTONE_SLAB: BlockEntry<out Block>? = null,
  var SANDSTONE_STAIRS: BlockEntry<out Block>? = null,
  var SANDSTONE_WALL: BlockEntry<out Block>? = null,
  var SANDSTONE_BUTTON: BlockEntry<out Block>? = null,
  var SANDSTONE_PRESSURE_PLATE: BlockEntry<out Block>? = null,

  var CUT: BlockEntry<out Block>? = null,
  var CUT_SLAB: BlockEntry<out Block>? = null,
  var CUT_STAIRS: BlockEntry<out Block>? = null,
  var CUT_WALL: BlockEntry<out Block>? = null,
  var CUT_BUTTON: BlockEntry<out Block>? = null,
  var CUT_PRESSURE_PLATE: BlockEntry<out Block>? = null,

  var CHISELED: BlockEntry<out Block>? = null,
  var CHISELED_SLAB: BlockEntry<out Block>? = null,
  var CHISELED_STAIRS: BlockEntry<out Block>? = null,
  var CHISELED_WALL: BlockEntry<out Block>? = null,
  var CHISELED_BUTTON: BlockEntry<out Block>? = null,
  var CHISELED_PRESSURE_PLATE: BlockEntry<out Block>? = null,

  var BRICKS: BlockEntry<out Block>? = null,
  var BRICK_SLAB: BlockEntry<out Block>? = null,
  var BRICK_STAIRS: BlockEntry<out Block>? = null,
  var BRICK_WALL: BlockEntry<out Block>? = null,
  var BRICK_BUTTON: BlockEntry<out Block>? = null,
  var BRICK_PRESSURE_PLATE: BlockEntry<out Block>? = null,

  var SMOOTH: BlockEntry<out Block>? = null,
  var SMOOTH_SLAB: BlockEntry<out Block>? = null,
  var SMOOTH_STAIRS: BlockEntry<out Block>? = null,
  var SMOOTH_WALL: BlockEntry<out Block>? = null,
  var SMOOTH_BUTTON: BlockEntry<out Block>? = null,
  var SMOOTH_PRESSURE_PLATE: BlockEntry<out Block>? = null,

  var PILLAR: BlockEntry<out Block>? = null,

  var LOG: BlockEntry<out Block>? = null,
  var STRIPPED_LOG: BlockEntry<out Block>? = null,
  var WOOD: BlockEntry<out Block>? = null,
  var STRIPPED_WOOD: BlockEntry<out Block>? = null,
  var STALK: BlockEntry<out Block>? = null,
  var STRIPPED_STALK: BlockEntry<out Block>? = null,
  var LEAVES: BlockEntry<out Block>? = null,
  var SAPLING: BlockEntry<out Block>? = null,
  var POTTED_SAPLING: BlockEntry<out Block>? = null,

  var FLOWER: BlockEntry<out Block>? = null,
  var POTTED_FLOWER: BlockEntry<out Block>? = null,
){

  enum class Type {
    MAIN,

    // other block models
    SLAB, STAIRS, WALL, BARS,
    TRAPDOOR, FENCE, FENCE_GATE, DOOR,
    BUTTON, PRESSURE_PLATE, LAMP,
    SCAFFOLDING, LADDER, SIGN, WALL_SIGN, CHAIN,

    // for stone variants
    POLISHED, POLISHED_SLAB, POLISHED_STAIRS, POLISHED_WALL, POLISHED_BUTTON, POLISHED_PRESSURE_PLATE,
    SANDSTONE, SANDSTONE_SLAB, SANDSTONE_STAIRS, SANDSTONE_WALL, SANDSTONE_BUTTON, SANDSTONE_PRESSURE_PLATE,
    CUT, CUT_SLAB, CUT_STAIRS, CUT_WALL, CUT_BUTTON, CUT_PRESSURE_PLATE,
    CHISELED, CHISELED_SLAB, CHISELED_STAIRS, CHISELED_WALL, CHISELED_BUTTON, CHISELED_PRESSURE_PLATE,
    BRICKS, BRICK_SLAB, BRICK_STAIRS, BRICK_WALL, BRICK_BUTTON, BRICK_PRESSURE_PLATE,
    SMOOTH, SMOOTH_SLAB, SMOOTH_STAIRS, SMOOTH_WALL, SMOOTH_BUTTON, SMOOTH_PRESSURE_PLATE,
    PILLAR,

    // for wood variants
    LOG, STRIPPED_LOG, WOOD, STRIPPED_WOOD, STALK, STRIPPED_STALK, LEAVES, SAPLING, POTTED_SAPLING,

    // for Flowers
    FLOWER, POTTED_FLOWER,
  }

  fun setVariant(type: Type, block: Supplier<BlockEntry<out Block>>) {
    when (type) {
      Type.MAIN -> MAIN = block.get()

      Type.SLAB -> SLAB = block.get()
      Type.STAIRS -> STAIRS = block.get()
      Type.WALL -> WALL = block.get()
      Type.BARS -> BARS = block.get()
      Type.TRAPDOOR -> TRAPDOOR = block.get()
      Type.FENCE -> FENCE = block.get()
      Type.FENCE_GATE -> FENCE_GATE = block.get()
      Type.DOOR -> DOOR = block.get()
      Type.BUTTON -> BUTTON = block.get()
      Type.PRESSURE_PLATE -> PRESSURE_PLATE = block.get()
      Type.LAMP -> LAMP = block.get()
      Type.SCAFFOLDING -> SCAFFOLDING = block.get()
      Type.LADDER -> LADDER = block.get()
      Type.SIGN -> SIGN = block.get()
      Type.WALL_SIGN -> WALL_SIGN = block.get()
      Type.CHAIN -> CHAIN = block.get()
      Type.POLISHED -> POLISHED = block.get()
      Type.POLISHED_SLAB -> POLISHED_SLAB = block.get()
      Type.POLISHED_STAIRS -> POLISHED_STAIRS = block.get()
      Type.POLISHED_WALL -> POLISHED_WALL = block.get()
      Type.POLISHED_BUTTON -> POLISHED_BUTTON = block.get()
      Type.POLISHED_PRESSURE_PLATE -> POLISHED_PRESSURE_PLATE = block.get()
      Type.SANDSTONE -> SANDSTONE = block.get()
      Type.SANDSTONE_SLAB -> SANDSTONE_SLAB = block.get()
      Type.SANDSTONE_STAIRS -> SANDSTONE_STAIRS = block.get()
      Type.SANDSTONE_WALL -> SANDSTONE_WALL = block.get()
      Type.SANDSTONE_BUTTON -> SANDSTONE_BUTTON = block.get()
      Type.SANDSTONE_PRESSURE_PLATE -> SANDSTONE_PRESSURE_PLATE = block.get()
      Type.CUT -> CUT = block.get()
      Type.CUT_SLAB -> CUT_SLAB = block.get()
      Type.CUT_STAIRS -> CUT_STAIRS = block.get()
      Type.CUT_WALL -> CUT_WALL = block.get()
      Type.CUT_BUTTON -> CUT_BUTTON = block.get()
      Type.CUT_PRESSURE_PLATE -> CUT_PRESSURE_PLATE = block.get()
      Type.CHISELED -> CHISELED = block.get()
      Type.CHISELED_SLAB -> CHISELED_SLAB = block.get()
      Type.CHISELED_STAIRS -> CHISELED_STAIRS = block.get()
      Type.CHISELED_WALL -> CHISELED_WALL = block.get()
      Type.CHISELED_BUTTON -> CHISELED_BUTTON = block.get()
      Type.CHISELED_PRESSURE_PLATE -> CHISELED_PRESSURE_PLATE = block.get()
      Type.BRICKS -> BRICKS = block.get()
      Type.BRICK_SLAB -> BRICK_SLAB = block.get()
      Type.BRICK_STAIRS -> BRICK_STAIRS = block.get()
      Type.BRICK_WALL -> BRICK_WALL = block.get()
      Type.BRICK_BUTTON -> BRICK_BUTTON = block.get()
      Type.BRICK_PRESSURE_PLATE -> BRICK_PRESSURE_PLATE = block.get()

      Type.SMOOTH -> SMOOTH = block.get()
      Type.SMOOTH_SLAB -> SMOOTH_SLAB = block.get()
      Type.SMOOTH_STAIRS -> SMOOTH_STAIRS = block.get()
      Type.SMOOTH_WALL -> SMOOTH_WALL = block.get()
      Type.SMOOTH_BUTTON -> SMOOTH_BUTTON = block.get()
      Type.SMOOTH_PRESSURE_PLATE -> SMOOTH_PRESSURE_PLATE = block.get()

      Type.PILLAR -> PILLAR = block.get()

      Type.LOG -> LOG = block.get()
      Type.STRIPPED_LOG -> STRIPPED_LOG = block.get()
      Type.WOOD -> WOOD = block.get()
      Type.STRIPPED_WOOD -> STRIPPED_WOOD = block.get()
      Type.STALK -> STALK = block.get()
      Type.STRIPPED_STALK -> STRIPPED_STALK = block.get()
      Type.LEAVES -> LEAVES = block.get()
      Type.SAPLING -> SAPLING = block.get()
      Type.POTTED_SAPLING -> POTTED_SAPLING = block.get()

      Type.FLOWER -> FLOWER = block.get()
      Type.POTTED_FLOWER -> POTTED_FLOWER = block.get()
    }
  }
}

