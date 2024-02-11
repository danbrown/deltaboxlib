package com.dannbrown.databoxlib.init

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.content.block.cargoBay.CargoBayBlockEntity
import com.dannbrown.databoxlib.content.block.invertedClutch.InvertedClutchBlockEntity
import com.dannbrown.databoxlib.content.block.kineticElectrolyzer.KineticElectrolyzerInstance
import com.dannbrown.databoxlib.content.block.kineticElectrolyzer.KineticElectrolyzerRenderer
import com.dannbrown.databoxlib.content.block.kineticElectrolyzer.KineticElectrolyzerTileEntity
import com.dannbrown.databoxlib.content.block.mechanicalCooler.MechanicalCoolerInstance
import com.dannbrown.databoxlib.content.block.mechanicalCooler.MechanicalCoolerRenderer
import com.dannbrown.databoxlib.content.block.mechanicalCooler.MechanicalCoolerTileEntity
import com.dannbrown.databoxlib.content.block.pressureChamberValve.PressureChamberBlockRenderer
import com.dannbrown.databoxlib.content.block.pressureChamberValve.PressureChamberTileEntity
import com.dannbrown.databoxlib.content.block.resistor.ResistorBlockEntity
import com.dannbrown.databoxlib.content.blockEntity.CustomSignBlockEntity
import com.dannbrown.databoxlib.content.core.renderer.BacktankRenderer
import com.dannbrown.databoxlib.content.ship.block.altitudeSensor.AltitudeSensorBlockEntity
import com.dannbrown.databoxlib.content.ship.block.angularSensor.AngularSensorBlockEntity
import com.dannbrown.databoxlib.content.ship.block.captainSeat.CaptainSeatBlockEntity
import com.dannbrown.databoxlib.content.ship.block.distanceSensor.DistanceSensorBlockEntity
import com.dannbrown.databoxlib.content.ship.block.sail.SailBlockEntity
import com.dannbrown.databoxlib.content.ship.block.shipConsole.ShipConsoleBlockEntity
import com.dannbrown.databoxlib.content.ship.block.thruster.ThrusterBlockEntity
import com.dannbrown.databoxlib.content.ship.block.thruster.ThrusterBlockInstance
import com.dannbrown.databoxlib.content.ship.block.thruster.ThrusterBlockRenderer
import com.dannbrown.databoxlib.lib.LibData
import com.jozufozu.flywheel.api.MaterialManager
import com.simibubi.create.content.equipment.armor.BacktankBlockEntity
import com.simibubi.create.content.equipment.armor.BacktankInstance
import com.simibubi.create.content.kinetics.transmission.SplitShaftInstance
import com.simibubi.create.content.kinetics.transmission.SplitShaftRenderer
import com.tterrag.registrate.builders.BlockEntityBuilder.BlockEntityFactory
import com.tterrag.registrate.util.nullness.NonNullFunction
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.eventbus.api.IEventBus
import java.util.function.BiFunction

