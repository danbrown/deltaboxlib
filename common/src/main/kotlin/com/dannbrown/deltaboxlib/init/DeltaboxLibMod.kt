package com.dannbrown.deltaboxlib.init

import com.dannbrown.deltaboxlib.content.block.FlammableBlock
import com.dannbrown.deltaboxlib.registrate.presets.StorageBlockPreset
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

  val IRON_BLOCK2 =
    StorageBlockPreset(REGISTRATE, "compiled_block", { Items.IRON_INGOT }, { Ingredient.of(Items.FLINT) }).create()
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
    .item()
    .itemTags(ItemTags.NON_FLAMMABLE_WOOD, ItemTags.WART_BLOCKS)
    .build()
    .blockstate { ctx, block -> ctx.bottomTopBlock(block.get(), "crate_bottom") }
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

  fun init() {
    REGISTRATE.buildRegistries()
  }
}