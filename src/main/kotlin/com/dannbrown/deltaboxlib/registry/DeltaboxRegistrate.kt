package com.dannbrown.deltaboxlib.registry

import com.dannbrown.deltaboxlib.DeltaboxLib
import com.dannbrown.deltaboxlib.lib.LibLang
import com.tterrag.registrate.AbstractRegistrate
import com.tterrag.registrate.builders.FluidBuilder
import com.tterrag.registrate.builders.FluidBuilder.FluidTypeFactory
import com.tterrag.registrate.util.entry.RegistryEntry
import com.tterrag.registrate.util.nullness.NonNullConsumer
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.level.block.Block
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fluids.FluidType
import net.minecraftforge.fluids.ForgeFlowingFluid
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.registries.RegistryObject
import java.util.*
import java.util.function.Consumer
import java.util.function.Supplier

// This Registrate file is based on CreateRegistrate: https://github.com/Creators-of-Create/Create/blob/mc1.18/dev/src/main/java/com/simibubi/create/foundation/data/CreateRegistrate.java
class DeltaboxRegistrate(modId: String) : AbstractRegistrate<DeltaboxRegistrate>(modId) {
  var creativeTab: RegistryObject<CreativeModeTab>? = null
    protected set


  fun setCreativeTab(tab: RegistryObject<CreativeModeTab>): DeltaboxRegistrate {
    creativeTab = tab
    return self()
  }

  public override fun registerEventListeners(bus: IEventBus): DeltaboxRegistrate {
    return super.registerEventListeners(bus)
  }



  /* Fluids */
  fun standardFluid(name: String): FluidBuilder<ForgeFlowingFluid.Flowing, DeltaboxRegistrate> {
    return fluid(name, ResourceLocation(modid, "fluid/" + name + "_still"), ResourceLocation(modid, "fluid/" + name + "_flow"))
  }

  fun standardFluid(name: String,
                    typeFactory: FluidTypeFactory
  ): FluidBuilder<ForgeFlowingFluid.Flowing, DeltaboxRegistrate> {
    return fluid(name, ResourceLocation(modid, "fluid/" + name + "_still"), ResourceLocation(modid, "fluid/" + name + "_flow"),
      typeFactory)
  }

  // @ LANG

  fun addFormulaLang(
    formula: String,
    name: String,
  ) : MutableComponent{
    return addRawLang("formula.${modid}.$formula", name)
  }

  fun addItemTooltipLang(
    itemId: String,
    phrase: String,
    mId: String = modid,
  ) :MutableComponent {
    return addRawLang(LibLang.getTooltipKey(mId, itemId), phrase)
  }

  fun addGenericTooltipLang(
    itemId: String,
    phrase: String,
  ) :MutableComponent {
    return addRawLang(LibLang.getTooltipKey(null, itemId), phrase)
  }

  fun addCreativeTabLang(
    tab: String,
    name: String,
    _modid: String = modid,
  ) :MutableComponent {
    return addRawLang("itemGroup.${_modid}.$tab", name)
  }

  fun addDeathMessageLang(
    name: String,
    phrase: String,
    _modid: String = modid,
  ) :MutableComponent {
    return addRawLang("death.attack.$name", phrase)
  }

  fun addGogglesLang(
    name: String,
    phrase: String,
    _modid: String = modid,
  ) :MutableComponent {
   return  addRawLang("${_modid}.gui.goggles.$name", phrase)
  }

  fun addBiomeLang(
    name: String,
    phrase: String,
    _modid: String = modid,
  ) :MutableComponent {
    return addRawLang("biome.${_modid}.$name", phrase)
  }

  fun addDimensionLang(
    name: String,
    phrase: String,
    _modid: String = modid,
  ) :MutableComponent {
    return addRawLang("dimension.${_modid}.$name", phrase)
  }

  fun addWorldPresetLang(
    name: String,
    phrase: String,
    _modid: String = modid,
  ) :MutableComponent {
    return addRawLang("generator.${_modid}.$name", phrase)
  }



  companion object {

    private val TAB_LOOKUP: Map<RegistryEntry<*>, RegistryObject<CreativeModeTab>> = IdentityHashMap()

    fun isInCreativeTab(entry: RegistryEntry<*>, tab: RegistryObject<CreativeModeTab>): Boolean {
      return TAB_LOOKUP.get(entry) === tab
    }

    fun defaultFluidType(properties: FluidType.Properties, stillTexture: ResourceLocation,
                         flowingTexture: ResourceLocation
    ): FluidType {
      return object : FluidType(properties) {
        override fun initializeClient(consumer: Consumer<IClientFluidTypeExtensions>) {
          consumer.accept(object : IClientFluidTypeExtensions {
            override fun getStillTexture(): ResourceLocation {
              return stillTexture
            }

            override fun getFlowingTexture(): ResourceLocation {
              return flowingTexture
            }
          })
        }
      }
    }

    protected fun onClient(toRun: Supplier<Runnable>) {
      DistExecutor.unsafeRunWhenOn(Dist.CLIENT, toRun)
    }


  }
}