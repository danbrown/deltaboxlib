package com.dannbrown.databoxlib.mixin.block;


import com.dannbrown.databoxlib.init.ProjectBlocks;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.transmission.ClutchBlock;
import com.simibubi.create.content.kinetics.transmission.GearshiftBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClutchBlock.class)
public abstract class ClutchBlockMixin extends GearshiftBlock implements IWrenchable {

  public ClutchBlockMixin(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult onWrenched(BlockState state, UseOnContext context) {
    InteractionResult wrenchResult = super.onWrenched(state, context);
    Direction.Axis directionAxis = state.getValue(AXIS);

    if (context.getClickedFace().getAxis() == directionAxis) {
      Level world = context.getLevel();
      BlockPos pos = context.getClickedPos();
      // put an inverted clutch block with the same state as this one
      BlockState invertedClutchBlock = ProjectBlocks.INSTANCE.getINVERTED_CLUTCH().getDefaultState()
              .setValue(AXIS, directionAxis)
              .setValue(POWERED, state.getValue(POWERED));
      world.setBlock(pos, invertedClutchBlock, 2 | 16);
      return InteractionResult.SUCCESS;
    }
    return wrenchResult;

  }
}
