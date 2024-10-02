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
import oshi.util.tuples.Quartet
import oshi.util.tuples.Quintet
import java.util.*
import java.util.function.Consumer
import java.util.function.Supplier

// This Registrate file is based on CreateRegistrate: https://github.com/Creators-of-Create/Create/blob/mc1.18/dev/src/main/java/com/simibubi/create/foundation/data/CreateRegistrate.java
// its goal is to expand the Registrate API to include more features and make it easier to use
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

  fun standardFluid(name: String, typeFactory: FluidTypeFactory
  ): FluidBuilder<ForgeFlowingFluid.Flowing, DeltaboxRegistrate> {
    return fluid(name, ResourceLocation(modid, "fluid/" + name + "_still"), ResourceLocation(modid, "fluid/" + name + "_flow"), typeFactory)
  }

  // @ LANG
  fun addFormulaLang(
    formula: String,
    name: String,
  ) : MutableComponent{
    return addRawLang("formula.${modid}.$formula", name)
  }

  fun addEntityLang(
    name: String,
    phrase: String,
  ) :MutableComponent {
    return addRawLang("entity.${modid}.$name", phrase)
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

  fun addPotionLang(
    name: String,
    phrase: String,
  ) :Quartet<MutableComponent, MutableComponent, MutableComponent, MutableComponent> {
    val potion = addRawLang("item.${"minecraft"}.potion.effect.$name", "Potion of $phrase")
    val splash = addRawLang("item.${"minecraft"}.splash_potion.effect.$name", "Splash Potion of $phrase")
    val lingering = addRawLang("item.${"minecraft"}.lingering_potion.effect.$name", "Lingering Potion of $phrase")
    val arrow = addRawLang("item.${"minecraft"}.tipped_arrow.effect.$name", "Arrow of $phrase")
    return Quartet(potion, splash, lingering, arrow)
  }

  fun addAdvancementLang(
    name: String,
    title: String,
    description: String,
    _modid: String = modid,
  ) :Pair<MutableComponent, MutableComponent> {
    return Pair(addRawLang("advancements.${_modid}.$name.title", title), addRawLang("advancements.${_modid}.$name.description", description))
  }

  fun addEffectLang(
    name: String,
    phrase: String,
    _modid: String = modid,
  ) :MutableComponent {
    return addRawLang("effect.${_modid}.$name", phrase)
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

  fun addPaintingVariantLang(
    name: String,
    phrase: String,
    author: String,
    _modid: String = modid,
  ) :Pair<MutableComponent, MutableComponent> {
    return Pair(addRawLang("painting.${_modid}.$name.title", phrase), addRawLang("painting.${_modid}.$name.author", author))
  }

  companion object {

    private val TAB_LOOKUP: Map<RegistryEntry<*>, RegistryObject<CreativeModeTab>> = IdentityHashMap()

    fun isInCreativeTab(entry: RegistryEntry<*>, tab: RegistryObject<CreativeModeTab>): Boolean {
      return TAB_LOOKUP.get(entry) === tab
    }

    fun defaultFluidType(properties: FluidType.Properties, stillTexture: ResourceLocation, flowingTexture: ResourceLocation): FluidType {
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