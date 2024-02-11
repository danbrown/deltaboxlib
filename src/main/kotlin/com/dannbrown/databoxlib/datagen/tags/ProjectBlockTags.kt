package com.dannbrown.databoxlib.datagen.tags

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.compat.ProjectModIntegrations
import com.dannbrown.databoxlib.init.ProjectBlocks
import com.dannbrown.databoxlib.init.ProjectTags
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.IntrinsicHolderTagsProvider
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraftforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture
import java.util.function.Function

class ProjectBlockTags(
  output: PackOutput,
  future: CompletableFuture<HolderLookup.Provider>,
  existingFileHelper: ExistingFileHelper
) : IntrinsicHolderTagsProvider<Block>(
  output,
  Registries.BLOCK,
  future,
  Function<Block, ResourceKey<Block>> { block: Block ->
    block.builtInRegistryHolder()
      .key()
  }, ProjectContent.MOD_ID,
  existingFileHelper
) {
  override fun getName(): String {
    return "Databox Block Tags"
  }

  override fun addTags(provider: HolderLookup.Provider) {
    // BEWARE: It can replace tags created by the REGISTRATE system
    tag(ProjectTags.BLOCK.RED_HEMATITE_REPLACEABLES)
      .add(ProjectBlocks.RED_HEMATITE.get())
    // Add Ship Assembler blacklist
    tag(ProjectTags.BLOCK.SHIP_BLACKLIST)
      // databoxlib blocks
      .add(ProjectBlocks.SILT.get())
      .add(ProjectBlocks.ROSEATE_GRAINS.get())
      .add(ProjectBlocks.ROSEATE_FAMILY.MAIN!!.get())
      .add(ProjectBlocks.ROSEATE_SANDSTONE_PEBBLES.get())
      .add(ProjectBlocks.SANDSTONE_PEBBLES.get())
      .add(ProjectBlocks.RED_SANDSTONE_PEBBLES.get())
      .add(ProjectBlocks.BASALT_PEBBLES.get())
      .add(ProjectBlocks.BROWN_SLATE.get())
      .add(ProjectBlocks.JOSHUA_FAMILY.SAPLING!!.get())
      .add(ProjectBlocks.JOSHUA_FAMILY.LEAVES!!.get())
      .add(ProjectBlocks.MOON_REGOLITH.get())
      // vanilla blocks
      .add(Blocks.AIR)
      // vanilla stone related
      .add(Blocks.STONE)
      .add(Blocks.DEEPSLATE)
      .add(Blocks.GRANITE)
      .add(Blocks.DIORITE)
      .add(Blocks.ANDESITE)
      .add(Blocks.TUFF)
      .add(Blocks.BASALT)
      .add(Blocks.POINTED_DRIPSTONE)
      .add(Blocks.DRIPSTONE_BLOCK)
      // vanilla fluids
      .add(Blocks.WATER)
      .add(Blocks.LAVA)
      // vanilla terrain/dirt related
      .add(Blocks.DIRT)
      .add(Blocks.GRASS_BLOCK)
      .add(Blocks.DIRT_PATH)
      .add(Blocks.COARSE_DIRT)
      .add(Blocks.MUD)
      .add(Blocks.PODZOL)
      .add(Blocks.MYCELIUM)
      .add(Blocks.MOSS_BLOCK)
      .add(Blocks.CLAY)
      // vanilla sand related
      .add(Blocks.SAND)
      .add(Blocks.RED_SAND)
      .add(Blocks.GRAVEL)
      .add(Blocks.SANDSTONE)
      .add(Blocks.RED_SANDSTONE)
      // vanilla nether
      .add(Blocks.BLACKSTONE)
      .add(Blocks.NETHERRACK)
      .add(Blocks.SOUL_SAND)
      .add(Blocks.SOUL_SOIL)
      .add(Blocks.CRIMSON_NYLIUM)
      .add(Blocks.WARPED_NYLIUM)
      // vanilla end
      .add(Blocks.END_STONE)
      // vanilla plants
      .add(Blocks.BROWN_MUSHROOM)
      .add(Blocks.RED_MUSHROOM)
      .add(Blocks.CRIMSON_FUNGUS)
      .add(Blocks.WARPED_FUNGUS)
      .add(Blocks.CRIMSON_ROOTS)
      .add(Blocks.WARPED_ROOTS)
      .add(Blocks.NETHER_SPROUTS)
      .add(Blocks.WEEPING_VINES)
      .add(Blocks.TWISTING_VINES)
      .add(Blocks.CHORUS_PLANT)
      .add(Blocks.CHORUS_FLOWER)
      .add(Blocks.CACTUS)
      .add(Blocks.VINE)
      .add(Blocks.SUNFLOWER)
      .add(Blocks.LILAC)
      .add(Blocks.ROSE_BUSH)
      .add(Blocks.PEONY)
      .add(Blocks.TALL_GRASS)
      .add(Blocks.LARGE_FERN)
      .add(Blocks.GRASS)
      .add(Blocks.FERN)
      .add(Blocks.DEAD_BUSH)
      .add(Blocks.SEAGRASS)
      .add(Blocks.TALL_SEAGRASS)
      .add(Blocks.SEA_PICKLE)
      .add(Blocks.KELP)
      .add(Blocks.BAMBOO)
      .add(Blocks.DANDELION)
      .add(Blocks.POPPY)
      .add(Blocks.BLUE_ORCHID)
      .add(Blocks.ALLIUM)
      .add(Blocks.AZURE_BLUET)
      .add(Blocks.RED_TULIP)
      .add(Blocks.ORANGE_TULIP)
      .add(Blocks.WHITE_TULIP)
      .add(Blocks.PINK_TULIP)
      .add(Blocks.OXEYE_DAISY)
      .add(Blocks.CORNFLOWER)
      .add(Blocks.LILY_OF_THE_VALLEY)
      .add(Blocks.LILY_PAD)
      .add(Blocks.PINK_PETALS)
      .add(Blocks.BIG_DRIPLEAF)
      .add(Blocks.BIG_DRIPLEAF_STEM)
      .add(Blocks.SMALL_DRIPLEAF)
      // vanilla snow related
      .add(Blocks.SNOW)
      .add(Blocks.SNOW_BLOCK)
      .add(Blocks.ICE)
      .add(Blocks.PACKED_ICE)
      .add(Blocks.BLUE_ICE)
      // vanilla indestructible blocks
      .add(Blocks.BEDROCK)
      .add(Blocks.NETHER_PORTAL)
      .add(Blocks.END_PORTAL_FRAME)
      .add(Blocks.END_PORTAL)
      .add(Blocks.END_GATEWAY)
      // vanilla trees
      .add(Blocks.OAK_SAPLING)
      .add(Blocks.SPRUCE_SAPLING)
      .add(Blocks.BIRCH_SAPLING)
      .add(Blocks.JUNGLE_SAPLING)
      .add(Blocks.ACACIA_SAPLING)
      .add(Blocks.DARK_OAK_SAPLING)
      .add(Blocks.MANGROVE_PROPAGULE)
      .add(Blocks.CHERRY_SAPLING)
      .add(Blocks.OAK_LEAVES)
      .add(Blocks.SPRUCE_LEAVES)
      .add(Blocks.BIRCH_LEAVES)
      .add(Blocks.JUNGLE_LEAVES)
      .add(Blocks.ACACIA_LEAVES)
      .add(Blocks.DARK_OAK_LEAVES)
      .add(Blocks.MANGROVE_LEAVES)
      .add(Blocks.CHERRY_LEAVES)
    // add mod compatibility tags
    ProjectModIntegrations.registerBlockTags(this::tag)
    // @ VANILLA
//    tag(BlockTags.WOOL).add(
//      DatagenBlocks.GLOW_WOOL.get(),
//      DatagenBlocks.GLOW_ORANGE_WOOL.get(),
//      DatagenBlocks.GLOW_MAGENTA_WOOL.get(),
//      DatagenBlocks.GLOW_LIGHT_BLUE_WOOL.get(),
//      DatagenBlocks.GLOW_YELLOW_WOOL.get(),
//      DatagenBlocks.GLOW_LIME_WOOL.get(),
//      DatagenBlocks.GLOW_PINK_WOOL.get(),
//      DatagenBlocks.GLOW_GRAY_WOOL.get(),
//      DatagenBlocks.GLOW_LIGHT_GRAY_WOOL.get(),
//      DatagenBlocks.GLOW_CYAN_WOOL.get(),
//      DatagenBlocks.GLOW_PURPLE_WOOL.get(),
//      DatagenBlocks.GLOW_BLUE_WOOL.get(),
//      DatagenBlocks.GLOW_BROWN_WOOL.get(),
//      DatagenBlocks.GLOW_GREEN_WOOL.get(),
//      DatagenBlocks.GLOW_RED_WOOL.get(),
//      DatagenBlocks.GLOW_BLACK_WOOL.get()
//    )
  }
}