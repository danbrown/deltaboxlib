package com.dannbrown.databoxlib.content.ship

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.content.ship.extensions.toBlockPos
import com.dannbrown.databoxlib.content.ship.extensions.toJOML
import com.dannbrown.databoxlib.init.ProjectBlocks
import com.dannbrown.databoxlib.init.ProjectConfig
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import org.joml.AxisAngle4d
import org.joml.Quaterniond
import org.joml.Vector3d
import org.joml.Vector3dc
import org.joml.Vector3ic
import org.valkyrienskies.core.api.VSBeta
import org.valkyrienskies.core.api.ships.PhysShip
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.api.ships.ServerTickListener
import org.valkyrienskies.core.api.ships.Ship
import org.valkyrienskies.core.api.ships.ShipForcesInducer
import org.valkyrienskies.core.api.ships.getAttachment
import org.valkyrienskies.core.api.ships.saveAttachment
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl
import org.valkyrienskies.core.util.datastructures.DenseBlockPosSet
import org.valkyrienskies.mod.api.SeatedControllingPlayer
import thedarkcolour.kotlinforforge.forge.vectorutil.v3d.toVector3d
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

@JsonAutoDetect(
  fieldVisibility = JsonAutoDetect.Visibility.ANY,
  getterVisibility = JsonAutoDetect.Visibility.NONE,
  isGetterVisibility = JsonAutoDetect.Visibility.NONE,
  setterVisibility = JsonAutoDetect.Visibility.NONE
)
@JsonIgnoreProperties(ignoreUnknown = true)
class SpaceShipControl : ShipForcesInducer, ServerTickListener {
  @JsonIgnore
  internal var ship: ServerShip? = null
  private var extraForceLinear = 0.0
  private var extraForceAngular = 0.0
  var aligning = false
  var disassembling = false // Disassembling also affects position
  private var physConsumption = 0f

  //  private val anchored get() = anchorsActive > 0
  private var angleUntilAligned = 0.0
  private var positionUntilAligned = Vector3d()
  private var alignTarget = 0
  val canDisassemble
    get() = ship != null &&
      disassembling &&
      abs(angleUntilAligned) < DISASSEMBLE_THRESHOLD &&
      positionUntilAligned.distanceSquared(this.ship!!.transform.positionInWorld) < 4.0
  val aligningTo: Direction get() = Direction.from2DDataValue(alignTarget)
  var consumed = 0f
    private set
  private var wasCruisePressed = false

  @JsonProperty("cruise")
  var isCruising = false
  private var controlData: ControlData? = null

  @JsonIgnore
  var seatedPlayer: Player? = null
  var powerLinear = 0.0
  var powerAngular = 0.0

  @JsonIgnore
  var physShip: PhysShipImpl? = null

  // SHIP PARTS
  private val antimatters = mutableListOf<Vector3ic>() // blockPos
  private val massNullifiers = mutableListOf<Pair<Vector3ic, Boolean>>() // blockPos, active
  private val sails = mutableListOf<Vector3ic>() // blockPos
  private val captainSeats = mutableListOf<Vector3ic>() // blockPos
  private val stabilizers = mutableListOf<Pair<Vector3ic, Boolean>>() // blockPos, active

  @JsonIgnore
  private val thrusters = mutableListOf<Triple<Vector3ic, Vector3dc, Int>>() // blockPos, force, tier
  fun getAntimatters(): List<Vector3ic> = antimatters
  fun getSails(): List<Vector3ic> = sails
  fun getCaptainSeats(): List<Vector3ic> = captainSeats
  fun getStabilizers(): List<Pair<Vector3ic, Boolean>> = stabilizers
  fun getMassNullifiers(): List<Pair<Vector3ic, Boolean>> = massNullifiers
  fun getThrusters(): List<Triple<Vector3ic, Vector3dc, Int>> = thrusters
  fun addAntimatter(pos: BlockPos) {
    antimatters.removeIf { it == pos.toJOML() }
    antimatters.add(pos.toJOML())
  }

  fun removeAntimatter(pos: BlockPos) {
    antimatters.remove(pos.toJOML())
  }

  fun addSail(pos: BlockPos) {
    sails.removeIf { it == pos.toJOML() }
    sails.add(pos.toJOML())
  }

  fun removeSail(pos: BlockPos) {
    sails.remove(pos.toJOML())
  }

