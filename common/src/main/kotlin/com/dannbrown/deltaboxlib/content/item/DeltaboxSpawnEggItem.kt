package com.dannbrown.deltaboxlib.content.item

import net.minecraft.core.Direction
import net.minecraft.core.dispenser.DispenseItemBehavior
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SpawnEggItem
import net.minecraft.world.level.block.DispenserBlock
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.level.gameevent.GameEvent.Context
import java.util.*
import java.util.function.Supplier

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DeltaboxSpawnEggItem(
  val typeSupplier: Supplier<out EntityType<out Mob>>,
  backgroundColor: Int,
  highlightColor: Int,
  props: Properties
) : SpawnEggItem(null, backgroundColor, highlightColor, props) {

  companion object {
    val MOD_EGGS: MutableList<DeltaboxSpawnEggItem> = ArrayList()
    val TYPE_MAP: MutableMap<EntityType<out Mob>, DeltaboxSpawnEggItem> = IdentityHashMap()

    private val DEFAULT_DISPENSE_BEHAVIOR = DispenseItemBehavior { source, stack ->
      val face = source.blockState.getValue(DispenserBlock.FACING)
      val type = (stack.item as SpawnEggItem).getType(stack.tag)

      return@DispenseItemBehavior try {
        type.spawn(
          source.level, stack, null, source.pos.relative(face),
          MobSpawnType.DISPENSER, face != Direction.UP, false
        )
        stack.shrink(1)
        source.level.gameEvent(GameEvent.ENTITY_PLACE, source.pos, Context.of(source.blockState))
        stack
      } catch (exception: Exception) {
        DispenseItemBehavior.LOGGER.error(
          "Error while dispensing spawn egg from dispenser at {}",
          source.pos,
          exception
        )
        ItemStack.EMPTY
      }
    }
  }

  init {
    MOD_EGGS.add(this)
  }

  override fun getType(tag: CompoundTag?): EntityType<*> {
    return super.getType(tag) ?: typeSupplier.get()
  }

  fun createDispenseBehavior(): DispenseItemBehavior {
    return DEFAULT_DISPENSE_BEHAVIOR
  }

  fun getDefaultType(): EntityType<*> {
    return typeSupplier.get()
  }
}
