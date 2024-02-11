package com.dannbrown.databoxlib.datagen.content

object BlockPresets { // @ OTHER
  //  fun <B : Block, P> noBlockState(): NonNullUnaryOperator<BlockBuilder<B, P>> {
  //    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
  //      b.blockstate { _, _ -> }
  //    }
  //  }
  //
  //  fun <B : Block, P> bottomTopBlock(name: String, bottomName: String = name, topName: String = name
  //  ): NonNullUnaryOperator<BlockBuilder<B, P>> {
  //    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
  //      b.blockstate { c, p ->
  //          p.simpleBlock(c.get(),
  //            p.models()
  //              .withExistingParent(c.name, p.mcLoc("block/cube_bottom_top"))
  //              .texture("side", LibUtils.resourceLocation("block/$name"))
  //              .texture("bottom", LibUtils.resourceLocation("block/$bottomName"))
  //              .texture("top", LibUtils.resourceLocation("block/$topName")))
  //        }
  //    }
  //  }
  //
  //  fun <B : Block, P> carpetBlock(name: String): NonNullUnaryOperator<BlockBuilder<B, P>> {
  //    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
  //      b.blockstate { c, p ->
  //          p.simpleBlock(c.get(),
  //            p.models()
  //              .withExistingParent(c.name, p.mcLoc("block/carpet"))
  //              .texture("wool", LibUtils.resourceLocation("block/$name"))
  //              .renderType("cutout_mipped"))
  //        }
  //    }
  //  }
  // @ Plants
  //  fun <B : Block, P> simpleCrossBlock(name: String): NonNullUnaryOperator<BlockBuilder<B, P>> {
  //    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
  //      b.blockstate { c, p ->
  //        p.getVariantBuilder(c.get())
  //          .partialState()
  //          .setModels(*ConfiguredModel.builder()
  //            .modelFile(p.models()
  //              .withExistingParent(c.name, p.mcLoc("block/cross"))
  //              .texture("cross", p.modLoc("block/$name"))
  //              .texture("particle", p.modLoc("block/$name"))
  //              .renderType("cutout_mipped"))
  //            .build())
  //      }
  //    }
  //  }
  //  fun <B : Block, P> simpleLayerItem(name: String): NonNullUnaryOperator<BlockBuilder<B, P>> {
  //    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
  //      b.item()
  //        .model { c, p ->
  //          p.withExistingParent(c.name, p.mcLoc("item/generated"))
  //            .texture("layer0", p.modLoc("block/$name"))
  //        }
  //        .build()
  //    }
  //  }
  //  fun <B : Block, P> pottedPlantBlock(name: String, item: Supplier<ItemLike> //  ): NonNullUnaryOperator<BlockBuilder<B, P>> {
  //    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
  //      b.blockstate { c, p ->
  //        p.getVariantBuilder(c.get())
  //          .partialState()
  //          .setModels(*ConfiguredModel.builder()
  //            .modelFile(p.models()
  //              .withExistingParent(c.name, p.mcLoc("block/flower_pot_cross"))
  //              .texture("plant", p.modLoc("block/$name"))
  //              .texture("particle", p.modLoc("block/$name"))
  //              .renderType("cutout_mipped"))
  //            .build())
  //      }
  //        .loot { lt, b ->
  //          lt.add(b, lt.createPotFlowerItemTable(item.get()))
  //        }
  //    }
  //  }
  //  fun <B : Block, P> tallPlantTopBlock(name: String): NonNullUnaryOperator<BlockBuilder<B, P>> {
  //    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
  //      b.blockstate { c, p ->
  //        p.getVariantBuilder(c.get())
  //          .partialState()
  //          .with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)
  //          .setModels(*ConfiguredModel.builder()
  //            .modelFile(p.models()
  //              .withExistingParent(c.name + "_top", p.mcLoc("block/cross"))
  //              .texture("cross", p.modLoc("block/" + name + "_top"))
  //              .texture("particle", p.modLoc("block/" + name + "_top"))
  //              .renderType("cutout_mipped"))
  //            .build())
  //          .partialState()
  //          .with(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)
  //          .setModels(*ConfiguredModel.builder()
  //            .modelFile(p.models()
  //              .withExistingParent(c.name + "_bottom", p.mcLoc("block/cross"))
  //              .texture("cross", p.modLoc("block/" + name + "_bottom"))
  //              .texture("particle", p.modLoc("block/" + name + "_bottom"))
  //              .renderType("cutout_mipped"))
  //            .build())
  //      }
  //    }
  //  }
  //  fun <B : Block, P> tallPlantBottomBlock(name: String, customItem: Boolean = true
  //  ): NonNullUnaryOperator<BlockBuilder<B, P>> {
  //    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
  //      b.blockstate { c, p ->
  //        p.getVariantBuilder(c.get())
  //          .partialState()
  //          .setModels(*ConfiguredModel.builder()
  //            .modelFile(p.models()
  //              .withExistingParent(c.name, p.mcLoc("block/cross"))
  //              .texture("cross", p.modLoc("block/$name"))
  //              .texture("particle", p.modLoc("block/$name"))
  //              .renderType("cutout_mipped"))
  //            .build())
  //      }
  //        .item()
  //        .model { c, p ->
  //          p.withExistingParent(c.name, p.mcLoc("item/generated"))
  //            .texture("layer0", p.modLoc((if (customItem) "item/" else "block/") + name))
  //        }
  //        .build()
  //    }
  //  }
  // @ ORES
  //  fun <B : Block, P> oreBlock(name: String, replace: String = "stone"): NonNullUnaryOperator<BlockBuilder<B, P>> {
  //    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
  //      b.tag(Tags.Blocks.ORES)
  //        .transform(TagGen.tagBlockAndItem("ores/$name", "ores_in_ground/$replace"))
  //        .tag(Tags.Items.ORES)
  //        .build()
  //    }
  //  }
  //  fun <B : Block, P> oreLootTable(dropItem: Supplier<ItemLike>): NonNullUnaryOperator<BlockBuilder<B, P>> {
  //    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
  //      b.loot { lt, b ->
  //        lt.add(b,
  //          RegistrateBlockLootTables.createSilkTouchDispatchTable(b,
  //            lt.applyExplosionDecay(b,
  //              LootItem.lootTableItem(dropItem.get())
  //                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)))))
  //      }
  //    }
  //  }
  // @ STORAGE BLOCKS
  //  fun <B : Block, P> storageBlock(name: String): NonNullUnaryOperator<BlockBuilder<B, P>> {
  //    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
  //      b.tag(Tags.Blocks.STORAGE_BLOCKS)
  //        .transform(TagGen.tagBlockAndItem("storage_blocks/$name"))
  //        .tag(Tags.Items.STORAGE_BLOCKS)
  //        .build()
  //    }
  //  }
  // @ STAIRS
  //  private fun <B : Block, P> stairsBase(isWooden: Boolean = false): NonNullUnaryOperator<BlockBuilder<B, P>> {
  //    val blockTags = if (isWooden) listOf(BlockTags.STAIRS, BlockTags.WOODEN_STAIRS) else listOf(BlockTags.STAIRS)
  //    val itemTags = if (isWooden) listOf(ItemTags.STAIRS, ItemTags.WOODEN_STAIRS) else listOf(ItemTags.STAIRS)
  //    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
  //      b.tag(*blockTags.toTypedArray())
  //        .item()
  //        .tag(*itemTags.toTypedArray())
  //        .build()
  //    }
  //  }
  //  fun <B : Block, P> stairs(name: String, isWooden: Boolean = false): NonNullUnaryOperator<BlockBuilder<B, P>> {
  //    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
  //      b.transform(stairsBase<B, P>(isWooden))
  //        .blockstate { c, p -> p.stairsBlock(c.entry as StairBlock, LibUtils.resourceLocation("block/$name")) }
  //    }
  //  }
  //  fun <B : Block, P> bottomTopStairs(name: String, isWooden: Boolean = false): NonNullUnaryOperator<BlockBuilder<B, P>> {
  //    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
  //      b.transform(stairsBase<B, P>(isWooden))
  //        .blockstate { c, p ->
  //          p.stairsBlock(c.entry as StairBlock, LibUtils.resourceLocation("block/$name"), LibUtils.resourceLocation("block/$name" + "_bottom"), LibUtils.resourceLocation("block/$name" + "_top"))
  //        }
  //    }
  //  }
  // @ SLAB
  //  private fun <B : Block, P> slabBase(isWooden: Boolean = false): NonNullUnaryOperator<BlockBuilder<B, P>> {
  //    val blockTags = if (isWooden) listOf(BlockTags.SLABS, BlockTags.WOODEN_SLABS) else listOf(BlockTags.SLABS)
  //    val itemTags = if (isWooden) listOf(ItemTags.SLABS, ItemTags.WOODEN_SLABS) else listOf(ItemTags.SLABS)
  //    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
  //      b.tag(*blockTags.toTypedArray())
  //        .item()
  //        .tag(*itemTags.toTypedArray())
  //        .build()
  //    }
  //  }
  //  fun <B : Block, P> slab(name: String, isWooden: Boolean = false): NonNullUnaryOperator<BlockBuilder<B, P>> {
  //    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
  //      b.transform(slabBase<B, P>(isWooden))
  //        .blockstate { c, p ->
  //          val mainTexture = LibUtils.resourceLocation("block/$name")
  //          val sideTexture = LibUtils.resourceLocation("block/$name")
  //          val bottom = p.models()
  //            .slab(c.name, sideTexture, mainTexture, mainTexture)
  //          val top = p.models()
  //            .slabTop(c.name + "_top", sideTexture, mainTexture, mainTexture)
  //          val doubleSlab = p.models()
  //            .cubeColumn(c.name + "_double", sideTexture, mainTexture)
  //
  //          p.slabBlock(c.get() as SlabBlock, bottom, top, doubleSlab)
  //        }
  //        .item()
  //        .model { c, p -> p.withExistingParent(c.name, p.modLoc("block/" + c.name)) }
  //        .build()
  //    }
  //  }
  //
  //  fun <B : Block, P> bottomTopSlab(name: String, isWooden: Boolean = false): NonNullUnaryOperator<BlockBuilder<B, P>> {
  //    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
  //      b.transform(slabBase<B, P>(isWooden))
  //        .blockstate { c, p ->
  //          val topTexture = LibUtils.resourceLocation("block/$name" + "_top")
  //          val bottomTexture = LibUtils.resourceLocation("block/$name" + "_bottom")
  //          val sideTexture = LibUtils.resourceLocation("block/$name")
  //          val bottom = p.models()
  //            .slab(c.name, sideTexture, bottomTexture, topTexture)
  //          val top = p.models()
  //            .slabTop(c.name + "_top", sideTexture, bottomTexture, topTexture)
  //          val doubleSlab = p.models()
  //            .cubeBottomTop(c.name + "_double", sideTexture, bottomTexture, topTexture)
  //
  //          p.slabBlock(c.get() as SlabBlock, bottom, top, doubleSlab)
  //        }
  //        .item()
  //        .model { c, p -> p.withExistingParent(c.name, p.modLoc("block/" + c.name)) }
  //        .build()
  //    }
  //  }
  // @ WALLS
  //  private fun <B : Block, P> wallBase(): NonNullUnaryOperator<BlockBuilder<B, P>> {
  //    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
  //      b.tag(BlockTags.WALLS)
  //        .item()
  //        .tag(ItemTags.WALLS)
  //        .build()
  //    }
  //  }
  //
  //  fun <B : Block, P> wall(name: String): NonNullUnaryOperator<BlockBuilder<B, P>> {
  //    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
  //      b.transform(wallBase<B, P>())
  //        .blockstate { c, p -> p.wallBlock(c.get() as WallBlock, LibUtils.resourceLocation("block/$name")) }
  //        .item()
  //        .model { c, p -> p.wallInventory(c.name, LibUtils.resourceLocation("block/$name")) }
  //        .build()
  //    }
  //  }
  //  fun <B : Block, P> stalkWall(name: String): NonNullUnaryOperator<BlockBuilder<B, P>> {
  //    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
  //      b.tag(BlockTags.WALLS)
  //        .blockstate { c, p ->
  //          val postModel = p.models()
  //            .withExistingParent(c.name, p.modLoc("block/wall_special_post"))
  //            .texture("wall", LibUtils.resourceLocation("block/$name"))
  //            .texture("bottom", LibUtils.resourceLocation("block/$name" + "_top"))
  //            .texture("top", LibUtils.resourceLocation("block/$name" + "_top"))
  //          val sideModel = p.models()
  //            .withExistingParent(c.name + "_side", p.mcLoc("block/template_wall_side"))
  //            .texture("wall", LibUtils.resourceLocation("block/$name"))
  //          val tallSideModel = p.models()
  //            .withExistingParent(c.name + "_tall_side", p.mcLoc("block/template_wall_side_tall"))
  //            .texture("wall", LibUtils.resourceLocation("block/$name"))
  //          val builder = p.getMultipartBuilder(c.get())
  //            .part()
  //            .modelFile(postModel)
  //            .addModel()
  //            .condition(WallBlock.UP, true)
  //            .end()
  //
  //          BlockStateProvider.WALL_PROPS.entries.stream()
  //            .filter { (key): Map.Entry<Direction, Property<WallSide?>?> ->
  //              key.axis.isHorizontal
  //            }
  //            .forEach { e: Map.Entry<Direction, Property<WallSide>> ->
  //              builder.part()
  //                .modelFile(sideModel)
  //                .rotationY((e.key.toYRot()
  //                  .toInt() + 180) % 360)
  //                .uvLock(true)
  //                .addModel()
  //                .condition(e.value, WallSide.LOW)
  //              builder.part()
  //                .modelFile(tallSideModel)
  //                .rotationY((e.key.toYRot()
  //                  .toInt() + 180) % 360)
  //                .uvLock(true)
  //                .addModel()
  //                .condition(e.value, WallSide.TALL)
  //            }
  //        }
  //    }
  //  }
  //  fun <B : Item, P> stalkWallItem(name: String): NonNullUnaryOperator<ItemBuilder<B, P>> {
  //    return NonNullUnaryOperator { b: ItemBuilder<B, P> ->
  //      b.model { c, p ->
  //        p.withExistingParent(c.name, p.modLoc("block/wall_inventory_special_top"))
  //          .texture("wall", LibUtils.resourceLocation("block/$name"))
  //          .texture("down", LibUtils.resourceLocation("block/$name" + "_top"))
  //          .texture("top", LibUtils.resourceLocation("block/$name" + "_top"))
  //      }
  //    }
  //  }
  //  fun <B : Block, P> bottomTopWall(name: String): NonNullUnaryOperator<BlockBuilder<B, P>> {
  //    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
  //      b.transform(wallBase<B, P>())
  //        .blockstate { c, p ->
  //          val postModel = p.models()
  //            .withExistingParent(c.name, p.modLoc("block/wall_special_post"))
  //            .texture("wall", LibUtils.resourceLocation("block/$name"))
  //            .texture("bottom", LibUtils.resourceLocation("block/$name" + "_top"))
  //            .texture("top", LibUtils.resourceLocation("block/$name" + "_top"))
  //          val sideModel = p.models()
  //            .withExistingParent(c.name + "_side", p.mcLoc("block/template_wall_side"))
  //            .texture("wall", LibUtils.resourceLocation("block/$name"))
  //          val tallSideModel = p.models()
  //            .withExistingParent(c.name + "_tall_side", p.mcLoc("block/template_wall_side_tall"))
  //            .texture("wall", LibUtils.resourceLocation("block/$name"))
  //          val builder = p.getMultipartBuilder(c.get())
  //            .part()
  //            .modelFile(postModel)
  //            .addModel()
  //            .condition(WallBlock.UP, true)
  //            .end()
  //
  //          BlockStateProvider.WALL_PROPS.entries.stream()
  //            .filter { (key): Map.Entry<Direction, Property<WallSide?>?> ->
  //              key.axis.isHorizontal
  //            }
  //            .forEach { e: Map.Entry<Direction, Property<WallSide>> ->
  //              builder.part()
  //                .modelFile(sideModel)
  //                .rotationY((e.key.toYRot()
  //                  .toInt() + 180) % 360)
  //                .uvLock(true)
  //                .addModel()
  //                .condition(e.value, WallSide.LOW)
  //              builder.part()
  //                .modelFile(tallSideModel)
  //                .rotationY((e.key.toYRot()
  //                  .toInt() + 180) % 360)
  //                .uvLock(true)
  //                .addModel()
  //                .condition(e.value, WallSide.TALL)
  //            }
  //        }
  //        .item()
  //        .model { c, p ->
  //          p.withExistingParent(c.name, p.modLoc("block/wall_inventory_special_top"))
  //            .texture("wall", LibUtils.resourceLocation("block/$name"))
  //            .texture("down", LibUtils.resourceLocation("block/$name" + "_top"))
  //            .texture("top", LibUtils.resourceLocation("block/$name" + "_top"))
  //        }
  //        .build()
  //    }
  //  }
  // @ CASINGS
  //  fun <B : Block, P> casingBlock(spriteShift: Supplier<CTSpriteShiftEntry>): NonNullUnaryOperator<BlockBuilder<B, P>> {
  //    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
  //      b.blockstate { c: DataGenContext<Block?, B>, p: RegistrateBlockstateProvider -> p.simpleBlock(c.get()) }
  //        .onRegister(CreateRegistrate.connectedTextures { EncasedCTBehaviour(spriteShift.get()) })
  //        .onRegister(CreateRegistrate.casingConnectivity { block: B, cc: CasingConnectivity ->
  //          cc.makeCasing(block, spriteShift.get())
  //        })
  //        .tag(AllTags.AllBlockTags.CASING.tag)
  //        .item()
  //        .tag(AllTags.AllItemTags.CASING.tag)
  //        .build()
  //        .tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag)
  //    }
  //  }
  // @ Scaffolding
  //  fun <B : Block, P> scaffolding(name: String, scaffoldSpriteShift: CTSpriteShiftEntry, insideSpriteShift: CTSpriteShiftEntry, casingSpriteShift: CTSpriteShiftEntry
  //  ): NonNullUnaryOperator<BlockBuilder<B, P>> {
  //    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
  //      b.initialProperties { Blocks.SCAFFOLDING }
  //        .blockstate { c: DataGenContext<Block?, B>, p: RegistrateBlockstateProvider ->
  //          p.getVariantBuilder(c.get())
  //            .forAllStatesExcept({ s: BlockState ->
  //              val suffix = if (s.getValue(MetalScaffoldingBlock.BOTTOM)) "_horizontal" else ""
  //              ConfiguredModel.builder()
  //                .modelFile(p.models()
  //                  .withExistingParent(c.name + suffix, p.modLoc("block/scaffold/block$suffix"))
  //                  .texture("top", p.modLoc("block/scaffold/" + name + "_scaffold_frame"))
  //                  .texture("inside", p.modLoc("block/scaffold/" + name + "_scaffold_inside"))
  //                  .texture("side", p.modLoc("block/scaffold/" + name + "_scaffold"))
  //                  .texture("casing", p.modLoc("block/" + name + "_casing"))
  //                  .texture("particle", p.modLoc("block/scaffold/" + name + "_scaffold")))
  //                .build()
  //            }, MetalScaffoldingBlock.WATERLOGGED, MetalScaffoldingBlock.DISTANCE)
  //        }
  //        .onRegister(CreateRegistrate.connectedTextures {
  //          MetalScaffoldingCTBehaviour(scaffoldSpriteShift, insideSpriteShift, casingSpriteShift)
  //        })
  //        .tag(BlockTags.CLIMBABLE)
  //        .tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag)
  //        .item { pBlock: B, pProperties: Item.Properties? -> MetalScaffoldingBlockItem(pBlock, pProperties) }
  //        .model { c: DataGenContext<Item?, MetalScaffoldingBlockItem>, p: RegistrateItemModelProvider ->
  //          p.withExistingParent(c.name, p.modLoc("block/" + c.name))
  //        }
  //        .build()
  //    }
  //  }
  // @ Bars
  //  fun <B : Block, P> bars(
  //    name: String,
  //    specialEdge: Boolean = true,
  //  ): NonNullUnaryOperator<BlockBuilder<B, P>> {
  //    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
  //      b.initialProperties { Blocks.IRON_BARS }
  //        .tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag)
  //        .tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)
  //        .blockstate(MetalBarsGen.barsBlockState<IronBarsBlock>(name, specialEdge) as NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider>)
  //        .item()
  //        .model { c: DataGenContext<Item?, BlockItem?>, p: RegistrateItemModelProvider ->
  //          val barsTexture = p.modLoc("block/bars/" + name + "_bars")
  //          p.withExistingParent(c.name, p.modLoc("item/bars"))
  //            .texture("bars", barsTexture)
  //            .texture("edge", if (specialEdge) p.modLoc("block/bars/" + name + "_bars_edge") else barsTexture)
  //        }
  //        .build()
  //    }
  //  }
  //
  // @ LADDER
  //  fun <B : Block?, P> ladder(name: String): NonNullUnaryOperator<BlockBuilder<B, P>> {
  //    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
  //      b.initialProperties { Blocks.LADDER }
  //        .blockstate { c, p ->
  //          p.horizontalBlock(c.get(),
  //            p.models()
  //              .withExistingParent(c.name, p.modLoc("block/ladder"))
  //              .texture("texture", p.modLoc("block/ladder_$name"))
  //              .texture("particle", p.modLoc("block/ladder_$name"))
  //              .renderType("cutout_mipped"))
  //        }
  //        .properties { p: BlockBehaviour.Properties ->
  //          p.sound(SoundType.COPPER)
  //            .noOcclusion()
  //        }
  //        .tag(BlockTags.CLIMBABLE)
  //        .item()
  //        .model { c, p -> p.blockSprite(c::get, p.modLoc("block/ladder_$name")) }
  //        .build()
  //    }
  //  }
  // @ Fence
  // @ Fence Gate
  // @ Door
  // @ TRAPDOOR
  //  fun <B : Block, P> metalTrapdoor(name: String, orientable: Boolean): NonNullUnaryOperator<BlockBuilder<B, P>> {
  //    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
  //      b.initialProperties { SharedProperties.softMetal() }
  //        .transform(trapdoorBase<B, P>(name, orientable))
  //        .tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag)
  //    }
  //  }
  //
  //  fun <B : Block, P> woodenTrapdoor(name: String, orientable: Boolean): NonNullUnaryOperator<BlockBuilder<B, P>> {
  //    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
  //      b.transform(trapdoorBase<B, P>(name, orientable))
  //        .tag(BlockTags.WOODEN_TRAPDOORS)
  //        .item()
  //        .tag(ItemTags.WOODEN_TRAPDOORS)
  //        .build()
  //    }
  //  }
  //  private fun <B : Block, P> trapdoorBase(name: String, orientable: Boolean): NonNullUnaryOperator<BlockBuilder<B, P>> {
  //    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
  //      b.properties { p -> p.noOcclusion() }
  //        .blockstate { c, p ->
  //          p.trapdoorBlockWithRenderType(c.entry as TrapDoorBlock, name, LibUtils.resourceLocation("block/$name" + "_trapdoor"), orientable, "cutout_mipped")
  //        }
  //        .tag(BlockTags.TRAPDOORS)
  //        .onRegister(CreateRegistrate.connectedTextures { TrapdoorCTBehaviour() })
  //        .onRegister(AllInteractionBehaviours.interactionBehaviour(TrapdoorMovingInteraction()))
  //        .item()
  //        .model { c, p ->
  //          val texture = p.modLoc("block/$name" + "_trapdoor")
  //          p.withExistingParent(c.name, p.modLoc("block/$name" + "_trapdoor_bottom"))
  //            .texture("texture", texture)
  //            .texture("particle", texture)
  //            .renderType("cutout_mipped")
  //        }
  //        .tag(ItemTags.TRAPDOORS)
  //        .build()
  //    }
  //  }
  // @ Pressure Plate
  // @ Button
  // @ Sign
  // @ Sticker
  //  fun <B : Block, P> sticker(name: String): NonNullUnaryOperator<BlockBuilder<B, P>> {
  //    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
  //      b.initialProperties { Blocks.OAK_LEAVES }
  //        .properties { p ->
  //          p.mapColor(MapColor.NONE)
  //            .noOcclusion()
  //            .instabreak()
  //            .noCollission()
  //        }
  //        .tag(LibTags.databoxlibBlockTag("stickers"))
  //        .blockstate { c, p ->
  //          val baseModel = p.models()
  //            .withExistingParent(c.name + "_base", p.modLoc("block/sticker/base"))
  //            .texture("texture", p.modLoc("block/stickers/$name" + "_sticker"))
  //            .texture("particle", p.modLoc("block/stickers/$name" + "_sticker"))
  //          val wallModel90 = p.models()
  //            .withExistingParent(c.name + "_wall_r90", p.modLoc("block/sticker/wall_r90"))
  //            .texture("texture", p.modLoc("block/stickers/$name" + "_sticker"))
  //            .texture("particle", p.modLoc("block/stickers/$name" + "_sticker"))
  //          val wallModel180 = p.models()
  //            .withExistingParent(c.name + "_wall_r180", p.modLoc("block/sticker/wall_r180"))
  //            .texture("texture", p.modLoc("block/stickers/$name" + "_sticker"))
  //            .texture("particle", p.modLoc("block/stickers/$name" + "_sticker"))
  //          val wallModel270 = p.models()
  //            .withExistingParent(c.name + "_wall_r270", p.modLoc("block/sticker/wall_r270"))
  //            .texture("texture", p.modLoc("block/stickers/$name" + "_sticker"))
  //            .texture("particle", p.modLoc("block/stickers/$name" + "_sticker"))
  //
  //          p.getVariantBuilder(c.get())
  //            .forAllStatesExcept({ state ->
  //              ConfiguredModel.builder()
  //                .modelFile(if (state.getValue(BlockStateProperties.ATTACH_FACE) == AttachFace.WALL) when (state.getValue(StickerBlock.ROTATION)) {
  //                  RotationState.PI_0 -> baseModel
  //                  RotationState.PI_90 -> wallModel90
  //                  RotationState.PI_180 -> wallModel180
  //                  RotationState.PI_270 -> wallModel270
  //                }
  //                else baseModel)
  //                .rotationX(state.getValue(BlockStateProperties.ATTACH_FACE).ordinal * 90)
  //                .rotationY(((state.getValue(BlockStateProperties.HORIZONTAL_FACING)
  //                  .toYRot()
  //                  .toInt() + 180) + if (state.getValue(BlockStateProperties.ATTACH_FACE) == AttachFace.CEILING) 180 else 0) % 360)
  //                .build()
  //            }, BlockStateProperties.WATERLOGGED, StickerBlock.GLOWING)
  //        }
  //        .item()
  //        .model { c, p ->
  //          val texture = p.modLoc("block/stickers/$name" + "_sticker")
  //          p.withExistingParent(c.name, p.mcLoc("item/generated"))
  //            .texture("layer0", texture)
  //        }
  //
  //        //      .tab { DataboxCreativeTabs.TAB_STICKERS }
  //        .build()
  //    }
  //  }
  // @ RECIPES
//  fun <B : Block> polishedCraftingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>, amount: Int = 1
//  ) {
//    val recipePrefix = DataboxContent.MOD_ID + ":" + c.name
//
//    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), amount)
//      .define('X', ingredient.get())
//      .pattern("XX")
//      .pattern("XX")
//      .unlockedBy("has_ingredient",
//        ingredient.get()
//          .getCritereon(p))
//      .save({ t: FinishedRecipe -> p.accept(t) }, recipePrefix + "_from_block")
//  }
//
//  fun <B : Block> simpleStonecuttingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>, amount: Int = 1
//  ) {
//    p.stonecutting(ingredient.get(), RecipeCategory.BUILDING_BLOCKS, { c.get() }, amount)
//  }
//
//  fun <B : Block> stairsCraftingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
//  ) {
//    p.stairs(ingredient.get(), RecipeCategory.BUILDING_BLOCKS, { c.get() }, c.name, false)
//  }
//
//  fun <B : Block> stairsStonecuttingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
//  ) {
//    simpleStonecuttingRecipe(c, p, ingredient, 1)
//  }
//
//  fun <B : Block> slabCraftingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
//  ) {
//    p.slab(ingredient.get(), RecipeCategory.BUILDING_BLOCKS, { c.get() }, c.name, false)
//  }
//
//  fun <B : Block> fenceCraftingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
//  ) {
//    p.fence(ingredient.get(), RecipeCategory.BUILDING_BLOCKS, { c.get() }, c.name)
//  }
//
//  fun <B : Block> fenceGateCraftingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
//  ) {
//    p.fenceGate(ingredient.get(), RecipeCategory.BUILDING_BLOCKS, { c.get() }, c.name)
//  }
//
//  fun <B : Block> pressurePlateCraftingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
//  ) {
//    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 1)
//      .define('X', ingredient.get())
//      .pattern("XX")
//      .unlockedBy("has_" + p.safeName(ingredient.get()),
//        ingredient.get()
//          .getCritereon(p))
//      .save(p, p.safeId(c.get()))
//  }
//
//  fun <B : Block> directShapelessRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>, amount: Int = 1
//  ) {
//    ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, c.get(), amount)
//      .requires(ingredient.get())
//      .unlockedBy("has_" + p.safeName(ingredient.get()),
//        ingredient.get()
//          .getCritereon(p))
//      .save(p, LibUtils.resourceLocation("crafting/" + c.name + "_from_" + p.safeName(ingredient.get())))
//  }
//
//  fun <B : Block> directConversionRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>, result: Supplier<ItemLike>, amount: Int = 1
//  ) {
//    ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, result.get(), amount)
//      .requires(ingredient.get())
//      .unlockedBy("has_" + p.safeName(ingredient.get()),
//        ingredient.get()
//          .getCritereon(p))
//      .save(p, LibUtils.resourceLocation("crafting/" + p.safeName(result.get()) + "_from_" + p.safeName(ingredient.get())))
//  }
//
//  fun <B : Block> slabStonecuttingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
//  ) {
//    simpleStonecuttingRecipe(c, p, ingredient, 2)
//  }
//
//  fun <B : Block> slabRecycleRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<ItemLike>
//  ) {
//    val asIngredient = DataIngredient.items(c.get())
//    ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ingredient.get())
//      .requires(asIngredient)
//      .requires(asIngredient)
//      .unlockedBy("has_" + c.name, asIngredient.getCritereon(p))
//      .save(p, DataboxContent.MOD_ID + ":" + c.name + "_recycling")
//  }
//
//  fun <B : Block> slabToChiseledRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
//  ) {
//    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 1)
//      .define('X', ingredient.get())
//      .pattern("X")
//      .pattern("X")
//      .unlockedBy("has_" + p.safeName(ingredient.get()),
//        ingredient.get()
//          .getCritereon(p))
//      .save(p, p.safeId(c.get()))
//  }
//
//  fun <B : Block> signCraftingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
//  ) {
//    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 3)
//      .define('X', ingredient.get())
//      .define('S', Tags.Items.RODS_WOODEN)
//      .pattern("XXX")
//      .pattern("XXX")
//      .pattern(" S ")
//      .unlockedBy("has_" + p.safeName(ingredient.get()),
//        ingredient.get()
//          .getCritereon(p))
//      .save(p, p.safeId(c.get()))
//  }
//
//  fun <B : Block> wallCraftingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
//  ) {
//    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 6)
//      .define('X', ingredient.get())
//      .pattern("XXX")
//      .pattern("XXX")
//      .unlockedBy("has_" + p.safeName(ingredient.get()),
//        ingredient.get()
//          .getCritereon(p))
//      .save(p, p.safeId(c.get()))
//  }
//
//  fun <B : Block> wallStonecuttingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
//  ) {
//    simpleStonecuttingRecipe(c, p, ingredient, 1)
//  }
//
//  fun <B : Block> trapdoorStonecuttingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
//  ) {
//    simpleStonecuttingRecipe(c, p, ingredient, 1)
//  }
//
//  fun <B : Block> trapdoorCraftingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
//  ) {
//    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 2)
//      .pattern("XXX")
//      .pattern("XXX")
//      .define('X', ingredient.get())
//      .group("trapdoors")
//      .unlockedBy("has_" + p.safeName(ingredient.get()),
//        ingredient.get()
//          .getCritereon(p))
//      .save(p, p.safeId(c.get()))
//  }
//
//  fun <B : Block> doorCraftingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
//  ) {
//    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 3)
//      .pattern("XX")
//      .pattern("XX")
//      .pattern("XX")
//      .define('X', ingredient.get())
//      .group("doors")
//      .unlockedBy("has_" + p.safeName(ingredient.get()),
//        ingredient.get()
//          .getCritereon(p))
//      .save(p, p.safeId(c.get()))
//  }
//
//  fun <B : Block> barsStonecuttingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
//  ) {
//    simpleStonecuttingRecipe(c, p, ingredient, 4)
//  }
//
//  fun <B : Block> scaffoldingStonecuttingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
//  ) {
//    simpleStonecuttingRecipe(c, p, ingredient, 2)
//  }
//
//  fun <B : Block> storageBlockRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingotItem: Supplier<ItemLike>, ingredient: Supplier<DataIngredient>
//  ) {
//    val recipePrefix = DataboxContent.MOD_ID + ":" + c.name
//
//    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 1)
//      .define('I', ingredient.get())
//      .pattern("III")
//      .pattern("III")
//      .pattern("III")
//      .unlockedBy("has_ingredient",
//        ingredient.get()
//          .getCritereon(p))
//      .save({ t: FinishedRecipe -> p.accept(t) }, recipePrefix + "_from_materials")
//
//    ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ingotItem.get(), 9)
//      .requires(c.get())
//      .unlockedBy("has_ingredient",
//        ingredient.get()
//          .getCritereon(p))
//      .save({ t: FinishedRecipe -> p.accept(t) }, recipePrefix + "_to_materials")
//  }
//
//  fun <B : Block> smallStorageBlockRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingotItem: Supplier<ItemLike>, ingredient: Supplier<DataIngredient>
//  ) {
//    val recipePrefix = DataboxContent.MOD_ID + ":" + c.name
//
//    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 1)
//      .define('I', ingredient.get())
//      .pattern("II")
//      .pattern("II")
//      .unlockedBy("has_ingredient",
//        ingredient.get()
//          .getCritereon(p))
//      .save({ t: FinishedRecipe -> p.accept(t) }, recipePrefix + "_from_materials")
//
//    ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ingotItem.get(), 4)
//      .requires(c.get())
//      .unlockedBy("has_ingredient",
//        ingredient.get()
//          .getCritereon(p))
//      .save({ t: FinishedRecipe -> p.accept(t) }, recipePrefix + "_to_materials")
//  }
//
//  fun <B : Block> ladderStonecuttingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>
//  ) {
//    simpleStonecuttingRecipe(c, p, ingredient, 2)
//  } // @ LOOT

//  fun <B : Block, P> dropItselfLoot(): NonNullUnaryOperator<BlockBuilder<B, P>> {
//    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
//      b.loot { lt, b -> lt.dropSelf(b) }
//    }
//  }
//
//  fun <B : Block, P> dropOtherLoot(other: Supplier<ItemLike>): NonNullUnaryOperator<BlockBuilder<B, P>> {
//    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
//      b.loot { lt, b ->
//        lt.dropOther(b,
//          other.get()
//            .asItem())
//      }
//    }
//  }
//
//  fun <B : Block, P> dropSelfSilkLoot(other: Supplier<ItemLike>): NonNullUnaryOperator<BlockBuilder<B, P>> {
//    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
//      b.loot { lt, b ->
//        lt.add(b, RegistrateBlockLootTables.createSilkTouchDispatchTable(b, lt.applyExplosionDecay(b, LootItem.lootTableItem(other.get()))))
//      }
//    }
//  }
//
//  fun <B : Block, P> dropSelfSilkShearsOtherLoot(other: Supplier<ItemLike>, chance: Float = 1f
//  ): NonNullUnaryOperator<BlockBuilder<B, P>> {
//    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
//      b.loot { lt, b ->
//        lt.add(b,
//          createShearsDispatchTable(b,
//            lt.applyExplosionDecay(b,
//              LootItem.lootTableItem(other.get())
//                .`when`(LootItemRandomChanceCondition.randomChance(chance))
//                .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE, 2)))))
//      }
//    }
//  }
//
//  fun <B : Block, P> dropSelfSilkShearsLoot(): NonNullUnaryOperator<BlockBuilder<B, P>> {
//    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
//      b.loot { lt, b ->
//        lt.add(b, createShearsDispatchTable(b))
//      }
//    }
//  }
//
//  fun <B : Block, P> dropCropLoot(cropItem: Supplier<Item>, seedItem: Supplier<Item>, chance: Float = 0.5f
//  ): NonNullUnaryOperator<BlockBuilder<B, P>> {
//    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
//      b.loot { lt, b ->
//        lt.add(b, lt.createCropDrops(b, cropItem.get(), seedItem.get(), LootItemRandomChanceCondition.randomChance(chance)))
//      }
//    }
//  }
//
//  fun <B : Block, P> dropDoubleCropLoot(cropItem: Supplier<Item>, seedItem: Supplier<Item>, chance: Float = 0.25f, count: Float = 2f
//  ): NonNullUnaryOperator<BlockBuilder<B, P>> {
//    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
//      b.loot { lt, b ->
//        val builder: LootPoolEntryContainer.Builder<*> = LootItem.lootTableItem(b)
//          .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1f)))
//          .`when`(HAS_SHEARS)
//          .otherwise(lt.applyExplosionCondition(b, LootItem.lootTableItem(seedItem.get()))
//            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(count)))
//            .`when`(LootItemRandomChanceCondition.randomChance(chance))
//            .otherwise(LootItem.lootTableItem(cropItem.get())))
//        val pool = LootTable.lootTable()
//          .withPool(LootPool.lootPool()
//            .add(builder)
//            .`when`(LootItemBlockStatePropertyCondition.hasBlockStateProperties(b)
//              .setProperties(StatePropertiesPredicate.Builder.properties()
//                .hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)))
//            .`when`(LocationCheck.checkLocation(LocationPredicate.Builder.location()
//              .setBlock(BlockPredicate.Builder.block()
//                .of(b)
//                .setProperties(StatePropertiesPredicate.Builder.properties()
//                  .hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)
//                  .build())
//                .build()), BlockPos(0, 1, 0))))
//          .withPool(LootPool.lootPool()
//            .add(builder)
//            .`when`(LootItemBlockStatePropertyCondition.hasBlockStateProperties(b)
//              .setProperties(StatePropertiesPredicate.Builder.properties()
//                .hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)))
//            .`when`(LocationCheck.checkLocation(LocationPredicate.Builder.location()
//              .setBlock(BlockPredicate.Builder.block()
//                .of(b)
//                .setProperties(StatePropertiesPredicate.Builder.properties()
//                  .hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)
//                  .build())
//                .build()), BlockPos(0, -1, 0))))
//
//        lt.add(b, pool)
//      }
//    }
//  }
//
//  fun <B : Block, P> dropDoubleFlowerLoot(): NonNullUnaryOperator<BlockBuilder<B, P>> {
//    return NonNullUnaryOperator { b: BlockBuilder<B, P> ->
//      b.loot { lt, b ->
//        lt.add(b, lt.createDoorTable(b))
//      }
//    }
//  }