  fun addCaptainSeat(pos: BlockPos) {
    captainSeats.removeIf { it == pos.toJOML() }
    captainSeats.add(pos.toJOML())
  }

  fun removeCaptainSeat(pos: BlockPos) {
    captainSeats.remove(pos.toJOML())
  }

  fun addStabilizer(pos: BlockPos, active: Boolean) {
    stabilizers.removeIf { it.first == pos.toJOML() }
    stabilizers.add(Pair(pos.toJOML(), active))
  }

  fun removeStabilizer(pos: BlockPos) {
    stabilizers.removeIf { it.first == pos.toJOML() }
  }

  fun addMassNullifier(pos: BlockPos, active: Boolean) {
    massNullifiers.removeIf { it.first == pos.toJOML() }
    massNullifiers.add(Pair(pos.toJOML(), active))
  }

  fun removeMassNullifier(pos: BlockPos) {
    massNullifiers.removeIf { it.first == pos.toJOML() }
  }

  fun updateStabilizerActive(pos: BlockPos, active: Boolean) {
    stabilizers.replaceAll {
      if (it.first == pos.toJOML()) {
        Pair(it.first, active)
      }
      else it
    }
  }

  fun updateMassNullifierActive(pos: BlockPos, active: Boolean) {
    massNullifiers.replaceAll {
      if (it.first == pos.toJOML()) {
        Pair(it.first, active)
      }
      else it
    }
  }

  fun addThruster(pos: BlockPos, force: Vector3dc, tier: Int) {
    thrusters.add(Triple(pos.toJOML(), force, tier))
  }

  fun removeThruster(pos: BlockPos) {
    thrusters.removeIf { it.first == pos.toJOML() }
  }

  fun forceStopThruster(pos: BlockPos) {
    thrusters.removeAll { it.first == pos }
  }

  fun updateThrusterForce(pos: BlockPos, force: Vector3dc, tier: Int) {
    thrusters.replaceAll {
      if (it.first == pos.toJOML()) {
        Triple(it.first, force, tier)
      }
      else it
    }
  }

  fun getShipPartsCount(): Int {
    return sails.size + stabilizers.size + captainSeats.size + antimatters.size + thrusters.size
  }

  fun updateShipControl(level: ServerLevel, blocks: DenseBlockPosSet) {
    sails.clear()
    sails.addAll(getShipBlockPositions(level, blocks, ProjectBlocks.SAIL_BLOCK.get()))

    stabilizers.clear()
    val stabilizersPos = getShipBlockPositions(level, blocks, ProjectBlocks.GRAVITY_STABILIZER.get())
    val stabilizersState = getShipBlockState(level, blocks, ProjectBlocks.GRAVITY_STABILIZER.get()).map { it.getValue(BlockStateProperties.POWERED) }
    stabilizers.addAll(stabilizersPos.zip(stabilizersState))
    stabilizers.filter { stabilizers.contains(it) || stabilizers.contains(Pair(it.first, !it.second)) }

    captainSeats.clear()
    captainSeats.addAll(getShipBlockPositions(level, blocks, ProjectBlocks.CAPTAIN_SEAT.get()))
    captainSeats.filter { captainSeats.contains(it) }

    antimatters.clear()
    antimatters.addAll(getShipBlockPositions(level, blocks, ProjectBlocks.ANTIMATTER_BLOCK.get()))
    antimatters.filter { antimatters.contains(it) }

    massNullifiers.clear()
    val massNullifiersPos = getShipBlockPositions(level, blocks, ProjectBlocks.MASS_NULLIFIER_BLOCK.get())
    val massNullifiersState = getShipBlockState(level, blocks, ProjectBlocks.MASS_NULLIFIER_BLOCK.get()).map { it.getValue(BlockStateProperties.POWERED) }
    massNullifiers.addAll(massNullifiersPos.zip(massNullifiersState))
    massNullifiers.filter { massNullifiers.contains(it) || massNullifiers.contains(Pair(it.first, !it.second)) }

    thrusters.clear()
//    for (blockPos in blocks) {
//      val block = level.getBlockState(blockPos.toBlockPos()).block
//      if (block is ThrusterBlock) {
//        thrusters.add(Triple(blockPos, Vector3d(0.0, 0.0, 0.0), block.tier))
//      }
//    }
  }

