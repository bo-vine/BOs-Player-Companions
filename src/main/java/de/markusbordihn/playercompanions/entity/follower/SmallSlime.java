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

package de.markusbordihn.playercompanions.entity.follower;

import java.util.Map;

import javax.annotation.Nullable;
import com.google.common.collect.Maps;

import net.minecraft.Util;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import de.markusbordihn.playercompanions.Constants;

public class SmallSlime extends FollowerEntity {

  // General Information
  public static final String ID = "small_slime";
  public static final String NAME = "Small Slime";
  public static final MobCategory CATEGORY = MobCategory.CREATURE;

  // Entity Data
  public static final Map<DyeColor, ResourceLocation> TEXTURE_BY_COLOR =
      Util.make(Maps.newHashMap(), hashMap -> {
        hashMap.put(DyeColor.WHITE, new ResourceLocation(Constants.MOD_ID,
            "textures/entity/small_slime/small_slime_white.png"));
        hashMap.put(DyeColor.ORANGE, new ResourceLocation(Constants.MOD_ID,
            "textures/entity/small_slime/small_slime_orange.png"));
        hashMap.put(DyeColor.MAGENTA, new ResourceLocation(Constants.MOD_ID,
            "textures/entity/small_slime/small_slime_magenta.png"));
        hashMap.put(DyeColor.LIGHT_BLUE, new ResourceLocation(Constants.MOD_ID,
            "textures/entity/small_slime/small_slime_light_blue.png"));
        hashMap.put(DyeColor.YELLOW, new ResourceLocation(Constants.MOD_ID,
            "textures/entity/small_slime/small_slime_yellow.png"));
        hashMap.put(DyeColor.LIME, new ResourceLocation(Constants.MOD_ID,
            "textures/entity/small_slime/small_slime_lime.png"));
        hashMap.put(DyeColor.PINK, new ResourceLocation(Constants.MOD_ID,
            "textures/entity/small_slime/small_slime_pink.png"));
        hashMap.put(DyeColor.GRAY, new ResourceLocation(Constants.MOD_ID,
            "textures/entity/small_slime/small_slime_gray.png"));
        hashMap.put(DyeColor.LIGHT_GRAY, new ResourceLocation(Constants.MOD_ID,
            "textures/entity/small_slime/small_slime_light_gray.png"));
        hashMap.put(DyeColor.CYAN, new ResourceLocation(Constants.MOD_ID,
            "textures/entity/small_slime/small_slime_cyan.png"));
        hashMap.put(DyeColor.PURPLE, new ResourceLocation(Constants.MOD_ID,
            "textures/entity/small_slime/small_slime_purple.png"));
        hashMap.put(DyeColor.BLUE, new ResourceLocation(Constants.MOD_ID,
            "textures/entity/small_slime/small_slime_blue.png"));
        hashMap.put(DyeColor.BROWN, new ResourceLocation(Constants.MOD_ID,
            "textures/entity/small_slime/small_slime_brown.png"));
        hashMap.put(DyeColor.GREEN, new ResourceLocation(Constants.MOD_ID,
            "textures/entity/small_slime/small_slime_green.png"));
        hashMap.put(DyeColor.RED, new ResourceLocation(Constants.MOD_ID,
            "textures/entity/small_slime/small_slime_red.png"));
        hashMap.put(DyeColor.BLACK, new ResourceLocation(Constants.MOD_ID,
            "textures/entity/small_slime/small_slime_black.png"));
      });

  public SmallSlime(EntityType<? extends SmallSlime> entityType, Level level) {
    super(entityType, level);
    this.keepOnJumping(true);
  }

  public ResourceLocation getResourceLocation() {
    return TEXTURE_BY_COLOR.getOrDefault(this.getColor(), TEXTURE_BY_COLOR.get(DyeColor.GREEN));
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.3F)
        .add(Attributes.MAX_HEALTH, 16.0D).add(Attributes.ATTACK_DAMAGE, 2.0D);
  }

  @Override
  @Nullable
  public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor,
      DifficultyInstance difficulty, MobSpawnType mobSpawnType,
      @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
    spawnGroupData = super.finalizeSpawn(serverLevelAccessor, difficulty, mobSpawnType,
        spawnGroupData, compoundTag);
    // Use random different color for spawn
    if (this.random.nextInt(3) == 0) {
      // Select one of the DyeColors from 0 .. 15.
      DyeColor spawnColor = DyeColor.byId(this.random.nextInt(16));
      setColor(spawnColor);
    }
    return spawnGroupData;
  }

  @Override
  public Item getTameItem() {
    return Items.SLIME_BALL;
  }

  @Override
  public InteractionResult mobInteract(Player player, InteractionHand hand) {
    ItemStack itemStack = player.getItemInHand(hand);
    Item item = itemStack.getItem();
    // Only owner specific options.
    if (this.isTame() && this.isOwnedBy(player)) {
      // Change color of the slime.
      if (item instanceof DyeItem dyeItem) {
        DyeColor dyeColor = dyeItem.getDyeColor();
        if (dyeColor != this.getColor()) {
          this.setColor(dyeColor);
          if (!player.getAbilities().instabuild) {
            itemStack.shrink(1);
          }
          return InteractionResult.SUCCESS;
        }
      }
    }
    return super.mobInteract(player, hand);
  }

  @Override
  protected float getSoundVolume() {
    return 0.4F;
  }

  @Override
  protected int getJumpDelay() {
    return this.random.nextInt(80) + 20;
  }

  @Override
  protected int getJumpMoveDelay() {
    return 5;
  }

  @Override
  protected ParticleOptions getParticleType() {
    return ParticleTypes.ITEM_SLIME;
  }

  @Override
  public Vec3 getLeashOffset() {
    return new Vec3(0.0D, 1.7F * this.getEyeHeight(), this.getBbWidth() * 0.4F);
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.SLIME_SQUISH_SMALL;
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundEvents.SLIME_HURT_SMALL;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.SLIME_DEATH_SMALL;
  }

}
