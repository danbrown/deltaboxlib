package com.dannbrown.databoxlib.content.ship.block.thruster

import com.dannbrown.databoxlib.content.ship.SpaceShipControl
import com.dannbrown.databoxlib.content.ship.extensions.getShipManagingPos
import com.dannbrown.databoxlib.content.ship.extensions.getShipObjectManagingPos
import com.dannbrown.databoxlib.content.ship.extensions.toJOMLD
import com.dannbrown.databoxlib.datagen.fuel.ProjectFuel
import com.dannbrown.databoxlib.init.ProjectBlockEntities
import com.dannbrown.databoxlib.init.ProjectTags
import com.simibubi.create.AllItems
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock
import com.simibubi.create.foundation.block.IBE
import com.simibubi.create.foundation.blockEntity.ComparatorUtil
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity
import com.simibubi.create.foundation.blockEntity.SmartBlockEntityTicker
import com.simibubi.create.foundation.placement.IPlacementHelper
import com.simibubi.create.foundation.placement.PlacementHelpers
import com.simibubi.create.foundation.placement.PlacementOffset
import net.minecraft.MethodsReturnNonnullByDefault
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.material.PushReaction
import net.minecraft.world.level.pathfinder.PathComputationType
import net.minecraft.world.phys.BlockHitResult
import org.joml.Vector3d
import org.valkyrienskies.core.api.ships.getAttachment
import java.util.function.Predicate

class ThrusterBlock(props: Properties, val tier: Int) : DirectionalAxisKineticBlock(props.lightLevel { state: BlockState -> if (state.getValue(BURNING)) 13 else 0 }), IBE<ThrusterBlockEntity> {

  init {
    registerDefaultState(
      stateDefinition.any()
        .setValue(FACING, Direction.UP)
        .setValue(BURNING, false)
    )
  }

  companion object {
    val BURNING: BooleanProperty = BooleanProperty.create("burning")
    fun hasPipeTowards(world: LevelReader?, pos: BlockPos?, state: BlockState, face: Direction?): Boolean {
//      return state.getValue(FACING).opposite == face // on the back
      return false
    }
  }

