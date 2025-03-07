package com.dannbrown.deltaboxlib.init

import com.dannbrown.deltaboxlib.content.block.FlammableBlock
import com.dannbrown.deltaboxlib.content.block.GenericSaplingBlock
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
import net.minecraft.world.level.block.FlowerPotBlock
import net.minecraft.world.level.block.RotatedPillarBlock

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

//  val SIMPLE_GRASS: BlockEntry<GenericGrassBlock> = BLOCKS.grassBlock("simple_grass", { Items.WHEAT_SEEDS })
//    .register()
//  val SIMPLE_FLOWER: BlockEntry<TrailFlowerBlock> =
//    BLOCKS.create<TrailFlowerBlock>("simple_flower")
////      .flowerBlock("simple_flower", true, true, true, { blockState, _, _ -> blockState.`is`(BlockTags.SAND) })
//      .blockFactory { p -> TrailFlowerBlock(p) }
//      .copyFrom { Blocks.POPPY }
//      .properties { p ->
//        p.sound(SoundType.GRASS)
//          .strength(0.0f)
//          .noCollission()
//          .noOcclusion()
//          .randomTicks()
//      }
//      .blockstate(BlockstatePresets.simpleCrossBlock("simple_flower"))
//      .loot(BlockLootPresets.dropItselfLoot())
//      .transform { t ->
//        t
//          .item()
//          .model(ItemModelPresets.simpleLayerItem("simple_flower"))
//          .build()
//      }
//      .cutoutRender()
//      .register()
//
//  val POTTED_SIMPLE_GRASS: BlockEntry<FlowerPotBlock> = BLOCKS.pottedBlock("simple_grass", SIMPLE_GRASS)
//    .register()
//  val POTTED_SIMPLE_FLOWER: BlockEntry<FlowerPotBlock> = BLOCKS.pottedBlock("simple_flower", SIMPLE_FLOWER)
//    .register()


  val PALE_OAK_LOG = REGISTRATE.blockPreset<RotatedPillarBlock>("pale_oak_log")
    .rotatedPillar()
    .copyFrom { Blocks.DARK_OAK_LOG }
    .register()


  fun init() {
    REGISTRATE.buildRegistries()
  }
}