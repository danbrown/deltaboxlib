package com.dannbrown.deltaboxlib.content.block

import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.tags.BlockTags
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.DoublePlantBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import java.util.function.Supplier


class GenericTallGrassTopBlock(
  private val baseBlock: Supplier<out GenericTallGrassBlock>,
  p: Properties,
  private val placeOn: ((blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos) -> Boolean)? = null
) : DoublePlantBlock(p) {
  override fun getCloneItemStack(blockGetter: BlockGetter, blockPos: BlockPos, blockState: BlockState): ItemStack {
    return ItemStack(baseBlock.get())
  }

  override fun mayPlaceOn(blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos): Boolean {
    if (placeOn != null) {
      return placeOn.invoke(blockState, blockGetter, blockPos)
    }
    return super.mayPlaceOn(blockState, blockGetter, blockPos)
  }

  override fun use(
    blockState: BlockState?,
    level: Level,
    blockPos: BlockPos,
    player: Player,
    interactionHand: InteractionHand?,
    blockHitResult: BlockHitResult?
  ): InteractionResult {
    val random: RandomSource = level.random
    val blockPos2 = blockPos.below()
    val blockPos3 = blockPos.above()
    if (player.getItemInHand(interactionHand).item == Items.SHEARS && level.getBlockState(blockPos2)
        .`is`(BlockTags.DIRT) || level.getBlockState(blockPos2).`is`(BlockTags.SAND)
    ) {
      level.playSound(
        null,
        blockPos,
        SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES,
        SoundSource.BLOCKS,
        1.0f,
        0.8f + level.random.nextFloat() * 0.4f
      )
      val d = blockPos.x.toDouble() + random.nextDouble()
      val e = blockPos.y.toDouble() + 1.0
      val f = blockPos.z.toDouble() + random.nextDouble()
      val j: Int = 1 + level.random.nextInt(2)
      popResource(level, blockPos, ItemStack(baseBlock.get(), j))
      level.setBlockAndUpdate(blockPos, baseBlock.get().defaultBlockState())
      level.neighborChanged(blockPos, baseBlock.get(), blockPos)
      level.neighborChanged(blockPos, Blocks.AIR, blockPos3)
      level.addParticle(ParticleTypes.HAPPY_VILLAGER, d, e, f, 0.0, 0.0, 0.0)
      return InteractionResult.sidedSuccess(level.isClientSide)
    }
    return InteractionResult.PASS
  }

}