  private fun getShipBlockPositions(level: ServerLevel, blocks: DenseBlockPosSet, blockType: Block): List<Vector3ic> {
    val listToReturn = mutableListOf<Vector3ic>()
    for (blockPos in blocks) {
      if (level.getBlockState(blockPos.toBlockPos()).block == blockType) {
        listToReturn.add(blockPos)
      }
    }
    return listToReturn
  }

  private fun getShipBlockState(level: ServerLevel, blocks: DenseBlockPosSet, blockType: Block): List<BlockState> {
    val listToReturn = mutableListOf<BlockState>()
    for (blockPos in blocks) {
      if (level.getBlockState(blockPos.toBlockPos()).block == blockType) {
        listToReturn.add(level.getBlockState(blockPos.toBlockPos()))
      }
    }
    return listToReturn
  }

  // END SHIP PARTS
  companion object {
    fun getOrCreate(ship: ServerShip): SpaceShipControl {
      return ship.getAttachment<SpaceShipControl>()
        ?: SpaceShipControl().also { ship.saveAttachment(it) }
    }

    private const val ALIGN_THRESHOLD = 0.01
    private const val DISASSEMBLE_THRESHOLD = 0.02
    private val forcePerAntimatter get() = ProjectConfig.SERVER.massPerAntimatter * -GRAVITY
    private val nullifierForcePerBlock get() = ProjectConfig.SERVER.massNullifierNegatePerBlock * -GRAVITY
    private const val GRAVITY = -10 // TODO: make the gravity affected by the planet
    val START_CRUISING_TRANSLATION_KEY = "hud.${ProjectContent.MOD_ID}.start_cruising"
    val STOP_CRUISING_TRANSLATION_KEY = "hud.${ProjectContent.MOD_ID}.stop_cruising"
  }

  override fun onServerTick() {
    extraForceLinear = powerLinear
    powerLinear = 0.0

    extraForceAngular = powerAngular
    powerAngular = 0.0

    consumed = physConsumption * /* should be physics ticks based*/ 0.1f
    physConsumption = 0.0f
  }

  //
  // Revisiting eureka control code.
  // [x] Move torque stabilization code
  // [x] Move linear stabilization code
  // [x] Revisit player controlled torque
  // [x] Revisit player controlled linear force
  // [x] Anchor freezing
  // [x] Rewrite Alignment code
  // [x] Revisit Elevation code
  // [x] Balloon limiter
  // [ ] Add Cruise code
  // [ ] Rotation based of ship-size
  // [x] Engine consumption
  // [ ] Fix elevation sensitivity
  //
  @OptIn(VSBeta::class)
  override fun applyForces(physShip: PhysShip) {
    physShip.doFluidDrag = true // make this an option on sail
    physShip as PhysShipImpl
    val ship = ship ?: return // if there is no ship attached to, don't do anything
    this.physShip = physShip
    val controllingPlayer = ship.getAttachment<SeatedControllingPlayer>()
//    val control = ship.getAttachment<DataboxShipControl>()
//    val alfa = ship.getAttachment<GameTickForceApplier>()
//    val mass = physShip.inertia.shipMass
//    val moiTensor = physShip.inertia.momentOfInertiaTensor
//    val omega: Vector3dc = physShip.poseVel.omega
//    val vel: Vector3dc = physShip.poseVel.vel
    handleAligning(physShip, ship)
    handleStabilize(physShip)
    handleAntimatters(physShip)
    handleMassNullifier(physShip)
    handleThrusters(physShip)
    handleWorldAltitude(physShip, ship)
//    handlePlayerControl(physShip, ship, controllingPlayer) // is absolutely broken
  }