object ProjectBlockEntities {
  val INVERTED_CLUTCH = ProjectContent.REGISTRATE
    .blockEntity("inverted_clutch", BlockEntityFactory(::InvertedClutchBlockEntity))
    .instance({ BiFunction { materialManager: MaterialManager, blockEntity: InvertedClutchBlockEntity -> SplitShaftInstance(materialManager, blockEntity) } }, false)
    .validBlocks(ProjectBlocks.INVERTED_CLUTCH)
    .renderer { NonNullFunction { SplitShaftRenderer(it) } }
    .register()
  val RESISTOR = ProjectContent.REGISTRATE
    .blockEntity("resistor", BlockEntityFactory(::ResistorBlockEntity))
    .instance({ BiFunction { materialManager: MaterialManager, blockEntity: ResistorBlockEntity -> SplitShaftInstance(materialManager, blockEntity) } }, false)
    .validBlocks(ProjectBlocks.RESISTOR)
    .renderer { NonNullFunction { SplitShaftRenderer(it) } }
    .register()
  val CAPTAIN_SEAT = ProjectContent.REGISTRATE
    .blockEntity("captain_seat", BlockEntityFactory(::CaptainSeatBlockEntity))
    .validBlocks(ProjectBlocks.CAPTAIN_SEAT)
    .register()
  val DISTANCE_SENSOR = ProjectContent.REGISTRATE
    .blockEntity("distance_sensor", BlockEntityFactory(::DistanceSensorBlockEntity))
    .validBlocks(ProjectBlocks.DISTANCE_SENSOR)
    .register()
  val ALTITUDE_SENSOR = ProjectContent.REGISTRATE
    .blockEntity("altitude_sensor", BlockEntityFactory(::AltitudeSensorBlockEntity))
    .validBlocks(ProjectBlocks.ALTITUDE_SENSOR)
    .register()
  val ANGULAR_SENSOR = ProjectContent.REGISTRATE
    .blockEntity("angular_sensor", BlockEntityFactory(::AngularSensorBlockEntity))
    .validBlocks(ProjectBlocks.ANGULAR_SENSOR)
    .register()
  val SHIP_CONSOLE = ProjectContent.REGISTRATE
    .blockEntity("ship_console", BlockEntityFactory(::ShipConsoleBlockEntity))
    .validBlocks(ProjectBlocks.SHIP_CONSOLE)
    .register()
  val THRUSTER = ProjectContent.REGISTRATE
    .blockEntity("thruster", BlockEntityFactory(::ThrusterBlockEntity))
    .instance { BiFunction { materialManager: MaterialManager, blockEntity: ThrusterBlockEntity -> ThrusterBlockInstance(materialManager, blockEntity) } }
    .validBlocks(ProjectBlocks.BASIC_THRUSTER, ProjectBlocks.SCALAR_THRUSTER)
    .renderer { NonNullFunction { ThrusterBlockRenderer(it) } }
    .register()
  val SAIL_BLOCK = ProjectContent.REGISTRATE
    .blockEntity("sail_block", BlockEntityFactory(::SailBlockEntity))
    .validBlocks(ProjectBlocks.SAIL_BLOCK)
    .register()
  val BACKTANK = ProjectContent.REGISTRATE
    .blockEntity<BacktankBlockEntity>("backtank") { type: BlockEntityType<BacktankBlockEntity>, pos: BlockPos, state: BlockState -> BacktankBlockEntity(type, pos, state) }
    .instance { BiFunction { materialManager: MaterialManager, blockEntity: BacktankBlockEntity -> BacktankInstance(materialManager, blockEntity) } }
    .validBlocks(ProjectBlocks.STEEL_BACKTANK)
    .renderer { NonNullFunction { context: BlockEntityRendererProvider.Context -> BacktankRenderer(context) } }
    .register()
  val MECHANICAL_COOLER_TILE = ProjectContent.REGISTRATE
    .blockEntity(LibData.BLOCKS.MECHANICAL_COOLER, BlockEntityFactory(::MechanicalCoolerTileEntity))
    .instance { BiFunction { manager, tile -> MechanicalCoolerInstance(manager, tile) } }
    .validBlock(ProjectBlocks.MECHANICAL_COOLER)
    .renderer { NonNullFunction { MechanicalCoolerRenderer(it) } }
    .register()
  val KINETIC_ELECTROLYZER_TILE = ProjectContent.REGISTRATE
    .blockEntity(LibData.BLOCKS.KINETIC_ELECTROLYZER, BlockEntityFactory(::KineticElectrolyzerTileEntity))
    .instance { BiFunction { manager, tile -> KineticElectrolyzerInstance(manager, tile) } }
    .validBlocks(ProjectBlocks.KINETIC_ELECTROLYZER)
    .renderer { NonNullFunction { KineticElectrolyzerRenderer(it) } }
    .register()
  val PRESSURE_CHAMBER_VALVE = ProjectContent.REGISTRATE
    .blockEntity(LibData.BLOCKS.PRESSURE_CHAMBER_CAP, BlockEntityFactory(::PressureChamberTileEntity))
    .renderer { NonNullFunction { PressureChamberBlockRenderer(it) } }
    .validBlocks(ProjectBlocks.PRESSURE_CHAMBER_VALVE)
    .register()
  val CARGO_BAY = ProjectContent.REGISTRATE
    .blockEntity(LibData.BLOCKS.CARGO_BAY, BlockEntityFactory(::CargoBayBlockEntity))
    .validBlocks(ProjectBlocks.CARGO_BAY)
    .register()
  val CUSTOM_SIGN = ProjectContent.REGISTRATE
    .blockEntity("custom_sign", BlockEntityFactory<CustomSignBlockEntity> { be, bp, bs -> CustomSignBlockEntity(bp, bs) })
    .validBlocks(ProjectBlocks.JOSHUA_FAMILY.SIGN!!,
      ProjectBlocks.JOSHUA_FAMILY.WALL_SIGN!!
    )
    .register()

  //  public static final BlockEntityEntry<ItemVaultBlockEntity> ITEM_VAULT = REGISTRATE
//  .blockEntity("item_vault", ItemVaultBlockEntity::new)
//  .validBlocks(AllBlocks.ITEM_VAULT)
//  .register();
  fun register(modBus: IEventBus) {}
}