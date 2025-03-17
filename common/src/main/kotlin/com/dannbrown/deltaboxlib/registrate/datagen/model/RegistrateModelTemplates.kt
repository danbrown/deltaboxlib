package com.dannbrown.deltaboxlib.registrate.datagen.model

import com.dannbrown.deltaboxlib.init.DeltaboxLibMod
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import net.minecraft.data.models.model.ModelTemplate
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.resources.ResourceLocation
import java.util.*

object RegistrateModelTemplates {
  // ITEM
  val FLAT_ITEM = create(DeltaboxUtil.resourceLocation("minecraft", "item/generated"), TextureSlot.LAYER0)
  val FLAT_HANDHELD_ITEM = create(DeltaboxUtil.resourceLocation("minecraft", "item/handheld"), TextureSlot.LAYER0)
  val SPAWN_EGG = create(DeltaboxUtil.resourceLocation("minecraft", "item/template_spawn_egg"))

  // BLOCK
  val CUBE_ALL = create(DeltaboxUtil.resourceLocation("minecraft", "block/cube_all"), RegistrateTextureSlots.ALL_SLOT)
  val LEAVES =
    create(
      DeltaboxUtil.resourceLocation(DeltaboxLibMod.MOD_ID, "block/template_leaves"),
      RegistrateTextureSlots.ALL_SLOT
    )

  val CROP =
    create(DeltaboxUtil.resourceLocation(DeltaboxLibMod.MOD_ID, "block/template_crop"), TextureSlot.CROP)
  val BOTTOM_TOP = create(
    DeltaboxUtil.resourceLocation("minecraft", "block/cube_bottom_top"),
    TextureSlot.BOTTOM,
    TextureSlot.TOP,
    TextureSlot.SIDE
  )

  val ROTATED_PILLAR = create(
    DeltaboxUtil.resourceLocation("minecraft", "block/cube_column"),
    TextureSlot.END,
    TextureSlot.SIDE
  )
  val CROSS = create(DeltaboxUtil.resourceLocation(DeltaboxLibMod.MOD_ID, "block/template_cross"), TextureSlot.CROSS)
  val POTTED_FLOWER =
    create(DeltaboxUtil.resourceLocation(DeltaboxLibMod.MOD_ID, "block/template_flower_pot_cross"), TextureSlot.PLANT)
  val BOTTOM_TOP_WALL_POST =
    create(
      DeltaboxUtil.resourceLocation(DeltaboxLibMod.MOD_ID, "block/template_wall_post"),
      TextureSlot.TOP,
      TextureSlot.BOTTOM,
      TextureSlot.WALL
    )
  val BOTTOM_TOP_WALL_INVENTORY =
    create(
      DeltaboxUtil.resourceLocation(DeltaboxLibMod.MOD_ID, "block/template_wall_inventory_top"),
      TextureSlot.TOP,
      TextureSlot.WALL
    )

  val TRAPDOOR_TOP = create(
    DeltaboxUtil.resourceLocation(DeltaboxLibMod.MOD_ID, "block/template_trapdoor_top"),
    TextureSlot.TEXTURE,
  )

  val TRAPDOOR_BOTTOM = create(
    DeltaboxUtil.resourceLocation(DeltaboxLibMod.MOD_ID, "block/template_trapdoor_bottom"),
    TextureSlot.TEXTURE,
  )

  val TRAPDOOR_OPEN = create(
    DeltaboxUtil.resourceLocation(DeltaboxLibMod.MOD_ID, "block/template_trapdoor_open"),
    TextureSlot.TEXTURE,
  )
  val DOOR_BOTTOM_LEFT = create(
    DeltaboxUtil.resourceLocation(DeltaboxLibMod.MOD_ID, "block/template_door_bottom_left"),
    TextureSlot.TOP,
    TextureSlot.BOTTOM,
  )
  val DOOR_BOTTOM_LEFT_OPEN = create(
    DeltaboxUtil.resourceLocation(DeltaboxLibMod.MOD_ID, "block/template_door_bottom_left_open"),
    TextureSlot.TOP,
    TextureSlot.BOTTOM,
  )
  val DOOR_BOTTOM_RIGHT = create(
    DeltaboxUtil.resourceLocation(DeltaboxLibMod.MOD_ID, "block/template_door_bottom_right"),
    TextureSlot.TOP,
    TextureSlot.BOTTOM,
  )
  val DOOR_BOTTOM_RIGHT_OPEN = create(
    DeltaboxUtil.resourceLocation(DeltaboxLibMod.MOD_ID, "block/template_door_bottom_right_open"),
    TextureSlot.TOP,
    TextureSlot.BOTTOM,
  )
  val DOOR_TOP_LEFT = create(
    DeltaboxUtil.resourceLocation(DeltaboxLibMod.MOD_ID, "block/template_door_top_left"),
    TextureSlot.TOP,
    TextureSlot.BOTTOM,
  )
  val DOOR_TOP_LEFT_OPEN = create(
    DeltaboxUtil.resourceLocation(DeltaboxLibMod.MOD_ID, "block/template_door_top_left_open"),
    TextureSlot.TOP,
    TextureSlot.BOTTOM,
  )
  val DOOR_TOP_RIGHT = create(
    DeltaboxUtil.resourceLocation(DeltaboxLibMod.MOD_ID, "block/template_door_top_right"),
    TextureSlot.TOP,
    TextureSlot.BOTTOM,
  )
  val DOOR_TOP_RIGHT_OPEN = create(
    DeltaboxUtil.resourceLocation(DeltaboxLibMod.MOD_ID, "block/template_door_top_right_open"),
    TextureSlot.TOP,
    TextureSlot.BOTTOM,
  )

  fun create(parent: ResourceLocation, vararg textureSlots: TextureSlot): ModelTemplate {
    return ModelTemplate(Optional.of(parent), Optional.empty(), *textureSlots)
  }
}