  private fun handleWorldAltitude(physShip: PhysShipImpl, ship: ServerShip) {
    // doesn't allow any part of the ship to be above/below the world height limit, by nullifying the force that is pushing it up/down
//    val dimensionId: DimensionId = ship.chunkClaimDimension
    val shipMass = physShip.inertia.shipMass
    val worldMaxHeight = 320 // TODO: get a way of finding the ship dimensionType and use that instead of the hardcoded one
    val worldMinHeight = 0 // TODO: get a way of finding the ship dimensionType and use that instead of the hardcoded one
    val highestShipPosition = ship.worldAABB.maxY()
    val lowestShipPosition = ship.worldAABB.minY()
    val heightTolerance = 10
    val hasNullifier = massNullifiers.any { it.second }
    // values for the ship being above the world height limit
    val closeToCeiling = (highestShipPosition + heightTolerance) >= (worldMaxHeight)
    val closeToBottom = (lowestShipPosition - heightTolerance) <= worldMinHeight

    if (closeToCeiling) {
      val elasticForceMultiplier = 24
      val forceStrength = if (hasNullifier) shipMass else shipMass / elasticForceMultiplier
      val gravityForce = if (hasNullifier) Vector3d(0.0, GRAVITY * shipMass * elasticForceMultiplier, 0.0) else Vector3d(0.0, GRAVITY * shipMass, 0.0)
      // negate all the forces that are pushing the ship up
      val shipUpForces = if (physShip.poseVel.vel.y() > 0.0) physShip.poseVel.vel.y() else physShip.poseVel.vel.y() * -1 // make sure the force is always positive
      val downwardForce = Vector3d(0.0, shipUpForces, 0.0).mul(-1.0) // negate the force
        .mul(forceStrength * ProjectConfig.SERVER.elevationSnappiness) // multiply by the snappiness
        .add(gravityForce) // add the gravity
      physShip.applyInvariantForce(downwardForce)
    }

    if (closeToBottom) {
      val elasticForceMultiplier = 16
      val forceStrength = if (hasNullifier) shipMass else shipMass / elasticForceMultiplier
      val gravityForce = if (hasNullifier) Vector3d(0.0, -GRAVITY * shipMass * elasticForceMultiplier, 0.0) else Vector3d(0.0, -GRAVITY * shipMass * elasticForceMultiplier, 0.0)
      // negate all the forces that are pushing the ship down
      val shipDownForces = if (physShip.poseVel.vel.y() < 0.0) physShip.poseVel.vel.y() else physShip.poseVel.vel.y() * -1 // make sure the force is always negative
      val upwardForce = Vector3d(0.0, shipDownForces, 0.0).mul(-1.0) // negate the force
        .mul(forceStrength * ProjectConfig.SERVER.elevationSnappiness) // multiply by the snappiness
        .add(gravityForce) // add the gravity
      physShip.applyInvariantForce(upwardForce)
      physShip.applyInvariantTorque(Vector3d(0.0, 0.0, 0.0)) // stop the ship from rotating
    }
  }

  private fun handleThrusters(physShip: PhysShipImpl) {
    try {
      val shipMass = physShip.inertia.shipMass
      val thrustersWithForce = thrusters.filter { it.second.lengthSquared() > 0.0 } // filter out the thrusters that have no force

      if (thrustersWithForce.isEmpty()) return
      thrustersWithForce.forEach {
        val (pos, force, tier) = it
        val forceInWorld = physShip.transform.shipToWorld.transformDirection(Vector3d(force)) // force in world
        val isScalarThruster = tier == 100 // Tier 100 is a Mass Special Thruster
        val thrusterForce = if (isScalarThruster) forceInWorld.mul(shipMass / 32)
        else forceInWorld.mul(ProjectConfig.SERVER.thrusterSpeed) // default is multiply by the speed constant
        val posCenter = Vector3d(pos).add(0.5, 0.5, 0.5) // center of the block
          .sub(physShip.transform.positionInShip)
        val hasForce = thrusterForce.lengthSquared() > 0.0 // if the force is not 0
//      // check if any of the variables are null
//      if (physShip == null || physShip.inertia == null || physShip.inertia.momentOfInertiaTensor == null || pos == null || force == null || thrusterForce == null || posCenter == null || hasForce == null) {
//        println("physShip: $physShip")
//      }
        if (force.isFinite && physShip.poseVel.vel.length() < ProjectConfig.SERVER.thrusterShutoffSpeed && hasForce) {
          physShip.applyInvariantForceToPos(thrusterForce, posCenter)
        }
      }
    } catch (e: Exception) {
      println("Error in handleThrusters: $e")
    }
  }

  private fun handleAntimatters(physShip: PhysShipImpl) {
    if (antimatters.size <= 0) return
    var idealUpwardVel = Vector3d(0.0, 0.0, 0.0)
    val antimatterForceProvided = antimatters.size * forcePerAntimatter
    val mass = physShip.inertia.shipMass
    val vel: Vector3dc = physShip.poseVel.vel
    // Floating
    val idealUpwardForceY = idealUpwardVel.y() - vel.y() - (GRAVITY / ProjectConfig.SERVER.elevationSnappiness)
    val idealUpwardForce = Vector3d(0.0, idealUpwardForceY, 0.0).mul(mass * ProjectConfig.SERVER.elevationSnappiness)
    val actualUpwardForce = Vector3d(0.0, min(antimatterForceProvided, max(idealUpwardForce.y(), 0.0)), 0.0)
    physShip.applyInvariantForce(actualUpwardForce)
  }

