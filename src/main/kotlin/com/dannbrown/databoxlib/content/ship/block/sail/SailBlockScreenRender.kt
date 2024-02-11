package com.dannbrown.databoxlib.content.ship.block.sail

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.content.ship.extensions.getShipManagingPos
import com.dannbrown.databoxlib.init.ProjectConfig
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.phys.BlockHitResult

class SailBlockScreenRender(handler: SailBlockScreenMenu, playerInventory: Inventory, title: Component) :
  AbstractContainerScreen<SailBlockScreenMenu>(handler, playerInventory, title) {
  private lateinit var assembleButton: SailBlockButtonComponent
  private lateinit var assembleDroneButton: SailBlockButtonComponent
  private lateinit var alignButton: SailBlockButtonComponent
  private lateinit var disassembleButton: SailBlockButtonComponent
  private val pos = (Minecraft.getInstance().hitResult as? BlockHitResult)?.blockPos

  init {
    titleLabelX = 120
  }

  override fun init() {
    super.init()
    val x = (width - imageWidth) / 2
    val y = (height - imageHeight) / 2

    assembleButton = addRenderableWidget(
      SailBlockButtonComponent(x + BUTTON_1_X, y + BUTTON_1_Y, Component.translatable(ASSEMBLE_TEXT), font) {
        minecraft?.gameMode?.handleInventoryButtonClick(menu.containerId, 0)
      }
    )

    alignButton = addRenderableWidget(
      SailBlockButtonComponent(x + BUTTON_2_X, y + BUTTON_2_Y, Component.translatable(ALIGN_TEXT), font) {
        minecraft?.gameMode?.handleInventoryButtonClick(menu.containerId, 1)
      }
    )

    disassembleButton = addRenderableWidget(
      SailBlockButtonComponent(x + BUTTON_3_X, y + BUTTON_3_Y, Component.translatable(DISSEMBLE_TEXT), font) {
        minecraft?.gameMode?.handleInventoryButtonClick(menu.containerId, 3)
      }
    )

    assembleDroneButton = addRenderableWidget(
      SailBlockButtonComponent(x + BUTTON_1_X + BUTTON_1_X, y + BUTTON_1_Y + BUTTON_1_Y, Component.translatable(ASSEMBLE_DRONE_TEXT), font) {
        minecraft?.gameMode?.handleInventoryButtonClick(menu.containerId, 4)
      }
    )

    disassembleButton.active = ProjectConfig.SERVER.allowDisassembly
    updateButtons()
  }

  private fun updateButtons() {
    val level = Minecraft.getInstance().level ?: return
    val isLookingAtShip = level.getShipManagingPos(pos ?: return) != null
    assembleButton.active = !isLookingAtShip
    assembleDroneButton.active = !isLookingAtShip
    alignButton.active = isLookingAtShip
    disassembleButton.active = ProjectConfig.SERVER.allowDisassembly && isLookingAtShip
  }

  override fun renderBg(guiGraphics: GuiGraphics, partialTicks: Float, mouseX: Int, mouseY: Int) {
    updateButtons()

    RenderSystem.setShader { GameRenderer.getPositionTexShader() }
    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
    RenderSystem.setShaderTexture(0, TEXTURE)
    val x = (width - imageWidth) / 2
    val y = (height - imageHeight) / 2
    guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight)
  }

  override fun renderLabels(guiGraphics: GuiGraphics, i: Int, j: Int) {
    guiGraphics.drawString(font, title, titleLabelX, titleLabelY, 0x404040, false)
    val blockEntity = this.menu.blockEntity

    if (blockEntity != null && blockEntity.aligning) {
      alignButton.message = Component.translatable(ALIGNING_TEXT)
      alignButton.active = false
    }
    else {
      alignButton.message = Component.translatable(ALIGN_TEXT)
      alignButton.active = true
    }
    // TODO render stats
  }

  // mojank doesn't check mouse release for their widgets for some reason
  override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
    isDragging = false
    if (getChildAt(mouseX, mouseY).filter { it.mouseReleased(mouseX, mouseY, button) }.isPresent) return true

    return super.mouseReleased(mouseX, mouseY, button)
  }

  companion object { // TEXTURE DATA
    internal val TEXTURE = ResourceLocation(ProjectContent.MOD_ID, "textures/gui/assembler.png")
    private const val BUTTON_1_X = 10
    private const val BUTTON_1_Y = 73
    private const val BUTTON_2_X = 10
    private const val BUTTON_2_Y = 103
    private const val BUTTON_3_X = 10
    private const val BUTTON_3_Y = 133
    val ASSEMBLE_TEXT = "gui.${ProjectContent.MOD_ID}.assemble"
    val ASSEMBLE_DRONE_TEXT = "gui.${ProjectContent.MOD_ID}.assemble_drone"
    val DISSEMBLE_TEXT = "gui.${ProjectContent.MOD_ID}.disassemble"
    val ALIGN_TEXT = "gui.${ProjectContent.MOD_ID}.align"
    val ALIGNING_TEXT = "gui.${ProjectContent.MOD_ID}.aligning"
    val TODO_TEXT = "gui.${ProjectContent.MOD_ID}.todo"
  }
}
