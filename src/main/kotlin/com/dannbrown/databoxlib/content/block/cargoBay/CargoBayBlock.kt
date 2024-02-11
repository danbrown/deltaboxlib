package com.dannbrown.databoxlib.content.block.cargoBay
//
//import com.dannbrown.databoxlib.init.DataboxBlockEntities
//import com.dannbrown.databoxlib.init.DataboxBlocks
//import javax.annotation.Nullable;
//
//import com.simibubi.create.api.connectivity.ConnectivityHandler;
//import com.simibubi.create.foundation.block.IBE
//import com.simibubi.create.foundation.item.ItemHelper
//import net.minecraft.core.BlockPos
//import net.minecraft.core.Direction
//import net.minecraft.core.Direction.Axis
//import net.minecraft.core.Direction.AxisDirection
//import net.minecraft.sounds.SoundEvents
//import net.minecraft.world.InteractionResult
//import net.minecraft.world.item.Item.Properties
//import net.minecraft.world.item.context.BlockPlaceContext
//import net.minecraft.world.item.context.UseOnContext
//import net.minecraft.world.level.Level
//import net.minecraft.world.level.block.Rotation
//import net.minecraft.world.level.block.SoundType
//import net.minecraft.world.level.block.entity.BlockEntity
//import net.minecraft.world.level.block.entity.BlockEntityType
//import net.minecraft.world.level.block.state.BlockState
//import net.minecraft.world.level.block.state.properties.BlockStateProperties
//import net.minecraft.world.level.block.state.properties.BooleanProperty
//import net.minecraft.world.level.block.state.properties.Property
//import net.minecraftforge.common.util.ForgeSoundType
import com.dannbrown.databoxlib.init.ProjectBlockEntities
import com.dannbrown.databoxlib.init.ProjectBlocks
import com.simibubi.create.api.connectivity.ConnectivityHandler
import com.simibubi.create.content.equipment.wrench.IWrenchable
import com.simibubi.create.foundation.block.IBE
import com.simibubi.create.foundation.item.ItemHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Direction.Axis
import net.minecraft.core.Direction.AxisDirection
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition.Builder
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.Property
import net.minecraftforge.common.capabilities.ForgeCapabilities
import net.minecraftforge.common.util.ForgeSoundType
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.IItemHandler
import javax.annotation.Nullable

