package com.dannbrown.databoxlib.mixin.render;

import com.dannbrown.databoxlib.datagen.planets.ProjectPlanets;
import com.dannbrown.databoxlib.datagen.planets.PlanetCodec;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.Set;

@Mixin(DimensionSpecialEffects.class)
public abstract class DimensionSpecialEffectsMixin {

  @Inject(method = "forType", at = @At("HEAD"), cancellable = true)
  private static void databoxlib$forType(DimensionType type, CallbackInfoReturnable<DimensionSpecialEffects> cir) {
    Set<PlanetCodec> planets = ProjectPlanets.PlanetManager.Companion.getPlanets();
    for (PlanetCodec planet : planets) {
      ResourceLocation location = planet.getDimension().location();
      ResourceLocation typeLocation = type.effectsLocation();
      if (location.equals(typeLocation)) {
        // check if the planet has a custom dimension effect
        Optional<PlanetCodec.SkyBoxProperties> skyBoxProperties = planet.getSkybox();
        if (skyBoxProperties.isPresent()) {
          PlanetCodec.SkyBoxProperties properties = skyBoxProperties.get();
          cir.setReturnValue(new ProjectPlanets.PlanetDimensionEffects(properties));
          return;
        }
        return;
      }
    }
  }
}
