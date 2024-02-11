package com.dannbrown.databoxlib.datagen.content

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.content.block.FlammableSandBlock
import com.dannbrown.databoxlib.content.block.StickerBlock
import com.dannbrown.databoxlib.content.utils.toolTiers.SetTier
import com.dannbrown.databoxlib.content.utils.toolTiers.SetTool
import com.dannbrown.databoxlib.datagen.transformers.BlockLootPresets
import com.dannbrown.databoxlib.datagen.transformers.BlockTagPresets
import com.dannbrown.databoxlib.datagen.transformers.BlockTransformers
import com.dannbrown.databoxlib.datagen.transformers.BlockstatePresets
import com.dannbrown.databoxlib.datagen.transformers.ItemModelPresets
import com.dannbrown.databoxlib.datagen.transformers.RecipePresets
import com.dannbrown.databoxlib.lib.LibUtils
import com.simibubi.create.content.decoration.MetalLadderBlock
import com.simibubi.create.content.decoration.MetalScaffoldingBlock
import com.simibubi.create.content.decoration.TrainTrapdoorBlock
import com.simibubi.create.content.decoration.encasing.CasingBlock
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry
import com.simibubi.create.foundation.data.AssetLookup
import com.simibubi.create.foundation.data.CreateRegistrate
import com.tterrag.registrate.builders.BlockBuilder
import com.tterrag.registrate.providers.DataGenContext
import com.tterrag.registrate.providers.RegistrateBlockstateProvider
import com.tterrag.registrate.providers.RegistrateRecipeProvider
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables
import com.tterrag.registrate.util.DataIngredient
import com.tterrag.registrate.util.entry.BlockEntry
import com.tterrag.registrate.util.nullness.NonNullBiConsumer
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.ButtonBlock
import net.minecraft.world.level.block.DoorBlock
import net.minecraft.world.level.block.FenceBlock
import net.minecraft.world.level.block.FenceGateBlock
import net.minecraft.world.level.block.IronBarsBlock
import net.minecraft.world.level.block.PressurePlateBlock
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.SandBlock
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.TrapDoorBlock
import net.minecraft.world.level.block.WallBlock
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockSetType
import net.minecraft.world.level.block.state.properties.WoodType
import net.minecraft.world.level.material.MapColor
import java.util.function.Supplier

class BlockGen<T : Block>(name: String) {
  //  val blocksToReturn = mutableListOf<BlockEntry<T>>()
  private val _name: String = name
  private var _prefix = ""
  private var _suffix = ""
  private var _blockFactory: (Properties) -> T = { p: Properties -> Block(p) as T }
  private var _blockTags = mutableListOf<TagKey<Block>>()
  private var _itemTags = mutableListOf<TagKey<Item>>()
  private var _color: MapColor? = null
  private var _copyFrom: Supplier<Block> = Supplier { Blocks.STONE }
  private var _toolTier = SetTier.WOOD
  private var _toolType = SetTool.NONE
  private var _correctToolForDrops = false
  private var _textureName = name
  private var _noItem = false
  private var _builder: NonNullUnaryOperator<BlockBuilder<T, CreateRegistrate>> = NonNullUnaryOperator { b: BlockBuilder<T, CreateRegistrate> -> b }
  private fun createBlockBase(registerName: String, registrate: CreateRegistrate): BlockBuilder<T, CreateRegistrate> {
    return registrate.block<T>(registerName, _blockFactory)
      .initialProperties { _copyFrom.get() }
      .properties { p -> p.mapColor(if (_color != null) { _color } else { MapColor.COLOR_GRAY }) }
      .properties(if (_correctToolForDrops) { p -> p.requiresCorrectToolForDrops() } else { p -> p })
      .transform(if (this._toolType.getTag() != null) { p -> p.tag(this._toolType.getTag()) } else { p -> p })
      .transform(if (this._toolTier.getTag() != null) { p -> p.tag(this._toolTier.getTag()) } else { p -> p })
      .tag(*_blockTags.toTypedArray())
      .transform(if (this._noItem) { b -> b }
      else { b ->
        b.item()
          .tag(*_itemTags.toTypedArray())
          .build()
      })
  }

  /**
   * Add sand properties to the block
   * @param tone The tone of the sand block as an Int
   */
  fun sandBlock(tone: Int): BlockGen<T> {
    this.checkCurrentBuilder()
    _copyFrom = Supplier { Blocks.SAND }
    _blockFactory = { p -> SandBlock(tone, p) as T }
    return this
  }

