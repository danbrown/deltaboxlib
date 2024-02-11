package com.dannbrown.databoxlib.content.block.pressureChamberValve

import com.dannbrown.databoxlib.init.ProjectPartialModels
import com.mojang.blaze3d.vertex.PoseStack
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer
import com.simibubi.create.foundation.render.CachedBufferer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.core.Direction

class PressureChamberBlockRenderer(context: BlockEntityRendererProvider.Context?) : SafeBlockEntityRenderer<PressureChamberTileEntity>() {
  override fun renderSafe(
    be: PressureChamberTileEntity,
    partialTicks: Float,
    ms: PoseStack,
    bufferSource: MultiBufferSource,
    light: Int,
    overlay: Int
  ) {
    if (!be.blockState.getValue(PressureChamberCapBlock.ON_A_BASIN)) return
    val facing: Direction = be.blockState.getValue(HorizontalKineticBlock.HORIZONTAL_FACING)
    var i = 0.4375f

    if (facing === Direction.EAST || facing === Direction.NORTH) i = 0.5625f
    CachedBufferer.partial(ProjectPartialModels.SMALL_GAUGE_DIAL, be.blockState)
      .translate(
        if (facing.axis === Direction.Axis.Z) i.toDouble() else 0.5,
        -0.375,
        if (facing.axis === Direction.Axis.X) i.toDouble() else 0.5
      )
      .rotateY((-facing.toYRot() + 180).toDouble())
      .rotateZ(be.processingTime.toDouble() / (if (be.getRecipe() != null) be.getRecipe()!!.processingDuration else 20).toDouble() * -9 / 2 + 90)
      .renderInto(ms, bufferSource.getBuffer(RenderType.solid()))
  }
}

