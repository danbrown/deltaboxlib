package com.dannbrown.deltaboxlib.registrate.types

import com.dannbrown.deltaboxlib.registrate.builders.BlockBuilderContext
import net.minecraft.world.level.block.state.BlockBehaviour
import java.util.function.BiFunction

typealias BlockPropertiesFactory = BiFunction<BlockBuilderContext<*>, BlockBehaviour.Properties, BlockBehaviour.Properties>