  /**
   * Add flammable sand properties to the block
   * @param tone The tone of the flameable sand block as an Int
   * @param flammability The flammability of the block
   * @param fireSpread The fire spread of the block
   */
  fun flammableSandBlock(tone: Int, flammability: Int = 20, fireSpread: Int = 5): BlockGen<T> {
    this.checkCurrentBuilder()
    _copyFrom = Supplier {Blocks.SAND }
    _blockFactory = { p -> FlammableSandBlock(p, tone, flammability, fireSpread) as T }
    return this
  }

  /**
   * Changes the blockstate and model to be a rotated pillar
   */
  fun rotatedPillarBlock(topTexture: String = "", sideTexture: String = ""): BlockGen<T> {
    this.checkCurrentBuilder()
    _blockFactory = { p -> RotatedPillarBlock(p) as T }
    addBuilder { b ->
      b.blockstate { c, p ->
        val topTexture = LibUtils.resourceLocation("block/${topTexture.ifEmpty { c.name + "_top" }}")
        val sideTexture = LibUtils.resourceLocation("block/${sideTexture.ifEmpty { c.name }}")

        p.axisBlock(c.get() as RotatedPillarBlock, sideTexture, topTexture)
      }
    }
    return this
  }

  /**
   * Add a custom model to the block, the model file must exist in MOD_ID/models/block/BLOCK_ID.json
   */
  fun customStandardModel(): BlockGen<T> {
    this.checkCurrentBuilder()
    addBuilder { b ->
      b.blockstate { c, p -> p.simpleBlock(c.entry, AssetLookup.standardModel(c, p)) }
    }
    return this
  }

  /**
   * Add a custom model to the block, the model file must exist in MOD_ID/models/block/BLOCK_ID/block.json
   */
  fun customPartialModel(): BlockGen<T> {
    this.checkCurrentBuilder()
    addBuilder { b ->
      b.blockstate { c, p -> p.simpleBlock(c.entry, AssetLookup.partialBaseModel(c, p)) }
    }
    return this
  }

  /**
   * Add Storage Block properties, tags and block-items, items-block recipes
   * @param ingotItem The ingot item to use in the block-items recipe
   * @param ingredient The ingredient to use in the items-block recipe
   * @param addSuffix Whether to add the "_block" suffix to the block name
   */
  fun storageBlock(ingotItem: Supplier<ItemLike>, ingredient: Supplier<DataIngredient>, addSuffix: Boolean = true
  ): BlockGen<T> {
    this.checkCurrentBuilder()
    if (addSuffix) _suffix += "_block"
    addBuilder { b ->
      b.tag(*BlockTagPresets.storageBlockTags(_textureName).first)
        .recipe { c, p -> RecipePresets.storageBlockRecipe(c, p, ingotItem, ingredient) }
        .item()
        .tag(*BlockTagPresets.storageBlockTags(_textureName).second)
        .build()
    }
    return this
  }

  fun smallStorageBlock(ingotItem: Supplier<ItemLike>, ingredient: Supplier<DataIngredient>, addSuffix: Boolean = true
  ): BlockGen<T> {
    this.checkCurrentBuilder()
    if (addSuffix) _suffix += "_block"
    addBuilder { b ->
      b.tag(*BlockTagPresets.storageBlockTags(_textureName).first)
        .recipe { c, p -> RecipePresets.smallStorageBlockRecipe(c, p, ingotItem, ingredient) }
        .item()
        .tag(*BlockTagPresets.storageBlockTags(_textureName).second)
        .build()
    }
    return this
  }

  /**
   * Create a ore block with a custom loot table
   * @param dropItem The item to drop when the block is mined without silk touch
   * @param replace The block to replace in the world (stone by default)
   * @param addSuffix Whether to add the "_ore" suffix to the block name
   */
  fun oreBlock(dropItem: Supplier<ItemLike>, replace: String = "stone", addSuffix: Boolean = true
  ): BlockGen<T> {
    this.checkCurrentBuilder()
    if (addSuffix) _suffix = "_ore"
    addBuilder { b ->
      b.tag(*BlockTagPresets.oreBlockTags(_textureName, replace).first)
        .loot(BlockLootPresets.oreLootTable(dropItem))
        .item()
        .tag(*BlockTagPresets.oreBlockTags(_textureName, replace).second)
        .build()
    }
    return this
  }