  // apply a force to the ship to counteract gravity and make it float
  private fun handleMassNullifier(physShip: PhysShipImpl) {
    if (massNullifiers.any { it.second }) { // if any of the mass nullifiers are active
      var idealUpwardVel = Vector3d(0.0, 0.0, 0.0)
      val mass = physShip.inertia.shipMass
      val forgeToNegate = ProjectConfig.SERVER.maxShipBlocks * nullifierForcePerBlock // the force needed considering the max blocks
      val vel: Vector3dc = physShip.poseVel.vel
      // Floating
      val idealUpwardForceY = idealUpwardVel.y() - vel.y() - (GRAVITY / ProjectConfig.SERVER.elevationSnappiness)
      val idealUpwardForce = Vector3d(0.0, idealUpwardForceY, 0.0).mul(mass * ProjectConfig.SERVER.elevationSnappiness)
      val actualUpwardForce = Vector3d(0.0, min(forgeToNegate, max(idealUpwardForce.y(), 0.0)), 0.0)
      physShip.applyInvariantForce(actualUpwardForce)
    }
  }

  private fun handleAligning(physShip: PhysShipImpl, ship: Ship) {
    val moiTensor = physShip.inertia.momentOfInertiaTensor
    // region Aligning
    val invRotation = physShip.poseVel.rot.invert(Quaterniond())
    val invRotationAxisAngle = AxisAngle4d(invRotation)
    // Floor makes a number 0 to 3, which corresponds to direction
    alignTarget = floor((invRotationAxisAngle.angle / (PI * 0.5)) + 4.5).toInt() % 4
    angleUntilAligned = (alignTarget.toDouble() * (0.5 * PI)) - invRotationAxisAngle.angle
    if (disassembling) {
      val pos = ship.transform.positionInWorld
      positionUntilAligned = pos.floor(Vector3d())
      val direction = pos.sub(positionUntilAligned, Vector3d())
      physShip.applyInvariantForce(direction)
    }
    if ((aligning) && abs(angleUntilAligned) > ALIGN_THRESHOLD) {
      if (angleUntilAligned < 0.3 && angleUntilAligned > 0.0) angleUntilAligned = 0.3
      if (angleUntilAligned > -0.3 && angleUntilAligned < 0.0) angleUntilAligned = -0.3
      val idealOmega = Vector3d(invRotationAxisAngle.x, invRotationAxisAngle.y, invRotationAxisAngle.z)
        .mul(-angleUntilAligned)
        .mul(ProjectConfig.SERVER.stabilizationSpeed)
      val idealTorque = moiTensor.transform(idealOmega)

      physShip.applyInvariantTorque(idealTorque)
    }
    // endregion
  }

