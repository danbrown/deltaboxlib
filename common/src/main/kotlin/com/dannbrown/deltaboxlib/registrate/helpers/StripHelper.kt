package com.dannbrown.deltaboxlib.registrate.helpers

import net.minecraft.world.item.AxeItem
import net.minecraft.world.level.block.Block

object StripHelper {
  fun registerStrippable(log: Block, stripped: Block) {
    AxeItem.STRIPPABLES = HashMap(AxeItem.STRIPPABLES)
    AxeItem.STRIPPABLES[log] = stripped
  }
}