  // @ Casings
  fun metalCasing(spriteShift: CTSpriteShiftEntry, addSuffix: Boolean = true
  ): BlockGen<T> {
    this.checkCurrentBuilder()
    _blockFactory = { p -> CasingBlock(p) as T }
    if (addSuffix) _suffix += "_casing"
    toolAndTier(SetTool.PICKAXE, SetTier.STONE)
    _copyFrom = Supplier {Blocks.CUT_COPPER }
    addBuilder { b ->
      b.transform(BlockTransformers.casingBlock { spriteShift })
        .properties { p -> p.sound(SoundType.METAL) }
    }
    return this
  }

  fun woodenCasing(spriteShift: CTSpriteShiftEntry): BlockGen<T> {
    this.checkCurrentBuilder()
    _blockFactory = { p -> CasingBlock(p) as T }
    toolAndTier(SetTool.AXE, SetTier.WOOD)
    _copyFrom = Supplier {Blocks.OAK_PLANKS }
    addBuilder { b ->
      b.transform(BlockTransformers.casingBlock { spriteShift })
        .properties { p -> p.sound(SoundType.WOOD) }
    }
    return this
  }

  fun metalPanelCasing(spriteShift: CTSpriteShiftEntry, lightLevel: Int = 10, addSuffix: Boolean = true
  ): BlockGen<T> {
    this.checkCurrentBuilder()
    if (addSuffix) _suffix += "_panel"
    metalCasing(spriteShift, addSuffix)
    addBuilder { b ->
      b.properties { p -> p.lightLevel { lightLevel } }
    }
    return this
  }

  // Scaffolding
  fun metalScaffoldingBlock(ingredient: Supplier<DataIngredient>, scaffoldShift: CTSpriteShiftEntry, scaffoldInsideShift: CTSpriteShiftEntry, casingShift: CTSpriteShiftEntry, addSuffix: Boolean = true
  ): BlockGen<T> {
    this.checkCurrentBuilder()
    _blockFactory = { p -> MetalScaffoldingBlock(p) as T }
    _copyFrom = Supplier {Blocks.CUT_COPPER }
    if (addSuffix) _suffix += "_scaffolding"
    toolAndTier(SetTool.PICKAXE, SetTier.STONE)
    addBuilder { b ->
      b.transform(BlockTransformers.metalScaffolding(_name, scaffoldShift, scaffoldInsideShift, casingShift))
        .recipe { c, p -> RecipePresets.scaffoldingStonecuttingRecipe(c, p, ingredient) }
        .properties { p ->
          p.sound(SoundType.METAL)
            .noOcclusion()
        }
    }
    return this
  }

  // Bars
  fun metalBarsBlock(ingredient: Supplier<DataIngredient>, specialEdge: Boolean = true, addSuffix: Boolean = true
  ): BlockGen<T> {
    this.checkCurrentBuilder()
    _blockFactory = { p -> IronBarsBlock(p) as T }
    _copyFrom = Supplier {Blocks.IRON_BARS }
    if (addSuffix) _suffix += "_bars"
    toolAndTier(SetTool.PICKAXE, SetTier.STONE)
    addBuilder { b ->
      b.transform(BlockTransformers.metalBars(_textureName, specialEdge))
        .recipe { c, p -> RecipePresets.barsStonecuttingRecipe(c, p, ingredient) }
        .properties { p ->
          p.sound(SoundType.COPPER)
            .noOcclusion()
        }
    }
    return this
  }

  // Trapdoors
  fun metalTrapdoorBlock(ingredient: Supplier<DataIngredient>, orientable: Boolean = true, addSuffix: Boolean = true
  ): BlockGen<T> {
    this.checkCurrentBuilder()
    _blockFactory = { p -> TrainTrapdoorBlock(p) as T }
    _copyFrom = Supplier {Blocks.IRON_TRAPDOOR }
    if (addSuffix) _suffix += "_trapdoor"
    toolAndTier(SetTool.PICKAXE, SetTier.STONE)
    addBuilder { b ->
      b.transform(BlockTransformers.trapdoor(_textureName, "metal", orientable))
        .recipe { c, p -> RecipePresets.trapdoorStonecuttingRecipe(c, p, ingredient) }
        .properties { p ->
          p.sound(SoundType.COPPER)
            .noOcclusion()
        }
    }
    return this
  }