  private fun handlePlayerControl(physShip: PhysShipImpl, ship: Ship, controllingPlayer: SeatedControllingPlayer?) {
    val mass = physShip.inertia.shipMass
    val moiTensor = physShip.inertia.momentOfInertiaTensor
    val omega: Vector3dc = physShip.poseVel.omega
    val vel: Vector3dc = physShip.poseVel.vel
    val balloonForceProvided = antimatters.size * forcePerAntimatter
    var idealUpwardVel = Vector3d(0.0, 0.0, 0.0)

    if (controllingPlayer != null && seatedPlayer != null) {
      val currentControlData = ControlData.create(controllingPlayer)
      // If the player is currently controlling the ship
      if (!wasCruisePressed && controllingPlayer.cruise) {
        // the player pressed the cruise button
        isCruising = !isCruising
        showCruiseStatus()
      }
      else if (!controllingPlayer.cruise &&
        isCruising &&
        (controllingPlayer.leftImpulse != 0.0f || controllingPlayer.sprintOn || controllingPlayer.upImpulse != 0.0f || controllingPlayer.forwardImpulse != 0.0f) &&
        currentControlData != controlData
      ) {
        // The player pressed another button
        isCruising = false
        showCruiseStatus()
      }

      if (!isCruising) {
        // only take the latest control data if the player is not cruising
        controlData = currentControlData
      }

      wasCruisePressed = controllingPlayer.cruise
    }
    else if (!isCruising) {
      // If the player isn't controlling the ship, and not cruising, reset the control data
      controlData = null
    }

    controlData?.let { control ->
      // region Player controlled rotation
      val transform = physShip.transform
      val aabb = ship.worldAABB
      val center = transform.positionInWorld
      val stw = transform.shipToWorld
      val wts = transform.worldToShip
      val largestDistance = run {
        var dist = center.distance(aabb.minX(), center.y(), aabb.minZ())
        dist = max(dist, center.distance(aabb.minX(), center.y(), aabb.maxZ()))
        dist = max(dist, center.distance(aabb.maxX(), center.y(), aabb.minZ()))
        dist = max(dist, center.distance(aabb.maxX(), center.y(), aabb.maxZ()))

        dist
      }.coerceIn(0.5, ProjectConfig.SERVER.maxSizeForTurnSpeedPenalty)
      val maxLinearAcceleration = ProjectConfig.SERVER.turnAcceleration
      val maxLinearSpeed = ProjectConfig.SERVER.turnSpeed + extraForceAngular
      // acceleration = alpha * r
      // therefore: maxAlpha = maxAcceleration / r
      val maxOmegaY = maxLinearSpeed / largestDistance
      val maxAlphaY = maxLinearAcceleration / largestDistance
      val isBelowMaxTurnSpeed = abs(omega.y()) < maxOmegaY
      val normalizedAlphaYMultiplier =
        if (isBelowMaxTurnSpeed && control.leftImpulse != 0.0f) control.leftImpulse.toDouble()
        else -omega.y()
          .coerceIn(-1.0, 1.0)
      val idealAlphaY = normalizedAlphaYMultiplier * maxAlphaY
      val alpha = Vector3d(0.0, idealAlphaY, 0.0)
      val angularImpulse =
        stw.transformDirection(moiTensor.transform(wts.transformDirection(Vector3d(alpha))))
      val torque = Vector3d(angularImpulse)
      physShip.applyInvariantTorque(torque)
      // endregion
      // region Player controlled banking
      val rotationVector = control.seatInDirection.normal.toVector3d()

      physShip.poseVel.transformDirection(rotationVector)

      rotationVector.y = 0.0

      rotationVector.mul(idealAlphaY * -1.5)

      physShip.poseVel.rot.transform(
        moiTensor.transform(
          physShip.poseVel.rot.transformInverse(rotationVector)
        )
      )

      physShip.applyInvariantTorque(rotationVector)
      // endregion
      // region Player controlled forward and backward thrust
      val forwardVector = control.seatInDirection.normal.toVector3d()
      physShip.poseVel.rot.transform(forwardVector)
      forwardVector.y *= 0.1 // Reduce vertical thrust
      forwardVector.normalize()

      forwardVector.mul(control.forwardImpulse.toDouble())
      val playerUpDirection = physShip.poseVel.transformDirection(Vector3d(0.0, 1.0, 0.0))
      val velOrthogonalToPlayerUp =
        vel.sub(playerUpDirection.mul(playerUpDirection.dot(vel), Vector3d()), Vector3d())
      // This is the speed that the ship is always allowed to go out, without engines
      val baseForwardVel = Vector3d(forwardVector).mul(ProjectConfig.SERVER.baseSpeed)
      val baseForwardForce = Vector3d(baseForwardVel).sub(velOrthogonalToPlayerUp)
        .mul(mass * 10)
      // This is the maximum speed we want to go in any scenario (when not sprinting)
      val idealForwardVel = Vector3d(forwardVector).mul(ProjectConfig.SERVER.maxCasualSpeed)
      val idealForwardForce = Vector3d(idealForwardVel).sub(velOrthogonalToPlayerUp)
        .mul(mass * 10)
      val extraForceNeeded = Vector3d(idealForwardForce).sub(baseForwardForce)
      val actualExtraForce = Vector3d(baseForwardForce)

      if (extraForceLinear != 0.0) {
        actualExtraForce.fma(min(extraForceLinear / extraForceNeeded.length(), 1.0), extraForceNeeded)
      }

      physShip.applyInvariantForce(actualExtraForce)
      // endregion
      // Player controlled elevation
      if (control.upImpulse != 0.0f) {
        idealUpwardVel = Vector3d(0.0, 1.0, 0.0)
          .mul(control.upImpulse.toDouble())
          .mul(
            ProjectConfig.SERVER.baseImpulseElevationRate +
              // Smoothing for how the elevation scales as you approaches the balloonElevationMaxSpeed
              smoothing(2.0, ProjectConfig.SERVER.balloonElevationMaxSpeed, balloonForceProvided / mass)
          )
      }
    }
  }

