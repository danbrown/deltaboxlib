package com.dannbrown.deltaboxlib.registrate.types

import com.dannbrown.deltaboxlib.registrate.util.AdvancementUtil
import net.minecraft.advancements.Advancement

typealias AdvancementSupplier = (String, AdvancementUtil, Advancement.Builder) -> Advancement