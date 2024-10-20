package com.dannbrown.deltaboxlib.registry.generators.family


import com.dannbrown.deltaboxlib.registry.generators.BlockFamily
import com.dannbrown.deltaboxlib.registry.generators.BlockGenerator
import net.minecraft.core.BlockPos
import net.minecraft.tags.TagKey
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.grower.AbstractTreeGrower
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.WoodType
import net.minecraft.world.level.material.MapColor
import java.util.function.Supplier

/**
 * Returns a Stalk wood block family
 */
class StalkWoodBlockFamilySet(
  private val generator: BlockGenerator,
  private val _name: String,
  private val _sharedProps: (BlockBehaviour.Properties) -> BlockBehaviour.Properties = { p: BlockBehaviour.Properties -> p },
  private val _toolType: TagKey<Block>? = null,
  private val _toolTier: TagKey<Block>? = null,
  private val _color: MapColor? = null,
  private val _accentColor: MapColor? = null,
  private val _copyFrom: Supplier<Block> = Supplier { Blocks.STONE },
  private val _denyList: List<BlockFamily.Type> = mutableListOf(),
  woodType: WoodType,
  grower: AbstractTreeGrower,
  placeOn: ((blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos) -> Boolean)? = null
): AbstractBlockFamilySet() {
//  init{
//    val LOG_TAG_BLOCK = LibTags.modBlockTag(generator.registrate.modid,_name + "_log_blocks")
//    val LOG_TAG_ITEM = LibTags.modItemTag(generator.registrate.modid, _name + "_log_blocks")
//    val FORGE_LEAVES_TAG_BLOCK = LibTags.forgeBlockTag("leaves")
//    val FORGE_LEAVES_TAG_ITEM = LibTags.forgeItemTag("leaves")
//    val FORGE_STRIPPED_LOGS_TAG_BLOCK = LibTags.forgeBlockTag("stripped_logs")
//    val FORGE_STRIPPED_LOGS_TAG_ITEM = LibTags.forgeItemTag("stripped_logs")
//    // Stalks
//    _blockFamily.setVariant(BlockFamily.Type.STALK) {
//      generator.create<FlammableWallBlock>(_name + "_stalk")
//        .wallBlock(true, false)
//        .blockFactory { p -> FlammableWallBlock(p) }
//        .copyFrom({ Blocks.OAK_LOG })
//        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
//        .blockTags(listOf(LOG_TAG_BLOCK, BlockTags.LOGS))
//        .itemTags(listOf(LOG_TAG_ITEM, ItemTags.LOGS))
//        .transform { b ->
//          b
//            .loot(BlockLootPresets.dropItselfLoot())
//            .item(BlockItemFactory.fuelBlockItem(75))
//            .lang("${
//              _name.replace("_", " ")
//                .replaceFirstChar { it.uppercase() }
//            } Stalk")
//            .model(ItemModelPresets.bottomTopWallItem(_name + "_stalk"))
//            .tag(LOG_TAG_ITEM)
//            .build()
//        }
//        .noItem()
//        .register()
//    }
//
//    _blockFamily.setVariant(BlockFamily.Type.STRIPPED_STALK) {
//      generator.create<FlammableWallBlock>("stripped_$_name" + "_stalk")
//        .wallBlock(true, false)
//        .blockFactory { p -> FlammableWallBlock(p) }
//        .copyFrom({ Blocks.STRIPPED_OAK_LOG })
//        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
//        .blockTags(listOf(LOG_TAG_BLOCK, BlockTags.LOGS))
//        .transform { b ->
//          b
//            .loot(BlockLootPresets.dropItselfLoot())
//            .item(BlockItemFactory.fuelBlockItem(75))
//            .lang("Stripped ${
//              _name.replace("_", " ")
//                .replaceFirstChar { it.uppercase() }
//            } Stalk")
//            .model(ItemModelPresets.bottomTopWallItem("stripped_$_name" + "_stalk"))
//            .tag(LOG_TAG_ITEM)
//            .build()
//        }
//        .register()
//    }
//    // Logs
//    _blockFamily.setVariant(BlockFamily.Type.LOG) {
//      generator.create<FlammablePillarBlock>(_name + "_logs")
//        .rotatedPillarBlock(_name + "_logs_top", _name + "_logs")
//        .blockFactory { p -> FlammablePillarBlock(p) }
//        .copyFrom({ Blocks.OAK_LOG })
//        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
////        .fromFamily(Blocks.OAK_LOG, sharedProps, color, toolType, toolTier, false)
//        .blockTags(listOf(BlockTags.LOGS, LOG_TAG_BLOCK, BlockTags.LOGS_THAT_BURN))
//        .itemTags(listOf(ItemTags.LOGS, LOG_TAG_ITEM, ItemTags.LOGS_THAT_BURN))
//        .recipe { c, p ->
//          RecipePresets.polishedCraftingRecipe(c, p, { DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.STALK]!!.get()) }, 1)
//        }
//        .register()
//    }
//
//    _blockFamily.setVariant(BlockFamily.Type.WOOD) {
//      generator.create<FlammablePillarBlock>(_name + "_wood")
//        .rotatedPillarBlock(_name + "_logs", _name + "_logs")
//        .blockFactory { p -> FlammablePillarBlock(p) }
//        .copyFrom({ Blocks.OAK_WOOD })
//        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
//        //        .fromFamily(Blocks.OAK_WOOD, sharedProps, accentColor, toolType, toolTier, false)
//        .blockTags(listOf(BlockTags.LOGS, LOG_TAG_BLOCK, BlockTags.LOGS_THAT_BURN))
//        .itemTags(listOf(ItemTags.LOGS, LOG_TAG_ITEM, ItemTags.LOGS_THAT_BURN))
//        .recipe { c, p ->
//          RecipePresets.polishedCraftingRecipe(c, p, { DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.LOG]!!.get()) }, 3)
//        }
//        .register()
//    }
//    // Stripped Logs
//    _blockFamily.setVariant(BlockFamily.Type.STRIPPED_LOG) {
//      generator.create<FlammablePillarBlock>("stripped_$_name" + "_logs")
//        .rotatedPillarBlock("stripped_$_name" + "_logs_top", "stripped_$_name" + "_logs")
//        .blockFactory { p -> FlammablePillarBlock(p) }
//        .copyFrom({ Blocks.STRIPPED_OAK_LOG })
//        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
////        .fromFamily(Blocks.STRIPPED_OAK_LOG, sharedProps, color, toolType, toolTier, false)
//        .blockTags(listOf(BlockTags.LOGS, LOG_TAG_BLOCK, FORGE_STRIPPED_LOGS_TAG_BLOCK, BlockTags.LOGS_THAT_BURN))
//        .itemTags(listOf(ItemTags.LOGS, LOG_TAG_ITEM, FORGE_STRIPPED_LOGS_TAG_ITEM, ItemTags.LOGS_THAT_BURN))
//        .recipe { c, p ->
//          RecipePresets.polishedCraftingRecipe(c, p, { DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.STRIPPED_STALK]!!.get()) }, 1)
//        }
//        .register()
//    }
//
//    _blockFamily.setVariant(BlockFamily.Type.STRIPPED_WOOD) {
//      generator.create<FlammablePillarBlock>("stripped_$_name" + "_wood")
//        .rotatedPillarBlock("stripped_$_name" + "_logs", "stripped_$_name" + "_logs")
//        .blockFactory { p -> FlammablePillarBlock(p) }
//        .copyFrom({ Blocks.STRIPPED_OAK_WOOD })
//        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
////        .fromFamily(Blocks.STRIPPED_OAK_WOOD, sharedProps, accentColor, toolType, toolTier, false)
//        .blockTags(listOf(BlockTags.LOGS, LOG_TAG_BLOCK, FORGE_STRIPPED_LOGS_TAG_BLOCK, BlockTags.LOGS_THAT_BURN))
//        .itemTags(listOf(ItemTags.LOGS, LOG_TAG_ITEM, FORGE_STRIPPED_LOGS_TAG_ITEM, ItemTags.LOGS_THAT_BURN))
//        .recipe { c, p ->
//          RecipePresets.polishedCraftingRecipe(c, p, { DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.STRIPPED_LOG]!!.get()) }, 3)
//        }
//        .register()
//    }
//
//    _blockFamily.setVariant(BlockFamily.Type.SAPLING) {
//      generator.create<GenericSaplingBlock>(_name + "_sapling")
//        .blockFactory { p -> GenericSaplingBlock(grower, p, placeOn) }
//        .blockTags(listOf(BlockTags.SAPLINGS))
//        .itemTags(listOf(ItemTags.SAPLINGS))
//        .copyFrom { Blocks.OAK_SAPLING }
//        .properties { p ->
//          p.mapColor(_accentColor)
//            .sound(SoundType.GRASS)
//            .strength(0.0f)
//            .randomTicks()
//            .noCollission()
//            .noOcclusion()
//        }
//        .transform { t ->
//          t
//            .blockstate(BlockstatePresets.simpleCrossBlock(_name + "_sapling"))
//            .item()
//            .model(ItemModelPresets.simpleLayerItem(_name + "_sapling"))
//            .build()
//        }
//        .register()
//    }
//
//    _blockFamily.setVariant(BlockFamily.Type.POTTED_SAPLING) {
//      generator.create<FlowerPotBlock>("potted_$_name" + "_sapling")
//        .blockFactory { p ->
//          FlowerPotBlock(
//            { Blocks.FLOWER_POT as FlowerPotBlock },
//            { _blockFamily.blocks[BlockFamily.Type.SAPLING]!!.get() },
//            p
//          )
//        }
//        .copyFrom({ Blocks.POTTED_POPPY })
//        .noItem()
//        .properties { p ->
//          p.mapColor(_color)
//            .noOcclusion()
//        }
//        .transform { t ->
//          t
//            .blockstate(BlockstatePresets.pottedPlantBlock(_name + "_sapling"))
//            .loot(BlockLootPresets.pottedPlantLoot { _blockFamily.blocks[BlockFamily.Type.SAPLING]!!.get() })
//        }
//        .register()
//    }
//
//    _blockFamily.setVariant(BlockFamily.Type.LEAVES) {
//      generator.create<FlammableLeavesBlock>(_name + "_leaves")
//        .blockFactory { p -> FlammableLeavesBlock(p, 60, 30) }
//        .color(MapColor.COLOR_GREEN)
//        .copyFrom({ Blocks.OAK_LEAVES })
//        .properties { p ->
//          p
//            .randomTicks()
//            .noOcclusion()
//            .isSuffocating { s, b, p -> false }
//            .isViewBlocking { s, b, p -> false }
//            .isRedstoneConductor { s, b, p -> false }
//            .ignitedByLava()
//        }
//        .blockTags(listOf(BlockTags.LEAVES, FORGE_LEAVES_TAG_BLOCK))
//        .itemTags(listOf(ItemTags.LEAVES, FORGE_LEAVES_TAG_ITEM))
//        .transform { t ->
//          t
//            .blockstate(BlockstatePresets.simpleTransparentBlock(_name + "_leaves"))
//            .loot(BlockLootPresets.leavesLoot { _blockFamily.blocks[BlockFamily.Type.SAPLING]!!.get() })
//        }
//        .register()
//    }
//    // Main Block
//    _blockFamily.setVariant(BlockFamily.Type.MAIN) {
//      generator.create<FlammableBlock>(_name + "_planks")
//        .blockFactory { p -> FlammableBlock(p, 20, 5) }
////        .fromFamily(Blocks.OAK_PLANKS, sharedProps, accentColor, toolType, toolTier, false)
//        .copyFrom { Blocks.OAK_PLANKS }
//        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
//        .blockTags(listOf(BlockTags.PLANKS))
//        .itemTags(listOf(ItemTags.PLANKS))
//        .recipe { c, p ->
//          RecipePresets.directShapelessRecipe(c, p, { DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.STALK]!!.get()) }, 1)
//          RecipePresets.directShapelessRecipe(c, p, { DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.STRIPPED_STALK]!!.get()) }, 1)
//          RecipePresets.directShapelessRecipe(c, p, { DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.LOG]!!.get()) }, 4)
//          RecipePresets.directShapelessRecipe(c, p, { DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.STRIPPED_LOG]!!.get()) }, 4)
//          RecipePresets.directShapelessRecipe(c, p, { DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.WOOD]!!.get()) }, 4)
//          RecipePresets.directShapelessRecipe(c, p, { DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.STRIPPED_WOOD]!!.get()) }, 4)
//          // make the boat as oak, as I am sleep-deprived and bored enough to not do it
//          ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, Items.OAK_BOAT, 1)
//            .define('X', c.get())
//            .pattern("X X")
//            .pattern("XXX")
//            .unlockedBy("has_" + p.safeName(c.get()), RegistrateRecipeProvider.has(c.get()))
//            .save(p, p.safeId(ResourceLocation("oak_boat_from_" + p.safeName(c.get()))))
//        }
//        .register()
//    }
//    // Stairs
//    _blockFamily.setVariant(BlockFamily.Type.STAIRS) {
//      generator.create<StairBlock>(_name)
//        .textureName(_name + "_planks")
////        .fromFamily(Blocks.OAK_STAIRS, sharedProps, accentColor, toolType, toolTier, false)
//        .copyFrom({ Blocks.OAK_STAIRS })
//        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
//        .stairsBlock({ _blockFamily.blocks[BlockFamily.Type.MAIN]!!.defaultState }, false, true)
//        .blockTags(listOf(BlockTags.STAIRS, BlockTags.WOODEN_STAIRS))
//        .itemTags(listOf(ItemTags.STAIRS, ItemTags.WOODEN_STAIRS))
//        .recipe { c, p ->
//          RecipePresets.stairsCraftingRecipe(c, p) {
//            DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.MAIN]!!.get()
//              .asItem())
//          }
//        }
//        .register()
//    }
//    // Slab
//    _blockFamily.setVariant(BlockFamily.Type.SLAB) {
//      generator.create<SlabBlock>(_name)
//        .textureName(_name + "_planks")
//        .slabBlock(false, true)
////        .fromFamily(Blocks.OAK_SLAB, sharedProps, accentColor, toolType, toolTier, false)
//        .copyFrom({ Blocks.OAK_SLAB })
//        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
//        .recipe { c, p ->
//          RecipePresets.slabRecycleRecipe(c, p) {
//            _blockFamily.blocks[BlockFamily.Type.MAIN]!!.get()
//              .asItem()
//          }
//          RecipePresets.slabCraftingRecipe(c, p) {
//            DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.MAIN]!!.get()
//              .asItem())
//          }
//        }
//        .register()
//    }
//    // Fence
//    _blockFamily.setVariant(BlockFamily.Type.FENCE) {
//      generator.create<FenceBlock>(_name)
//        .textureName(_name + "_planks")
//        .fenceBlock(true)
//        .copyFrom({ Blocks.OAK_FENCE })
//        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
//        .blockTags(listOf(BlockTags.FENCES, BlockTags.WOODEN_FENCES))
//        .itemTags(listOf(ItemTags.FENCES, ItemTags.WOODEN_FENCES))
//        .recipe { c, p ->
//          RecipePresets.fenceCraftingRecipe(c, p) {
//            DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.MAIN]!!.get()
//              .asItem())
//          }
//        }
//        .register()
//    }
//    // Fence Gate
//    _blockFamily.setVariant(BlockFamily.Type.FENCE_GATE) {
//      generator.create<FenceGateBlock>(_name)
//        .textureName(_name + "_planks")
//        .fenceGateBlock(woodType)
//        .copyFrom({ Blocks.OAK_FENCE_GATE })
//        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
//        .blockTags(listOf(BlockTags.FENCE_GATES))
//        .itemTags(listOf(ItemTags.FENCE_GATES))
//        .recipe { c, p ->
//          RecipePresets.fenceGateCraftingRecipe(c, p) {
//            DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.MAIN]!!.get()
//              .asItem())
//          }
//        }
//        .register()
//    }
//    // Pressure Plate
//    _blockFamily.setVariant(BlockFamily.Type.PRESSURE_PLATE) {
//      generator.create<PressurePlateBlock>(_name)
//        .textureName(_name + "_planks")
//        .pressurePlateBlock(BlockSetType.OAK)
//        .copyFrom({ Blocks.OAK_PRESSURE_PLATE })
//        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
////        .fromFamily(Blocks.OAK_PRESSURE_PLATE, sharedProps, accentColor, toolType, toolTier, false)
//        .blockTags(listOf(BlockTags.WOODEN_PRESSURE_PLATES))
//        .itemTags(listOf(ItemTags.WOODEN_PRESSURE_PLATES))
//        .recipe { c, p ->
//          RecipePresets.pressurePlateCraftingRecipe(c, p) {
//            DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.MAIN]!!.get()
//              .asItem())
//          }
//        }
//        .register()
//    }
//    // Button
//    _blockFamily.setVariant(BlockFamily.Type.BUTTON) {
//      generator.create<ButtonBlock>(_name)
//        .textureName(_name + "_planks")
//        .buttonBlock(BlockSetType.OAK, true)
//        .copyFrom({ Blocks.OAK_BUTTON })
//        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
////        .fromFamily(Blocks.OAK_BUTTON, sharedProps, accentColor, toolType, toolTier, false)
//        .recipe { c, p ->
//          RecipePresets.directShapelessRecipe(c,
//            p,
//            {
//              DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.MAIN]!!.get()
//                .asItem())
//            },
//            1)
//        }
//        .register()
//    }
//    // Door
//    _blockFamily.setVariant(BlockFamily.Type.DOOR) {
//      generator.create<DoorBlock>(_name)
//        .woodenDoorBlock(BlockSetType.OAK)
//        .copyFrom({ Blocks.OAK_DOOR })
//        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
//        .color(_accentColor!!)
//        .recipe { c, p ->
//          RecipePresets.doorCraftingRecipe(c, p) {
//            DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.MAIN]!!.get()
//              .asItem())
//          }
//        }
//        .transform { b ->
//          b
//            .loot(BlockLootPresets.doorLoot())
//        }
//        .register()
//    }
//    // Trapdoor
////    blockFamily.setVariant(BlockFamily.Type.TRAPDOOR) {
////      generator.create)<TrapDoorBlock>(name)
////        .woodenTrapdoorBlock({ DataIngredient.items(blockFamily.blocks[BlockFamily.Type.MAIN]!!.get()) }, BlockSetType.OAK)
////        .copyFrom({ Blocks.OAK_TRAPDOOR })
////        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
////        .color(accentColor)
////        .properties { p ->
////          p.sound(SoundType.WOOD)
////            .noOcclusion()
////        }
////        .register()
////    }
//    // Sign
//    _blockFamily.setVariant(BlockFamily.Type.WALL_SIGN) {
//      generator.create<GenericWallSignBlock>(_name + "_wall_sign")
//        .noItem()
//        .copyFrom({ Blocks.OAK_WALL_SIGN })
//        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
//        .blockFactory { p -> GenericWallSignBlock(p, woodType) { _blockFamily.blocks[BlockFamily.Type.SIGN]!!.get() } }
//        .color(_accentColor!!)
//        .properties { p ->
//          p.strength(1.0F)
//            .sound(SoundType.WOOD)
//            .noOcclusion()
//        }
//        .blockTags(listOf(BlockTags.WALL_SIGNS, BlockTags.SIGNS))
//        .transform { t ->
//          t
//            .blockstate(BlockstatePresets.noBlockState())
//            .loot(BlockLootPresets.dropOtherLoot { _blockFamily.blocks[BlockFamily.Type.SIGN]!!.get() })
//        }
//        .register()
//    }
//
//    _blockFamily.setVariant(BlockFamily.Type.SIGN) {
//      generator.create<GenericStandingSignBlock>(_name + "_sign")
//        .blockFactory { p -> GenericStandingSignBlock(p, woodType) }
//        .copyFrom { Blocks.OAK_SIGN }
//        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
//        .color(_accentColor!!)
//        .noItem()
//        .recipe { c, p ->
//          RecipePresets.signCraftingRecipe(c, p) {
//            DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.MAIN]!!.get()
//              .asItem())
//          }
//        }
//        .transform { b ->
//          b
//            .properties { p ->
//              p.strength(1.0F)
//                .sound(SoundType.WOOD)
//                .noOcclusion()
//            }
//            .tag(BlockTags.STANDING_SIGNS, BlockTags.SIGNS)
//            .blockstate { c, p ->
//              val signModel: ModelFile = p.models()
//                .sign(c.name, p.modLoc("block/${_name + "_planks"}"))
//              p.simpleBlock(c.get() as StandingSignBlock, signModel)
//              p.simpleBlock(_blockFamily.blocks[BlockFamily.Type.WALL_SIGN]!!.get() as WallSignBlock, signModel)
//            }
//            .item { b, p -> GenericSignItem(p.stacksTo(16), b, _blockFamily.blocks[BlockFamily.Type.WALL_SIGN]!!.get()) }
//            .tag(ItemTags.SIGNS)
//            .model { c, p ->
//              p.withExistingParent(c.name, p.mcLoc("item/generated"))
//                .texture("layer0", p.modLoc("item/${c.name}"))
//            }
//            .build()
//        }
//        .register()
//    }
//    // DONE: BLOCK, LOG, STRIPPED LOG, WOOD, STRIPPED WOOD
//    // DONE: STAIRS, SLAB, FENCE, FENCE GATE, BUTTON, PRESSURE PLATE
//    // DONE: STALK, STRIPPED STALK, LEAVES, DOOR, TRAPDOOR, SIGN
//    // TODO: SAPLING, WINDOW, WINDOW PANE, BOAT, CHEST BOAT
//  }
}