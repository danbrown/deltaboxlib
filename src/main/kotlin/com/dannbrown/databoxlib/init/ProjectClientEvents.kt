package com.dannbrown.databoxlib.init

import com.dannbrown.databoxlib.content.ship.block.sail.SailBlockScreenRender
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.network.chat.Component
import net.minecraftforge.client.event.InputEvent
import net.minecraftforge.client.event.RegisterKeyMappingsEvent

object ProjectClientEvents {
  fun onKeyRegister(event: RegisterKeyMappingsEvent) {
    event.register(ProjectKeyBinds.SPACESHIP_ASSEMBLE)
  }

  fun onKeyInput(event: InputEvent.Key) {
    if (ProjectKeyBinds.SPACESHIP_ASSEMBLE.consumeClick()) {
      Minecraft.getInstance().player!!.sendSystemMessage(Component.literal("Assemble pressed!"))
    }
  }

  fun onRegisterMenuScreens() {
    MenuScreens.register(ProjectScreens.SAIL_BLOCK.get(), ::SailBlockScreenRender)
  }
}
