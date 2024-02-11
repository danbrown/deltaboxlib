package com.dannbrown.databoxlib.init

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.content.fluid.FluidPropertiesExtended
import com.dannbrown.databoxlib.content.fluid.FluidVariant
import com.dannbrown.databoxlib.datagen.content.FluidGen
import com.dannbrown.databoxlib.datagen.fuel.ProjectFuel
import com.dannbrown.databoxlib.lib.LibData
import com.tterrag.registrate.util.entry.FluidEntry
import net.minecraft.world.effect.MobEffects
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fluids.FluidType
import net.minecraftforge.fluids.ForgeFlowingFluid
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries

object ProjectFluids {
  val FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, ProjectContent.MOD_ID)
  val FLUID_BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ProjectContent.MOD_ID)
  val FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, ProjectContent.MOD_ID)
  val FLUID_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ProjectContent.MOD_ID)

  // Load this class
  fun register(bus: IEventBus?) {
    FLUID_BLOCKS.register(bus)
    FLUIDS.register(bus)
    FLUID_TYPES.register(bus)
    FLUID_ITEMS.register(bus)
  }

  // @ FLUIDS
//  val HYDROGEN_BLOCK: RegistryObject<LiquidBlock> = FLUID_BLOCKS.register(LibData.FLUIDS.HYDROGEN) { LiquidBlock(HYDROGEN_SOURCE, BlockBehaviour.Properties.copy(Blocks.WATER)) }
//  val HYDROGEN_CANISTER = FLUID_ITEMS.register(LibData.FLUIDS.HYDROGEN + "_bucket") { BucketItem(HYDROGEN_SOURCE, LibUtils.defaultItemProps()) }
//  val HYDROGEN_TYPE: RegistryObject<FluidType> = FLUID_TYPES.register(LibData.FLUIDS.HYDROGEN) { GeneralFluidType(FluidVariant.LIGHT, 0x00FFFF , lightFluidProps(FluidType.Properties.create())) }
//  val HYDROGEN_SOURCE: RegistryObject<FlowingFluid>  = FLUIDS.register(LibData.FLUIDS.HYDROGEN + "_source") { SpecialFluid.Source(HYDROGEN_PROPERTIES, FluidPropertiesExtended().cryogenic()) }
//  val HYDROGEN_FLOWING: RegistryObject<FlowingFluid> = FLUIDS.register(LibData.FLUIDS.HYDROGEN + "_flow") { SpecialFluid.Flowing(HYDROGEN_PROPERTIES, FluidPropertiesExtended().cryogenic()) }
//  val HYDROGEN_PROPERTIES: ForgeFlowingFluid.Properties = ForgeFlowingFluid.Properties(HYDROGEN_TYPE, HYDROGEN_SOURCE, HYDROGEN_FLOWING).bucket(HYDROGEN_CANISTER).block(HYDROGEN_BLOCK)
  val HYDROGEN = FluidGen.createTankFluid(
    LibData.FLUIDS.HYDROGEN,
    FluidVariant.LIGHT,
    0x40CDE2E2,
    0.5f,
    true,
    1,
    1,
    8,
    100f,
    { p: FluidType.Properties -> gasFluidProps(p) },
    { p: FluidPropertiesExtended ->
      p.cryogenic()
        .harmful(
//      DataboxDamageSources.GAS_SUFFOCATION,
          4f)
    },
    { t -> t }
  )
  val NITROGEN = FluidGen.createTankFluid(
    LibData.FLUIDS.NITROGEN,
    FluidVariant.LIGHT,
    0x4084A9FF,
    0.5f,
    true,
    1,
    1,
    8,
    100f,
    { p: FluidType.Properties -> gasFluidProps(p) },
    { p: FluidPropertiesExtended ->
      p.cryogenic()
        .harmful(
//      DataboxDamageSources.GAS_SUFFOCATION,
          4f)
    },
    { t -> t }
  )
  val OXYGEN = FluidGen.createTankFluid(
    LibData.FLUIDS.OXYGEN,
    FluidVariant.LIGHT,
    0x40F3F5FA,
    0.5f,
    true,
    1,
    1,
    8,
    100f,
    { p: FluidType.Properties -> gasFluidProps(p) },
    { p: FluidPropertiesExtended ->
      p.cryogenic()
        .harmful(
//      DataboxDamageSources.GAS_SUFFOCATION,
          4f)
    },
    { t -> t }
  )

  //  val SULFURIC_ACID = FluidGenerators.simpleFluid(
