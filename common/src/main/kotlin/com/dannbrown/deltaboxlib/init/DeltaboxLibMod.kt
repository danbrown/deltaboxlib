package com.dannbrown.deltaboxlib.init

import com.dannbrown.deltaboxlib.content.block.*
import com.dannbrown.deltaboxlib.content.worldgen.tree.DeltaboxTreeGrower
import com.dannbrown.deltaboxlib.registrate.presets.blocks.StorageBlockPreset
import com.dannbrown.deltaboxlib.registrate.registry.BlockEntry
import com.dannbrown.deltaboxlib.registrate.registry.ItemEntry
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
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
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.TrapDoorBlock
import net.minecraft.world.level.block.WallBlock
import net.minecraft.world.level.block.state.properties.BlockSetType
import net.minecraft.world.level.block.state.properties.WoodType
import net.minecraft.world.level.material.MapColor

object DeltaboxLibMod {
  const val MOD_ID = "deltaboxlib"
  var REGISTRATE = DeltaboxRegistrate(MOD_ID)

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
    .strippable(ADAMANTIUM_BLOCK)
    .register()

  val IRON_BLOCK2 = REGISTRATE
    .blockPreset<Block>("compiled_block")
    .storageBlock({ Items.IRON_INGOT }, { Ingredient.of(Items.FLINT) })
    .copyFrom { Blocks.IRON_BLOCK }
    .toolAndTier(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_IRON_TOOL)
    .register()


  val ACAI_CRATE = REGISTRATE
    .block<RotatedPillarBlock>("acai_berries_crate")
    .lang("Acai Berries Crate AHA")
    .factory { c, p -> RotatedPillarBlock(p) }
    .strippable(BlockEntry.from(Blocks.ACACIA_LOG))
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

  val ADAMANTIUM_INGOT = REGISTRATE
    .item<Item>("adamantium_ingot")
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
    .saplingBlock({ DeltaboxTreeGrower.SAMPLE }) { blockState, _, _ -> blockState.`is`(BlockTags.DIRT) }
    .register()
  val POTTED_LEMON_SAPLING: BlockEntry<FlowerPotBlock> = REGISTRATE
    .blockPreset<FlowerPotBlock>("lemon")
    .pottedBlock(LEMON_SAPLING, "_sapling")
    .register()

  val SIMPLE_GRASS: BlockEntry<GenericGrassBlock> = REGISTRATE.blockPreset<GenericGrassBlock>("simple_grass")
    .grassBlock({ Items.WHEAT_SEEDS })
    .register()

  val POTTED_SIMPLE_GRASS: BlockEntry<FlowerPotBlock> = REGISTRATE
    .blockPreset<FlowerPotBlock>("simple_grass")
    .pottedBlock(SIMPLE_GRASS)
    .register()


  val SIMPLE_FLOWER: BlockEntry<GenericGrassBlock> = REGISTRATE.blockPreset<GenericGrassBlock>("simple_flower")
    .flowerBlock({ Items.WHEAT_SEEDS })
    .register()

  val POTTED_SIMPLE_FLOWER: BlockEntry<FlowerPotBlock> = REGISTRATE
    .blockPreset<FlowerPotBlock>("simple_flower")
    .pottedBlock(SIMPLE_FLOWER)
    .register()


  val TALL_SPARSE_DRY_GRASS: BlockEntry<GenericDoublePlantBlock> =
    REGISTRATE.blockPreset<GenericDoublePlantBlock>("sparse_dry_grass").createDoubleTallGrassBlock(
      { Items.BEETROOT_SEEDS },
      null,
      { blockState, _, _ -> blockState.`is`(BlockTags.SAND) })
      .color(MapColor.TERRACOTTA_YELLOW)
      .register()
  val SPARSE_DRY_GRASS: BlockEntry<GenericTallGrassBlock> =
    REGISTRATE.blockPreset<GenericTallGrassBlock>("sparse_dry_grass").createSmallTallGrassBlock(
      TALL_SPARSE_DRY_GRASS,
      { Items.BEETROOT_SEEDS },
      false,
      { blockState, _, _ -> blockState.`is`(BlockTags.SAND) })
      .color(MapColor.TERRACOTTA_YELLOW)
      .register()


  val PALE_OAK_LOG = REGISTRATE.blockPreset<RotatedPillarBlock>("pale_oak_log")
    .rotatedPillar()
    .copyFrom { Blocks.DARK_OAK_LOG }
    .register()

  val PALE_OAK_STAIRS = REGISTRATE.blockPreset<StairBlock>("pale_oak").stairs("pale_oak_planks", false, true)
    .copyFrom { Blocks.OAK_STAIRS }
    .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
    .register()

  val PALE_OAK_SLAB = REGISTRATE.blockPreset<SlabBlock>("pale_oak").slab("pale_oak_planks", false, true)
    .copyFrom { Blocks.OAK_STAIRS }
    .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
    .register()

  val ROSEATE_SANDSTONE_WALL = REGISTRATE.blockPreset<WallBlock>("roseate_sandstone").wall("roseate_sandstone", true)
    .copyFrom { Blocks.RED_SANDSTONE_WALL }
    .toolAndTier(BlockTags.MINEABLE_WITH_PICKAXE, null, true)
    .register()

  val PALE_OAK_FENCE = REGISTRATE.blockPreset<FenceBlock>("pale_oak").fence("pale_oak_planks")
    .copyFrom { Blocks.OAK_FENCE }
    .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
    .register()

  val PALE_OAK_FENCE_GATE =
    REGISTRATE.blockPreset<FenceGateBlock>("pale_oak").fenceGate("pale_oak_planks", WoodType.BAMBOO)
      .copyFrom { Blocks.OAK_FENCE }
      .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
      .register()

  val PALE_OAK_PRESSURE_PLATE =
    REGISTRATE.blockPreset<PressurePlateBlock>("pale_oak").pressurePlate("pale_oak_planks", BlockSetType.BIRCH)
      .copyFrom { Blocks.OAK_PRESSURE_PLATE }
      .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
      .register()

  val PALE_OAK_BUTTON =
    REGISTRATE.blockPreset<ButtonBlock>("pale_oak").button("pale_oak_planks", BlockSetType.BIRCH)
      .copyFrom { Blocks.OAK_BUTTON }
      .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
      .register()

  val PALE_OAK_TRAPDOOR =
    REGISTRATE.blockPreset<TrapDoorBlock>("pale_oak").woodenTrapdoor(BlockSetType.BIRCH)
      .copyFrom { Blocks.OAK_TRAPDOOR }
      .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
      .register()

  val PALE_OAK_DOOR =
    REGISTRATE.blockPreset<DoorBlock>("pale_oak").door(BlockSetType.BIRCH)
      .copyFrom { Blocks.OAK_DOOR }
      .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
      .register()


  fun init() {
    REGISTRATE.buildRegistries()
  }
}