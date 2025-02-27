/**
 * Copyright 2022 Markus Bordihn
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

package de.markusbordihn.playercompanions.entity.companions;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import de.markusbordihn.playercompanions.Constants;
import de.markusbordihn.playercompanions.entity.PlayerCompanionEntity;
import de.markusbordihn.playercompanions.entity.PlayerCompanionVariant;
import de.markusbordihn.playercompanions.entity.ai.goal.AvoidCreeperGoal;
import de.markusbordihn.playercompanions.entity.ai.goal.FleeGoal;
import de.markusbordihn.playercompanions.entity.ai.goal.MoveToPositionGoal;
import de.markusbordihn.playercompanions.entity.type.collector.CollectorEntityFloating;
import de.markusbordihn.playercompanions.item.ModItems;

public class Snail extends CollectorEntityFloating {

  protected static final Logger log = LogManager.getLogger(Constants.LOG_NAME);

  // General Information
  public static final String ID = "snail";
  public static final String NAME = "Snail";
  public static final Ingredient FOOD_ITEMS = Ingredient.of(Items.SEAGRASS);

  // Variants
  public static final List<PlayerCompanionVariant> VARIANTS =
      List.of(PlayerCompanionVariant.DEFAULT, PlayerCompanionVariant.BROWN);

  // Companion Item by variant
  private static final Map<PlayerCompanionVariant, Item> COMPANION_ITEM_BY_VARIANT =
      Util.make(new EnumMap<>(PlayerCompanionVariant.class), hashMap -> {
        hashMap.put(PlayerCompanionVariant.DEFAULT, ModItems.SNAIL_DEFAULT.get());
        hashMap.put(PlayerCompanionVariant.BROWN, ModItems.SNAIL_BROWN.get());
      });

  public Snail(EntityType<? extends PlayerCompanionEntity> entityType, Level level) {
    super(entityType, level, COMPANION_ITEM_BY_VARIANT);
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.3F)
        .add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.ATTACK_DAMAGE, 0.5D);
  }

  @Override
  public boolean canBreatheUnderwater() {
    return true;
  }

  @Override
  public boolean isPushedByFluid() {
    return false;
  }

  @Override
  protected void registerGoals() {
    super.registerGoals();

    this.goalSelector.addGoal(1, new FleeGoal(this, 1.0D));
    this.goalSelector.addGoal(1, new PanicGoal(this, 1.0D));
    this.goalSelector.addGoal(1, new MoveToPositionGoal(this, 1.0D, 0.5F));
    this.goalSelector.addGoal(2, new AvoidCreeperGoal(this));
    this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
    this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
    this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0D, 6.0F, 2.0F, false));
    this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
    this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
  }

  @Override
  public Item getTameItem() {
    return ModItems.TAME_SEAGRASS.get();
  }

  @Override
  public Ingredient getFoodItems() {
    return FOOD_ITEMS;
  }

  @Override
  public PlayerCompanionVariant getRandomVariant() {
    if (VARIANTS.size() > 1 && this.random.nextInt(2) == 0) {
      return VARIANTS.get(this.random.nextInt(VARIANTS.size()));
    }
    return PlayerCompanionVariant.DEFAULT;
  }

  @Override
  public SoundEvent getPetSound() {
    return SoundEvents.CAT_PURR;
  }

  @Override
  public Vec3 getLeashOffset() {
    return new Vec3(0.0D, 0.1F * this.getEyeHeight(), this.getBbWidth() * 0.4F);
  }

  @Override
  public float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
    return 0.60F;
  }

  @Override
  public int getEntityGuiScaling() {
    return 50;
  }

  @Override
  public int getEntityGuiTop() {
    return 14;
  }

}
