package com.nxiiio.felox.entity.animation;

import com.nxiiio.felox.entity.FeloxEntity;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

/** GeckoLib controller definitions are common-side; this class must not import client classes. */
public final class FeloxAnimations {
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.felox.idle");

    private FeloxAnimations() {}

    public static void register(FeloxEntity entity, AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(entity, "locomotion", 5,
                state -> state.setAndContinue(IDLE)));
    }
}
