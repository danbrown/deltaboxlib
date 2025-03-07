package com.dannbrown.deltaboxlib.registrate.presets.blocks


import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.builders.BlockBuilder
import com.dannbrown.deltaboxlib.registrate.presets.tags.BlockTagPresets
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.ButtonBlock
import net.minecraft.world.level.block.DoorBlock
import net.minecraft.world.level.block.FenceBlock
import net.minecraft.world.level.block.FenceGateBlock
import net.minecraft.world.level.block.PressurePlateBlock
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.TrapDoorBlock
import net.minecraft.world.level.block.WallBlock
import net.minecraft.world.level.block.state.properties.BlockSetType
import net.minecraft.world.level.block.state.properties.WoodType

class CommonBlockPreset(
  val registrate: AbstractDeltaboxRegistrate,
  val blockId: String,
) : IBlockBuilderPreset(registrate, blockId) {
  fun <T : Block> createBottomTop(
    bottomName: String = "",
    topName: String = "",
    sideName: String = ""
  ): BlockBuilder<T> {
    val bottomTextureName = bottomName.ifEmpty { "${blockId}_bottom" }
    val topTextureName = topName.ifEmpty { "${blockId}_top" }
    val sideTextureName = sideName.ifEmpty { blockId }
    return registrate
      .block<T>(blockId)
      .blockstate { g, b -> g.bottomTopBlock(b.get(), bottomTextureName, topTextureName, sideTextureName) }
  }

  fun <T : Block> createRotatedPillar(
    _topTexture: String = "",
    _sideTexture: String = ""
  ): BlockBuilder<T> {
    val topTextureName = _topTexture.ifEmpty { "${blockId}_top" }
    val sideTextureName = _sideTexture.ifEmpty { blockId }
    return registrate
      .block<T>(blockId)
      .factory { c, p -> RotatedPillarBlock(p) }
      .blockstate { g, b -> g.rotatedPillarBlock(b.get(), topTextureName, sideTextureName) }
  }

  fun <T : Block> createStairs(
    textureName: String,
    bottomTop: Boolean = false,
    isWooden: Boolean = false,
    addSuffix: Boolean = true
  ): BlockBuilder<T> {
    val nameWithSuffix = if (addSuffix) this.blockId + "_stairs" else blockId
    return registrate
      .block<T>(nameWithSuffix)
      .factory { c, p -> StairBlock(Blocks.STONE.defaultBlockState(), p) }
      .copyFrom { if (isWooden) Blocks.OAK_STAIRS else Blocks.COBBLESTONE_STAIRS }
      .blockstate { g, b ->
        if (bottomTop) g.bottomTopStairs(
          b.get(),
          "${textureName}_bottom",
          "${textureName}_top",
          textureName
        ) else g.stairs(b.get(), textureName)
      }
      .blockTags(*(if (isWooden) BlockTagPresets.woodenStairsTags().first.toTypedArray() else BlockTagPresets.stairsTags().first.toTypedArray()))
      .item()
      .itemTags(*(if (isWooden) BlockTagPresets.woodenStairsTags().second.toTypedArray() else BlockTagPresets.stairsTags().second.toTypedArray()))
      .build()
      .loot { g, b -> g.dropSelf(b.get()) } as BlockBuilder<T>
  }

  fun <T : Block> createSlab(
    textureName: String,
    bottomTop: Boolean = false,
    isWooden: Boolean = false,
    addSuffix: Boolean = true
  ): BlockBuilder<T> {
    val nameWithSuffix = if (addSuffix) this.blockId + "_slab" else blockId
    return registrate
      .block<T>(nameWithSuffix)
      .factory { c, p -> SlabBlock(p) }
      .copyFrom { if (isWooden) Blocks.OAK_SLAB else Blocks.COBBLESTONE_SLAB }
      .blockstate { g, b ->
        if (bottomTop) g.bottomTopSlab(
          b.get(),
          "${textureName}_bottom",
          "${textureName}_top",
          textureName
        ) else g.slab(b.get(), textureName)
      }
      .blockTags(*(if (isWooden) BlockTagPresets.woodenSlabTags().first.toTypedArray() else BlockTagPresets.slabTags().first.toTypedArray()))
      .item()
      .itemTags(*(if (isWooden) BlockTagPresets.woodenSlabTags().second.toTypedArray() else BlockTagPresets.slabTags().second.toTypedArray()))
      .build()
      .loot { g, b -> g.dropSlab(b.get()) } as BlockBuilder<T>
  }

  fun <T : Block> createWall(
    textureName: String,
    bottomTop: Boolean = false,
    addSuffix: Boolean = true
  ): BlockBuilder<T> {
    val nameWithSuffix = if (addSuffix) this.blockId + "_wall" else blockId
    return registrate
      .block<T>(nameWithSuffix)
      .factory { c, p -> WallBlock(p) }
      .blockstate { g, b ->
        if (bottomTop) g.bottomTopWall(
          b.get(),
          "${textureName}_bottom",
          "${textureName}_top",
          textureName
        ) else g.wall(b.get(), textureName)
      }
      .blockTags(*BlockTagPresets.wallTags().first.toTypedArray())
      .item()
      .model { g, i ->
        if (bottomTop) g.bottomTopWallInventory(
          i.get(),
          textureName,
          "${textureName}_top",
        ) else g.wallInventory(i.get(), textureName)
      }
      .itemTags(*BlockTagPresets.wallTags().second.toTypedArray())
      .build()
      .loot { g, b -> g.dropSelf(b.get()) } as BlockBuilder<T>

  }

  fun <T : Block> createFence(
    textureName: String,
    isWooden: Boolean = false,
    addSuffix: Boolean = true
  ): BlockBuilder<T> {
    val nameWithSuffix = if (addSuffix) this.blockId + "_fence" else blockId
    return registrate
      .block<T>(nameWithSuffix)
      .factory { c, p -> FenceBlock(p) }
      .copyFrom { if (isWooden) Blocks.OAK_FENCE else Blocks.NETHER_BRICK_FENCE }
      .blockstate { g, b -> g.fence(b.get(), textureName) }
      .blockTags(*BlockTagPresets.fenceTags(isWooden).first.toTypedArray())
      .item()
      .model { g, i -> g.fenceInventory(i.get(), textureName) }
      .itemTags(*BlockTagPresets.fenceTags(isWooden).second.toTypedArray())
      .build()
      .loot { g, b -> g.dropSelf(b.get()) } as BlockBuilder<T>
  }

  fun <T : Block> createFenceGate(
    textureName: String,
    woodType: WoodType,
    addSuffix: Boolean = true
  ): BlockBuilder<T> {
    val nameWithSuffix = if (addSuffix) this.blockId + "_fence_gate" else blockId
    return registrate
      .block<T>(nameWithSuffix)
      .factory { c, p -> FenceGateBlock(p, woodType) }
      .copyFrom { Blocks.OAK_FENCE_GATE }
      .blockstate { g, b -> g.fenceGate(b.get(), textureName) }
      .loot { g, b -> g.dropSelf(b.get()) }
      .blockTags(BlockTags.FENCE_GATES)
  }

  fun <T : Block> createPressurePlate(
    textureName: String,
    blockSetType: BlockSetType,
    isWooden: Boolean = true,
    addSuffix: Boolean = true
  ): BlockBuilder<T> {
    val nameWithSuffix = if (addSuffix) this.blockId + "_pressure_plate" else blockId
    return registrate
      .block<T>(nameWithSuffix)
      .factory { c, p -> PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, p, blockSetType) }
      .copyFrom { if (isWooden) Blocks.OAK_PRESSURE_PLATE else Blocks.STONE_PRESSURE_PLATE }
      .blockstate { g, b -> g.pressurePlate(b.get(), textureName) }
      .properties { c, p -> p.noCollission().strength(0.5F) }
      .blockTags(*BlockTagPresets.pressurePlateTags(isWooden).first.toTypedArray())
      .item()
      .itemTags(*BlockTagPresets.pressurePlateTags(isWooden).second.toTypedArray())
      .build()
      .loot { g, b -> g.dropSelf(b.get()) } as BlockBuilder<T>
  }

  fun <T : Block> createButton(
    textureName: String,
    blockSetType: BlockSetType,
    isWooden: Boolean = true,
    addSuffix: Boolean = true
  ): BlockBuilder<T> {
    val nameWithSuffix = if (addSuffix) this.blockId + "_button" else blockId
    return registrate
      .block<T>(nameWithSuffix)
      .factory { c, p -> ButtonBlock(p, blockSetType, 30, isWooden) }
      .copyFrom { if (isWooden) Blocks.OAK_BUTTON else Blocks.STONE_BUTTON }
      .properties { c, p -> p.noCollission().strength(0.5F) }
      .blockstate { g, b -> g.button(b.get(), textureName) }
      .blockTags(*BlockTagPresets.buttonTags(isWooden).first.toTypedArray())
      .item()
      .model { g, i -> g.buttonInventory(i.get(), textureName) }
      .itemTags(*BlockTagPresets.buttonTags(isWooden).second.toTypedArray())
      .build()
      .loot { g, b -> g.dropSelf(b.get()) } as BlockBuilder<T>
  }

  fun <T : Block> createWoodenTrapdoor(
    blockSetType: BlockSetType,
    addSuffix: Boolean = true
  ): BlockBuilder<T> {
    val nameWithSuffix = if (addSuffix) "${blockId}_trapdoor" else blockId
    return registrate
      .block<T>(nameWithSuffix)
      .factory { c, p -> TrapDoorBlock(p, blockSetType) }
      .copyFrom { Blocks.OAK_TRAPDOOR }
      .blockstate { g, b -> g.trapdoor(b.get(), nameWithSuffix) }
      .properties { c, p ->
        p.sound(SoundType.WOOD).noOcclusion()
      }
      .blockTags(*BlockTagPresets.woodenTrapdoorTags().first.toTypedArray())
      .item()
      .itemTags(*BlockTagPresets.woodenTrapdoorTags().second.toTypedArray())
      .build()
      .cutoutRender()
      .loot { g, b -> g.dropSelf(b.get()) } as BlockBuilder<T>
  }

  fun <T : Block> createDoor(
    blockSetType: BlockSetType,
    isWooden: Boolean = true,
    addSuffix: Boolean = true
  ): BlockBuilder<T> {
    val nameWithSuffix = if (addSuffix) "${blockId}_door" else blockId
    return registrate
      .block<T>(nameWithSuffix)
      .factory { c, p -> DoorBlock(p, blockSetType) }
      .copyFrom { if (isWooden) Blocks.OAK_DOOR else Blocks.IRON_DOOR }
//      .blockstate(BlockstatePresets.doorTransparentBlock())
      .properties { c, p ->
        p.noOcclusion()
      }
      .blockTags(*BlockTagPresets.doorTags(isWooden).first.toTypedArray())
      .item()
//      .model(ItemModelPresets.doorItem(nameWithSuffix))
      .itemTags(*BlockTagPresets.doorTags(isWooden).second.toTypedArray())
      .build()
//      .loot(BlockLootPresets.doorLoot())
      .cutoutRender() as BlockBuilder<T>
  }
}