  // loot table private methods
//  private fun createSelfDropDispatchTable(pBlock: Block, pConditionBuilder: LootItemCondition.Builder, pAlternativeBuilder: LootPoolEntryContainer.Builder<*>
//  ): LootTable.Builder {
//    return LootTable.lootTable()
//      .withPool(LootPool.lootPool()
//        .setRolls(ConstantValue.exactly(1.0f))
//        .add(LootItem.lootTableItem(pBlock)
//          .`when`(pConditionBuilder)
//          .otherwise(pAlternativeBuilder)))
//  }
//
//  private fun createSelfDropDispatchTable(
//    pBlock: Block,
//    pConditionBuilder: LootItemCondition.Builder,
//  ): LootTable.Builder {
//    return LootTable.lootTable()
//      .withPool(LootPool.lootPool()
//        .setRolls(ConstantValue.exactly(1.0f))
//        .add(LootItem.lootTableItem(pBlock)
//          .`when`(pConditionBuilder)))
//  }
//
//  private fun createShearsDispatchTable(pBlock: Block, pBuilder: LootPoolEntryContainer.Builder<*>
//  ): LootTable.Builder {
//    return createSelfDropDispatchTable(pBlock, HAS_SHEARS, pBuilder)
//  }
//
//  private fun createShearsDispatchTable(
//    pBlock: Block,
//  ): LootTable.Builder {
//    return createSelfDropDispatchTable(pBlock, HAS_SHEARS)
//  }
//
//  private val HAS_SHEARS = MatchTool.toolMatches(ItemPredicate.Builder.item()
//    .of(Items.SHEARS))
}