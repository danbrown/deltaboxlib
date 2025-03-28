package com.dannbrown.deltaboxlib.init.test

import com.dannbrown.deltaboxlib.content.item.arrow.BaseArrowRenderer
import com.dannbrown.deltaboxlib.init.DeltaboxLibMod
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.projectile.AbstractArrow
import java.util.function.Supplier
import com.dannbrown.deltaboxlib.init.DeltaboxLibMod.REGISTRATE
import java.util.function.Function


object DeltaboxEntityTypes {
//  val EXPLOSIVE_ARROW = createArrow<ExplosiveArrow>("explosive_arrow", { e, l -> ExplosiveArrow(e, l) })
//
//  private fun <T : AbstractArrow> createArrow(
//    name: String,
//    arrowFun: EntityType.EntityFactory<T>
//  ): Supplier<EntityType<T>> {
//    return REGISTRATE.entityType<T>(name)
//      .factory(arrowFun)
//      .category(MobCategory.MISC)
//      .properties { p ->
//        p
//          .sized(0.5f, 0.5f)
//          .clientTrackingRange(4)
//          .updateInterval(20)
//
//      }
//      .renderer { Function { ctx -> BaseArrowRenderer(ctx, DeltaboxLibMod.MOD_ID, name) } }
//      .register()
//  }

  fun register() {
    // init
  }
}