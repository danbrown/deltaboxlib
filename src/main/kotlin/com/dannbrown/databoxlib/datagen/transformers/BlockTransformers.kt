package com.dannbrown.databoxlib.datagen.transformers

import com.simibubi.create.AllInteractionBehaviours
import com.simibubi.create.AllTags
import com.simibubi.create.content.contraptions.behaviour.TrapdoorMovingInteraction
import com.simibubi.create.content.decoration.MetalScaffoldingCTBehaviour
import com.simibubi.create.content.decoration.TrapdoorCTBehaviour
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry
import com.simibubi.create.foundation.data.CreateRegistrate
import com.tterrag.registrate.builders.BlockBuilder
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import java.util.function.Supplier

object BlockTransformers {
  fun <B : Block, P> casingBlock(spriteShift: Supplier<CTSpriteShiftEntry>): NonNullUnaryOperator<BlockBuilder<B, P>> {
    return NonNullUnaryOperator { b ->
      b.blockstate(BlockstatePresets.simpleBlock())
        .onRegister(CreateRegistrate.connectedTextures { EncasedCTBehaviour(spriteShift.get()) })
        .onRegister(CreateRegistrate.casingConnectivity { block, cc -> cc.makeCasing(block, spriteShift.get()) })
        .tag(*BlockTagPresets.casingBlockTags().first)
        .item()
        .tag(*BlockTagPresets.casingBlockTags().second)
        .build()
    }
  }

  fun <B : Block, P> metalScaffolding(name: String, scaffoldSpriteShift: CTSpriteShiftEntry, insideSpriteShift: CTSpriteShiftEntry, casingSpriteShift: CTSpriteShiftEntry
  ): NonNullUnaryOperator<BlockBuilder<B, P>> {
    return NonNullUnaryOperator { b ->
      b.initialProperties { Blocks.SCAFFOLDING }
        .blockstate(BlockstatePresets.scaffoldingBlock(name))
        .onRegister(CreateRegistrate.connectedTextures { MetalScaffoldingCTBehaviour(scaffoldSpriteShift, insideSpriteShift, casingSpriteShift) })
        .tag(*BlockTagPresets.scaffoldingBlockTags().first)
        .item(BlockItemFactory.metalScaffoldingItem())
        .model(ItemModelPresets.simpleBlockItem())
        .build()
    }
  }

  fun <B : Block, P> metalBars(name: String, specialEdge: Boolean = true): NonNullUnaryOperator<BlockBuilder<B, P>> {
    return NonNullUnaryOperator { b ->
      b.initialProperties { Blocks.IRON_BARS }
        .tag(*BlockTagPresets.barsBlockTags().first)
        .blockstate(BlockstatePresets.barsBlock(name, specialEdge))
        .item()
        .model(ItemModelPresets.barsItem(name, specialEdge))
        .build()
    }
  }

  fun <B : Block, P> metalLadder(name: String): NonNullUnaryOperator<BlockBuilder<B, P>> {
    return NonNullUnaryOperator { b ->
      b.initialProperties { Blocks.LADDER }
        .tag(*BlockTagPresets.ladderBlockTags().first, AllTags.AllBlockTags.WRENCH_PICKUP.tag)
        .blockstate(BlockstatePresets.ladderBlock(name))
        .properties { p: BlockBehaviour.Properties ->
          p.sound(SoundType.COPPER)
            .noOcclusion()
        }
        .item()
        .model(ItemModelPresets.simpleLayerItem("ladder_$name"))
        .build()
    }
  }

  fun <B : Block, P> trapdoor(name: String, type: String, orientable: Boolean): NonNullUnaryOperator<BlockBuilder<B, P>> {
    var tags = BlockTagPresets.trapdoorTags()

    if (type == "wooden") tags = BlockTagPresets.woodenTrapdoorTags()
    else if (type == "metal") tags = BlockTagPresets.metalTrapdoorTags()

    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
      b.properties { p -> p.noOcclusion() }
        .blockstate(BlockstatePresets.trapdoorBlock(name, orientable))
        .onRegister(CreateRegistrate.connectedTextures { TrapdoorCTBehaviour() })
        .onRegister(AllInteractionBehaviours.interactionBehaviour(TrapdoorMovingInteraction()))
        .tag(*tags.first)
        .item()
        .tag(*tags.second)
        .model(ItemModelPresets.trapdoorItem(name))
        .build()
    }
  }

  fun <B : Block, P> sticker(name: String): NonNullUnaryOperator<BlockBuilder<B, P>> {
    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
      b.initialProperties { Blocks.OAK_LEAVES }
        .properties { p ->
          p.mapColor(MapColor.NONE)
            .noOcclusion()
            .instabreak()
            .noCollission()
        }
        .tag(*BlockTagPresets.stickerTags().first)
        .blockstate(BlockstatePresets.stickerBlock(name))
        .item()
        .tag(*BlockTagPresets.stickerTags().second)
        .model(ItemModelPresets.stickerItem(name))
        .build()
    }
  }
}