  override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
    builder.add(BURNING)
    super.createBlockStateDefinition(builder)
  }

  private val placementHelperId = PlacementHelpers.register(PlacementHelper())
  override fun getPistonPushReaction(state: BlockState?): PushReaction {
    return PushReaction.NORMAL
  }

  override fun getBlockEntityClass(): Class<ThrusterBlockEntity> {
    return ThrusterBlockEntity::class.java
  }

  override fun getBlockEntityType(): BlockEntityType<out ThrusterBlockEntity>? {
    return ProjectBlockEntities.THRUSTER.get()
  }

  override fun neighborChanged(state: BlockState, world: Level, pos: BlockPos, block: Block, newBlockPos: BlockPos, isMoving: Boolean) {
    withBlockEntityDo(world, pos) { obj: ThrusterBlockEntity -> obj.updated() }
  }

  override fun isPathfindable(state: BlockState, reader: BlockGetter, pos: BlockPos, type: PathComputationType): Boolean {
    return false
  }

  override fun getFacingForPlacement(context: BlockPlaceContext): Direction? {
    var facing = context.nearestLookingDirection
    if (context.player != null && context.player!!.isShiftKeyDown) facing = facing.opposite
    return facing
  }

  override fun showCapacityWithAnnotation(): Boolean {
    return true
  }

  override fun use(state: BlockState, worldIn: Level, pos: BlockPos, player: Player, handIn: InteractionHand, hit: BlockHitResult): InteractionResult {
    val useResult = super.use(state, worldIn, pos, player, handIn, hit)
    val playerHandItem = player.getItemInHand(handIn)
      .copy()
    // Placement helper
    val placementHelper = PlacementHelpers.get(placementHelperId)
    if (!player.isShiftKeyDown && player.mayBuild()) {
      if (placementHelper.matchesItem(playerHandItem) && placementHelper.getOffset(player, worldIn, state, pos, hit)
          .placeInWorld(worldIn, playerHandItem.item as BlockItem, player, handIn, hit)
          .consumesAction()) return InteractionResult.SUCCESS
    }
    // end placement helper
    // pass the wrench check, it will be handled by the wrenchable interface
    if (AllItems.WRENCH.isIn(playerHandItem)) return InteractionResult.PASS

    return useResult
  }

  override fun onWrenched(state: BlockState?, context: UseOnContext?): InteractionResult {
    val world = context!!.level
    val pos = context.clickedPos
    withBlockEntityDo(world, pos) { obj: ThrusterBlockEntity -> obj.updated() }
    return super.onWrenched(state, context)
  }

  //
  override fun hasAnalogOutputSignal(state: BlockState): Boolean {
    return true
  }

  override fun getAnalogOutputSignal(blockState: BlockState, worldIn: Level, pos: BlockPos): Int {
    return ComparatorUtil.levelOfSmartFluidTank(worldIn, pos)
  }

  //
  fun getThrusterForce(state: BlockState, speedCoerce: Double, fuelData: ProjectFuel.FuelData = ProjectFuel.FuelData.empty()): Vector3d {
    return state.getValue(FACING).opposite.normal.toJOMLD()
//      .mul(fuelData.force.get()
//        .toDouble()) // multiply by the force of the fuel
      // multiply by the total of speed, so it will only be at full power when the shaft rotation is at full speed
      .mul(speedCoerce)
      .mul(tier.toDouble())
  }

  override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, isMoving: Boolean) {
    super.onPlace(state, level, pos, oldState, isMoving)
    // update the block entity data
    withBlockEntityDo(level, pos) { obj: ThrusterBlockEntity -> obj.updated() }
    // avoid running on the client side
    if (level.isClientSide) return
    level as ServerLevel
    // get the ship, if it exists update the thruster data
    val ship = level.getShipObjectManagingPos(pos) ?: level.getShipManagingPos(pos) ?: return
    val shipControl = SpaceShipControl.getOrCreate(ship)
    shipControl.addThruster(pos, getThrusterForce(state, 0.0), tier)
  }

  override fun destroy(level: LevelAccessor, pos: BlockPos, state: BlockState) {
    super.destroy(level, pos, state)

    if (level.isClientSide) return
    level as ServerLevel
    val ship = level.getShipObjectManagingPos(pos) ?: level.getShipManagingPos(pos) ?: return
    val shipControl = ship.getAttachment<SpaceShipControl>() ?: return
    shipControl.removeThruster(pos)
    shipControl.forceStopThruster(pos)
  }

  override fun onRemove(blockState: BlockState, level: Level, pos: BlockPos, newBlockState: BlockState, pIsMoving: Boolean) {
    super.onRemove(blockState, level, pos, newBlockState, pIsMoving)
    // handle thruster removal from the ship control
    if (level.isClientSide) return
    level as ServerLevel
    val ship = level.getShipObjectManagingPos(pos) ?: level.getShipManagingPos(pos) ?: return
    val shipControl = ship.getAttachment<SpaceShipControl>() ?: return
    shipControl.removeThruster(pos)
    shipControl.forceStopThruster(pos)
  }

  // override and re-implements the super.getTicker method to add the ThrusterBlockEntity ticker
  override fun <S : BlockEntity?> getTicker(pLevel: Level, pState: BlockState, type: BlockEntityType<S>): BlockEntityTicker<S>? {
    if (SmartBlockEntity::class.java.isAssignableFrom(blockEntityClass)) return object : SmartBlockEntityTicker<S>() {
      override fun tick(level: Level, pos: BlockPos, state: BlockState, blockEntity: S) {
        super.tick(level, pos, state, blockEntity)
        if (level.isClientSide) return
        if (blockEntity !is ThrusterBlockEntity) return
        blockEntity.ticker()
      }
    }
    return null
  }

  override fun animateTick(state: BlockState, level: Level, pos: BlockPos, random: RandomSource) {
    super.animateTick(state, level, pos, random)
    val blockEntity = level.getBlockEntity(pos) as? ThrusterBlockEntity ?: return // Not a thruster

    if (!blockEntity.running) return // Not enough force to animate
    val speedDistance = blockEntity.getSpeedCoerce() * 2.0
    // add particles
    val dir = state.getValue(FACING).opposite
    val x = pos.x.toDouble() + (0.5 * (dir.stepX + 1))
    val y = pos.y.toDouble() + (0.5 * (dir.stepY + 1))
    val z = pos.z.toDouble() + (0.5 * (dir.stepZ + 1))
    val speedX = dir.stepX * -0.4 * speedDistance
    val speedY = dir.stepY * -0.4 * speedDistance
    val speedZ = dir.stepZ * -0.4 * speedDistance

    level.addParticle(ParticleTypes.FLAME, x, y, z, speedX, speedY, speedZ)
  }

  // Placement
  @MethodsReturnNonnullByDefault
  private class PlacementHelper : IPlacementHelper {
    override fun getItemPredicate(): Predicate<ItemStack> {
      return Predicate { stack: ItemStack -> stack.tags.anyMatch { tag -> ProjectTags.ITEM.IS_THRUSTER == tag } }
    }

    override fun getStatePredicate(): Predicate<BlockState> {
      return Predicate { state: BlockState -> state.tags.anyMatch { tag -> ProjectTags.BLOCK.IS_THRUSTER == tag } }
    }

    override fun getOffset(player: Player, world: Level, state: BlockState, pos: BlockPos,
                           ray: BlockHitResult
    ): PlacementOffset {
      val directions = IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.location,
        state.getValue(FACING)
          .axis
      ) { dir: Direction ->
        world.getBlockState(pos.relative(dir))
          .canBeReplaced()
      }

      return if (directions.isEmpty()) PlacementOffset.fail()
      else {
        PlacementOffset.success(pos.relative(directions[0])
        ) { s: BlockState ->
          s.setValue(FACING, state.getValue(FACING))
            .setValue(AXIS_ALONG_FIRST_COORDINATE, state.getValue(AXIS_ALONG_FIRST_COORDINATE))
        }
      }
    }
  }
  // -----
}