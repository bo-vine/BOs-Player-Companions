/**
 * Copyright 2021 Markus Bordihn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package de.markusbordihn.playercompanions.client.model;

import com.google.common.collect.Iterables;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.TamableAnimal;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import de.markusbordihn.playercompanions.Constants;

@OnlyIn(Dist.CLIENT)
public class FairyModel<T extends TamableAnimal> extends HumanoidModel<T> {

  private final ModelPart leftWing;
  private final ModelPart rightWing;

  public FairyModel(ModelPart modelPart) {
    super(modelPart);
    this.leftLeg.visible = false;
    this.hat.visible = false;
    this.leftWing = modelPart.getChild("left_wing");
    this.rightWing = modelPart.getChild("right_wing");
  }

  public static LayerDefinition createBodyLayer() {
    float offsetY = 16.0F;
    MeshDefinition meshDefinition = HumanoidModel.createMesh(CubeDeformation.NONE, offsetY);
    PartDefinition partDefinition = meshDefinition.getRoot();

    // Head
    partDefinition.addOrReplaceChild("head",
        CubeListBuilder.create().texOffs(32, 25)
            .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(0, 25)
            .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)),
        PartPose.offset(0.0F, 0.0F, 0.0F));

    // Body
    partDefinition.addOrReplaceChild("body",
        CubeListBuilder.create().texOffs(24, 41)
            .addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(0, 41)
            .addBox(-4.0F, 0.0F, -2.0F, 8.0F, 15.0F, 4.0F, new CubeDeformation(0.5F)),
        PartPose.offset(0.0F, 0.0F, 0.0F));

    // Smaller arms
    partDefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(56, 14)
        .addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(0, 60)
        .addBox(-2.0F, -2.0F, -2.0F, 3.0F, 8.0F, 4.0F, new CubeDeformation(0.5F)),
        PartPose.offset(-5.0F, 2.0F, 0.0F));
    partDefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(56, 14).mirror()
        .addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
        .texOffs(20, 57).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 8.0F, 4.0F, new CubeDeformation(0.5F)),
        PartPose.offset(5.0F, 2.0F, 0.0F));

    // Combined legs like Vex
    partDefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(44, 55)
        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(48, 41)
        .addBox(-1.0F, -1.0F, -2.0F, 6.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(42, 0)
        .addBox(-1.0F, -1.0F, -2.0F, 6.0F, 10.0F, 4.0F, new CubeDeformation(0.5F)),
        PartPose.offset(-1.9F, 12.0F, 0.0F));

    // Adding Wings
    partDefinition.addOrReplaceChild("left_wing",
        CubeListBuilder.create().texOffs(0, 0).mirror()
            .addBox(0.0F, -8.0F, 0.0F, 20.0F, 24.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false),
        PartPose.offset(0.0F, 0.0F, 0.0F));
    partDefinition.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(0, 0)
        .addBox(-20.0F, -8.0F, 0.0F, 20.0F, 24.0F, 1.0F, new CubeDeformation(0.0F)),
        PartPose.offset(0.0F, 0.0F, 0.0F));

    return LayerDefinition.create(meshDefinition, 128, 128);
  }

  @Override
  protected Iterable<ModelPart> bodyParts() {
    return Iterables.concat(super.bodyParts(), java.util.List.of(this.leftWing, this.rightWing));
  }

  @Override
  public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks,
      float netHeadYaw, float headPitch) {
    // Don't animate death entities
    if (entity.isDeadOrDying()) {
      return;
    }

    // General animations
    super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

    // Wing animations
    this.rightLeg.xRot += ((float) Math.PI / 5F);
    this.rightWing.z = 2.0F;
    this.leftWing.z = 2.0F;
    this.rightWing.y = 1.0F;
    this.leftWing.y = 1.0F;
    this.rightWing.yRot = Constants.MATH_27DEG_TO_RAD
        + Mth.cos(ageInTicks * 45.836624F * ((float) Math.PI / 180F)) * (float) Math.PI * 0.05F;
    this.leftWing.yRot = -this.rightWing.yRot;
    this.leftWing.zRot = Constants.MATH_27DEG_TO_RAD_INVERTED;
    this.leftWing.xRot = Constants.MATH_27DEG_TO_RAD;
    this.rightWing.xRot = Constants.MATH_27DEG_TO_RAD;
    this.rightWing.zRot = Constants.MATH_27DEG_TO_RAD;
  }
}
