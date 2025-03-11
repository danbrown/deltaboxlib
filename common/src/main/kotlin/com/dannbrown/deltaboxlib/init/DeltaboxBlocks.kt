package com.dannbrown.deltaboxlib.init

import com.dannbrown.deltaboxlib.init.DeltaboxLibMod.REGISTRATE
import com.dannbrown.deltaboxlib.content.block.*
import com.dannbrown.deltaboxlib.content.block.eyeblossom.EyeBlossomBlock
import com.dannbrown.deltaboxlib.content.worldgen.tree.DeltaboxTreeGrower
import com.dannbrown.deltaboxlib.registrate.presets.family.BlockFamily
import com.dannbrown.deltaboxlib.registrate.registry.BlockEntry
import com.dannbrown.deltaboxlib.registrate.registry.ItemEntry
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.ButtonBlock
import net.minecraft.world.level.block.DoorBlock
import net.minecraft.world.level.block.FenceBlock
import net.minecraft.world.level.block.FenceGateBlock
import net.minecraft.world.level.block.FlowerPotBlock
import net.minecraft.world.level.block.PressurePlateBlock
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.TrapDoorBlock
import net.minecraft.world.level.block.WallBlock
import net.minecraft.world.level.block.state.BlockBehaviour.OffsetType
import net.minecraft.world.level.block.state.properties.BlockSetType
import net.minecraft.world.level.block.state.properties.WoodType
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.material.PushReaction


object DeltaboxBlocks {
  val ADAMANTIUM_BLOCK = REGISTRATE
    .block<RotatedPillarBlock>("adamantium_block")
    .copyFrom { Blocks.STONE }
    .factory { c, p -> RotatedPillarBlock(p) }
    .loot { loot, block -> loot.dropSelf(block.get()) }
    .item { a, b -> BlockItem(b, a.food(FoodProperties.Builder().fast().build())) }
    .build()
    .register()

  val SECOND_BLOCK = REGISTRATE
    .block<RotatedPillarBlock>("second_block")
    .factory { c, p -> RotatedPillarBlock(p) }
    .strippable { ADAMANTIUM_BLOCK.get() }
    .register()

  val IRON_BLOCK2 = REGISTRATE
    .blockPreset<Block>("compiled")
    .storageBlock({ Items.IRON_INGOT }, { Ingredient.of(Items.FLINT) })
    .copyFrom { Blocks.IRON_BLOCK }
    .toolAndTier(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_IRON_TOOL)
    .register()


  val ACAI_CRATE = REGISTRATE
    .block<RotatedPillarBlock>("acai_berries_crate")
    .lang("Acai Berries Crate AHA")
    .factory { c, p -> RotatedPillarBlock(p) }
    .strippable { Blocks.ACACIA_LOG }
    .blockTags(BlockTags.DIRT)
    .recipe { r, b ->
      r.directShapelessRecipe(
        { b.get() },
        { Ingredient.of(SECOND_BLOCK.getItem()) },
        RecipeCategory.BUILDING_BLOCKS,
        2,
        "_from_second"
      )
    }
    .toolAndTier(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_STONE_TOOL)
    .blockstate { ctx, block -> ctx.bottomTopBlock(block.get(), "crate_bottom") }
    .item()
    .itemTags(ItemTags.NON_FLAMMABLE_WOOD, ItemTags.WART_BLOCKS)
    .build()
    .register()


  val FLAMMABLE_BLOCK = REGISTRATE.block<FlammableBlock>("flammable_block")
    .factory { c, p -> FlammableBlock(p, c.flammabilityBurnChance, c.flammabilitySpreadChance) }
    .flammable()
    .toolAndTier(null, BlockTags.NEEDS_STONE_TOOL)
    .register()

  val LANGS = REGISTRATE
    .langs()
    .genericTooltip("flint", "It's a Delta!")
    .register()

  val TAGS = REGISTRATE.blockTags(BlockTags.DIRT)
    .add(ADAMANTIUM_BLOCK)
    .add(BlockEntry.from(Blocks.WHITE_WOOL))

  val ITEMTAGS = REGISTRATE.itemTags(ItemTags.NON_FLAMMABLE_WOOD)
    .add(ADAMANTIUM_BLOCK.getItemEntry())
    .add(FLAMMABLE_BLOCK.getItemEntry())
    .add(ItemEntry.from(Items.ACACIA_LOG))

  val RECIPES = REGISTRATE.recipe { r ->
    r.directShapelessRecipe(
      { Blocks.STONE },
      { Ingredient.of(Blocks.END_STONE) },
      RecipeCategory.BUILDING_BLOCKS,
      4,
      "_from_wood"
    )

    r.directShapelessRecipe(
      { Blocks.OAK_BUTTON },
      { Ingredient.of(Items.STICK) },
      RecipeCategory.BUILDING_BLOCKS,
      2,
      "_from_wood"
    )
  }

