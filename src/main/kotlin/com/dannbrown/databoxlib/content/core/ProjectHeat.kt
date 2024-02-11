package com.dannbrown.databoxlib.content.utils

import com.simibubi.create.content.processing.recipe.HeatCondition

object ProjectHeat {
  val NONE: HeatCondition = HeatCondition.NONE
  val PASSIVEHEATED: HeatCondition = HeatCondition.valueOf("PASSIVEHEATED")
  val HEATED: HeatCondition = HeatCondition.HEATED
  val SUPERHEATED: HeatCondition = HeatCondition.SUPERHEATED
  val HYPERHEATED: HeatCondition = HeatCondition.valueOf("HYPERHEATED")
}