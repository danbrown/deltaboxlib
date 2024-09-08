package com.dannbrown.deltaboxlib.registry.client.armorRenderer

import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.util.Mth
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.decoration.ArmorStand

abstract class AbstractArmorModel<T : LivingEntity>(root: ModelPart) : HumanoidModel<T>(root), ArmorModelSupplier {
  /**
   * Override this function to animate the model, instead of overriding [AbstractArmorModel.setupAnim].
   */
  protected abstract fun setupArmorPartAnim(entity: LivingEntity, ageInTicks: Float)
  override fun setupAnim(entity: T, limbSwing: Float, limbSwingAmount: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float) {
    // Fix wrong head rotation on ArmorStands
    if (entity is ArmorStand) {
      val f = Math.PI.toFloat() / 180f
      head.xRot = f * entity.headPose.x
      head.yRot = f * entity.headPose.y
      head.zRot = f * entity.headPose.z
      body.xRot = f * entity.bodyPose.x
      body.yRot = f * entity.bodyPose.y
      body.zRot = f * entity.bodyPose.z
      leftArm.xRot = f * entity.leftArmPose.x
      leftArm.yRot = f * entity.leftArmPose.y
      leftArm.zRot = f * entity.leftArmPose.z
      rightArm.xRot = f * entity.rightArmPose.x
      rightArm.yRot = f * entity.rightArmPose.y
      rightArm.zRot = f * entity.rightArmPose.z
      leftLeg.xRot = f * entity.leftLegPose.x
      leftLeg.yRot = f * entity.leftLegPose.y
      leftLeg.zRot = f * entity.leftLegPose.z
      rightLeg.xRot = f * entity.rightLegPose.x
      rightLeg.yRot = f * entity.rightLegPose.y
      rightLeg.zRot = f * entity.rightLegPose.z
    }
    else {
      this.setupArmorPartAnim(entity, ageInTicks)
    }
  }

  companion object {
    /**
     * This function must be called before adding the parts in the other models.
     *
     * @return A minimal mesh with all the part of the player model and their appropriate rotation positions.
     */
    fun templateLayerDefinition(scale: Float): MeshDefinition {
      val deformation = CubeDeformation(0f)
      val mesh = MeshDefinition()
      val root = mesh.root
      root.addOrReplaceChild("head",
        CubeListBuilder.create()
          .texOffs(0, 0)
          .addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, CubeDeformation(0.6f)),
        PartPose.offset(0.0f, 0.0f + scale, 0.0f))
      root.addOrReplaceChild("hat",
        CubeListBuilder.create()
          .texOffs(32, 0)
          .addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, CubeDeformation(1.0f)),
        PartPose.offset(0.0f, 0.0f + scale, 0.0f))
      root.addOrReplaceChild("body",
        CubeListBuilder.create()
          .texOffs(16, 16)
          .addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, CubeDeformation(0.38f))
          .texOffs(16, 31)
          .addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, CubeDeformation(0.7f)),
        PartPose.offset(0.0f, 0.0f + scale, 0.0f))
      root.addOrReplaceChild("right_arm",
        CubeListBuilder.create()
          .texOffs(40, 16)
          .addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.35f))
          .texOffs(40, 32)
          .addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.8f)),
        PartPose.offset(-5.0f, 2.0f + scale, 0.0f))
      root.addOrReplaceChild("left_arm",
        CubeListBuilder.create()
          .texOffs(32, 48)
          .addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.35f))
          .texOffs(48, 48)
          .addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.8f)),
        PartPose.offset(5.0f, 2.0f + scale, 0.0f))
      root.addOrReplaceChild("right_leg",
        CubeListBuilder.create()
          .texOffs(0, 16)
          .addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.4f)),
        PartPose.offset(-1.9f, 12.0f + scale, 0.0f))
      root.addOrReplaceChild("left_leg",
        CubeListBuilder.create()
          .texOffs(0, 16)
          .mirror()
          .addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.4f)),
        PartPose.offset(1.9f, 12.0f + scale, 0.0f))
      return mesh
    }

    fun sinPI(f: Float): Float {
      return Mth.sin(f * Math.PI.toFloat())
    }

    fun cosPI(f: Float): Float {
      return Mth.cos(f * Math.PI.toFloat())
    }
  }
}