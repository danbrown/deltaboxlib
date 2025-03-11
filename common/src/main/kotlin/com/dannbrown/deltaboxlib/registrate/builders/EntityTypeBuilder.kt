package com.dannbrown.deltaboxlib.registrate.builders

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EntityType.EntityFactory
import net.minecraft.world.entity.MobCategory
import java.util.function.Function
import java.util.function.Supplier
import javax.management.BadAttributeValueExpException

class EntityTypeBuilder<T : Entity>(registrate: AbstractDeltaboxRegistrate, val entityId: String) :
  AbstractBuilder(registrate) {

  protected var mobCategory = MobCategory.MISC
  protected var entityName: String = DeltaboxUtil.asName(entityId)

  protected var propertiesFactory: Function<EntityType.Builder<T>, EntityType.Builder<T>>? = null
  protected var entityFactory: EntityFactory<T>? = null

  protected var entityRenderer: Function<EntityRendererProvider.Context, EntityRenderer<out Entity>>? = null

  var entityInstance: Supplier<EntityType<T>>? = null

  fun factory(_entityFactory: EntityFactory<T>): EntityTypeBuilder<T> {
    entityFactory = _entityFactory
    return this
  }

  fun properties(_propertiesFactory: Function<EntityType.Builder<T>, EntityType.Builder<T>>): EntityTypeBuilder<T> {
    propertiesFactory = _propertiesFactory
    return this
  }

  fun category(_category: MobCategory): EntityTypeBuilder<T> {
    mobCategory = _category
    return this
  }

  fun renderer(_entityRenderer: Function<EntityRendererProvider.Context, EntityRenderer<out Entity>>): EntityTypeBuilder<T> {
    entityRenderer = _entityRenderer
    return this
  }

  fun modelLayer(path: String, model: Supplier<LayerDefinition>, folder: String = "main") {
    this.registrate.modelLayersRegistry.add(path, model, folder)
  }

  fun lang(langKey: String): EntityTypeBuilder<T> {
    this.entityName = langKey
    return this
  }

  // @ Get Functions
  fun getEntity(): Supplier<EntityType<T>> {
    return entityInstance!!
  }

  fun getName(): String {
    return entityName
  }

  // @ Register Functions
  fun register(): Supplier<EntityType<T>> {
    if (entityFactory == null || propertiesFactory == null) throw BadAttributeValueExpException("Can't create an entity with no builder")
    val builder = EntityType.Builder.of(entityFactory!!, mobCategory)
    entityInstance = this.registrate.entityTypeRegistry.register(
      entityId,
      { propertiesFactory!!.apply(builder).build("${registrate.modId}:${entityId}") },
      this
    )
    return entityInstance!!
  }

  fun getRenderer(ctx: EntityRendererProvider.Context): EntityRenderer<in Entity> {
    return this.entityRenderer!!.apply(ctx) as EntityRenderer<in Entity>
  }
}