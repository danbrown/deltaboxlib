package com.dannbrown.deltaboxlib.registrate.datagen

import net.minecraft.data.models.model.ModelTemplate
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.resources.ResourceLocation
import java.util.*

object RegistrateModelTemplates {
  fun create(vararg textureSlots: TextureSlot): ModelTemplate {
    return ModelTemplate(Optional.empty(), Optional.empty(), *textureSlots)
  }

  fun create(string: String, vararg textureSlots: TextureSlot): ModelTemplate {
    return ModelTemplate(Optional.of(ResourceLocation("minecraft", "block/$string")), Optional.empty(), *textureSlots)
  }

  fun createItem(string: String, vararg textureSlots: TextureSlot): ModelTemplate {
    return ModelTemplate(Optional.of(ResourceLocation("minecraft", "item/$string")), Optional.empty(), *textureSlots)
  }

  fun create(string: String, string2: String, vararg textureSlots: TextureSlot): ModelTemplate {
    return ModelTemplate(Optional.of(ResourceLocation("minecraft", "block/$string")), Optional.of(string2), *textureSlots)
  }
}