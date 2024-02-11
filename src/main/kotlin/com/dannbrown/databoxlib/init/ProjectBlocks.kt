package com.dannbrown.databoxlib.init

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.content.block.AbsorberBlock
import com.dannbrown.databoxlib.content.block.BuddingBushBlock
import com.dannbrown.databoxlib.content.block.BuddingCottonBlock
import com.dannbrown.databoxlib.content.block.CottonCropBlock
import com.dannbrown.databoxlib.content.block.CustomBacktankBlock
import com.dannbrown.databoxlib.content.block.FlammableSandBlock
import com.dannbrown.databoxlib.content.block.GenericDoubleFlowerBlock
import com.dannbrown.databoxlib.content.block.GenericDoublePlantBlock
import com.dannbrown.databoxlib.content.block.GenericFlowerBlock
import com.dannbrown.databoxlib.content.block.GenericGrassBlock
import com.dannbrown.databoxlib.content.block.GenericTallGrassBlock
import com.dannbrown.databoxlib.content.block.GenericTallGrassTopBlock
import com.dannbrown.databoxlib.content.block.GuayuleShrubBlock
import com.dannbrown.databoxlib.content.block.GuayuleShrubTopBlock
import com.dannbrown.databoxlib.content.block.HyperHeaterBlock
import com.dannbrown.databoxlib.content.block.StickerBlock
import com.dannbrown.databoxlib.content.block.cargoBay.CargoBayBlock
import com.dannbrown.databoxlib.content.block.cargoBay.CargoBayCTBehaviour
import com.dannbrown.databoxlib.content.block.cargoBay.CargoBayItem
import com.dannbrown.databoxlib.content.block.invertedClutch.InvertedClutchBlock
import com.dannbrown.databoxlib.content.block.kineticElectrolyzer.KineticElectrolyzerBlock
import com.dannbrown.databoxlib.content.block.mechanicalCooler.MechanicalCoolerBlock
import com.dannbrown.databoxlib.content.block.pressureChamberValve.PressureChamberCapBlock
import com.dannbrown.databoxlib.content.block.resistor.ResistorBlock
import com.dannbrown.databoxlib.content.ship.block.AntimatterBlock
import com.dannbrown.databoxlib.content.ship.block.GravityStabilizerBlock
import com.dannbrown.databoxlib.content.ship.block.MassNullifierBlock
import com.dannbrown.databoxlib.content.ship.block.altitudeSensor.AltitudeSensorBlock
import com.dannbrown.databoxlib.content.ship.block.angularSensor.AngularSensorBlock
import com.dannbrown.databoxlib.content.ship.block.captainSeat.CaptainSeatBlock
import com.dannbrown.databoxlib.content.ship.block.distanceSensor.DistanceSensorBlock
import com.dannbrown.databoxlib.content.ship.block.sail.SailBlock
import com.dannbrown.databoxlib.content.ship.block.shipConsole.ShipConsoleBlock
import com.dannbrown.databoxlib.content.ship.block.thruster.ThrusterBlock
import com.dannbrown.databoxlib.content.utils.spriteShifts.CreateBlockSpriteShifts
import com.dannbrown.databoxlib.content.utils.toolTiers.SetTier
import com.dannbrown.databoxlib.content.utils.toolTiers.SetTool
import com.dannbrown.databoxlib.content.worldgen.tree.JoshuaTreeGrower
import com.dannbrown.databoxlib.datagen.content.BlockFamily
import com.dannbrown.databoxlib.datagen.content.BlockFamilyGen
import com.dannbrown.databoxlib.datagen.content.BlockGen
import com.dannbrown.databoxlib.datagen.transformers.BlockLootPresets
import com.dannbrown.databoxlib.datagen.transformers.BlockstatePresets
import com.dannbrown.databoxlib.datagen.transformers.ItemModelPresets
import com.dannbrown.databoxlib.datagen.valkyrienSkies.ProjectMass
import com.dannbrown.databoxlib.lib.LibData
import com.dannbrown.databoxlib.lib.LibTags
import com.simibubi.create.content.decoration.encasing.CasingBlock
import com.simibubi.create.content.kinetics.BlockStressDefaults
import com.simibubi.create.content.kinetics.base.KineticBlock
import com.simibubi.create.foundation.data.AssetLookup
import com.simibubi.create.foundation.data.BlockStateGen
import com.simibubi.create.foundation.data.BuilderTransformers
import com.simibubi.create.foundation.data.CreateRegistrate
import com.simibubi.create.foundation.data.ModelGen
import com.simibubi.create.foundation.data.SharedProperties
import com.simibubi.create.foundation.data.TagGen
import com.tterrag.registrate.util.DataIngredient
import com.tterrag.registrate.util.entry.BlockEntry
import net.minecraft.core.Direction.Axis
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.AmethystClusterBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.CarpetBlock
import net.minecraft.world.level.block.DoublePlantBlock
import net.minecraft.world.level.block.FlowerPotBlock
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.SandBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import net.minecraft.world.level.material.MapColor
import net.minecraftforge.client.model.generators.ConfiguredModel
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries

object ProjectBlocks {
  private val BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ProjectContent.MOD_ID)
  private val BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ProjectContent.MOD_ID)
  fun register(bus: IEventBus?) {
    BLOCKS.register(bus)
    BLOCK_ITEMS.register(bus)
  }

  val SAIL_BLOCK = BlockGen<SailBlock>("sail_block")
//    .color(MapColor.COLOR_GRAY)
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .blockFactory { p -> SailBlock(p) }
    .blockstate(BlockstatePresets.horizontalAxisBlock("sail_block_side", "sail_block_side", "sail_block_side", "sail_block_front"))
    .register()
  val SHIP_CONSOLE = BlockGen<ShipConsoleBlock>("ship_console").color(MapColor.COLOR_GRAY)
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .blockFactory { p -> ShipConsoleBlock(p) }
    .blockstate(BlockstatePresets.horizontalAxisBlock("ship_console_side", "ship_console_side", "ship_console_side", "ship_console_front"))
    .register()
  val GRAVITY_STABILIZER = BlockGen<GravityStabilizerBlock>("gravity_stabilizer").color(MapColor.COLOR_GRAY)
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .blockFactory { p -> GravityStabilizerBlock(p) }
    .blockstate(BlockstatePresets.poweredBlock())
    .register()
  val MASS_NULLIFIER_BLOCK = BlockGen<MassNullifierBlock>("mass_nullifier_block").color(MapColor.COLOR_GRAY)
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .blockFactory { p -> MassNullifierBlock(p) }
    .blockstate(BlockstatePresets.poweredBlock())
    .register()
  val ANTIMATTER_BLOCK = BlockGen<AntimatterBlock>("antimatter_block").color(MapColor.COLOR_GRAY)
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .blockFactory { p -> AntimatterBlock(p) }
    .transform { t -> t.onRegister(ProjectMass.register(0f)) }
    .register()
  val CAPTAIN_SEAT = BlockGen<CaptainSeatBlock>("captain_seat").color(MapColor.COLOR_GRAY)
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .blockFactory { p -> CaptainSeatBlock(p.noOcclusion()) }
    .blockstate(BlockstatePresets.halfBlock("captain_seat"))
    .register()
  val BASIC_THRUSTER = BlockGen<ThrusterBlock>("basic_thruster").color(MapColor.COLOR_GRAY)
    .properties { p -> p.sound(SoundType.NETHERITE_BLOCK) }
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .blockFactory { p -> ThrusterBlock(p.noOcclusion(), 1) }
    .blockstate(BlockstatePresets.thrusterBlock())
    .blockTags(listOf(ProjectTags.BLOCK.IS_THRUSTER))
    .itemTags(listOf(ProjectTags.ITEM.IS_THRUSTER))
    .transform { t -> t.onRegister(ProjectMass.register(100f)) }
    .transform(BlockStressDefaults.setImpact(1.0))
    .transform { t ->
      t.item()
        .model(ItemModelPresets.thrusterItem())
        .build()
    }
    .register()
  val SCALAR_THRUSTER = BlockGen<ThrusterBlock>("scalar_thruster").color(MapColor.COLOR_GRAY)
    .properties { p -> p.sound(SoundType.NETHERITE_BLOCK) }
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .blockFactory { p -> ThrusterBlock(p.noOcclusion(), 100) }
    .blockstate(BlockstatePresets.thrusterBlock())
    .blockTags(listOf(ProjectTags.BLOCK.IS_THRUSTER))
    .itemTags(listOf(ProjectTags.ITEM.IS_THRUSTER))
    .transform { t -> t.onRegister(ProjectMass.register(100f)) }
    .transform(BlockStressDefaults.setImpact(1.0))
    .transform { t ->
      t.item()
        .model(ItemModelPresets.thrusterItem())
        .build()
    }
    .register()
  val INVERTED_CLUTCH = BlockGen<InvertedClutchBlock>("inverted_clutch")
    .blockFactory { p -> InvertedClutchBlock(p.noOcclusion()) }
    .copyFrom({ Blocks.ANDESITE })
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .blockstate { c, p -> BlockStateGen.axisBlock(c, p, AssetLookup.forPowered(c, p)) }
    .blockTags(listOf(BlockTags.MINEABLE_WITH_AXE))
    .transform(BlockStressDefaults.setNoImpact())
    .transform { t -> t.onRegister(ProjectMass.register(100f)) }
    .transform { t ->
      t.item()
        .transform(ModelGen.customItemModel())
    }
    .register()
  val RESISTOR = BlockGen<ResistorBlock>("resistor")
    .blockFactory { p -> ResistorBlock(p.noOcclusion()) }
    .copyFrom({ Blocks.ANDESITE })
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .blockstate(BlockstatePresets.resistorBlock())
    .blockTags(listOf(BlockTags.MINEABLE_WITH_AXE))
    .transform(BlockStressDefaults.setNoImpact())
    .transform { t -> t.onRegister(ProjectMass.register(100f)) }
    .transform { t ->
      t.item()
        .transform(ModelGen.customItemModel())
    }
    .register()
  val DISTANCE_SENSOR = BlockGen<DistanceSensorBlock>("distance_sensor").color(MapColor.COLOR_GRAY)
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .blockFactory { p -> DistanceSensorBlock(p) }
    .blockstate(BlockstatePresets.axisBlock("distance_sensor_side", "distance_sensor_side", "distance_sensor_side", "distance_sensor_front"))
    .register()
  val ALTITUDE_SENSOR = BlockGen<AltitudeSensorBlock>("altitude_sensor").color(MapColor.COLOR_GRAY)
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .blockFactory { p -> AltitudeSensorBlock(p) }
    .blockstate(BlockstatePresets.axisBlock("altitude_sensor_side", "altitude_sensor_side", "altitude_sensor_side", "altitude_sensor_front"))
    .register()
  val ANGULAR_SENSOR = BlockGen<AngularSensorBlock>("angular_sensor").color(MapColor.COLOR_GRAY)
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .blockFactory { p -> AngularSensorBlock(p) }
    .blockstate(BlockstatePresets.angularSensor("angular_sensor"))
    .register()
  val OXYGEN_EMITTER = BlockGen<Block>(LibData.BLOCKS.OXYGEN_EMITTER).color(MapColor.COLOR_GRAY)
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .register()
  val STEEL_BACKTANK: BlockEntry<CustomBacktankBlock> = ProjectContent.REGISTRATE.block<CustomBacktankBlock>(LibData.NAMES.STEEL + "_backtank") { p -> CustomBacktankBlock(p) }
    .initialProperties { Blocks.IRON_BLOCK }
    .transform(BuilderTransformers.backtank { ProjectItems.STEEL_BACKTANK.first.get() })
    .register()

  //  val CONTRAPTION_ACTOR_SAMPLE = BlockGen<SailBlock>("sail_block").color(MapColor.COLOR_GRAY)