//class  CargoBayBlock(props: Properties): ItemVaultBlock(props), IBE<ItemVaultBlockEntity>{
//
//
//
//  override fun getBlockEntityType(): BlockEntityType<out CargoBayBlockEntity> {
//    return DataboxBlockEntities.CARGO_BAY.get()
//  }
//
//  override fun getBlockEntityClass(): Class<ItemVaultBlockEntity> {
//    return CargoBayBlockEntity::class.java as (Class<ItemVaultBlockEntity>)
//  }
//
//  companion object {
//    val HORIZONTAL_AXIS: Property<Axis> = BlockStateProperties.HORIZONTAL_AXIS
//    val LARGE = BooleanProperty.create("large")
//    fun isVault(state: BlockState): Boolean {
//      return DataboxBlocks.CARGO_BAY.has(state)
//    }
//
//    @Nullable
//    fun getVaultBlockAxis(state: BlockState): Axis? {
//      return if (!isVault(state)) null else state.getValue(HORIZONTAL_AXIS)
//    }
//
//    fun isLarge(state: BlockState): Boolean {
//      return if (!isVault(state)) false else state.getValue(LARGE)
//    }
//
//    // Vaults are less noisy when placed in batch
//    val SILENCED_METAL: SoundType = ForgeSoundType(0.1f, 1.5f,
//      { SoundEvents.NETHERITE_BLOCK_BREAK },
//      { SoundEvents.NETHERITE_BLOCK_STEP },
//      { SoundEvents.NETHERITE_BLOCK_PLACE },
//      { SoundEvents.NETHERITE_BLOCK_HIT }
//    ) { SoundEvents.NETHERITE_BLOCK_FALL }
//  }
//
//  init {
//    registerDefaultState(defaultBlockState().setValue(LARGE, false))
//  }
//
//
//    override fun getStateForPlacement(pContext: BlockPlaceContext): BlockState {
//    if (pContext.player == null || !pContext.player!!
//        .isShiftKeyDown
//    ) {
//      val placedOn = pContext.level
//        .getBlockState(
//          pContext.clickedPos
//            .relative(
//              pContext.clickedFace
//                .opposite
//            )
//        )
//      val preferredAxis: Axis? = getVaultBlockAxis(placedOn)
//      if (preferredAxis != null) return this.defaultBlockState()
//        .setValue(HORIZONTAL_AXIS, preferredAxis)
//    }
//    return this.defaultBlockState()
//      .setValue(
//        HORIZONTAL_AXIS, pContext.horizontalDirection
//          .axis
//      )
//  }
//
//
//  override fun onPlace(pState: BlockState, pLevel: Level?, pPos: BlockPos?, pOldState: BlockState, pIsMoving: Boolean) {
//    if (pOldState.block === pState.block) return
//    if (pIsMoving) return
//    withBlockEntityDo(pLevel, pPos) { obj -> (obj as CargoBayBlockEntity).updateConnectivity() }
//  }
//
//  override fun onWrenched(state: BlockState, context: UseOnContext): InteractionResult {
//    var state = state
//    if (context.clickedFace
//        .axis
//        .isVertical
//    ) {
//      val be = context.level
//        .getBlockEntity(context.clickedPos)
//      if (be is CargoBayBlockEntity) {
//        ConnectivityHandler.splitMulti(be)
//        be.removeController(true)
//      }
//      state = state.setValue(LARGE, false)
//    }
//    return super.onWrenched(state, context)
//  }
//
//  override fun onRemove(state: BlockState, world: Level, pos: BlockPos, newState: BlockState, pIsMoving: Boolean) {
//    if (state.hasBlockEntity() && (state.block !== newState.block || !newState.hasBlockEntity())) {
//      val be: BlockEntity = world.getBlockEntity(pos) as? CargoBayBlockEntity ?: return
//      val vaultBE = be as CargoBayBlockEntity
//      ItemHelper.dropContents(world, pos, vaultBE.inventoryOfBlock)
//      world.removeBlockEntity(pos)
//      ConnectivityHandler.splitMulti(vaultBE)
//    }
//  }
//
//  override fun rotate(state: BlockState, rot: Rotation): BlockState {
//    val axis: Axis = state.getValue(HORIZONTAL_AXIS)
//    return state.setValue(
//      HORIZONTAL_AXIS, rot.rotate(Direction.fromAxisAndDirection(axis, AxisDirection.POSITIVE)).axis
//    )
//  }
//
//
//
//
//}
class CargoBayBlock(props: Properties?) : Block(props), IWrenchable, IBE<CargoBayBlockEntity> {
  override fun createBlockStateDefinition(pBuilder: Builder<Block?, BlockState?>) {
    pBuilder.add(HORIZONTAL_AXIS, LARGE)
    super.createBlockStateDefinition(pBuilder)
  }

  override fun getStateForPlacement(pContext: BlockPlaceContext): BlockState {
    if (pContext.player == null || !pContext.player!!
        .isShiftKeyDown
    ) {
      val placedOn = pContext.level
        .getBlockState(
          pContext.clickedPos
            .relative(
              pContext.clickedFace
                .opposite
            )
        )
      val preferredAxis: Axis? = getVaultBlockAxis(placedOn)
      if (preferredAxis != null) return this.defaultBlockState()
        .setValue(HORIZONTAL_AXIS, preferredAxis)
    }
    return this.defaultBlockState()
      .setValue(
        HORIZONTAL_AXIS, pContext.horizontalDirection
          .axis
      )
  }

  override fun onPlace(pState: BlockState, pLevel: Level?, pPos: BlockPos?, pOldState: BlockState, pIsMoving: Boolean) {
    if (pOldState.block === pState.block) return
    if (pIsMoving) return
    withBlockEntityDo(pLevel, pPos) { obj: CargoBayBlockEntity -> obj.updateConnectivity() }
  }