  fun woodenTrapdoorBlock(ingredient: Supplier<DataIngredient>, blockSetType: BlockSetType, orientable: Boolean = true, addSuffix: Boolean = true
  ): BlockGen<T> {
    this.checkCurrentBuilder()
    _blockFactory = { p -> TrapDoorBlock(p, blockSetType) as T }
    _copyFrom = Supplier {Blocks.OAK_TRAPDOOR }
    if (addSuffix) _suffix += "_trapdoor"
    addBuilder { b ->
      b.transform(BlockTransformers.trapdoor(_textureName, "wooden", orientable))
        .recipe { c, p -> RecipePresets.trapdoorCraftingRecipe(c, p, ingredient) }
        .properties { p ->
          p.sound(SoundType.WOOD)
            .noOcclusion()
        }
    }
    return this
  }

  // @ Ladder
  fun metalLadderBlock(ingredient: Supplier<DataIngredient>, addSuffix: Boolean = true
  ): BlockGen<T> {
    this.checkCurrentBuilder()
    _blockFactory = { p -> MetalLadderBlock(p) as T }
    _copyFrom = Supplier {Blocks.LADDER }
    if (addSuffix) _suffix += "_ladder"
    toolAndTier(SetTool.PICKAXE, SetTier.STONE)
    addBuilder { b ->
      b.transform(BlockTransformers.metalLadder(_textureName))
        .recipe { c, p -> RecipePresets.ladderStonecuttingRecipe(c, p, ingredient) }
        .properties { p ->
          p.sound(SoundType.COPPER)
            .noOcclusion()
        }
    }
    return this
  }

  // @ Sticker
  fun stickerBlock(addSuffix: Boolean = true
  ): BlockGen<T> {
    this.checkCurrentBuilder()
    _blockFactory = { p -> StickerBlock(p) as T }
    _copyFrom = Supplier {Blocks.OAK_LEAVES }
    if (addSuffix) _suffix += "_sticker"
    addBuilder { b ->
      b.transform(BlockTransformers.sticker(_textureName))
    }
    return this
  }

  // @ BOTTOM TOP BLOCK
  fun bottomTopBlock(bottomName: String = "", topName: String = "", sideName: String = ""): BlockGen<T> {
    this.checkCurrentBuilder()
    _blockFactory = { p -> Block(p) as T }
    _copyFrom = Supplier {Blocks.SANDSTONE }
    addBuilder { b ->
      b.blockstate(BlockstatePresets.cubeBottomTopBlock(sideName.ifEmpty { _textureName }, bottomName.ifEmpty { _textureName + "_bottom" }, topName.ifEmpty { _textureName + "_top" }))
    }
    return this
  }

  // @ SLAB
  fun slabBlock(bottomTop: Boolean = false, isWooden: Boolean = false, addSuffix: Boolean = true
  ): BlockGen<T> {
    this.checkCurrentBuilder()
    _blockFactory = { p -> SlabBlock(p) as T }
    _copyFrom = Supplier {Blocks.OAK_SLAB }
    if (addSuffix) _suffix += "_slab"
    addBuilder { b ->
      b.blockstate(if (bottomTop) BlockstatePresets.bottomTopSlabBlock(_textureName) else BlockstatePresets.slabBlock(_textureName))
        .tag(*(if (isWooden) BlockTagPresets.woodenSlabTags().first else BlockTagPresets.slabTags().first))
        .item()
        .tag(*(if (isWooden) BlockTagPresets.woodenSlabTags().second else BlockTagPresets.slabTags().second))
        .model(ItemModelPresets.simpleBlockItem())
        .build()
    }
    return this
  }

  // @ STAIRS
  fun stairsBlock(referenceBlockState: Supplier<BlockState>, bottomTop: Boolean = false, isWooden: Boolean = false, addSuffix: Boolean = true
  ): BlockGen<T> {
    this.checkCurrentBuilder()
    _blockFactory = { p -> StairBlock(referenceBlockState, p) as T }
    _copyFrom = Supplier {Blocks.OAK_STAIRS }
    if (addSuffix) _suffix += "_stairs"
    addBuilder { b ->
      b.blockstate(if (bottomTop) BlockstatePresets.bottomTopStairsBlock(_textureName) else BlockstatePresets.stairsBlock(_textureName))
        .tag(*(if (isWooden) BlockTagPresets.woodenStairsTags().first else BlockTagPresets.stairsTags().first))
        .item()
        .tag(*(if (isWooden) BlockTagPresets.woodenStairsTags().second else BlockTagPresets.stairsTags().second))
        .model(ItemModelPresets.simpleBlockItem())
        .build()
    }
    return this
  }