//    LibData.FLUIDS.SULFURIC_ACID,
//    FluidVariant.LIGHT,
//  )
  val SULFURIC_ACID = FluidGen.createTankFluid(
    LibData.FLUIDS.SULFURIC_ACID,
    FluidVariant.DEFAULT,
    0xD3f8fccf,
    2f,
    true,
    2,
    5,
    8,
    100f,
    { p: FluidType.Properties -> defaultFluidProps(p) },
    { p: FluidPropertiesExtended ->
      p.harmful(
//      DataboxDamageSources.ACID_CORROSION,
        4f)
        .giveEffect(listOf(MobEffects.POISON))
    },
    { t -> t }
  )
  val NITRIC_ACID = FluidGen.createTankFluid(
    LibData.FLUIDS.NITRIC_ACID,
    FluidVariant.DEFAULT,
    0xD3e3cb52,
    2f,
    true,
    2,
    5,
    8,
    100f,
    { p: FluidType.Properties -> defaultFluidProps(p) },
    { p: FluidPropertiesExtended ->
      p.harmful(
//      DataboxDamageSources.ACID_CORROSION,
        4f)
        .giveEffect(listOf(MobEffects.WITHER))
    },
    { t -> t }
  )
  val PROPANE = FluidGen.createTankFluid(
    LibData.FLUIDS.PROPANE,
    FluidVariant.DEFAULT,
    0x80797FE8,
    2f,
    true,
    2,
    5,
    8,
    100f,
    { p: FluidType.Properties -> defaultFluidProps(p) },
    { p: FluidPropertiesExtended -> p.giveEffect(listOf(MobEffects.CONFUSION)) },
    { t -> t.onRegister { f -> ProjectFuel.register(f, 5f, 3f) } }
  )
  val HARD_WATER = FluidGen.createBucketFluid(
    LibData.FLUIDS.HARD_WATER,
    FluidVariant.DEFAULT,
    0xd32d69ff,
    32f,
    true,
    2,
    18,
    4,
    100f,
    { p: FluidType.Properties -> defaultFluidProps(p) },
    { p: FluidPropertiesExtended -> p },
    { t -> t }
  )
  val INK = FluidGen.createBucketFluid(
    LibData.FLUIDS.INK,
    FluidVariant.DEFAULT,
    0xff2C2D2E,
    32f,
    true,
    1,
    9,
    4,
    100f,
    { p: FluidType.Properties -> defaultFluidProps(p) },
    { p: FluidPropertiesExtended -> p },
    { t -> t }
  )
  val GLOW_INK = FluidGen.createBucketFluid(
    LibData.FLUIDS.GLOW_INK,
    FluidVariant.DEFAULT,
    0xff32a1a1,
    32f,
    true,
    1,
    9,
    4,
    100f,
    { p: FluidType.Properties -> defaultFluidProps(p).lightLevel(15) },
    { p: FluidPropertiesExtended -> p.giveEffect(listOf(MobEffects.GLOWING), 20 * 20) } // 20 seconds
    ,
    { t -> t }
  )
  val LATEX = FluidGen.createBucketFluid(
    LibData.FLUIDS.LATEX,
    FluidVariant.DENSE,
    0xf0FFFFFF,
    32f,
    true,
    2,
    18,
    4,
    100f,
    { p: FluidType.Properties -> defaultFluidProps(p) },
    { p: FluidPropertiesExtended -> p.sticky() },
    { t -> t }
  )
  val CREOSOTE_OIL = FluidGen.createBucketFluid(
    LibData.FLUIDS.CREOSOTE_OIL,
    FluidVariant.DENSE,
    0xff1f1303,
    0.05f,
    true,
    2,
    25,
    3,
    100f,
    { p: FluidType.Properties -> denseFluidProps(p) },
    { p: FluidPropertiesExtended ->
      p.sticky()
        .giveEffect(listOf(MobEffects.POISON))
    },
    { t -> t }
  )
  val GASOLINE = FluidGen.createBucketFluid(
    LibData.FLUIDS.GASOLINE,
    FluidVariant.LIGHT,
    0xa0e6cb55,
    5f,
    true,
    2,
    5,
    8,
    100f,
    { p: FluidType.Properties -> lightFluidProps(p) },
    { p: FluidPropertiesExtended -> p.giveEffect(listOf(MobEffects.CONFUSION)) },
    { t -> t.onRegister { f -> ProjectFuel.register(f, 5f, 3f) } }
  )
  val ETHANOL = FluidGen.createBucketFluid(
    LibData.FLUIDS.ETHANOL,
    FluidVariant.DENSE,
    0xd3fffff2,
    5f,
    true,
    2,
    5,
    8,
    100f,
    { p: FluidType.Properties -> lightFluidProps(p) },
    { p: FluidPropertiesExtended -> p.giveEffect(listOf(MobEffects.CONFUSION)) },
    { t -> t.onRegister { f -> ProjectFuel.register(f, 5f, 3f) } }
  )
  val BIODIESEL = FluidGen.createBucketFluid(
    LibData.FLUIDS.BIODIESEL,
    FluidVariant.DENSE,
    0xd3ad7f13,
    2.5f,
    true,
    2,
    5,
    8,
    100f,
    { p: FluidType.Properties -> denseFluidProps(p) },
    { p: FluidPropertiesExtended -> p.giveEffect(listOf(MobEffects.CONFUSION)) },
    { t -> t.onRegister { f -> ProjectFuel.register(f, 5f, 3f) } }
  )
  val KEROSENE = FluidGen.createBucketFluid(
    LibData.FLUIDS.KEROSENE,
    FluidVariant.DENSE,
    0x804CA3D5,
    5f,
    true,
    2,
    5,
    8,
    100f,
    { p: FluidType.Properties -> lightFluidProps(p) },
    { p: FluidPropertiesExtended -> p.giveEffect(listOf(MobEffects.CONFUSION)) },
    { t -> t.onRegister { f -> ProjectFuel.register(f, 5f, 3f) } }
  )
  val PLANT_OIL = FluidGen.createBucketFluid(
    LibData.FLUIDS.PLANT_OIL,
    FluidVariant.DENSE,
    0xd3eefcc7,
    2f,
    true,
    2,
    18,
    4,
    100f,
    { p: FluidType.Properties -> defaultFluidProps(p) },
    { p: FluidPropertiesExtended -> p.sticky() },
    { t -> t }
  )

  //
