package com.dannbrown.deltaboxlib.registrate.presets.family

import com.dannbrown.deltaboxlib.content.block.FlammableBlock
import com.dannbrown.deltaboxlib.content.block.FlammablePillarBlock
import com.dannbrown.deltaboxlib.content.block.GenericSaplingBlock
import com.dannbrown.deltaboxlib.content.entity.boat.BaseBoatEntity
import com.dannbrown.deltaboxlib.content.entity.boat.BaseBoatRenderer
import com.dannbrown.deltaboxlib.content.entity.boat.BaseChestBoatEntity
import com.dannbrown.deltaboxlib.content.item.BoatItem
import com.dannbrown.deltaboxlib.content.worldgen.tree.DeltaboxTreeGrower
import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.builders.BlockBuilderContext
import com.dannbrown.deltaboxlib.registrate.registry.ItemEntry
import com.dannbrown.deltaboxlib.registrate.types.BlockPropertiesFactory
import com.dannbrown.deltaboxlib.registrate.util.DataIngredient
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import net.minecraft.core.BlockPos
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.item.HangingSignItem
import net.minecraft.world.item.SignItem
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.ButtonBlock
import net.minecraft.world.level.block.CeilingHangingSignBlock
import net.minecraft.world.level.block.DoorBlock
import net.minecraft.world.level.block.FenceBlock
import net.minecraft.world.level.block.FenceGateBlock
import net.minecraft.world.level.block.FlowerPotBlock
import net.minecraft.world.level.block.PressurePlateBlock
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.StandingSignBlock
import net.minecraft.world.level.block.TrapDoorBlock
import net.minecraft.world.level.block.WallHangingSignBlock
import net.minecraft.world.level.block.WallSignBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockSetType
import net.minecraft.world.level.block.state.properties.WoodType
import net.minecraft.world.level.material.MapColor
import java.util.function.BiFunction
import java.util.function.Supplier

/**
 * Returns a wood block family
 */