  private fun handleStabilize(physShip: PhysShipImpl) {
    val omega: Vector3dc = physShip.poseVel.omega
    val vel: Vector3dc = physShip.poseVel.vel
//    val isLinear = controllingPlayer == null && !aligning
//    val isAngular = controllingPlayer == null
    val isLinear = !aligning
    val isAngular = true
    // check if any stabilizers are active
    if (stabilizers.any { it.second }) {
      physShip.doFluidDrag = ProjectConfig.SERVER.doFluidDrag
      stabilizeGravity(physShip, omega, vel, physShip, isLinear, isAngular)
    }
  }

  private fun showCruiseStatus() {
    val cruisingTranslateKey = if (isCruising) START_CRUISING_TRANSLATION_KEY else STOP_CRUISING_TRANSLATION_KEY
    seatedPlayer?.displayClientMessage(Component.translatable(cruisingTranslateKey), true)
  }

  private fun deleteIfEmpty() {
    if (getShipPartsCount() <= 0) {
//    if (helms <= 0 && floaters <= 0 && anchors <= 0 && balloons <= 0) {
      ship?.saveAttachment<SpaceShipControl>(null)
    }
  }

  /**
   * f(x) = max - smoothing / (x + (smoothing / max))
   */
  private fun smoothing(smoothing: Double, max: Double, x: Double): Double = max - smoothing / (x + (smoothing / max))
  private fun stabilizeGravity(physShip: PhysShipImpl, omega: Vector3dc, vel: Vector3dc, forces: PhysShip, linear: Boolean, yaw: Boolean) {
    val shipUp = Vector3d(0.0, 1.0, 0.0)
    val worldUp = Vector3d(0.0, 1.0, 0.0)
    physShip.poseVel.rot.transform(shipUp)
    val angleBetween = shipUp.angle(worldUp)
    val idealAngularAcceleration = Vector3d()
    if (angleBetween > .01) {
      val stabilizationRotationAxisNormalized = shipUp.cross(worldUp, Vector3d())
        .normalize()
      idealAngularAcceleration.add(
        stabilizationRotationAxisNormalized.mul(
          angleBetween,
          stabilizationRotationAxisNormalized
        )
      )
    }
    // Only subtract the x/z components of omega.
    // We still want to allow rotation along the Y-axis (yaw).
    // Except if yaw is true, then we stabilize
    idealAngularAcceleration.sub(
      omega.x(),
      if (!yaw) 0.0 else omega.y(),
      omega.z()
    )
    val stabilizationTorque = physShip.poseVel.rot.transform(
      physShip.inertia.momentOfInertiaTensor.transform(
        physShip.poseVel.rot.transformInverse(idealAngularAcceleration)
      )
    )

    stabilizationTorque.mul(ProjectConfig.SERVER.stabilizationTorqueConstant)
    forces.applyInvariantTorque(stabilizationTorque)

    if (linear) {
      val idealVelocity = Vector3d(vel).negate()
      idealVelocity.y = 0.0

      if (idealVelocity.lengthSquared() > (ProjectConfig.SERVER.linearStabilizeMaxAntiVelocity * ProjectConfig.SERVER.linearStabilizeMaxAntiVelocity))
        idealVelocity.normalize(ProjectConfig.SERVER.linearStabilizeMaxAntiVelocity)

      idealVelocity.mul(physShip.inertia.shipMass * (10 - ProjectConfig.SERVER.antiVelocityMassRelevance))
      forces.applyInvariantForce(idealVelocity)
    }
  }

  // SubClasses
  private data class ControlData(
    val seatInDirection: Direction,
    var forwardImpulse: Float = 0.0f,
    var leftImpulse: Float = 0.0f,
    var upImpulse: Float = 0.0f,
    var sprintOn: Boolean = false
  ) {
    companion object {
      fun create(seatedControllingPlayer: SeatedControllingPlayer): ControlData {
        return ControlData(
          seatedControllingPlayer.seatInDirection,
          seatedControllingPlayer.forwardImpulse,
          seatedControllingPlayer.leftImpulse,
          seatedControllingPlayer.upImpulse,
          seatedControllingPlayer.sprintOn
        )
      }
    }
  }
  // --- end SubClasses
}