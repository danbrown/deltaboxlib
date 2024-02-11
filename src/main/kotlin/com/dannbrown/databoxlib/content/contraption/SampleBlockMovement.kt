package com.dannbrown.databoxlib.content.contraption

import com.dannbrown.databoxlib.ProjectContent
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour
import com.simibubi.create.content.contraptions.behaviour.MovementContext

class SampleBlockMovement : MovementBehaviour {
  override fun tick(context: MovementContext?) {
    ProjectContent.LOGGER.info("Should Update the Contraption Data of the SailBlockEntity here")
    super.tick(context)
  }

  override fun renderAsNormalBlockEntity(): Boolean {
    return true
  }
}