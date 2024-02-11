package com.dannbrown.databoxlib.content.utils.spriteShifts

import com.simibubi.create.AllTags.AllBlockTags
import com.simibubi.create.AllTags.AllItemTags
import com.simibubi.create.content.decoration.MetalScaffoldingBlock
import com.simibubi.create.content.decoration.MetalScaffoldingBlockItem
import com.simibubi.create.content.decoration.MetalScaffoldingCTBehaviour
import com.simibubi.create.content.decoration.encasing.CasingBlock
import com.simibubi.create.content.decoration.encasing.CasingConnectivity
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry
import com.simibubi.create.foundation.data.CreateRegistrate
import com.simibubi.create.foundation.data.SharedProperties
import com.simibubi.create.foundation.data.TagGen
import com.tterrag.registrate.builders.BlockBuilder
import com.tterrag.registrate.providers.DataGenContext
import com.tterrag.registrate.providers.RegistrateBlockstateProvider
import com.tterrag.registrate.providers.RegistrateItemModelProvider
import com.tterrag.registrate.util.DataIngredient
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.tags.BlockTags
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.MapColor
import net.minecraftforge.client.model.generators.ConfiguredModel
import java.util.function.Supplier


// import com.simibubi.create.foundation.data.BuilderTransformers
// here we override some funcions from: com.simibubi.create.foundation.data.BuilderTransformers
object CreateBuilderTransformers {
  fun <B : CasingBlock?> casing(
    ct: Supplier<CTSpriteShiftEntry?>
  ): NonNullUnaryOperator<BlockBuilder<B, CreateRegistrate>> {
    return NonNullUnaryOperator { b: BlockBuilder<B, CreateRegistrate> ->
      b.initialProperties { SharedProperties.stone() }
        .blockstate { c: DataGenContext<Block?, B>, p: RegistrateBlockstateProvider -> p.simpleBlock(c.get()) }
        .onRegister(CreateRegistrate.connectedTextures { EncasedCTBehaviour(ct.get()) })
        .onRegister(CreateRegistrate.casingConnectivity { block: B, cc: CasingConnectivity -> cc.makeCasing(block, ct.get()) })
        .tag(AllBlockTags.CASING.tag)
        .item()
        .tag(AllItemTags.CASING.tag)
        .build()
    }
  }

  fun <B : Block?, P> scaffold(
    name: String,
    ingredient: Supplier<DataIngredient>,
    color: MapColor?,
    scaffoldShift: CTSpriteShiftEntry?,
    scaffoldInsideShift: CTSpriteShiftEntry?,
    casingShift: CTSpriteShiftEntry?
  ): NonNullUnaryOperator<BlockBuilder<B, P>> {
    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
      b.initialProperties { Blocks.SCAFFOLDING }
        .properties { p: BlockBehaviour.Properties ->
          p.sound(SoundType.COPPER)
            .mapColor(color)
        }
//        .addLayer { Supplier { RenderType.cutout() } }
        .blockstate { c: DataGenContext<Block?, B>, p: RegistrateBlockstateProvider ->
          p.getVariantBuilder(c.get())
            .forAllStatesExcept({ s: BlockState ->
              val suffix =
                if (s.getValue(MetalScaffoldingBlock.BOTTOM)) "_horizontal" else ""
              ConfiguredModel.builder()
                .modelFile(
                  p.models()
                    .withExistingParent(c.name + suffix, p.modLoc("block/scaffold/block$suffix"))
                    .texture("top", p.modLoc("block/scaffold/" + name + "_scaffold_frame"))
                    .texture("inside", p.modLoc("block/scaffold/" + name + "_scaffold_inside"))
                    .texture("side", p.modLoc("block/scaffold/" + name + "_scaffold"))
                    .texture("casing", p.modLoc("block/" + name + "_casing"))
                    .texture("particle", p.modLoc("block/scaffold/" + name + "_scaffold"))
                )
                .build()
            }, MetalScaffoldingBlock.WATERLOGGED, MetalScaffoldingBlock.DISTANCE)
        }
        .onRegister(
          CreateRegistrate.connectedTextures {
            MetalScaffoldingCTBehaviour(
              scaffoldShift,
              scaffoldInsideShift,
              casingShift
            )
          }
        )
        .transform(TagGen.pickaxeOnly())
        .tag(BlockTags.CLIMBABLE)
        .item { pBlock: B, pProperties: Item.Properties? -> MetalScaffoldingBlockItem(pBlock, pProperties) }
        .recipe { c, p -> p.stonecutting(ingredient.get(), RecipeCategory.BUILDING_BLOCKS, { c.get() }, 2) }
        .model { c: DataGenContext<Item?, MetalScaffoldingBlockItem>, p: RegistrateItemModelProvider -> p.withExistingParent(c.name, p.modLoc("block/" + c.name)) }
        .build()
    }
  }
}