package com.dannbrown.deltaboxlib.registrate.util

import net.minecraft.resources.ResourceLocation

object DeltaboxUtil {
  fun resourceLocation(namespace: String, path: String): ResourceLocation{
    return ResourceLocation(namespace, path)
  }

  fun resourceLocation(path: String): ResourceLocation {
    return ResourceLocation(path)
  }
}