  val LEMON_SAPLING: BlockEntry<GenericSaplingBlock> = REGISTRATE
    .blockPreset<GenericSaplingBlock>("lemon")
    .saplingBlock(DeltaboxTreeGrower.SAMPLE) { blockState, _, _ -> blockState.`is`(BlockTags.DIRT) }
    .register()
  val POTTED_LEMON_SAPLING: BlockEntry<FlowerPotBlock> = REGISTRATE
    .blockPreset<FlowerPotBlock>("lemon")
    .pottedBlock({ LEMON_SAPLING.get() }, "_sapling")
    .register()

  val SIMPLE_GRASS: BlockEntry<GenericGrassBlock> = REGISTRATE.blockPreset<GenericGrassBlock>("simple_grass")
    .grassBlock({ Items.WHEAT_SEEDS })
    .register()

  val POTTED_SIMPLE_GRASS: BlockEntry<FlowerPotBlock> = REGISTRATE
    .blockPreset<FlowerPotBlock>("simple_grass")
    .pottedBlock({ SIMPLE_GRASS.get() })
    .register()


  val SIMPLE_FLOWER = REGISTRATE.block<GenericGrassBlock>("simple_flower")
    .factory { c, p -> TrailFlowerBlock(p) }
    .copyFrom { Blocks.POPPY }
    .properties { c, p ->
      p.sound(SoundType.GRASS)
        .strength(0.0f)
        .noCollission()
        .noOcclusion()
        .randomTicks()
    }
    .blockstate { g, b -> g.crossBlock(b.get()) }
    .cutoutRender()
    .item()
    .model { g, i -> g.flatItemBlock(i.get()) }
    .build()
    .compostable(0.3f)
    .loot { g, b -> g.dropSelf(b.get()) }
    .register()

  val POTTED_SIMPLE_FLOWER: BlockEntry<FlowerPotBlock> = REGISTRATE
    .blockPreset<FlowerPotBlock>("simple_flower")
    .pottedBlock({ SIMPLE_FLOWER.get() })
    .register()


  val TALL_SPARSE_DRY_GRASS: BlockEntry<GenericDoublePlantBlock> =
    REGISTRATE.blockPreset<GenericDoublePlantBlock>("sparse_dry_grass").doubleTallGrassBlock(
      { Items.BEETROOT_SEEDS },
      null,
      { blockState, _, _ -> blockState.`is`(BlockTags.SAND) })
      .color(MapColor.TERRACOTTA_YELLOW)
      .register()
  val SPARSE_DRY_GRASS: BlockEntry<GenericTallGrassBlock> =
    REGISTRATE.blockPreset<GenericTallGrassBlock>("sparse_dry_grass").smallTallGrassBlock(
      TALL_SPARSE_DRY_GRASS,
      { Items.BEETROOT_SEEDS },
      true,
      { blockState, _, _ -> blockState.`is`(BlockTags.SAND) })
      .color(MapColor.TERRACOTTA_YELLOW)
      .register()

  // Leaves
  val ACAI_LEAVES = REGISTRATE.blockPreset<PalmLeavesBlock>("acai")
    .palmLeaves({ LEMON_SAPLING.get() })
    .color(MapColor.COLOR_LIGHT_GREEN)
    .biomeColors()
    .register()

  val CROP_LEAVES = REGISTRATE.blockPreset<CropLeavesBlock>("budding_lemon")
    .cropLeaves({ LEMON_SAPLING.get() }, { Items.EMERALD })
    .color(MapColor.COLOR_LIGHT_GREEN)
    .register()

  val BUDDING_LEMON_LEAVES = REGISTRATE.blockPreset<BuddingLeavesBlock>("coconut")
    .buddingLeaves({ LEMON_SAPLING.get() }, { Blocks.MANGROVE_PROPAGULE })
    .color(MapColor.COLOR_LIGHT_GREEN)
    .biomeColors()
    .register()


  // Crops
  val GARLIC_CROP = REGISTRATE.blockPreset<GenericCropBlock>("garlic_clove")
    .crop("garlic", "Garlic Crop", "Garlic Clove", { DeltaboxItems.WARP_CRYSTAL.get() }, false, false)
    .color(MapColor.COLOR_LIGHT_GREEN)
    .register()

  val CARIOCA_BEANS_CROP = REGISTRATE.blockPreset<GenericCropBlock>("carioca_beans")
    .crop("bean", "Carioca Beans Crop", "Carioca Beans", { DeltaboxItems.BEAN_POD.get() }, true, false)
    .color(MapColor.COLOR_LIGHT_GREEN)
    .register()

  val BLACK_BEANS_CROP = REGISTRATE.blockPreset<GenericCropBlock>("black_beans")
    .crop("bean", "Black Beans Crop", "Black Beans", { DeltaboxItems.BEAN_POD.get() }, true, false)
    .color(MapColor.COLOR_LIGHT_GREEN)
    .register()

