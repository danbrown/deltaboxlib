package com.dannbrown.deltaboxlib.registrate.datagen.model

import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import net.minecraft.data.models.model.ModelTemplate
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.resources.ResourceLocation
import java.util.*

object RegistrateModelTemplates {
  // ITEM
  val FLAT_ITEM = create(DeltaboxUtil.resourceLocation("minecraft", "item/generated"), TextureSlot.LAYER0)
  val FLAT_HANDHELD_ITEM = create(DeltaboxUtil.resourceLocation("minecraft", "item/handheld"), TextureSlot.LAYER0)

  // BLOCK
  val CUBE_ALL = create(DeltaboxUtil.resourceLocation("minecraft", "block/cube_all"), RegistrateTextureSlots.ALL_SLOT)
  val BOTTOM_TOP = create(
    DeltaboxUtil.resourceLocation("minecraft", "block/cube_bottom_top"),
    TextureSlot.BOTTOM,
    TextureSlot.TOP,
    TextureSlot.SIDE
  )
  val CROSS = create(DeltaboxUtil.resourceLocation("minecraft", "block/cross"), TextureSlot.CROSS)
  val POTTED_FLOWER = create(DeltaboxUtil.resourceLocation("minecraft", "block/flower_pot_cross"), TextureSlot.PLANT)

  fun create(parent: ResourceLocation, vararg textureSlots: TextureSlot): ModelTemplate {
    return ModelTemplate(Optional.of(parent), Optional.empty(), *textureSlots)
  }
}