//    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
//    .blockFactory { p -> SailBlock(p) }
//    .transform { t -> t.onRegister(AllInteractionBehaviours.interactionBehaviour(SailBlockMovingInteraction())) }
//    .transform { t -> t.onRegister(AllMovementBehaviours.movementBehaviour(SailBlockMovement())) }
//    .register()
  // @ Functional Blocks
  val MECHANICAL_COOLER = ProjectContent.REGISTRATE.block<KineticBlock>(LibData.BLOCKS.MECHANICAL_COOLER) { props ->
    MechanicalCoolerBlock(props)
  }
    .initialProperties { SharedProperties.softMetal() }
    .defaultBlockstate()
    .blockstate { c, p -> p.simpleBlock(c.entry, AssetLookup.partialBaseModel(c, p)) }
    .properties {
      BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
        .noOcclusion()
    }
    .item()
    .build()
    .transform(BlockStressDefaults.setImpact(8.0))
    .transform(TagGen.pickaxeOnly())
    .register()
  val KINETIC_ELECTROLYZER = ProjectContent.REGISTRATE.block<KineticBlock>(LibData.BLOCKS.KINETIC_ELECTROLYZER) { props ->
    KineticElectrolyzerBlock(props)
  }
    .initialProperties { SharedProperties.softMetal() }
    .blockstate { c, p -> p.horizontalBlock(c.entry, AssetLookup.partialBaseModel(c, p)) }
    .properties {
      BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
        .noOcclusion()
    }
    .item()
    .model { c, p ->
      p.withExistingParent(c.name, p.modLoc("block/kinetic_electrolyzer/block"))
    }
    .build()
    .transform(BlockStressDefaults.setImpact(8.0))
    .transform(TagGen.pickaxeOnly())
    .register()
  val PRESSURE_CHAMBER_VALVE = ProjectContent.REGISTRATE.block<PressureChamberCapBlock>(LibData.BLOCKS.PRESSURE_CHAMBER_CAP) { p ->
    PressureChamberCapBlock(p)
  }
    .initialProperties { SharedProperties.softMetal() }
    .properties { p ->
      p.sound(SoundType.NETHERITE_BLOCK)
        .noOcclusion()
        .strength(3f)
    }
    .blockstate(BlockstatePresets.pressureChamberValveBlock())
    .item()
    .model { c, p ->
      p.withExistingParent(c.name, p.modLoc("block/pressure_chamber/block"))
    }
    .build()
    .register()
  val CARGO_BAY = ProjectContent.REGISTRATE.block<CargoBayBlock>(LibData.BLOCKS.CARGO_BAY) { props ->
    CargoBayBlock(props)
  }
    .initialProperties { SharedProperties.softMetal() }
    .onRegister(CreateRegistrate.connectedTextures { CargoBayCTBehaviour() })
    .properties { p ->
      p.mapColor(MapColor.TERRACOTTA_MAGENTA)
    }
    .properties { p ->
      p.sound(SoundType.NETHERITE_BLOCK)
        .explosionResistance(1200f)
    }
    .defaultBlockstate()
    .transform(TagGen.pickaxeOnly())
    .blockstate { c, p ->
      p.getVariantBuilder(c.get())
        .forAllStates { s ->
          ConfiguredModel.builder()
            .modelFile(AssetLookup.standardModel(c, p))
            .rotationY(if (s.getValue(CargoBayBlock.HORIZONTAL_AXIS) == Axis.X) 90
            else 0)
            .build()
        }
    }
    .item { b, p ->
      CargoBayItem(b, p)
    }
    .build()
    .register()

  //  val PASSIVE_HEATER = BlockGen<PassiveHeaterBlock>("passive_heater").blockFactory { p -> PassiveHeaterBlock(p) }.color(MapColor.COLOR_RED).properties { p -> p.sound(SoundType.METAL).noOcclusion() }.customPartialModel().register()
  val HYPER_HEATER = BlockGen<HyperHeaterBlock>("hyper_heater").blockFactory { p ->
    HyperHeaterBlock(p)
  }
    .color(MapColor.COLOR_RED)
    .properties { p ->
      p.sound(SoundType.METAL)
        .noOcclusion()
    }
    .customPartialModel()
    .register()

  //  Other casings
  val WALL_PANEL = BlockGen<CasingBlock>(LibData.BLOCKS.WALL_PANEL).metalPanelCasing(CreateBlockSpriteShifts.WALL_PANEL, 0, false)
    .toolAndTier(SetTool.PICKAXE, SetTier.STONE)
    .color(MapColor.SNOW)
    .register()
  val HULL_PANEL = BlockGen<CasingBlock>(LibData.BLOCKS.HULL_PANEL).metalPanelCasing(CreateBlockSpriteShifts.HULL_PANEL, 0, false)
    .toolAndTier(SetTool.PICKAXE, SetTier.STONE)
    .color(MapColor.SNOW)
    .register()

  // @ Create Blocks Registering
  val STEEL_PURPLE_PANEL_CASING = BlockGen<CasingBlock>(LibData.BLOCKS.STEEL_PURPLE_PANEL_CASING).metalPanelCasing(CreateBlockSpriteShifts.STEEL_PURPLE_PANEL_CASING, 10, false)
    .toolAndTier(SetTool.PICKAXE, SetTier.STONE)
    .color(MapColor.COLOR_PURPLE)
    .register()
  val STEEL_BLUE_PANEL_CASING = BlockGen<CasingBlock>(LibData.BLOCKS.STEEL_BLUE_PANEL_CASING).metalPanelCasing(CreateBlockSpriteShifts.STEEL_BLUE_PANEL_CASING, 10, false)
    .toolAndTier(SetTool.PICKAXE, SetTier.STONE)
    .color(MapColor.COLOR_BLUE)
    .register()
  val STEEL_ORANGE_PANEL_CASING = BlockGen<CasingBlock>(LibData.BLOCKS.STEEL_ORANGE_PANEL_CASING).metalPanelCasing(CreateBlockSpriteShifts.STEEL_ORANGE_PANEL_CASING, 10, false)
    .toolAndTier(SetTool.PICKAXE, SetTier.STONE)
    .color(MapColor.COLOR_ORANGE)
    .register()
  val STEEL_RED_PANEL_CASING = BlockGen<CasingBlock>(LibData.BLOCKS.STEEL_RED_PANEL_CASING).metalPanelCasing(CreateBlockSpriteShifts.STEEL_RED_PANEL_CASING, 10, false)
    .toolAndTier(SetTool.PICKAXE, SetTier.STONE)
    .color(MapColor.COLOR_RED)
    .register()
  val STEEL_GREEN_PANEL_CASING = BlockGen<CasingBlock>(LibData.BLOCKS.STEEL_GREEN_PANEL_CASING).metalPanelCasing(CreateBlockSpriteShifts.STEEL_GREEN_PANEL_CASING, 10, false)
    .toolAndTier(SetTool.PICKAXE, SetTier.STONE)
    .color(MapColor.COLOR_GREEN)
    .register()
  val STEEL_FAMILY = BlockFamilyGen(LibData.NAMES.STEEL).color(MapColor.COLOR_GRAY)
    .denyList(BlockFamily.Type.LAMP)
    .toolAndTier(SetTool.PICKAXE, SetTier.STONE)
    .metalCasingFamily({
      DataIngredient.tag(LibTags.forgeItemTag("ingots/steel"))
    }, CreateBlockSpriteShifts.STEEL_CASING, CreateBlockSpriteShifts.STEEL_PURPLE_PANEL_CASING, CreateBlockSpriteShifts.STEEL_SCAFFOLD, CreateBlockSpriteShifts.STEEL_SCAFFOLD_INSIDE)

  // @ BLOCKS
  //  val ADAMANTIUM_BLOCK = BlockGen<Block>(LibData.NAMES.ADAMANTIUM).storageBlock({ DataboxItems.ADAMANTIUM_INGOT.get() }, {