class WoodBlockFamilySet(
  private val registrate: AbstractDeltaboxRegistrate,
  private val _name: String,
  private val _sharedProps: BlockPropertiesFactory = BiFunction { c: BlockBuilderContext<out Block>, p: BlockBehaviour.Properties -> p },
  private val _toolType: TagKey<Block>? = null,
  private val _toolTier: TagKey<Block>? = null,
  private val _color: MapColor? = null,
  private val _accentColor: MapColor? = null,
  private val _copyFrom: Supplier<Block> = Supplier { Blocks.STONE },
  private val _denyList: List<BlockFamily.Type> = mutableListOf(),
  woodType: WoodType,
  setType: BlockSetType,
  grower: DeltaboxTreeGrower,
  placeOn: ((blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos) -> Boolean)? = null
) : AbstractBlockFamilySet() {

  var BOAT_ENTITY: Supplier<EntityType<BaseBoatEntity>>? = null
  var CHEST_BOAT_ENTITY: Supplier<EntityType<BaseChestBoatEntity>>? = null
  var BOAT_ITEM: ItemEntry<BoatItem>? = null
  var CHEST_BOAT_ITEM: ItemEntry<BoatItem>? = null

  class WoodFamilyComponents(
    val blockFamily: BlockFamily,
    val boatEntity: Supplier<EntityType<BaseBoatEntity>>,
    val chestBoatEntity: Supplier<EntityType<BaseChestBoatEntity>>,
    val boatItem: ItemEntry<BoatItem>,
    val chestBoatItem: ItemEntry<BoatItem>
  ) {}

  init {
    val LOG_TAG_BLOCK = DeltaboxUtil.TAGS.modBlockTag(registrate.modId, _name + "_log_blocks")
    val LOG_TAG_ITEM = DeltaboxUtil.TAGS.modItemTag(registrate.modId, _name + "_log_blocks")
    val FORGE_LEAVES_TAG_BLOCK = DeltaboxUtil.TAGS.modloaderBlockTag("leaves")
    val FORGE_LEAVES_TAG_ITEM = DeltaboxUtil.TAGS.modloaderItemTag("leaves")
    val FORGE_STRIPPED_LOGS_TAG_BLOCK = DeltaboxUtil.TAGS.modloaderBlockTag("stripped_logs")
    val FORGE_STRIPPED_LOGS_TAG_ITEM = DeltaboxUtil.TAGS.modloaderItemTag("stripped_logs")
    // Logs

    _blockFamily.setVariant(BlockFamily.Type.LOG) {
      registrate.blockPreset<FlammablePillarBlock>(_name + "_log")
        .rotatedPillar()
        .factory { c, p ->
          FlammablePillarBlock(
            p,
            c.flammabilityBurnChance,
            c.flammabilitySpreadChance
          )
        }
        .flammable()
        .strippable { _blockFamily.blocks[BlockFamily.Type.STRIPPED_LOG]!!.get() }
        .copyFrom { Blocks.OAK_LOG }
        .color(_color ?: MapColor.WOOD)
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .blockTags(BlockTags.LOGS, LOG_TAG_BLOCK, BlockTags.LOGS_THAT_BURN)
        .itemTags(ItemTags.LOGS, LOG_TAG_ITEM, ItemTags.LOGS_THAT_BURN)
        .register()
    }
//
    _blockFamily.setVariant(BlockFamily.Type.WOOD) {
      registrate.blockPreset<FlammablePillarBlock>(_name + "_wood").rotatedPillar(_name + "_log", _name + "_log")
        .factory { c, p ->
          FlammablePillarBlock(
            p,
            c.flammabilityBurnChance,
            c.flammabilitySpreadChance
          )
        }
        .flammable()
        .strippable { _blockFamily.blocks[BlockFamily.Type.STRIPPED_WOOD]!!.get() }
        .copyFrom { Blocks.OAK_WOOD }
        .color(_color ?: MapColor.WOOD)
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .blockTags(BlockTags.LOGS, LOG_TAG_BLOCK, BlockTags.LOGS_THAT_BURN)
        .itemTags(ItemTags.LOGS, LOG_TAG_ITEM, ItemTags.LOGS_THAT_BURN)
        .recipe { c, p ->
          c.polishedCraftingRecipe(
            { p.get() },
            { DataIngredient(_blockFamily.blocks[BlockFamily.Type.LOG]!!.get()) },
            3
          )
        }
        .register()
    }
    // Stripped Logs
    _blockFamily.setVariant(BlockFamily.Type.STRIPPED_LOG) {
      registrate.blockPreset<FlammablePillarBlock>("stripped_$_name" + "_log")
        .rotatedPillar()
        .factory { c, p -> FlammablePillarBlock(p, c.flammabilityBurnChance, c.flammabilitySpreadChance) }
        .flammable()
        .copyFrom { Blocks.STRIPPED_OAK_LOG }
        .color(_accentColor ?: MapColor.WOOD)
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .blockTags(
          BlockTags.LOGS,
          LOG_TAG_BLOCK,
          *FORGE_STRIPPED_LOGS_TAG_BLOCK.toTypedArray(),
          BlockTags.LOGS_THAT_BURN
        )
        .itemTags(
          ItemTags.LOGS,
          LOG_TAG_ITEM,
          *FORGE_STRIPPED_LOGS_TAG_ITEM.toTypedArray(),
          ItemTags.LOGS_THAT_BURN
        )
        .register()
    }

    _blockFamily.setVariant(BlockFamily.Type.STRIPPED_WOOD) {
      registrate.blockPreset<FlammablePillarBlock>("stripped_$_name" + "_wood")
        .rotatedPillar(
          "stripped_$_name" + "_log",
          "stripped_$_name" + "_log"
        )
        .factory { c, p -> FlammablePillarBlock(p, c.flammabilityBurnChance, c.flammabilitySpreadChance) }
        .flammable()
        .copyFrom { Blocks.STRIPPED_OAK_WOOD }
        .color(_accentColor ?: MapColor.WOOD)
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .blockTags(
          BlockTags.LOGS,
          LOG_TAG_BLOCK,
          *FORGE_STRIPPED_LOGS_TAG_BLOCK.toTypedArray(),
          BlockTags.LOGS_THAT_BURN
        )
        .itemTags(
          ItemTags.LOGS,
          LOG_TAG_ITEM,
          *FORGE_STRIPPED_LOGS_TAG_ITEM.toTypedArray(),
          ItemTags.LOGS_THAT_BURN
        )
        .recipe { c, p ->
          c.polishedCraftingRecipe(
            { p.get() },
            { DataIngredient(_blockFamily.blocks[BlockFamily.Type.STRIPPED_LOG]!!.get()) },
            3
          )
        }
        .register()
    }

    _blockFamily.setVariant(BlockFamily.Type.SAPLING) {
      registrate.blockPreset<GenericSaplingBlock>(_name).saplingBlock(grower, placeOn).register()
    }

    _blockFamily.setVariant(BlockFamily.Type.POTTED_SAPLING) {
      registrate.blockPreset<FlowerPotBlock>(_name)
        .pottedBlock({ _blockFamily.blocks[BlockFamily.Type.SAPLING]!!.get() }, "_sapling").register()
    }

    if (!_denyList.contains(BlockFamily.Type.LEAVES)) {
      _blockFamily.setVariant(BlockFamily.Type.LEAVES) {
        registrate.blockPreset<FlowerPotBlock>(_name).leaves(
          { _blockFamily.blocks[BlockFamily.Type.SAPLING]!!.get() as GenericSaplingBlock })
          .blockTags(BlockTags.LEAVES, *FORGE_LEAVES_TAG_BLOCK.toTypedArray(), BlockTags.MINEABLE_WITH_HOE)
          .itemTags(ItemTags.LEAVES, *FORGE_LEAVES_TAG_ITEM.toTypedArray())
          .biomeColors()
          .register()
      }
    }
    // Main Block
    _blockFamily.setVariant(BlockFamily.Type.MAIN) {
      registrate.block<FlammableBlock>(_name + "_planks")
        .factory { c, p -> FlammableBlock(p, c.flammabilityBurnChance, c.flammabilitySpreadChance) }
        .flammable(20, 5)
        .copyFrom { Blocks.OAK_PLANKS }
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .blockTags(BlockTags.PLANKS)
        .itemTags(ItemTags.PLANKS)
        .recipe { c, p ->
          c.directShapelessRecipe(
            { p.get() },
            { DataIngredient(_blockFamily.blocks[BlockFamily.Type.LOG]!!.get()) },
            RecipeCategory.BUILDING_BLOCKS,
            4,
            "_from_log"
          )
          c.directShapelessRecipe(
            { p.get() },
            { DataIngredient(_blockFamily.blocks[BlockFamily.Type.STRIPPED_LOG]!!.get()) },
            RecipeCategory.BUILDING_BLOCKS,
            4,
            "_from_stripped_log"
          )
          c.directShapelessRecipe(
            { p.get() },
            { DataIngredient(_blockFamily.blocks[BlockFamily.Type.WOOD]!!.get()) },
            RecipeCategory.BUILDING_BLOCKS,
            4,
            "_from_wood"
          )
          c.directShapelessRecipe(
            { p.get() },
            { DataIngredient(_blockFamily.blocks[BlockFamily.Type.STRIPPED_WOOD]!!.get()) },
            RecipeCategory.BUILDING_BLOCKS,
            4,
            "_from_stripped_wood"
          )
        }
        .register()
    }
    // Stairs
    _blockFamily.setVariant(BlockFamily.Type.STAIRS) {
      registrate.blockPreset<StairBlock>(_name).stairs(_name + "_planks", false, true)
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .color(_accentColor ?: MapColor.WOOD)
        .recipe { c, p ->
          c.stairsCraftingRecipe({ p.get() }) {
            DataIngredient(_blockFamily.blocks[BlockFamily.Type.MAIN]!!.get().asItem())
          }
        }
        .register()
    }
    // Slab
    _blockFamily.setVariant(BlockFamily.Type.SLAB) {
      registrate.blockPreset<SlabBlock>(_name).slab(_name + "_planks", false, true)
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .color(_accentColor ?: MapColor.WOOD)
        .recipe { c, p ->
          c.slabCraftingRecipe({ p.get() }) {
            DataIngredient(_blockFamily.blocks[BlockFamily.Type.MAIN]!!.get().asItem())
          }
        }
        .register()
    }
    // Fence
    _blockFamily.setVariant(BlockFamily.Type.FENCE) {
      registrate.blockPreset<FenceBlock>(_name).fence(_name + "_planks", true)
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .color(_accentColor ?: MapColor.WOOD)
        .recipe { c, p ->
          c.fenceCraftingRecipe({ p.get() }) {
            DataIngredient(_blockFamily.blocks[BlockFamily.Type.MAIN]!!.get().asItem())
          }
        }
        .register()
    }
    // Fence Gate
    _blockFamily.setVariant(BlockFamily.Type.FENCE_GATE) {
      registrate.blockPreset<FenceGateBlock>(_name).fenceGate(_name + "_planks", woodType)
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .color(_accentColor ?: MapColor.WOOD)
        .recipe { c, p ->
          c.fenceGateCraftingRecipe({ p.get() }) {
            DataIngredient(_blockFamily.blocks[BlockFamily.Type.MAIN]!!.get().asItem())
          }
        }
        .register()
    }
    // Pressure Plate
    _blockFamily.setVariant(BlockFamily.Type.PRESSURE_PLATE) {
      registrate.blockPreset<PressurePlateBlock>(_name).pressurePlate(_name + "_planks", setType, true)
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .color(_accentColor ?: MapColor.WOOD)
        .recipe { c, p ->
          c.pressurePlateCraftingRecipe({ p.get() }) {
            DataIngredient(_blockFamily.blocks[BlockFamily.Type.MAIN]!!.get().asItem())
          }
        }
        .register()
    }
    // Button
    _blockFamily.setVariant(BlockFamily.Type.BUTTON) {
      registrate.blockPreset<ButtonBlock>(_name).button(_name + "_planks", setType, true)
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .color(_accentColor ?: MapColor.WOOD)
        .recipe { c, p ->
          c.directShapelessRecipe(
            { p.get() },
            { DataIngredient(_blockFamily.blocks[BlockFamily.Type.MAIN]!!.get()) },
            RecipeCategory.BUILDING_BLOCKS,
            1
          )
        }
        .register()
    }
    // Door
    _blockFamily.setVariant(BlockFamily.Type.DOOR) {
      registrate.blockPreset<DoorBlock>(_name).door(setType, true)
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .color(_accentColor ?: MapColor.WOOD)
        .recipe { c, p ->
          c.doorCraftingRecipe({ p.get() }) {
            DataIngredient(_blockFamily.blocks[BlockFamily.Type.MAIN]!!.get().asItem())
          }
        }
        .register()
    }
    // Trapdoor
    _blockFamily.setVariant(BlockFamily.Type.TRAPDOOR) {
      registrate.blockPreset<TrapDoorBlock>(_name).woodenTrapdoor(setType, true)
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .color(_accentColor ?: MapColor.WOOD)
        .recipe { c, p ->
          c.trapdoorCraftingRecipe({ p.get() }) {
            DataIngredient(_blockFamily.blocks[BlockFamily.Type.MAIN]!!.get().asItem())
          }
        }
        .register()
    }

    // Wall Sign
    _blockFamily.setVariant(BlockFamily.Type.WALL_SIGN) {
      registrate.block<WallSignBlock>(_name + "_wall_sign")
        .copyFrom { Blocks.OAK_WALL_SIGN }
        .factory { c, p -> WallSignBlock(p, woodType) }
        .properties { c, p -> p.strength(1.0F).sound(SoundType.WOOD).noOcclusion() }
        .cutoutRender()
        .color(_accentColor ?: MapColor.WOOD)
        .blockTags(BlockTags.WALL_SIGNS, BlockTags.SIGNS)
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .blockstate { g, b -> g.simpleParticleOnly(b.get(), _name + "_planks") }
        .loot { g, b -> g.dropAnother(b.get(), _blockFamily.blocks[BlockFamily.Type.SIGN]!!.get()) }
        .noItem()
        .lang(DeltaboxUtil.asName(_name + "_sign"))
        .register()
    }

    // Sign
    _blockFamily.setVariant(BlockFamily.Type.SIGN) {
      registrate.block<StandingSignBlock>(_name + "_sign")
        .copyFrom { Blocks.OAK_SIGN }
        .factory { c, p -> StandingSignBlock(p, woodType) }
        .properties { c, p -> p.strength(1.0F).sound(SoundType.WOOD).noOcclusion() }
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .color(_accentColor ?: MapColor.WOOD)
        .blockTags(BlockTags.STANDING_SIGNS, BlockTags.SIGNS)
        .itemTags(ItemTags.SIGNS)
        .recipe { c, p ->
          c.signCraftingRecipe({ p.get() }) {
            DataIngredient(_blockFamily.blocks[BlockFamily.Type.MAIN]!!.get().asItem())
          }
        }
        .blockstate { g, b -> g.simpleParticleOnly(b.get(), _name + "_planks") }
        .item { p, b ->
          SignItem(
            p.stacksTo(16),
            b,
            _blockFamily.blocks[BlockFamily.Type.WALL_SIGN]!!.get()
          )
        }
        .model { g, i -> g.flatItem(i.get()) }
        .build()
        .lang(DeltaboxUtil.asName(_name + "_sign"))
        .register()
    }

    // Hanging Wall Sign
    _blockFamily.setVariant(BlockFamily.Type.WALL_HANGING_SIGN) {
      registrate.block<WallHangingSignBlock>(_name + "_hanging_wall_sign")
        .copyFrom { Blocks.OAK_WALL_HANGING_SIGN }
        .factory { c, p -> WallHangingSignBlock(p, woodType) }
        .properties { c, p -> p.strength(1.0F).sound(SoundType.WOOD).noOcclusion() }
        .color(_accentColor ?: MapColor.WOOD)
        .blockTags(BlockTags.ALL_HANGING_SIGNS, BlockTags.WALL_HANGING_SIGNS)
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .blockstate { g, b -> g.simpleParticleOnly(b.get(), _name + "_planks") }
        .loot { g, b -> g.dropAnother(b.get(), _blockFamily.blocks[BlockFamily.Type.HANGING_SIGN]!!.get()) }
        .noItem()
        .lang(DeltaboxUtil.asName(_name + "_hanging_sign"))
        .register()
    }

    // Sign
    _blockFamily.setVariant(BlockFamily.Type.HANGING_SIGN) {
      registrate.block<CeilingHangingSignBlock>(_name + "_hanging_sign")
        .copyFrom { Blocks.OAK_HANGING_SIGN }
        .factory { c, p -> CeilingHangingSignBlock(p, woodType) }
        .properties { c, p -> p.strength(1.0F).sound(SoundType.WOOD).noOcclusion() }
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .color(_accentColor ?: MapColor.WOOD)
        .blockTags(BlockTags.ALL_HANGING_SIGNS, BlockTags.CEILING_HANGING_SIGNS)
        .itemTags(ItemTags.HANGING_SIGNS)
        .recipe { c, p ->
          c.hangingSignCraftingRecipe({ p.get() }) {
            DataIngredient(_blockFamily.blocks[BlockFamily.Type.STRIPPED_LOG]!!.get().asItem())
          }
        }
        .blockstate { g, b -> g.simpleParticleOnly(b.get(), _name + "_planks") }
        .item { p, b ->
          HangingSignItem(
            b,
            _blockFamily.blocks[BlockFamily.Type.WALL_HANGING_SIGN]!!.get(),
            p.stacksTo(16)
          )
        }
        .model { g, i -> g.flatItem(i.get()) }
        .build()
        .lang(DeltaboxUtil.asName(_name + "_hanging_sign"))
        .register()
    }
//
    registrate.boatVariant(_name)

    BOAT_ENTITY = registrate.entityType<BaseBoatEntity>("${_name}_boat")
      .renderer { c ->
        BaseBoatRenderer(registrate.modId, _name, c, false)
      }
      .factory { e, l -> BaseBoatEntity({ BOAT_ITEM!!.get() }, _name, { e }, l) }
      .properties { p -> p.sized(1.375f, 0.5625f) }
      .category(MobCategory.MISC)
      .register()

    CHEST_BOAT_ENTITY = registrate.entityType<BaseChestBoatEntity>("${_name}_chest_boat")
      .renderer { c ->
        BaseBoatRenderer(registrate.modId, _name, c, true)
      }
      .factory { e, l -> BaseChestBoatEntity({ getContent().chestBoatItem.get() }, _name, { e }, l) }
      .properties { p -> p.sized(1.375f, 0.5625f) }
      .category(MobCategory.MISC)
      .register()

    BOAT_ITEM =
      registrate.item<BoatItem>("${_name}_boat")
        .factory { p -> BoatItem(_name, { BOAT_ENTITY!!.get() }, false, p.stacksTo(1)) }
        .recipe { c, p ->
          c.boatCraftingRecipe({ p.get() }) {
            DataIngredient(_blockFamily.blocks[BlockFamily.Type.MAIN]!!.get().asItem())
          }
        }
        .register()

    CHEST_BOAT_ITEM =
      registrate.item<BoatItem>("${_name}_chest_boat")
        .factory { p -> BoatItem(_name, { getContent().chestBoatEntity.get() }, true, p.stacksTo(1)) }
        .recipe { c, p ->
          c.chestboatCraftingRecipe({ p.get() }) {
            _blockFamily.blocks[BlockFamily.Type.MAIN]!!.get().asItem()
          }
        }
        .register()
  }

  fun getContent(): WoodFamilyComponents {
    return WoodFamilyComponents(
      this._blockFamily,
      this.BOAT_ENTITY!!,
      this.CHEST_BOAT_ENTITY!!,
      this.BOAT_ITEM!!,
      this.CHEST_BOAT_ITEM!!
    )
  }
}