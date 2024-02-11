package com.dannbrown.databoxlib.init

import com.dannbrown.databoxlib.ProjectContent
import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.KeyMapping
import net.minecraftforge.client.settings.KeyConflictContext
import org.lwjgl.glfw.GLFW

class ProjectKeyBinds {
  companion object {
    val KEY_CATEGORY = "key.category." + ProjectContent.MOD_ID
    val KEY_SPACESHIP_ASSEMBLE = "key.${ProjectContent.MOD_ID}.spaceship_assemble"

    // Keys
    val SPACESHIP_ASSEMBLE: KeyMapping = KeyMapping(KEY_SPACESHIP_ASSEMBLE, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_J, KEY_CATEGORY)
  }
}