package com.dannbrown.databoxlib.datagen.recipes

import net.minecraft.data.DataGenerator
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.material.Fluids

class CoolingRecipeGen(generator: DataGenerator): ProjectRecipeGen(generator) {
  override val recipeName = "Cooling"

  val ICE = databoxlib("ice"){ b -> b
    .cooling { b -> b
      .require(Fluids.WATER, 1000)
      .output(Blocks.ICE, 1)
    }
  }

  val PACKED_ICE = databoxlib("packed_ice"){ b -> b
    .cooling { b -> b
      .require(Fluids.WATER, 1000)
      .require(Blocks.ICE)
      .require(Blocks.ICE)
      .require(Blocks.ICE)
      .require(Blocks.ICE)
      .output(Blocks.PACKED_ICE, 1)
    }
  }

  val BLUE_ICE = databoxlib("blue_ice"){ b -> b
    .cooling { b -> b
      .require(Fluids.WATER, 1000)
      .require(Blocks.PACKED_ICE)
      .require(Blocks.PACKED_ICE)
      .require(Blocks.PACKED_ICE)
      .require(Blocks.PACKED_ICE)
      .output(Blocks.BLUE_ICE, 1)
    }
  }

  val SNOW_BALL = databoxlib("snow_ball"){ b -> b
    .cooling { b -> b
      .require(Fluids.WATER, 100)
      .output(Items.SNOWBALL, 1)
    }
  }
}