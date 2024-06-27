package com.dannbrown.deltaboxlib.content.entity.projectile

import net.minecraft.client.renderer.entity.ArrowRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.projectile.AbstractArrow

open class BaseArrowRenderer<T : AbstractArrow>(ctx: EntityRendererProvider.Context, private val modId: String, private val textureLocationString: String) : ArrowRenderer<T>(ctx) {
  override fun getTextureLocation(arrow: T): ResourceLocation {
    return ResourceLocation(modId, "textures/entity/projectiles/${textureLocationString}.png")
  }
}