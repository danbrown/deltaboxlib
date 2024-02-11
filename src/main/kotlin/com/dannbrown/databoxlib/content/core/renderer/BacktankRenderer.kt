package com.dannbrown.databoxlib.content.core.renderer

import com.dannbrown.databoxlib.init.ProjectBlocks
import com.dannbrown.databoxlib.init.ProjectPartialModels
import com.jozufozu.flywheel.core.PartialModel
import com.mojang.blaze3d.vertex.PoseStack
import com.simibubi.create.AllBlocks
import com.simibubi.create.AllPartialModels
import com.simibubi.create.content.equipment.armor.BacktankBlock
import com.simibubi.create.content.equipment.armor.BacktankBlockEntity
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer
import com.simibubi.create.foundation.render.CachedBufferer
import com.simibubi.create.foundation.render.SuperByteBuffer
import com.simibubi.create.foundation.utility.AngleHelper
import com.simibubi.create.foundation.utility.AnimationTickHolder
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.BlockState

class BacktankRenderer(context: BlockEntityRendererProvider.Context) : KineticBlockEntityRenderer<BacktankBlockEntity>(context) {
  override fun renderSafe(be: BacktankBlockEntity, partialTicks: Float, ms: PoseStack, buffer: MultiBufferSource, light: Int, overlay: Int) {
    super.renderSafe(be, partialTicks, ms, buffer, light, overlay)
    val blockState = be.blockState
    val cogs = CachedBufferer.partial(getCogsModel(blockState), blockState)
    cogs.centre()
      .rotateY((180 + AngleHelper.horizontalAngle(blockState.getValue(BacktankBlock.HORIZONTAL_FACING))).toDouble())
      .unCentre()
      .translate(0.0, (6.5f / 16).toDouble(), (11f / 16).toDouble())
      .rotate(Direction.EAST,
        AngleHelper.rad((be.getSpeed() / 4f * AnimationTickHolder.getRenderTime(be.level) % 360).toDouble()))
      .translate(0.0, (-6.5f / 16).toDouble(), (-11f / 16).toDouble())
    cogs.light(light)
      .renderInto(ms, buffer.getBuffer(RenderType.solid()))
  }

  override fun getRotatedModel(be: BacktankBlockEntity, state: BlockState): SuperByteBuffer {
    return CachedBufferer.partial(getShaftModel(state), state)
  }

  companion object {
    fun getCogsModel(block: BlockState): PartialModel {
      if (block.`is`(AllBlocks.COPPER_BACKTANK.get())) {
        return AllPartialModels.COPPER_BACKTANK_COGS
      }
      else if (block.`is`(AllBlocks.NETHERITE_BACKTANK.get())) {
        return AllPartialModels.NETHERITE_BACKTANK_COGS
      }
      else if (block.`is`(ProjectBlocks.STEEL_BACKTANK.get())) {
        return ProjectPartialModels.STEEL_BACKTANK_COGS
      }
      return AllPartialModels.COPPER_BACKTANK_COGS
    }

    fun getShaftModel(state: BlockState): PartialModel {
      if (state.`is`(AllBlocks.COPPER_BACKTANK.get())) {
        return AllPartialModels.COPPER_BACKTANK_SHAFT
      }
      else if (state.`is`(AllBlocks.NETHERITE_BACKTANK.get())) {
        return AllPartialModels.NETHERITE_BACKTANK_SHAFT
      }
      else if (state.`is`(ProjectBlocks.STEEL_BACKTANK.get())) {
        return ProjectPartialModels.STEEL_BACKTANK_SHAFT
      }
      return AllPartialModels.COPPER_BACKTANK_SHAFT
    }
  }
}