  val BUDDING_CORN: BlockEntry<GenericCropBlock> = REGISTRATE.blockPreset<GenericCropBlock>("kernels")
    .buddingCrop("corn", "Kernels", "Corn Crop", { CORN_CROP.get() }, false)
    .color(MapColor.COLOR_LIGHT_GREEN)
    .register()

  val CORN_CROP: BlockEntry<GenericCropBlock> = REGISTRATE.blockPreset<GenericCropBlock>("corn_crop")
    .doubleCrop("corn", "Corn Crop", { BUDDING_CORN.get().asItem() }, { DeltaboxItems.BEAN_POD.get() }, true)
    .color(MapColor.COLOR_LIGHT_GREEN)
    .register()

  val BUDDING_CASSAVA: BlockEntry<GenericCropBlock> = REGISTRATE.blockPreset<GenericCropBlock>("cassava_root")
    .buddingCrop("cassava", "Cassava Root", "Cassava Crop", { CASSAVA_CROP.get() }, false)
    .color(MapColor.COLOR_LIGHT_GREEN)
    .register()

  val CASSAVA_CROP: BlockEntry<GenericCropBlock> = REGISTRATE.blockPreset<GenericCropBlock>("cassava_crop")
    .doubleCrop(
      "cassava",
      "Cassava Crop",
      { BUDDING_CASSAVA.get().asItem() },
      { BUDDING_CASSAVA.get().asItem() },
      false,
      true,
      1f,
      3
    )
    .color(MapColor.COLOR_LIGHT_GREEN)
    .register()

  // Family blocks test
  val LONG_FAMILY_TEST = REGISTRATE.blockfamily("pyrite")
    .toolAndTier(BlockTags.MINEABLE_WITH_PICKAXE, null, true)
    .denyList(BlockFamily.Type.PILLAR)
    .longBlockFamily()

  val WOOD_TEST = REGISTRATE.blockfamily("pale_oak")
    .woodFamily(
      DeltaboxWoodTypes.PALE_OAK,
      DeltaboxWoodTypes.PALE_OAK_SET,
      DeltaboxTreeGrower.SAMPLE,
      { blockState, _, _ -> blockState.`is`(BlockTags.DIRT) })


  // Eye blossom
  val EYE_BLOSSOM: BlockEntry<EyeBlossomBlock> = REGISTRATE.block<EyeBlossomBlock>("open_eyeblossom")
    .factory { c, p -> EyeBlossomBlock(true, p) }
    .copyFrom { Blocks.POPPY }
    .color(MapColor.PLANT)
    .properties { c, p ->
      p.noCollission().instabreak().sound(SoundType.GRASS).offsetType(OffsetType.XZ).pushReaction(PushReaction.DESTROY)
        .randomTicks()
    }
    .blockstate { g, b -> g.crossBlock(b.get(), "open_eyeblossom") }
    .blockTags(BlockTags.FLOWERS)
    .itemTags(ItemTags.FLOWERS)
    .cutoutRender()
    .item()
    .model { g, i -> g.flatItemBlock(i.get(), "open_eyeblossom_item") }
    .build()
    .register() as BlockEntry<EyeBlossomBlock>
  val CLOSED_EYE_BLOSSOM: BlockEntry<EyeBlossomBlock> = REGISTRATE.block<EyeBlossomBlock>("closed_eyeblossom")
    .factory { c, p -> EyeBlossomBlock(false, p) }
    .copyFrom { Blocks.POPPY }
    .color(MapColor.PLANT)
    .properties { c, p ->
      p.noCollission().instabreak().sound(SoundType.GRASS).offsetType(OffsetType.XZ).pushReaction(PushReaction.DESTROY)
        .randomTicks()
    }
    .blockstate { g, b -> g.crossBlock(b.get(), "closed_eyeblossom") }
    .blockTags(BlockTags.FLOWERS)
    .itemTags(ItemTags.FLOWERS)
    .cutoutRender()
    .item()
    .model { g, i -> g.flatItemBlock(i.get(), "closed_eyeblossom") }
    .build()
    .register() as BlockEntry<EyeBlossomBlock>

  val POTTED_EYE_BLOSSOM: BlockEntry<FlowerPotBlock> = REGISTRATE.blockPreset<FlowerPotBlock>("open_eyeblossom")
    .pottedBlock({ EYE_BLOSSOM.get() })
    .register()
  val POTTED_CLOSED_EYE_BLOSSOM: BlockEntry<FlowerPotBlock> =
    REGISTRATE.blockPreset<FlowerPotBlock>("closed_eyeblossom")
      .pottedBlock({ CLOSED_EYE_BLOSSOM.get() })
      .register()

  fun register() {
    REGISTRATE.buildBlocks()
  }
}