//    DataIngredient.tag(LibTags.forgeItemTag("ingots/adamantium"))
//  })
//    .copyFrom({Blocks.NETHERITE_BLOCK})
//    .color(MapColor.COLOR_BLACK)
//    .toolAndTier(SetTool.PICKAXE, SetTier.ADAMANTIUM)
//    .register()
//  val RAW_ADAMANTIUM_BLOCK = BlockGen<Block>(LibData.NAMES.RAW_ADAMANTIUM).storageBlock({ DataboxItems.ADAMANTIUM_FRAGMENT.get() }, {
//    DataIngredient.tag(LibTags.forgeItemTag("raw_materials/adamantium"))
//  })
//    .copyFrom({Blocks.ANCIENT_DEBRIS})
//    .color(MapColor.COLOR_BLACK)
//    .toolAndTier(SetTool.PICKAXE, SetTier.NETHERITE)
//    .register()
//  val BASALT_ADAMANTIUM_ORE = BlockGen<RotatedPillarBlock>(LibData.NAMES.ADAMANTIUM).prefix("basalt_")
//    .rotatedPillarBlock()
//    .oreBlock({ DataboxItems.ADAMANTIUM_FRAGMENT.get() }, "basalt")
//    .copyFrom({Blocks.ANCIENT_DEBRIS})
//    .color(MapColor.COLOR_GRAY)
//    .toolAndTier(SetTool.PICKAXE, SetTier.NETHERITE)
//    .blockTags(listOf(DataboxTags.BLOCK.ADAMANTIUM_ORES))
//    .register()
//  val BRONZE_BLOCK = BlockGen<Block>(LibData.NAMES.BRONZE).storageBlock({ DataboxItems.BRONZE_INGOT.get() }, {
//    DataIngredient.tag(LibTags.forgeItemTag("ingots/bronze"))
//  })
//    .copyFrom({Blocks.IRON_BLOCK})
//    .color(MapColor.COLOR_ORANGE)
//    .toolAndTier(SetTool.PICKAXE, SetTier.STONE)
//    .register()
  val STEEL_BLOCK = BlockGen<Block>(LibData.NAMES.STEEL).storageBlock({ ProjectItems.STEEL_INGOT.get() }, {
    DataIngredient.tag(LibTags.forgeItemTag("ingots/steel"))
  })
    .copyFrom({ Blocks.IRON_BLOCK })
    .color(MapColor.COLOR_GRAY)
    .toolAndTier(SetTool.PICKAXE, SetTier.IRON)
    .register()

  // tungsten
  val TUNGSTEN_ORE = BlockGen<Block>(LibData.NAMES.TUNGSTEN).oreBlock({ ProjectItems.RAW_TUNGSTEN.get() })
    .copyFrom({ Blocks.DIAMOND_ORE })
    .color(MapColor.TERRACOTTA_YELLOW)
    .toolAndTier(SetTool.PICKAXE, SetTier.IRON)
    .blockTags(listOf(ProjectTags.BLOCK.TUNGSTEN_ORES))
    .register()
  val DEEPSLATE_TUNGSTEN_ORE = BlockGen<Block>(LibData.NAMES.TUNGSTEN).prefix("deepslate_")
    .oreBlock({ ProjectItems.RAW_TUNGSTEN.get() }, "deepslate")
    .copyFrom({ Blocks.DEEPSLATE_DIAMOND_ORE })
    .color(MapColor.TERRACOTTA_YELLOW)
    .toolAndTier(SetTool.PICKAXE, SetTier.IRON)
    .blockTags(listOf(ProjectTags.BLOCK.TUNGSTEN_ORES))
    .register()
  val RAW_TUNGSTEN_BLOCK = BlockGen<Block>(LibData.NAMES.RAW_TUNGSTEN).storageBlock({ ProjectItems.RAW_TUNGSTEN.get() }, {
    DataIngredient.tag(LibTags.forgeItemTag("raw_materials/tungsten"))
  })
    .copyFrom({ Blocks.RAW_COPPER_BLOCK })
    .color(MapColor.TERRACOTTA_YELLOW)
    .toolAndTier(SetTool.PICKAXE, SetTier.IRON)
    .register()
  val TUNGSTEN_BLOCK = BlockGen<Block>(LibData.NAMES.TUNGSTEN).storageBlock({ ProjectItems.TUNGSTEN_INGOT.get() }, {
    DataIngredient.tag(LibTags.forgeItemTag("ingots/tungsten"))
  })
    .copyFrom({ Blocks.IRON_BLOCK })
    .color(MapColor.COLOR_GRAY)
    .toolAndTier(SetTool.PICKAXE, SetTier.IRON)
    .register()

  // titanium
  val TITANIUM_BLOCK = BlockGen<Block>(LibData.NAMES.TITANIUM).storageBlock({ ProjectItems.TITANIUM_INGOT.get() }, {
    DataIngredient.tag(LibTags.forgeItemTag("ingots/titanium"))
  })
    .copyFrom({ Blocks.IRON_BLOCK })
    .color(MapColor.COLOR_GRAY)
    .toolAndTier(SetTool.PICKAXE, SetTier.IRON)
    .register()

  //  val TIN_ORE = BlockGen<Block>(LibData.NAMES.TIN).oreBlock({ DataboxItems.RAW_TIN.get() })
