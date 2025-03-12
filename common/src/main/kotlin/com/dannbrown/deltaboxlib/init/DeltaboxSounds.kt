package com.dannbrown.deltaboxlib.init

import com.dannbrown.deltaboxlib.init.DeltaboxLibMod.REGISTRATE
import com.dannbrown.deltaboxlib.registrate.util.SoundTypeSupplier
import net.minecraft.world.level.block.SoundType

object DeltaboxSounds {
  // Resin
  val RESIN_BRICK_BREAK = REGISTRATE.soundEvent("resin_bricks_break")
  val RESIN_BRICK_STEP = REGISTRATE.soundEvent("resin_bricks_step")
  val RESIN_BRICK_FALL = REGISTRATE.soundEvent("resin_bricks_fall")
  val RESIN_BRICK_PLACE = REGISTRATE.soundEvent("resin_bricks_place")
  val RESIN_BRICK_HIT = REGISTRATE.soundEvent("resin_bricks_hit")

  val BLOCK_OF_RESIN_BREAK = REGISTRATE.soundEvent("block_of_resin_break")
  val BLOCK_OF_RESIN_STEP = REGISTRATE.soundEvent("block_of_resin_step")
  val BLOCK_OF_RESIN_FALL = REGISTRATE.soundEvent("block_of_resin_fall")
  val BLOCK_OF_RESIN_PLACE = REGISTRATE.soundEvent("block_of_resin_place")
  val BLOCK_OF_RESIN_HIT = REGISTRATE.soundEvent("block_of_resin_hit")

  val RESIN_BRICK_SOUNDS = SoundTypeSupplier(
    1f,
    1f,
    RESIN_BRICK_BREAK,
    RESIN_BRICK_STEP,
    RESIN_BRICK_PLACE,
    RESIN_BRICK_HIT,
    RESIN_BRICK_FALL
  )
  val BLOCK_OF_RESIN_SOUNDS = SoundTypeSupplier(
    1f,
    1f,
    BLOCK_OF_RESIN_BREAK,
    BLOCK_OF_RESIN_STEP,
    BLOCK_OF_RESIN_PLACE,
    BLOCK_OF_RESIN_HIT,
    BLOCK_OF_RESIN_FALL
  )

  // Creaking
  val CREAKING_AMBIENT = REGISTRATE.soundEvent("creaking_ambient")
  val CREAKING_ACTIVATE = REGISTRATE.soundEvent("creaking_activate")
  val CREAKING_DEACTIVATE = REGISTRATE.soundEvent("creaking_deactivate")
  val CREAKING_ATTACK = REGISTRATE.soundEvent("creaking_attack")
  val CREAKING_DEATH = REGISTRATE.soundEvent("creaking_death")
  val CREAKING_STEP = REGISTRATE.soundEvent("creaking_step")
  val CREAKING_FREEZE = REGISTRATE.soundEvent("creaking_freeze")
  val CREAKING_UNFREEZE = REGISTRATE.soundEvent("creaking_unfreeze")
  val CREAKING_SPAWN = REGISTRATE.soundEvent("creaking_spawn")
  val CREAKING_HIT = REGISTRATE.soundEvent("creaking_hit")
  val CREAKING_TWITCH = REGISTRATE.soundEvent("creaking_twitch")

  // Creaking Heart
  val CREAKING_HEART_BREAK = REGISTRATE.soundEvent("creaking_heart_break")
  val CREAKING_HEART_FALL = REGISTRATE.soundEvent("creaking_heart_fall")
  val CREAKING_HEART_HIT = REGISTRATE.soundEvent("creaking_heart_hit")
  val CREAKING_HEART_HURT = REGISTRATE.soundEvent("creaking_heart_hurt")
  val CREAKING_HEART_PLACE = REGISTRATE.soundEvent("creaking_heart_place")
  val CREAKING_HEART_STEP = REGISTRATE.soundEvent("creaking_heart_step")
  val CREAKING_HEART_IDLE = REGISTRATE.soundEvent("creaking_heart_idle")

  val CREAKING_HEART_SOUNDS = SoundTypeSupplier(
    1f,
    1f,
    CREAKING_HEART_BREAK,
    CREAKING_HEART_STEP,
    CREAKING_HEART_PLACE,
    CREAKING_HEART_HIT,
    CREAKING_HEART_FALL
  )


  // Eyeblossom
  val EYEBLOSSOM_IDLE = REGISTRATE.soundEvent("eyeblossom_idle")
  val EYEBLOSSOM_OPEN = REGISTRATE.soundEvent("eyeblossom_open")
  val EYEBLOSSOM_CLOSE = REGISTRATE.soundEvent("eyeblossom_close")
  val EYEBLOSSOM_OPEN_LONG = REGISTRATE.soundEvent("eyeblossom_open_long")
  val EYEBLOSSOM_CLOSE_LONG = REGISTRATE.soundEvent("eyeblossom_close_long")

  fun register() {
    // init
  }
}