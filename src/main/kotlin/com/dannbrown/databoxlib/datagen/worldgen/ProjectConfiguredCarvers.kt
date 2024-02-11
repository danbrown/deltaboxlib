package com.dannbrown.databoxlib.datagen.worldgen



import com.dannbrown.databoxlib.init.ProjectCarvers
import com.dannbrown.databoxlib.init.ProjectTags
import com.dannbrown.databoxlib.lib.LibUtils
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.util.valueproviders.ConstantFloat
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight

object ProjectConfiguredCarvers {
  val SAMPLE_CAVE = ResourceKey.create(
    Registries.CONFIGURED_CARVER,
    LibUtils.resourceLocation("sample_cave")
  )

  fun bootstrap(context: BootstapContext<ConfiguredWorldCarver<*>>) {
    val blocks = context.lookup(Registries.BLOCK)
    context.register(
      SAMPLE_CAVE,
      ProjectCarvers.SAMPLE_CAVE.get().configured(
        CaveCarverConfiguration(
          0.5f,
          UniformHeight.of(VerticalAnchor.aboveBottom(5), VerticalAnchor.belowTop(1)),
          ConstantFloat.of(0.5f),
          VerticalAnchor.aboveBottom(10),
          BuiltInRegistries.BLOCK.getOrCreateTag(ProjectTags.BLOCK.DATABOX_CARVER_REPLACEABLES),
          ConstantFloat.of(1.0f),
          ConstantFloat.of(1.0f),
          ConstantFloat.of(-0.7f)
        )
      )
    )
    return
  }
}