  // @ WALL
  fun wallBlock(bottomTop: Boolean = false, addSuffix: Boolean = true
  ): BlockGen<T> {
    this.checkCurrentBuilder()
    _blockFactory = { p -> WallBlock(p) as T }
    _copyFrom = Supplier {Blocks.COBBLESTONE_WALL }
    if (addSuffix) _suffix += "_wall"
    addBuilder { b ->
      b.blockstate(if (bottomTop) BlockstatePresets.bottomTopWallBlock(_textureName) else BlockstatePresets.wallBlock(_textureName))
        .tag(*BlockTagPresets.wallTags().first)
        .item()
        .tag(*BlockTagPresets.wallTags().second)
        .model(if (bottomTop) ItemModelPresets.bottomTopWallItem(_textureName) else ItemModelPresets.wallItem(_textureName))
        .build()
    }
    return this
  }
  // @ FENCE
  /**
   * Changes the blockstate and model to be a fence block
   */
  fun fenceBlock(isWooden: Boolean = false, addSuffix: Boolean = true
  ): BlockGen<T> {
    this.checkCurrentBuilder()
    _blockFactory = { p -> FenceBlock(p) as T }
    _copyFrom = Supplier {Blocks.OAK_FENCE }
    if (addSuffix) _suffix += "_fence"

    addBuilder { b ->
      b.blockstate(BlockstatePresets.fenceBlock(_textureName))
        .tag(*BlockTagPresets.fenceTags(isWooden).first)
        .item()
        .tag(*BlockTagPresets.fenceTags(isWooden).second)
        .model(ItemModelPresets.fenceItem(_textureName))
        .build()
    }
    return this
  } // @ FENCE GATE

  /**
   * Changes the blockstate and model to be a fence gate block
   */
  fun fenceGateBlock(woodType: WoodType, addSuffix: Boolean = true
  ): BlockGen<T> {
    this.checkCurrentBuilder()
    _blockFactory = { p -> FenceGateBlock(p, woodType) as T }
    _copyFrom = Supplier {Blocks.OAK_FENCE_GATE }
    if (addSuffix) _suffix += "_fence_gate"
    addBuilder { b ->
      b.blockstate(BlockstatePresets.fenceGateBlock(_textureName))
        .tag(BlockTags.FENCE_GATES)
    }
    return this
  } // @ PRESSURE PLATE