//    .copyFrom({Blocks.COPPER_ORE})
//    .color(MapColor.TERRACOTTA_YELLOW)
//    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
//    .blockTags(listOf(DataboxTags.BLOCK.TIN_ORES))
//    .register()
//  val DEEPSLATE_TIN_ORE = BlockGen<Block>(LibData.NAMES.TIN).prefix("deepslate_")
//    .oreBlock({ DataboxItems.RAW_TIN.get() }, "deepslate")
//    .copyFrom({Blocks.DEEPSLATE_COPPER_ORE})
//    .color(MapColor.TERRACOTTA_ORANGE)
//    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
//    .blockTags(listOf(DataboxTags.BLOCK.TIN_ORES))
//    .register()
//  val RAW_TIN_BLOCK = BlockGen<Block>(LibData.NAMES.RAW_TIN).storageBlock({ DataboxItems.RAW_TIN.get() }, {
//    DataIngredient.tag(LibTags.forgeItemTag("raw_materials/tin"))
//  })
//    .copyFrom({Blocks.RAW_COPPER_BLOCK})
//    .color(MapColor.TERRACOTTA_YELLOW)
//    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
//    .register()
//  val TIN_BLOCK = BlockGen<Block>(LibData.NAMES.TIN).storageBlock({ DataboxItems.TIN_INGOT.get() }, {
//    DataIngredient.tag(LibTags.forgeItemTag("ingots/tin"))
//  })
//    .copyFrom({Blocks.COPPER_BLOCK})
//    .color(MapColor.METAL)
//    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
//    .register()
  val ALUMINIUM_ORE = BlockGen<Block>(LibData.NAMES.ALUMINIUM).oreBlock({ ProjectItems.RAW_ALUMINIUM.get() })
    .copyFrom({ Blocks.IRON_ORE })
    .color(MapColor.TERRACOTTA_PINK)
    .toolAndTier(SetTool.PICKAXE, SetTier.STONE)
    .blockTags(listOf(ProjectTags.BLOCK.ALUMINIUM_ORES))
    .register()
  val DEEPSLATE_ALUMINIUM_ORE = BlockGen<Block>(LibData.NAMES.ALUMINIUM).prefix("deepslate_")
    .oreBlock({ ProjectItems.RAW_ALUMINIUM.get() }, "deepslate")
    .copyFrom({ Blocks.DEEPSLATE_IRON_ORE })
    .color(MapColor.TERRACOTTA_MAGENTA)
    .toolAndTier(SetTool.PICKAXE, SetTier.STONE)
    .blockTags(listOf(ProjectTags.BLOCK.ALUMINIUM_ORES))
    .register()
  val RAW_ALUMINIUM_BLOCK = BlockGen<Block>(LibData.NAMES.RAW_ALUMINIUM).storageBlock({ ProjectItems.RAW_ALUMINIUM.get() }, {
    DataIngredient.tag(LibTags.forgeItemTag("raw_materials/aluminium"))
  })
    .copyFrom({ Blocks.RAW_IRON_BLOCK })
    .color(MapColor.TERRACOTTA_PINK)
    .toolAndTier(SetTool.PICKAXE, SetTier.STONE)
    .register()
  val ALUMINIUM_BLOCK = BlockGen<Block>(LibData.NAMES.ALUMINIUM).storageBlock({ ProjectItems.ALUMINIUM_INGOT.get() }, {
    DataIngredient.tag(LibTags.forgeItemTag("ingots/aluminium"))
  })
    .copyFrom({ Blocks.IRON_BLOCK })
    .color(MapColor.METAL)
    .toolAndTier(SetTool.PICKAXE, SetTier.STONE)
    .register()
  val SILICA_CRYSTAL_BLOCK = BlockGen<Block>(LibData.NAMES.SILICA).suffix("_crystal")
    .storageBlock({ ProjectItems.SILICA_CRYSTAL.get() }, {
      DataIngredient.tag(LibTags.forgeItemTag("ingots/silica"))
    })
    .copyFrom({ Blocks.AMETHYST_BLOCK })
    .color(MapColor.COLOR_LIGHT_BLUE)
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .properties { p ->
      p.sound(SoundType.GLASS)
        .noOcclusion()
    }
    .customStandardModel()
    .register()
  val SILICA_GEL = BlockGen<AbsorberBlock>(LibData.BLOCKS.SILICA_GEL).color(MapColor.COLOR_LIGHT_BLUE)
    .copyFrom({ Blocks.SLIME_BLOCK })
    .properties { p ->
      p.sound(SoundType.SLIME_BLOCK)
        .noOcclusion()
    }
    .customStandardModel()
    .blockFactory { p ->
      AbsorberBlock({ Blocks.WATER }, { ProjectItems.WATER_GEL_BUBBLE.get() }, p)
    }
    .register()
  val MAGMATIC_SILICA_GEL = BlockGen<AbsorberBlock>(LibData.BLOCKS.MAGMATIC_SILICA_GEL).color(MapColor.COLOR_ORANGE)
    .copyFrom({ Blocks.SLIME_BLOCK })
    .properties { p ->
      p.sound(SoundType.SLIME_BLOCK)
        .noOcclusion()
    }
    .customStandardModel()
    .blockFactory { p ->
      AbsorberBlock({ Blocks.LAVA }, { ProjectItems.LAVA_GEL_BUBBLE.get() }, p)
    }
    .register()
  val SUGAR_BLOCK = BlockGen<SandBlock>(LibData.NAMES.SUGAR).sandBlock(16250871)
    .storageBlock({ Items.SUGAR }, {
      DataIngredient.items(Items.SUGAR)
    })
    .color(MapColor.QUARTZ)
    .toolAndTier(SetTool.SHOVEL, SetTier.WOOD, false)
    .register()
  val CARBON_BLOCK = BlockGen<SandBlock>(LibData.NAMES.CARBON).sandBlock(16250871)
    .storageBlock({ ProjectItems.CARBON_GRIT.get() }, {
      DataIngredient.tag(LibTags.forgeItemTag("dusts/carbon"))
    })
    .color(MapColor.COLOR_BLACK)
    .toolAndTier(SetTool.SHOVEL, SetTier.WOOD, false)
    .register()
  val SULPHUR_BLOCK = BlockGen<SandBlock>(LibData.NAMES.SULPHUR).sandBlock(16250871)
    .storageBlock({ ProjectItems.SULPHUR.get() }, {
      DataIngredient.tag(LibTags.forgeItemTag("dusts/sulphur"))
    })
    .color(MapColor.COLOR_YELLOW)
    .toolAndTier(SetTool.SHOVEL, SetTier.WOOD, false)
    .register()
  val PHOSPHORUS_BLOCK = BlockGen<FlammableSandBlock>(LibData.NAMES.PHOSPHORUS).flammableSandBlock(16250871)
    .storageBlock({ ProjectItems.PHOSPHORUS_POWDER.get() }, {
      DataIngredient.tag(LibTags.forgeItemTag("dusts/phosphorus"))
    })
    .color(MapColor.TERRACOTTA_MAGENTA)
    .toolAndTier(SetTool.SHOVEL, SetTier.WOOD, false)
    .register()
  val MAGNESIUM_BLOCK = BlockGen<SandBlock>(LibData.NAMES.MAGNESIUM).sandBlock(16250871)
    .storageBlock({ ProjectItems.MAGNESIUM_DUST.get() }, {
      DataIngredient.tag(LibTags.forgeItemTag("dusts/magnesium"))
    })
    .color(MapColor.COLOR_LIGHT_GRAY)
    .toolAndTier(SetTool.SHOVEL, SetTier.WOOD, false)
    .register()
  val GRAPHITE_BLOCK = BlockGen<Block>(LibData.NAMES.GRAPHITE).storageBlock({ ProjectItems.RAW_GRAPHITE.get() }, {
    DataIngredient.tag(LibTags.forgeItemTag("ingots/graphite"))
  })
    .copyFrom({ Blocks.CALCITE })
    .color(MapColor.COLOR_GRAY)
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .register()

  // ASTEROID
  val ASTEROID_ROCK_FAMILY = BlockFamilyGen(LibData.BLOCKS.ASTEROID_ROCK).color(MapColor.COLOR_BROWN)
    .copyFrom { Blocks.STONE }
    .toolAndTier(SetTool.PICKAXE, SetTier.IRON)
    .denyList(BlockFamily.Type.PILLAR)
    .longBlockFamily()
  val DENSE_ASTEROID_ROCK = BlockGen<Block>(LibData.BLOCKS.DENSE_ASTEROID_ROCK).color(MapColor.DIRT)
    .copyFrom({ Blocks.DEEPSLATE })
    .toolAndTier(SetTool.PICKAXE, SetTier.DIAMOND)
    .register()

  // BRECCIAS
  val BRECCIA_BROWN = BlockGen<Block>("brown_${LibData.NAMES.BRECCIA}")
    .copyFrom({ Blocks.DEEPSLATE })
    .color(MapColor.COLOR_BROWN)
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .register()
  val BRECCIA_LIGHT_GRAY = BlockGen<Block>("light_gray_${LibData.NAMES.BRECCIA}")
    .copyFrom({ Blocks.DEEPSLATE })
    .color(MapColor.COLOR_LIGHT_GRAY)
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .register()
  val BRECCIA_DARK_GRAY = BlockGen<Block>("dark_gray_${LibData.NAMES.BRECCIA}")
    .copyFrom({ Blocks.DEEPSLATE })
    .color(MapColor.COLOR_GRAY)
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .register()

  // TERRAIN
  val SILT = BlockGen<SandBlock>(LibData.BLOCKS.SILT).sandBlock(14866349)
    .color(MapColor.SAND)
    .toolAndTier(SetTool.SHOVEL, SetTier.WOOD, false)
    .blockTags(listOf(BlockTags.SAND))
    .itemTags(listOf(ItemTags.SAND))
    .register()
  val RUST = BlockGen<SandBlock>(LibData.BLOCKS.RUST).sandBlock(14866349)
    .color(MapColor.COLOR_ORANGE)
    .toolAndTier(SetTool.SHOVEL, SetTier.WOOD, false)
    .register()
  val BASALT_PEBBLES = BlockGen<SandBlock>(LibData.BLOCKS.BASALT_PEBBLES).sandBlock(14406560)
    .copyFrom({ Blocks.GRAVEL })
    .color(MapColor.COLOR_GRAY)
    .toolAndTier(SetTool.SHOVEL, SetTier.WOOD, false)
    .register()
  val SANDSTONE_PEBBLES = BlockGen<SandBlock>(LibData.BLOCKS.SANDSTONE_PEBBLES).sandBlock(14406560)
    .copyFrom({ Blocks.GRAVEL })
    .color(MapColor.SAND)
    .toolAndTier(SetTool.SHOVEL, SetTier.WOOD, false)
    .register()
  val RED_SANDSTONE_PEBBLES = BlockGen<SandBlock>(LibData.BLOCKS.RED_SANDSTONE_PEBBLES).sandBlock(11098145)
    .copyFrom({ Blocks.GRAVEL })
    .color(MapColor.COLOR_ORANGE)
    .toolAndTier(SetTool.SHOVEL, SetTier.WOOD, false)
    .register()
  val ROSEATE_SANDSTONE_PEBBLES = BlockGen<SandBlock>(LibData.BLOCKS.ROSEATE_SANDSTONE_PEBBLES).sandBlock(15839662)
    .copyFrom({ Blocks.GRAVEL })
    .color(MapColor.COLOR_PINK)
    .toolAndTier(SetTool.SHOVEL, SetTier.WOOD, false)
    .register()
  val ROSEATE_GRAINS = BlockGen<SandBlock>(LibData.BLOCKS.ROSEATE_GRAINS).sandBlock(15839662)
    .copyFrom({ Blocks.SAND })
    .color(MapColor.COLOR_PINK)
    .toolAndTier(SetTool.SHOVEL, SetTier.WOOD, false)
    .blockTags(listOf(BlockTags.SAND))
    .itemTags(listOf(ItemTags.SAND))
    .register()
  val ROSEATE_FAMILY = BlockFamilyGen(LibData.NAMES.ROSEATE).color(MapColor.COLOR_PINK)
    .copyFrom { Blocks.SANDSTONE }
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .sandstoneFamily(ROSEATE_GRAINS)
  val HIMALAYAN_SALT_CLUSTER = BlockGen<AmethystClusterBlock>("${LibData.NAMES.HIMALAYAN_SALT}_cluster").copyFrom({ Blocks.AMETHYST_CLUSTER })
    .color(MapColor.COLOR_MAGENTA)
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .blockFactory { p -> AmethystClusterBlock(7, 3, p) }
    .properties { p ->
      p.lightLevel { 5 }
        .sound(SoundType.GLASS)
        .noOcclusion()
        .noCollission()
    }
    .transform { t ->
      t
        .loot(BlockLootPresets.dropSelfSilkLoot { ProjectItems.HIMALAYAN_SALT.get() })
        .item()
        .model(ItemModelPresets.simpleLayerItem("${LibData.NAMES.HIMALAYAN_SALT}_cluster"))
        .build()
        .blockstate(BlockstatePresets.clusterCrossBlock())
    }
    .register()
  val RAW_HIMALAYAN_SALT = BlockGen<Block>(LibData.NAMES.RAW_HIMALAYAN_SALT).copyFrom({ Blocks.QUARTZ_BLOCK })
    .color(MapColor.COLOR_MAGENTA)
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .smallStorageBlock({ ProjectItems.HIMALAYAN_SALT.get() }, {
      DataIngredient.tag(LibTags.forgeItemTag("ingots/himalayan_salt"))
    })
    .register()
  val HIMALAYAN_SALT_FAMILY = BlockFamilyGen(LibData.NAMES.HIMALAYAN_SALT).color(MapColor.COLOR_MAGENTA)
    .copyFrom { Blocks.QUARTZ_BLOCK }
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .longBlockFamily(RAW_HIMALAYAN_SALT)
  val BROWN_SLATE = BlockGen<RotatedPillarBlock>(LibData.NAMES.BROWN_SLATE).copyFrom({ Blocks.DEEPSLATE })
    .color(MapColor.COLOR_BROWN)
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .loot(BlockLootPresets.dropSelfSilkLoot { COBBLED_BROWN_SLATE.get() })
    .rotatedPillarBlock()
    .register()
  val BROWN_SLATE_FAMILY = BlockFamilyGen(LibData.NAMES.BROWN_SLATE).color(MapColor.COLOR_BROWN)
    .copyFrom { Blocks.COBBLED_DEEPSLATE }
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .denyList(BlockFamily.Type.POLISHED, BlockFamily.Type.BRICKS)
    .mineralFamily(BROWN_SLATE, true)
  val COBBLED_BROWN_SLATE = BlockGen<Block>("cobbled_${LibData.NAMES.BROWN_SLATE}").copyFrom({ Blocks.COBBLED_DEEPSLATE })
    .color(MapColor.COLOR_BROWN)
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .blockTags(listOf(LibTags.forgeBlockTag("cobblestone")))
    .blockTags(listOf(LibTags.vanillaBlockTag("stone_crafting_materials")))
    .blockTags(listOf(LibTags.vanillaBlockTag("stone_tool_materials")))
    .itemTags(listOf(LibTags.forgeItemTag("cobblestone")))
    .itemTags(listOf(LibTags.vanillaItemTag("stone_crafting_materials")))
    .itemTags(listOf(LibTags.vanillaItemTag("stone_tool_materials")))
    .register()
  val COBBLED_BROWN_SLATE_FAMILY = BlockFamilyGen("cobbled_${LibData.NAMES.BROWN_SLATE}").color(MapColor.COLOR_BROWN)
    .copyFrom { Blocks.COBBLED_DEEPSLATE }
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .denyList(BlockFamily.Type.POLISHED, BlockFamily.Type.BRICKS)
    .mineralFamily(COBBLED_BROWN_SLATE)
  val FULL_BROWN_SLATE_FAMILY = BlockFamilyGen(LibData.NAMES.BROWN_SLATE).color(MapColor.COLOR_BROWN)
    .copyFrom { Blocks.DEEPSLATE }
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .denyList(BlockFamily.Type.MAIN, BlockFamily.Type.CHISELED, BlockFamily.Type.PILLAR)
    .longBlockFamily(COBBLED_BROWN_SLATE, true)
  val RED_HEMATITE_IRON_ORE = BlockGen<Block>(LibData.NAMES.IRON).prefix("red_hematite_")
    .oreBlock({ Items.RAW_IRON }, LibData.NAMES.RED_HEMATITE)
    .copyFrom({ Blocks.IRON_ORE })
    .color(MapColor.COLOR_ORANGE)
    .toolAndTier(SetTool.PICKAXE, SetTier.STONE)
    .register()
  val RED_HEMATITE = BlockGen<Block>(LibData.NAMES.RED_HEMATITE).copyFrom({ Blocks.STONE })
    .color(MapColor.COLOR_ORANGE)
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .loot(BlockLootPresets.dropSelfSilkLoot { COBBLED_RED_HEMATITE.get() })
    .register()
  val RED_HEMATITE_FAMILY = BlockFamilyGen(LibData.NAMES.RED_HEMATITE).color(MapColor.COLOR_ORANGE)
    .copyFrom { Blocks.COBBLESTONE }
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .denyList(BlockFamily.Type.POLISHED, BlockFamily.Type.BRICKS)
    .mineralFamily(RED_HEMATITE)
  val COBBLED_RED_HEMATITE = BlockGen<Block>("cobbled_${LibData.NAMES.RED_HEMATITE}").copyFrom({ Blocks.COBBLESTONE })
    .color(MapColor.COLOR_ORANGE)
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .blockTags(listOf(LibTags.forgeBlockTag("cobblestone")))
    .blockTags(listOf(LibTags.vanillaBlockTag("stone_crafting_materials")))
    .blockTags(listOf(LibTags.vanillaBlockTag("stone_tool_materials")))
    .itemTags(listOf(LibTags.forgeItemTag("cobblestone")))
    .itemTags(listOf(LibTags.vanillaItemTag("stone_crafting_materials")))
    .itemTags(listOf(LibTags.vanillaItemTag("stone_tool_materials")))
    .register()
  val COBBLED_RED_HEMATITE_FAMILY = BlockFamilyGen("cobbled_${LibData.NAMES.RED_HEMATITE}").color(MapColor.COLOR_ORANGE)
    .copyFrom { Blocks.COBBLESTONE }
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .denyList(BlockFamily.Type.POLISHED, BlockFamily.Type.BRICKS)
    .mineralFamily(COBBLED_RED_HEMATITE)
  val FULL_RED_HEMATITE_FAMILY = BlockFamilyGen(LibData.NAMES.RED_HEMATITE).color(MapColor.COLOR_ORANGE)
    .copyFrom { Blocks.STONE }
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .denyList(BlockFamily.Type.MAIN, BlockFamily.Type.CHISELED, BlockFamily.Type.PILLAR)
    .longBlockFamily(COBBLED_RED_HEMATITE)

  // @ Minerals
  val PHOSPHORITE_FAMILY = BlockFamilyGen(LibData.NAMES.PHOSPHORITE).color(MapColor.NETHER)
    .copyFrom { Blocks.GRANITE }
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .denyList(BlockFamily.Type.BRICKS)
    .mineralFamily()
  val PYRITE_FAMILY = BlockFamilyGen(LibData.NAMES.PYRITE).color(MapColor.GOLD)
    .copyFrom { Blocks.DIORITE }
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .mineralFamily()

  //  val MOON_SILT = BlockGen<SandBlock>(LibData.BLOCKS.MOON_SILT).sandBlock(16250871)
