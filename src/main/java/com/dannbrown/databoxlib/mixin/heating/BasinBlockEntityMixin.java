package com.dannbrown.databoxlib.mixin.heating;

import com.dannbrown.databoxlib.content.block.HyperHeaterBlock;
import com.dannbrown.databoxlib.content.block.PassiveHeaterBlock;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = BasinBlockEntity.class, remap = false)
public class BasinBlockEntityMixin {

  /**
   * @author DannBrown & ZehMaria
   * @reason Adds HYPER and PASSIVE to the basin recipes, thanks to ZehMaria for
   *         how to implement new heat levels!
   */
  @Overwrite
  public static BlazeBurnerBlock.HeatLevel getHeatLevelOf(BlockState state) {

    // hyper heating
    if (state.hasProperty(HyperHeaterBlock.HEAT_LEVEL))
      return state.getValue(HyperHeaterBlock.HEAT_LEVEL);

    // passive heating
    if(state.getBlock() instanceof FireBlock)
      return BlazeBurnerBlock.HeatLevel.valueOf("PASSIVE");

    // blaze burner default levels
    if (state.hasProperty(BlazeBurnerBlock.HEAT_LEVEL))
      return state.getValue(BlazeBurnerBlock.HEAT_LEVEL);
    return AllTags.AllBlockTags.PASSIVE_BOILER_HEATERS.matches(state) ? BlazeBurnerBlock.HeatLevel.SMOULDERING
        : BlazeBurnerBlock.HeatLevel.NONE;
  }
}
