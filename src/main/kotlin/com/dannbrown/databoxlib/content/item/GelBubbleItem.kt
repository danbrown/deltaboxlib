package com.dannbrown.databoxlib.content.item

import com.dannbrown.databoxlib.content.entity.LavaGelBubbleProjectileEntity
import com.dannbrown.databoxlib.content.entity.WaterGelBubbleProjectileEntity
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import java.util.function.Supplier


class GelBubbleItem(private val fluid: Supplier<Block>, pProperties: Properties) : Item(pProperties) {
  override fun use(pLevel: Level, pPlayer: Player, pHand: InteractionHand): InteractionResultHolder<ItemStack> {
    val itemStack: ItemStack = pPlayer.getItemInHand(pHand)
    pLevel.playSound(pPlayer, pPlayer.x, pPlayer.y, pPlayer.z,
      SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5f, 0.4f / (pLevel.getRandom().nextFloat() * 0.4f + 0.8f)
    )
    if (!pLevel.isClientSide) {
//      val bubble = WaterGelBubbleProjectileEntity(pLevel, pPlayer)
      val bubble = if (fluid.get() == Blocks.WATER) WaterGelBubbleProjectileEntity(pLevel, pPlayer)
        else LavaGelBubbleProjectileEntity(pLevel, pPlayer)

      bubble.item = itemStack
      bubble.shootFromRotation(pPlayer, pPlayer.xRot, pPlayer.yRot, 0.0f, 1.5f, 1.0f)
      pLevel.addFreshEntity(bubble)
    }
    pPlayer.awardStat(Stats.ITEM_USED.get(this))
    if (!pPlayer.abilities.instabuild) {
      itemStack.shrink(1)
    }
    return InteractionResultHolder.sidedSuccess(itemStack, pLevel.isClientSide())
  }
}