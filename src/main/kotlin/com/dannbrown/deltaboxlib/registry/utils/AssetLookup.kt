package com.dannbrown.deltaboxlib.registry.utils

import com.tterrag.registrate.providers.DataGenContext
import com.tterrag.registrate.providers.RegistrateBlockstateProvider
import net.minecraftforge.client.model.generators.ModelFile

object AssetLookup {
  /**
   * Custom block models packaged with other partials. Example:
   * models/block/schematicannon/block.json <br></br>
   * <br></br>
   * Adding "powered", "vertical" will look for /block_powered_vertical.json
   */
  fun partialBaseModel(ctx: DataGenContext<*, *>, prov: RegistrateBlockstateProvider, vararg suffix: String): ModelFile {
    var string = "/block"
    for (suf in suffix) string += "_$suf"
    val location = "block/" + ctx.name + string
    return prov.models()
      .getExistingFile(prov.modLoc(location))
  }

  /**
   * Custom block model from models/block/x.json
   */
  fun standardModel(ctx: DataGenContext<*, *>, prov: RegistrateBlockstateProvider): ModelFile {
    return prov.models().getExistingFile(prov.modLoc("block/" + ctx.name))
  }
}