  /**
   * Changes the blockstate and model to be a pressure plate block
   */
  fun pressurePlateBlock(blockSetType: BlockSetType, isWooden: Boolean = true, addSuffix: Boolean = true
  ): BlockGen<T> {
    this.checkCurrentBuilder()
    _blockFactory = { p -> PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, p, blockSetType) as T }
    _copyFrom = Supplier {Blocks.OAK_PRESSURE_PLATE }
    if (addSuffix) _suffix += "_pressure_plate"
    addBuilder { b ->
      b.blockstate(BlockstatePresets.pressurePlateBlock(_textureName))
        .properties { p ->
          p.noCollission()
            .strength(0.5F)
        }
        .tag(*BlockTagPresets.pressurePlateTags(isWooden).first)
        .item()
        .tag(*BlockTagPresets.pressurePlateTags(isWooden).second)
        .model(ItemModelPresets.pressurePlateItem(_textureName))
        .build()
    }
    return this
  }
  // @ BUTTON
  /**
   * Changes the blockstate and model to be a button block
   */
  fun buttonBlock(blockSetType: BlockSetType, isWooden: Boolean = true, addSuffix: Boolean = true
  ): BlockGen<T> {
    this.checkCurrentBuilder()
    _blockFactory = { p -> ButtonBlock(p, blockSetType, 30, true) as T }
    _copyFrom = Supplier {Blocks.OAK_BUTTON }
    if (addSuffix) _suffix += "_button"
    addBuilder { b ->
      b.blockstate(BlockstatePresets.buttonBlock(_textureName))
        .properties { p ->
          p.noCollission()
            .strength(0.5F)
        }
        .tag(*BlockTagPresets.buttonTags(isWooden).first)
        .item()
        .tag(*BlockTagPresets.buttonTags(isWooden).second)
        .model(ItemModelPresets.buttonItem(_textureName))
        .build()
    }
    return this
  }
  // DOOR
  /**
   * Changes the blockstate and model to be a door block
   */
  fun woodenDoorBlock(blockSetType: BlockSetType, isWooden: Boolean = true, addSuffix: Boolean = true
  ): BlockGen<T> {
    this.checkCurrentBuilder()
    _blockFactory = { p -> DoorBlock(p, blockSetType) as T }
    _copyFrom = Supplier {Blocks.OAK_PLANKS }
    if (addSuffix) _suffix += "_door"
    addBuilder { b ->
      b.properties { p ->
        p.strength(3.0F)
          .sound(SoundType.WOOD)
          .noOcclusion()
      }
        .blockstate(BlockstatePresets.doorTransparentBlock())
        .tag(*BlockTagPresets.doorTags(isWooden).first)
        .item()
        .tag(*BlockTagPresets.doorTags(isWooden).second)
        .model(ItemModelPresets.doorItem())
        .build()
    }
    return this
  }

  // UTILITY
  fun fromFamily(
    copyFrom: Supplier<Block>,
    props: (Properties) -> Properties = { p: Properties -> p },
    color: MapColor,
    tool: SetTool,
    tier: SetTier,
    correctToolForDrops: Boolean = true,
  ): BlockGen<T> {
    this._copyFrom = copyFrom
    addBuilder { b -> b.properties { p -> props(p) } }
    this._color = color
    this._toolTier = tier
    this._toolType = tool
    this._correctToolForDrops = correctToolForDrops
    return this
  }

  fun toolAndTier(
    tool: SetTool = SetTool.NONE,
    tier: SetTier = SetTier.WOOD,
    correctToolForDrops: Boolean = true,
  ): BlockGen<T> {
    this._toolTier = tier
    this._toolType = tool
    this._correctToolForDrops = correctToolForDrops
    return this
  }

  // REGISTERING
  private fun addBuilder(toApply: (BlockBuilder<T, CreateRegistrate>) -> BlockBuilder<T, CreateRegistrate>): BlockGen<T> {
    this.checkCurrentBuilder()
    val oldBuilder = _builder
    _builder = NonNullUnaryOperator { b: BlockBuilder<T, CreateRegistrate> -> toApply(b).transform(oldBuilder) }

    return this
  }

  fun register(registrate: CreateRegistrate = ProjectContent.REGISTRATE): BlockEntry<T> {
    this.checkCurrentBuilder()
    return createBlockBase(fullName(), registrate).transform(_builder)
      .register()
  }

  // CHAINING
  fun blockFactory(factory: (Properties) -> T = { p: Properties -> Block(p) as T }): BlockGen<T> {
    this._blockFactory = factory
    return this
  }

  fun properties(props: (Properties) -> Properties = { p: Properties -> p }): BlockGen<T> {
    addBuilder { b -> b.properties { p -> props(p) } }
    return this
  }

  fun transform(t: NonNullUnaryOperator<BlockBuilder<T, CreateRegistrate>>): BlockGen<T> {
    this.checkCurrentBuilder()
    addBuilder { b ->
      b.transform(t)
    }
    return this
  }

  fun recipe(r: NonNullBiConsumer<DataGenContext<Block, T>, RegistrateRecipeProvider>): BlockGen<T> {
    this.checkCurrentBuilder()
    addBuilder { b ->
      b.recipe(r)
    }
    return this
  }

  fun loot(l: NonNullBiConsumer<RegistrateBlockLootTables, T>): BlockGen<T> {
    this.checkCurrentBuilder()
    addBuilder { b ->
      b.loot(l)
    }
    return this
  }

  fun blockstate(bs: NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider>): BlockGen<T> {
    this.checkCurrentBuilder()
    addBuilder { b ->
      b.blockstate(bs)
    }
    return this
  }

  fun copyFrom(block: Supplier<Block> = Supplier { Blocks.STONE }): BlockGen<T> {
    this._copyFrom = block
    return this
  }

  fun blockTags(tags: List<TagKey<Block>> = mutableListOf()): BlockGen<T> {
    this._blockTags.addAll(tags)
    return this
  }

  fun itemTags(tags: List<TagKey<Item>> = mutableListOf()): BlockGen<T> {
    this._itemTags.addAll(tags)
    return this
  }

  fun color(color: MapColor = MapColor.COLOR_GRAY): BlockGen<T> {
    this._color = color
    return this
  }

  fun noItem(): BlockGen<T> {
    this._noItem = true
    return this
  }

  fun prefix(prefix: String = ""): BlockGen<T> {
    this._prefix += prefix
    return this
  }

  fun suffix(suffix: String = ""): BlockGen<T> {
    this._suffix += suffix
    return this
  }

  fun textureName(textureName: String = _name): BlockGen<T> {
    this._textureName = textureName
    return this
  }

  private fun checkCurrentBuilder() {
    if (_builder == null) {
      throw Exception("No block started")
    }
  }

  private fun fullName(): String {
    return _prefix + _name + _suffix
  }
}


