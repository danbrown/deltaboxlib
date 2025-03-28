package com.dannbrown.deltaboxlib.registrate.registry

import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.builders.LayerDefinition
import java.util.function.Supplier

class ModelLayerRegistry(val modId: String) {
  private val MODEL_LAYERS: MutableMap<String, Pair<Supplier<LayerDefinition>, Supplier<ModelLayerLocation>>> =
    mutableMapOf()

  fun add(path: String, model: Supplier<LayerDefinition>, folder: String = "main"): Supplier<ModelLayerLocation> {
    val modelLayer = Supplier {
      ModelLayerLocation(
        DeltaboxUtil.resourceLocation(
          modId,
          path
        ), folder
      )
    }
    MODEL_LAYERS[path] = Pair(model, modelLayer)
    return modelLayer
  }

  fun getModelLayers(): MutableMap<String, Pair<Supplier<LayerDefinition>, Supplier<ModelLayerLocation>>> {
    return MODEL_LAYERS
  }
}