  override fun onWrenched(state: BlockState, context: UseOnContext): InteractionResult {
    var state = state
    if (context.clickedFace
        .axis
        .isVertical
    ) {
      val be = context.level
        .getBlockEntity(context.clickedPos)
      if (be is CargoBayBlockEntity) {
        ConnectivityHandler.splitMulti(be)
        be.removeController(true)
      }
      state = state.setValue(LARGE, false)
    }
    return super.onWrenched(state, context)
  }

  override fun onRemove(state: BlockState, level: Level, pos: BlockPos, newState: BlockState, pIsMoving: Boolean) {
    val be: BlockEntity = level.getBlockEntity(pos) as? CargoBayBlockEntity ?: return
    val vaultBE = be as CargoBayBlockEntity

    if (state.hasBlockEntity() && (state.block !== newState.block || !newState.hasBlockEntity())) {
      ItemHelper.dropContents(level, pos, vaultBE.inventoryOfBlock)
      level.removeBlockEntity(pos)
      ConnectivityHandler.splitMulti(vaultBE)
    }
  }

  override fun rotate(state: BlockState, rot: Rotation): BlockState {
    val axis: Axis = state.getValue(HORIZONTAL_AXIS)
    return state.setValue(
      HORIZONTAL_AXIS, rot.rotate(Direction.fromAxisAndDirection(axis, AxisDirection.POSITIVE)).axis
    )
  }

  override fun mirror(state: BlockState, mirrorIn: Mirror?): BlockState {
    return state
  }

  init {
    registerDefaultState(defaultBlockState().setValue(LARGE, false))
  }

  override fun getSoundType(state: BlockState?, world: LevelReader?, pos: BlockPos?, entity: Entity?): SoundType {
    val soundType: SoundType = super.getSoundType(state, world, pos, entity)
    return if (entity != null && entity.getPersistentData()
        .contains("SilenceVaultSound")
    ) SILENCED_METAL
    else soundType
  }

  override fun hasAnalogOutputSignal(blockState: BlockState?): Boolean {
    return true
  }

  override fun getAnalogOutputSignal(pState: BlockState?, pLevel: Level, pPos: BlockPos?): Int {
    return getBlockEntityOptional(pLevel, pPos)
      .map { vte: CargoBayBlockEntity ->
        vte.getCapability(
          ForgeCapabilities.ITEM_HANDLER
        )
      }
      .map { lo: LazyOptional<IItemHandler?> ->
        lo.map { inv: IItemHandler? ->
          ItemHelper.calcRedstoneFromInventory(inv)
        }
          .orElse(0)
      }
      .orElse(0)
  }

  override fun getBlockEntityType(): BlockEntityType<out CargoBayBlockEntity> {
    return ProjectBlockEntities.CARGO_BAY.get()
  }

  override fun getBlockEntityClass(): Class<CargoBayBlockEntity> {
    return CargoBayBlockEntity::class.java
  }

  companion object {
    val HORIZONTAL_AXIS: Property<Axis> = BlockStateProperties.HORIZONTAL_AXIS
    val LARGE = BooleanProperty.create("large")
    fun isVault(state: BlockState?): Boolean {
      return ProjectBlocks.CARGO_BAY.has(state)
    }

    @Nullable
    fun getVaultBlockAxis(state: BlockState): Axis? {
      return if (!isVault(state)) null else state.getValue(HORIZONTAL_AXIS)
    }

    fun isLarge(state: BlockState): Boolean {
      return if (!isVault(state)) false else state.getValue(LARGE)
    }

    // Vaults are less noisy when placed in batch
    val SILENCED_METAL: SoundType = ForgeSoundType(0.1f, 1.5f,
      { SoundEvents.NETHERITE_BLOCK_BREAK },
      { SoundEvents.NETHERITE_BLOCK_STEP },
      { SoundEvents.NETHERITE_BLOCK_PLACE },
      { SoundEvents.NETHERITE_BLOCK_HIT }
    ) { SoundEvents.NETHERITE_BLOCK_FALL }
  }
}