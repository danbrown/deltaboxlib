package com.dannbrown.deltaboxlib.content.item.arrow

import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import net.minecraft.client.renderer.entity.ArrowRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.projectile.AbstractArrow

open class BaseArrowRenderer<T : AbstractArrow>(
  ctx: EntityRendererProvider.Context,
  private val modId: String,
  private val textureLocationString: String
) : ArrowRenderer<T>(ctx) {
  override fun getTextureLocation(arrow: T): ResourceLocation {
    return DeltaboxUtil.resourceLocation(modId, "textures/entity/projectiles/${textureLocationString}.png")
  }
}