//  val MOLTEN_IRON_PROPS = FluidPropertiesExtended().sticky().hot().harmful(DataboxDamageSources.MOLTEN_METAL, 4f)
//  val MOLTEN_IRON = DataboxMod.REGISTRATE.fluid<ForgeFlowingFluid>(LibData.FLUIDS.MOLTEN_IRON,
//    fluidStillResourceLocation(FluidVariant.MOLTEN),
//    fluidFlowResourceLocation(FluidVariant.MOLTEN),
//    DataboxFluidType.create(0xFF00FF, 1f, true),
//  ) { p: ForgeFlowingFluid.Properties -> FluidGen.Flowing(p, MOLTEN_IRON_PROPS)}
//    .source { p: ForgeFlowingFluid.Properties -> FluidGen.Source(p, MOLTEN_IRON_PROPS)}
//    .properties { b: FluidType.Properties -> moltenFluidProps(b) }
//    .fluidProperties { p: ForgeFlowingFluid.Properties -> p.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(100f) }
//    .tag(AllTags.forgeFluidTag(LibData.FLUIDS.MOLTEN_IRON))
//    .bucket()
//    .build()
//    .register()
  val TRANSPARENT_RENDERING: List<FluidEntry<ForgeFlowingFluid>> = mutableListOf(
//    HYDROGEN,
//    SULFURIC_ACID,
//    NITRIC_ACID,
//    CREOSOTE_OIL,
//    LATEX,
//    GASOLINE,
//    ETHANOL,
//    BIODIESEL,
//    KEROSENE,
//    PROPANE,
//    PLANT_OIL,
//    MOLTEN_IRON,
  )

  // Fluid Props
  private fun gasFluidProps(p: FluidType.Properties): FluidType.Properties {
    return p
      .motionScale(0.002)
      .canExtinguish(true)
      .density(-2000)
      .viscosity(0)
  }

  private fun lightFluidProps(p: FluidType.Properties): FluidType.Properties {
    return p
      .motionScale(0.002)
      .canExtinguish(true)
      .density(100)
      .viscosity(100)
  }

  private fun denseFluidProps(p: FluidType.Properties): FluidType.Properties {
    return p
      .motionScale(0.00116666666)
      .canExtinguish(true)
      .density(1500)
      .viscosity(1000)
  }

  private fun defaultFluidProps(p: FluidType.Properties): FluidType.Properties {
    return p
      .motionScale(0.002)
      .canExtinguish(true)
      .density(100)
      .viscosity(100)
  }

  private fun moltenFluidProps(p: FluidType.Properties): FluidType.Properties {
    return p
      .motionScale(0.00116666666)
      .canExtinguish(true)
      .density(1500)
      .temperature(600)
      .viscosity(1000)
      .lightLevel(15)
  }
}


