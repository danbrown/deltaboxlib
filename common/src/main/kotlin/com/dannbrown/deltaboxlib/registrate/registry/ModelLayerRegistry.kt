package com.dannbrown.deltaboxlib.registrate.registry

import net.minecraft.client.model.geom.builders.LayerDefinition
import java.util.function.Supplier

class ModelLayerRegistry(modId: String) {
  private val MODEL_LAYERS: MutableMap<String, Pair<Supplier<LayerDefinition>, String>> = mutableMapOf()
  fun add(path: String, model: Supplier<LayerDefinition>, folder: String = "main") {
    MODEL_LAYERS[path] = Pair(model, folder)
  }

  fun getModelLayers(): MutableMap<String, Pair<Supplier<LayerDefinition>, String>> {
    return MODEL_LAYERS
  }
}