//    .color(MapColor.COLOR_GRAY)
//    .toolAndTier(SetTool.SHOVEL, SetTier.WOOD, false)
//    .blockTags(listOf(BlockTags.SAND))
//    .itemTags(listOf(ItemTags.SAND))
//    .register()
  val MOON_REGOLITH = BlockGen<SandBlock>(LibData.BLOCKS.MOON_REGOLITH).sandBlock(16250871)
    .color(MapColor.COLOR_GRAY)
    .toolAndTier(SetTool.SHOVEL, SetTier.WOOD, false)
    .blockTags(listOf(BlockTags.SAND))
    .itemTags(listOf(ItemTags.SAND))
    .register()

  // @ Moon Rocks
  val ANORTHOSITE_ALUMINIUM_ORE = BlockGen<Block>(LibData.NAMES.ALUMINIUM).prefix("${LibData.NAMES.ANORTHOSITE}_")
    .oreBlock({ ProjectItems.RAW_ALUMINIUM.get() }, LibData.NAMES.ANORTHOSITE)
    .copyFrom({ Blocks.IRON_ORE })
    .color(MapColor.COLOR_GRAY)
    .toolAndTier(SetTool.PICKAXE, SetTier.STONE)
    .blockTags(listOf(ProjectTags.BLOCK.ALUMINIUM_ORES))
    .register()
  val ANORTHOSITE_TUNGSTEN_ORE = BlockGen<Block>(LibData.NAMES.TUNGSTEN).prefix("${LibData.NAMES.ANORTHOSITE}_")
    .oreBlock({ ProjectItems.RAW_TUNGSTEN.get() }, LibData.NAMES.ANORTHOSITE)
    .copyFrom({ Blocks.IRON_ORE })
    .color(MapColor.COLOR_GRAY)
    .toolAndTier(SetTool.PICKAXE, SetTier.IRON)
    .blockTags(listOf(ProjectTags.BLOCK.TUNGSTEN_ORES))
    .register()
  val ANORTHOSITE = BlockGen<Block>(LibData.NAMES.ANORTHOSITE).color(MapColor.COLOR_LIGHT_GRAY)
    .blockTags(listOf(BlockTags.STONE_ORE_REPLACEABLES, ProjectTags.BLOCK.ANORTHOSITE_REPLACEABLES))
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .loot(BlockLootPresets.dropSelfSilkLoot { COBBLED_ANORTHOSITE.get() })
    .register()
  val ANORTHOSITE_FAMILY = BlockFamilyGen(LibData.NAMES.ANORTHOSITE).color(MapColor.COLOR_LIGHT_GRAY)
    .copyFrom { Blocks.DIORITE }
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .denyList(BlockFamily.Type.POLISHED, BlockFamily.Type.BRICKS)
    .mineralFamily(ANORTHOSITE)
  val COBBLED_ANORTHOSITE = BlockGen<Block>("cobbled_${LibData.NAMES.ANORTHOSITE}").copyFrom({ Blocks.COBBLESTONE })
    .color(MapColor.COLOR_ORANGE)
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .blockTags(listOf(LibTags.forgeBlockTag("cobblestone")))
    .blockTags(listOf(LibTags.vanillaBlockTag("stone_crafting_materials")))
    .blockTags(listOf(LibTags.vanillaBlockTag("stone_tool_materials")))
    .itemTags(listOf(LibTags.forgeItemTag("cobblestone")))
    .itemTags(listOf(LibTags.vanillaItemTag("stone_crafting_materials")))
    .itemTags(listOf(LibTags.vanillaItemTag("stone_tool_materials")))
    .register()
  val COBBLED_ANORTHOSITE_FAMILY = BlockFamilyGen("cobbled_${LibData.NAMES.ANORTHOSITE}").color(MapColor.COLOR_ORANGE)
    .copyFrom { Blocks.COBBLESTONE }
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .denyList(BlockFamily.Type.POLISHED, BlockFamily.Type.BRICKS)
    .mineralFamily(COBBLED_ANORTHOSITE)
  val FULL_ANORTHOSITE_FAMILY = BlockFamilyGen(LibData.NAMES.ANORTHOSITE).color(MapColor.COLOR_ORANGE)
    .copyFrom { Blocks.STONE }
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .denyList(BlockFamily.Type.MAIN, BlockFamily.Type.CHISELED, BlockFamily.Type.PILLAR)
    .longBlockFamily(COBBLED_ANORTHOSITE)

  // Moonslate
  val MOONSLATE_ALUMINIUM_ORE = BlockGen<Block>(LibData.NAMES.ALUMINIUM).prefix("${LibData.NAMES.MOONSLATE}_")
    .oreBlock({ ProjectItems.RAW_ALUMINIUM.get() }, LibData.NAMES.MOONSLATE)
    .copyFrom({ Blocks.DEEPSLATE_IRON_ORE })
    .color(MapColor.COLOR_GRAY)
    .toolAndTier(SetTool.PICKAXE, SetTier.STONE)
    .blockTags(listOf(ProjectTags.BLOCK.ALUMINIUM_ORES))
    .register()
  val MOONSLATE_TUNGSTEN_ORE = BlockGen<Block>(LibData.NAMES.TUNGSTEN).prefix("${LibData.NAMES.MOONSLATE}_")
    .oreBlock({ ProjectItems.RAW_TUNGSTEN.get() }, LibData.NAMES.MOONSLATE)
    .copyFrom({ Blocks.DEEPSLATE_IRON_ORE })
    .color(MapColor.COLOR_GRAY)
    .toolAndTier(SetTool.PICKAXE, SetTier.IRON)
    .blockTags(listOf(ProjectTags.BLOCK.TUNGSTEN_ORES))
    .register()
  val MOONSLATE = BlockGen<RotatedPillarBlock>(LibData.NAMES.MOONSLATE).color(MapColor.COLOR_GRAY)
    .copyFrom({ Blocks.DEEPSLATE })
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .blockTags(listOf(BlockTags.DEEPSLATE_ORE_REPLACEABLES, ProjectTags.BLOCK.MOONSLATE_REPLACEABLES))
    .rotatedPillarBlock()
    .loot(BlockLootPresets.dropSelfSilkLoot { COBBLED_MOONSLATE.get() })
    .register()
  val MOONSLATE_FAMILY = BlockFamilyGen(LibData.NAMES.MOONSLATE).color(MapColor.COLOR_LIGHT_GRAY)
    .copyFrom { Blocks.DEEPSLATE }
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .denyList(BlockFamily.Type.POLISHED, BlockFamily.Type.BRICKS)
    .mineralFamily(MOONSLATE, true)
  val COBBLED_MOONSLATE = BlockGen<Block>("cobbled_${LibData.NAMES.MOONSLATE}")
    .copyFrom({ Blocks.COBBLED_DEEPSLATE })
    .color(MapColor.COLOR_ORANGE)
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .blockTags(listOf(LibTags.forgeBlockTag("cobblestone")))
    .blockTags(listOf(LibTags.vanillaBlockTag("stone_crafting_materials")))
    .blockTags(listOf(LibTags.vanillaBlockTag("stone_tool_materials")))
    .itemTags(listOf(LibTags.forgeItemTag("cobblestone")))
    .itemTags(listOf(LibTags.vanillaItemTag("stone_crafting_materials")))
    .itemTags(listOf(LibTags.vanillaItemTag("stone_tool_materials")))
    .register()
  val COBBLED_MOONSLATE_FAMILY = BlockFamilyGen("cobbled_${LibData.NAMES.MOONSLATE}").color(MapColor.COLOR_ORANGE)
    .copyFrom { Blocks.COBBLED_DEEPSLATE }
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .denyList(BlockFamily.Type.POLISHED, BlockFamily.Type.BRICKS)
    .mineralFamily(COBBLED_MOONSLATE)
  val FULL_MOONSLATE_FAMILY = BlockFamilyGen(LibData.NAMES.MOONSLATE).color(MapColor.COLOR_ORANGE)
    .copyFrom { Blocks.DEEPSLATE }
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .denyList(BlockFamily.Type.MAIN, BlockFamily.Type.CHISELED, BlockFamily.Type.PILLAR)
    .longBlockFamily(COBBLED_MOONSLATE)

  // OLIVINE
  val OLIVINE_FAMILY = BlockFamilyGen(LibData.NAMES.OLIVINE).color(MapColor.COLOR_GREEN)
    .copyFrom { Blocks.GRANITE }
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .mineralFamily()

  // PERIDOT
  val PERIDOT_CRYSTAL = BlockGen<Block>("${LibData.NAMES.PERIDOT}_crystal")
    .copyFrom({ Blocks.EMERALD_BLOCK })
    .color(MapColor.COLOR_GREEN)
    .toolAndTier(SetTool.PICKAXE, SetTier.STONE)
    .register()
  val PERIDOT_CLUSTER = BlockGen<AmethystClusterBlock>("${LibData.NAMES.PERIDOT}_cluster").copyFrom({ Blocks.AMETHYST_CLUSTER })
    .color(MapColor.COLOR_GREEN)
    .toolAndTier(SetTool.PICKAXE, SetTier.STONE)
    .blockFactory { p -> AmethystClusterBlock(7, 3, p) }
    .properties { p ->
      p.lightLevel { 5 }
        .sound(SoundType.GLASS)
        .noOcclusion()
        .noCollission()
    }
    .transform { t ->
      t
        .loot(BlockLootPresets.dropSelfSilkLoot { ProjectItems.PERIDOT.get() })
        .item()
        .model(ItemModelPresets.simpleLayerItem("${LibData.NAMES.PERIDOT}_cluster"))
        .build()
        .blockstate(BlockstatePresets.clusterCrossBlock())
    }
    .register()
  val PERIDOT_BLOCK = BlockGen<Block>(LibData.NAMES.PERIDOT).storageBlock({ ProjectItems.PERIDOT.get() }, {
    DataIngredient.tag(LibTags.forgeItemTag("gems/peridot"))
  })
    .copyFrom({ Blocks.EMERALD_BLOCK })
    .color(MapColor.COLOR_GREEN)
    .toolAndTier(SetTool.PICKAXE, SetTier.STONE)
    .register()

  // NORITE
  val NORITE_FAMILY = BlockFamilyGen(LibData.NAMES.NORITE).color(MapColor.LAPIS)
    .copyFrom { Blocks.GRANITE }
    .toolAndTier(SetTool.PICKAXE, SetTier.WOOD)
    .mineralFamily()

  // PLANTS
  val GUAYULE_SHRUB_TALL: BlockEntry<GuayuleShrubTopBlock> = ProjectContent.REGISTRATE.block<GuayuleShrubTopBlock>("double_" + LibData.PLANTS.GUAYULE_SHRUB) { props ->
    GuayuleShrubTopBlock(props)
  }
    .initialProperties { Blocks.POPPY }
    .properties { p ->
      p.mapColor(MapColor.TERRACOTTA_LIGHT_GREEN)
        .sound(SoundType.WET_GRASS)
        .strength(0.0f)
        .randomTicks()
        .noCollission()
        .noOcclusion()
    }
    .loot(BlockLootPresets.dropDoubleFlowerLootUpper({ GUAYULE_SHRUB.get() }, 2))
    .blockstate { c, p ->
      p.getVariantBuilder(c.get())
        .partialState()
        .with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)
        .setModels(*ConfiguredModel.builder()
          .modelFile(p.models()
            .withExistingParent(c.name + "_top", p.mcLoc("block/cross"))
            .texture("cross", p.modLoc("block/" + LibData.PLANTS.GUAYULE_SHRUB + "_top"))
            .texture("particle", p.modLoc("block/" + LibData.PLANTS.GUAYULE_SHRUB + "_top"))
            .renderType("cutout_mipped"))
          .build())
        .partialState()
        .with(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)
        .setModels(*ConfiguredModel.builder()
          .modelFile(p.models()
            .withExistingParent(c.name + "_bottom", p.mcLoc("block/cross"))
            .texture("cross", p.modLoc("block/" + LibData.PLANTS.GUAYULE_SHRUB + "_bottom"))
            .texture("particle", p.modLoc("block/" + LibData.PLANTS.GUAYULE_SHRUB + "_bottom"))
            .renderType("cutout_mipped"))
          .build())
    }
    .register()
  val GUAYULE_SHRUB: BlockEntry<GuayuleShrubBlock> = ProjectContent.REGISTRATE.block<GuayuleShrubBlock>(LibData.PLANTS.GUAYULE_SHRUB) { props ->
    GuayuleShrubBlock(GUAYULE_SHRUB_TALL.get(), props)
  }
    .initialProperties { Blocks.POPPY }
    .properties { p ->
      p.mapColor(MapColor.TERRACOTTA_LIGHT_GREEN)
        .sound(SoundType.WET_GRASS)
        .strength(0.0f)
        .randomTicks()
        .noCollission()
        .noOcclusion()
    }
    .blockstate { c, p ->
      p.getVariantBuilder(c.get())
        .partialState()
        .setModels(*ConfiguredModel.builder()
          .modelFile(p.models()
            .withExistingParent(c.name, p.mcLoc("block/cross"))
            .texture("cross", p.modLoc("block/" + LibData.PLANTS.GUAYULE_SHRUB))
            .texture("particle", p.modLoc("block/" + LibData.PLANTS.GUAYULE_SHRUB))
            .renderType("cutout_mipped"))
          .build())
    }
    .loot(BlockLootPresets.dropItselfLoot())
    .item()
    .model { c, p ->
      p.withExistingParent(c.name, p.mcLoc("item/generated"))
        .texture("layer0", p.modLoc("item/" + LibData.PLANTS.GUAYULE_SHRUB))
    }
    .build()
    .register()
  val BUDDING_COTTON_CROP = ProjectContent.REGISTRATE.block<BuddingCottonBlock>(LibData.PLANTS.BUDDING_COTTON) { props ->
    BuddingCottonBlock(props)
  }
    .initialProperties { Blocks.WHEAT }
    .properties { p ->
      p.mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
        .sound(SoundType.CROP)
        .strength(0.0f)
        .randomTicks()
        .noCollission()
        .noOcclusion()
    }
    .blockstate { c, p ->
      p.getVariantBuilder(c.get())
        .forAllStates { state ->
          ConfiguredModel.builder()
            .modelFile(p.models()
              .withExistingParent(c.name + "_stage" + state.getValue(BuddingBushBlock.AGE), p.mcLoc("block/cross"))
              .texture("cross", p.modLoc("block/cotton/" + LibData.PLANTS.BUDDING_COTTON + "_stage" + state.getValue(BuddingBushBlock.AGE)))
              .texture("particle", p.modLoc("block/cotton/" + LibData.PLANTS.BUDDING_COTTON + "_stage" + state.getValue(BuddingBushBlock.AGE)))
              .renderType("cutout"))
            .build()
        }
    }
    .register()
  val COTTON_CROP = ProjectContent.REGISTRATE.block<CottonCropBlock>(LibData.PLANTS.COTTON) { props ->
    CottonCropBlock(props)
  }
    .initialProperties { Blocks.WHEAT }
    .properties { p ->
      p.mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
        .sound(SoundType.CROP)
        .strength(0.0f)
        .randomTicks()
        .noCollission()
        .noOcclusion()
    }
    .blockstate { c, p ->
      p.getVariantBuilder(c.get())
        .forAllStates { state ->
          ConfiguredModel.builder()
            .modelFile(p.models()
              .withExistingParent(c.name + "_stage" + state.getValue(CottonCropBlock.CROP_AGE), p.mcLoc("block/cross"))
              .texture("cross", p.modLoc("block/cotton/" + LibData.PLANTS.COTTON + "_stage" + state.getValue(CottonCropBlock.CROP_AGE)))
              .texture("particle", p.modLoc("block/cotton/" + LibData.PLANTS.COTTON + "_stage" + state.getValue(CottonCropBlock.CROP_AGE)))
              .renderType("cutout"))
            .build()
        }
    }
    .register() //  val SIMPLE_GRASS = BlockGen<GenericGrassBlock>("simple_grass")

  //    .blockFactory { p -> GenericGrassBlock(p) { b, _, _ -> b.`is`(BlockTags.DIRT) } }
  //    .copyFrom({Blocks.GRASS})
  //    .properties { p ->
  //      p.mapColor(MapColor.TERRACOTTA_GREEN).sound(SoundType.GRASS).strength(0.0f).randomTicks().noCollission()
  //        .noOcclusion()
  //    }
  //    .transform(BlockPresets.simpleCrossBlock("simple_grass"))
  //    .transform(BlockPresets.simpleLayerItem("simple_grass"))
  //    .register()
  val SIMPLE_FLOWER = BlockGen<GenericFlowerBlock>("simple_flower").blockFactory { p ->
    GenericFlowerBlock({ MobEffects.SATURATION }, 5, p)
  }
    .copyFrom({ Blocks.POPPY })
    .properties { p ->
      p.mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)
        .sound(SoundType.GRASS)
        .strength(0.0f)
        .randomTicks()
        .noCollission()
        .noOcclusion()
    }
    .transform { t ->
      t
        .blockstate(BlockstatePresets.simpleCrossBlock("simple_flower"))
        .item()
        .model(ItemModelPresets.simpleLayerItem("simple_flower"))
        .build()
    }
    .register()
  val POTTED_SIMPLE_FLOWER = BlockGen<FlowerPotBlock>("potted_simple_flower").blockFactory { p ->
    FlowerPotBlock({ Blocks.FLOWER_POT as FlowerPotBlock }, { SIMPLE_FLOWER.get() }, p)
  }
    .copyFrom({ Blocks.POTTED_POPPY })
    .noItem()
    .properties { p ->
      p.mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)
        .noOcclusion()
    }
    .loot(BlockLootPresets.pottedPlantLoot { SIMPLE_FLOWER.get() })
    .transform { t ->
      t.blockstate(BlockstatePresets.pottedPlantBlock("simple_flower"))
    }
    .register() //  val TALL_SIMPLE_GRASS_TOP: BlockEntry<GenericTallGrassTopBlock> =

  //    BlockGen<GenericTallGrassTopBlock>("double_" + "tall_simple_grass")
  //      .blockFactory { p -> GenericTallGrassTopBlock({ TALL_SIMPLE_GRASS.get() }, p) }
  //      .copyFrom({Blocks.POPPY})
  //      .color(MapColor.TERRACOTTA_LIGHT_GREEN)
  //      .properties { p -> p.strength(0.0f).randomTicks().noCollission().noOcclusion() }
  //      .transform(BlockPresets.tallPlantTopBlock("tall_simple_grass"))
  //      .transform(BlockPresets.dropOtherLoot({ SIMPLE_GRASS.get() }))
  //      .noItem()
  //      .register()
  //
  //
  //  val TALL_SIMPLE_GRASS: BlockEntry<GenericTallGrassBlock> = BlockGen<GenericTallGrassBlock>("tall_simple_grass")
  //    .blockFactory { p -> GenericTallGrassBlock({ TALL_SIMPLE_GRASS_TOP.get() }, p) }
  //    .copyFrom({Blocks.POPPY})
  //    .color(MapColor.TERRACOTTA_LIGHT_GREEN)
  //    .properties { p -> p.strength(0.0f).randomTicks().noCollission().noOcclusion() }
  //    .transform(BlockPresets.tallPlantBottomBlock("tall_simple_grass", false))
  //    .transform(BlockPresets.dropOtherLoot({ SIMPLE_FLOWER.get() }))
  //    .register()
  val TALL_SPARSE_DRY_GRASS: BlockEntry<GenericDoublePlantBlock> = BlockGen<GenericDoublePlantBlock>("tall_" + "sparse_dry_grass").blockFactory { p ->
    GenericDoublePlantBlock(p) { b, _, _ ->
      b.`is`(BlockTags.SAND) || b.`is`(BlockTags.DIRT)
    }
  }
    .copyFrom({ Blocks.TALL_GRASS })
    .color(MapColor.TERRACOTTA_YELLOW)
    .properties { p ->
      p.strength(0.0f)
        .randomTicks()
        .noCollission()
        .noOcclusion()
    }
    .loot(BlockLootPresets.dropDoubleCropLoot({ Items.BEETROOT_SEEDS }, { Items.BEETROOT_SEEDS }))
    .transform { t ->
      t.blockstate(BlockstatePresets.tallPlantTopBlock("sparse_dry_grass"))
        .item()
        .model(ItemModelPresets.simpleLayerItem("sparse_dry_grass_top"))
        .build()
    }
    .register()
  val SPARSE_DRY_GRASS: BlockEntry<GenericTallGrassBlock> = BlockGen<GenericTallGrassBlock>("sparse_dry_grass").blockFactory { p ->
    GenericTallGrassBlock({ TALL_SPARSE_DRY_GRASS.get() }, p) { b, _, _ ->
      b.`is`(BlockTags.SAND) || b.`is`(BlockTags.DIRT)
    }
  }
    .copyFrom({ Blocks.TALL_GRASS })
    .color(MapColor.TERRACOTTA_YELLOW)
    .properties { p ->
      p.strength(0.0f)
        .randomTicks()
        .noCollission()
        .noOcclusion()
    }
    .transform { t ->
      t.blockstate(BlockstatePresets.tallPlantBottomBlock("sparse_dry_grass"))
        .item()
        .model(ItemModelPresets.tallPlantBottomItem("sparse_dry_grass", false))
        .build()
    }
    .loot(BlockLootPresets.dropCropLoot({ Items.BEETROOT_SEEDS }, { Items.BEETROOT_SEEDS }))
    .register()
  val DEAD_GRASS = BlockGen<GenericGrassBlock>("dead_grass").blockFactory { p ->
    GenericGrassBlock(p, { b, _, _ ->
      b.`is`(BlockTags.SAND) || b.`is`(BlockTags.DIRT) || b.`is`(BlockTags.TERRACOTTA)
    })
  }
    .copyFrom({ Blocks.GRASS })
    .properties { p ->
      p.mapColor(MapColor.TERRACOTTA_YELLOW)
        .sound(SoundType.GRASS)
        .strength(0.0f)
        .randomTicks()
        .noCollission()
        .noOcclusion()
    }
    .loot(BlockLootPresets.dropSelfSilkShearsLoot())
    .transform { t ->
      t.blockstate(BlockstatePresets.simpleCrossBlock("dead_grass"))
        .item()
        .model(ItemModelPresets.simpleLayerItem("dead_grass"))
        .build()
    }
    .register()
  val DRY_SHRUB = BlockGen<GenericGrassBlock>("dry_shrub").blockFactory { p ->
    GenericGrassBlock(p, { b, _, _ ->
      b.`is`(BlockTags.SAND) || b.`is`(BlockTags.DIRT) || b.`is`(BlockTags.TERRACOTTA)
    }, true)
  }
    .copyFrom({ Blocks.GRASS })
    .properties { p ->
      p.mapColor(MapColor.TERRACOTTA_YELLOW)
        .sound(SoundType.MANGROVE_ROOTS)
        .strength(0.0f)
        .randomTicks()
        .noCollission()
        .noOcclusion()
    }
    .loot(BlockLootPresets.dropSelfSilkShearsOtherLoot({ Items.STICK }))
    .transform { t ->
      t.blockstate(BlockstatePresets.simpleCrossBlock("dry_shrub"))
        .item()
        .model(ItemModelPresets.simpleLayerItem("dry_shrub"))
        .build()
    }
    .register()
  val DRY_PATCHES = BlockGen<CarpetBlock>("dry_patches").blockFactory { p ->
    CarpetBlock(p)
  }
    .copyFrom({ Blocks.MOSS_CARPET })
    .properties { p ->
      p.mapColor(MapColor.TERRACOTTA_YELLOW)
        .sound(SoundType.MANGROVE_ROOTS)
        .strength(0.0f)
        .randomTicks()
        .noCollission()
        .noOcclusion()
    }
    .transform { t ->
      t.blockstate(BlockstatePresets.carpetBlock("dry_patches"))
    }
    .register()
  val OCOTILLO: BlockEntry<GenericDoubleFlowerBlock> = BlockGen<GenericDoubleFlowerBlock>("ocotillo").blockFactory { p ->
    GenericDoubleFlowerBlock(p) { b, _, _ ->
      b.`is`(BlockTags.SAND) || b.`is`(BlockTags.DIRT)
    }
  }
    .copyFrom({ Blocks.TALL_GRASS })
    .color(MapColor.TERRACOTTA_ORANGE)
    .properties { p ->
      p.strength(0.0f)
        .randomTicks()
        .noCollission()
        .noOcclusion()
    }
    .transform { t ->
      t.blockstate(BlockstatePresets.tallPlantTopBlock("ocotillo"))
        .item()
        .model(ItemModelPresets.simpleLayerItem("ocotillo_top"))
        .build()
    }
    .loot(BlockLootPresets.dropDoubleFlowerLootLower())
    .register()
  val AGAVE_TOP: BlockEntry<GenericTallGrassTopBlock> = BlockGen<GenericTallGrassTopBlock>("double_" + "agave").blockFactory { p ->
    GenericTallGrassTopBlock({ AGAVE.get() }, p) { b, _, _ ->
      b.`is`(BlockTags.SAND)
    }
  }
    .copyFrom({ Blocks.POPPY })
    .color(MapColor.TERRACOTTA_LIGHT_GREEN)
    .properties { p ->
      p.strength(0.0f)
        .randomTicks()
        .noCollission()
        .noOcclusion()
    }
    .loot(BlockLootPresets.dropOtherLoot { AGAVE.get() })
    .transform { t ->
      t.blockstate { c, p ->
        p.getVariantBuilder(c.get())
          .partialState()
          .with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)
          .setModels(*ConfiguredModel.builder()
            .modelFile(p.models()
              .withExistingParent(c.name + "_top", p.modLoc("block/cross_large"))
              .texture("cross", p.modLoc("block/" + "agave" + "_top"))
              .texture("particle", p.modLoc("block/" + "agave" + "_top"))
              .renderType("cutout_mipped"))
            .build())
          .partialState()
          .with(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)
          .setModels(*ConfiguredModel.builder()
            .modelFile(p.models()
              .withExistingParent(c.name + "_bottom", p.mcLoc("block/template_wall_post"))
              .texture("wall", p.modLoc("block/" + "agave" + "_bottom"))
              .texture("particle", p.modLoc("block/" + "agave" + "_bottom"))
              .renderType("cutout_mipped"))
            .build())
      }
    }
    .noItem()
    .register()
  val AGAVE: BlockEntry<GenericTallGrassBlock> = BlockGen<GenericTallGrassBlock>("agave").blockFactory { p ->
    GenericTallGrassBlock({ AGAVE_TOP.get() }, p) { b, _, _ ->
      b.`is`(BlockTags.SAND)
    }
  }
    .copyFrom({ Blocks.POPPY })
    .color(MapColor.TERRACOTTA_LIGHT_GREEN)
    .properties { p ->
      p.strength(0.0f)
        .randomTicks()
        .noCollission()
        .noOcclusion()
    }
    .loot(BlockLootPresets.dropItselfLoot())
    .transform { t ->
      t.blockstate(BlockstatePresets.tallPlantBottomBlock("agave"))
        .item()
        .model(ItemModelPresets.tallPlantBottomItem("agave", true))
        .build()
    }
    .register()

  // Joshua stalk tree
  val JOSHUA_FAMILY = BlockFamilyGen(LibData.NAMES.JOSHUA).color(MapColor.COLOR_BROWN, MapColor.TERRACOTTA_LIGHT_BLUE)
    .toolAndTier(SetTool.AXE, SetTier.WOOD)
    .stalkWoodFamily(ProjectWoodTypes.JOSHUA, JoshuaTreeGrower()) { b, _, _ ->
      b.`is`(BlockTags.SAND)
    } //  val TALL_SIMPLE_GRASS_TOP: BlockEntry<GenericTallGrassTopBlock> = DataboxContent.REGISTRATE.block<GenericTallGrassTopBlock>("double_" + "tall_simple_grass")

  //  { props -> GenericTallGrassTopBlock({ TALL_SIMPLE_GRASS.get() }, props) }
  //    .initialProperties { Blocks.POPPY }
  //    .properties { p -> p.color(MapColor.TERRACOTTA_LIGHT_GREEN).sound(SoundType.GRASS).strength(0.0f).randomTicks().noCollission().noOcclusion() }
  //    .transform(BlockPresets.tallPlantTopBlock("tall_simple_grass"))
  //    .register()
  //
  //
  //  val TALL_SIMPLE_GRASS: BlockEntry<GenericTallGrassBlock> = DataboxContent.REGISTRATE.block<GenericTallGrassBlock>("tall_simple_grass")
  //  { props -> GenericTallGrassBlock({ TALL_SIMPLE_GRASS_TOP.get() },  props) }
  //    .initialProperties { Blocks.POPPY }
  //    .properties { p -> p.color(MapColor.TERRACOTTA_LIGHT_GREEN).sound(SoundType.GRASS).strength(0.0f).randomTicks().noCollission().noOcclusion() }
  //    .transform(BlockPresets.tallPlantBottomBlock("tall_simple_grass", false))
  //    .register()
  // @ STICKERS
  val LAUGHING_STICKER = BlockGen<StickerBlock>(LibData.STICKERS.LAUGHING).stickerBlock()
    .register()
  val COOL_STICKER = BlockGen<StickerBlock>(LibData.STICKERS.COOL).stickerBlock()
    .register()
  val CRYING_STICKER = BlockGen<StickerBlock>(LibData.STICKERS.CRYING).stickerBlock()
    .register()
  val LOVE_STICKER = BlockGen<StickerBlock>(LibData.STICKERS.LOVE).stickerBlock()
    .register()
  val SWEAT_SMILE_STICKER = BlockGen<StickerBlock>(LibData.STICKERS.SWEAT_SMILE).stickerBlock()
    .register()
  val SCARY_FACE_STICKER = BlockGen<StickerBlock>(LibData.STICKERS.SCARY_FACE).stickerBlock()
    .register()
  val EVIL_FACE_STICKER = BlockGen<StickerBlock>(LibData.STICKERS.EVIL_FACE).stickerBlock()
    .register()
  val CREEPER_FACE_STICKER = BlockGen<StickerBlock>(LibData.STICKERS.CREEPER_FACE).stickerBlock()
    .register()
  val FAKE_DIAMOND_STICKER = BlockGen<StickerBlock>(LibData.STICKERS.FAKE_DIAMOND).stickerBlock()
    .register()
  val TARGET_OVERLAY_STICKER = BlockGen<StickerBlock>(LibData.STICKERS.TARGET_OVERLAY).stickerBlock()
    .register()
  val BRICK_OVERLAY_STICKER = BlockGen<StickerBlock>(LibData.STICKERS.BRICK_OVERLAY).stickerBlock()
    .register()
  val RED_ARROW_STICKER = BlockGen<StickerBlock>(LibData.STICKERS.RED_ARROW).stickerBlock()
    .register()
  val GREEN_ARROW_STICKER = BlockGen<StickerBlock>(LibData.STICKERS.GREEN_ARROW).stickerBlock()
    .register()
  val BLUE_ARROW_STICKER = BlockGen<StickerBlock>(LibData.STICKERS.BLUE_ARROW).stickerBlock()
    .register()
  val YELLOW_ARROW_STICKER = BlockGen<StickerBlock>(LibData.STICKERS.YELLOW_ARROW).stickerBlock()
    .register()
  val HAZARD_STICKER = BlockGen<StickerBlock>(LibData.STICKERS.HAZARD).stickerBlock()
    .register()
  val RECYCLE_STICKER = BlockGen<StickerBlock>(LibData.STICKERS.RECYCLE).stickerBlock()
    .register()
  val WARNING_STICKER = BlockGen<StickerBlock>(LibData.STICKERS.WARNING).stickerBlock()
    .register()
  val SAFETY_TAPE_STICKER = BlockGen<StickerBlock>(LibData.STICKERS.SAFETY_TAPE).stickerBlock()
    .register()
  val SAFETY_TAPE_CROSS_STICKER = BlockGen<StickerBlock>(LibData.STICKERS.SAFETY_TAPE_CROSS).stickerBlock()
    .register()
  val SAFETY_TAPE_CORNER_STICKER = BlockGen<StickerBlock>(LibData.STICKERS.SAFETY_TAPE_CORNER).stickerBlock()
    .register()
  val SAFETY_TAPE_T_STICKER = BlockGen<StickerBlock>(LibData.STICKERS.SAFETY_TAPE_T).stickerBlock()
    .register() //  val JUMP_BLOCK = BlockGen<SandBlock>("jump_block").startBlock { p -> SandBlock(16250871, p) }.item().register().end() // @ Blacklist for block items, it will not register a block item for these // blocks //  val DONT_INCLUDE_BLOCK_ITEM: List<RegistryObject<*>> = mutableListOf() // @ Blacklist for creative tab, block item may still be registered //  val DONT_INCLUDE_CREATIVE: List<RegistryObject<*>> = mutableListOf()
}

