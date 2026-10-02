package com.nxiiio.felox.entity;

import com.nxiiio.felox.entity.animation.FeloxAnimations;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

/** Grounded, passive animation prototype. Flight and decision-making arrive in later stages. */
public final class FeloxEntity extends PathfinderMob implements GeoEntity {
    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);

    public FeloxEntity(EntityType<? extends FeloxEntity> type, Level level) {
        super(type, level);
        // Keep manually spawned development specimens across unload/reload and player distance.
        setPersistenceRequired();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FLYING_SPEED, 0.6D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D);
    }

    @Override
    protected void registerGoals() {
        // Deliberately stationary for idle inspection, with no targeting or retaliation goals.
        // Do not disable the entity's AI globally: future movement controllers need normal ticks.
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        FeloxAnimations.register(this, controllers);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return animationCache;
    }
}
