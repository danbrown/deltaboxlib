package com.dannbrown.deltaboxlib.registrate.builders

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.types.NonNullConsumer
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.monster.Creeper
import net.minecraft.world.level.block.entity.BlockEntityType
import java.util.function.Supplier

class BlockEntityBuilder<T : BlockEntityType<*>>(registrate: AbstractDeltaboxRegistrate, val entityId: String) :
  AbstractBuilder(registrate) {


}