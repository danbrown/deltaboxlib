package com.dannbrown.deltaboxlib.registrate.builders

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.registry.BlockEntry
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import org.apache.commons.lang3.function.TriFunction
import java.util.function.Supplier
import java.util.function.Function

class BlockEntityBuilder<T : BlockEntity>(
  private val registrate: AbstractDeltaboxRegistrate,
  private val entityId: String
) {

  protected var blockEntityFactory: TriFunction<Supplier<BlockEntityType<T>>, BlockPos, BlockState, T>? = null
  protected var validBlocks: Array<out BlockEntry<*>> = arrayOf()
  protected var entityName: String = entityId

  var blockEntityRenderer: Function<BlockEntityRendererProvider.Context, BlockEntityRenderer<out BlockEntity>>? =
    null


  var entityInstance: Supplier<BlockEntityType<T>>? = null

  fun factory(_factory: TriFunction<Supplier<BlockEntityType<T>>, BlockPos, BlockState, T>): BlockEntityBuilder<T> {
    blockEntityFactory = _factory
    return this
  }

  fun validBlocks(vararg blocks: BlockEntry<*>): BlockEntityBuilder<T> {
    validBlocks = blocks
    return this
  }

  fun renderer(_blockEntityRenderer: Function<BlockEntityRendererProvider.Context, BlockEntityRenderer<out BlockEntity>>): BlockEntityBuilder<T> {
    blockEntityRenderer = _blockEntityRenderer
    return this
  }

  fun lang(langKey: String): BlockEntityBuilder<T> {
    this.entityName = langKey
    return this
  }

  // @ Get Functions
  fun getBlockEntity(): Supplier<BlockEntityType<T>> {
    return entityInstance!!
  }

  fun getName(): String {
    return entityName
  }

  // @ Register Functions
  fun register(): Supplier<BlockEntityType<T>> {
    if (validBlocks.isEmpty()) throw IllegalArgumentException("Block Entity must have at least one valid block.")
    if (blockEntityFactory == null) throw IllegalArgumentException("Block Entity factory is not set.")
    entityInstance = registrate.blockEntityRegistry.register(entityId, {
      BlockEntityType.Builder.of(
        { pos, state -> blockEntityFactory!!.apply({ entityInstance!!.get() }, pos, state) },
        *validBlocks.map { it.get() }.toTypedArray()
      ).build(null)
    }, this)
    return entityInstance!!
  }

  fun getRenderer(ctx: BlockEntityRendererProvider.Context): BlockEntityRenderer<in BlockEntity> {
    return this.blockEntityRenderer!!.apply(ctx) as BlockEntityRenderer<in BlockEntity>
  }
}
