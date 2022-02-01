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

package de.markusbordihn.playercompanions.client.renderer;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import de.markusbordihn.playercompanions.client.model.SmallSlimeModel;
import de.markusbordihn.playercompanions.client.renderer.layers.SmallSlimeOuterLayer;
import de.markusbordihn.playercompanions.client.renderer.layers.SmallSlimeStatusLayer;
import de.markusbordihn.playercompanions.entity.follower.SmallSlime;

@OnlyIn(Dist.CLIENT)
public class SmallSlimeRenderer extends MobRenderer<SmallSlime, SmallSlimeModel<SmallSlime>> {

  public SmallSlimeRenderer(EntityRendererProvider.Context context) {
    super(context, new SmallSlimeModel<>(context.bakeLayer(ClientRenderer.SMALL_SLIME)), 0.26F);
    this.addLayer(new SmallSlimeOuterLayer<>(this, context.getModelSet()));
    this.addLayer(new SmallSlimeStatusLayer(this));
  }

  @Override
  public void render(SmallSlime entity, float p_115977_, float p_115978_, PoseStack poseStack,
      MultiBufferSource buffer, int p_115981_) {
    this.shadowRadius = 0.4F;
    super.render(entity, p_115977_, p_115978_, poseStack, buffer, p_115981_);
  }

  @Override
  public Vec3 getRenderOffset(SmallSlime entity, float offset) {
    return super.getRenderOffset(entity, offset);//.add(0, -0.6, 0);
  }

  @Nullable
  @Override
  public ResourceLocation getTextureLocation(SmallSlime entity) {
    return entity.getResourceLocation();
  }
}
