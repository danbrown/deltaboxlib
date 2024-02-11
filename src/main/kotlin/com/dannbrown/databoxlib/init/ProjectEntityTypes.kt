package com.dannbrown.databoxlib.init

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.content.entity.WaterGelBubbleProjectileEntity
import com.dannbrown.databoxlib.content.entity.LavaGelBubbleProjectileEntity
import com.dannbrown.databoxlib.lib.LibData
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries


object ProjectEntityTypes {
  val ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ProjectContent.MOD_ID)

  fun register(bus: IEventBus?) {
    ENTITY_TYPES.register(bus)
  }

  val WATER_GEL_BUBBLE_PROJECTILE = ENTITY_TYPES.register(LibData.ITEMS.WATER_GEL_BUBBLE) { EntityType.Builder.of({ e, l -> WaterGelBubbleProjectileEntity(e, l) } , MobCategory.MISC).sized(0.5f, 0.5f).build(LibData.ITEMS.WATER_GEL_BUBBLE) }
  val LAVA_GEL_BUBBLE_PROJECTILE = ENTITY_TYPES.register(LibData.ITEMS.LAVA_GEL_BUBBLE) { EntityType.Builder.of({ e, l -> LavaGelBubbleProjectileEntity(e, l) } , MobCategory.MISC).sized(0.5f, 0.5f).build(LibData.ITEMS.LAVA_GEL_BUBBLE) }
}