package com.dannbrown.databoxlib.content.ship.block.distanceSensor

import com.dannbrown.databoxlib.lib.LibLang
import com.google.common.collect.ImmutableList
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour
import com.simibubi.create.foundation.blockEntity.behaviour.CenteredSideValueBoxTransform
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBehaviour.ValueSettings
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBoard
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsFormatter
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.INamedIconOptions
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour
import com.simibubi.create.foundation.gui.AllIcons
import com.simibubi.create.foundation.utility.Components
import com.simibubi.create.foundation.utility.Lang
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.util.StringRepresentable
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.BlockHitResult
import java.util.function.BiPredicate
import kotlin.math.abs
import kotlin.math.max

class DistanceSensorBlockEntity(type: BlockEntityType<DistanceSensorBlockEntity>, pos: BlockPos, state: BlockState) : SmartBlockEntity(type, pos, state) {
  companion object {
    private val DEFAULT_DISTANCE = 8
    private val MAX_DISTANCE = 64

    // translation keys
    val DISTANCE_SCROLL_TITLE = "sensors.distance.detection_distance"
    val MODE_SCROLL_TITLE = "sensors.distance.detection_mode"
  }

  fun updated() {
    sendData()
  }

  private var detectionDistance: ScrollValueBehaviour? = null
  override fun addBehaviours(behaviours: MutableList<BlockEntityBehaviour?>) {
    // DISTANCE
    detectionDistance = DistanceScrollValueBehaviour(LibLang.translateDirect(DISTANCE_SCROLL_TITLE), this, DistanceModeValueBoxTransform())
    detectionDistance!!.between(0, MAX_DISTANCE)
    detectionDistance!!.value = DEFAULT_DISTANCE
    detectionDistance!!.withCallback { i -> this.updateDetectionDistance(i) }
    behaviours.add(detectionDistance)
    registerAwardables(behaviours)
  }

  private fun updateDetectionDistance(distance: Int) {
    val state = blockState
    if (level === null) return
    if (level!!.isClientSide) return
    level!!.setBlock(worldPosition, state.setValue(DistanceSensorBlock.DISTANCE, distance), 3)
  }

  private class DistanceModeValueBoxTransform : CenteredSideValueBoxTransform(BiPredicate { state: BlockState, direction: Direction ->
    val facing = state.getValue(BlockStateProperties.FACING)
    return@BiPredicate facing.opposite !== direction
  })

  private class DistanceScrollValueBehaviour(label: Component, be: SmartBlockEntity, slot: ValueBoxTransform) : ScrollValueBehaviour(label, be, slot) {
    init {
      withFormatter { v: Int ->
        abs(v.toDouble())
          .toString()
      }
    }

    override fun createBoard(player: Player, hitResult: BlockHitResult): ValueSettingsBoard {
      val rows = ImmutableList.of<Component>(Components.literal("\u27f2")
        .withStyle(ChatFormatting.BOLD))
      val formatter = ValueSettingsFormatter { settings: ValueSettings -> this.formatSettings(settings) }
      return ValueSettingsBoard(label, MAX_DISTANCE, 8, rows, formatter)
    }

    override fun setValueSettings(player: Player, valueSetting: ValueSettings, ctrlHeld: Boolean) {
      val value = max(1.0,
        valueSetting.value()
          .toDouble()).toInt()
      if (valueSetting != valueSettings) playFeedbackSound(this)
      setValue(value)
    }

    override fun getValueSettings(): ValueSettings {
      return ValueSettings(if (value < 0) 0 else 1,
        abs(value.toDouble())
          .toInt())
    }

    fun formatSettings(settings: ValueSettings): MutableComponent {
      return Lang.number(max(1.0,
        abs(settings.value()
          .toDouble())))
        .add(Lang.text("\u27f2")
          .style(ChatFormatting.BOLD))
        .component()
    }
  }

  enum class DetectionMode(private val icon: AllIcons) : INamedIconOptions, StringRepresentable {
    WORLD(AllIcons.I_CART_ROTATE),
    SHIP(AllIcons.I_CART_ROTATE_PAUSED),
    WORLD_AND_SHIP(AllIcons.I_CART_ROTATE_LOCKED),
    ;

    private val translationKey = MODE_SCROLL_TITLE + Lang.asId(name)
    override fun getIcon(): AllIcons {
      return icon
    }

    override fun getTranslationKey(): String {
      return translationKey
    }

    override fun getSerializedName(): String {
      return Lang.asId(name)
    }
  }
}