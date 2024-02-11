package com.dannbrown.databoxlib.datagen.recipes

import com.dannbrown.databoxlib.datagen.content.BlockFamily
import com.dannbrown.databoxlib.init.ProjectBlocks
import com.tterrag.registrate.util.entry.BlockEntry
import net.minecraft.data.DataGenerator
import net.minecraft.world.level.block.Block

class WoodRecipeGen(generator: DataGenerator) : ProjectRecipeGen(generator) {
  override val recipeName = "Woods"

  val JOSHUA_WOOD_RECIPES = woodRecipesCompat(ProjectBlocks.JOSHUA_FAMILY)

  fun woodRecipesCompat(blockFamily: BlockFamily) {
    sawingWood(blockFamily.LOG!!, blockFamily.STRIPPED_LOG!!)
    sawingWood(blockFamily.WOOD!!, blockFamily.STRIPPED_WOOD!!)
    sawingWood(blockFamily.STALK!!, blockFamily.STRIPPED_STALK!!)

    sawingToPlanks(blockFamily.LOG!!, blockFamily.MAIN!!)
    sawingToPlanks(blockFamily.STRIPPED_LOG!!, blockFamily.MAIN!!)
    sawingToPlanks(blockFamily.WOOD!!, blockFamily.MAIN!!)
    sawingToPlanks(blockFamily.STRIPPED_WOOD!!, blockFamily.MAIN!!)
    sawingToPlanks(blockFamily.STALK!!, blockFamily.MAIN!!, 2)
    sawingToPlanks(blockFamily.STRIPPED_STALK!!, blockFamily.MAIN!!, 2)
  }


  private fun sawingWood(block: BlockEntry<out Block>, result: BlockEntry<out Block>) {
    if (block == null || result == null) return
    create(block.get().descriptionId + "_to_stripped") { b ->
      b.cutting { c ->
        c.require(block.get())
        c.output(result.get())
      }
    }
  }

  private fun sawingToPlanks(block: BlockEntry<out Block>, result: BlockEntry<out Block>, amount: Int = 6) {
    if (block == null || result == null) return
    create(block.get().descriptionId + "_to_planks") { b ->
      b.cutting { c ->
        c.require(block.get())
        c.output(result.get(), amount)
      }
    }
  }

//  private fun woodToCoal(block: BlockEntry<out Block>){
//    if (block == null) return
//    cooking({ DataIngredient.items(block.get()) }, { Items.CHARCOAL }) { b -> b
//      .prefix(block.get().descriptionId + "_to_")
//      .inFurnace(200, 0.15f) }
//  }
}