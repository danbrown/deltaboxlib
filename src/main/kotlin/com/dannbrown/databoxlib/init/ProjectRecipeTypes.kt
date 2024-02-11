package com.dannbrown.databoxlib.init

import com.simibubi.create.foundation.utility.Lang
import com.dannbrown.databoxlib.content.recipe.CoolingRecipe
import com.dannbrown.databoxlib.lib.LibUtils
import com.simibubi.create.content.processing.recipe.ProcessingRecipe
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeFactory
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo
import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.content.recipe.ElectrolyzerRecipe
import com.dannbrown.databoxlib.content.recipe.PressureChamberRecipe
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.ShapedRecipe
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import java.util.function.Supplier
import javax.annotation.Nullable


enum class ProjectRecipeTypes : IRecipeTypeInfo {
  ELECTROLYSIS(ProcessingRecipeFactory<ProcessingRecipe<*>> { params: ProcessingRecipeParams? ->  ElectrolyzerRecipe(params!!) }),
  COOLING(ProcessingRecipeFactory<ProcessingRecipe<*>> { params: ProcessingRecipeParams? -> CoolingRecipe(params!!) }),
  PRESSURE_CHAMBER(ProcessingRecipeFactory<ProcessingRecipe<*>> { params: ProcessingRecipeParams? -> PressureChamberRecipe(params!!) });

  private val id: ResourceLocation
  private val serializerObject: RegistryObject<RecipeSerializer<*>>

  @Nullable
  private val typeObject: RegistryObject<RecipeType<*>>?
  private val type: Supplier<RecipeType<*>>?

  constructor(
    serializerSupplier: Supplier<RecipeSerializer<*>>,
    typeSupplier: Supplier<RecipeType<*>>,
    registerType: Boolean
  ) {
    val name: String = Lang.asId(name)
    id = LibUtils.resourceLocation(name)
    serializerObject = Registers.SERIALIZER_REGISTER.register(name, serializerSupplier)
    if (registerType) {
      typeObject = Registers.TYPE_REGISTER.register(name, typeSupplier)
      type = typeObject
    } else {
      typeObject = null
      type = typeSupplier
    }
  }

  constructor(serializerSupplier: Supplier<RecipeSerializer<*>>) {
    val name: String = Lang.asId(name)
    id = LibUtils.resourceLocation(name)
    serializerObject = Registers.SERIALIZER_REGISTER.register(name, serializerSupplier)
    typeObject = Registers.TYPE_REGISTER.register(name) { simpleType<Recipe<*>>(id) }
    type = typeObject
  }

//  constructor(processingFactory: ProcessingRecipeFactory<*>) : this({ ProcessingRecipeSerializer<T?>(processingFactory) })

  constructor(processingFactory: ProcessingRecipeFactory<ProcessingRecipe<*>>) : this(Supplier<RecipeSerializer<*>> {
    ProcessingRecipeSerializer<ProcessingRecipe<*>>(
      processingFactory
    )
  })

  override fun getId(): ResourceLocation {
    return id
  }

  override fun <T : RecipeSerializer<*>?> getSerializer(): T {
    return serializerObject.get() as T
  }

  override fun < T : RecipeType<*>> getType(): T {
    return type?.get() as T
  }

  private object Registers {
    val SERIALIZER_REGISTER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ProjectContent.MOD_ID)
    val TYPE_REGISTER = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, ProjectContent.MOD_ID)
  }

  companion object {
    fun <T : Recipe<*>?> simpleType(id: ResourceLocation): RecipeType<T> {
      val stringId = id.toString()
      return object : RecipeType<T> {
        override fun toString(): String {
          return stringId
        }
      }
    }

    fun register(modEventBus: IEventBus?) {
      ShapedRecipe.setCraftingSize(9, 9)
      Registers.SERIALIZER_REGISTER.register(modEventBus)
      Registers.TYPE_REGISTER.register(modEventBus)
    }
  }
}
