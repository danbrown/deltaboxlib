package com.dannbrown.deltaboxlib.registry.transformers

import com.dannbrown.deltaboxlib.lib.LibTags
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraftforge.common.Tags

object BlockTagPresets {
  fun oreBlockTags(name: String, replace: String = "stone"): Pair<Array<TagKey<Block>>, Array<TagKey<Item>>> {
    val forgeOreBlockTag = LibTags.forgeBlockTag("ores/$name")
    val forgeOreItemTag = LibTags.forgeItemTag("ores/$name")
    val forgeOreGroundBlockTag = LibTags.forgeBlockTag("ores_in_ground/$replace")
    val forgeOreGroundItemTag = LibTags.forgeItemTag("ores_in_ground/$replace")

    return Pair(arrayOf(forgeOreBlockTag, forgeOreGroundBlockTag, Tags.Blocks.ORES), arrayOf(forgeOreItemTag, forgeOreGroundItemTag, Tags.Items.ORES))
  }

  fun storageBlockTags(name: String): Pair<Array<TagKey<Block>>, Array<TagKey<Item>>> {
    val forgeStorageBlockTag = LibTags.forgeBlockTag("storage_blocks/$name")
    val forgeStorageItemTag = LibTags.forgeItemTag("storage_blocks/$name")

    return Pair(arrayOf(forgeStorageBlockTag, Tags.Blocks.STORAGE_BLOCKS), arrayOf(forgeStorageItemTag, Tags.Items.STORAGE_BLOCKS))
  }

  fun woodenStairsTags(): Pair<Array<TagKey<Block>>, Array<TagKey<Item>>> {
    return Pair(arrayOf(BlockTags.STAIRS, BlockTags.WOODEN_STAIRS), arrayOf(ItemTags.STAIRS, ItemTags.WOODEN_STAIRS))
  }

  fun stairsTags(): Pair<Array<TagKey<Block>>, Array<TagKey<Item>>> {
    return Pair(arrayOf(BlockTags.STAIRS), arrayOf(ItemTags.STAIRS))
  }

  fun woodenSlabTags(): Pair<Array<TagKey<Block>>, Array<TagKey<Item>>> {
    return Pair(arrayOf(BlockTags.SLABS, BlockTags.WOODEN_SLABS), arrayOf(ItemTags.SLABS, ItemTags.WOODEN_SLABS))
  }

  fun slabTags(): Pair<Array<TagKey<Block>>, Array<TagKey<Item>>> {
    return Pair(arrayOf(BlockTags.SLABS), arrayOf(ItemTags.SLABS))
  }

  fun wallTags(): Pair<Array<TagKey<Block>>, Array<TagKey<Item>>> {
    return Pair(arrayOf(BlockTags.WALLS), arrayOf(ItemTags.WALLS))
  }


  fun ladderBlockTags(): Pair<Array<TagKey<Block>>, Array<TagKey<Item>>> {
    return Pair(arrayOf(BlockTags.CLIMBABLE), arrayOf())
  }

  fun woodenTrapdoorTags(): Pair<Array<TagKey<Block>>, Array<TagKey<Item>>> {
    return Pair(arrayOf(BlockTags.WOODEN_TRAPDOORS, BlockTags.TRAPDOORS), arrayOf(ItemTags.WOODEN_TRAPDOORS, ItemTags.TRAPDOORS))
  }

  fun trapdoorTags(): Pair<Array<TagKey<Block>>, Array<TagKey<Item>>> {
    return Pair(arrayOf(BlockTags.TRAPDOORS), arrayOf(ItemTags.TRAPDOORS))
  }


  fun fenceTags(isWooden: Boolean): Pair<Array<TagKey<Block>>, Array<TagKey<Item>>> {
    return if (isWooden) Pair(arrayOf(BlockTags.FENCES, BlockTags.WOODEN_FENCES), arrayOf(ItemTags.FENCES, ItemTags.WOODEN_FENCES))
    else Pair(arrayOf(BlockTags.FENCES), arrayOf(ItemTags.FENCES))
  }

  fun pressurePlateTags(isWooden: Boolean): Pair<Array<TagKey<Block>>, Array<TagKey<Item>>> {
    return if (isWooden) Pair(arrayOf(BlockTags.PRESSURE_PLATES, BlockTags.WOODEN_PRESSURE_PLATES), arrayOf(ItemTags.WOODEN_PRESSURE_PLATES))
    else Pair(arrayOf(BlockTags.PRESSURE_PLATES), arrayOf())
  }

  fun buttonTags(isWooden: Boolean): Pair<Array<TagKey<Block>>, Array<TagKey<Item>>> {
    return if (isWooden) Pair(arrayOf(BlockTags.BUTTONS, BlockTags.WOODEN_BUTTONS), arrayOf(ItemTags.WOODEN_BUTTONS, ItemTags.BUTTONS))
    else Pair(arrayOf(BlockTags.BUTTONS), arrayOf(ItemTags.BUTTONS))
  }

  fun doorTags(isWooden: Boolean): Pair<Array<TagKey<Block>>, Array<TagKey<Item>>> {
    return if (isWooden) Pair(arrayOf(BlockTags.DOORS, BlockTags.WOODEN_DOORS), arrayOf(ItemTags.DOORS, ItemTags.WOODEN_DOORS))
    else Pair(arrayOf(BlockTags.DOORS), arrayOf(ItemTags.DOORS))
  }

  fun caveReplaceableTags(): Pair<Array<TagKey<Block>>, Array<TagKey<Item>>> {
    return Pair(arrayOf(BlockTags.DRIPSTONE_REPLACEABLE, BlockTags.AZALEA_ROOT_REPLACEABLE, BlockTags.MOSS_REPLACEABLE, BlockTags.LUSH_GROUND_REPLACEABLE), arrayOf())
  }
}
