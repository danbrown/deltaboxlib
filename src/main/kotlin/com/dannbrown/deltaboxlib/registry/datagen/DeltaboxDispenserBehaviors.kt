package com.dannbrown.deltaboxlib.registry.datagen

import net.minecraft.Util
import net.minecraft.core.BlockPos
import net.minecraft.core.BlockSource
import net.minecraft.core.Position
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.ThrowableItemProjectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.DispenserBlock
import org.apache.logging.log4j.LogManager
import java.util.function.Supplier
import kotlin.reflect.KClass

abstract class DeltaboxDispenserBehaviors {
  companion object{
    val LOGGER = LogManager.getLogger()
  }
  fun <T : ThrowableItemProjectile> registerThrowableBehavior(item: Supplier<ItemLike>, projectileClass: KClass<T>) {
    DispenserBlock.registerBehavior(item.get(), object : AbstractProjectileDispenseBehavior() {
      override fun getProjectile(
        level: Level,
        position: Position,
        itemStack: ItemStack
      ): Projectile {
        try {
          // Create an instance of the specified class using reflection
          val projectileInstance = projectileClass.java.getDeclaredConstructor(
            Level::class.java,
            Double::class.java,
            Double::class.java,
            Double::class.java
          )
            .newInstance(level, position.x(), position.y(), position.z())
          // Set the itemStack property
          Util.make(projectileInstance) { entity -> entity.item = itemStack }

          return projectileInstance
        } catch (e: Exception) {
          // Handle any exceptions that may occur during reflection or instantiation
          e.printStackTrace()
          throw RuntimeException("Failed to create instance of $projectileClass", e)
        }
      }
    })
  }

  fun <T : Block> registerBlockPlaceRemoveBehavior(block: Supplier<Block>, blockClass: KClass<T>) {
    DispenserBlock.registerBehavior(block.get(), object : DefaultDispenseItemBehavior() {
      private val defaultDispenseItemBehavior = DefaultDispenseItemBehavior()
      override fun execute(blockSource: BlockSource, itemStack: ItemStack): ItemStack {
        val levelaccessor: LevelAccessor = blockSource.level
        val blockPosInFront: BlockPos = blockSource.pos.relative(blockSource.blockState.getValue(DispenserBlock.FACING))
        val blockstate = levelaccessor.getBlockState(blockPosInFront)
        val blockInFront = blockstate.block
        // block in front of dispenser is the same as the block we want to place, so we remove it
        if (blockInFront.javaClass == blockClass.java) {
          // remove the block
          levelaccessor.destroyBlock(blockPosInFront, false)
          // if item stack is at the max size, we return a new item stack
          if (itemStack.count >= itemStack.maxStackSize) {
            this.defaultDispenseItemBehavior.dispense(blockSource, ItemStack(itemStack.item))
          }
          // otherwise we add 1 to the item stack
          else {
            itemStack.grow(1) // add 1 to the item stack
          }
//          if (itemStack.isEmpty) {
//            return ItemStack(itemStack.item)
//          }
//          else if (blockSource.getEntity<DispenserBlockEntity>()
//              .addItem(ItemStack(itemStack.item)) < 0) {
//            this.defaultDispenseItemBehavior.dispense(blockSource, ItemStack(itemStack.item))
//          }
          return itemStack
        }
        else if (blockstate.isAir) {
          // block in front of dispenser is air, so we place the block
          val putBlockstate = block.get()
            .defaultBlockState()
          levelaccessor.setBlock(blockPosInFront, putBlockstate, 3)
          itemStack.shrink(1) // remove 1 from the item stack
          return itemStack
        }

        return super.execute(blockSource, itemStack)
      }
    })
  }

  fun registerAll() {
    LOGGER.info("Registering all dispenser behaviors for DeltaboxLib")
  }
}